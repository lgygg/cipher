package com.lgy.cipher;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

/**
 * @author: Administrator
 * @date: 2023/9/26
 */
public class MessageDigest2 extends MessageDigest<String,String>{

    public MessageDigest2(String type) {
        super(type);
    }

    @Override
    public String encode(String s) {
        md.update(s.getBytes(StandardCharsets.UTF_8));
        byte[] result = md.digest();
        return new BigInteger(1,result).toString(16);
    }
}
