package com.i3e3.mindlet.domain.dandelion.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString(of = {"dandelionSeq", "totalPetalCount"})
public class DandelionDetailSvcDto {
    private Long dandelionSeq;

    private int totalPetalCount;

    private List<DandelionDetailSvcDto.PetalInfo> petalInfos;

    @Builder
    public DandelionDetailSvcDto(int totalPetalCount, Long dandelionSeq, List<DandelionDetailSvcDto.PetalInfo> petalInfos) {
        this.totalPetalCount = totalPetalCount;
        this.dandelionSeq = dandelionSeq;
        this.petalInfos = petalInfos;
    }

    @Getter
    @ToString(of = {"seq", "message", "nation", "nationImageUrlPath", "contentImageUrlPath", "createdDate"})
    static public class PetalInfo {

        private final Long seq;

        private final String message;

        private final String nation;

        private final String nationImageUrlPath;

        private final String contentImageUrlPath;

        private final LocalDate createdDate;

        @Builder
        public PetalInfo(Long seq, String message, String nation, String nationImageUrlPath, String contentImageUrlPath, LocalDateTime createdDate) {
            this.seq = seq;
            this.message = message;
            this.nation = nation;
            this.nationImageUrlPath = nationImageUrlPath;
            this.contentImageUrlPath = contentImageUrlPath;
            this.createdDate = createdDate.toLocalDate();
        }
    }
}
