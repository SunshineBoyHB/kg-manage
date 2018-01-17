package org.dml.core.cluster;

import java.util.List;

/**
 * 集群详细信息
 * 
 * @author HuangBo
 *
 */
public class ClusterDetailInfo {

	/**
	 * 主机名（IP地址）
	 */
	private String host;

	/**
	 * 实体数
	 */
	private Long entityNum;

	/**
	 * 关系数
	 */
	private Long RelaNum;

	/**
	 * 实体标签集合
	 */
	private List<String> nodeLabels;

	/**
	 * 关系标签集合
	 */
	private List<String> relaTypes;

	/**
	 * 图谱属性名集合
	 */
	private List<String> propertyKeys;

	/**
	 * 图谱中的索引信息
	 */
	private List<DbIndexsInfo> indexs;

	/**
	 * 无参构造函数，jackson序列化与反序列必须需要
	 */
	public ClusterDetailInfo() {
	}

	public ClusterDetailInfo(String host, Long entityNum, Long relaNum, List<String> nodeLabels, List<String> relaTypes,
			List<String> propertyKeys, List<DbIndexsInfo> indexs) {
		this.host = host;
		this.entityNum = entityNum;
		RelaNum = relaNum;
		this.nodeLabels = nodeLabels;
		this.relaTypes = relaTypes;
		this.propertyKeys = propertyKeys;
		this.indexs = indexs;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Long getEntityNum() {
		return entityNum;
	}

	public void setEntityNum(Long entityNum) {
		this.entityNum = entityNum;
	}

	public Long getRelaNum() {
		return RelaNum;
	}

	public void setRelaNum(Long relaNum) {
		RelaNum = relaNum;
	}

	public List<String> getNodeLabels() {
		return nodeLabels;
	}

	public void setNodeLabels(List<String> nodeLabels) {
		this.nodeLabels = nodeLabels;
	}

	public List<String> getRelaTypes() {
		return relaTypes;
	}

	public void setRelaTypes(List<String> relaTypes) {
		this.relaTypes = relaTypes;
	}

	public List<String> getPropertyKeys() {
		return propertyKeys;
	}

	public void setPropertyKeys(List<String> propertyKeys) {
		this.propertyKeys = propertyKeys;
	}

	public List<DbIndexsInfo> getIndexs() {
		return indexs;
	}

	public void setIndexs(List<DbIndexsInfo> indexs) {
		this.indexs = indexs;
	}

}
