package org.dml.core.metadata;

import java.io.Serializable;
import java.util.List;

/**
 * 实体元数据类
 * 
 * @author HuangBo
 *
 */
public class EntityMetaInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 实体唯一标识
	 */
	private String entitiId;

	/**
	 * 将当前实体作为核心实体存储的机器
	 */
	private String coreHost;

	/**
	 * 将当前实体作为扩展实体存储的机器
	 */
	private List<String> extendHosts;

	/**
	 * 无参构造函数，jackson序列化与反序列必须需要
	 */
	public EntityMetaInfo() {
	}

	public EntityMetaInfo(String entitiId, String coreHost, List<String> extendHosts) {
		this.entitiId = entitiId;
		this.coreHost = coreHost;
		this.extendHosts = extendHosts;
	}

	public String getEntitiId() {
		return entitiId;
	}

	public void setEntitiId(String entitiId) {
		this.entitiId = entitiId;
	}

	public String getCoreHost() {
		return coreHost;
	}

	public void setCoreHost(String coreHost) {
		this.coreHost = coreHost;
	}

	public List<String> getExtendHosts() {
		return extendHosts;
	}

	public void setExtendHosts(List<String> extendHosts) {
		this.extendHosts = extendHosts;
	}

	@Override
	public String toString() {
		return "EntityMetaInfo[id=" + entitiId + ",coreHost=" + coreHost + ",extendHosts=" + extendHosts + "]";
	}

}
