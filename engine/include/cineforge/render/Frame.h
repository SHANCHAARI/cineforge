#pragma once

#include <cstdint>

namespace cineforge::render {

enum class PixelFormat {
    RGBA8,
    BGRA8,
    NV12
};

struct GPUTextureHandle {
    // Backend-specific opaque handle (Vulkan image, Metal texture, etc.).
    uint64_t id = 0;
};

struct Frame {
    int width = 0;
    int height = 0;
    PixelFormat format = PixelFormat::RGBA8;

    GPUTextureHandle texture;   // Preferred GPU path.

    // Optional CPU-side buffer for backends that need readback.
    std::uint8_t* cpuData = nullptr;
    int cpuStride = 0;
};

} // namespace cineforge::render

