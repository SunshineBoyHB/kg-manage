package org.dml.backend.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Spring MVC应用配置类，主要用于配置数据库访问，RPC服务，消息服务等
 * 
 * @author HuangBo
 *
 */
@Configuration
@ComponentScan(basePackages = { "org.dml.backend.util", "org.dml.backend.service" })
@PropertySource(value = "classpath:singleService.properties", encoding = "utf-8")
public class IntegrateServiceTestConfig {

	/**
	 * 读取.properties文件的配置
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer peopertyHolder() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
