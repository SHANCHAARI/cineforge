#pragma once

#include <string>

namespace cineforge::media {

struct MediaSource {
    std::string id;
    std::string path;
    std::string proxyPath;
    std::string mediaType; // "video","audio","image"
    double duration = 0.0;
};

} // namespace cineforge::media

