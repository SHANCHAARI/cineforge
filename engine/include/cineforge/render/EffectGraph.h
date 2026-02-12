#pragma once

#include <memory>
#include <vector>

#include "cineforge/render/Effect.h"
#include "cineforge/render/FrameBuffer.h"

namespace cineforge::render {

/**
 * Linear effect chain with ping‑pong framebuffers.
 *
 * This intentionally keeps the model simple for now; it can be replaced
 * by a more general node‑based render graph later without changing the
 * public engine facade.
 */
class EffectGraph {
public:
    void addEffect(std::unique_ptr<Effect> effect);
    void clear();

    void process(const Frame& sourceFrame, Frame& outFrame) const;

private:
    std::vector<std::unique_ptr<Effect>> effects_;
};

} // namespace cineforge::render

