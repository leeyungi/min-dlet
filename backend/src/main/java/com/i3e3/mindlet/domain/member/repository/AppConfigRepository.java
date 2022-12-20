package com.i3e3.mindlet.domain.member.repository;

import com.i3e3.mindlet.domain.member.entity.AppConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AppConfigRepository extends JpaRepository<AppConfig, Long> {
    @Query("SELECT ac FROM AppConfig ac WHERE ac.member.seq = :memberSeq AND ac.member.isDeleted = FALSE")
    Optional<AppConfig> findByMemberSeq(@Param("memberSeq") Long memberSeq);
}
