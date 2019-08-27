package com.buttongames.butterflymodel.model.gdmatixx;

import com.buttongames.butterflymodel.model.ButterflyUser;
import com.buttongames.butterflymodel.model.Card;

import javax.persistence.*;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

@Entity
@Table(name = "matixx_playerboard")
public class matixxPlayerboard implements Externalizable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @OneToOne
    @JoinColumn(name = "card")
    private Card card;

    @Column(name = "stickerId")
    private int stickerId;

    @Column(name = "pos_x")
    private float pos_x;

    @Column(name = "pos_y")
    private float pos_y;

    @Column(name = "scale_x")
    private float scale_x;

    @Column(name = "scale_y")
    private float scale_y;

    @Column(name = "rotate")
    private float rotate;


    public matixxPlayerboard() {
    }

    public matixxPlayerboard(Card card, int stickerId, float pos_x, float pos_y, float scale_x, float scale_y, float rotate) {
        this.card = card;
        this.stickerId = stickerId;
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.scale_x = scale_x;
        this.scale_y = scale_y;
        this.rotate = rotate;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(this.id);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.setId(in.readLong());
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

    public int getStickerId() {
        return stickerId;
    }

    public void setStickerId(int stickerId) {
        this.stickerId = stickerId;
    }

    public float getPos_x() {
        return pos_x;
    }

    public void setPos_x(float pos_x) {
        this.pos_x = pos_x;
    }

    public float getPos_y() {
        return pos_y;
    }

    public void setPos_y(float pos_y) {
        this.pos_y = pos_y;
    }

    public float getScale_x() {
        return scale_x;
    }

    public void setScale_x(float scale_x) {
        this.scale_x = scale_x;
    }

    public float getScale_y() {
        return scale_y;
    }

    public void setScale_y(float scale_y) {
        this.scale_y = scale_y;
    }

    public float getRotate() {
        return rotate;
    }

    public void setRotate(float rotate) {
        this.rotate = rotate;
    }
}
