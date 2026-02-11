#ifndef VIDEOEDITOR_VEC3_H
#define VIDEOEDITOR_VEC3_H

#include <cmath>

namespace videoeditor {

struct Vec3 {
  float x, y, z;

  Vec3() : x(0.0f), y(0.0f), z(0.0f) {}
  Vec3(float x, float y, float z) : x(x), y(y), z(z) {}

  Vec3 operator+(const Vec3 &other) const {
    return Vec3(x + other.x, y + other.y, z + other.z);
  }

  Vec3 operator-(const Vec3 &other) const {
    return Vec3(x - other.x, y - other.y, z - other.z);
  }

  Vec3 operator*(float scalar) const {
    return Vec3(x * scalar, y * scalar, z * scalar);
  }

  Vec3 operator/(float scalar) const {
    return Vec3(x / scalar, y / scalar, z / scalar);
  }

  float length() const { return std::sqrt(x * x + y * y + z * z); }

  Vec3 normalized() const {
    float len = length();
    if (len > 0.0f) {
      return *this / len;
    }
    return Vec3(0.0f, 0.0f, 0.0f);
  }

  float dot(const Vec3 &other) const {
    return x * other.x + y * other.y + z * other.z;
  }

  Vec3 cross(const Vec3 &other) const {
    return Vec3(y * other.z - z * other.y, z * other.x - x * other.z,
                x * other.y - y * other.x);
  }
};

} // namespace videoeditor

#endif // VIDEOEDITOR_VEC3_H
