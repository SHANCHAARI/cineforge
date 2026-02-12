#pragma once

#include "cineforge/render/Frame.h"

namespace cineforge::render {

class Effect {
public:
    virtual ~Effect() = default;

    // Unique identifier for debugging / profiling.
    virtual const char* id() const = 0;

    // Process a single frame. Implementations are free to assume that
    // input and output refer to different underlying textures.
    virtual void process(const Frame& input, Frame& output) = 0;
};

} // namespace cineforge::render

