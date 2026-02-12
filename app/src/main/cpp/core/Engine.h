#ifndef VIDEOEDITOR_ENGINE_H
#define VIDEOEDITOR_ENGINE_H

#include "../utils/Logger.h"
#include "../video/VideoDecoder.h"
#include <algorithm>
#include <android/native_window.h>
#include <atomic>
#include <cineforge/timeline/Timeline.h>
#include <cstdint>
#include <memory>
#include <mutex>
#include <string>
#include <thread>
#include <vector>

namespace videoeditor {

class Engine {
public:
  static Engine &getInstance();

  void initialize();
  void shutdown();
  void setWindow(ANativeWindow *window);

  void setPlayheadMs(long timeMs);
  long playheadMs() const { return playheadMs_.load(); }

  struct MediaClip {
    std::string id;
    std::string path;
    long startTime;
    long duration;
    uint32_t textureId = 0;
    std::unique_ptr<VideoDecoder> decoder;
  };

  void addMediaClip(const std::string &id, const std::string &path,
                    long startTime, long duration);
  void removeMediaClip(const std::string &id);
  void splitClip(const std::string &clipId, long timeMs);
  void moveClip(const std::string &clipId, long newStartTimeMs);

  void setColorGrading(float brightness, float contrast, float saturation);

private:
  Engine();
  ~Engine();

  void renderLoop();

  bool initialized_;
  ANativeWindow *window_;
  std::mutex windowMutex_;

  std::thread renderThread_;
  std::atomic<bool> isRunning_;

  std::atomic<long> playheadMs_{0};

  std::vector<MediaClip> clips_;
  std::mutex clipsMutex_;

  cineforge::timeline::Timeline timeline_;

  std::atomic<float> brightness_{1.0f};
  std::atomic<float> contrast_{1.0f};
  std::atomic<float> saturation_{1.0f};
};

} // namespace videoeditor

#endif // VIDEOEDITOR_ENGINE_H
