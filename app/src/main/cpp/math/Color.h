#ifndef VIDEOEDITOR_COLOR_H
#define VIDEOEDITOR_COLOR_H

#include <algorithm>
#include <cstdint>


namespace videoeditor {

struct Color {
  float r, g, b, a;

  Color() : r(0.0f), g(0.0f), b(0.0f), a(1.0f) {}
  Color(float r, float g, float b, float a = 1.0f) : r(r), g(g), b(b), a(a) {}

  // Create from 32-bit RGBA
  static Color fromRGBA(uint32_t rgba) {
    return Color(((rgba >> 24) & 0xFF) / 255.0f, ((rgba >> 16) & 0xFF) / 255.0f,
                 ((rgba >> 8) & 0xFF) / 255.0f, (rgba & 0xFF) / 255.0f);
  }

  // Convert to 32-bit RGBA
  uint32_t toRGBA() const {
    return (static_cast<uint32_t>(r * 255.0f) << 24) |
           (static_cast<uint32_t>(g * 255.0f) << 16) |
           (static_cast<uint32_t>(b * 255.0f) << 8) |
           static_cast<uint32_t>(a * 255.0f);
  }

  Color operator*(float scalar) const {
    return Color(r * scalar, g * scalar, b * scalar, a);
  }

  Color operator+(const Color &other) const {
    return Color(std::min(r + other.r, 1.0f), std::min(g + other.g, 1.0f),
                 std::min(b + other.b, 1.0f), a);
  }

  // Linear interpolation
  static Color lerp(const Color &a, const Color &b, float t) {
    return Color(a.r + (b.r - a.r) * t, a.g + (b.g - a.g) * t,
                 a.b + (b.b - a.b) * t, a.a + (b.a - a.a) * t);
  }
};

} // namespace videoeditor

#endif // VIDEOEDITOR_COLOR_H
