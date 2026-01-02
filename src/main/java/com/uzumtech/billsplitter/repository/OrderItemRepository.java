package com.uzumtech.billsplitter.repository;

import com.uzumtech.billsplitter.entity.order.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {

    @Query("SELECT oi FROM OrderItemEntity oi " +
        "LEFT JOIN FETCH oi.guest g " +
        "LEFT JOIN FETCH oi.meal m " +
        "WHERE oi.order.id = ?1 ORDER BY oi.id")
    List<OrderItemEntity> findAllByOrderId(final Long orderId);

    Optional<OrderItemEntity> findByOrderIdAndGuestIdAndMealId(Long orderId, Long guestId, Long mealId);
}
