package com.i3e3.mindlet.domain.dandelion.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(of = {"leftSeedCount"})
public class SeedCountDto {

    private Integer leftSeedCount;

    @Builder
    public SeedCountDto(Integer leftSeedCount) {
        this.leftSeedCount = leftSeedCount;
    }
}
