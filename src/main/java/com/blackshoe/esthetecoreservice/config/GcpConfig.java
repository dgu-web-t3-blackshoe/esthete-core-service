package com.blackshoe.esthetecoreservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Configuration
public class GcpConfig {

    @Value("${cloud.gcp.credential-download-link}")
    private String CREDENTIAL_DOWNLOAD_LINK;

    @PostConstruct
    public void init() {

        try {
            String path = "src/main/resources/esthete-gcp.json";

            File file = new File(path);

            if (!file.exists()) {
                log.info("GCP credential file download start");
                log.info("Credential download link: " + CREDENTIAL_DOWNLOAD_LINK);
                URL url = new URL(CREDENTIAL_DOWNLOAD_LINK);
                Files.copy(url.openStream(), Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
                log.info("GCP credential file download complete");
            }
        } catch (IOException e) {
            log.error("GCP credential file download failed");
        }
    }
}
