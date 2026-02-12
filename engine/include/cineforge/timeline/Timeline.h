#pragma once

#include <string>
#include <vector>

namespace cineforge::timeline {

enum class TrackType {
    Video,
    Audio,
    Subtitle,
    Adjustment,
    Unknown
};

struct ClipRef {
    std::string id;
};

struct Clip {
    std::string id;
    std::string sourceId;
    double start = 0.0;   // timeline start (ms or seconds – engine‑wide convention)
    double end = 0.0;     // timeline end
    double inPoint = 0.0; // source in
    double outPoint = 0.0;// source out
};

struct Track {
    std::string id;
    TrackType type = TrackType::Unknown;
    std::vector<Clip> clips;
};

class Timeline {
public:
    void addTrack(const Track& track);
    void addClip(const std::string& trackId, const Clip& clip);

    const std::vector<Track>& tracks() const { return tracks_; }
    std::vector<Track>& tracks() { return tracks_; }

    // Simplified editing operations for now.
    void splitClip(const std::string& clipId, double time);
    void moveClip(const std::string& clipId, double newStart);

private:
    std::vector<Track> tracks_;
};

} // namespace cineforge::timeline

