package com.uzumtech.billsplitter.entity;

import com.uzumtech.billsplitter.entity.user.MerchantEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "merchant_commissions")
public class MerchantCommissionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", updatable = false, foreignKey = @ForeignKey(name = "fk_merchant"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MerchantEntity merchant;

    @Column(nullable = false)
    private BigDecimal commissionRate;

    @CreationTimestamp
    @Column(updatable = false)
    private OffsetDateTime createdAt;

    @Builder.Default
    @Column(name = "is_active")
    boolean isActive = true;
}
