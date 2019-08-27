package com.buttongames.butterflycore.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils{
    static {

    }

    public static String genSalt(){
        return StringUtils.getRandomHexString(10);
    }

    public static String getHash(String salt, String password){
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            if(salt!=null){
                md.update(salt.getBytes());
            }else{ return ""; }

            md.update(password.getBytes());

            byte[] bytes = md.digest();

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }

            return sb.toString();

        }catch (NoSuchAlgorithmException e){
            return "";
        }

    }
}
