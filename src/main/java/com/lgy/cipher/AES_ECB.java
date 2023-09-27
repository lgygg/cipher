package com.lgy.cipher;

import android.annotation.SuppressLint;
import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author: Administrator
 * @date: 2023/9/26
 */
public class AES_ECB implements Cipher<byte[],String>{
    public final static String NO_Padding = "AES/ECB/NoPadding";
    public final static String PKCS7Padding = "AES/ECB/PKCS7Padding";
    public final static String PKCS5Padding = "AES/ECB/PKCS5Padding";
    private SecretKey keySpec;
    private String mode;

    public AES_ECB(String mode,String key){
        this.mode = mode;
        init(key);
    }

    private void init(String key){
        MessageDigest<byte[],byte[]> messageDigest = new MessageDigest3(MessageDigest.MD5);
        keySpec = new SecretKeySpec(messageDigest.encode(key.getBytes(StandardCharsets.UTF_8)), "AES");
    }
    @SuppressLint("NewApi")
    @Override
    public String encode(byte[] s) {
        try {
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(mode);
            cipher.init(javax.crypto.Cipher.ENCRYPT_MODE,keySpec);
            return Base64.encodeToString(cipher.doFinal(s),Base64.NO_WRAP);
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] decode(String s) {
        javax.crypto.Cipher cipher = null;
        try {
            cipher = javax.crypto.Cipher.getInstance(mode);
            cipher.init(javax.crypto.Cipher.DECRYPT_MODE, keySpec);
            return cipher.doFinal(Base64.decode(s, android.util.Base64.NO_WRAP));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
