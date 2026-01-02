package com.uzumtech.billsplitter.entity;

import com.uzumtech.billsplitter.entity.user.MerchantEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tables", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"merchant_id", "table_number"})
})
public class TableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", updatable = false, foreignKey = @ForeignKey(name = "fk_merchant"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MerchantEntity merchant;

    @Column(nullable = false)
    private String tableNumber;
}
