#pragma once

#include <memory>
#include <string>

namespace cineforge {

namespace timeline {
class Timeline;
class KeyframeManager;
} // namespace timeline

namespace render {
class Renderer;
} // namespace render

namespace media {
class ProxyManager;
} // namespace media

namespace exporter {
class Exporter;
} // namespace exporter

/**
 * Core cross‑platform engine facade.
 *
 * This is intentionally minimal for now: it provides a singleton-style
 * access point that higher-level UIs (Android, desktop, web) can talk to
 * via a thin C API or JNI/FFI layer.
 */
class Engine {
public:
    static Engine& instance();

    // Project lifecycle
    bool loadProjectFromFile(const std::string& path);
    bool loadProjectFromJson(const std::string& json);
    std::string saveProjectToJson() const;

    // Playback / preview control
    void setPreviewTime(double timeSeconds);
    double previewTime() const;

    // Accessors to subsystems
    timeline::Timeline& timeline();
    const timeline::Timeline& timeline() const;

    timeline::KeyframeManager& keyframes();
    const timeline::KeyframeManager& keyframes() const;

    render::Renderer& renderer();
    const render::Renderer& renderer() const;

    media::ProxyManager& proxyManager();
    const media::ProxyManager& proxyManager() const;

private:
    Engine();
    ~Engine();

    Engine(const Engine&) = delete;
    Engine& operator=(const Engine&) = delete;

    struct Impl;
    std::unique_ptr<Impl> impl_;
};

} // namespace cineforge

