#include <jni.h>
#include <android/log.h>
#include "include/FLAC/stream_encoder.h"
#include <string.h>

#define  LOG_TAG    "main"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

FLAC__StreamEncoder * m_encoder;

void copyBuffer(FLAC__int32* outbuf, jshort* shortBuffer, int bufferSize) {
    FLAC__int16 *int16Buffer = reinterpret_cast<FLAC__int16 *>(shortBuffer);
    for (int i = 0; i < bufferSize / sizeof(FLAC__int16); ++i) {
        FLAC__int16 cur = int16Buffer[i];
        // Convert sized sample to int32
        outbuf[i] = cur;
    }
}

char * convert_jstring_path(JNIEnv * env, jstring input)
{
    jboolean copy = false;
    char const * str = env->GetStringUTFChars(input, &copy);
    if (NULL == str) {
        // OutOfMemoryError has already been thrown here.
        return NULL;
    }

    char * ret = strdup(str);
    env->ReleaseStringUTFChars(input, str);
    return ret;
}

extern "C" {

jboolean Java_com_ryeeeeee_faceandflacdemo_flac_FlacEncoder_init(JNIEnv * env, jclass, jstring outfileString, jint sampleRate,
                                                 jint channels, jint bitsPerSample)
{
    if (!outfileString) {
        LOGD("Invalid file");
        return false;
    }

    // Try to create the encoder instance
    m_encoder = FLAC__stream_encoder_new();
    if (!m_encoder) {
        LOGD("Could not create FLAC__StreamEncoder!");
        return false;
    }

    // Try to initialize the encoder.
    FLAC__bool ok = true;
    ok &= FLAC__stream_encoder_set_sample_rate(m_encoder, sampleRate);
    ok &= FLAC__stream_encoder_set_channels(m_encoder, channels);
    ok &= FLAC__stream_encoder_set_bits_per_sample(m_encoder, bitsPerSample);
    ok &= FLAC__stream_encoder_set_verify(m_encoder, true);
    if (!ok) {
        LOGD("Could not set up FLAC__StreamEncoder with the given parameters!");
        return false;
    }

    // Try initializing the file stream.
    char * outfile = convert_jstring_path(env, outfileString);
    FLAC__StreamEncoderInitStatus init_status = FLAC__stream_encoder_init_file(m_encoder, outfile, NULL, NULL);

    if (FLAC__STREAM_ENCODER_INIT_STATUS_OK != init_status) {
        LOGD("Could not initialize FLAC__StreamEncoder for the given file!");
        return false;
    }

    return true;
}

void Java_com_ryeeeeee_faceandflacdemo_flac_FlacEncoder_deinit(JNIEnv * env, jclass)
{
    if(!m_encoder) {
        LOGD("Please init encoder first");
        return;
    }
    FLAC__stream_encoder_delete(m_encoder);
}

void Java_com_ryeeeeee_faceandflacdemo_flac_FlacEncoder_write(JNIEnv * env, jclass, jshortArray buffer, jint bufferSize)
{
    if(!m_encoder) {
        LOGD("Please init encoder first");
        return;
    }

    int bufsize32 = bufferSize / 2;
    FLAC__int32* int32Buffer = new FLAC__int32[bufsize32];
    jshort * jshort = env->GetShortArrayElements(buffer, 0);
    copyBuffer(int32Buffer, jshort, bufferSize);

    // Encode!
    FLAC__bool ok = FLAC__stream_encoder_process_interleaved(m_encoder, int32Buffer, bufsize32);
    if(!ok) {
        // todo return to java
        LOGD("Failed to write");
    }
}

void Java_com_ryeeeeee_faceandflacdemo_flac_FlacEncoder_flush(JNIEnv * env, jclass)
{
    if(!m_encoder) {
        LOGD("Please init encoder first");
        return;
    }
    FLAC__bool ok = FLAC__stream_encoder_finish(m_encoder);
    if(!ok) {
        // todo return to java
        LOGD("Failed to flush");
    }
}
}