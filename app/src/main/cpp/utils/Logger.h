#ifndef VIDEOEDITOR_LOGGER_H
#define VIDEOEDITOR_LOGGER_H

#include <android/log.h>

// Log tag for all native logging
#define LOG_TAG "VideoEditorNative"

// Logging macros
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL, LOG_TAG, __VA_ARGS__)

// Assert with logging
#define LOG_ASSERT(condition, ...)                                             \
  do {                                                                         \
    if (!(condition)) {                                                        \
      LOGE("Assertion failed: " #condition);                                   \
      LOGE(__VA_ARGS__);                                                       \
      __android_log_assert(#condition, LOG_TAG, __VA_ARGS__);                  \
    }                                                                          \
  } while (0)

// Performance timing
namespace videoeditor {

class PerformanceTimer {
public:
  PerformanceTimer(const char *name);
  ~PerformanceTimer();

private:
  const char *name_;
  long long startTime_;
};

// Usage: PERF_TIMER("RenderFrame");
#define PERF_TIMER(name) videoeditor::PerformanceTimer timer(name)

} // namespace videoeditor

#endif // VIDEOEDITOR_LOGGER_H
