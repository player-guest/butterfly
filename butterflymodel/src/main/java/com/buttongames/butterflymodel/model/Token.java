package com.buttongames.butterflymodel.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "apitokens")
public class Token implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "user")
    private ButterflyUser user;

    @Column(name = "token")
    private String token;

    @Column(name = "expire_time")
    private LocalDateTime expireTime;


    public Token() {
    }

    public Token(ButterflyUser user, String token, LocalDateTime expireTime) {
        this.user = user;
        this.token = token;
        this.expireTime = expireTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ButterflyUser getUser() {
        return user;
    }

    public void setUser(ButterflyUser user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }
}
