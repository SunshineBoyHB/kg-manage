package org.dml.backend.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Spring MVC应用web配置，主要配置Controller，web资源读取等
 * 
 * @author HuangBo
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "org.dml.backend.web" })
public class BackendWebConfig extends WebMvcConfigurerAdapter {

}
