package com.i3e3.mindlet.global.config.web;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(@Value("${springdoc.version}") String appVersion) {
        Info info = new Info()
                .title("Min'dLet API")
                .version(appVersion)
                .description("Mind'Let API 명세서입니다.")
                .termsOfService("https://smartbear.com/terms-of-use/")
                .contact(new Contact().name("Mind'Let").url("https://k6a106.p.ssafy.io/help").email("help@ssafy.com"))
                .license(new License().name("Apache License Version 2.0").url("http://www.apache.org/licenses/LICENSE-2.0"));

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
