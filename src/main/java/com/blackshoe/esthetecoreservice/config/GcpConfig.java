package com.blackshoe.esthetecoreservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Configuration
public class GcpConfig {

    @Value("${cloud.gcp.credential-download-link}")
    private String CREDENTIAL_DOWNLOAD_LINK;

    @PostConstruct
    public void init() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                String fileName = "gcp.json";

                Path destinationPath = Path.of("src/main/resources", fileName);

                log.info("GCP credential file download start");
                log.info("Credential download link: " + CREDENTIAL_DOWNLOAD_LINK);

                try (InputStream inputStream = new URL(CREDENTIAL_DOWNLOAD_LINK).openStream()) {
                    Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                }

                log.info("GCP credential file download complete");
            } catch (IOException e) {
                log.error("GCP credential file download failed", e);
            }
        });

        executorService.shutdown();
    }
}
