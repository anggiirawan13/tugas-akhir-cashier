package com.be.app.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class UserEntity extends BaseEntity {

    @Column(name = "fullname", nullable = false, length = 100)
    @JsonProperty("fullname")
    private String fullname;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    @JsonProperty("email")
    private String email;

    @Column(name = "username", unique = true, nullable = false, length = 50)
    @JsonProperty("username")
    private String username;

    @Column(name = "password", nullable = false)
    @JsonProperty("password")
    private String password;

    @Column(name = "role", nullable = false, length = 10)
    @JsonProperty("role")
    private String role;

}
