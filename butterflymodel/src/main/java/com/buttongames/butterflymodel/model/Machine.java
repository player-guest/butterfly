package com.buttongames.butterflymodel.model;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Model class that represents a machine / PCB on the network.
 * @author skogaby (skogabyskogaby@gmail.com)
 */
@Entity
@Table(name = "machines")
public class Machine implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID of the machine, primary key.
     */
    @Id
    @GeneratedValue
    @Expose
    @Column(name = "id")
    private long id;

    /**
     * The user this machine belongs to.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private ButterflyUser user;

    /**
     * The PCBID string for this machine.
     */
    @Expose
    @Column(name = "pcbid")
    private String pcbId;

    /**
     * The date and time this machine was registered.
     */
    @Expose
    @Column(name = "register_time")
    private LocalDateTime registerTime;

    /**
     * Whether this machine is enabled.
     */
    @Expose
    @Column(name = "enabled")
    private boolean enabled;

    /**
     * The port to use for this machine.
     */
    @Expose
    @Column(name = "port")
    private int port;

    public Machine() { }

    public Machine(final ButterflyUser user, final String pcbId, final LocalDateTime registerTime,
                   final boolean enabled, final int port) {
        this.user = user;
        this.pcbId = pcbId;
        this.registerTime = registerTime;
        this.enabled = enabled;
        this.port = port;
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

    public String getPcbId() {
        return pcbId;
    }

    public void setPcbId(String pcbId) {
        this.pcbId = pcbId;
    }

    public LocalDateTime getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(LocalDateTime registerTime) {
        this.registerTime = registerTime;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}