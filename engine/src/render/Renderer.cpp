#include "cineforge/render/Renderer.h"

namespace cineforge::render {

Renderer::Renderer() = default;

void Renderer::setEffectGraph(const EffectGraph* graph) {
    graph_ = graph;
}

void Renderer::ensureBuffers(int w, int h) {
    if (!ping_ || ping_->frame().width != w || ping_->frame().height != h) {
        ping_ = std::make_unique<FrameBuffer>(w, h, PixelFormat::RGBA8);
        pong_ = std::make_unique<FrameBuffer>(w, h, PixelFormat::RGBA8);
    }
}

Frame Renderer::renderPreview(const RenderContext& ctx) {
    ensureBuffers(ctx.targetWidth, ctx.targetHeight);

    // In a full implementation this would pull a decoded frame for ctx.timeSeconds
    // from the media subsystem. For now we just clear/return an empty frame.
    Frame source = ping_->frame();

    if (graph_) {
        graph_->process(source, pong_->frame());
        return pong_->frame();
    }

    return source;
}

} // namespace cineforge::render

