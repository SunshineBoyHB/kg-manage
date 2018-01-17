package org.dml.core.cluster;

import java.io.Serializable;

/**
 * 集群概览信息
 * 
 * @author HuangBo
 *
 */
public class ClusterInfo implements Serializable {

	private static final long serialVersionUID = 1L;

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
	 * 无参构造函数，jackson序列化与反序列必须需要
	 */
	public ClusterInfo() {
	}

	public ClusterInfo(String host, Long entityNum, Long relaNum) {
		this.host = host;
		this.entityNum = entityNum;
		RelaNum = relaNum;
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

}
