package com.i3e3.mindlet.domain.dandelion.service;

import com.i3e3.mindlet.domain.dandelion.entity.Dandelion;
import com.i3e3.mindlet.domain.dandelion.entity.Petal;
import com.i3e3.mindlet.domain.dandelion.service.dto.*;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface DandelionService {

    boolean isBlossomed(Long dandelionSeq);

    boolean isOwner(Long dandelionSeq, Long memberSeq);

    String changeDescription(Long dandelionSeq, String description);

    SeedCountDto getLeftSeedCount(Long memberSeq);

    boolean isReturn(Long dandelionSeq);

    void changeStatus(Long dandelionSeq, Dandelion.Status status);

    void deleteTag(Long tagSeq, Long memberSeq);

    List<ResponseGardenInfoDto> getGardenInfoList(Long memberSeq);

    void deleteDandelion(Long dandelionSeq, Long memberSeq);

    boolean isParticipated(Long dandelionSeq, Long memberSeq);

    boolean isAlbum(Long dandelionSeq);

    DandelionDetailSvcDto getDandelionSeedDto(Long memberSeq);

    AlbumListPageSvcDto getAlbumInfo(Long memberSeq, int page, int size);

    Dandelion createDandelion(Long memberSeq, DandelionCreateSvcDto dandelionCreateSvcDto) throws IOException;

    boolean isHold(Long dandelionSeq);

    boolean isMostRecentParticipant(Long dandelionSeq, Long memberSeq);

    boolean isFlying(Long dandelionSeq);

    Petal addPetal(Long memberSeq, Long dandelionSeq, PetalCreateSvcDto petalCreateSvcDto) throws IOException;

    DandelionDetailSvcDto getDandelionDetail(Long dandelionSeq, Long memberSeq);

    ParticipationListPageSvcDto getParticipationInfo(Long memberSeq, Pageable pageable);

    boolean isReady(Long dandelionSeq);
}
