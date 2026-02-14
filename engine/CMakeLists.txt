cmake_minimum_required(VERSION 3.20)
project(cineforge_engine LANGUAGES CXX)

add_library(cineforge STATIC
    src/core/Engine.cpp
    src/render/EffectGraph.cpp
    src/render/FrameBuffer.cpp
    src/render/Renderer.cpp
    src/timeline/KeyframeManager.cpp
    src/timeline/Timeline.cpp
    src/media/ProxyManager.cpp
)

target_include_directories(cineforge
    PUBLIC
        ${CMAKE_CURRENT_SOURCE_DIR}/include
)

target_compile_features(cineforge PUBLIC cxx_std_17)

