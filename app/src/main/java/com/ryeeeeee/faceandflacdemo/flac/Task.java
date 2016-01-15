package com.ryeeeeee.faceandflacdemo.flac;

/**
 * Created by Ryeeeeee on 1/14/16.
 */
public class Task {
    public short[] rawData;
    public int readSize;

    public Task(short[] rawData, int readSize) {
        this.rawData = rawData;
        this.readSize = readSize;
    }
}
