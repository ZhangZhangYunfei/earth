package com.yxedu.earth.examination.config;

import com.alibaba.druid.pool.DruidAbstractDataSource;
import com.alibaba.druid.pool.DruidDataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import redis.clients.jedis.JedisPoolConfig;

import javax.sql.DataSource;

@EnableJpaRepositories("com.yxedu.earth")
@EnableTransactionManagement
@Configuration
public class DataSourceConfig {

  /**
   * Creates dataSource.
   */
  @Bean
  public DataSource dataSource(@Value("${spring.datasource.driver-class-name}") String driver,
                               @Value("${spring.datasource.max-active}") int maxActive,
                               @Value("${EARTH_DATABASE_URL}") String url,
                               @Value("${EARTH_DATABASE_USERNAME}") String username,
                               @Value("${EARTH_DATABASE_PASSWORD}") String password) {
    DruidAbstractDataSource dateSource = new DruidDataSource();
    dateSource.setDriverClassName(driver);
    dateSource.setUrl(url);
    dateSource.setUsername(username);
    dateSource.setPassword(password);
    dateSource.setMaxActive(maxActive);

    return dateSource;
  }

  /**
   * Create JDBCTemplate with current datasource.
   */
  @Bean
  public JdbcTemplate jdbcTemplate(DataSource dataSource) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.setFetchSize(200);
    return jdbcTemplate;
  }

  /**
   * Creates the redis connection factory.
   */
  @Bean
  public RedisConnectionFactory redisConnectionFactory(
      @Value("${jedis.pool.max-idle}") int maxIdle,
      @Value("${jedis.pool.max-total}") int maxTotal,
      @Value("${jedis.pool.max-wait-millis}") long maxWaitMillis,
      @Value("${EARTH_REDIS_HOST}") String redisHost,
      @Value("${EARTH_REDIS_PORT}") int redisPort,
      @Value("${EARTH_REDIS_PASSWORD}") String redisPassword) {
    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    jedisPoolConfig.setMaxIdle(maxIdle);
    jedisPoolConfig.setMaxTotal(maxTotal);
    jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
    jedisPoolConfig.setTestOnBorrow(true);

    JedisConnectionFactory factory = new JedisConnectionFactory(jedisPoolConfig);
    factory.setHostName(redisHost);
    factory.setPort(redisPort);
    factory.setPassword(redisPassword);
    return factory;
  }
}
