package com.i3e3.mindlet.domain.dandelion.service;

import com.i3e3.mindlet.domain.dandelion.entity.Dandelion;
import com.i3e3.mindlet.domain.dandelion.entity.Petal;
import com.i3e3.mindlet.domain.dandelion.entity.Tag;
import com.i3e3.mindlet.domain.dandelion.repository.DandelionRepository;
import com.i3e3.mindlet.domain.dandelion.repository.PetalRepository;
import com.i3e3.mindlet.domain.dandelion.repository.TagRepository;
import com.i3e3.mindlet.domain.dandelion.service.dto.*;
import com.i3e3.mindlet.domain.file.service.FileService;
import com.i3e3.mindlet.domain.member.entity.Member;
import com.i3e3.mindlet.domain.member.entity.MemberDandelionHistory;
import com.i3e3.mindlet.domain.member.repository.MemberDandelionHistoryRepository;
import com.i3e3.mindlet.domain.member.repository.MemberRepository;
import com.i3e3.mindlet.global.constant.dandelion.DandelionConst;
import com.i3e3.mindlet.global.constant.message.ErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DandelionServiceImpl implements DandelionService {

    private final DandelionRepository dandelionRepository;
    private final MemberRepository memberRepository;

    private final TagRepository tagRepository;

    private final PetalRepository petalRepository;

    private final MemberDandelionHistoryRepository memberDandelionHistoryRepository;

    private final FileService fileService;

    @Value("${path.access}")
    private String fileStorageUrl;

    @Value("${path.access.files.images.content}")
    private String contentImagePath;

    @Value("${path.access.files.images.nation}")
    private String nationImagePath;

    @Override
    public boolean isBlossomed(Long dandelionSeq) {
        Dandelion findDandelion = dandelionRepository.findBySeq(dandelionSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        return findDandelion.getStatus() == Dandelion.Status.BLOSSOMED;
    }

    @Override
    public boolean isOwner(Long dandelionSeq, Long memberSeq) {
        Dandelion findDandelion = dandelionRepository.findBySeq(dandelionSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        return findDandelion.getMember().getSeq().equals(memberSeq);
    }

    @Transactional
    @Override
    public String changeDescription(Long dandelionSeq, String description) {
        Dandelion findDandelion = dandelionRepository.findBySeq(dandelionSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        findDandelion.changeDescription(description);
        return findDandelion.getDescription();
    }

    @Override
    public SeedCountDto getLeftSeedCount(Long memberSeq) {

        if (!memberRepository.existsBySeq(memberSeq)) {
            throw new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage());
        }

        Integer countUsingSeed = dandelionRepository.countUsingSeed(memberSeq);
        int maxUsingDandelionCount = DandelionConst.MAX_USING_DANDELION_COUNT.getValue();
        if (countUsingSeed > maxUsingDandelionCount) {
            throw new IllegalStateException(ErrorMessage.MORE_THAN_MAX_COUNT.getMessage());
        }

        int leftSeedCount = maxUsingDandelionCount - countUsingSeed;

        return SeedCountDto.builder()
                .leftSeedCount(leftSeedCount)
                .build();
    }

    @Override
    public boolean isReturn(Long dandelionSeq) {
        Dandelion findDandelion = dandelionRepository.findBySeq(dandelionSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        return findDandelion.getStatus() == Dandelion.Status.RETURN;
    }

    @Transactional
    @Override
    public void changeStatus(Long dandelionSeq, Dandelion.Status status) {

        Dandelion findDandelion = dandelionRepository.findBySeq(dandelionSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        findDandelion.changeStatus(status);
    }

    @Transactional
    @Override
    public void deleteTag(Long tagSeq, Long memberSeq) {
        Tag findTag = tagRepository.findBySeq(tagSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        if (!findTag.getMember().getSeq().equals(memberSeq)) {
            throw new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage());
        } else {
            tagRepository.delete(findTag);
        }
    }

    @Transactional
    @Override
    public void deleteDandelion(Long dandelionSeq, Long memberSeq) {
        Dandelion findDandelion = dandelionRepository.findBySeq(dandelionSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        if (!findDandelion.getMember().getSeq().equals(memberSeq) ||
                findDandelion.getMember().isDeleted()) {
            throw new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage());
        } else {
            findDandelion.delete();
            findDandelion.getPetals().forEach((petal -> petal.delete()));
            findDandelion.getTags().forEach(tag -> tagRepository.delete(tag));
        }
    }

    @Override
    public boolean isParticipated(Long dandelionSeq, Long memberSeq) {
        return petalRepository.existsPetalByDandelionSeqAndMemberSeq(dandelionSeq, memberSeq);
    }

    @Override
    public boolean isAlbum(Long dandelionSeq) {
        Dandelion findDandelion = dandelionRepository.findBySeq(dandelionSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        return findDandelion.getStatus() == Dandelion.Status.ALBUM;
    }

    @Transactional
    @Override
    public List<ResponseGardenInfoDto> getGardenInfoList(Long memberSeq) {

        if (!memberRepository.existsBySeq(memberSeq)) {
            throw new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage());
        }

        List<Dandelion> dandelions = dandelionRepository.findActiveDandelionListByMemberSeq(memberSeq);

        int listSize = DandelionConst.MAX_USING_DANDELION_COUNT.getValue();

        List<ResponseGardenInfoDto> responseGardenInfos = new ArrayList<>(listSize);
        for (int i = 0; i < listSize; i++) {
            responseGardenInfos.add(null);
        }

        for (Dandelion dandelion : dandelions) {
            int index = dandelion.getFlowerSignNumber() - 1;
            responseGardenInfos.set(index,
                    ResponseGardenInfoDto.builder()
                            .blossomedDate(dandelion.getBlossomedDate())
                            .description(dandelion.getDescription())
                            .flowerSignNumber(dandelion.getFlowerSignNumber())
                            .seq(dandelion.getSeq())
                            .status(String.valueOf(dandelion.getStatus()))
                            .build()
            );
        }

        return responseGardenInfos;
    }

    @Transactional
    @Override
    public DandelionDetailSvcDto getDandelionSeedDto(Long memberSeq) {
        Member findMember = memberRepository.findBySeq(memberSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));
        Dandelion findDandelion = dandelionRepository.findRandomFlyingDandelionExceptMember(findMember)
                .orElse(null);

        if (findDandelion == null) {
            return null;
        }

        List<Petal> petals = findDandelion.getPetals();

        List<DandelionDetailSvcDto.PetalInfo> petalInfos = new ArrayList<>();

        for (Petal petal : petals) {
            if (petal.isDeleted()) continue;

            petalInfos.add(DandelionDetailSvcDto.PetalInfo.builder()
                    .seq(petal.getSeq())
                    .message(petal.getMessage())
                    .nation(petal.getNation())
                    .nationImageUrlPath(getNationImagePath(petal.getNation()))
                    .contentImageUrlPath(getContentImagePath(petal.getImageFilename()))
                    .createdDate(petal.getCreatedDate())
                    .build());
        }
        petalInfos.sort(Comparator.comparing(DandelionDetailSvcDto.PetalInfo::getCreatedDate));

        MemberDandelionHistory.builder()
                .member(findMember)
                .dandelion(findDandelion)
                .build();

        findDandelion.changeStatus(Dandelion.Status.HOLD);

        return DandelionDetailSvcDto.builder()
                .dandelionSeq(findDandelion.getSeq())
                .totalPetalCount(petalInfos.size())
                .petalInfos(petalInfos)
                .build();
    }

    @Override
    public AlbumListPageSvcDto getAlbumInfo(Long memberSeq, int page, int size) {
        Page<Dandelion> dandelionPage = dandelionRepository.findAlbumByMemberSeq(memberSeq, PageRequest.of(page - 1, size));

        if (dandelionPage.getTotalElements() == 0) {
            return null;
        }

        List<AlbumListPageSvcDto.dandelionInfo> dandelionInfos = new ArrayList<>();

        for (int i = 0; i < dandelionPage.getNumberOfElements(); i++) {
            dandelionInfos.add(
                    AlbumListPageSvcDto.dandelionInfo.builder()
                            .dandelionSeq(dandelionPage.getContent().get(i).getSeq())
                            .description(dandelionPage.getContent().get(i).getDescription())
                            .build()
            );
        }

        AlbumListPageSvcDto albumListPageSvcDto = AlbumListPageSvcDto.builder()
                .totalDandelionCount(dandelionPage.getTotalElements())
                .totalPageNum(dandelionPage.getTotalPages())
                .nowPageNum(dandelionPage.getNumber() + 1)
                .dandelionInfos(dandelionInfos)
                .build();

        return albumListPageSvcDto;
    }

    @Transactional
    @Override
    public Dandelion createDandelion(Long memberSeq, DandelionCreateSvcDto dandelionCreateSvcDto) throws IOException {
        Member findMember = memberRepository.findBySeq(memberSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        int flowerSignNumber = getFlowerSignNumber(findMember.getSeq());

        Dandelion newDandelion = Dandelion.builder()
                .blossomedDate(dandelionCreateSvcDto.getBlossomedDate())
                .community(findMember.getAppConfig().getCommunity())
                .flowerSignNumber(flowerSignNumber)
                .member(findMember)
                .build();

        createPetal(findMember, newDandelion, dandelionCreateSvcDto.toPetalCreateSvcDto());

        return newDandelion;
    }

    @Override
    public boolean isFlying(Long dandelionSeq) {
        Dandelion findDandelion = dandelionRepository.findBySeq(dandelionSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        return findDandelion.getStatus() == Dandelion.Status.FLYING;
    }

    @Transactional
    @Override
    public Petal addPetal(Long memberSeq, Long dandelionSeq, PetalCreateSvcDto petalCreateSvcDto) throws IOException {
        Member findMember = memberRepository.findBySeq(memberSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));
        Dandelion findDandelion = dandelionRepository.findBySeq(dandelionSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        if (findDandelion.getStatus() == Dandelion.Status.HOLD) {
            findDandelion.changeStatus(Dandelion.Status.FLYING);
        }

        return createPetal(findMember, findDandelion, petalCreateSvcDto);
    }

    @Override
    public boolean isHold(Long dandelionSeq) {
        Dandelion findDandelion = dandelionRepository.findBySeq(dandelionSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));
        return findDandelion.getStatus() == Dandelion.Status.HOLD;
    }

    @Override
    public boolean isMostRecentParticipant(Long dandelionSeq, Long memberSeq) {
        if (!dandelionRepository.existsBySeq(dandelionSeq)) {
            throw new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage());
        }

        Member findMember = memberRepository.findBySeq(memberSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));
        List<MemberDandelionHistory> memberDandelionHistories = findMember.getMemberDandelionHistories();
        memberDandelionHistories.sort((o1, o2) -> o1.getCreatedDate().compareTo(o2.getCreatedDate()) * -1);

        return memberDandelionHistories.get(0).getDandelion().getSeq().equals(dandelionSeq);
    }


    @Override
    public DandelionDetailSvcDto getDandelionDetail(Long dandelionSeq, Long memberSeq) {
        memberRepository.findBySeq(memberSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        Dandelion findDandelion = dandelionRepository.findBySeq(dandelionSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        List<Petal> petals = findDandelion.getPetals();

        List<DandelionDetailSvcDto.PetalInfo> petalInfos = new ArrayList<>();

        for (Petal petal : petals) {
            if (petal.isDeleted()) continue;

            petalInfos.add(DandelionDetailSvcDto.PetalInfo.builder()
                    .seq(petal.getSeq())
                    .message(petal.getMessage())
                    .nation(petal.getNation())
                    .nationImageUrlPath(getNationImagePath(petal.getNation()))
                    .contentImageUrlPath(getContentImagePath(petal.getImageFilename()))
                    .createdDate(petal.getCreatedDate())
                    .build());
        }
        petalInfos.sort(Comparator.comparing(DandelionDetailSvcDto.PetalInfo::getCreatedDate));

        return DandelionDetailSvcDto.builder()
                .dandelionSeq(dandelionSeq)
                .totalPetalCount(petalInfos.size())
                .petalInfos(petalInfos)
                .build();
    }

    private Petal createPetal(Member member, Dandelion dandelion, PetalCreateSvcDto petalCreateSvcDto) throws IOException {
        if (dandelion == null || dandelion.isDeleted()) {
            throw new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage());
        }

        if (member == null || member.isDeleted()) {
            throw new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage());
        }

        if (petalRepository.existsPetalByDandelionSeqAndMemberSeq(dandelion.getSeq(), member.getSeq())) {
            throw new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage());
        }

        String filename = null;
        if (petalCreateSvcDto.getImageFile() != null) {
            String filePath = fileService.s3Upload(petalCreateSvcDto.getImageFile());
            filename = filePath.substring(filePath.lastIndexOf("/") + 1);
        }

        return petalRepository.save(Petal.builder()
                .message(petalCreateSvcDto.getMessage())
                .imageFilename(filename)
                .nation(petalCreateSvcDto.getNation())
                .dandelion(dandelion)
                .member(member)
                .build());
    }

    private int getFlowerSignNumber(Long memberSeq) {
        List<Dandelion> findActiveDandelions = dandelionRepository.findActiveDandelionListByMemberSeq(memberSeq);

        if (findActiveDandelions != null && findActiveDandelions.size() == 5) {
            throw new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage());
        }

        int flowerSign = 1;

        if (findActiveDandelions != null && findActiveDandelions.size() != 0) {
            TreeSet<Integer> ts = new TreeSet<>(List.of(1, 2, 3, 4, 5));

            findActiveDandelions.forEach(dandelion -> ts.remove(dandelion.getFlowerSignNumber()));

            flowerSign = ts.pollFirst();
        }
        return flowerSign;
    }

    public String getNationImagePath(String nation) {
        return nation == null ? null : fileStorageUrl + nationImagePath + nation + ".png";
    }

    public String getContentImagePath(String imageFilename) {
        return imageFilename == null ? null : fileStorageUrl + contentImagePath + imageFilename;
    }

    @Override
    public ParticipationListPageSvcDto getParticipationInfo(Long memberSeq, Pageable pageable) {

        long totalCount = dandelionRepository.countParticipationDandelions(memberSeq);

        if (totalCount == 0) {
            return null;
        }

        long totalPageNumber = (long) Math.ceil((double) totalCount / pageable.getPageSize());

        List<Dandelion> dandelions = dandelionRepository.findParticipationByMemberSeqAndPageable(memberSeq, pageable)
                .orElse(null);

        List<Tag> tags = tagRepository.findTagListByMemberSeq(memberSeq)
                .orElse(null);


        List<ParticipationListPageSvcDto.dandelionInfo> dandelionInfos = new ArrayList<>();

        for (Dandelion dandelion : dandelions) {
            List<ParticipationListPageSvcDto.dandelionInfo.tagInfo> tagInfos = new ArrayList<>();
            for (Tag tag : tags) {
                if (tag.getDandelion().getSeq().equals(dandelion.getSeq())) {
                    tagInfos.add(ParticipationListPageSvcDto.dandelionInfo.tagInfo.builder()
                            .tagSeq(tag.getSeq())
                            .tagName(tag.getName())
                            .build()
                    );
                }
            }
            dandelionInfos.add(ParticipationListPageSvcDto.dandelionInfo.builder()
                    .dandelionSeq(dandelion.getSeq())
                    .tagInfos(tagInfos)
                    .build()
            );
        }

        ParticipationListPageSvcDto participationListPageSvcDto = ParticipationListPageSvcDto.builder()
                .totalDandelionCount(totalCount)
                .totalPageNum(totalPageNumber)
                .nowPageNum(pageable.getPageNumber())
                .dandelionInfos(dandelionInfos)
                .build();

        return participationListPageSvcDto;
    }

    @Override
    public boolean isReady(Long dandelionSeq) {

        Dandelion findDandelion = dandelionRepository.findBySeq(dandelionSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        return findDandelion.getStatus() == Dandelion.Status.READY;
    }
}
