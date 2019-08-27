package com.buttongames.butterflymodel.model.popn24;

import com.buttongames.butterflymodel.model.Card;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "popn24_mission")
public class popn24Mission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    /**
     * The card this profile belongs to
     */
    @OneToOne
    @JoinColumn(name = "card_id")
    private Card card;

    // id
    @Column(name = "mission_id")
    private int mission_id;

    @Column(name = "gauge_point")
    private int gauge_point;

    @Column(name = "mission_comp")
    private int mission_comp;


    public popn24Mission() {
    }

    public popn24Mission(Card card, int mission_id, int gauge_point, int mission_comp) {
        this.card = card;
        this.mission_id = mission_id;
        this.gauge_point = gauge_point;
        this.mission_comp = mission_comp;
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

    public int getMission_id() {
        return mission_id;
    }

    public void setMission_id(int mission_id) {
        this.mission_id = mission_id;
    }

    public int getGauge_point() {
        return gauge_point;
    }

    public void setGauge_point(int gauge_point) {
        this.gauge_point = gauge_point;
    }

    public int getMission_comp() {
        return mission_comp;
    }

    public void setMission_comp(int mission_comp) {
        this.mission_comp = mission_comp;
    }
}
