package com.vantuz.video_extractor;

import com.vantuz.video_extractor.extractor.*;
import com.vantuz.video_extractor.model.StreamEntry;
import com.vantuz.video_extractor.model.VideoInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoPageURLProcessor {

    private static String getDomainFromUrl(String url) throws Exception {
        Matcher m = Pattern.compile("http[s]?://([a-zA-Z.]+)").matcher(url);
        m.find();
        return m.group(1);
    }

    private static Extractor getExtractorForUrlDomain(String domain) throws CantExtractException {
        Extractor[] extractors = {new YoutubeExtractor(), new VkExtractor()};
        for (Extractor ext : extractors) {
            for (String curDomain : ext.getCompatibleDomains()) {
                if (curDomain.equals(domain)) {
                    return ext;
                }
            }
        }
        throw new CantExtractException("No extractor found", domain, R.string.domain_not_supported_text);
    }

    public static VideoInfo getVideoStreams(String url) throws Exception {
        return getExtractorForUrlDomain(getDomainFromUrl(url)).extractStreams(url);
    }
}
