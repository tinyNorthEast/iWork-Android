package com.iwork.utils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DesEncryptUtils {

    // 根据参数生成KEY
    private static Key getKey(String strKey) throws Exception {
        DESKeySpec dks = new DESKeySpec(strKey.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        return keyFactory.generateSecret(dks);
    }

    // 加密String明文输入,String密文输出
    public static String encode(String key, String src) {

        String result = null;
        try {
            byte[] srcByts = src.getBytes("UTF-8");
            byte[] encodes = getEncCode(key, srcByts);
            result = Base64.encode(encodes);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    // 加密以byte[]明文输入,byte[]密文输出
    private static byte[] getEncCode(String key, byte[] srcByts) {

        byte[] result = null;
        try {
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, getKey(key));
            result = cipher.doFinal(srcByts);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    // 解密:以String密文输入,String明文输出
    public static String decode(String key, String src) {

        String result = null;
        try {
            byte[] srcByts = Base64.decode(src);
            byte[] decodes = getDesCode(key, srcByts);
            result = new String(decodes);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    // 解密以byte[]密文输入,以byte[]明文输出
    private static byte[] getDesCode(String key, byte[] srcByts) {

        byte[] result = null;
        try {
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, getKey(key));
            result = cipher.doFinal(srcByts);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
