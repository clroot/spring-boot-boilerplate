package io.clroot.boilerplate.auth.model;

import java.time.LocalDateTime;

import io.clroot.boilerplate.common.model.BaseEntity;
import io.clroot.boilerplate.user.model.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "app_user_refresh_token", indexes = {
    @Index(name = "refresh_token_user_id_index", columnList = "user_id"),
    @Index(name = "refresh_token_payload_uindex", columnList = "payload", unique = true)
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
public class RefreshToken extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;

    @Column(columnDefinition = "TEXT", unique = true, nullable = false)
    private String payload;

    private LocalDateTime lastUsedAt;

    public void renew(String payload) {
        this.payload = payload;
    }
}
