package org.dml.core.metadata;

import java.io.Serializable;
import java.util.List;

/**
 * 关系元数据类
 * 
 * @author HuangBo
 *
 */
public class RelationshipMetaInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 关系唯一标识
	 */
	private String relationshipId;

	/**
	 * 将当前关系作为核心关系存储的机器
	 */
	private String coreHost;

	/**
	 * 将当前关系作为扩展关系存储的机器
	 */
	private List<String> extendHosts;

	/**
	 * 无参构造函数，jackson序列化与反序列必须需要
	 */
	public RelationshipMetaInfo() {
	}

	public RelationshipMetaInfo(String relationshipId, String coreHost, List<String> extendHosts) {
		this.relationshipId = relationshipId;
		this.coreHost = coreHost;
		this.extendHosts = extendHosts;
	}

	public String getRelationshipId() {
		return relationshipId;
	}

	public void setRelationshipId(String relationshipId) {
		this.relationshipId = relationshipId;
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
		return "RelationshipMetaInfo[id=" + relationshipId + ", coreHost=" + coreHost + ", extendHosts=" + extendHosts
				+ "]";
	}

}
