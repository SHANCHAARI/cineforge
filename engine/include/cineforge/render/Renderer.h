#pragma once

#include <memory>

#include "cineforge/render/FrameBuffer.h"
#include "cineforge/render/EffectGraph.h"

namespace cineforge::render {

struct RenderContext {
    int targetWidth = 0;
    int targetHeight = 0;
    double timeSeconds = 0.0;
};

/**
 * High‑level preview/export renderer entrypoint.
 */
class Renderer {
public:
    Renderer();

    void setEffectGraph(const EffectGraph* graph);

    Frame renderPreview(const RenderContext& ctx);

private:
    const EffectGraph* graph_ = nullptr;
    std::unique_ptr<FrameBuffer> ping_;
    std::unique_ptr<FrameBuffer> pong_;

    void ensureBuffers(int w, int h);
};

} // namespace cineforge::render

