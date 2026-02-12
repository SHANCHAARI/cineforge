#include "cineforge/timeline/Keyframe.h"

#include <algorithm>

namespace cineforge::timeline {

double KeyframeCurve::evaluate(double time) const {
    if (keys.empty()) {
        return 0.0;
    }
    if (time <= keys.front().time) {
        return keys.front().value;
    }
    if (time >= keys.back().time) {
        return keys.back().value;
    }

    auto it = std::upper_bound(
        keys.begin(), keys.end(), time,
        [](double t, const Keyframe& k) { return t < k.time; });

    const Keyframe& k1 = *(it - 1);
    const Keyframe& k2 = *it;

    double dt = k2.time - k1.time;
    double localT = (dt > 0.0) ? (time - k1.time) / dt : 0.0;

    switch (k1.interp) {
    case InterpolationType::Hold:
        return k1.value;
    case InterpolationType::Linear:
        return k1.value + (k2.value - k1.value) * localT;
    case InterpolationType::Bezier:
    case InterpolationType::EaseIn:
    case InterpolationType::EaseOut:
    case InterpolationType::EaseInOut:
    default:
        // For now, fall back to linear; proper bezier evaluation can be added later.
        return k1.value + (k2.value - k1.value) * localT;
    }
}

} // namespace cineforge::timeline

