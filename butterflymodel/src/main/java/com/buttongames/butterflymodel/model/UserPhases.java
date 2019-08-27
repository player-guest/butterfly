package com.buttongames.butterflymodel.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Model class to represent the phases a user has set for different games
 * on the network. This applies to all PCBs belonging to a particular user.
 * @author skogaby (skogabyskogaby@gmail.com)
 */
@Entity
@Table(name = "user_phases")
public class UserPhases implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID of the phases, primary key.
     */
    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    /**
     * The user these phases belong to.
     */
    @OneToOne
    @JoinColumn(name = "user_id")
    private ButterflyUser user;

    /**
     * The user's phase for DDR Ace.
     */
    @Column(name = "ddr_16_phase")
    private int ddr16Phase;

    public UserPhases() { }

    public UserPhases(final ButterflyUser user, final int ddr16Phase) {
        this.user = user;
        this.ddr16Phase = ddr16Phase;
    }

    private long getId() {
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

    public int getDdr16Phase() {
        return ddr16Phase;
    }

    public void setDdr16Phase(int ddr16Phase) {
        this.ddr16Phase = ddr16Phase;
    }
}
