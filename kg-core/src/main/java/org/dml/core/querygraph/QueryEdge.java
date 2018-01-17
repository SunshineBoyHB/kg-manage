package org.dml.core.querygraph;

import java.io.Serializable;
import java.util.Map;

/**
 * 查询图边类
 * 
 * @author HuangBo
 *
 */
public class QueryEdge implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 查询边编号，同一查询图中不能含有相同的查询边编号
	 */
	private int queryId;

	/**
	 * 查询边标识约束
	 */
	private String edgeId;

	/**
	 * 查询边起始节点查询编号
	 */
	private int srcId;

	/**
	 * 查询边终止节点查询编号
	 */
	private int dstId;

	/**
	 * 查询边标签约束
	 */
	private String label;

	/**
	 * 查询边属性约束
	 */
	private Map<String, String> attributes;

	public QueryEdge(int queryId) {
		this.queryId = queryId;
	}

	public QueryEdge(int queryId, int srcId, int dstId, String label) {
		this.queryId = queryId;
		this.srcId = srcId;
		this.dstId = dstId;
		this.label = label;
	}

	public int getQueryId() {
		return queryId;
	}

	public void setQueryId(int queryId) {
		this.queryId = queryId;
	}

	public String getEdgeId() {
		return edgeId;
	}

	public void setEdgeId(String edgeId) {
		this.edgeId = edgeId;
	}

	public int getSrcId() {
		return srcId;
	}

	public void setSrcId(int srcId) {
		this.srcId = srcId;
	}

	public int getDstId() {
		return dstId;
	}

	public void setDstId(int dstId) {
		this.dstId = dstId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

}
