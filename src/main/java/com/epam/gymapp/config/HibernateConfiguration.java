package com.epam.gymapp.config;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@RequiredArgsConstructor
public class HibernateConfiguration {

  private final DataSource dataSource;

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setGenerateDdl(true);

    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
    factory.setJpaVendorAdapter(vendorAdapter);
    factory.setPackagesToScan("com.epam.gymapp.model");
    factory.setDataSource(dataSource);
    return factory;
  }

  @Bean
  public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory);
  }

//  @Bean(name = "entityManagerFactory")
//  public LocalSessionFactoryBean sessionFactory() {
//    LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
//    sessionFactory.setDataSource(dataSource);
//    sessionFactory.setPackagesToScan("com.epam.gymapp.model");
//    return sessionFactory;
//  }
//
//  @Bean
//  public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
//    return new HibernateTransactionManager(sessionFactory);
//  }
}
