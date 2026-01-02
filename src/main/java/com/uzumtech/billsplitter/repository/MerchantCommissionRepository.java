package com.uzumtech.billsplitter.repository;

import com.uzumtech.billsplitter.entity.MerchantCommissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MerchantCommissionRepository extends JpaRepository<MerchantCommissionEntity, Long> {


    @Query("SELECT m FROM MerchantCommissionEntity m WHERE m.merchant.id = ?1 AND m.isActive = true ORDER BY m.id DESC LIMIT 1")
    Optional<MerchantCommissionEntity> findCurrentCommission(final Long merchantId);
}
