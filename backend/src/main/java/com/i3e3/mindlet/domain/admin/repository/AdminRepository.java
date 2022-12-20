package com.i3e3.mindlet.domain.admin.repository;

import com.i3e3.mindlet.domain.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    boolean existsById(String id);

    @Query("SELECT a FROM Admin a WHERE a.seq = :seq AND a.isDeleted = FALSE")
    Optional<Admin> findAdminBySeq(@Param("seq") Long seq);
}
