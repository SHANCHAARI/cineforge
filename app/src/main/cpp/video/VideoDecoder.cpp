#include "VideoDecoder.h"

#include <android/log.h>

namespace {
constexpr const char *TAG = "VideoDecoder";

void loge(const char *msg) { __android_log_print(ANDROID_LOG_ERROR, TAG, "%s", msg); }
void logi(const char *msg) { __android_log_print(ANDROID_LOG_INFO, TAG, "%s", msg); }
} // namespace

namespace videoeditor {

VideoDecoder::VideoDecoder(const std::string &path) : path_(path) {}

VideoDecoder::~VideoDecoder() { shutdown(); }

bool VideoDecoder::initialize() {
  std::lock_guard<std::mutex> lock(mutex_);

  extractor_ = AMediaExtractor_new();
  if (!extractor_) {
    loge("Failed to create AMediaExtractor");
    return false;
  }

  media_status_t status =
      AMediaExtractor_setDataSource(extractor_, path_.c_str());
  if (status != AMEDIA_OK) {
    loge("AMediaExtractor_setDataSource failed");
    return false;
  }

  const size_t numTracks = AMediaExtractor_getTrackCount(extractor_);
  for (size_t i = 0; i < numTracks; ++i) {
    AMediaFormat *format = AMediaExtractor_getTrackFormat(extractor_, i);
    const char *mime = nullptr;
    if (AMediaFormat_getString(format, AMEDIAFORMAT_KEY_MIME, &mime) &&
        mime && std::string(mime).rfind("video/", 0) == 0) {
      videoTrackIndex_ = static_cast<int>(i);
      AMediaExtractor_selectTrack(extractor_, i);

      AMediaFormat_getInt32(format, AMEDIAFORMAT_KEY_WIDTH, &width_);
      AMediaFormat_getInt32(format, AMEDIAFORMAT_KEY_HEIGHT, &height_);
      AMediaFormat_getInt64(format, AMEDIAFORMAT_KEY_DURATION, &durationUs_);

      codec_ = AMediaCodec_createDecoderByType(mime);
      if (!codec_) {
        loge("Failed to create AMediaCodec");
        AMediaFormat_delete(format);
        return false;
      }

      if (AMediaCodec_configure(codec_, format, nullptr, nullptr, 0) !=
          AMEDIA_OK) {
        loge("AMediaCodec_configure failed");
        AMediaCodec_delete(codec_);
        codec_ = nullptr;
        AMediaFormat_delete(format);
        return false;
      }

      AMediaCodec_start(codec_);
      AMediaFormat_delete(format);
      logi("VideoDecoder initialized");
      return true;
    }
    AMediaFormat_delete(format);
  }

  loge("No video track found");
  return false;
}

void VideoDecoder::shutdown() {
  std::lock_guard<std::mutex> lock(mutex_);

  if (codec_) {
    AMediaCodec_stop(codec_);
    AMediaCodec_delete(codec_);
    codec_ = nullptr;
  }
  if (extractor_) {
    AMediaExtractor_delete(extractor_);
    extractor_ = nullptr;
  }
}

bool VideoDecoder::seekToUs(int64_t timeUs) {
  std::lock_guard<std::mutex> lock(mutex_);
  if (!extractor_ || videoTrackIndex_ < 0)
    return false;

  AMediaExtractor_seekTo(extractor_, timeUs, AMEDIAEXTRACTOR_SEEK_CLOSEST_SYNC);
  return true;
}

bool VideoDecoder::decodeNext() {
  std::lock_guard<std::mutex> lock(mutex_);
  if (!codec_ || !extractor_)
    return false;

  // Input
  ssize_t inIndex =
      AMediaCodec_dequeueInputBuffer(codec_, /*timeoutUs*/ 0);
  if (inIndex >= 0) {
    size_t bufSize = 0;
    uint8_t *buf = AMediaCodec_getInputBuffer(codec_, inIndex, &bufSize);
    if (buf) {
      ssize_t sampleSize = AMediaExtractor_readSampleData(extractor_, buf, bufSize);
      if (sampleSize < 0) {
        AMediaCodec_queueInputBuffer(codec_, inIndex, 0, 0, 0,
                                     AMEDIACODEC_BUFFER_FLAG_END_OF_STREAM);
      } else {
        int64_t ptsUs = AMediaExtractor_getSampleTime(extractor_);
        AMediaCodec_queueInputBuffer(codec_, inIndex, 0,
                                     static_cast<size_t>(sampleSize), ptsUs, 0);
        AMediaExtractor_advance(extractor_);
      }
    }
  }

  // Output
  AMediaCodecBufferInfo info{};
  ssize_t outIndex =
      AMediaCodec_dequeueOutputBuffer(codec_, &info, /*timeoutUs*/ 0);
  if (outIndex >= 0) {
    size_t bufSize = 0;
    uint8_t *buf = AMediaCodec_getOutputBuffer(codec_, outIndex, &bufSize);
    if (buf && width_ > 0 && height_ > 0) {
      // Naive NV12/NV21 -> RGBA conversion (CPU). Real implementation
      // should query format and handle all planes precisely.
      const int w = width_;
      const int h = height_;
      const size_t ySize = static_cast<size_t>(w * h);
      if (bufSize >= ySize) {
        rgbaBuffer_.resize(static_cast<size_t>(w * h * 4));
        const uint8_t *yPlane = buf;
        // Assume a single chroma plane that we ignore for now; render
        // luma as grayscale to keep it simple and robust.
        for (int j = 0; j < h; ++j) {
          for (int i = 0; i < w; ++i) {
            uint8_t Y = yPlane[j * w + i];
            std::size_t idx = static_cast<std::size_t>((j * w + i) * 4);
            rgbaBuffer_[idx + 0] = Y;
            rgbaBuffer_[idx + 1] = Y;
            rgbaBuffer_[idx + 2] = Y;
            rgbaBuffer_[idx + 3] = 255;
          }
        }
      }
    }
    AMediaCodec_releaseOutputBuffer(codec_, outIndex, /*render*/ false);
    return true;
  }

  return false;
}

} // namespace videoeditor

