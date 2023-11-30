package com.blackshoe.esthetecoreservice.service;

import com.itextpdf.text.DocumentException;

import java.io.IOException;
import java.util.UUID;

public interface ExhibitionPdfService {
    byte[] generatePdf(UUID exhibitionId) throws DocumentException, IOException;
}
