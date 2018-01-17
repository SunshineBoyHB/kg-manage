package org.dml.core.matchgraph;

import java.io.Serializable;
import java.util.Map;

/**
 * 数据图关系类
 * 
 * @author HuangBo
 *
 */
public class Relationship implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 关系唯一标识
	 */
	private String identifier;

	/**
	 * 关系标签
	 */
	private String label;

	/**
	 * 关系属性集合
	 */
	private Map<String, String> attributes;

	/**
	 * 关系起始实体唯一标识
	 */
	private String start;

	/**
	 * 关系终止实体唯一标识
	 */
	private String end;

	/**
	 * 无参构造函数，jackson序列化与反序列必须需要
	 */
	public Relationship() {
	}

	public Relationship(String identifier, String label, Map<String, String> attributes, String start, String end) {
		this.identifier = identifier;
		this.label = label;
		this.attributes = attributes;
		this.start = start;
		this.end = end;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

}
