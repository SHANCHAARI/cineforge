#pragma once

#include <string>
#include <unordered_map>

#include "cineforge/timeline/Keyframe.h"

namespace cineforge::timeline {

class KeyframeManager {
public:
    void registerCurve(const KeyframeCurve& curve);
    void removeCurve(const std::string& id);

    const KeyframeCurve* getCurve(const std::string& id) const;

    double eval(const std::string& id, double time) const;

private:
    std::unordered_map<std::string, KeyframeCurve> curves_;
};

} // namespace cineforge::timeline

