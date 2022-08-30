package com.bstek.ureport.console.config;

import com.bstek.ureport.console.UReportServlet;
import com.bstek.ureport.definition.datasource.BuildinDatasource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
@Configuration
//不加项目能够启动但是会导致加载数据源报错或加载不了
@ImportResource("classpath:ureport-console-context.xml")
public class UreportDatasource implements BuildinDatasource {

    private static final String NAME = "oracle";
    private Logger log = LoggerFactory.getLogger(UreportDatasource.class);

    @Autowired
    private DataSource dataSource;

    /**
     * 配置ureport的servlet
     * @return
     */
    @Bean
    public ServletRegistrationBean buildUReportServlet() {
        //htreport
        return new ServletRegistrationBean(new UReportServlet(),"/ureport/*");
    }

    /**
     * ds1数据库配置
     */
    @Bean("ds1")
    @ConfigurationProperties(prefix = "spring.datasource.druid.ds1")
    public DataSource ds1Source() {
        return DataSourceBuilder.create().build();
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            log.error("Ureport 数据源 获取连接失败！");
            e.printStackTrace();
        }
        return null;
    }

}