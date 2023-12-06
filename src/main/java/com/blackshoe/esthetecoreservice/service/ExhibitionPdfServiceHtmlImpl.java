package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.entity.Exhibition;
import com.blackshoe.esthetecoreservice.entity.Photo;
import com.blackshoe.esthetecoreservice.exception.ExhibitionErrorResult;
import com.blackshoe.esthetecoreservice.exception.ExhibitionException;
import com.blackshoe.esthetecoreservice.exception.PhotoErrorResult;
import com.blackshoe.esthetecoreservice.exception.PhotoException;
import com.blackshoe.esthetecoreservice.repository.ExhibitionRepository;
import com.blackshoe.esthetecoreservice.repository.PhotoRepository;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.text.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Primary
@Slf4j
@Service
@RequiredArgsConstructor
public class ExhibitionPdfServiceHtmlImpl implements ExhibitionPdfService {

    private final ExhibitionRepository exhibitionRepository;
    private final PhotoRepository photoRepository;
    private final TemplateEngine templateEngine;

    @Value("${itext.font-directory}")
    private String FONT_DIRECTORY;
    @Value("${itext.font-name}")
    private String FONT_NAME;

    @Override
    public byte[] generatePdf(UUID exhibitionId) throws DocumentException, IOException {

        final Exhibition exhibition = exhibitionRepository.findByExhibitionId(exhibitionId)
                .orElseThrow(() -> new ExhibitionException(ExhibitionErrorResult.EXHIBITION_NOT_FOUND));

        exhibition.getUser();
        exhibition.getRooms().forEach(room -> {
            room.getRoomPhotos().forEach(roomPhoto -> {
                roomPhoto.getPhoto().getPhotoUrl();
            });
        });

        final Photo photo = photoRepository.findByPhotoId(UUID.fromString(exhibition.getThumbnail()))
                .orElseThrow(() -> new PhotoException(PhotoErrorResult.PHOTO_NOT_FOUND));
        String exhibitionThumbnailUrl = photo.getPhotoUrl().getCloudfrontUrl();

        Context thymeleafContext = new Context();
        thymeleafContext.setVariable("exhibition", exhibition);
        thymeleafContext.setVariable("exhibitionThumbnailUrl", exhibitionThumbnailUrl);
        String htmlContent = templateEngine.process("exhibition-template", thymeleafContext);

        FontProvider fontProvider = new DefaultFontProvider(false, false, false);
        fontProvider.addFont(FONT_DIRECTORY + FONT_NAME);

        ConverterProperties properties = new ConverterProperties();
        properties.setFontProvider(fontProvider);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);

        PdfWriter pdfWriter = new PdfWriter(byteArrayOutputStream);

        document.open();
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        HtmlConverter.convertToPdf(inputStream, pdfWriter, properties);
        document.close();

        return byteArrayOutputStream.toByteArray();
    }
}
