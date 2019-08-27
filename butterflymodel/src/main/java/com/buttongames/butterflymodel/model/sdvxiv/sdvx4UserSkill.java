package com.buttongames.butterflymodel.model.sdvxiv;

import com.buttongames.butterflymodel.model.ButterflyUser;
import com.buttongames.butterflymodel.model.Card;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Model class that represents a user skill in SDVX 4.
 * @author skogaby (skogabyskogaby@gmail.com)
 */
@Entity
@Table(name = "sdvx_4_skills")
public class sdvx4UserSkill implements Serializable {

    private static final long serialVersionUID = 1L;

    /** ID of the object, primary key */
    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    /** The user this skill belongs to */
    @OneToOne
    @JoinColumn(name = "user_id")
    private ButterflyUser user;

    /** The card this profile belongs to */
    @OneToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @Column(name = "ssnid")
    private int ssnid;

    @Column(name = "crsid")
    private int crsid;

    @Column(name = "sc")
    private int sc;

    @Column(name = "ct")
    private int ct;

    @Column(name = "gr")
    private int gr;

    @Column(name = "ar")
    private int ar;

    @Column(name = "cnt")
    private int cnt;

    public sdvx4UserSkill() { }

    public sdvx4UserSkill(ButterflyUser user, Card card, int ssnid, int crsid, int sc, int ct, int gr, int ar, int cnt) {
        this.user = user;
        this.card = card;
        this.ssnid = ssnid;
        this.crsid = crsid;
        this.sc = sc;
        this.ct = ct;
        this.gr = gr;
        this.ar = ar;
        this.cnt = cnt;
    }

    public sdvx4UserSkill(Card card, int ssnid, int crsid, int sc, int ct, int gr, int ar, int cnt) {
        this.card = card;
        this.ssnid = ssnid;
        this.crsid = crsid;
        this.sc = sc;
        this.ct = ct;
        this.gr = gr;
        this.ar = ar;
        this.cnt = cnt;
    }

    public long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }

    public ButterflyUser getUser() {
        return user;
    }

    public void setUser(ButterflyUser user) {
        this.user = user;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public int getSsnid() {
        return ssnid;
    }

    public void setSsnid(int ssnid) {
        this.ssnid = ssnid;
    }

    public int getCrsid() {
        return crsid;
    }

    public void setCrsid(int crsid) {
        this.crsid = crsid;
    }

    public int getSc() {
        return sc;
    }

    public void setSc(int sc) {
        this.sc = sc;
    }

    public int getCt() {
        return ct;
    }

    public void setCt(int ct) {
        this.ct = ct;
    }

    public int getGr() {
        return gr;
    }

    public void setGr(int gr) {
        this.gr = gr;
    }

    public int getAr() {
        return ar;
    }

    public void setAr(int ar) {
        this.ar = ar;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }
}
