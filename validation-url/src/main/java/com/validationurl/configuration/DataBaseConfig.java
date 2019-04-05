package com.validationurl.configuration;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataBaseConfig {

	private static final Logger logger = LoggerFactory.getLogger(DataBaseConfig.class);

	private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

	private static final int WAIT_DELAY = 80000;

	@Value("${plain.datasource.url}")
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {

		sleep();

		logger.info("Criou BasicDataSource");

		BasicDataSource dataSource = new BasicDataSource();

		dataSource.setDriverClassName(JDBC_DRIVER);
		dataSource.setUrl(this.url);

		return dataSource;
	}

	private void sleep() {
		try {
			Thread.sleep(WAIT_DELAY);
		} catch (InterruptedException e) {
			logger.error("Exception while sleeping", e);
		}
	}

}
