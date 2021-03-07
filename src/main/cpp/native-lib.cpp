#include <jni.h>
#include <string>

extern "C"
jstring
Java_com_example_bank_model_DisplayAccounts_baseUrlFromJNI(JNIEnv* env, jclass clazz) {
    std::string baseURL = "https://60102f166c21e10017050128.mockapi.io/labbbank";
    return env->NewStringUTF(baseURL.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_bank_model_CreateAccountActivity_baseUrlFromJNI(JNIEnv *env, jclass clazz) {
    std::string baseURL = "https://60102f166c21e10017050128.mockapi.io/labbbank";
    return env->NewStringUTF(baseURL.c_str());
}