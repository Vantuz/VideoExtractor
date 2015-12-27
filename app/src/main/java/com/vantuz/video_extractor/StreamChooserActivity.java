package com.vantuz.video_extractor;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.vantuz.video_extractor.extractor.StreamEntry;

public class StreamChooserActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private int selectedItem = 0;
    private StreamEntry[] streams;
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stream_chooser_layout_loading);

        Intent intent = getIntent();
        url = intent.getStringExtra(Intent.EXTRA_TEXT);

        new AsyncTask<String, Void, ExceptionAndVideoStreams>() {
            @Override
            protected ExceptionAndVideoStreams doInBackground(String... params) {
                try {
                    return new ExceptionAndVideoStreams(null,
                            VideoPageURLProcessor.getVideoStreams(params[0]));
                } catch (Exception e) {
                    return new ExceptionAndVideoStreams(e, null);
                }
            }

            @Override
            protected void onPostExecute(ExceptionAndVideoStreams arg) {
                StreamChooserActivity.this.onPostExecute(arg);
            }
        }.execute(url);
    }

    private void onPostExecute(ExceptionAndVideoStreams arg) {
        if (arg.e != null) {
            setContentView(R.layout.stream_chooser_error_layout);
            TextView txt = (TextView) findViewById(R.id.textViewError);
            txt.setText("Error: " + arg.e);
        } else {
            streams = arg.streams;
            String[] qualities = new String[arg.streams.length];
            for (int i = 0; i < arg.streams.length; i++) {
                qualities[i] = arg.streams[i].quality;
            }
            setContentView(R.layout.stream_chooser_layout);
            ((TextView) findViewById(R.id.videoName)).setText(url);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(StreamChooserActivity.this,
                    android.R.layout.simple_spinner_item, qualities);
            Spinner spinner = (Spinner) findViewById(R.id.quality_chooser);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);
            ((Button) findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(streams[selectedItem].url), "video/*");
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedItem = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private static class ExceptionAndVideoStreams {
        public final Exception e;
        public final StreamEntry[] streams;

        public ExceptionAndVideoStreams(Exception e, StreamEntry[] streams) {
            this.e = e;
            this.streams = streams;
        }
    }
}
