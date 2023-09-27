package com.lgy.cipher;


import android.os.Build;
import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author: Administrator
 * @date: 2023/9/26
 */
public class AES_CBC implements Cipher<byte[],String>{
    public final static String NO_Padding = "AES/CBC/NoPadding";
    public final static String PKCS7Padding = "AES/CBC/PKCS7Padding";
    public final static String PKCS5Padding = "AES/CBC/PKCS5Padding";
    private SecretKey keySpec;
    private String mode;
    private IvParameterSpec ivParam;

    public AES_CBC(String mode, String key,byte[] iv){
        this.mode = mode;
        this.ivParam = new IvParameterSpec(iv);
        init(key);
    }


    private void init(String key){
        MessageDigest<byte[],byte[]> messageDigest = new MessageDigest3(MessageDigest.MD5);
        keySpec = new SecretKeySpec(messageDigest.encode(key.getBytes(StandardCharsets.UTF_8)), "AES");
    }

    @androidx.annotation.RequiresApi(api = Build.VERSION_CODES.O)
    public static byte[] generateIVParam(){
        SecureRandom sr = null;
        try {
            sr = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sr.generateSeed(16);
    }

    @Override
    public String encode(byte[] s) {
        try {
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(mode);
            cipher.init(javax.crypto.Cipher.ENCRYPT_MODE,keySpec,ivParam);
            return Base64.encodeToString(cipher.doFinal(s), android.util.Base64.NO_WRAP);
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
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] decode(String s) {
        javax.crypto.Cipher cipher = null;
        try {
            cipher = javax.crypto.Cipher.getInstance(mode);
            cipher.init(javax.crypto.Cipher.DECRYPT_MODE, keySpec,ivParam);
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
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }
}
