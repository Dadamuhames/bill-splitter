package com.uzumtech.billsplitter.entity.order;


import com.uzumtech.billsplitter.constant.enums.OrderStatus;
import com.uzumtech.billsplitter.entity.TableEntity;
import com.uzumtech.billsplitter.entity.user.WaiterEntity;
import com.uzumtech.billsplitter.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class OrderEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "waiter_id", updatable = false, foreignKey = @ForeignKey(name = "fk_waiter"))
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private WaiterEntity waiter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", updatable = false, foreignKey = @ForeignKey(name = "fk_table"))
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private TableEntity table;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "status")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private OrderStatus status;

    @Builder.Default
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private Set<OrderItemEntity> items = new HashSet<>();
}
