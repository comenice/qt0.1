package com.zxb.qt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ServletComponentScan( value = "com.zxb.qt.exploit.common" )
@MapperScan(  "com.zxb.qt.exploit"  )


@ComponentScan("com.zxb.qt.exploit")
public class QtApplication {

    public static void main(String[] args) {
        SpringApplication.run(QtApplication.class, args);
    }

}
