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

} // extern "C"
