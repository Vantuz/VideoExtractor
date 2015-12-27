package com.vantuz.video_extractor;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;
import com.vantuz.video_extractor.extractor.YoutubeExtractor;

public class TestActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testlayout);
        TextView text = (TextView) findViewById(R.id.textView);

        try {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
            text.setText(VideoPageURLProcessor.getVideoStreams("http://www.youtube.com/watch?itct=CBYQpDAYBiITCNym3Oma28gCFY-WGAodGoINRDINd2F0Y2gtdnJlYy1jaEim35HalIGSuV0%3D&layout=mobile&sts=16725&tsp=1&utcoffset=180&v=ZJb7C2OE9xM&app=desktop")[0].quality);
        } catch (Exception e) {
            text.setText("Exception: " + e.getMessage());
        }
    }
}