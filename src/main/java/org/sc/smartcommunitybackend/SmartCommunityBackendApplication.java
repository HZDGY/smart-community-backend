package org.sc.smartcommunitybackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.sc.smartcommunitybackend.mapper")
public class SmartCommunityBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartCommunityBackendApplication.class, args);
    }

}
