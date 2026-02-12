#pragma once

#include <optional>
#include <string>
#include <vector>

namespace cineforge::timeline {

enum class InterpolationType {
    Hold,
    Linear,
    Bezier,
    EaseIn,
    EaseOut,
    EaseInOut
};

struct BezierHandles {
    float inX = 0.0f;
    float inY = 0.0f;
    float outX = 0.0f;
    float outY = 0.0f;
};

struct Keyframe {
    double time = 0.0;  // seconds in timeline timebase
    double value = 0.0;
    InterpolationType interp = InterpolationType::Linear;
    std::optional<BezierHandles> bezier;
};

struct KeyframeCurve {
    std::string id;
    std::string target; // e.g. "clip:c1:param:opacity"
    std::vector<Keyframe> keys;

    // Optional sampled cache for fast evaluation.
    std::vector<double> sampleTimes;
    std::vector<double> sampleValues;

    double evaluate(double time) const;
};

} // namespace cineforge::timeline

