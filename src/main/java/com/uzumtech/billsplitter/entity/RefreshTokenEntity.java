package com.uzumtech.billsplitter.entity;

import com.uzumtech.billsplitter.constant.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "refresh_token_entity")
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    @Column(nullable = false)
    private String subject;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "user_role")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private Role userRole;
}