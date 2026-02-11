#include "Logger.h"
#include <chrono>

namespace videoeditor {

PerformanceTimer::PerformanceTimer(const char *name) : name_(name) {
  auto now = std::chrono::high_resolution_clock::now();
  startTime_ = std::chrono::duration_cast<std::chrono::microseconds>(
                   now.time_since_epoch())
                   .count();
}

PerformanceTimer::~PerformanceTimer() {
  auto now = std::chrono::high_resolution_clock::now();
  long long endTime = std::chrono::duration_cast<std::chrono::microseconds>(
                          now.time_since_epoch())
                          .count();

  long long elapsed = endTime - startTime_;

  if (elapsed > 16000) { // > 16ms (slower than 60fps)
    LOGW("PERF: %s took %lld µs (%.2f ms)", name_, elapsed, elapsed / 1000.0);
  } else {
    LOGD("PERF: %s took %lld µs", name_, elapsed);
  }
}

} // namespace videoeditor
