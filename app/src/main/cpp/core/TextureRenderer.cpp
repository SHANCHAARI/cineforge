#include "TextureRenderer.h"
#include "utils/Logger.h"
#include <cstdint>
#include <vector>


namespace videoeditor {

static const char *VERTEX_SHADER = R"(
    #version 300 es
    layout(location = 0) in vec4 a_Position;
    layout(location = 1) in vec2 a_TexCoord;
    out vec2 v_TexCoord;
    uniform mat4 u_Matrix;
    void main() {
        gl_Position = u_Matrix * a_Position;
        v_TexCoord = a_TexCoord;
    }
)";

static const char *FRAGMENT_SHADER = R"(
    #version 300 es
    precision mediump float;
    in vec2 v_TexCoord;
    out vec4 outColor;
    uniform sampler2D u_Texture;
    void main() {
        // outColor = texture(u_Texture, v_TexCoord);
        // Debugging: Draw a gradient instead of texture for now if texture is invalid
        outColor = vec4(v_TexCoord.x, v_TexCoord.y, 0.5, 1.0); 
    }
)";

TextureRenderer::TextureRenderer() : vbo_(0), vao_(0), program_(0) {}

TextureRenderer::~TextureRenderer() {
  if (vbo_)
    glDeleteBuffers(1, &vbo_);
  if (vao_)
    glDeleteVertexArrays(1, &vao_);
}

void TextureRenderer::initialize() {
  program_ = ShaderManager::getInstance().compileProgram(
      "SimpleTexture", VERTEX_SHADER, FRAGMENT_SHADER);

  // Quad vertices (x, y, u, v)
  GLfloat vertices[] = {
      -0.5f, 0.5f,  0.0f, 0.0f, // Top Left
      -0.5f, -0.5f, 0.0f, 1.0f, // Bottom Left
      0.5f,  -0.5f, 1.0f, 1.0f, // Bottom Right
      0.5f,  0.5f,  1.0f, 0.0f  // Top Right
  };

  glGenVertexArrays(1, &vao_);
  glBindVertexArray(vao_);

  glGenBuffers(1, &vbo_);
  glBindBuffer(GL_ARRAY_BUFFER, vbo_);
  glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);

  // Position
  glVertexAttribPointer(0, 2, GL_FLOAT, GL_FALSE, 4 * sizeof(float), (void *)0);
  glEnableVertexAttribArray(0);

  // TexCoord
  glVertexAttribPointer(1, 2, GL_FLOAT, GL_FALSE, 4 * sizeof(float),
                        (const void *)(uintptr_t)(2 * sizeof(float)));
  glEnableVertexAttribArray(1);

  glBindVertexArray(0);
}

void TextureRenderer::render(GLuint textureId, float x, float y, float width,
                             float height, float rotation) {
  if (program_ == 0)
    return;

  glUseProgram(program_);

  // TODO: Construct matrix from x,y,width,height,rotation
  // For now, identity matrix logic or simple pass-through.
  // Assuming u_Matrix for now is identity or handled partially.

  // Simple mock matrix (identity)
  GLfloat matrix[] = {1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
                      0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};

  GLint loc = glGetUniformLocation(program_, "u_Matrix");
  glUniformMatrix4fv(loc, 1, GL_FALSE, matrix);

  glBindVertexArray(vao_);
  glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
  glBindVertexArray(0);
}

} // namespace videoeditor
