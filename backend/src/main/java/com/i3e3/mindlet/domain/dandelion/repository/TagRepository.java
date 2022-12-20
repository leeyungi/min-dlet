package com.i3e3.mindlet.domain.dandelion.repository;

import com.i3e3.mindlet.domain.dandelion.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findBySeq(Long seq);

    @Query("SELECT t FROM Tag t WHERE t.member.seq = :memberSeq AND t.dandelion.seq = :dandelionSeq")
    Optional<List<Tag>> findTagListByMemberSeqAndDandelionSeq(@Param("memberSeq") Long memberSeq, @Param("dandelionSeq") Long dandelionSeq);

    @Query("SELECT t FROM Tag t WHERE t.member.seq = :memberSeq")
    Optional<List<Tag>> findTagListByMemberSeq(@Param("memberSeq") Long memberSeq);
}
