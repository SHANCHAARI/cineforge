#include "cineforge/core/Engine.h"

#include "cineforge/media/ProxyManager.h"
#include "cineforge/render/Renderer.h"
#include "cineforge/timeline/KeyframeManager.h"
#include "cineforge/timeline/Timeline.h"

namespace cineforge {

struct Engine::Impl {
    double previewTimeSeconds = 0.0;
    timeline::Timeline timeline;
    timeline::KeyframeManager keyframes;
    render::Renderer renderer;
    media::ProxyManager proxyManager;

    Impl()
        : proxyManager("", "media/proxies") {}
};

Engine& Engine::instance() {
    static Engine inst;
    return inst;
}

Engine::Engine()
    : impl_(std::make_unique<Impl>()) {}

Engine::~Engine() = default;

bool Engine::loadProjectFromFile(const std::string& /*path*/) {
    // TODO: parse JSON and populate timeline, keyframes, sources, etc.
    return true;
}

bool Engine::loadProjectFromJson(const std::string& /*json*/) {
    // TODO: parse JSON string.
    return true;
}

std::string Engine::saveProjectToJson() const {
    // TODO: serialize current timeline + keyframes + media to JSON.
    return "{}";
}

void Engine::setPreviewTime(double timeSeconds) {
    impl_->previewTimeSeconds = timeSeconds;
}

double Engine::previewTime() const {
    return impl_->previewTimeSeconds;
}

timeline::Timeline& Engine::timeline() {
    return impl_->timeline;
}

const timeline::Timeline& Engine::timeline() const {
    return impl_->timeline;
}

timeline::KeyframeManager& Engine::keyframes() {
    return impl_->keyframes;
}

const timeline::KeyframeManager& Engine::keyframes() const {
    return impl_->keyframes;
}

render::Renderer& Engine::renderer() {
    return impl_->renderer;
}

const render::Renderer& Engine::renderer() const {
    return impl_->renderer;
}

media::ProxyManager& Engine::proxyManager() {
    return impl_->proxyManager;
}

const media::ProxyManager& Engine::proxyManager() const {
    return impl_->proxyManager;
}

} // namespace cineforge

