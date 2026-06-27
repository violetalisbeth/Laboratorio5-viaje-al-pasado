package com.ncpas.laboratorio3.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class DotenvInitializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(
            ConfigurableApplicationContext applicationContext
    ) {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        Map<String, Object> envVariables = new HashMap<>();

        dotenv.entries().forEach(entry ->
                envVariables.put(entry.getKey(), entry.getValue())
        );

        applicationContext
                .getEnvironment()
                .getPropertySources()
                .addFirst(
                        new MapPropertySource(
                                "dotenvProperties",
                                envVariables
                        )
                );
    }
}