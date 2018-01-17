package org.dml.core.matchgraph;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 查询图对应匹配图的中间匹配结果
 * 
 * @author HuangBo
 *
 */
public class InterMatchResult implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 查询图节点与数据图实体标识的映射
	 */
	private Map<Integer, String> node2Entity;

	public Map<Integer, String> getNode2Entity() {
		return node2Entity;
	}

	public void setNode2Entity(Map<Integer, String> node2Entity) {
		this.node2Entity = node2Entity;
	}

	/**
	 * 深拷贝函数
	 * 
	 * @return 深拷贝对象
	 */
	public InterMatchResult copy() {
		InterMatchResult copy = new InterMatchResult();

		Map<Integer, String> copyMapping = new HashMap<>();
		if (node2Entity != null)
			copyMapping.putAll(node2Entity);
		copy.setNode2Entity(copyMapping);

		return copy;
	}

	/**
	 * 添加映射关系
	 * 
	 * @param queryId
	 *            查询节点查询编号
	 * @param entityId
	 *            匹配实体唯一标识
	 */
	public void addMapping(int queryId, String entityId) {
		if (this.node2Entity == null)
			this.node2Entity = new HashMap<>();
		this.node2Entity.put(queryId, entityId);
	}

	/**
	 * 判断当前中间匹配结果和输入的中间匹配结果是否能够进行连接
	 * 
	 * @param another
	 *            输入的另一个中间匹配结果
	 * @return 能合并返回true，不能返回false
	 */
	public boolean isMerge(InterMatchResult another) {
		Map<Integer, String> anoNode2Entity = another.getNode2Entity();

		// if one of merge object's Mapping set is null, they cannot merge
		if (node2Entity == null || anoNode2Entity == null)
			return false;

		// check if contain same key and the value of same key is equal
		for (Map.Entry<Integer, String> entry : node2Entity.entrySet()) {
			Integer queryId = entry.getKey();
			if (anoNode2Entity.containsKey(queryId) && !entry.getValue().equals(anoNode2Entity.get(queryId))) {
				return false;
			}
		}

		// if do not contain the same key return true
		return true;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder("InterMatchResult:[");
		if (node2Entity != null) {
			for (Map.Entry<Integer, String> entry : node2Entity.entrySet()) {
				stringBuilder.append("<" + entry.getKey() + "," + entry.getValue() + ">;");
			}
		}
		stringBuilder.append("]");
		return stringBuilder.toString();
	}

}
