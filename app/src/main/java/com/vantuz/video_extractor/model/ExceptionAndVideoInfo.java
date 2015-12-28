package com.vantuz.video_extractor.model;

public class ExceptionAndVideoInfo {
    public final Exception e;
    public final VideoInfo videoInfo;

    public ExceptionAndVideoInfo(Exception e, VideoInfo videoInfo) {
        this.e = e;
        this.videoInfo = videoInfo;
    }
}
