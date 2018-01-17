package org.dml.core.stwig;

import java.io.Serializable;
import java.util.List;

/**
 * 子查询STwig的根节点类
 * 
 * @author HuangBo
 *
 */
public class RootNode implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * STwig根节点的查询编号
	 */
	private int queryId;

	/**
	 * STwig根节点的标签约束
	 */
	private List<String> nodeLabels;

	public RootNode(int queryId) {
		this(queryId, null);
	}

	public RootNode(int queryId, List<String> nodeLabels) {
		this.queryId = queryId;
		this.nodeLabels = nodeLabels;
	}

	public int getQueryId() {
		return queryId;
	}

	public void setQueryId(int queryId) {
		this.queryId = queryId;
	}

	public List<String> getNodeLabels() {
		return nodeLabels;
	}

	public void setNodeLabels(List<String> nodeLabels) {
		this.nodeLabels = nodeLabels;
	}

}
