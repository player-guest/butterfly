package com.buttongames.butterflymodel.model.popn24;

import com.buttongames.butterflymodel.model.Card;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "popn24_charaparam")
public class popn24CharaParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    /** The card this profile belongs to */
    @OneToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @Column(name = "chara_id")
    private int chara_id;

    @Column(name = "friendship")
    private int friendship;

    public popn24CharaParam() {
    }

    public popn24CharaParam(Card card, int chara_id, int friendship) {
        this.card = card;
        this.chara_id = chara_id;
        this.friendship = friendship;
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

    public int getChara_id() {
        return chara_id;
    }

    public void setChara_id(int chara_id) {
        this.chara_id = chara_id;
    }

    public int getFriendship() {
        return friendship;
    }

    public void setFriendship(int friendship) {
        this.friendship = friendship;
    }
}
