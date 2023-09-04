package com.lunix.javagame.configs;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "paths")
public record PathsConfigs(Map<String, String> save) {
}
