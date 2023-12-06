package com.blackshoe.esthetecoreservice.config;

import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.layout.font.FontProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Slf4j
@Configuration
public class ITextConfig {

    @Value("${itext.font-directory}")
    private String FONT_DIRECTORY;
    @Value("${itext.font-name}")
    private String FONT_NAME;
    @Value("${itext.font-download-url}")
    private String FONT_DOWNLOAD_URL;

    @PostConstruct
    public void init() {
        try {
            log.info("폰트 다운로드를 시작합니다.");

            Path fontFilePath = Path.of(FONT_DIRECTORY, FONT_NAME);
            log.info("폰트 파일 경로: {}", fontFilePath);

            URL fontUrl = new URL(FONT_DOWNLOAD_URL);
            log.info("폰트 다운로드 URL: {}", FONT_DOWNLOAD_URL);

            Files.copy(fontUrl.openStream(), fontFilePath, StandardCopyOption.REPLACE_EXISTING);

            log.info("폰트 다운로드에 성공하였습니다.");

        } catch (Exception e) {
            log.error("폰트 다운로드에 실패하였습니다.", e);
        }

        try {
            log.info("폰트를 등록 테스트합니다.");

            FontProvider fontProvider = new DefaultFontProvider();
            fontProvider.addFont(FONT_DIRECTORY + FONT_NAME);

            log.info("폰트 등록 테스트에 성공하였습니다.");

        } catch (Exception e) {
            log.error("폰트 등록 테스트에 실패하였습니다.", e);
        }
    }
}
