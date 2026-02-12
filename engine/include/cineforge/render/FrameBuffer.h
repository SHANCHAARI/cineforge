#pragma once

#include "cineforge/render/Frame.h"

namespace cineforge::render {

/**
 * Lightweight wrapper around a GPU-backed framebuffer/texture.
 *
 * The concrete allocation is delegated to the GPU backend; for now this
 * class only tracks dimensions and lifetime. Backends should specialize
 * create/destroy to integrate with Vulkan/Metal/D3D/WebGPU.
 */
class FrameBuffer {
public:
    FrameBuffer(int width, int height, PixelFormat fmt);
    ~FrameBuffer();

    Frame& frame() { return frame_; }
    const Frame& frame() const { return frame_; }

    void resize(int width, int height);

private:
    Frame frame_{};

    void createTexture();
    void destroyTexture();
};

} // namespace cineforge::render

