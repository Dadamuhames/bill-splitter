package com.uzumtech.billsplitter.entity.user;

import com.uzumtech.billsplitter.constant.enums.Role;
import com.uzumtech.billsplitter.entity.base.BaseDeactivatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "merchants")
public class MerchantEntity extends BaseDeactivatableEntity implements CustomUserDetails {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false, unique = true)
    private String taxNumber;

    @Column(nullable = false, length = 80)
    private String password;

    @Override
    public Role getUserRole() {
        return Role.MERCHANT;
    }

    @Override
    public String getUsername() {
        return login;
    }
}
