package com.uzumtech.billsplitter.repository;

import com.uzumtech.billsplitter.entity.user.MerchantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MerchantRepository extends JpaRepository<MerchantEntity, Long> {
    Optional<MerchantEntity> findByLogin(final String login);

    boolean existsByLoginOrTaxNumber(final String login, final String taxNumber);
}
