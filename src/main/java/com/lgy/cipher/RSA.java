package com.lgy.cipher;

import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * @author: Administrator
 * @date: 2023/9/27
 */
public class RSA implements Cipher<byte[],byte[]>{
    // 私钥:
    private String privateKey;
    // 公钥:
    private String publicKey;

    public RSA(String privateKey,String publicKey){
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }


    public static String[] generateKey(){
        String[] keyPair = new String[2];
        // 生成公钥／私钥对:
        KeyPairGenerator kpGen = null;
        try {
            kpGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        kpGen.initialize(1024); //RSA密钥长度必须是64的倍数，在512~65536之间。默认是1024
        KeyPair kp = kpGen.generateKeyPair();

        keyPair[0] = Base64.encodeToString(kp.getPrivate().getEncoded(),Base64.NO_WRAP);
        keyPair[1] = Base64.encodeToString(kp.getPublic().getEncoded(),Base64.NO_WRAP);
        return keyPair;
    }

    /**
     * 获取公钥对象
     *  @param publicKeyBase64
     *  @return
     *  @throws InvalidKeySpecException
     *  @throws NoSuchAlgorithmException
     */
    private PublicKey getPublicKey(String publicKeyBase64)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec =
                new X509EncodedKeySpec(Base64.decode(publicKeyBase64,Base64.NO_WRAP));
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }
    /**
     * 获取私钥对象
     * @param privateKeyBase64
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private PrivateKey getPrivateKey(String privateKeyBase64)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec =
                new PKCS8EncodedKeySpec(Base64.decode(privateKeyBase64,Base64.NO_WRAP));
        //由于加密后的密文都是字节码形式的，我们要以字符串方式保存或传输的话，可以使用Base64编码。
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }


    @Override
    public byte[] encode(byte[] bytes) {
        javax.crypto.Cipher cipher = null;
        try {
            cipher = javax.crypto.Cipher.getInstance("RSA");
            cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, getPublicKey(this.publicKey));
            return cipher.doFinal(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public byte[] decode(byte[] bytes) {
        javax.crypto.Cipher cipher = null;
        try {
            cipher = javax.crypto.Cipher.getInstance("RSA");
            cipher.init(javax.crypto.Cipher.DECRYPT_MODE, getPrivateKey(this.privateKey));
            return cipher.doFinal(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
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
