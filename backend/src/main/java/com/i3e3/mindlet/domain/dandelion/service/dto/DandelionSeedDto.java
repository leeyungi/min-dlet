package com.i3e3.mindlet.domain.dandelion.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString(of = {"seq", "petalInfos"})
public class DandelionSeedDto {

    private Long seq;

    private List<DandelionSeedDto.PetalInfo> petalInfos;

    @Builder
    public DandelionSeedDto(Long seq, List<DandelionSeedDto.PetalInfo> petalInfos) {
        this.seq = seq;
        this.petalInfos = petalInfos;
    }

    @Getter
    @ToString(of = {"seq", "message", "imageUrlPath", "nation", "city", "createdDate"})
    static public class PetalInfo {

        private final Long seq;

        private final String message;

        private final String imageUrlPath;

        private final String nation;

        private final String city;

        private final LocalDate createdDate;

        @Builder
        public PetalInfo(Long seq, String message, String imageUrlPath, String nation, String city, LocalDateTime createdDate) {
            this.seq = seq;
            this.message = message;
            this.imageUrlPath = imageUrlPath;
            this.nation = nation;
            this.city = city;
            this.createdDate = createdDate.toLocalDate();
        }
    }
}
