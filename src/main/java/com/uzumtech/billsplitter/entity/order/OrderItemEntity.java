package com.uzumtech.billsplitter.entity.order;


import com.uzumtech.billsplitter.entity.MealEntity;
import com.uzumtech.billsplitter.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_items", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"order_id", "guest_id", "meal_id"})
})
public class OrderItemEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", updatable = false, foreignKey = @ForeignKey(name = "fk_order"))
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id", updatable = false, foreignKey = @ForeignKey(name = "fk_guest"))
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private GuestEntity guest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", updatable = false, foreignKey = @ForeignKey(name = "fk_meals"))
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private MealEntity meal;

    @Column(nullable = false)
    @Positive
    private Long mealPrice;

    @Column(nullable = false)
    @Positive
    private int quantity;
}
