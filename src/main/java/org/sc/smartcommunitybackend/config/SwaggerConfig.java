package org.sc.smartcommunitybackend.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j配置类
 */
@Configuration
@EnableKnife4j
public class SwaggerConfig {

    /**
     * 配置OpenAPI信息（Knife4j基于OpenAPI 3）
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("智能社区后端API文档")
                        .description("智能社区后端系统API接口文档\n\n" +
                                "### 认证说明\n" +
                                "1. 调用登录接口获取token\n" +
                                "2. 点击右上角'文档管理'→'全局参数设置'\n" +
                                "3. 添加参数名: **Authorization**\n" +
                                "4. 参数值: **Bearer {你的token}**\n" +
                                "5. 参数类型: **header**\n" +
                                "6. 点击保存,之后所有接口都会自动携带此参数")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("开发团队")
                                .email("2870959239@qq.com")
                                .url("https://www.smartcommunity.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .externalDocs(new ExternalDocumentation()
                        .description("项目文档")
                        .url("https://doc.xiaominfo.com/"))
                .components(new Components()
                        .addSecuritySchemes("Authorization", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                                .description("JWT认证token，格式: Bearer {token}")))
                .addSecurityItem(new SecurityRequirement().addList("Authorization"));
    }
}

