#ifndef VIDEOEDITOR_VIDEO_DECODER_H
#define VIDEOEDITOR_VIDEO_DECODER_H

#include <media/NdkMediaCodec.h>
#include <media/NdkMediaExtractor.h>
#include <cstdint>
#include <mutex>
#include <string>
#include <vector>

namespace videoeditor {

/**
 * Minimal MediaCodec-based video decoder skeleton.
 *
 * This class is responsible for:
 *  - opening a media file via AMediaExtractor
 *  - configuring an AMediaCodec decoder
 *  - stepping decode to produce frames near a requested presentation time
 *
 * For now, the implementation will focus on wiring and lifetime; the
 * actual YUV->texture upload path will be handled by the Engine /
 * TextureRenderer integration.
 */
class VideoDecoder {
public:
  explicit VideoDecoder(const std::string &path);
  ~VideoDecoder();

  bool initialize();
  void shutdown();

  // Seek to a presentation time in microseconds.
  bool seekToUs(int64_t timeUs);

  // Decode one frame; returns true if a frame was produced.
  bool decodeNext();

  // Pointer to last decoded RGBA frame data (width * height * 4 bytes).
  const std::uint8_t *lastFrameRgba() const {
    return rgbaBuffer_.empty() ? nullptr : rgbaBuffer_.data();
  }

  int width() const { return width_; }
  int height() const { return height_; }
  int64_t durationUs() const { return durationUs_; }

private:
  std::string path_;
  AMediaExtractor *extractor_ = nullptr;
  AMediaCodec *codec_ = nullptr;

  int videoTrackIndex_ = -1;
  int width_ = 0;
  int height_ = 0;
  int64_t durationUs_ = 0;

  std::mutex mutex_;
  std::vector<std::uint8_t> rgbaBuffer_;
};

} // namespace videoeditor

#endif // VIDEOEDITOR_VIDEO_DECODER_H

