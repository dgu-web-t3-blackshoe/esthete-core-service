package com.blackshoe.esthetecoreservice.controller;

import com.blackshoe.esthetecoreservice.dto.ErrorDto;
import com.blackshoe.esthetecoreservice.exception.ExhibitionErrorResult;
import com.blackshoe.esthetecoreservice.exception.ExhibitionException;
import com.blackshoe.esthetecoreservice.service.ExhibitionPdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("core/exhibitions")
@RequiredArgsConstructor
public class ExhibitionPdfController {

    private final ExhibitionPdfService exhibitionPdfService;

    @GetMapping(value = "/{exhibitionId}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<ByteArrayResource> getExhibitionPdf(@PathVariable UUID exhibitionId) {


        final byte[] pdfBytes;
        try {
            pdfBytes = exhibitionPdfService.generatePdf(exhibitionId);
        } catch (Exception e) {
            throw new ExhibitionException(ExhibitionErrorResult.FAILED_TO_GENERATE_PDF);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("inline").filename(exhibitionId + ".pdf").build());

        return new ResponseEntity<>(new ByteArrayResource(pdfBytes), headers, HttpStatus.OK);
    }
}
