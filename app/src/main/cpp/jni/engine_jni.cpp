#include "core/Engine.h"
#include "utils/Logger.h"
#include <android/log.h>
#include <android/native_window_jni.h>
#include <jni.h>

extern "C" {

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
  JNIEnv *env;
  if (vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
    return JNI_ERR;
  }
  LOGI("VideoEditor Native Engine loaded successfully");
  return JNI_VERSION_1_6;
}

JNIEXPORT void JNICALL
Java_com_videoeditor_pro_app_VideoEditorApplication_initializeNativeEngine(
    JNIEnv *env, jobject /* this */) {
  LOGI("Initializing Native Video Editor Engine");
  videoeditor::Engine::getInstance().initialize();
}

/**
 * Called when the SurfaceView is created/changed
 */
JNIEXPORT void JNICALL
Java_com_videoeditor_pro_presentation_editor_NativeBridge_nativeSetSurface(
    JNIEnv *env, jobject /* this */, jobject surface) {

  ANativeWindow *window = ANativeWindow_fromSurface(env, surface);
  if (window == nullptr) {
    LOGE("Failed to get ANativeWindow from surface");
    return;
  }

  LOGI("Setting native window surface");
  videoeditor::Engine::getInstance().setWindow(window);
}

/**
 * Called when the SurfaceView is destroyed
 */
JNIEXPORT void JNICALL
Java_com_videoeditor_pro_presentation_editor_NativeBridge_nativeReleaseSurface(
    JNIEnv *env, jobject /* this */) {

  LOGI("Releasing native window surface");
  videoeditor::Engine::getInstance().setWindow(nullptr);
}

/**
 * Called when the SurfaceView is destroyed
 */
JNIEXPORT void JNICALL
Java_com_videoeditor_pro_presentation_editor_NativeBridge_nativeSetColorGrading(
    JNIEnv *env, jobject /* this */, jfloat brightness, jfloat contrast,
    jfloat saturation) {
  // Assuming Engine handles delegating this to the renderer
  videoeditor::Engine::getInstance().setColorGrading(brightness, contrast,
                                                     saturation);
  LOGI("Setting color grading: B=%.2f, C=%.2f, S=%.2f", brightness, contrast,
       saturation);
}

JNIEXPORT void JNICALL
Java_com_videoeditor_pro_presentation_editor_NativeBridge_nativeSetPlayheadMs(
    JNIEnv *env, jobject /* this */, jlong timeMs) {
  videoeditor::Engine::getInstance().setPlayheadMs(static_cast<long>(timeMs));
}

JNIEXPORT void JNICALL
Java_com_videoeditor_pro_presentation_editor_NativeBridge_nativeAddMediaClip(
    JNIEnv *env, jobject /* this */, jstring id, jstring path,
    jlong startTimeMs, jlong durationMs) {
  const char *nativeId = env->GetStringUTFChars(id, nullptr);
  const char *nativePath = env->GetStringUTFChars(path, nullptr);

  videoeditor::Engine::getInstance().addMediaClip(nativeId, nativePath,
                                                  startTimeMs, durationMs);

  env->ReleaseStringUTFChars(id, nativeId);
  env->ReleaseStringUTFChars(path, nativePath);
}

JNIEXPORT void JNICALL
Java_com_videoeditor_pro_presentation_editor_NativeBridge_nativeRemoveMediaClip(
    JNIEnv *env, jobject /* this */, jstring id) {
  const char *nativeId = env->GetStringUTFChars(id, nullptr);
  videoeditor::Engine::getInstance().removeMediaClip(nativeId);
  env->ReleaseStringUTFChars(id, nativeId);
}

JNIEXPORT void JNICALL
Java_com_videoeditor_pro_presentation_editor_NativeBridge_nativeSplitClip(
    JNIEnv *env, jobject /* this */, jstring id, jlong timeMs) {
  const char *nativeId = env->GetStringUTFChars(id, nullptr);
  videoeditor::Engine::getInstance().splitClip(nativeId,
                                               static_cast<long>(timeMs));
  env->ReleaseStringUTFChars(id, nativeId);
}

JNIEXPORT void JNICALL
Java_com_videoeditor_pro_presentation_editor_NativeBridge_nativeMoveClip(
    JNIEnv *env, jobject /* this */, jstring id, jlong newStartTimeMs) {
  const char *nativeId = env->GetStringUTFChars(id, nullptr);
  videoeditor::Engine::getInstance().moveClip(
      nativeId, static_cast<long>(newStartTimeMs));
  env->ReleaseStringUTFChars(id, nativeId);
}

} // extern "C"
