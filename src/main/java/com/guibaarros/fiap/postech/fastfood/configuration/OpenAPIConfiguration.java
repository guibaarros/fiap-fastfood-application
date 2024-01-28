package com.guibaarros.fiap.postech.fastfood.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "FastFood Manager APIs",
        version = "v1"))
public class OpenAPIConfiguration {
}
