package com.sparta.team30.user.domain;

import com.sparta.team30.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_user")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "nickname", nullable = false, length = 100)
    private String nickname;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @Column(name = "business_number")
    private String businessNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRoleEnum role;

    @Builder
    public User(String email, String username, String password, String nickname, Boolean isPublic, UserRoleEnum role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.isPublic = isPublic;
        this.role = role;
    }

    public void updateRole(UserRoleEnum userRoleEnum){
        this.role = userRoleEnum;
    }

    public void updateRoleOwner(String businessNumber){
        this.businessNumber = businessNumber;
        this.role = UserRoleEnum.OWNER;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public void deleteUser(String deletedBy) {
        super.delete(deletedBy);
    }
}
