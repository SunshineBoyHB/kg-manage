package org.dml.backend.config;

import org.dml.core.cluster.ClusterInfo;
import org.dml.core.metadata.EntityMetaInfo;
import org.dml.core.metadata.RelationshipMetaInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

/**
 * Spring MVC应用配置类，主要用于配置数据库访问，RPC服务，消息服务等
 * 
 * @author HuangBo
 *
 */
@Configuration
@ComponentScan(basePackages = { "org.dml.backend.util", "org.dml.backend.service", "org.dml.backend.repository" })
@PropertySource(value = "classpath:application.properties", encoding = "utf-8")
public class BackendRootConfig {

	/**
	 * 读取.properties文件的配置
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer peopertyHolder() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	// 配置redis连接工厂
	@Bean
	public JedisPoolConfig redisPoolConfig(@Value("${redis.max.active}") int maxActive,
			@Value("${redis.max.idle}") int maxIdle, @Value("${redis.max.wait}") long maxWait,
			@Value("${redis.test.on.borrow}") boolean testOnBorrow) {
		JedisPoolConfig redisPoolConfig = new JedisPoolConfig();
		redisPoolConfig.setMaxTotal(maxActive);
		redisPoolConfig.setMaxIdle(maxIdle);
		redisPoolConfig.setMaxWaitMillis(maxWait);
		redisPoolConfig.setTestOnBorrow(testOnBorrow);

		return redisPoolConfig;
	}

	// 配置redis工厂
	@Bean
	public JedisConnectionFactory redisConnFactory(@Value("${redis.hostname}") String hostname,
			@Value("${redis.port}") int port, @Value("${redis.database}") int database,
			@Value("${redis.password}") String password, JedisPoolConfig poolConfig) {
		JedisConnectionFactory connFactory = new JedisConnectionFactory();
		connFactory.setHostName(hostname);
		connFactory.setPort(port);
		connFactory.setDatabase(database);
		connFactory.setPassword(password);
		connFactory.setPoolConfig(poolConfig);

		return connFactory;
	}

	// 配置实体元数据redis模板
	@Bean
	public RedisTemplate<String, EntityMetaInfo> entityRedisTemplate(JedisConnectionFactory connFactory) {
		RedisTemplate<String, EntityMetaInfo> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(EntityMetaInfo.class));

		return redisTemplate;
	}

	// 配置关系元数据redis模板
	@Bean
	public RedisTemplate<String, RelationshipMetaInfo> relationshipRedisTemplate(JedisConnectionFactory connFactory) {
		RedisTemplate<String, RelationshipMetaInfo> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(RelationshipMetaInfo.class));

		return redisTemplate;
	}

	// 配置集群概览信息redis
	@Bean
	public RedisTemplate<String, ClusterInfo> clusterInfoTemplate(JedisConnectionFactory connectionFactory) {
		RedisTemplate<String, ClusterInfo> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ClusterInfo.class));

		return redisTemplate;
	}

}
