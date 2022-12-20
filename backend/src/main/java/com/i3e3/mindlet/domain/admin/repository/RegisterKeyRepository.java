package com.i3e3.mindlet.domain.admin.repository;

import com.i3e3.mindlet.domain.admin.entity.RegisterKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterKeyRepository extends JpaRepository<RegisterKey, Long> {

    boolean existsByValue(String value);
}
