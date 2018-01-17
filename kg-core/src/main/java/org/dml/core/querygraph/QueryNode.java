package org.dml.core.querygraph;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 查询图节点类
 * 
 * @author HuangBo
 *
 */
public class QueryNode implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 查询节点编号，同一查询图中不能含有相同的节点编号
	 */
	private int queryId;

	/**
	 * 查询图节点的标识（在数据图的编号）约束
	 */
	private String nodeId;

	/**
	 * 查询图节点的标签约束
	 */
	private List<String> labels;

	/**
	 * 查询图节点的属性约束
	 */
	private Map<String, String> attributes;

	public QueryNode(int queryId) {
		this.queryId = queryId;
	}

	public QueryNode(int queryId, List<String> labels) {
		this.queryId = queryId;
		this.labels = labels;
	}

	public int getQueryId() {
		return queryId;
	}

	public void setQueryId(int queryId) {
		this.queryId = queryId;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

}
