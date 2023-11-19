package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.NewWorkDto;

import java.util.List;
import java.util.UUID;

public interface NewWorkService {
    List<NewWorkDto.ReadResponse> readNewWork(UUID userId);
    NewWorkDto.UpdateResponse viewNewPhoto(NewWorkDto.UpdateViewOfPhotoRequest updateRequest);
    NewWorkDto.UpdateResponse viewNewExhibition(NewWorkDto.UpdateViewOfExhibitionRequest updateRequest);
}
