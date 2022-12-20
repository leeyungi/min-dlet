package com.i3e3.mindlet.domain.dandelion.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString(of = {"blossomedDate", "description", "flowerSignNumber", "seq", "status"})
public class ResponseGardenInfoDto {
    private LocalDate blossomedDate;

    private String description;

    private Integer flowerSignNumber;

    private Long seq;

    private String status;

    @Builder
    public ResponseGardenInfoDto(LocalDate blossomedDate, String description, Integer flowerSignNumber, Long seq, String status) {
        this.blossomedDate = blossomedDate;
        this.description = description;
        this.flowerSignNumber = flowerSignNumber;
        this.seq = seq;
        this.status = status;
    }
}
