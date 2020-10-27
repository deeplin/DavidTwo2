package com.david.core.enumeration;

/**
 * author: Ling Lin
 * created on: 2017/12/26 15:28
 * email: 10525677@qq.com
 * description:
 */
public enum Spo2AverageTimeEnum {

    Zero("2-4 s"), One("4-6 s"), Two("8 s"), Three("10 s"), Four("12 s"), Five("14 s"), Six("16 s");

    private final String text;

    Spo2AverageTimeEnum(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}