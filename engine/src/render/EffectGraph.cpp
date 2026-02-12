#include "cineforge/render/EffectGraph.h"

namespace cineforge::render {

void EffectGraph::addEffect(std::unique_ptr<Effect> effect) {
    effects_.push_back(std::move(effect));
}

void EffectGraph::clear() {
    effects_.clear();
}

void EffectGraph::process(const Frame& sourceFrame, Frame& outFrame) const {
    if (effects_.empty()) {
        outFrame = sourceFrame;
        return;
    }

    FrameBuffer tmpA(sourceFrame.width, sourceFrame.height, sourceFrame.format);
    FrameBuffer tmpB(sourceFrame.width, sourceFrame.height, sourceFrame.format);

    const Frame* in = &sourceFrame;
    Frame* out = &tmpA.frame();

    for (std::size_t i = 0; i < effects_.size(); ++i) {
        if (i == effects_.size() - 1) {
            effects_[i]->process(*in, outFrame);
        } else {
            effects_[i]->process(*in, *out);
            std::swap(in, out);
        }
    }
}

} // namespace cineforge::render

