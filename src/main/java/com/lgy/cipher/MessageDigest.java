package com.lgy.cipher;

import java.security.NoSuchAlgorithmException;

/**
 * @author: Administrator
 * @date: 2023/9/26
 */
public abstract class MessageDigest<T,S> implements Cipher<T,S>{

    public final static String MD5 = "MD5";
    public final static String SHA1 = "SHA1";
    public final static String SHA256 = "SHA-256";
    public final static String SHA512 = "SHA-512";

    protected java.security.MessageDigest md;
    public MessageDigest(String type){
        init(type);
    }

    private void init(String type){
        try {
            md = java.security.MessageDigest.getInstance(type);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public T decode(S s) {
        throw new RuntimeException("can't use this method");
    }
}

