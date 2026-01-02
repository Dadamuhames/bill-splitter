package com.uzumtech.billsplitter.repository;

import com.uzumtech.billsplitter.entity.order.OrderEntity;
import com.uzumtech.billsplitter.repository.projection.OrderProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query("SELECT o.id AS id, t.id AS tableId, t.tableNumber as tableNumber, SUM(it.mealPrice * it.quantity) AS price, o.createdAt AS createdAt " +
        "FROM OrderEntity o " +
        "LEFT JOIN o.table t " +
        "LEFT JOIN o.items it " +
        "WHERE o.waiter.id = ?1 GROUP BY o.id, t.id, t.tableNumber, o.createdAt")
    Page<OrderProjection> findAllByWaiterWithPrice(final Long waiterId, final Pageable pageable);

    Optional<OrderEntity> findByIdAndWaiterId(final Long id, final Long waiterId);
}
