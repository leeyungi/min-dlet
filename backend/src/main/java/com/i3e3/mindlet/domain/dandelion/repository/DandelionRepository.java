package com.i3e3.mindlet.domain.dandelion.repository;

import com.i3e3.mindlet.domain.dandelion.entity.Dandelion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DandelionRepository extends JpaRepository<Dandelion, Long>, DandelionRepositoryCustom {

    @Query("SELECT d FROM Dandelion d WHERE d.seq = :seq AND d.isDeleted = FALSE")
    Optional<Dandelion> findBySeq(@Param("seq") Long seq);

    @Query("SELECT d FROM Dandelion d WHERE d.seq = :seq")
    Optional<Dandelion> findBySeqContainsDeleted(@Param("seq") Long seq);

    @Query("SELECT d FROM Dandelion d WHERE d.member.seq = :memberSeq AND d.status = 'ALBUM' AND d.isDeleted = FALSE ORDER BY d.blossomedDate DESC ")
    Page<Dandelion> findAlbumByMemberSeq(@Param("memberSeq") Long memberSeq, Pageable pageable);

    @Query("SELECT COUNT(d) > 0 FROM Dandelion d WHERE d.seq = :seq AND d.isDeleted = FALSE")
    boolean existsBySeq(@Param("seq") Long seq);

    @Modifying
    @Query("UPDATE Dandelion d SET d.status = 'RETURN' WHERE d.status = 'READY' AND d.isDeleted = FALSE")
    void updateReadyDandelionToReturn();
}
