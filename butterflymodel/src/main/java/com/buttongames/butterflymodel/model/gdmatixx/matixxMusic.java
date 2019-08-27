package com.buttongames.butterflymodel.model.gdmatixx;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

// TODO: Support multiple  music list for omnimix
@Entity
@Table(name = "matixx_music")
public class matixxMusic implements Externalizable {

    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Column(name = "list_type")
    private int list_type;

    @Id
    @Expose
    @Column(name = "musicid")
    private int musicid;

    @Expose
    @Column(name = "guitar_diff")
    private String guitar_diff;

    @Expose
    @Column(name = "bass_diff")
    private String bass_diff;

    @Expose
    @Column(name = "drum_diff")
    private String drum_diff;

    @Expose
    @Column(name = "is_hot")
    private boolean is_hot;

    @Column(name = "contain_stat")
    private String contain_stat;

    @Expose
    @Column(name = "first_ver")
    private String first_ver;

    @Column(name = "first_classic_ver")
    private String first_classic_ver;

    @Expose
    @Column(name = "b_long")
    private boolean b_long;

    @Column(name = "b_eemail")
    private boolean b_eemail;

    @Expose
    @Column(name = "bpm")
    private int bpm;

    @Expose
    @Column(name = "bpm2")
    private int bpm2;

    @Expose
    @Column(name = "title_name")
    private String title_name;

    @Expose
    @Column(name = "title_ascii")
    private String title_ascii;

    @Expose
    @Column(name = "artist_title_ascii")
    private String artist_title_ascii;

    @Column(name = "xg_secret")
    private String xg_secret;

    @Column(name = "xg_b_session")
    private boolean xg_b_session;

    @Column(name = "speed")
    private int speed;

    @Expose
    @Column(name = "chart_list")
    private String chart_list;

    @Column(name = "origin")
    private int origin;

    @Expose
    @Column(name = "music_type")
    private int music_type;

    @Expose
    @Column(name = "genre")
    private int genre;

    @Expose
    @Column(name = "type_category")
    private int type_category;

    public matixxMusic() {
    }

    public matixxMusic(int list_type, int musicid, String guitar_diff, String bass_diff, String drum_diff, boolean is_hot, String contain_stat, String first_ver, String first_classic_ver, boolean b_long, boolean b_eemail, int bpm, int bpm2, String title_name, String title_ascii, String artist_title_ascii, String xg_secret, boolean xg_b_session, int speed, String chart_list, int origin, int music_type, int genre, int type_category) {
        this.list_type = list_type;
        this.musicid = musicid;
        this.guitar_diff = guitar_diff;
        this.bass_diff = bass_diff;
        this.drum_diff = drum_diff;
        this.is_hot = is_hot;
        this.contain_stat = contain_stat;
        this.first_ver = first_ver;
        this.first_classic_ver = first_classic_ver;
        this.b_long = b_long;
        this.b_eemail = b_eemail;
        this.bpm = bpm;
        this.bpm2 = bpm2;
        this.title_name = title_name;
        this.title_ascii = title_ascii;
        this.artist_title_ascii = artist_title_ascii;
        this.xg_secret = xg_secret;
        this.xg_b_session = xg_b_session;
        this.speed = speed;
        this.chart_list = chart_list;
        this.origin = origin;
        this.music_type = music_type;
        this.genre = genre;
        this.type_category = type_category;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(this.id);
        out.writeObject(this.musicid);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.setId(in.readLong());
        this.setMusicid(in.readInt());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getList_type() {
        return list_type;
    }

    public void setList_type(int list_type) {
        this.list_type = list_type;
    }

    public int getMusicid() {
        return musicid;
    }

    public void setMusicid(int musicid) {
        this.musicid = musicid;
    }

    public String getGuitar_diff() {
        return guitar_diff;
    }

    public void setGuitar_diff(String guitar_diff) {
        this.guitar_diff = guitar_diff;
    }

    public String getBass_diff() {
        return bass_diff;
    }

    public void setBass_diff(String bass_diff) {
        this.bass_diff = bass_diff;
    }

    public String getDrum_diff() {
        return drum_diff;
    }

    public void setDrum_diff(String drum_diff) {
        this.drum_diff = drum_diff;
    }

    public boolean isIs_hot() {
        return is_hot;
    }

    public void setIs_hot(boolean is_hot) {
        this.is_hot = is_hot;
    }

    public String getContain_stat() {
        return contain_stat;
    }

    public void setContain_stat(String contain_stat) {
        this.contain_stat = contain_stat;
    }

    public String getFirst_ver() {
        return first_ver;
    }

    public void setFirst_ver(String first_ver) {
        this.first_ver = first_ver;
    }

    public String getFirst_classic_ver() {
        return first_classic_ver;
    }

    public void setFirst_classic_ver(String first_classic_ver) {
        this.first_classic_ver = first_classic_ver;
    }

    public boolean isB_long() {
        return b_long;
    }

    public void setB_long(boolean b_long) {
        this.b_long = b_long;
    }

    public boolean isB_eemail() {
        return b_eemail;
    }

    public void setB_eemail(boolean b_eemail) {
        this.b_eemail = b_eemail;
    }

    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    public int getBpm2() {
        return bpm2;
    }

    public void setBpm2(int bpm2) {
        this.bpm2 = bpm2;
    }

    public String getTitle_name() {
        return title_name;
    }

    public void setTitle_name(String title_name) {
        this.title_name = title_name;
    }

    public String getTitle_ascii() {
        return title_ascii;
    }

    public void setTitle_ascii(String title_ascii) {
        this.title_ascii = title_ascii;
    }

    public String getArtist_title_ascii() {
        return artist_title_ascii;
    }

    public void setArtist_title_ascii(String artist_title_ascii) {
        this.artist_title_ascii = artist_title_ascii;
    }

    public String getXg_secret() {
        return xg_secret;
    }

    public void setXg_secret(String xg_secret) {
        this.xg_secret = xg_secret;
    }

    public boolean isXg_b_session() {
        return xg_b_session;
    }

    public void setXg_b_session(boolean xg_b_session) {
        this.xg_b_session = xg_b_session;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getChart_list() {
        return chart_list;
    }

    public void setChart_list(String chart_list) {
        this.chart_list = chart_list;
    }

    public int getOrigin() {
        return origin;
    }

    public void setOrigin(int origin) {
        this.origin = origin;
    }

    public int getMusic_type() {
        return music_type;
    }

    public void setMusic_type(int music_type) {
        this.music_type = music_type;
    }

    public int getGenre() {
        return genre;
    }

    public void setGenre(int genre) {
        this.genre = genre;
    }

    public int getType_category() {
        return type_category;
    }

    public void setType_category(int type_category) {
        this.type_category = type_category;
    }
}
