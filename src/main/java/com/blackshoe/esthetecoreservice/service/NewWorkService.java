package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.NewWorkDto;

import java.util.List;
import java.util.UUID;

public interface NewWorkService {
    List<NewWorkDto.ReadNewWorkResponse> readNewWork(UUID userId);
    NewWorkDto.UpdateNewWorkResponse viewNewPhoto(NewWorkDto.UpdateViewOfPhotoRequest updateRequest);
    NewWorkDto.UpdateNewWorkResponse viewNewExhibition(NewWorkDto.UpdateViewOfExhibitionRequest updateRequest);
}
