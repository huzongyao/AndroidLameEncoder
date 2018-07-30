#include <jni.h>
#include <android/log.h>

#include "lame.h"

#define JNI_FUNC(f) Java_com_hzy_lame_LameEncoder_##f

#ifdef LOG_ENABLE
#define LOG_TAG "LAME.NATIVE"
#define LOGI(...) do { __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__); } while(0)
#define LOGD(...) do { __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__); } while(0)
#define LOGE(...) do { __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__); } while(0)
#else
#define LOGI(...) do { } while(0)
#define LOGD(...) do { } while(0)
#define LOGE(...) do { } while(0)
#endif


static lame_global_flags *glf = NULL;

JNIEXPORT void
JNICALL JNI_FUNC(init)(JNIEnv *env, jclass cls, jint inSamplerate, jint outChannel,
                       jint outSamplerate, jint outBitrate, jint quality) {
    if (glf != NULL) {
        lame_close(glf);
        glf = NULL;
    }
    LOGD("native init(inSamplerate[%d], outChannel[%d], outSamplerate[%d], outBitrate[%d], quality[%d])",
         inSamplerate, outChannel, outSamplerate, outBitrate, quality);
    glf = lame_init();
    lame_set_in_samplerate(glf, inSamplerate);
    lame_set_num_channels(glf, outChannel);
    lame_set_out_samplerate(glf, outSamplerate);
    lame_set_brate(glf, outBitrate);
    lame_set_quality(glf, quality);
    lame_init_params(glf);
}

JNIEXPORT jint JNICALL
JNI_FUNC(encode)(JNIEnv *env, jclass cls, jshortArray buffer_l, jshortArray buffer_r, jint samples,
                 jbyteArray mp3buf) {
    jshort *j_buffer_l = (*env)->GetShortArrayElements(env, buffer_l, NULL);
    jshort *j_buffer_r = (*env)->GetShortArrayElements(env, buffer_r, NULL);
    const jsize mp3buf_size = (*env)->GetArrayLength(env, mp3buf);
    jbyte *j_mp3buf = (*env)->GetByteArrayElements(env, mp3buf, NULL);
    LOGD("native encode buffer size[%d]", mp3buf_size);

    int result = lame_encode_buffer(glf, j_buffer_l, j_buffer_r, samples,
                                    (unsigned char *) j_mp3buf, mp3buf_size);

    (*env)->ReleaseShortArrayElements(env, buffer_l, j_buffer_l, 0);
    (*env)->ReleaseShortArrayElements(env, buffer_r, j_buffer_r, 0);
    (*env)->ReleaseByteArrayElements(env, mp3buf, j_mp3buf, 0);

    return result;
}

JNIEXPORT jint
JNICALL JNI_FUNC(flush)(JNIEnv *env, jclass cls, jbyteArray mp3buf) {
    const jsize mp3buf_size = (*env)->GetArrayLength(env, mp3buf);
    jbyte *j_mp3buf = (*env)->GetByteArrayElements(env, mp3buf, NULL);
    LOGD("native flush buffer size[%d]", mp3buf_size);

    int result = lame_encode_flush(glf, (unsigned char *) j_mp3buf, mp3buf_size);

    (*env)->ReleaseByteArrayElements(env, mp3buf, j_mp3buf, 0);
    return result;
}

JNIEXPORT void
JNICALL JNI_FUNC(close)(JNIEnv *env, jclass cls) {
    LOGD("native close");
    lame_close(glf);
    glf = NULL;
}
