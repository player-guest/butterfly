package com.buttongames.butterflymodel.model.popn24;

import com.buttongames.butterflymodel.model.Card;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "popn24_item")
public class popn24Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    /** The card this profile belongs to */
    @OneToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @Column(name = "type")
    private int type;

    // id
    @Column(name = "itemId")
    private int itemId;

    @Column(name = "param")
    private int param;

    @Column(name = "is_new")
    private boolean is_new;

    @Column(name = "get_time")
    private long get_time;


    public popn24Item() {
    }

    public popn24Item(Card card, int type, int itemId, int param, boolean is_new, long get_time) {
        this.card = card;
        this.type = type;
        this.itemId = itemId;
        this.param = param;
        this.is_new = is_new;
        this.get_time = get_time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getParam() {
        return param;
    }

    public void setParam(int param) {
        this.param = param;
    }

    public boolean isIs_new() {
        return is_new;
    }

    public void setIs_new(boolean is_new) {
        this.is_new = is_new;
    }

    public long getGet_time() {
        return get_time;
    }

    public void setGet_time(long get_time) {
        this.get_time = get_time;
    }
}
