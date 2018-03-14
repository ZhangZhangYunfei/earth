package com.yxedu.earth.user.config;

import com.alibaba.druid.pool.DruidAbstractDataSource;
import com.alibaba.druid.pool.DruidDataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@EnableJpaRepositories("com.yxedu.earth")
@EnableTransactionManagement
@Configuration
public class DateSourceConfig {

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
}
