package com.me.security;

import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName MD5Utils
 * @Description TODO
 * @Author noname
 * @Data
 * @Version 1.0
 **/
/**
 * Md5Utils 类用于对密码进行加密，以保证密码的安全性。
 */
public class Md5Utils {
    
    /**
     * EncoderByMd5 方法用于对字符串进行 MD5 加密。
     * @param str 要加密的字符串。
     * @return 经过 MD5 加密后的字符串。
     * @throws NoSuchAlgorithmException 当指定的加密算法不存在时抛出此异常。
     * @throws UnsupportedEncodingException 当指定的字符集不被支持时抛出此异常。
     */
    private static String EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // 获取 MD5 实例
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        // 创建 BASE64Encoder 实例
        BASE64Encoder base64Encoder=new BASE64Encoder();

        // 对字符串进行 MD5 加密，并将加密后的字节数组转换为字符串
        String newstr=base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }

    /**
     * EncodePassword 方法用于对密码进行多次 MD5 加密。
     * @param password 要加密的密码。
     * @return 经过多次 MD5 加密后的字符串。
     * @throws UnsupportedEncodingException 当指定的字符集不被支持时抛出此异常。
     * @throws NoSuchAlgorithmException 当指定的加密算法不存在时抛出此异常。
     */
    public static String EncodePassword(String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        // 将密码和字符串 "xq" 拼接起来
        String str=password+"xq";
        // 对拼接后的字符串进行三次 MD5 加密
        return EncoderByMd5(EncoderByMd5(EncoderByMd5(str)));
    }

}
