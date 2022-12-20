package com.i3e3.mindlet.domain.dandelion.service;

import com.i3e3.mindlet.domain.admin.entity.Report;
import com.i3e3.mindlet.domain.dandelion.entity.Dandelion;
import com.i3e3.mindlet.domain.dandelion.entity.Petal;
import com.i3e3.mindlet.domain.dandelion.entity.Tag;
import com.i3e3.mindlet.domain.dandelion.repository.PetalRepository;
import com.i3e3.mindlet.domain.dandelion.repository.TagRepository;
import com.i3e3.mindlet.domain.member.entity.Member;
import com.i3e3.mindlet.domain.member.repository.MemberRepository;
import com.i3e3.mindlet.global.constant.Report.ReportConst;
import com.i3e3.mindlet.global.constant.message.ErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetalServiceImpl implements PetalService {

    private final MemberRepository memberRepository;
    private final PetalRepository petalRepository;

    private final TagRepository tagRepository;

    @Override
    @Transactional
    public Report reportPetal(Long memberSeq, Long petalSeq, Report.Reason reason) {

        Petal petal = petalRepository.findBySeq(petalSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        List<Report> reports = petal.getReports();

        Member member = memberRepository.findBySeq(memberSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));


        if (reports.size() != 0 && reports.get(0).getStatus().equals(Report.Status.REJECTED)) {
            return null;
        }

        Report newReport = Report.builder()
                .reason(reason)
                .member(member)
                .petal(petal)
                .build();

        if (reports.size() == ReportConst.REPORT_STATUS_CHANGE_T0_PENDING_COUNT.getValue()) {
            for (Report report : reports) {
                report.changeStatus(Report.Status.PENDING, null);
            }
            Dandelion dandelion = petalRepository.findDandelionBySeq(petalSeq)
                    .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));
            dandelion.changeStatus(Dandelion.Status.PENDING);
        }
        return newReport;
    }

    @Override
    public boolean isDandelionOwnerByPetal(Long memberSeq, Long petalSeq) {

        Dandelion dandelion = petalRepository.findDandelionBySeq(petalSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        return dandelion.getMember().getSeq().equals(memberSeq);
    }

    @Override
    @Transactional
    public void deletePetal(Long petalSeq) {

        Petal petal = petalRepository.findBySeq(petalSeq)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.INVALID_REQUEST.getMessage()));

        petal.delete();

        List<Tag> tags = tagRepository.findTagListByMemberSeqAndDandelionSeq(petal.getMember().getSeq(), petal.getDandelion().getSeq())
                .orElse(null);

        tags.forEach(tag -> tagRepository.delete(tag));
    }
}
