package com.i3e3.mindlet.domain.dandelion.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString(of = {"totalDandelionCount", "nowPageNum", "totalPageNum", "dandelionInfos"})
public class ParticipationListPageSvcDto {

    private long totalDandelionCount;

    private long nowPageNum;

    private long totalPageNum;

    private List<dandelionInfo> dandelionInfos;

    @Builder
    public ParticipationListPageSvcDto(long totalDandelionCount, long nowPageNum, long totalPageNum, List<dandelionInfo> dandelionInfos) {
        this.totalDandelionCount = totalDandelionCount;
        this.nowPageNum = nowPageNum;
        this.totalPageNum = totalPageNum;
        this.dandelionInfos = dandelionInfos;
    }

    @Getter
    @ToString(of = {"dandelionSeq", "tagInfos"})
    public static class dandelionInfo {

        private Long dandelionSeq;

        private List<tagInfo> tagInfos;

        @Builder
        public dandelionInfo(Long dandelionSeq, List<tagInfo> tagInfos) {
            this.dandelionSeq = dandelionSeq;
            this.tagInfos = tagInfos;
        }

        @Getter
        @ToString(of = {"tagSeq", "tagName"})
        public static class tagInfo {
            private Long tagSeq;

            private String tagName;

            @Builder
            public tagInfo(Long tagSeq, String tagName) {
                this.tagSeq = tagSeq;
                this.tagName = tagName;
            }
        }
    }
}
