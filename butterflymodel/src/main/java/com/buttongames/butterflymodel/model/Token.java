package com.buttongames.butterflymodel.model;

import javax.persistence.*;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.time.LocalDateTime;

@Entity
@Table(name = "apitokens")
public class Token implements Externalizable {

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

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(this.id);
        out.writeObject(this.user);
        out.writeUTF(this.token);
        out.writeObject(this.expireTime);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.setId(in.readLong());
        this.setExpireTime((LocalDateTime) in.readObject());
        this.setToken(in.readUTF());
        this.setUser((ButterflyUser) in.readObject());
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
