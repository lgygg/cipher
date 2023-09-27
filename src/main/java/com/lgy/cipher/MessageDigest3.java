package com.lgy.cipher;

/**
 * @author: Administrator
 * @date: 2023/9/26
 */
public class MessageDigest3 extends MessageDigest<byte[], byte[]> {

    public MessageDigest3(String type){
        super(type);
    }


    @Override
    public byte[] encode(byte[] bytes) {
        md.update(bytes);
        return md.digest();
    }
}
