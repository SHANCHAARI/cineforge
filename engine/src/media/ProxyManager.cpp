#include "cineforge/media/ProxyManager.h"

#include <cstdlib>
#include <filesystem>

namespace cineforge::media {

namespace {
namespace fs = std::filesystem;
}

ProxyManager::ProxyManager(const std::string& ffmpegBinDir,
                           const std::string& proxyRoot)
    : ffmpegBinDir_(ffmpegBinDir), proxyRoot_(proxyRoot) {}

ProxyInfo ProxyManager::ensureProxy(const MediaSource& src, int targetWidth) {
    std::lock_guard<std::mutex> lock(mtx_);

    auto it = map_.find(src.id);
    if (it != map_.end() && it->second.valid &&
        it->second.width == targetWidth) {
        return it->second;
    }

    fs::create_directories(proxyRoot_);

    const auto original = fs::path(src.path).filename().string();
    const std::string codec = "h264";
    const std::string proxyName =
        original + "__proxy_" + std::to_string(targetWidth) + "x" + codec + ".mp4";
    const fs::path proxyPath = fs::path(proxyRoot_) / proxyName;

    const fs::path ffmpegExe = fs::path(ffmpegBinDir_) / "ffmpeg";

    // NOTE: escaping / quoting for spaces is omitted here for brevity.
    std::string cmd = "\"" + ffmpegExe.string() + "\" -y -i \"" + src.path + "\" "
        "-vf scale=" + std::to_string(targetWidth) + ":-2 "
        "-c:v libx264 -preset veryfast -crf 28 "
        "-c:a aac -b:a 96k \"" + proxyPath.string() + "\"";

    std::system(cmd.c_str());

    ProxyInfo info;
    info.proxyPath = proxyPath.string();
    info.width = targetWidth;
    info.codec = codec;
    info.valid = fs::exists(proxyPath);
    map_[src.id] = info;
    return info;
}

ProxyInfo ProxyManager::getProxy(const std::string& sourceId) const {
    std::lock_guard<std::mutex> lock(mtx_);
    auto it = map_.find(sourceId);
    return it == map_.end() ? ProxyInfo{} : it->second;
}

void ProxyManager::setProxy(const std::string& sourceId, const ProxyInfo& info) {
    std::lock_guard<std::mutex> lock(mtx_);
    map_[sourceId] = info;
}

} // namespace cineforge::media

