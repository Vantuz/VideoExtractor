package com.vantuz.video_extractor.extractor;

import com.vantuz.video_extractor.R;
import com.vantuz.video_extractor.model.StreamEntry;
import com.vantuz.video_extractor.model.VideoInfo;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeExtractor implements Extractor {
    public YoutubeExtractor() {}

    @Override
    public String[] getCompatibleDomains() {
        return new String[] {"www.youtube.com", "m.youtube.com", "youtube.com"};
    }

    @Override
    public VideoInfo extractStreams(String url) throws Exception {
        String videoId = null;
        try {
            videoId = getFirstGroupFromRegexp(
                    "http[s]?://(?:www\\.|m\\.)?youtube\\.com/watch\\?[a-zA-Z0-9&%+\\-_=]*v=([a-zA-Z0-9\\-_]*)", url);
        } catch (IllegalStateException e) {
            throw new CantExtractException("Bad url", url, R.string.bad_url_text);
        }
        String all = getStringByUrl("http://www.youtube.com/get_video_info?video_id=" + videoId);
        String stream_map;
        try {
            stream_map = getFirstGroupFromRegexp("url_encoded_fmt_stream_map=([^&]*)", all);
        } catch (IllegalStateException e) {
            throw new CantExtractException("Video is not embeddable", url, R.string.video_not_embeddable_text);
        }
        String[] tokens = URLDecoder.decode(stream_map,"utf-8").split(",");
        StreamEntry[] res = new StreamEntry[tokens.length];
        for (int i = 0; i < res.length; i++) {
            String streamUrl = URLDecoder.decode(getFirstGroupFromRegexp("url=([^&]*)", tokens[i]), "utf-8");
            String type = getFirstGroupFromRegexp("(type=[^&]*)", tokens[i]);
            type = getFirstGroupFromRegexp("type=([a-z0-9/\\-]*)", URLDecoder.decode(type, "utf-8"));
            String quality = getFirstGroupFromRegexp("quality=([a-z0-9]*)", tokens[i]);
            res[i] = new StreamEntry(quality + " (" + type + ")", streamUrl);
        }
        return new VideoInfo(res);
    }

    private String getStringByUrl(String urlString) throws IOException{
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        return reader.readLine();
    }

    private String getFirstGroupFromRegexp(String regexp, String str) throws Exception {
        Matcher m = Pattern.compile(regexp).matcher(str);
        m.find();
        return m.group(1);
    }
}
