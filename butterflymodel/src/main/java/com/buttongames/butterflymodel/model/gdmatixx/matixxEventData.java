package com.buttongames.butterflymodel.model.gdmatixx;

import com.buttongames.butterflymodel.model.Card;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "matixx_event_data")
public class matixxEventData implements Serializable {

    private static final long serialVersionUID = 1L;

    /** ID of the object, primary key */
    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    /** The card this eventdata belongs to */
    @OneToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @Column(name = "event_name")
    private String event_name;

    @Column(name = "value")
    private String value;

    public matixxEventData() {
    }

    public matixxEventData(Card card, String event_name, String value) {
        this.card = card;
        this.event_name = event_name;
        this.value = value;
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

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
