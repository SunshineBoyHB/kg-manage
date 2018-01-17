package org.dml.backend.util;

import java.util.HashMap;
import java.util.Map;

import org.dml.backend.util.BackendConfigHolder;
import org.dml.server.service.GraphDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

/**
 * RMI集成服务代理集合
 * 
 * @author HuangBo
 *
 */
@Component
public class IntegrateServiceProxy {

	/**
	 * 布置RMI服务的机器数
	 */
	private int serviceNum;
	
	/**
	 * 机器名（IP地址）--图谱操作服务映射
	 */
	private Map<String, GraphDataService> serviceProxys;

	/**
	 * 根据RMI服务配置类初始化RMI服务代理
	 * @param rmiConfig RMI服务配置类，自动注入
	 */
	@Autowired
	public IntegrateServiceProxy(BackendConfigHolder rmiConfig) {
		if (rmiConfig == null || rmiConfig.getRmiServiceNum() <= 0) {
			this.serviceNum = 0;
			this.serviceProxys = null;
			return;
		}
		this.serviceNum = rmiConfig.getRmiServiceNum();
		Map<String, GraphDataService> services = new HashMap<>();
		for (int i = 0; i < serviceNum; i++) {
			RmiProxyFactoryBean rmiProxy = new RmiProxyFactoryBean();
			String serviceHost = rmiConfig.getRmiServiceHosts().get(i);

			// construct rmi service url
			String rmiServiceUrl = new StringBuilder("rmi://").append(serviceHost).append(":")
					.append(rmiConfig.getRmiServicePort()).append("/").append(rmiConfig.getRmiServiceName()).toString();

			// set rmi service url
			rmiProxy.setServiceUrl(rmiServiceUrl);
			// sett rmi service interface class
			rmiProxy.setServiceInterface(GraphDataService.class);
			// set properties
			rmiProxy.afterPropertiesSet();

			// get proxy target object
			GraphDataService graphService = (GraphDataService) rmiProxy.getObject();

			// add service
			services.put(serviceHost, graphService);
		}
		this.serviceProxys = services;
	}

	public int getServiceNum() {
		return serviceNum;
	}

	public void setServiceNum(int serviceNum) {
		this.serviceNum = serviceNum;
	}

	public Map<String, GraphDataService> getServiceProxys() {
		return serviceProxys;
	}

	public void setServiceProxys(Map<String, GraphDataService> serviceProxys) {
		this.serviceProxys = serviceProxys;
	}

	/**
	 *根据机器IP地址获取Neo4j图数据图操作服务
	 * 
	 * @param host
	 *            机器IP地址
	 * @return 图谱操作服务
	 */
	public GraphDataService getGraphServiceByHost(String host) {
		if (this.serviceProxys == null || !this.serviceProxys.containsKey(host))
			return null;
		return this.serviceProxys.get(host);
	}

}
