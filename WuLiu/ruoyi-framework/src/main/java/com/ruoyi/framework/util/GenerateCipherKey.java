package com.ruoyi.framework.util;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

/**
 * @Description: shrio记住我默认秘钥修改
 * @Author zheng
 * @Date 2021/7/6 16:41
 * @Version 1.0
 */
public class GenerateCipherKey {

    public static byte[] generateNewKey() {
        KeyGenerator kg;
        try {
            kg = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException var5) {
            String msg = "Unable to acquire AES algorithm.  This is required to function.";
            throw new IllegalStateException(msg, var5);
        }
        kg.init(128);
        SecretKey key = kg.generateKey();
        return key.getEncoded();
    }
}
