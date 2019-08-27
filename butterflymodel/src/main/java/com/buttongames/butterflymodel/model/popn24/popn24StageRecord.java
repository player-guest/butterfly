package com.buttongames.butterflymodel.model.popn24;

import com.buttongames.butterflymodel.model.Card;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "popn24_stagerecord")
public class popn24StageRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    /** The card this profile belongs to */
    @OneToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @Column(name = "chara_num")
    private int chara_num;

    @Column(name = "mode")
    private int mode;

    @Column(name = "play_id")
    private int play_id;

    @Column(name = "stage")
    private int stage;

    @Column(name = "music_num")
    private int music_num;

    @Column(name = "sheet_num")
    private int sheet_num;

    @Column(name = "clear_type")
    private int clear_type;

    @Column(name = "clear_rank")
    private int clear_rank;

    @Column(name = "score")
    private int score;

    @Column(name = "cool")
    private int cool;

    @Column(name = "great")
    private int great;

    @Column(name = "good")
    private int good;

    @Column(name = "bad")
    private int bad;

    @Column(name = "combo")
    private int combo;

    @Column(name = "highlight")
    private int highlight;

    @Column(name = "gauge")
    private int gauge;

    @Column(name = "gauge_type")
    private int gauge_type;

    @Column(name = "is_netvs")
    private int is_netvs;

    @Column(name = "is_win")
    private int is_win;

    @Column(name = "is_image_store")
    private boolean is_image_store;

    @Column(name = "hispeed")
    private int hispeed;

    @Column(name = "popkun")
    private int popkun;

    @Column(name = "hidden")
    private boolean hidden;

    @Column(name = "hidden_rate")
    private int hidden_rate;

    @Column(name = "sudden")
    private boolean sudden;

    @Column(name = "sudden_rate")
    private int sudden_rate;

    @Column(name = "randmir")
    private int randmir;

    @Column(name = "ojama_0")
    private int ojama_0;

    @Column(name = "ojama_1")
    private int ojama_1;

    @Column(name = "forever_0")
    private boolean forever_0;

    @Column(name = "forever_1")
    private boolean forever_1;

    @Column(name = "full_setting")
    private boolean full_setting;

    @Column(name = "guide_se")
    private int guide_se;

    @Column(name = "judge")
    private int judge;

    @Column(name = "slow")
    private int slow;

    @Column(name = "fast")
    private int fast;

    @Column(name = "netvs_ojama_type")
    private int netvs_ojama_type;

    @Column(name = "netvs_type")
    private int netvs_type;

    @Column(name = "netvs_rank")
    private int netvs_rank;

    @Column(name = "netvs_ojama_0")
    private int netvs_ojama_0;

    @Column(name = "netvs_ojama_1")
    private int netvs_ojama_1;

    @Column(name = "netvs_ojama_2")
    private int netvs_ojama_2;

    @Column(name = "is_staff")
    private boolean is_staff;

    @Column(name = "course_id")
    private int course_id;

    @Column(name = "course_name")
    private String course_name;

    public popn24StageRecord() {
    }

    public popn24StageRecord(Card card, int chara_num, int mode, int play_id, int stage, int music_num, int sheet_num, int clear_type, int clear_rank, int score, int cool, int great, int good, int bad, int combo, int highlight, int gauge, int gauge_type, int is_netvs, int is_win, boolean is_image_store, int hispeed, int popkun, boolean hidden, int hidden_rate, boolean sudden, int sudden_rate, int randmir, int ojama_0, int ojama_1, boolean forever_0, boolean forever_1, boolean full_setting, int guide_se, int judge, int slow, int fast, int netvs_ojama_type, int netvs_type, int netvs_rank, int netvs_ojama_0, int netvs_ojama_1, int netvs_ojama_2, boolean is_staff, int course_id, String course_name) {
        this.card = card;
        this.chara_num = chara_num;
        this.mode = mode;
        this.play_id = play_id;
        this.stage = stage;
        this.music_num = music_num;
        this.sheet_num = sheet_num;
        this.clear_type = clear_type;
        this.clear_rank = clear_rank;
        this.score = score;
        this.cool = cool;
        this.great = great;
        this.good = good;
        this.bad = bad;
        this.combo = combo;
        this.highlight = highlight;
        this.gauge = gauge;
        this.gauge_type = gauge_type;
        this.is_netvs = is_netvs;
        this.is_win = is_win;
        this.is_image_store = is_image_store;
        this.hispeed = hispeed;
        this.popkun = popkun;
        this.hidden = hidden;
        this.hidden_rate = hidden_rate;
        this.sudden = sudden;
        this.sudden_rate = sudden_rate;
        this.randmir = randmir;
        this.ojama_0 = ojama_0;
        this.ojama_1 = ojama_1;
        this.forever_0 = forever_0;
        this.forever_1 = forever_1;
        this.full_setting = full_setting;
        this.guide_se = guide_se;
        this.judge = judge;
        this.slow = slow;
        this.fast = fast;
        this.netvs_ojama_type = netvs_ojama_type;
        this.netvs_type = netvs_type;
        this.netvs_rank = netvs_rank;
        this.netvs_ojama_0 = netvs_ojama_0;
        this.netvs_ojama_1 = netvs_ojama_1;
        this.netvs_ojama_2 = netvs_ojama_2;
        this.is_staff = is_staff;
        this.course_id = course_id;
        this.course_name = course_name;
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

    public int getChara_num() {
        return chara_num;
    }

    public void setChara_num(int chara_num) {
        this.chara_num = chara_num;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getPlay_id() {
        return play_id;
    }

    public void setPlay_id(int play_id) {
        this.play_id = play_id;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getMusic_num() {
        return music_num;
    }

    public void setMusic_num(int music_num) {
        this.music_num = music_num;
    }

    public int getSheet_num() {
        return sheet_num;
    }

    public void setSheet_num(int sheet_num) {
        this.sheet_num = sheet_num;
    }

    public int getClear_type() {
        return clear_type;
    }

    public void setClear_type(int clear_type) {
        this.clear_type = clear_type;
    }

    public int getClear_rank() {
        return clear_rank;
    }

    public void setClear_rank(int clear_rank) {
        this.clear_rank = clear_rank;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCool() {
        return cool;
    }

    public void setCool(int cool) {
        this.cool = cool;
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

    public int getBad() {
        return bad;
    }

    public void setBad(int bad) {
        this.bad = bad;
    }

    public int getCombo() {
        return combo;
    }

    public void setCombo(int combo) {
        this.combo = combo;
    }

    public int getHighlight() {
        return highlight;
    }

    public void setHighlight(int highlight) {
        this.highlight = highlight;
    }

    public int getGauge() {
        return gauge;
    }

    public void setGauge(int gauge) {
        this.gauge = gauge;
    }

    public int getGauge_type() {
        return gauge_type;
    }

    public void setGauge_type(int gauge_type) {
        this.gauge_type = gauge_type;
    }

    public int getIs_netvs() {
        return is_netvs;
    }

    public void setIs_netvs(int is_netvs) {
        this.is_netvs = is_netvs;
    }

    public int getIs_win() {
        return is_win;
    }

    public void setIs_win(int is_win) {
        this.is_win = is_win;
    }

    public boolean isIs_image_store() {
        return is_image_store;
    }

    public void setIs_image_store(boolean is_image_store) {
        this.is_image_store = is_image_store;
    }

    public int getHispeed() {
        return hispeed;
    }

    public void setHispeed(int hispeed) {
        this.hispeed = hispeed;
    }

    public int getPopkun() {
        return popkun;
    }

    public void setPopkun(int popkun) {
        this.popkun = popkun;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public int getHidden_rate() {
        return hidden_rate;
    }

    public void setHidden_rate(int hidden_rate) {
        this.hidden_rate = hidden_rate;
    }

    public boolean isSudden() {
        return sudden;
    }

    public void setSudden(boolean sudden) {
        this.sudden = sudden;
    }

    public int getSudden_rate() {
        return sudden_rate;
    }

    public void setSudden_rate(int sudden_rate) {
        this.sudden_rate = sudden_rate;
    }

    public int getRandmir() {
        return randmir;
    }

    public void setRandmir(int randmir) {
        this.randmir = randmir;
    }

    public int getOjama_0() {
        return ojama_0;
    }

    public void setOjama_0(int ojama_0) {
        this.ojama_0 = ojama_0;
    }

    public int getOjama_1() {
        return ojama_1;
    }

    public void setOjama_1(int ojama_1) {
        this.ojama_1 = ojama_1;
    }

    public boolean isForever_0() {
        return forever_0;
    }

    public void setForever_0(boolean forever_0) {
        this.forever_0 = forever_0;
    }

    public boolean isForever_1() {
        return forever_1;
    }

    public void setForever_1(boolean forever_1) {
        this.forever_1 = forever_1;
    }

    public boolean isFull_setting() {
        return full_setting;
    }

    public void setFull_setting(boolean full_setting) {
        this.full_setting = full_setting;
    }

    public int getGuide_se() {
        return guide_se;
    }

    public void setGuide_se(int guide_se) {
        this.guide_se = guide_se;
    }

    public int getJudge() {
        return judge;
    }

    public void setJudge(int judge) {
        this.judge = judge;
    }

    public int getSlow() {
        return slow;
    }

    public void setSlow(int slow) {
        this.slow = slow;
    }

    public int getFast() {
        return fast;
    }

    public void setFast(int fast) {
        this.fast = fast;
    }

    public int getNetvs_ojama_type() {
        return netvs_ojama_type;
    }

    public void setNetvs_ojama_type(int netvs_ojama_type) {
        this.netvs_ojama_type = netvs_ojama_type;
    }

    public int getNetvs_type() {
        return netvs_type;
    }

    public void setNetvs_type(int netvs_type) {
        this.netvs_type = netvs_type;
    }

    public int getNetvs_rank() {
        return netvs_rank;
    }

    public void setNetvs_rank(int netvs_rank) {
        this.netvs_rank = netvs_rank;
    }

    public int getNetvs_ojama_0() {
        return netvs_ojama_0;
    }

    public void setNetvs_ojama_0(int netvs_ojama_0) {
        this.netvs_ojama_0 = netvs_ojama_0;
    }

    public int getNetvs_ojama_1() {
        return netvs_ojama_1;
    }

    public void setNetvs_ojama_1(int netvs_ojama_1) {
        this.netvs_ojama_1 = netvs_ojama_1;
    }

    public int getNetvs_ojama_2() {
        return netvs_ojama_2;
    }

    public void setNetvs_ojama_2(int netvs_ojama_2) {
        this.netvs_ojama_2 = netvs_ojama_2;
    }

    public boolean isIs_staff() {
        return is_staff;
    }

    public void setIs_staff(boolean is_staff) {
        this.is_staff = is_staff;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }
}
