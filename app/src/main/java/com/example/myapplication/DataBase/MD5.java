package com.example.myapplication.DataBase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    private final static String slat = "&%15AAFsac$&";
    public static String getMD5(String value) {
        value = value + slat;
        try {
            // 获取MD5加密后的16字节
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(value.getBytes());
            byte[] messages = md.digest();
            StringBuffer result = new StringBuffer();
            // 每个字节转换为两个16进制数
            for(byte message : messages) {
                result.append(
                        Integer.toHexString(
                                (0x000000FF & message) | 0xFFFFFF00
                        ).substring(6)
                );
            }

            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
