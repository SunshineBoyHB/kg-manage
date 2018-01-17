package org.dml.core.querygraph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dml.core.stwig.Direction;
import org.dml.core.stwig.LeafNode;
import org.dml.core.stwig.RootNode;
import org.dml.core.stwig.STwig;

/**
 * query graph class
 * 
 * @author HuangBo
 *
 */
public class QueryGraph implements Serializable {

	private static final long serialVersionUID = 7018563000058636097L;

	/**
	 * 查询图节点集合
	 */
	private List<QueryNode> nodes;

	/**
	 * 查询图边集合
	 */
	private List<QueryEdge> edges;

	public List<QueryNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<QueryNode> nodes) {
		this.nodes = nodes;
	}

	public List<QueryEdge> getEdges() {
		return edges;
	}

	public void setEdges(List<QueryEdge> edges) {
		this.edges = edges;
	}

	public void addNode(QueryNode node) {
		if (this.nodes == null)
			this.nodes = new ArrayList<QueryNode>();
		this.nodes.add(node);
	}

	public void addEdge(QueryEdge edge) {
		if (this.edges == null)
			this.edges = new ArrayList<QueryEdge>();
		this.edges.add(edge);
	}

	/**
	 * 根据查询节点编号获取查询节点
	 * 
	 * @param queryId
	 *            查询节点编号
	 * @return 满足条件的查询节点或null（不存在查询编号为queryId的查询节点）
	 */
	public QueryNode getNodeById(int queryId) {
		if (this.nodes == null)
			return null;
		for (QueryNode node : this.nodes) {
			if (node.getQueryId() == queryId)
				return node;
		}
		return null;
	}

	/**
	 * 根据查询边的两端的查询节点编号获取查询边
	 * 
	 * @param id1
	 *            查询边一端节点的编号（起点或终点）
	 * @param id2
	 *            查询边另一端节点的编号（起点或者终点）
	 * @return 满足条件的查询边或null（不存在两端查询编号为id1和id2的查询边）
	 */
	public QueryEdge getEdgeByIds(int id1, int id2) {
		if (this.edges == null)
			return null;
		for (QueryEdge edge : this.edges) {
			if ((edge.getSrcId() == id1) && (edge.getDstId() == id2))
				return edge;
			if ((edge.getDstId() == id1) && (edge.getSrcId() == id2))
				return edge;
		}
		return null;
	}

	/**
	 * 构造查询编号为rootId的STwig根节点
	 * 
	 * @param rootId
	 *            节点查询编号
	 * @return 满足条件的STwig根节点或null(不存在查询编号为rootId的查询节点)
	 */
	public RootNode getRootNode(int rootId) {
		QueryNode node = getNodeById(rootId);
		if (node == null)
			return null;
		return new RootNode(rootId, node.getLabels());
	}

	/**
	 * construct LeafNode according to the root id and the leaf id
	 * 构造根节点查询编号rootId，叶子节点查询编号为leafId的STwig叶子节点
	 * 
	 * @param rootId
	 *            STwig根节点查询编号
	 * @param leafId
	 *            STwig叶子节点查询编号
	 * @return 满足条件的STwig的叶子节点或null（不存在根节点查询编号rootId，叶子节点查询编号为leafId的STwig叶子节点）
	 */
	public LeafNode getLeafNode(int rootId, int leafId) {
		QueryEdge edge = getEdgeByIds(rootId, leafId);
		if(edge == null)
			return null;
		
		LeafNode leaf = new LeafNode(leafId);
		if (edge.getSrcId() == rootId)
			leaf.setDirection(Direction.STWIG_EDGE_FORWARD);
		else
			leaf.setDirection(Direction.STWIG_EDGE_REVERSE);
		leaf.setEdgeLabel(edge.getLabel());
		leaf.setNodeLabels(getNodeById(leafId).getLabels());
		
		return leaf;
	}

	/**
	 * 将查询图拆分多个子查询STwig，每个子查询在单个机器可以回答
	 * 
	 * @return 子查询STwig集合
	 */
	public List<STwig> decompGraph() {
		List<STwig> stwigs = new ArrayList<>();
		List<QueryEdge> chosenEdges = new ArrayList<>();
		Set<Integer> candidateIds = new LinkedHashSet<>();
		Map<Integer, Integer> node2Degree = new HashMap<>();

		// initialize node degree
		for (QueryNode node : this.nodes) {
			node2Degree.put(node.getQueryId(), 0);
		}

		// calculate the degree of all nodes according to edges
		for (QueryEdge edge : this.edges) {
			int srcDegree = node2Degree.get(edge.getSrcId()) + 1;
			int dstDegree = node2Degree.get(edge.getDstId()) + 1;
			node2Degree.put(edge.getSrcId(), srcDegree);
			node2Degree.put(edge.getDstId(), dstDegree);
		}

		// initialize candidate query node set
		if (candidateIds.isEmpty()) {
			int maxDegree = 0;
			int fisrtNodeId = -1;
			for (Map.Entry<Integer, Integer> entry : node2Degree.entrySet()) {
				int curDegree = entry.getValue();
				if (curDegree > maxDegree) {
					maxDegree = curDegree;
					fisrtNodeId = entry.getKey();
				}
			}
			candidateIds.add(fisrtNodeId);
		}

		while (chosenEdges.size() < this.edges.size()) { // have edges
			STwig stwig = new STwig();
			int rootId = -1;
			List<LeafNode> leaves = new ArrayList<>();

			// chosen node from candidate set and get correspond STwig
			int maxDegree = 0;
			for (int candidateId : candidateIds) {
				int curDegree = node2Degree.get(candidateId);
				if (curDegree > maxDegree) {
					maxDegree = curDegree;
					rootId = candidateId;
				}
			}

			// step1:delete chosen node from candidate set
			candidateIds.remove(new Integer(rootId));

			// step2:update candidate nodes set and node degree
			for (QueryEdge edge : this.edges) {
				// current edge have been chosen before
				if (chosenEdges.contains(edge)) {
					continue;
				}
				// if source node connect to chosen node
				if (edge.getSrcId() == rootId) {
					chosenEdges.add(edge);
					candidateIds.add(edge.getDstId());
					leaves.add(getLeafNode(rootId, edge.getDstId()));
					int updateDegree = node2Degree.get(edge.getDstId()) - 1;
					node2Degree.put(edge.getDstId(), updateDegree);
				}
				// if destination node connect to chosen node
				if (edge.getDstId() == rootId) {
					chosenEdges.add(edge);
					candidateIds.add(edge.getSrcId());
					leaves.add(getLeafNode(rootId, edge.getSrcId()));
					int updateDegree = node2Degree.get(edge.getSrcId()) - 1;
					node2Degree.put(edge.getSrcId(), updateDegree);
				}
			}

			// update and add STwig
			stwig.setRoot(getRootNode(rootId));
			stwig.setLeaves(leaves);
			stwigs.add(stwig);
		} // end while

		return stwigs;
	}

}
