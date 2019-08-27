package com.buttongames.butterflymodel.model.gdmatixx;


import com.buttongames.butterflymodel.model.ButterflyUser;
import com.buttongames.butterflymodel.model.Card;

import javax.persistence.*;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

@Entity
@Table(name = "matixx_profile")
public class matixxPlayerProfile implements Externalizable {

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

    @Column(name = "player_type")
    private int player_type;

    /** some kind of 8 digit id ,format 00000000 */
    @Column(name = "did")
    private int did;

    @Column(name = "name")
    private String name;

    @Column(name = "title")
    private String title;

    @Column(name = "charaid")
    private int charaid;

    /** The xml string of playinfo */
    @Column(name = "playinfo")
    private String playinfo;

    @Column(name = "customdata_gf")
    private String customdata_gf;

    @Column(name = "customdata_dm")
    private String customdata_dm;

    /** Not know new and exist means ,save as csv*/
    @Column(name = "skilldata")
    private String skilldata;

    @Column(name = "secretmusic")
    private String secretmusic;

    /** three slot ,save as csv */
    @Column(name = "favoritemusic")
    private String favoritemusic;


    @Column(name = "chara_list")
    private String chara_list;

    /** empty */
    @Column(name = "title_parts")
    private String title_parts;

    @Column(name = "information")
    private String information;

    @Column(name = "reward")
    private String reward;

    @Column(name = "groove")
    private String groove;

    @Column(name = "tutorial")
    private String tutorial;

    @Column(name = "gf_record")
    private String gf_record;

    @Column(name = "dm_record")
    private String dm_record;

    /** need to add greeting*/
    @Column(name = "battledata")
    private String battledata;

    @Column(name = "monthly_skill")
    private String monthly_skill;

    public matixxPlayerProfile() {
    }

    public matixxPlayerProfile(ButterflyUser user, Card card, int player_type, int did, String name, String title, int charaid, String playinfo, String customdata_gf, String customdata_dm, String skilldata, String secretmusic, String favoritemusic, String chara_list, String title_parts, String information, String reward, String groove, String tutorial, String gf_record, String dm_record, String battledata, String monthly_skill) {
        this.user = user;
        this.card = card;
        this.player_type = player_type;
        this.did = did;
        this.name = name;
        this.title = title;
        this.charaid = charaid;
        this.playinfo = playinfo;
        this.customdata_gf = customdata_gf;
        this.customdata_dm = customdata_dm;
        this.skilldata = skilldata;
        this.secretmusic = secretmusic;
        this.favoritemusic = favoritemusic;
        this.chara_list = chara_list;
        this.title_parts = title_parts;
        this.information = information;
        this.reward = reward;
        this.groove = groove;
        this.tutorial = tutorial;
        this.gf_record = gf_record;
        this.dm_record = dm_record;
        this.battledata = battledata;
        this.monthly_skill = monthly_skill;
    }

    public matixxPlayerProfile(Card card, int player_type, int did, String name, String title, int charaid, String playinfo, String customdata_gf, String customdata_dm, String skilldata, String secretmusic, String favoritemusic, String chara_list, String title_parts, String information, String reward, String groove, String tutorial, String gf_record, String dm_record, String battledata, String monthly_skill) {
        this.card = card;
        this.player_type = player_type;
        this.did = did;
        this.name = name;
        this.title = title;
        this.charaid = charaid;
        this.playinfo = playinfo;
        this.customdata_gf = customdata_gf;
        this.customdata_dm = customdata_dm;
        this.skilldata = skilldata;
        this.secretmusic = secretmusic;
        this.favoritemusic = favoritemusic;
        this.chara_list = chara_list;
        this.title_parts = title_parts;
        this.information = information;
        this.reward = reward;
        this.groove = groove;
        this.tutorial = tutorial;
        this.gf_record = gf_record;
        this.dm_record = dm_record;
        this.battledata = battledata;
        this.monthly_skill = monthly_skill;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(this.id);
        out.writeObject(this.user);
        out.writeUTF(this.name);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.setId(in.readLong());
        this.setUser((ButterflyUser) in.readObject());
        this.setName(in.readUTF());
    }


    public long getId() {
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

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public int getPlayer_type() {
        return player_type;
    }

    public void setPlayer_type(int player_type) {
        this.player_type = player_type;
    }

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCharaid() {
        return charaid;
    }

    public void setCharaid(int charaid) {
        this.charaid = charaid;
    }

    public String getPlayinfo() {
        return playinfo;
    }

    public void setPlayinfo(String playinfo) {
        this.playinfo = playinfo;
    }

    public String getCustomdata_gf() {
        return customdata_gf;
    }

    public void setCustomdata_gf(String customdata_gf) {
        this.customdata_gf = customdata_gf;
    }

    public String getSkilldata() {
        return skilldata;
    }

    public void setSkilldata(String skilldata) {
        this.skilldata = skilldata;
    }

    public String getSecretmusic() {
        return secretmusic;
    }

    public void setSecretmusic(String secretmusic) {
        this.secretmusic = secretmusic;
    }

    public String getFavoritemusic() {
        return favoritemusic;
    }

    public void setFavoritemusic(String favoritemusic) {
        this.favoritemusic = favoritemusic;
    }

    public String getChara_list() {
        return chara_list;
    }

    public void setChara_list(String chara_list) {
        this.chara_list = chara_list;
    }

    public String getTitle_parts() {
        return title_parts;
    }

    public void setTitle_parts(String title_parts) {
        this.title_parts = title_parts;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getGroove() {
        return groove;
    }

    public void setGroove(String groove) {
        this.groove = groove;
    }

    public String getTutorial() {
        return tutorial;
    }

    public void setTutorial(String tutorial) {
        this.tutorial = tutorial;
    }

    public String getGf_record() {
        return gf_record;
    }

    public void setGf_record(String gf_record) {
        this.gf_record = gf_record;
    }

    public String getDm_record() {
        return dm_record;
    }

    public void setDm_record(String dm_record) {
        this.dm_record = dm_record;
    }

    public String getBattledata() {
        return battledata;
    }

    public void setBattledata(String battledata) {
        this.battledata = battledata;
    }

    public String getMonthly_skill() {
        return monthly_skill;
    }

    public void setMonthly_skill(String monthly_skill) {
        this.monthly_skill = monthly_skill;
    }

    public String getCustomdata_dm() {
        return customdata_dm;
    }

    public void setCustomdata_dm(String customdata_dm) {
        this.customdata_dm = customdata_dm;
    }
}
