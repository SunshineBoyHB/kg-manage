package org.dml.backend.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * RMI服务配置信息类
 * 
 * @author HuangBo
 *
 */
@Component
public class BackendConfigHolder {

	/**
	 * 布置RMI服务的机器个数
	 */
	@Value("${rmi.service.number}")
	private int rmiServiceNum;

	/**
	 * RMI服务的个数
	 */
	@Value("${rmi.service.name}")
	private String rmiServiceName;

	/**
	 * RMI服务的访问端口
	 */
	@Value("${rmi.service.port}")
	private int rmiServicePort;

	/**
	 * 提供RMI服务的主机IP地址集合
	 */
	@Value("#{'${rmi.services.hosts}'.split(';')}")
	private List<String> rmiServiceHosts;

	public BackendConfigHolder() {}

	public int getRmiServiceNum() {
		return rmiServiceNum;
	}

	public void setRmiServiceNum(int rmiServiceNum) {
		this.rmiServiceNum = rmiServiceNum;
	}

	public String getRmiServiceName() {
		return rmiServiceName;
	}

	public void setRmiServiceName(String rmiServiceName) {
		this.rmiServiceName = rmiServiceName;
	}

	public int getRmiServicePort() {
		return rmiServicePort;
	}

	public void setRmiServicePort(int rmiServicePort) {
		this.rmiServicePort = rmiServicePort;
	}

	public List<String> getRmiServiceHosts() {
		return rmiServiceHosts;
	}

	public void setRmiServiceHosts(List<String> rmiServiceHosts) {
		this.rmiServiceHosts = rmiServiceHosts;
	}

}
