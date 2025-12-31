package org.sc.smartcommunitybackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Knife4j/OpenAPI 配置类
 */
@Configuration
public class SwaggerConfig {

    /**
     * 配置OpenAPI信息（Knife4j基于OpenAPI 3）
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("智能社区后端API文档")
                        .version("1.0.0")
                        .description("智能社区后端系统API接口文档，基于Spring Boot 3.x和Knife4j")
                        .contact(new Contact()
                                .name("开发团队")
                                .email("2870959239@qq.com")
                                .url("https://www.smartcommunity.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("本地开发环境"),
                        new Server()
                                .url("https://api.smartcommunity.com")
                                .description("生产环境")
                ));
    }
}
