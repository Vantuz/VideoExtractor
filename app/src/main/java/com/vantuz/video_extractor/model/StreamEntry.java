package com.vantuz.video_extractor.model;

public class StreamEntry {
    public final String quality;
    public final String url;

    public StreamEntry(String quality, String url) {
        this.quality = quality;
        this.url = url;
    }
}
