#ifndef VIDEOEDITOR_SHADER_MANAGER_H
#define VIDEOEDITOR_SHADER_MANAGER_H

#include "utils/Logger.h"
#include <GLES3/gl3.h>
#include <string>
#include <unordered_map>


namespace videoeditor {

class ShaderManager {
public:
  static ShaderManager &getInstance();

  GLuint getProgram(const std::string &name);
  GLuint compileProgram(const std::string &name, const char *vertexSrc,
                        const char *fragmentSrc);

private:
  ShaderManager() = default;
  GLuint loadShader(GLenum type, const char *shaderSrc);

  std::unordered_map<std::string, GLuint> programs_;
};

} // namespace videoeditor

#endif // VIDEOEDITOR_SHADER_MANAGER_H
