package com.buttongames.butterflymodel.model.popn24;

import com.buttongames.butterflymodel.model.ButterflyUser;
import com.buttongames.butterflymodel.model.Card;

import javax.persistence.*;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

@Entity
@Table(name = "popn24_account")
public class popn24Account implements Externalizable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    /** The card this profile belongs to */
    @OneToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @Column(name = "tutorial")
    private int tutorial;

    @Column(name = "area_id")
    private int area_id;

    @Column(name = "lumina")
    private int lumina;

    @Column(name = "medal_set")
    private String medal_set;

    @Column(name = "read_news")
    private int read_news;

    @Column(name = "staff")
    private int staff;

    @Column(name = "is_conv")
    private int is_conv;

    @Column(name = "item_type")
    private int item_type;

    @Column(name = "item_id")
    private int item_id;

    @Column(name = "license_data")
    private String license_data;

    @Column(name = "name")
    private String name;

    @Column(name = "g_pm_id")
    private String g_pm_id;

    @Column(name = "total_play_cnt")
    private int total_play_cnt;

    @Column(name = "today_play_cnt")
    private int today_play_cnt;

    @Column(name = "consecutive_days")
    private int consecutive_days;

    @Column(name = "total_days")
    private int total_days;

    @Column(name = "interval_day")
    private int interval_day;

    @Column(name = "active_fr_num")
    private int active_fr_num;

    @Column(name = "my_best")
    private String my_best;

    @Column(name = "latest_music")
    private String latest_music;

    @Column(name = "nice")
    private String nice;

    @Column(name = "favorite_chara")
    private String favorite_chara;

    @Column(name = "special_area")
    private String special_area;

    @Column(name = "chocolate_charalist")
    private String chocolate_charalist;

    @Column(name = "chocolate_sp_chara")
    private int chocolate_sp_chara;

    @Column(name = "chocolate_pass_cnt")
    private int chocolate_pass_cnt;

    @Column(name = "chocolate_hon_cnt")
    private int chocolate_hon_cnt;

    @Column(name = "chocolate_giri_cnt")
    private int chocolate_giri_cnt;

    @Column(name = "chocolate_kokyu_cnt")
    private int chocolate_kokyu_cnt;

    @Column(name = "teacher_setting")
    private String teacher_setting;

    @Column(name = "welcom_pack")
    private boolean welcom_pack;

    @Column(name = "meteor_flg")
    private boolean meteor_flg;

    @Column(name = "use_navi")
    private int use_navi;

    @Column(name = "ranking_node")
    private int ranking_node;

    @Column(name = "chara_ranking_kind_id")
    private int chara_ranking_kind_id;

    @Column(name = "navi_evolution_flg")
    private int navi_evolution_flg;

    @Column(name = "ranking_news_last_no")
    private int ranking_news_last_no;

    @Column(name = "power_point")
    private int power_point;

    @Column(name = "player_point")
    private int player_point;

    @Column(name = "power_point_list")
    private String power_point_list;


    public popn24Account() {
    }

    public popn24Account(Card card, int tutorial, int area_id, int lumina, String medal_set, int read_news, int staff, int is_conv, int item_type, int item_id, String license_data, String name, String g_pm_id, int total_play_cnt, int today_play_cnt, int consecutive_days, int total_days, int interval_day, int active_fr_num, String my_best, String latest_music, String nice, String favorite_chara, String special_area, String chocolate_charalist, int chocolate_sp_chara, int chocolate_pass_cnt, int chocolate_hon_cnt, int chocolate_giri_cnt, int chocolate_kokyu_cnt, String teacher_setting, boolean welcom_pack, boolean meteor_flg, int use_navi, int ranking_node, int chara_ranking_kind_id, int navi_evolution_flg, int ranking_news_last_no, int power_point, int player_point, String power_point_list) {
        this.card = card;
        this.tutorial = tutorial;
        this.area_id = area_id;
        this.lumina = lumina;
        this.medal_set = medal_set;
        this.read_news = read_news;
        this.staff = staff;
        this.is_conv = is_conv;
        this.item_type = item_type;
        this.item_id = item_id;
        this.license_data = license_data;
        this.name = name;
        this.g_pm_id = g_pm_id;
        this.total_play_cnt = total_play_cnt;
        this.today_play_cnt = today_play_cnt;
        this.consecutive_days = consecutive_days;
        this.total_days = total_days;
        this.interval_day = interval_day;
        this.active_fr_num = active_fr_num;
        this.my_best = my_best;
        this.latest_music = latest_music;
        this.nice = nice;
        this.favorite_chara = favorite_chara;
        this.special_area = special_area;
        this.chocolate_charalist = chocolate_charalist;
        this.chocolate_sp_chara = chocolate_sp_chara;
        this.chocolate_pass_cnt = chocolate_pass_cnt;
        this.chocolate_hon_cnt = chocolate_hon_cnt;
        this.chocolate_giri_cnt = chocolate_giri_cnt;
        this.chocolate_kokyu_cnt = chocolate_kokyu_cnt;
        this.teacher_setting = teacher_setting;
        this.welcom_pack = welcom_pack;
        this.meteor_flg = meteor_flg;
        this.use_navi = use_navi;
        this.ranking_node = ranking_node;
        this.chara_ranking_kind_id = chara_ranking_kind_id;
        this.navi_evolution_flg = navi_evolution_flg;
        this.ranking_news_last_no = ranking_news_last_no;
        this.power_point = power_point;
        this.player_point = player_point;
        this.power_point_list = power_point_list;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(this.id);
        out.writeUTF(this.name);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.setId(in.readLong());
        this.setName(in.readUTF());
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

    public int getTutorial() {
        return tutorial;
    }

    public void setTutorial(int tutorial) {
        this.tutorial = tutorial;
    }

    public int getArea_id() {
        return area_id;
    }

    public void setArea_id(int area_id) {
        this.area_id = area_id;
    }

    public int getLumina() {
        return lumina;
    }

    public void setLumina(int lumina) {
        this.lumina = lumina;
    }

    public String getMedal_set() {
        return medal_set;
    }

    public void setMedal_set(String medal_set) {
        this.medal_set = medal_set;
    }

    public int getRead_news() {
        return read_news;
    }

    public void setRead_news(int read_news) {
        this.read_news = read_news;
    }

    public int getStaff() {
        return staff;
    }

    public void setStaff(int staff) {
        this.staff = staff;
    }

    public int getIs_conv() {
        return is_conv;
    }

    public void setIs_conv(int is_conv) {
        this.is_conv = is_conv;
    }

    public int getItem_type() {
        return item_type;
    }

    public void setItem_type(int item_type) {
        this.item_type = item_type;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getLicense_data() {
        return license_data;
    }

    public void setLicense_data(String license_data) {
        this.license_data = license_data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getG_pm_id() {
        return g_pm_id;
    }

    public void setG_pm_id(String g_pm_id) {
        this.g_pm_id = g_pm_id;
    }

    public int getTotal_play_cnt() {
        return total_play_cnt;
    }

    public void setTotal_play_cnt(int total_play_cnt) {
        this.total_play_cnt = total_play_cnt;
    }

    public int getToday_play_cnt() {
        return today_play_cnt;
    }

    public void setToday_play_cnt(int today_play_cnt) {
        this.today_play_cnt = today_play_cnt;
    }

    public int getConsecutive_days() {
        return consecutive_days;
    }

    public void setConsecutive_days(int consecutive_days) {
        this.consecutive_days = consecutive_days;
    }

    public int getTotal_days() {
        return total_days;
    }

    public void setTotal_days(int total_days) {
        this.total_days = total_days;
    }

    public int getInterval_day() {
        return interval_day;
    }

    public void setInterval_day(int interval_day) {
        this.interval_day = interval_day;
    }

    public int getActive_fr_num() {
        return active_fr_num;
    }

    public void setActive_fr_num(int active_fr_num) {
        this.active_fr_num = active_fr_num;
    }

    public String getMy_best() {
        return my_best;
    }

    public void setMy_best(String my_best) {
        this.my_best = my_best;
    }

    public String getLatest_music() {
        return latest_music;
    }

    public void setLatest_music(String latest_music) {
        this.latest_music = latest_music;
    }

    public String getNice() {
        return nice;
    }

    public void setNice(String nice) {
        this.nice = nice;
    }

    public String getFavorite_chara() {
        return favorite_chara;
    }

    public void setFavorite_chara(String favorite_chara) {
        this.favorite_chara = favorite_chara;
    }

    public String getSpecial_area() {
        return special_area;
    }

    public void setSpecial_area(String special_area) {
        this.special_area = special_area;
    }

    public String getChocolate_charalist() {
        return chocolate_charalist;
    }

    public void setChocolate_charalist(String chocolate_charalist) {
        this.chocolate_charalist = chocolate_charalist;
    }

    public int getChocolate_sp_chara() {
        return chocolate_sp_chara;
    }

    public void setChocolate_sp_chara(int chocolate_sp_chara) {
        this.chocolate_sp_chara = chocolate_sp_chara;
    }

    public int getChocolate_pass_cnt() {
        return chocolate_pass_cnt;
    }

    public void setChocolate_pass_cnt(int chocolate_pass_cnt) {
        this.chocolate_pass_cnt = chocolate_pass_cnt;
    }

    public int getChocolate_hon_cnt() {
        return chocolate_hon_cnt;
    }

    public void setChocolate_hon_cnt(int chocolate_hon_cnt) {
        this.chocolate_hon_cnt = chocolate_hon_cnt;
    }

    public int getChocolate_giri_cnt() {
        return chocolate_giri_cnt;
    }

    public void setChocolate_giri_cnt(int chocolate_giri_cnt) {
        this.chocolate_giri_cnt = chocolate_giri_cnt;
    }

    public int getChocolate_kokyu_cnt() {
        return chocolate_kokyu_cnt;
    }

    public void setChocolate_kokyu_cnt(int chocolate_kokyu_cnt) {
        this.chocolate_kokyu_cnt = chocolate_kokyu_cnt;
    }

    public String getTeacher_setting() {
        return teacher_setting;
    }

    public void setTeacher_setting(String teacher_setting) {
        this.teacher_setting = teacher_setting;
    }

    public boolean isWelcom_pack() {
        return welcom_pack;
    }

    public void setWelcom_pack(boolean welcom_pack) {
        this.welcom_pack = welcom_pack;
    }

    public int getRanking_node() {
        return ranking_node;
    }

    public void setRanking_node(int ranking_node) {
        this.ranking_node = ranking_node;
    }

    public int getChara_ranking_kind_id() {
        return chara_ranking_kind_id;
    }

    public void setChara_ranking_kind_id(int chara_ranking_kind_id) {
        this.chara_ranking_kind_id = chara_ranking_kind_id;
    }

    public int getNavi_evolution_flg() {
        return navi_evolution_flg;
    }

    public void setNavi_evolution_flg(int navi_evolution_flg) {
        this.navi_evolution_flg = navi_evolution_flg;
    }

    public int getRanking_news_last_no() {
        return ranking_news_last_no;
    }

    public void setRanking_news_last_no(int ranking_news_last_no) {
        this.ranking_news_last_no = ranking_news_last_no;
    }

    public int getPower_point() {
        return power_point;
    }

    public void setPower_point(int power_point) {
        this.power_point = power_point;
    }

    public int getPlayer_point() {
        return player_point;
    }

    public void setPlayer_point(int player_point) {
        this.player_point = player_point;
    }

    public String getPower_point_list() {
        return power_point_list;
    }

    public void setPower_point_list(String power_point_list) {
        this.power_point_list = power_point_list;
    }


    public boolean isMeteor_flg() {
        return meteor_flg;
    }

    public void setMeteor_flg(boolean meteor_flg) {
        this.meteor_flg = meteor_flg;
    }

    public int getUse_navi() {
        return use_navi;
    }

    public void setUse_navi(int use_navi) {
        this.use_navi = use_navi;
    }
}
