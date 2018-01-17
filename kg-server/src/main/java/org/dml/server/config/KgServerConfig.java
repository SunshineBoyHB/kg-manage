package org.dml.server.config;

import org.dml.server.service.GraphDataService;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.remoting.rmi.RmiServiceExporter;

@Configuration
@ComponentScan({"org.dml.server.repository", "org.dml.server.service"})
@PropertySource(value="classpath:application.properties", encoding="utf-8")
public class KgServerConfig {
	
	// 读取properties文件的bean配置
	@Bean
	public static PropertySourcesPlaceholderConfigurer placeHolderConfigure() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	//neo4j图数据库驱动配置
	@Bean
	public Driver neo4jDriver(
			@Value("${neo4j.url}") String url,
			@Value("${neo4j.username}") String username,
			@Value("${neo4j.password}") String password) {
		return GraphDatabase.driver(url, AuthTokens.basic(username, password));
	}
	
	//RMI服务器端暴露图操作服务的bean配置
	@Bean
	public RmiServiceExporter rmiExporter(
			@Value("${rmi.service.name}") String serviceName,
			@Value("${rmi.registry.host}") String registryHost,
			@Value("${rmi.registry.port}") int registryPort,
			GraphDataService graphDataService) {
		RmiServiceExporter rmiExporter = new RmiServiceExporter();
		//set service instance
		rmiExporter.setService(graphDataService);
		//set service interface
		rmiExporter.setServiceInterface(GraphDataService.class);
		/*//set service registry host(when registry do not on the localhost)
		rmiExporter.setRegistryHost(registryHost);*/
		//set service registry port
		rmiExporter.setRegistryPort(registryPort);
		//set service name
		rmiExporter.setServiceName(serviceName);
		return rmiExporter;
	}

}
