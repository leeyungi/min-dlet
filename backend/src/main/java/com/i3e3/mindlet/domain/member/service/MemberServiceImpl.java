package com.i3e3.mindlet.domain.member.service;

import com.i3e3.mindlet.domain.dandelion.repository.TagRepository;
import com.i3e3.mindlet.domain.member.entity.AppConfig;
import com.i3e3.mindlet.domain.member.entity.Member;
import com.i3e3.mindlet.domain.member.repository.AppConfigRepository;
import com.i3e3.mindlet.domain.member.repository.MemberRepository;
import com.i3e3.mindlet.domain.member.service.dto.request.MemberLoginDto;
import com.i3e3.mindlet.domain.member.service.dto.request.MemberRegisterDto;
import com.i3e3.mindlet.domain.member.service.dto.response.MemberInfoDto;
import com.i3e3.mindlet.global.constant.message.ErrorMessage;
import com.i3e3.mindlet.global.enums.Community;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final AppConfigRepository appConfigRepository;

    private final PasswordEncoder passwordEncoder;

    private final TagRepository tagRepository;

    @Override
    public boolean login(MemberLoginDto memberLoginDto) {
        Member findMember = memberRepository.findById(memberLoginDto.getId())
                .orElse(null);

        String password = memberLoginDto.getPassword();
        return findMember == null ?
                false : passwordEncoder.matches(password, findMember.getPassword());
    }

    @Override
    public MemberInfoDto getMemberInfoById(String id) {
        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_ID.getMessage()));

        return MemberInfoDto.createByMember(findMember);
    }

    @Override
    public MemberInfoDto getMemberInfoBySeq(Long seq) {
        Member findMember = memberRepository.findBySeq(seq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        return MemberInfoDto.createByMember(findMember);
    }

    @Transactional
    @Override
    public Member register(MemberRegisterDto memberRegisterDto) {
        Member newMember = Member.builder()
                .id(memberRegisterDto.getId())
                .password(passwordEncoder.encode(memberRegisterDto.getPassword()))
                .build();

        AppConfig newAppConfig = AppConfig.builder()
                .language(AppConfig.Language.KOREAN)
                .member(newMember)
                .build();

        Member savedMember = memberRepository.save(newMember);
        appConfigRepository.save(newAppConfig);

        return savedMember;
    }

    @Override
    public boolean isExistsId(String id) {
        return memberRepository.existsByIdContainsDeleted(id);
    }

    @Transactional
    @Override
    public void changeCommunity(Long memberSeq, Community community) {
        AppConfig appConfig = appConfigRepository.findByMemberSeq(memberSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        appConfig.changeCommunity(community);
    }

    @Transactional
    @Override
    public void changeSound(Long memberSeq, boolean soundOff) {
        AppConfig appConfig = appConfigRepository.findByMemberSeq(memberSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        if (soundOff) {
            appConfig.soundOff();
        } else {
            appConfig.soundOn();
        }
    }

    @Transactional
    @Override
    public void changeLanguage(Long memberSeq, AppConfig.Language changeLanguage) {
        AppConfig appConfig = appConfigRepository.findByMemberSeq(memberSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        appConfig.changeLanguage(changeLanguage);
    }

    @Transactional
    @Override
    public void delete(Long memberSeq) {
        Member findMember = memberRepository.findBySeq(memberSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        if (findMember.isDeleted()) {
            throw new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage());
        }

        findMember.delete();
    }
}
