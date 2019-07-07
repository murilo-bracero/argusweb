package dev.projects.argus;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
public class DataConfiguration {

	@Bean
	public DataSource datasource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost/argusweb?serverTimezone=UTC");
		dataSource.setUsername("{database login}");
		dataSource.setPassword("{database password}");
		return dataSource;
	}
	
	@Bean
	public JpaVendorAdapter jpaAdapter() {
		HibernateJpaVendorAdapter hjva = new HibernateJpaVendorAdapter();
		hjva.setDatabase(Database.MYSQL);
		hjva.setShowSql(true);
		hjva.setGenerateDdl(true);
		hjva.setDatabasePlatform("org.hibernate.dialect.MySQL5InnoDBDialect");
		hjva.setPrepareConnection(true);
		return hjva;
	}
	
}
