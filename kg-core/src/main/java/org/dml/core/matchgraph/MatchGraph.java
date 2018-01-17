package org.dml.core.matchgraph;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询图对应的匹配图
 * 
 * @author HuangBo
 *
 */
public class MatchGraph {

	/**
	 * 匹配图的第一种表示（通过中间匹配连接得到的）的属性域
	 */
	private List<InterMatchResult> subResults;

	/**
	 * 匹配图的第二种表示（用于web展示）的属性域，匹配的实体集合
	 */
	private List<Entity> entities;

	/**
	 * 匹配图的第二种表示（用于web展示）的属性域，实体之间具有的关系
	 */
	private List<Relationship> relationships;

	public MatchGraph() {
		this.subResults = new ArrayList<>();
		this.entities = new ArrayList<>();
		this.relationships = new ArrayList<>();
	}

	public List<InterMatchResult> getSubResults() {
		return subResults;
	}

	public void setSubResults(List<InterMatchResult> subResults) {
		this.subResults = subResults;
	}

	/**
	 * 添加中间匹配结果
	 * 
	 * @param 匹配图的某个中间匹配结果
	 */
	public void addSubResult(InterMatchResult interMatchResult) {
		if (subResults == null)
			subResults = new ArrayList<>();
		subResults.add(interMatchResult);
	}

	/**
	 * 如果存在则删除匹配图中的某个中间匹配结果
	 * 
	 * @param 匹配图的某个中间匹配结果
	 */
	public void removeSubResult(InterMatchResult interMatchResult) {
		if (subResults != null && subResults.contains(interMatchResult))
			subResults.remove(interMatchResult);
	}

	/**
	 * 尝试去链接一个中间匹配图
	 * 
	 * @param interMatchResult
	 *            某个子查询的匹配结果
	 * @return 如果能够连接返回true（当前匹配图会更新），否则返回false
	 */
	public boolean tryMerge(InterMatchResult interMatchResult) {
		boolean isMerge = true;

		// check if can merge(for every previous result)
		for (InterMatchResult preInterResult : subResults) {
			if (!preInterResult.isMerge(interMatchResult)) {
				isMerge = false;
				break;
			}
		}

		// merge if can
		if (isMerge)
			addSubResult(interMatchResult);

		return isMerge;
	}

	/**
	 * 深拷贝
	 * 
	 * @return 深拷贝对象
	 */
	public MatchGraph copy() {
		MatchGraph copyMatchGraph = new MatchGraph();
		for (InterMatchResult subResult : subResults) {
			InterMatchResult copySubResult = subResult.copy();
			copyMatchGraph.addSubResult(copySubResult);
		}
		return copyMatchGraph;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder("MatchGraph(merge)[");
		for (int i = 0; i < subResults.size(); i++) {
			InterMatchResult subResult = subResults.get(i);
			stringBuilder.append(i + "th subResult=" + subResult);
		}

		return stringBuilder.toString();
	}

}
