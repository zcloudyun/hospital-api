package com.example.hospital.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@EnableAsync
@ServletComponentScan
@ComponentScan("com.example.*")
@MapperScan("com.example.hospital.api.db.dao")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class HospitalApiApplication {
//调用异步线程任务类

  public static void main(String[] args) {
        SpringApplication.run(HospitalApiApplication.class, args);
    }
  @PostConstruct
    public void init(){


  }
}
