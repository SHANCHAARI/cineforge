#pragma once

#include <mutex>
#include <string>
#include <unordered_map>

#include "cineforge/media/MediaSource.h"

namespace cineforge::media {

struct ProxyInfo {
    std::string proxyPath;
    int width = 0;
    int height = 0;
    std::string codec;
    bool valid = false;
};

/**
 * Minimal proxy manager skeleton. The concrete implementation for now
 * will be a simple FFmpeg‑CLI invoker; later this can be replaced by a
 * proper libavfilter pipeline or platform‑specific transcoder.
 */
class ProxyManager {
public:
    ProxyManager(const std::string& ffmpegBinDir,
                 const std::string& proxyRoot);

    ProxyInfo ensureProxy(const MediaSource& src, int targetWidth);
    ProxyInfo getProxy(const std::string& sourceId) const;
    void setProxy(const std::string& sourceId, const ProxyInfo& info);

private:
    std::string ffmpegBinDir_;
    std::string proxyRoot_;

    mutable std::mutex mtx_;
    std::unordered_map<std::string, ProxyInfo> map_;
};

} // namespace cineforge::media

