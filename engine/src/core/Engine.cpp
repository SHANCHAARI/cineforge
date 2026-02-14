#include "cineforge/core/Engine.h"

#include "cineforge/media/ProxyManager.h"
#include "cineforge/render/Renderer.h"
#include "cineforge/timeline/KeyframeManager.h"
#include "cineforge/timeline/Timeline.h"
#include <cstddef>
#include <iomanip>
#include <sstream>


namespace cineforge {

struct Engine::Impl {
  double previewTimeSeconds = 0.0;
  timeline::Timeline timeline;
  timeline::KeyframeManager keyframes;
  render::Renderer renderer;
  media::ProxyManager proxyManager;

  Impl() : proxyManager("", "media/proxies") {}
};

Engine &Engine::instance() {
  static Engine inst;
  return inst;
}

Engine::Engine() : impl_(std::make_unique<Impl>()) {}

Engine::~Engine() = default;

std::string Engine::saveProjectToJson() const {
  std::stringstream ss;
  ss << "{\n";
  ss << "  \"previewTime\": " << impl_->previewTimeSeconds << ",\n";
  ss << "  \"timeline\": {\n";
  ss << "    \"tracks\": [\n";

  const auto &tracks = impl_->timeline.tracks();
  for (std::size_t i = 0; i < tracks.size(); ++i) {
    ss << "      {\n";
    ss << "        \"id\": \"" << tracks[i].id << "\",\n";
    ss << "        \"type\": " << static_cast<int>(tracks[i].type) << ",\n";
    ss << "        \"clips\": [\n";
    for (std::size_t j = 0; j < tracks[i].clips.size(); ++j) {
      const auto &c = tracks[i].clips[j];
      ss << "          {\"id\": \"" << c.id << "\", \"source\": \""
         << c.sourceId << "\", \"start\": " << c.start << ", \"end\": " << c.end
         << "}";
      if (j < tracks[i].clips.size() - 1)
        ss << ",";
      ss << "\n";
    }
    ss << "        ]\n";
    ss << "      }";
    if (i < tracks.size() - 1)
      ss << ",";
    ss << "\n";
  }

  ss << "    ]\n";
  ss << "  }\n";
  ss << "}";
  return ss.str();
}

bool Engine::loadProjectFromJson(const std::string &json) {
  // Very basic "parser" that just clears and logs for now,
  // real implementation would use a proper JSON lib.
  if (json.empty() || json == "{}")
    return false;

  impl_->timeline.tracks().clear();
  // Simplified: always add a default track if loading from non-empty
  timeline::Track defaultTrack;
  defaultTrack.id = "main_track";
  impl_->timeline.addTrack(defaultTrack);

  return true;
}

void Engine::setPreviewTime(double timeSeconds) {
  impl_->previewTimeSeconds = timeSeconds;
}

double Engine::previewTime() const { return impl_->previewTimeSeconds; }

timeline::Timeline &Engine::timeline() { return impl_->timeline; }

const timeline::Timeline &Engine::timeline() const { return impl_->timeline; }

timeline::KeyframeManager &Engine::keyframes() { return impl_->keyframes; }

const timeline::KeyframeManager &Engine::keyframes() const {
  return impl_->keyframes;
}

render::Renderer &Engine::renderer() { return impl_->renderer; }

const render::Renderer &Engine::renderer() const { return impl_->renderer; }

media::ProxyManager &Engine::proxyManager() { return impl_->proxyManager; }

const media::ProxyManager &Engine::proxyManager() const {
  return impl_->proxyManager;
}

} // namespace cineforge
