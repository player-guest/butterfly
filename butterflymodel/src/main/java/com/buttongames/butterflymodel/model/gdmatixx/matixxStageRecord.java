package com.buttongames.butterflymodel.model.gdmatixx;

import com.buttongames.butterflymodel.model.Card;
import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "matixx_stage")
public class matixxStageRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /** ID of the object, primary key */
    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    /** The card this profile belongs to */
    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @Expose
    @Column(name = "date")
    private LocalDateTime date;

    @Expose
    @Column(name = "type")
    private String type;

    @Expose
    @ManyToOne
    @JoinColumn(name = "musicid")
    private matixxMusic musicid;

    @Expose
    @Column(name = "seq")
    private int seq;

    @Expose
    @Column(name = "skill")
    private int skill;

    @Column(name = "new_skill")
    private int new_skill;

    @Column(name = "clear")
    private boolean clear;

    @Column(name = "auto_clear")
    private boolean auto_clear;

    @Expose
    @Column(name = "fullcombo")
    private boolean fullcombo;

    @Expose
    @Column(name = "excellent")
    private boolean excellent;

    @Column(name = "medal")
    private int medal;

    @Expose
    @Column(name = "perc")
    private int perc;

    @Column(name = "new_perc")
    private int new_perc;

    @Column(name = "rank")
    private int rank;

    @Expose
    @Column(name = "score")
    private int score;

    @Expose
    @Column(name = "combo")
    private int combo;

    @Column(name = "max_combo_perc")
    private int max_combo_perc;

    @Column(name = "flags")
    private int flags;

    @Column(name = "phrase_combo_perc")
    private int phrase_combo_perc;

    @Expose
    @Column(name = "perfect")
    private int perfect;

    @Expose
    @Column(name = "great")
    private int great;

    @Expose
    @Column(name = "good")
    private int good;

    @Expose
    @Column(name = "ok")
    private int ok;

    @Expose
    @Column(name = "miss")
    private int miss;

    @Column(name = "perfect_perc")
    private int perfect_perc;

    @Column(name = "great_perc")
    private int great_perc;

    @Column(name = "good_perc")
    private int good_perc;

    @Column(name = "ok_perc")
    private int ok_perc;

    @Column(name = "miss_perc")
    private int miss_perc;

    @Expose
    @Column(name = "meter")
    private String meter;

    @Column(name = "meter_prog")
    private String meter_prog;

    @Column(name = "before_meter")
    private String before_meter;

    @Column(name = "before_meter_prog")
    private String before_meter_prog;

    @Column(name = "is_new_meter")
    private boolean is_new_meter;

    @Column(name = "phrase_data_num")
    private int phrase_data_num;

    @Column(name = "phrase_addr")
    private String phrase_addr;

    @Column(name = "phrase_type")
    private String phrase_type;

    @Column(name = "phrase_status")
    private String phrase_status;

    @Column(name = "phrase_end_addr")
    private int phrase_end_addr;


    public matixxStageRecord() {
    }

    public matixxStageRecord(Card card, LocalDateTime date, String type, matixxMusic musicid, int seq, int skill, int new_skill, boolean clear, boolean auto_clear, boolean fullcombo, boolean excellent, int medal, int perc, int new_perc, int rank, int score, int combo, int max_combo_perc, int flags, int phrase_combo_perc, int perfect, int great, int good, int ok, int miss, int perfect_perc, int great_perc, int good_perc, int ok_perc, int miss_perc, String meter, String meter_prog, String before_meter, String before_meter_prog, boolean is_new_meter, int phrase_data_num, String phrase_addr, String phrase_type, String phrase_status, int phrase_end_addr) {
        this.card = card;
        this.date = date;
        this.type = type;
        this.musicid = musicid;
        this.seq = seq;
        this.skill = skill;
        this.new_skill = new_skill;
        this.clear = clear;
        this.auto_clear = auto_clear;
        this.fullcombo = fullcombo;
        this.excellent = excellent;
        this.medal = medal;
        this.perc = perc;
        this.new_perc = new_perc;
        this.rank = rank;
        this.score = score;
        this.combo = combo;
        this.max_combo_perc = max_combo_perc;
        this.flags = flags;
        this.phrase_combo_perc = phrase_combo_perc;
        this.perfect = perfect;
        this.great = great;
        this.good = good;
        this.ok = ok;
        this.miss = miss;
        this.perfect_perc = perfect_perc;
        this.great_perc = great_perc;
        this.good_perc = good_perc;
        this.ok_perc = ok_perc;
        this.miss_perc = miss_perc;
        this.meter = meter;
        this.meter_prog = meter_prog;
        this.before_meter = before_meter;
        this.before_meter_prog = before_meter_prog;
        this.is_new_meter = is_new_meter;
        this.phrase_data_num = phrase_data_num;
        this.phrase_addr = phrase_addr;
        this.phrase_type = phrase_type;
        this.phrase_status = phrase_status;
        this.phrase_end_addr = phrase_end_addr;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public matixxMusic getMusicid() {
        return musicid;
    }

    public void setMusicid(matixxMusic musicid) {
        this.musicid = musicid;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getSkill() {
        return skill;
    }

    public void setSkill(int skill) {
        this.skill = skill;
    }

    public int getNew_skill() {
        return new_skill;
    }

    public void setNew_skill(int new_skill) {
        this.new_skill = new_skill;
    }

    public boolean isClear() {
        return clear;
    }

    public void setClear(boolean clear) {
        this.clear = clear;
    }

    public boolean isAuto_clear() {
        return auto_clear;
    }

    public void setAuto_clear(boolean auto_clear) {
        this.auto_clear = auto_clear;
    }

    public boolean isFullcombo() {
        return fullcombo;
    }

    public void setFullcombo(boolean fullcombo) {
        this.fullcombo = fullcombo;
    }

    public boolean isExcellent() {
        return excellent;
    }

    public void setExcellent(boolean excellent) {
        this.excellent = excellent;
    }

    public int getMedal() {
        return medal;
    }

    public void setMedal(int medal) {
        this.medal = medal;
    }

    public int getPerc() {
        return perc;
    }

    public void setPerc(int perc) {
        this.perc = perc;
    }

    public int getNew_perc() {
        return new_perc;
    }

    public void setNew_perc(int new_perc) {
        this.new_perc = new_perc;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCombo() {
        return combo;
    }

    public void setCombo(int combo) {
        this.combo = combo;
    }

    public int getMax_combo_perc() {
        return max_combo_perc;
    }

    public void setMax_combo_perc(int max_combo_perc) {
        this.max_combo_perc = max_combo_perc;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public int getPhrase_combo_perc() {
        return phrase_combo_perc;
    }

    public void setPhrase_combo_perc(int phrase_combo_perc) {
        this.phrase_combo_perc = phrase_combo_perc;
    }

    public int getPerfect() {
        return perfect;
    }

    public void setPerfect(int perfect) {
        this.perfect = perfect;
    }

    public int getGreat() {
        return great;
    }

    public void setGreat(int great) {
        this.great = great;
    }

    public int getGood() {
        return good;
    }

    public void setGood(int good) {
        this.good = good;
    }

    public int getOk() {
        return ok;
    }

    public void setOk(int ok) {
        this.ok = ok;
    }

    public int getMiss() {
        return miss;
    }

    public void setMiss(int miss) {
        this.miss = miss;
    }

    public int getPerfect_perc() {
        return perfect_perc;
    }

    public void setPerfect_perc(int perfect_perc) {
        this.perfect_perc = perfect_perc;
    }

    public int getGreat_perc() {
        return great_perc;
    }

    public void setGreat_perc(int great_perc) {
        this.great_perc = great_perc;
    }

    public int getGood_perc() {
        return good_perc;
    }

    public void setGood_perc(int good_perc) {
        this.good_perc = good_perc;
    }

    public int getOk_perc() {
        return ok_perc;
    }

    public void setOk_perc(int ok_perc) {
        this.ok_perc = ok_perc;
    }

    public int getMiss_perc() {
        return miss_perc;
    }

    public void setMiss_perc(int miss_perc) {
        this.miss_perc = miss_perc;
    }

    public String getMeter() {
        return meter;
    }

    public void setMeter(String meter) {
        this.meter = meter;
    }

    public String getMeter_prog() {
        return meter_prog;
    }

    public void setMeter_prog(String meter_prog) {
        this.meter_prog = meter_prog;
    }

    public String getBefore_meter() {
        return before_meter;
    }

    public void setBefore_meter(String before_meter) {
        this.before_meter = before_meter;
    }

    public String getBefore_meter_prog() {
        return before_meter_prog;
    }

    public void setBefore_meter_prog(String before_meter_prog) {
        this.before_meter_prog = before_meter_prog;
    }

    public boolean isIs_new_meter() {
        return is_new_meter;
    }

    public void setIs_new_meter(boolean is_new_meter) {
        this.is_new_meter = is_new_meter;
    }

    public int getPhrase_data_num() {
        return phrase_data_num;
    }

    public void setPhrase_data_num(int phrase_data_num) {
        this.phrase_data_num = phrase_data_num;
    }

    public String getPhrase_addr() {
        return phrase_addr;
    }

    public void setPhrase_addr(String phrase_addr) {
        this.phrase_addr = phrase_addr;
    }

    public String getPhrase_type() {
        return phrase_type;
    }

    public void setPhrase_type(String phrase_type) {
        this.phrase_type = phrase_type;
    }

    public String getPhrase_status() {
        return phrase_status;
    }

    public void setPhrase_status(String phrase_status) {
        this.phrase_status = phrase_status;
    }

    public int getPhrase_end_addr() {
        return phrase_end_addr;
    }

    public void setPhrase_end_addr(int phrase_end_addr) {
        this.phrase_end_addr = phrase_end_addr;
    }
}
