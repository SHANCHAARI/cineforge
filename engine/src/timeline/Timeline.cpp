#include "cineforge/timeline/Timeline.h"

namespace cineforge::timeline {

void Timeline::addTrack(const Track& track) {
    tracks_.push_back(track);
}

void Timeline::addClip(const std::string& trackId, const Clip& clip) {
    for (auto& t : tracks_) {
        if (t.id == trackId) {
            t.clips.push_back(clip);
            return;
        }
    }
}

void Timeline::splitClip(const std::string& clipId, double time) {
    for (auto& t : tracks_) {
        for (std::size_t i = 0; i < t.clips.size(); ++i) {
            auto& c = t.clips[i];
            if (c.id == clipId && time > c.start && time < c.end) {
                double mid = time;
                Clip second = c;
                second.id = c.id + "_b";
                second.start = mid;
                second.inPoint = c.inPoint + (mid - c.start);
                c.end = mid;
                t.clips.insert(t.clips.begin() + static_cast<long>(i + 1), second);
                return;
            }
        }
    }
}

void Timeline::moveClip(const std::string& clipId, double newStart) {
    for (auto& t : tracks_) {
        for (auto& c : t.clips) {
            if (c.id == clipId) {
                double duration = c.end - c.start;
                c.start = newStart;
                c.end = newStart + duration;
                return;
            }
        }
    }
}

} // namespace cineforge::timeline

