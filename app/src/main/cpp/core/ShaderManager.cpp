#include "ShaderManager.h"
#include <vector>

namespace videoeditor {

ShaderManager &ShaderManager::getInstance() {
  static ShaderManager instance;
  return instance;
}

GLuint ShaderManager::getProgram(const std::string &name) {
  if (programs_.find(name) != programs_.end()) {
    return programs_[name];
  }
  return 0;
}

GLuint ShaderManager::compileProgram(const std::string &name,
                                     const char *vertexSrc,
                                     const char *fragmentSrc) {
  GLuint vertexShader = loadShader(GL_VERTEX_SHADER, vertexSrc);
  if (vertexShader == 0)
    return 0;

  GLuint fragmentShader = loadShader(GL_FRAGMENT_SHADER, fragmentSrc);
  if (fragmentShader == 0) {
    glDeleteShader(vertexShader);
    return 0;
  }

  GLuint program = glCreateProgram();
  if (program != 0) {
    glAttachShader(program, vertexShader);
    glAttachShader(program, fragmentShader);
    glLinkProgram(program);

    GLint linkStatus = GL_FALSE;
    glGetProgramiv(program, GL_LINK_STATUS, &linkStatus);
    if (linkStatus != GL_TRUE) {
      GLint bufLength = 0;
      glGetProgramiv(program, GL_INFO_LOG_LENGTH, &bufLength);
      if (bufLength) {
        std::vector<char> buf(bufLength);
        glGetProgramInfoLog(program, bufLength, nullptr, buf.data());
        LOGE("Could not link program: %s", buf.data());
      }
      glDeleteProgram(program);
      program = 0;
    }
  }

  glDeleteShader(vertexShader);
  glDeleteShader(fragmentShader);

  if (program != 0) {
    programs_[name] = program;
  }
  return program;
}

GLuint ShaderManager::loadShader(GLenum type, const char *shaderSrc) {
  GLuint shader = glCreateShader(type);
  if (shader == 0)
    return 0;

  glShaderSource(shader, 1, &shaderSrc, nullptr);
  glCompileShader(shader);

  GLint compiled = 0;
  glGetShaderiv(shader, GL_COMPILE_STATUS, &compiled);
  if (!compiled) {
    GLint infoLen = 0;
    glGetShaderiv(shader, GL_INFO_LOG_LENGTH, &infoLen);
    if (infoLen) {
      std::vector<char> buf(infoLen);
      glGetShaderInfoLog(shader, infoLen, nullptr, buf.data());
      LOGE("Could not compile shader %d:\n%s\n", type, buf.data());
    }
    glDeleteShader(shader);
    return 0;
  }
  return shader;
}

} // namespace videoeditor
