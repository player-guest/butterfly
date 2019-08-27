package com.buttongames.butterflymodel.model.sdvxiv;

import com.buttongames.butterflymodel.model.ButterflyUser;
import com.buttongames.butterflymodel.model.Card;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Model class that represents a user profile in SDVX 4.
 * @author skogaby (skogabyskogaby@gmail.com)
 */
@Entity
@Table(name = "sdvx_4_profiles")
public class sdvx4UserProfile implements Serializable {

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

    /** The name of this profile */
    @Column(name = "name")
    private String name;

    /** The dancer code for this profile */
    @Column(name = "code")
    private String code;

    /** The sdvxId of this profile */
    @Column(name = "sdvx_id")
    private String sdvxId;

    /** The appeal_id for this profile */
    @Column(name = "appeal_id")
    private int appeal_id;

    /** The skill_level for this profile */
    @Column(name = "skill_level")
    private int skill_level;

    /** The skill_base_id for this profile */
    @Column(name = "skill_base_id")
    private int skill_base_id;

    /** The skill_name_id for this profile */
    @Column(name = "skill_name_id")
    private int skill_name_id;

    /** The gamecoin_packet for this profile */
    @Column(name = "gamecoin_packet")
    private int gamecoin_packet;

    /** The gamecoin_block for this profile */
    @Column(name = "gamecoin_block")
    private int gamecoin_block;

    /** The blaster_energy for this profile */
    @Column(name = "blaster_energy")
    private int blaster_energy;

    /** The blaster_count for this profile */
    @Column(name = "blaster_count")
    private int blaster_count;

    /** The play_count for this profile */
    @Column(name = "play_count")
    private int play_count;

    /** The day_count for this profile */
    @Column(name = "day_count")
    private int day_count;

    /** The today_count for this profile */
    @Column(name = "today_count")
    private int today_count;

    /** The max_play_chain for this profile */
    @Column(name = "max_play_chain")
    private int max_play_chain;

    /** The week_count for this profile */
    @Column(name = "week_count")
    private int week_count;

    /** The week_play_count for this profile */
    @Column(name = "week_play_count")
    private int week_play_count;

    /** The week_chain for this profile */
    @Column(name = "week_chain")
    private int week_chain;

    /** The max_week_chain for this profile */
    @Column(name = "max_week_chain")
    private int max_week_chain;

    /** The creator_id for this profile */
    @Column(name = "creator_id")
    private int creator_id;

    /** The hispeed for this profile */
    @Column(name = "hispeed")
    private int hispeed;

    /** The lanespeed for this profile */
    @Column(name = "lanespeed")
    private int lanespeed;

    /** The gauge_option for this profile */
    @Column(name = "gauge_option")
    private int gauge_option;
    /** The ars_option for this profile */
    @Column(name = "ars_option")
    private int ars_option;
    /** The notes_option for this profile */
    @Column(name = "notes_option")
    private int notes_option;
    /** The early_late_disp for this profile */
    @Column(name = "early_late_disp")
    private int early_late_disp;
    /** The draw_adjust for this profile */
    @Column(name = "draw_adjust")
    private int draw_adjust;
    /** The eff_c_left for this profile */
    @Column(name = "eff_c_left")
    private int eff_c_left;
    /** The eff_c_right for this profile */
    @Column(name = "eff_c_right")
    private int eff_c_right;

    /** The last_music_id for this profile */
    @Column(name = "last_music_id")
    private int last_music_id;
    /** The last_music_type for this profile */
    @Column(name = "last_music_type")
    private int last_music_type;
    /** The sort_type for this profile */
    @Column(name = "sort_type")
    private int sort_type;
    /** The narrow_down for this profile */
    @Column(name = "narrow_down")
    private int narrow_down;
    /** The headphone for this profile */
    @Column(name = "headphone")
    private int headphone;

    public sdvx4UserProfile() { }

    public sdvx4UserProfile(ButterflyUser user, Card card, String name, String code, String sdvxId, int appeal_id, int skill_level, int skill_base_id, int skill_name_id, int gamecoin_packet, int gamecoin_block, int blaster_energy, int blaster_count, int play_count, int day_count, int today_count, int max_play_chain, int week_count, int week_play_count, int week_chain, int max_week_chain, int creator_id, int hispeed, int lanespeed, int gauge_option, int ars_option, int notes_option, int early_late_disp, int draw_adjust, int eff_c_left, int eff_c_right, int last_music_id, int last_music_type, int sort_type, int narrow_down, int headphone) {
        this.user = user;
        this.card = card;
        this.name = name;
        this.code = code;
        this.sdvxId = sdvxId;
        this.appeal_id = appeal_id;
        this.skill_level = skill_level;
        this.skill_base_id = skill_base_id;
        this.skill_name_id = skill_name_id;
        this.gamecoin_packet = gamecoin_packet;
        this.gamecoin_block = gamecoin_block;
        this.blaster_energy = blaster_energy;
        this.blaster_count = blaster_count;
        this.play_count = play_count;
        this.day_count = day_count;
        this.today_count = today_count;
        this.max_play_chain = max_play_chain;
        this.week_count = week_count;
        this.week_play_count = week_play_count;
        this.week_chain = week_chain;
        this.max_week_chain = max_week_chain;
        this.creator_id = creator_id;
        this.hispeed = hispeed;
        this.lanespeed = lanespeed;
        this.gauge_option = gauge_option;
        this.ars_option = ars_option;
        this.notes_option = notes_option;
        this.early_late_disp = early_late_disp;
        this.draw_adjust = draw_adjust;
        this.eff_c_left = eff_c_left;
        this.eff_c_right = eff_c_right;
        this.last_music_id = last_music_id;
        this.last_music_type = last_music_type;
        this.sort_type = sort_type;
        this.narrow_down = narrow_down;
        this.headphone = headphone;
    }

    public sdvx4UserProfile(Card card, String name, String code, String sdvxId, int appeal_id, int skill_level, int skill_base_id, int skill_name_id, int gamecoin_packet, int gamecoin_block, int blaster_energy, int blaster_count, int play_count, int day_count, int today_count, int max_play_chain, int week_count, int week_play_count, int week_chain, int max_week_chain, int creator_id, int hispeed, int lanespeed, int gauge_option, int ars_option, int notes_option, int early_late_disp, int draw_adjust, int eff_c_left, int eff_c_right, int last_music_id, int last_music_type, int sort_type, int narrow_down, int headphone) {
        this.card = card;
        this.name = name;
        this.code = code;
        this.sdvxId = sdvxId;
        this.appeal_id = appeal_id;
        this.skill_level = skill_level;
        this.skill_base_id = skill_base_id;
        this.skill_name_id = skill_name_id;
        this.gamecoin_packet = gamecoin_packet;
        this.gamecoin_block = gamecoin_block;
        this.blaster_energy = blaster_energy;
        this.blaster_count = blaster_count;
        this.play_count = play_count;
        this.day_count = day_count;
        this.today_count = today_count;
        this.max_play_chain = max_play_chain;
        this.week_count = week_count;
        this.week_play_count = week_play_count;
        this.week_chain = week_chain;
        this.max_week_chain = max_week_chain;
        this.creator_id = creator_id;
        this.hispeed = hispeed;
        this.lanespeed = lanespeed;
        this.gauge_option = gauge_option;
        this.ars_option = ars_option;
        this.notes_option = notes_option;
        this.early_late_disp = early_late_disp;
        this.draw_adjust = draw_adjust;
        this.eff_c_left = eff_c_left;
        this.eff_c_right = eff_c_right;
        this.last_music_id = last_music_id;
        this.last_music_type = last_music_type;
        this.sort_type = sort_type;
        this.narrow_down = narrow_down;
        this.headphone = headphone;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSdvxId() {
        return sdvxId;
    }

    public void setSdvxId(String sdvxId) {
        this.sdvxId = sdvxId;
    }

    public int getAppeal_id() {
        return appeal_id;
    }

    public void setAppeal_id(int appeal_id) {
        this.appeal_id = appeal_id;
    }

    public int getSkill_level() {
        return skill_level;
    }

    public void setSkill_level(int skill_level) {
        this.skill_level = skill_level;
    }

    public int getSkill_base_id() {
        return skill_base_id;
    }

    public void setSkill_base_id(int skill_base_id) {
        this.skill_base_id = skill_base_id;
    }

    public int getSkill_name_id() {
        return skill_name_id;
    }

    public void setSkill_name_id(int skill_name_id) {
        this.skill_name_id = skill_name_id;
    }

    public int getGamecoin_packet() {
        return gamecoin_packet;
    }

    public void setGamecoin_packet(int gamecoin_packet) {
        this.gamecoin_packet = gamecoin_packet;
    }

    public int getGamecoin_block() {
        return gamecoin_block;
    }

    public void setGamecoin_block(int gamecoin_block) {
        this.gamecoin_block = gamecoin_block;
    }

    public int getBlaster_energy() {
        return blaster_energy;
    }

    public void setBlaster_energy(int blaster_energy) {
        this.blaster_energy = blaster_energy;
    }

    public int getBlaster_count() {
        return blaster_count;
    }

    public void setBlaster_count(int blaster_count) {
        this.blaster_count = blaster_count;
    }

    public int getPlay_count() {
        return play_count;
    }

    public void setPlay_count(int play_count) {
        this.play_count = play_count;
    }

    public int getDay_count() {
        return day_count;
    }

    public void setDay_count(int day_count) {
        this.day_count = day_count;
    }

    public int getToday_count() {
        return today_count;
    }

    public void setToday_count(int today_count) {
        this.today_count = today_count;
    }

    public int getMax_play_chain() {
        return max_play_chain;
    }

    public void setMax_play_chain(int max_play_chain) {
        this.max_play_chain = max_play_chain;
    }

    public int getWeek_count() {
        return week_count;
    }

    public void setWeek_count(int week_count) {
        this.week_count = week_count;
    }

    public int getWeek_play_count() {
        return week_play_count;
    }

    public void setWeek_play_count(int week_play_count) {
        this.week_play_count = week_play_count;
    }

    public int getWeek_chain() {
        return week_chain;
    }

    public void setWeek_chain(int week_chain) {
        this.week_chain = week_chain;
    }

    public int getMax_week_chain() {
        return max_week_chain;
    }

    public void setMax_week_chain(int max_week_chain) {
        this.max_week_chain = max_week_chain;
    }

    public int getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }

    public int getHispeed() {
        return hispeed;
    }

    public void setHispeed(int hispeed) {
        this.hispeed = hispeed;
    }

    public int getLanespeed() {
        return lanespeed;
    }

    public void setLanespeed(int lanespeed) {
        this.lanespeed = lanespeed;
    }

    public int getGauge_option() {
        return gauge_option;
    }

    public void setGauge_option(int gauge_option) {
        this.gauge_option = gauge_option;
    }

    public int getArs_option() {
        return ars_option;
    }

    public void setArs_option(int ars_option) {
        this.ars_option = ars_option;
    }

    public int getNotes_option() {
        return notes_option;
    }

    public void setNotes_option(int notes_option) {
        this.notes_option = notes_option;
    }

    public int getEarly_late_disp() {
        return early_late_disp;
    }

    public void setEarly_late_disp(int early_late_disp) {
        this.early_late_disp = early_late_disp;
    }

    public int getDraw_adjust() {
        return draw_adjust;
    }

    public void setDraw_adjust(int draw_adjust) {
        this.draw_adjust = draw_adjust;
    }

    public int getEff_c_left() {
        return eff_c_left;
    }

    public void setEff_c_left(int eff_c_left) {
        this.eff_c_left = eff_c_left;
    }

    public int getEff_c_right() {
        return eff_c_right;
    }

    public void setEff_c_right(int eff_c_right) {
        this.eff_c_right = eff_c_right;
    }

    public int getLast_music_id() {
        return last_music_id;
    }

    public void setLast_music_id(int last_music_id) {
        this.last_music_id = last_music_id;
    }

    public int getLast_music_type() {
        return last_music_type;
    }

    public void setLast_music_type(int last_music_type) {
        this.last_music_type = last_music_type;
    }

    public int getSort_type() {
        return sort_type;
    }

    public void setSort_type(int sort_type) {
        this.sort_type = sort_type;
    }

    public int getNarrow_down() {
        return narrow_down;
    }

    public void setNarrow_down(int narrow_down) {
        this.narrow_down = narrow_down;
    }

    public int getHeadphone() {
        return headphone;
    }

    public void setHeadphone(int headphone) {
        this.headphone = headphone;
    }
}
