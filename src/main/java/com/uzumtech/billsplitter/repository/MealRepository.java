package com.uzumtech.billsplitter.repository;

import com.uzumtech.billsplitter.entity.MealEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MealRepository extends JpaRepository<MealEntity, Long> {
    Page<MealEntity> findAllByMerchantId(final Long merchantId, final Pageable pageable);

    List<MealEntity> findAllByMerchantIdAndIdIn(final Long merchantId, final Set<Long> ids);

    Optional<MealEntity> findByIdAndMerchantId(final Long id, final Long merchantId);
}
