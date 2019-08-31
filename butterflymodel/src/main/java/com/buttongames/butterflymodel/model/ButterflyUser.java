package com.buttongames.butterflymodel.model;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Model class for a user within the server. This user is tied to a person -- email
 * address, password, etc. This user also has a PIN, which is applied across any
 * and all cards they possess. <code>ButterflyUser</code> does *NOT* represent a profile for any
 * particular game.
 *
 * Update: PIN are bind to card not user now. Butterfly User use to interact with API
 * @author skogaby (skogabyskogaby@gmail.com)
 */
@Entity
@Table(name = "users")
public class ButterflyUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The integer user ID for this user. Serves as the primary key in the database.
     */
    @Id
    @Expose
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Expose
    @Column(name = "email")
    private String email;

    @Column(name = "salt")
    private String salt;

    @Column(name = "passwordHash")
    private String passwordHash;


    /**
     * The date and time this user was registered.
     */
    @Expose
    @Column(name = "register_time")
    private LocalDateTime registerTime;

    /**
     * The date and time this user last logged into a game on a card.
     */
    @Column(name = "last_play_time")
    private LocalDateTime lastPlayTime;

    /**
     * The amount of Paseli this user has.
     */
    @Expose
    @Column(name = "paseli_balance")
    private int paseliBalance;

    @Expose
    @Column(name = "user_group")
    private String user_group;

    public ButterflyUser() { }

    public ButterflyUser(String email, String salt, String passwordHash, LocalDateTime registerTime, LocalDateTime lastPlayTime, int paseliBalance, String user_group) {
        this.email = email;
        this.salt = salt;
        this.passwordHash = passwordHash;
        this.registerTime = registerTime;
        this.lastPlayTime = lastPlayTime;
        this.paseliBalance = paseliBalance;
        this.user_group = user_group;
    }

    public long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(LocalDateTime registerTime) {
        this.registerTime = registerTime;
    }

    public LocalDateTime getLastPlayTime() {
        return lastPlayTime;
    }

    public void setLastPlayTime(LocalDateTime lastPlayTime) {
        this.lastPlayTime = lastPlayTime;
    }

    public int getPaseliBalance() {
        return paseliBalance;
    }

    public void setPaseliBalance(int paseliBalance) {
        this.paseliBalance = paseliBalance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getUser_group() {
        return user_group;
    }

    public void setUser_group(String group) {
        this.user_group = user_group;
    }
}
