package com.uzumtech.billsplitter.entity.user;

import com.uzumtech.billsplitter.constant.enums.Role;
import com.uzumtech.billsplitter.entity.base.BaseDeactivatableEntity;
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
@Table(name = "waiters")
public class WaiterEntity extends BaseDeactivatableEntity implements CustomUserDetails {
    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false, length = 80)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", updatable = false, foreignKey = @ForeignKey(name = "fk_merchant"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MerchantEntity merchant;


    @Override
    public Role getUserRole() {
        return Role.WAITER;
    }

    @Override
    public String getUsername() {
        return login;
    }
}
