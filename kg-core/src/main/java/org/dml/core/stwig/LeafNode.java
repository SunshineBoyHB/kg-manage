package org.dml.core.stwig;

import java.io.Serializable;
import java.util.List;

/**
 * 子查询STwig的叶子节点类
 * 
 * @author HuangBo
 *
 */
public class LeafNode implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * STwig叶子节点的查询编号
	 */
	private int queryId;

	/**
	 * STwig根节点到当前STwig叶子节点的方向
	 */
	private Direction direction;

	/**
	 * STwig根节点到当前STwig叶子节点的标签约束
	 */
	private String edgeLabel;

	/**
	 * STwig叶子节点标签约束
	 */
	private List<String> nodeLabels;

	public LeafNode(int queryId) {
		this(queryId, null, null, null);
	}

	public LeafNode(int queryId, Direction direction, String edgeLabel, List<String> nodeLabels) {
		this.queryId = queryId;
		this.direction = direction;
		this.edgeLabel = edgeLabel;
		this.nodeLabels = nodeLabels;
	}

	public int getQueryId() {
		return queryId;
	}

	public void setQueryId(int queryId) {
		this.queryId = queryId;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public String getEdgeLabel() {
		return edgeLabel;
	}

	public void setEdgeLabel(String edgeLabel) {
		this.edgeLabel = edgeLabel;
	}

	public List<String> getNodeLabels() {
		return nodeLabels;
	}

	public void setNodeLabels(List<String> nodeLabels) {
		this.nodeLabels = nodeLabels;
	}

}
