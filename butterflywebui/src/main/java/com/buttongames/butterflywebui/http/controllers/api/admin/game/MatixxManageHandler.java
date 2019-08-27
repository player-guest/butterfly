package com.buttongames.butterflywebui.http.controllers.api.admin.game;

import com.buttongames.butterflycore.util.JSONUtil;
import com.buttongames.butterflycore.xml.XmlUtils;
import com.buttongames.butterflydao.hibernate.dao.impl.gdmatixx.MatixxMusicDao;
import com.buttongames.butterflymodel.model.gdmatixx.matixxMusic;
import com.buttongames.butterflywebui.http.controllers.api.game.MatixxHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import spark.Request;
import spark.Response;

public class MatixxManageHandler {

    private final Logger LOG = LogManager.getLogger(MatixxManageHandler.class);

    final MatixxMusicDao matixxMusicDao;

    public MatixxManageHandler(MatixxMusicDao matixxMusicDao) {
        this.matixxMusicDao = matixxMusicDao;
    }

    public Object handleRequest(final String function, final Request request, final Response response){

        if (function.equals("set_musiclist")) {
            return handleSetMusiclistRequest(request, response);
        }

        return null;
    }

    private Object handleSetMusiclistRequest(final Request request, final Response response){

        byte[] mdb_mtfile = request.bodyAsBytes();
        Element mdb = XmlUtils.byteArrayToXmlFile(mdb_mtfile);
        NodeList mdb_datas = XmlUtils.nodesAtPath(mdb,"/mdb_data");

        LOG.info("Adding "+mdb_datas.getLength()+" records");

        for(int j=0;j<mdb_datas.getLength();j++) {

            Element item = (Element) mdb_datas.item(j);
            int music_id = XmlUtils.intAtChild(item, "music_id");

            boolean isNew = false;
            matixxMusic musicObj = matixxMusicDao.findByMusicId(music_id);
            if(musicObj==null) {
                isNew = true;
                musicObj = new matixxMusic();
                musicObj.setMusicid(music_id);

                //omnimix
                musicObj.setList_type(2);

                String[] l1 = XmlUtils.strAtChild(item, "xg_diff_list").split(" ");

                String guitar_diff = l1[0] + " " + l1[1] + " " + l1[2] + " " + l1[3] + " " + l1[4];
                String bass_diff = l1[10] + " " + l1[11] + " " + l1[12] + " " + l1[13] + " " + l1[14];
                String drum_diff = l1[5] + " " + l1[6] + " " + l1[7] + " " + l1[8] + " " + l1[9];

                musicObj.setGuitar_diff(guitar_diff);
                musicObj.setBass_diff(bass_diff);
                musicObj.setDrum_diff(drum_diff);

                musicObj.setIs_hot(false);

                musicObj.setContain_stat(XmlUtils.strAtChild(item, "contain_stat"));

                musicObj.setFirst_ver(XmlUtils.strAtChild(item, "first_ver"));

                musicObj.setFirst_classic_ver(XmlUtils.strAtChild(item, "first_classic_ver"));

                musicObj.setB_long(XmlUtils.boolAtChild(item, "b_long"));

                musicObj.setB_eemail(XmlUtils.boolAtChild(item, "b_eemall"));

                musicObj.setBpm(XmlUtils.intAtChild(item, "bpm"));

                musicObj.setBpm2(XmlUtils.intAtChild(item, "bpm2"));

                String title_name = XmlUtils.strAtChild(item, "title_name");
                musicObj.setTitle_name(title_name);

                musicObj.setTitle_ascii(XmlUtils.strAtChild(item, "title_ascii"));

                musicObj.setArtist_title_ascii(XmlUtils.strAtChild(item, "artist_title_ascii"));

                musicObj.setXg_secret(XmlUtils.strAtChild(item, "xg_secret"));

                musicObj.setXg_b_session(XmlUtils.boolAtChild(item, "xg_b_session"));

                musicObj.setSpeed(XmlUtils.intAtChild(item, "speed"));

                musicObj.setChart_list(XmlUtils.strAtChild(item, "chart_list"));

                musicObj.setOrigin(XmlUtils.intAtChild(item, "origin"));

                musicObj.setMusic_type(XmlUtils.intAtChild(item, "music_type"));

                musicObj.setGenre(XmlUtils.intAtChild(item, "genre"));

                musicObj.setType_category(XmlUtils.intAtChild(item, "type_category"));

                if (isNew) {
                    matixxMusicDao.create(musicObj);
                    LOG.info("Added " + title_name);
                } else {
                    matixxMusicDao.update(musicObj);
                    LOG.info("Updated " + title_name);
                }
            }

        }
        return JSONUtil.successMsg("OK");
    }
}
