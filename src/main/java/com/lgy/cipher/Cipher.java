package com.lgy.cipher;

/**
 *
 * @author: Administrator
 * @date: 2023/9/26
 */
public interface Cipher<T,S> {
    S encode(T t);
    T decode(S s);
}