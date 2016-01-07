package com.iwork.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static String toMD5(String psd) {
        MessageDigest messageDigest;
        StringBuffer md5StrBuff = new StringBuffer();
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(psd.getBytes("UTF-8"));

            byte[] byteArray = messageDigest.digest();
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                } else {
                    md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return md5StrBuff.toString();
    }

    public static String toMD5(byte[] b) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.reset();
        messageDigest.update(b);

        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            md5StrBuff.append(String.format("%02X", byteArray[i]));
        }
        return md5StrBuff.toString();
    }

    public static byte[] readRandomByte(String fileName) {
        byte[] begin = new byte[50 * 1024];// 文件头
        byte[] middle = new byte[50 * 1024];// 中间部分
        byte[] end = new byte[50 * 1024];// 文件尾

        try {
            FileInputStream fis = new FileInputStream(fileName);
            int t = fis.available();
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.skip(-1);
            bis.read(begin, 0, begin.length);

            bis.skip(t / 2);
            bis.read(middle, 0, middle.length);

            bis.skip(t - end.length);
            bis.read(end, 0, end.length);

            bis.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] total = new byte[begin.length + middle.length + end.length];
        for (int i = 0; i < begin.length; i++) {
            total[i] = begin[i];
        }
        for (int i = 0; i < middle.length; i++) {
            total[i + begin.length] = middle[i];
        }
        for (int i = 0; i < end.length; i++) {
            total[i + begin.length + middle.length] = end[i];
        }
        return total;
    }

    /**
     * @param fileName 包含路径的文件名
     */
    public static String toMD5ForFile(String fileName) throws Exception {
        if (TextUtil.isEmpty(fileName)) {
            return fileName;
        }
        FileInputStream fis = new FileInputStream(fileName);
        byte[] buffer = new byte[128 * 1024];

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        int len = 0;
        while ((len = fis.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.flush();
        outStream.close();
        fis.close();

        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.reset();
        messageDigest.update(outStream.toByteArray());
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            md5StrBuff.append(String.format("%02X", byteArray[i]));
        }
        return md5StrBuff.toString();
    }
}
