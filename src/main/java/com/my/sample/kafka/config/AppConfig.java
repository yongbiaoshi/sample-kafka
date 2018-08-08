package com.my.sample.kafka.config;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ServletComponentScan(basePackages = {"com.my.sample.kafka.config.filter"})
@EnableSwagger2Doc
public class AppConfig {

    /**
     * 应用启动时，如果没有该topic会自动创建，如果有，则忽略该配置
     * @return kafka topic
     */
    @Bean
    public NewTopic addNewTopic() {
        return new NewTopic("t-ag-1", 4, Integer.valueOf(2).shortValue());
    }
}
