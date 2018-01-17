package org.dml.core.cluster;

/**
 * 单机图谱存储索引信息
 * 
 * @author HuangBo
 *
 */
public class DbIndexsInfo {

	private String description;

	private String state;

	private String type;

	/**
	 * 无参构造函数，jackson序列化与反序列必须需要
	 */
	public DbIndexsInfo() {
	}

	public DbIndexsInfo(String description, String state, String type) {
		this.description = description;
		this.state = state;
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
