package com.buttongames.butterflymodel.model.sdvxiv;

import com.buttongames.butterflymodel.model.ButterflyUser;
import com.buttongames.butterflymodel.model.Card;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Model class that represents a user param in SDVX 4.
 * @author skogaby (skogabyskogaby@gmail.com)
 */
@Entity
@Table(name = "sdvx_4_params")
public class sdvx4UserParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /** ID of the object, primary key */
    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    /** The user this profile belongs to */
    @OneToOne
    @JoinColumn(name = "user_id")
    private ButterflyUser user;

    /** The card this profile belongs to */
    @OneToOne
    @JoinColumn(name = "card_id")
    private Card card;

    /** The type of param */
    @Column(name = "type")
    private int type;

    /** The param id */
    @Column(name = "param_id")
    private int paramId;

    /** The param content */
    @Column(name = "param")
    private String param;

    /** The param count */
    @Column(name = "count")
    private int count;

    public sdvx4UserParam() { }


    public sdvx4UserParam(ButterflyUser user, Card card, int type, int paramId, String param, int count) {
        this.user = user;
        this.card = card;
        this.type = type;
        this.paramId = paramId;
        this.param = param;
        this.count = count;
    }

    public sdvx4UserParam(Card card, int type, int paramId, String param, int count) {
        this.card = card;
        this.type = type;
        this.paramId = paramId;
        this.param = param;
        this.count = count;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getParamId() {
        return paramId;
    }

    public void setParamId(int paramId) {
        this.paramId = paramId;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
