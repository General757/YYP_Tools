//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyp.tools.utils;

import android.util.Base64;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * Created by generalYan on 2019/10/17.
 */
public class DESUtils {
    private final String DES = "DES";
    private String key;

    public DESUtils(String key) {
        this.key = key;
    }

    public String encrypt(String data) throws Exception {
        byte[] bt = this.encrypt(data.getBytes("utf-8"));
        String strs = Base64Utils.encode(bt);
        return Base64Utils.encode(strs.replace("\n", "").getBytes());
    }

    public String decrypt(String data) throws Exception {
        if (data == null) {
            return null;
        } else {
            byte[] buf = Base64.decode(data, 0);
            buf = Base64.decode(buf, 0);
            byte[] bt = this.decrypt(buf);
            return new String(bt);
        }
    }

    private byte[] encrypt(byte[] data) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(this.key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(1, securekey, sr);
        return cipher.doFinal(data);
    }

    private byte[] decrypt(byte[] data) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(this.key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(2, securekey, sr);
        return cipher.doFinal(data);
    }
}

