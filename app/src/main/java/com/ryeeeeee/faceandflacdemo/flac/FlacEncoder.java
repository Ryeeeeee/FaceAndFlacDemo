package com.ryeeeeee.faceandflacdemo.flac;

import java.nio.ByteBuffer;

/**
 * Created by Ryeeeeee on 1/14/16.
 */
public class FlacEncoder {
    static {
        System.loadLibrary("app");
    }

    public static native boolean init(String outfile, int sampleRate, int channels, int bitsPerSample);
    public static native void deinit();
    public static native void write(short[] buffer, int bufferSize);
    public static native void flush();
}
