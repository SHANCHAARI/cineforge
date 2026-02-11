#ifndef VIDEOEDITOR_RECT_H
#define VIDEOEDITOR_RECT_H

#include "Vec2.h"

namespace videoeditor {

struct Rect {
  float x, y, width, height;

  Rect() : x(0.0f), y(0.0f), width(0.0f), height(0.0f) {}
  Rect(float x, float y, float width, float height)
      : x(x), y(y), width(width), height(height) {}

  float left() const { return x; }
  float right() const { return x + width; }
  float top() const { return y; }
  float bottom() const { return y + height; }

  Vec2 center() const { return Vec2(x + width * 0.5f, y + height * 0.5f); }

  bool contains(const Vec2 &point) const {
    return point.x >= left() && point.x <= right() && point.y >= top() &&
           point.y <= bottom();
  }

  bool intersects(const Rect &other) const {
    return !(right() < other.left() || left() > other.right() ||
             bottom() < other.top() || top() > other.bottom());
  }
};

} // namespace videoeditor

#endif // VIDEOEDITOR_RECT_H
