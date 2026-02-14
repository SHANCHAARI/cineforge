#include "Engine.h"
#include "TextureRenderer.h"
#include <EGL/egl.h>
#include <GLES3/gl3.h>
#include <algorithm>
#include <chrono>
#include <mutex>
#include <thread>

namespace videoeditor {

Engine &Engine::getInstance() {
  static Engine instance;
  return instance;
}

Engine::Engine() : initialized_(false), window_(nullptr), isRunning_(false) {}

Engine::~Engine() { shutdown(); }

void Engine::initialize() {
  if (initialized_)
    return;

  LOGI("Initializing Engine");
  isRunning_ = true;
  renderThread_ = std::thread(&Engine::renderLoop, this);

  initialized_ = true;
}

void Engine::shutdown() {
  if (!initialized_)
    return;

  LOGI("Shutting down Engine");
  isRunning_ = false;
  if (renderThread_.joinable()) {
    renderThread_.join();
  }

  initialized_ = false;
}

void Engine::setWindow(ANativeWindow *window) {
  std::lock_guard<std::mutex> lock(windowMutex_);
  if (window_ != nullptr) {
    ANativeWindow_release(window_);
  }
  window_ = window;
  if (window_ != nullptr) {
    ANativeWindow_acquire(window_);
  }
}

void Engine::setColorGrading(float brightness, float contrast,
                             float saturation) {
  brightness_ = brightness;
  contrast_ = contrast;
  saturation_ = saturation;
}

void Engine::setPlayheadMs(long timeMs) { playheadMs_.store(timeMs); }

void Engine::addMediaClip(const std::string &id, const std::string &path,
                          long startTime, long duration) {
  std::lock_guard<std::mutex> lock(clipsMutex_);
  MediaClip clip;
  clip.id = id;
  clip.path = path;
  clip.startTime = startTime;
  clip.duration = duration;
  clip.textureId = 0;
  clip.decoder = std::make_unique<VideoDecoder>(path);
  clip.decoder->initialize();
  clips_.push_back(std::move(clip));

  // Sync with cineforge timeline
  cineforge::timeline::Clip c;
  c.id = id;
  c.sourceId = path;
  c.start = static_cast<double>(startTime);
  c.end = static_cast<double>(startTime + duration);
  timeline_.addClip("default_track", c); // Assuming a default track for now

  LOGI("Added native clip: ID=%s, Path=%s", id.c_str(), path.c_str());
}

void Engine::removeMediaClip(const std::string &id) {
  std::lock_guard<std::mutex> lock(clipsMutex_);
  clips_.erase(std::remove_if(clips_.begin(), clips_.end(),
                              [&id](const MediaClip &c) { return c.id == id; }),
               clips_.end());

  // cineforge::timeline doesn't have removeClip yet in the shown header,
  // but we should ideally sync it if we add it there.

  LOGI("Removed native clip: ID=%s", id.c_str());
}

void Engine::splitClip(const std::string &clipId, long timeMs) {
  std::lock_guard<std::mutex> lock(clipsMutex_);

  // Sync with cineforge timeline
  timeline_.splitClip(clipId, static_cast<double>(timeMs));

  // Update local clips_ to match the split (simplified update)
  auto it =
      std::find_if(clips_.begin(), clips_.end(),
                   [&clipId](const MediaClip &c) { return c.id == clipId; });

  if (it != clips_.end() && timeMs > it->startTime &&
      timeMs < (it->startTime + it->duration)) {
    long firstPartDuration = timeMs - it->startTime;
    long secondPartDuration = it->duration - firstPartDuration;

    MediaClip secondPart;
    secondPart.id = it->id + "_b";
    secondPart.path = it->path;
    secondPart.startTime = timeMs;
    secondPart.duration = secondPartDuration;
    secondPart.textureId = 0; // Will be generated on first render
    secondPart.decoder = std::make_unique<VideoDecoder>(it->path);
    secondPart.decoder->initialize();

    it->duration = firstPartDuration;
    clips_.insert(it + 1, std::move(secondPart));

    LOGI("Split native clip: ID=%s at %ldms", clipId.c_str(), timeMs);
  }
}

void Engine::moveClip(const std::string &clipId, long newStartTimeMs) {
  std::lock_guard<std::mutex> lock(clipsMutex_);

  // Sync with cineforge timeline
  timeline_.moveClip(clipId, static_cast<double>(newStartTimeMs));

  // Update local clips_
  for (auto &c : clips_) {
    if (c.id == clipId) {
      c.startTime = newStartTimeMs;
      LOGI("Moved native clip: ID=%s to %ldms", clipId.c_str(), newStartTimeMs);
      return;
    }
  }
}

void Engine::renderLoop() {
  LOGI("Render loop started");

  EGLDisplay display = eglGetDisplay(EGL_DEFAULT_DISPLAY);
  eglInitialize(display, nullptr, nullptr);

  const EGLint attribs[] = {EGL_RENDERABLE_TYPE,
                            EGL_OPENGL_ES3_BIT,
                            EGL_SURFACE_TYPE,
                            EGL_WINDOW_BIT,
                            EGL_BLUE_SIZE,
                            8,
                            EGL_GREEN_SIZE,
                            8,
                            EGL_RED_SIZE,
                            8,
                            EGL_NONE};

  EGLConfig config;
  EGLint numConfigs;
  eglChooseConfig(display, attribs, &config, 1, &numConfigs);

  const EGLint contextAttribs[] = {EGL_CONTEXT_CLIENT_VERSION, 3, EGL_NONE};
  EGLContext context =
      eglCreateContext(display, config, EGL_NO_CONTEXT, contextAttribs);

  EGLSurface surface = EGL_NO_SURFACE;
  ANativeWindow *currentWindow = nullptr;

  TextureRenderer renderer;
  bool rendererInitialized = false;

  while (isRunning_) {
    // Check for window change
    {
      std::lock_guard<std::mutex> lock(windowMutex_);
      if (currentWindow != window_) {
        currentWindow = window_;
        if (surface != EGL_NO_SURFACE) {
          eglDestroySurface(display, surface);
          surface = EGL_NO_SURFACE;
          rendererInitialized = false; // Surface changed, strictly context is
                                       // same but good to know
        }
        if (currentWindow != nullptr) {
          surface =
              eglCreateWindowSurface(display, config, currentWindow, nullptr);
        }
      }
    }

    if (surface != EGL_NO_SURFACE) {
      if (eglMakeCurrent(display, surface, surface, context) == EGL_FALSE) {
        // Surface might be lost
        continue;
      }

      if (!rendererInitialized) {
        renderer.initialize();
        rendererInitialized = true;
        LOGI("Renderer Initialized");
      }

      // Render Frame
      // Background: Dark Gray (Editor BG)
      glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
      glClear(GL_COLOR_BUFFER_BIT);

      // Render clips from the timeline
      {
        std::lock_guard<std::mutex> lock(clipsMutex_);
        renderer.setColorGrading(brightness_, contrast_, saturation_);

        if (clips_.empty()) {
          // If no clips, draw a debug quad (placeholder)
          renderer.render(0, 0, 0, 1.0f, 1.0f, 0.0f);
        } else {
          const long currentTime = playheadMs_.load();

          for (auto &clip : clips_) {
            const long clipStart = clip.startTime;
            const long clipEnd = clip.startTime + clip.duration;
            if (currentTime >= clipStart && currentTime <= clipEnd) {
              if (clip.decoder) {
                const int64_t localUs =
                    static_cast<int64_t>(currentTime - clipStart) * 1000;
                clip.decoder->seekToUs(localUs);
                clip.decoder->decodeNext();

                const auto *rgba = clip.decoder->lastFrameRgba();
                if (rgba) {
                  if (clip.textureId == 0) {
                    glGenTextures(1, &clip.textureId);
                    glBindTexture(GL_TEXTURE_2D, clip.textureId);
                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,
                                    GL_LINEAR);
                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER,
                                    GL_LINEAR);
                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S,
                                    GL_CLAMP_TO_EDGE);
                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T,
                                    GL_CLAMP_TO_EDGE);
                  } else {
                    glBindTexture(GL_TEXTURE_2D, clip.textureId);
                  }

                  glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, clip.decoder->width(),
                               clip.decoder->height(), 0, GL_RGBA,
                               GL_UNSIGNED_BYTE, rgba);
                }
              }

              renderer.render(clip.textureId, 0, 0, 1.0f, 1.0f, 0.0f);
              break;
            }
          }
        }
      }

      eglSwapBuffers(display, surface);
    } else {
      // Sleep to avoid burning CPU when no surface
      std::this_thread::sleep_for(std::chrono::milliseconds(16));
    }
  }

  if (surface != EGL_NO_SURFACE)
    eglDestroySurface(display, surface);
  if (context != EGL_NO_CONTEXT)
    eglDestroyContext(display, context);
  if (display != EGL_NO_DISPLAY)
    eglTerminate(display);

  LOGI("Render loop stopped");
}

} // namespace videoeditor
