package com.uzumtech.billsplitter.entity.order;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "guests")
public class GuestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", updatable = false, foreignKey = @ForeignKey(name = "fk_order"))
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private OrderEntity order;

    @CreationTimestamp
    @Column(updatable = false)
    private OffsetDateTime createdAt;
}
