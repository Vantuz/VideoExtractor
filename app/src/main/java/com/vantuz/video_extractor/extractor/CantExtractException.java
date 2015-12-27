package com.vantuz.video_extractor.extractor;

public class CantExtractException extends Exception {
    String url;

    public CantExtractException(String detailMessage, String url) {
        super(detailMessage);
        this.url = url;
    }
}
