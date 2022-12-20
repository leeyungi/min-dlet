package com.i3e3.mindlet.domain.dandelion.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@ToString(of = {"message", "imageFile", "nation"})
public class PetalCreateSvcDto {
    private String message;

    private MultipartFile imageFile;

    private String nation;

    @Builder
    public PetalCreateSvcDto(String message, MultipartFile imageFile, String nation) {
        this.message = message;
        this.imageFile = imageFile;
        this.nation = nation;
    }
}
