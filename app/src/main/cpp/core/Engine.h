#ifndef VIDEOEDITOR_ENGINE_H
#define VIDEOEDITOR_ENGINE_H

#include "../utils/Logger.h"
#include <android/native_window.h>
#include <atomic>
#include <memory>
#include <mutex>
#include <thread>

namespace videoeditor {

class Engine {
public:
  static Engine &getInstance();

  void initialize();
  void shutdown();
  void setWindow(ANativeWindow *window);

private:
  Engine();
  ~Engine();

  void renderLoop();

  bool initialized_;
  ANativeWindow *window_;
  std::mutex windowMutex_;

  std::thread renderThread_;
  std::atomic<bool> isRunning_;
};

} // namespace videoeditor

#endif // VIDEOEDITOR_ENGINE_H
