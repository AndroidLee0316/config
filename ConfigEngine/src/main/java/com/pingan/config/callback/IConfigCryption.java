package com.pingan.config.callback;

public interface IConfigCryption {
    public String encrypt(String content);
    public String decrypt(String sources);
}
