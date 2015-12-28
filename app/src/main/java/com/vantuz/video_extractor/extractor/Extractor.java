package com.vantuz.video_extractor.extractor;

import com.vantuz.video_extractor.model.StreamEntry;
import com.vantuz.video_extractor.model.VideoInfo;

public interface Extractor {
    String[] getCompatibleDomains();
    VideoInfo extractStreams(String url) throws Exception;
}
