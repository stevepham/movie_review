#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL Java_com_ht117_data_source_RemoteConfig_getAppId(JNIEnv* env, jobject thiz) {
    std::string appId = "55957fcf3ba81b137f8fc01ac5a31fb5";
    return env->NewStringUTF(appId.c_str());
}
