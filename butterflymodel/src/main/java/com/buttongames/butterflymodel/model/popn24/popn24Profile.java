package com.buttongames.butterflymodel.model.popn24;


import com.buttongames.butterflymodel.model.Card;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "popn24_profile")
public class popn24Profile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    /** The card this profile belongs to */
    @OneToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @Column(name = "config")
    private String config;

    @Column(name = "option")
    private String option;

    @Column(name = "custom_cate")
    private String custom_cate;

    @Column(name = "info")
    private String info;

    @Column(name = "customize")
    private String customize;

    @Column(name = "netvs")
    private String netvs;

    public popn24Profile() {
    }

    public popn24Profile(Card card, String config, String option, String custom_cate, String info, String customize, String netvs) {
        this.card = card;
        this.config = config;
        this.option = option;
        this.custom_cate = custom_cate;
        this.info = info;
        this.customize = customize;
        this.netvs = netvs;
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

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getCustom_cate() {
        return custom_cate;
    }

    public void setCustom_cate(String custom_cate) {
        this.custom_cate = custom_cate;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCustomize() {
        return customize;
    }

    public void setCustomize(String customize) {
        this.customize = customize;
    }

    public String getNetvs() {
        return netvs;
    }

    public void setNetvs(String netvs) {
        this.netvs = netvs;
    }
}
