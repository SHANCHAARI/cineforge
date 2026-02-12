#ifndef VIDEOEDITOR_TEXTURE_RENDERER_H
#define VIDEOEDITOR_TEXTURE_RENDERER_H

#include "ShaderManager.h"
#include <GLES3/gl3.h>

namespace videoeditor {

class TextureRenderer {
public:
  TextureRenderer();
  ~TextureRenderer();

  void initialize();
  void setColorGrading(float brightness, float contrast, float saturation);
  void render(GLuint textureId, float x, float y, float width, float height,
              float rotation);

private:
  GLuint vbo_;
  GLuint vao_;
  GLuint program_;

  float brightness_ = 1.0f;
  float contrast_ = 1.0f;
  float saturation_ = 1.0f;
};

} // namespace videoeditor

#endif // VIDEOEDITOR_TEXTURE_RENDERER_H
