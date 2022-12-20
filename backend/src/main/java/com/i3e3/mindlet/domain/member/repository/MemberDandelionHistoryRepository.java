package com.i3e3.mindlet.domain.member.repository;

import com.i3e3.mindlet.domain.dandelion.entity.Dandelion;
import com.i3e3.mindlet.domain.member.entity.Member;
import com.i3e3.mindlet.domain.member.entity.MemberDandelionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberDandelionHistoryRepository extends JpaRepository<MemberDandelionHistory, Long> {

    Optional<MemberDandelionHistory> findByMemberAndDandelion(Member member, Dandelion dandelion);
}
