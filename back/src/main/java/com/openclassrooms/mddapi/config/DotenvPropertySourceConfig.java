package com.openclassrooms.mddapi.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;

import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySource(value = "classpath:application.properties")
public class DotenvPropertySourceConfig {

    private final Environment environment;

    public DotenvPropertySourceConfig(Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    public void addDotenvProperties() {
        Dotenv dotenv = Dotenv.configure().load();
        Map<String, Object> dotenvProperties = new HashMap<>();

        dotenv.entries().forEach(entry -> dotenvProperties.put(entry.getKey(), entry.getValue()));

        MutablePropertySources propertySources = ((StandardEnvironment) environment).getPropertySources();
        propertySources.addFirst(new MapPropertySource("dotenvProperties", dotenvProperties));
    }
}
