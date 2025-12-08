package com.uzumtech.billsplitter.repository;

import com.uzumtech.billsplitter.entity.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TableRepository extends JpaRepository<TableEntity, Long> {

    @Query("SELECT t FROM TableEntity t " +
        "LEFT JOIN OrderEntity o ON o.table = t AND o.status = 'OPEN' " +
        "WHERE t.id = :id AND t.merchant.id = :merchantId AND o.id IS NULL")
    Optional<TableEntity> findFreeByIdAndMerchantId(@Param("id") Long id, @Param("merchantId") Long merchantId);}
