package com.uzumtech.billsplitter.repository;

import com.uzumtech.billsplitter.entity.order.GuestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GuestRepository extends JpaRepository<GuestEntity, Long> {

    Optional<GuestEntity> findByIdAndOrderId(final Long id, final Long orderId);

    int countByOrderId(final Long orderId);


    List<GuestEntity> findAllByOrderId(final Long orderId);
}
