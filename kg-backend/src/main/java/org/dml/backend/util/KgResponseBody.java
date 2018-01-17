package org.dml.backend.util;

import java.io.Serializable;

/**
 * 请求响应体泛型类
 * 
 * @author HuangBo
 *
 */
public class KgResponseBody<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 具体的状态码
	 */
	private Integer code;

	/**
	 * 具体的状态消息
	 */
	private String message;

	/**
	 * 响应体
	 */
	private T body;

	public KgResponseBody() {

	}

	public KgResponseBody(Integer code, String message, T body) {
		this.code = code;
		this.message = message;
		this.body = body;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}

}
