#include "Engine.h"
#include "TextureRenderer.h"
#include <EGL/egl.h>
#include <GLES3/gl3.h>
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

      // Draw a Quad in the center (Debug Gradient)
      renderer.render(0, 0, 0, 1.0f, 1.0f, 0.0f);

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
