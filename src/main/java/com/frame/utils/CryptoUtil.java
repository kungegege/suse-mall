package com.frame.utils;

import com.google.common.base.Function;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/5/29
 * Time: 19:20
 * Description: 加密工具类
 */
public class CryptoUtil {

    /**
     * md5散列
     * @param str
     * @return
     */
    public static String md5Encode(String str) {
        return md5Encode(str, null);
    }


    /**
     * @param str
     * @param handle  对str进行相关操作
     * @return
     */
    public static String md5Encode(String str, Function<String, String> handle) {
        if (handle != null) {  str = handle.apply(str);}
        byte[] digest = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            digest = md5.digest(str.getBytes("utf-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new BigInteger(1, digest).toString(16);
    }

}