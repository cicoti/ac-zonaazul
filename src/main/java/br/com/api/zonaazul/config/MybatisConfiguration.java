package br.com.api.candidato.config;


import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.camel.component.mybatis.MyBatisComponent;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import br.com.api.candidato.config.utils.DataSourcePropertiesConfig;

@Configuration
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class, MybatisAutoConfiguration.class})
public class MybatisConfiguration {
	
	Logger log = LoggerFactory.getLogger(MybatisConfiguration.class);


	@Bean(name="zonaAzulDB")
	public org.apache.camel.component.mybatis.MyBatisComponent myBatisPDA(@Qualifier("zonaAzulSessionFactory") SqlSessionFactory sqlSessionFactory){
		MyBatisComponent mybatis = new MyBatisComponent();
		mybatis.setSqlSessionFactory(sqlSessionFactory);
		return mybatis;
	}

	@Bean(name="zonaAzulSessionFactory")
	@Autowired
	public org.mybatis.spring.SqlSessionFactoryBean sessionFactory(@Qualifier("zonaAzulConnection") DataSource dataSource){
		org.mybatis.spring.SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
		TransactionFactory transactionFactory = new JdbcTransactionFactory();
		factory.setTransactionFactory(transactionFactory);
		factory.setDataSource(dataSource);
		Resource[] locations = {new ClassPathResource("/META-INF/mappers/autentica.mapper.xml"),
								new ClassPathResource("/META-INF/mappers/compra.mapper.xml"),
								new ClassPathResource("/META-INF/mappers/venda.mapper.xml")
		} ;
		factory.setMapperLocations(locations);
		return factory;
	}

	@Bean(name="zonaAzulConnection")
	@Autowired
	public DataSource getNuAcademicoDS(@Qualifier("zonaAzulDBConfig") DataSourcePropertiesConfig parametros) throws SQLException {

		PoolProperties p = getConnectionProperties(parametros);

		org.apache.tomcat.jdbc.pool.DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource();

		ds.setPoolProperties(p);

		return ds;
	}

	@Bean(name="zonaAzulDBConfig")
	@ConfigurationProperties(prefix="servico.zonaazul.datasource")
	public DataSourcePropertiesConfig getOlimpoConfig() {
		return new DataSourcePropertiesConfig();
	}

	private PoolProperties getConnectionProperties(DataSourcePropertiesConfig parametros) {
		PoolProperties p = new PoolProperties();
        p.setUrl(parametros.getUrl());
        p.setDriverClassName(parametros.getDriverClassName());
        p.setUsername(parametros.getUsername());
        p.setPassword(parametros.getPassword());
        p.setJmxEnabled(false);
        p.setTestWhileIdle(false);
        p.setTestOnBorrow(true);
        p.setValidationQuery("SELECT 1 from dual");
        p.setTestOnReturn(false);
        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(1);
        p.setInitialSize(1);
        p.setMaxWait(10000);
        p.setRemoveAbandonedTimeout(600);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(1);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
		return p;
	}

}
