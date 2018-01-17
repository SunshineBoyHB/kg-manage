package org.dml.core.stwig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询图子查询类
 * 
 * @author HuangBo
 *
 */
public class STwig implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * STwig的根节点
	 */
	private RootNode root;

	/**
	 * STwig的叶子节点集合
	 */
	private List<LeafNode> leaves;

	public STwig() {
	}

	public RootNode getRoot() {
		return root;
	}

	public void setRoot(RootNode root) {
		this.root = root;
	}

	public List<LeafNode> getLeaves() {
		return leaves;
	}

	public void setLeaves(List<LeafNode> leaves) {
		this.leaves = leaves;
	}

	public void addLeaf(LeafNode leaf) {
		if (this.leaves == null)
			this.leaves = new ArrayList<LeafNode>();
		this.leaves.add(leaf);
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("STwig[root=" + root.getQueryId() + ", leaves=");
		for (LeafNode leaf : leaves) {
			stringBuilder.append(leaf.getQueryId() + ",");
		}
		stringBuilder.append("]");
		return stringBuilder.toString();
	}
}
