package com.uzumtech.billsplitter.repository;

import com.uzumtech.billsplitter.entity.user.WaiterEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WaiterRepository extends JpaRepository<WaiterEntity, Long> {
    Optional<WaiterEntity> findByLogin(final String login);

    Page<WaiterEntity> findAllByMerchantId(final Long merchantId, final Pageable pageable);

    boolean existsByLogin(final String login);

    boolean existsByLoginAndIdNot(final String login, final Long id);

    Optional<WaiterEntity> findByIdAndMerchantId(final Long id, final Long merchantId);
}
