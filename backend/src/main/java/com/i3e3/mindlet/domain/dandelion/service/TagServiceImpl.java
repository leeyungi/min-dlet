package com.i3e3.mindlet.domain.dandelion.service;

import com.i3e3.mindlet.domain.dandelion.entity.Dandelion;
import com.i3e3.mindlet.domain.dandelion.entity.Tag;
import com.i3e3.mindlet.domain.dandelion.repository.DandelionRepository;
import com.i3e3.mindlet.domain.dandelion.repository.TagRepository;
import com.i3e3.mindlet.domain.member.entity.Member;
import com.i3e3.mindlet.domain.member.repository.MemberRepository;
import com.i3e3.mindlet.global.constant.message.ErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagServiceImpl implements TagService {

    private final DandelionRepository dandelionRepository;

    private final MemberRepository memberRepository;

    private final TagRepository tagRepository;

    @Transactional
    @Override
    public Tag registerDandelionTag(Long dandelionSeq, Long memberSeq, String name) {
        Dandelion findDandelion = dandelionRepository.findBySeq(dandelionSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        Member findMember = memberRepository.findBySeq(memberSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        return tagRepository.save(Tag.builder()
                .dandelion(findDandelion)
                .member(findMember)
                .name(name)
                .build());
    }

}
