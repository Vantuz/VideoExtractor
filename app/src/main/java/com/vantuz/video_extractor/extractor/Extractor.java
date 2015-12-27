package com.vantuz.video_extractor.extractor;

public interface Extractor {
    String[] getCompatibleDomains();
    StreamEntry[] extractStreams(String url) throws Exception;
}
