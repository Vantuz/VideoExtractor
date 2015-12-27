package com.vantuz.video_extractor;

import com.vantuz.video_extractor.extractor.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoPageURLProcessor {

    private static String getDomainFromUrl(String url) throws Exception {
        Matcher m = Pattern.compile("http[s]?://([a-zA-Z.]+)").matcher(url);
        m.find();
        return m.group(1);
    }

    private static Extractor getExtractorForUrlDomain(String domain) {
        Extractor[] extractors = {new YoutubeExtractor(), new VkExtractor()};
        for (Extractor ext : extractors) {
            for (String curDomain : ext.getCompatibleDomains()) {
                if (curDomain.equals(domain)) {
                    return ext;
                }
            }
        }
        throw new RuntimeException("No extractor found");
    }

    public static StreamEntry[] getVideoStreams(String url) throws Exception {
        return getExtractorForUrlDomain(getDomainFromUrl(url)).extractStreams(url);
    }
}
