package com.vantuz.video_extractor.extractor;

public class CantExtractException extends Exception {
    public String url;
    public int strResId;

    public CantExtractException(String detailMessage, String url, int strResId) {
        super(detailMessage);
        this.url = url;
        this.strResId = strResId;
    }
}
