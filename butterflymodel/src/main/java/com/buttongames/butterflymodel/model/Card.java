package com.buttongames.butterflymodel.model;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.time.LocalDateTime;

/**
 * Model class that represents a shop housing a DDR 16 machine.
 * @author skogaby (skogabyskogaby@gmail.com)
 */
@Entity
@Table(name = "cards")
public class Card implements Externalizable {

    private static final long serialVersionUID = 1L;

    /**
     * ID of the object, primary key
     * */
    @Id
    @GeneratedValue
    @Expose
    @Column(name = "id")
    private long id;

    /**
     * The user this card belongs to
     */
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private ButterflyUser user;

    /**
     * The type of this card (old or FeliCa)
     */
    @Expose
    @Column(name = "card_type")
    @Enumerated(EnumType.STRING)
    private CardType type;

    /**
     * The internal NFC ID for this card
     */
    @Expose
    @Column(name = "nfc_id")
    private String nfcId;

    /**
     * The external display ID for this card
     */
    @Expose
    @Column(name = "display_id")
    private String displayId;

    /**
     * The value to use for dataid/refid in the profile responses.
     */
    @Column(name = "ref_id")
    private String refId;

    /**
     * The user's card PIN.
     */
    @Column(name = "pin")
    private String pin;

    /**
     * The date and time this card was registered
     */
    @Expose
    @Column(name = "register_time")
    private LocalDateTime registerTime;

    /**
     * The date and time this card last logged into a game
     */
    @Expose
    @Column(name = "last_play_time")
    private LocalDateTime lastPlayTime;

    public Card() { }

    public Card(final CardType type, final String nfcId, final String displayId,
                final String refId, final String pin, final LocalDateTime registerTime, final LocalDateTime lastPlayTime) {
        this.type = type;
        this.nfcId = nfcId;
        this.displayId = displayId;
        this.refId = refId;
        this.pin = pin;
        this.registerTime = registerTime;
        this.lastPlayTime = lastPlayTime;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(this.id);
        out.writeObject(this.user);
        out.writeObject(this.type);
        out.writeUTF(this.nfcId);
        out.writeUTF(this.displayId);
        out.writeUTF(this.refId);
        out.writeUTF(this.pin);
        out.writeObject(this.registerTime);
        out.writeObject(this.lastPlayTime);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.setId(in.readLong());
        this.setUser((ButterflyUser) in.readObject());
        this.setType((CardType) in.readObject());
        this.setNfcId(in.readUTF());
        this.setDisplayId(in.readUTF());
        this.setRefId(in.readUTF());
        this.setPin(in.readUTF());
        this.setRegisterTime((LocalDateTime) in.readObject());
        this.setLastPlayTime((LocalDateTime) in.readObject());
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

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public String getNfcId() {
        return nfcId;
    }

    public void setNfcId(String nfcId) {
        this.nfcId = nfcId;
    }

    public String getDisplayId() {
        return displayId;
    }

    public void setDisplayId(String displayId) {
        this.displayId = displayId;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public LocalDateTime getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(LocalDateTime registerTime) {
        this.registerTime = registerTime;
    }

    public LocalDateTime getLastPlayTime() {
        return lastPlayTime;
    }

    public void setLastPlayTime(LocalDateTime lastPlayTime) {
        this.lastPlayTime = lastPlayTime;
    }
}
