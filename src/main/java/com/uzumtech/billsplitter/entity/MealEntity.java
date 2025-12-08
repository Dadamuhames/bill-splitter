package com.uzumtech.billsplitter.entity;

import com.uzumtech.billsplitter.entity.base.BaseDeactivatableEntity;
import com.uzumtech.billsplitter.entity.user.MerchantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "meals")
public class MealEntity extends BaseDeactivatableEntity {

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", updatable = false, foreignKey = @ForeignKey(name = "fk_merchant"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MerchantEntity merchant;
}
