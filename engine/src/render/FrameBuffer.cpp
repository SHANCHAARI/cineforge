#include "cineforge/render/FrameBuffer.h"

namespace cineforge::render {

FrameBuffer::FrameBuffer(int width, int height, PixelFormat fmt) {
    frame_.width = width;
    frame_.height = height;
    frame_.format = fmt;
    createTexture();
}

FrameBuffer::~FrameBuffer() {
    destroyTexture();
}

void FrameBuffer::resize(int width, int height) {
    if (frame_.width == width && frame_.height == height) {
        return;
    }
    destroyTexture();
    frame_.width = width;
    frame_.height = height;
    createTexture();
}

void FrameBuffer::createTexture() {
    // GPU‑specific allocation is delegated to the backend; for now we only
    // track an opaque id.
    static uint64_t nextId = 1;
    frame_.texture.id = nextId++;
}

void FrameBuffer::destroyTexture() {
    // Backend should free real GPU resources associated with this handle.
    frame_.texture.id = 0;
}

} // namespace cineforge::render

