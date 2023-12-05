package com.blackshoe.esthetecoreservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Configuration
public class GcpConfig {

    @Value("${cloud.gcp.credentials.string}")
    private String GCP_CREDENTIALS;

    @PostConstruct
    public void init() {
        try {
            Path resourcesPath = Paths.get("src", "main", "resources");

            Path gcpJsonPath = resourcesPath.resolve("gcp.json");

            Files.write(gcpJsonPath, GCP_CREDENTIALS.getBytes());

            log.info("GCP credentials saved to: {}", gcpJsonPath);
        } catch (IOException e) {
            log.error("Error saving GCP credentials to file", e);
        }
    }
}

