#include "cineforge/timeline/KeyframeManager.h"

namespace cineforge::timeline {

void KeyframeManager::registerCurve(const KeyframeCurve& curve) {
    curves_[curve.id] = curve;
}

void KeyframeManager::removeCurve(const std::string& id) {
    curves_.erase(id);
}

const KeyframeCurve* KeyframeManager::getCurve(const std::string& id) const {
    auto it = curves_.find(id);
    return it == curves_.end() ? nullptr : &it->second;
}

double KeyframeManager::eval(const std::string& id, double time) const {
    auto* c = getCurve(id);
    return c ? c->evaluate(time) : 0.0;
}

} // namespace cineforge::timeline

