package com.vantuz.video_extractor.extractor;

import com.vantuz.video_extractor.R;
import com.vantuz.video_extractor.model.StreamEntry;
import com.vantuz.video_extractor.model.VideoInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VkExtractor implements Extractor {
    @Override
    public String[] getCompatibleDomains() {
        return new String[] {"vk.com", "m.vk.com"};
    }

    @Override
    public VideoInfo extractStreams(String url) throws Exception {
        String html = getStringByUrl(url);
        List<StreamEntry> res = new ArrayList<StreamEntry>();
        extractVkHosted(html, res);
        if (res.size() > 0) {
            return new VideoInfo(res.toArray(new StreamEntry[res.size()]));
        } else {
            throw new CantExtractException("Bad url", url, R.string.bad_url_text);
        }
    }

    private void extractVkHosted(String html, List<StreamEntry> res) {
        Matcher m = Pattern.compile("\\\\\"url(\\d+)\\\\\":\\\\\"(http[s]?:.+?)\\?").matcher(html);
        while (m.find()) {
            String quality = m.group(1);
            String stream = m.group(2).replaceAll("\\\\\\\\\\\\", "");
            res.add(new StreamEntry(quality, stream));
        }
    }

    private String getStringByUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36");
        conn.setRequestMethod("GET");
        conn.connect();
        if (conn.getResponseCode() == 403) {
            throw new IOException("403 Forbidden");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder res = new StringBuilder();
        while (reader.ready()) {
            res.append(reader.readLine());
        }
        return res.toString();
    }
}
