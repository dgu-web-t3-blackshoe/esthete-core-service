package com.blackshoe.esthetecoreservice.service;

import com.amazonaws.util.IOUtils;
import com.blackshoe.esthetecoreservice.entity.Exhibition;
import com.blackshoe.esthetecoreservice.entity.Room;
import com.blackshoe.esthetecoreservice.entity.RoomPhoto;
import com.blackshoe.esthetecoreservice.exception.ExhibitionErrorResult;
import com.blackshoe.esthetecoreservice.exception.ExhibitionException;
import com.blackshoe.esthetecoreservice.repository.ExhibitionRepository;
import com.blackshoe.esthetecoreservice.repository.PhotoRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExhibitionPdfServiceImpl implements ExhibitionPdfService {

    private final ExhibitionRepository exhibitionRepository;
    private final PhotoRepository photoRepository;

    @Override
    public byte[] generatePdf(UUID exhibitionId) throws DocumentException, IOException {

        final Exhibition exhibition = exhibitionRepository.findByExhibitionId(exhibitionId)
                .orElseThrow(() -> new ExhibitionException(ExhibitionErrorResult.EXHIBITION_NOT_FOUND));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);

        PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();

        addExhibitionDetails(document, exhibition);

        addRoomDetails(document, exhibition.getRooms());

        document.close();

        return byteArrayOutputStream.toByteArray();
    }

    private void addExhibitionDetails(Document document, Exhibition exhibition) throws DocumentException, IOException {
        document.add(new Paragraph("Exhibition Title: " + exhibition.getTitle()));
        document.add(new Paragraph("Exhibition Description: " + exhibition.getDescription()));
        document.add(new Paragraph("Photographer: " + exhibition.getUser().getNickname()));

        UUID exhibitionThumbnailId = UUID.fromString(exhibition.getThumbnail());

        String exhibitionThumbnailUrl
                = photoRepository.findByPhotoId(exhibitionThumbnailId).get().getPhotoUrl().getCloudfrontUrl();

        addImageToPdf(document, exhibitionThumbnailUrl, "Exhibition Thumbnail");
    }

    private void addRoomDetails(Document document, List<Room> rooms) throws DocumentException, IOException {
        for (Room room : rooms) {
            document.add(new Paragraph("Room Title: " + room.getTitle()));
            document.add(new Paragraph("Room Description: " + room.getDescription()));

            UUID roomThumbnailId = UUID.fromString(room.getThumbnail());

            String roomThumbnailUrl
                    = photoRepository.findByPhotoId(roomThumbnailId).get().getPhotoUrl().getCloudfrontUrl();

            addImageToPdf(document, roomThumbnailUrl, "Room Thumbnail");
            addPhotoDetails(document, room.getRoomPhotos());
            document.newPage();
        }
    }

    private void addPhotoDetails(Document document, List<RoomPhoto> roomPhotos) throws DocumentException, IOException {
        for (RoomPhoto roomPhoto : roomPhotos) {
            document.add(new Paragraph("Photo Title: " + roomPhoto.getPhoto().getTitle()));
            document.add(new Paragraph("Photo Description: " + roomPhoto.getPhoto().getDescription()));
            addImageToPdf(document, roomPhoto.getPhoto().getPhotoUrl().getCloudfrontUrl(), "Photo Thumbnail");
        }
    }

    private void addImageToPdf(Document document, String cloudfrontUrl, String altText) throws DocumentException, IOException {
        URL url = new URL(cloudfrontUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (InputStream inputStream = connection.getInputStream()) {
            byte[] imageBytes = IOUtils.toByteArray(inputStream);

            float fixedHeight = 200f;

            Image image = Image.getInstance(imageBytes);
            float aspectRatio = image.getWidth() / image.getHeight();
            float fixedWidth = fixedHeight * aspectRatio;

            image.scaleAbsolute(fixedWidth, fixedHeight);

            document.add(image);
        }
    }
}
