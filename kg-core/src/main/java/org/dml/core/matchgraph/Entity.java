package org.dml.core.matchgraph;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 数据图实体类
 * 
 * @author HuangBo
 *
 */
public class Entity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 实体唯一标识
	 */
	private String identifier;

	/**
	 * 实体标签集合
	 */
	private List<String> labels;

	/**
	 * 实体属性集合
	 */
	private Map<String, String> attributes;

	/**
	 * 无参构造函数，jackson序列化与反序列必须需要
	 */
	public Entity() {
	}

	public Entity(String identifier) {
		this(identifier, null, null);
	}

	public Entity(String identifier, List<String> labels, Map<String, String> attibiutes) {
		this.identifier = identifier;
		this.labels = labels;
		this.attributes = attibiutes;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

}
