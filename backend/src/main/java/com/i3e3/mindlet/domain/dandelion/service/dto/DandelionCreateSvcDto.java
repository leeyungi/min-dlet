package com.i3e3.mindlet.domain.dandelion.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@ToString(of = {"message", "blossomedDate", "imageFile"})
public class DandelionCreateSvcDto {
    private String message;

    private LocalDate blossomedDate;

    private MultipartFile imageFile;

    private String nation;

    @Builder
    public DandelionCreateSvcDto(String message, LocalDate blossomedDate, MultipartFile imageFile, String nation) {
        this.message = message;
        this.blossomedDate = blossomedDate;
        this.imageFile = imageFile;
        this.nation = nation;
    }

    public PetalCreateSvcDto toPetalCreateSvcDto() {
        return PetalCreateSvcDto.builder()
                .message(message)
                .imageFile(imageFile)
                .nation(nation)
                .build();
    }
}
