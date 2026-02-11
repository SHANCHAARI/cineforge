#ifndef VIDEOEDITOR_VEC2_H
#define VIDEOEDITOR_VEC2_H

#include <cmath>

namespace videoeditor {

struct Vec2 {
  float x, y;

  Vec2() : x(0.0f), y(0.0f) {}
  Vec2(float x, float y) : x(x), y(y) {}

  Vec2 operator+(const Vec2 &other) const {
    return Vec2(x + other.x, y + other.y);
  }

  Vec2 operator-(const Vec2 &other) const {
    return Vec2(x - other.x, y - other.y);
  }

  Vec2 operator*(float scalar) const { return Vec2(x * scalar, y * scalar); }

  Vec2 operator/(float scalar) const { return Vec2(x / scalar, y / scalar); }

  float length() const { return std::sqrt(x * x + y * y); }

  Vec2 normalized() const {
    float len = length();
    if (len > 0.0f) {
      return *this / len;
    }
    return Vec2(0.0f, 0.0f);
  }

  float dot(const Vec2 &other) const { return x * other.x + y * other.y; }
};

} // namespace videoeditor

#endif // VIDEOEDITOR_VEC2_H
