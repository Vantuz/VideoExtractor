package com.vantuz.video_extractor;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.vantuz.video_extractor.db.HistoryDBSQLiteOpenHelper;
import com.vantuz.video_extractor.extractor.CantExtractException;
import com.vantuz.video_extractor.model.StreamEntry;
import com.vantuz.video_extractor.model.ExceptionAndVideoInfo;

import java.io.IOException;

public class StreamChooserActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private int selectedItem = 0;
    private StreamEntry[] streams;
    private String url;
    private StreamChooserActivityAsyncTask task;

    @Override
    @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stream_chooser_layout_loading);

        Intent intent = getIntent();
        url = intent.getStringExtra(Intent.EXTRA_TEXT);

        if (savedInstanceState == null) {
            Log.d(TAG, "savedInstanceState == null");
            task = new StreamChooserActivityAsyncTask(this);
            task.execute(url);
        } else {
            Log.d(TAG, "savedInstanceState != null");
            task = (StreamChooserActivityAsyncTask) getLastNonConfigurationInstance();
            task.bindActivity(this);
        }
    }

    private void onPostExecute(ExceptionAndVideoInfo arg) {
        if (arg.e != null) {
            setContentView(R.layout.stream_chooser_error_layout);
            TextView txt = (TextView) findViewById(R.id.textViewError);
            processError(arg.e, txt);
        } else {
            streams = arg.videoInfo.streams;
            String[] qualities = new String[arg.videoInfo.streams.length];
            for (int i = 0; i < arg.videoInfo.streams.length; i++) {
                qualities[i] = arg.videoInfo.streams[i].quality;
            }
            setContentView(R.layout.stream_chooser_layout);
            ((TextView) findViewById(R.id.videoName)).setText(url);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(StreamChooserActivity.this,
                    android.R.layout.simple_spinner_item, qualities);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

    private void processError(Exception e, TextView txt) {
        if (e instanceof IOException) {
            txt.setText(R.string.IOException_text);
        } else if (e instanceof CantExtractException) {
            txt.setText(((CantExtractException) e).strResId);
        } else {
            txt.setText(e.toString());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedItem = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    @SuppressWarnings("deprecation")
    public Object onRetainNonConfigurationInstance() {
        return task;
    }

    private class StreamChooserActivityAsyncTask extends AsyncTask<String, Void, ExceptionAndVideoInfo> {
        StreamChooserActivity activity;
        boolean ready = false;
        ExceptionAndVideoInfo res;

        public StreamChooserActivityAsyncTask(StreamChooserActivity activity) {
            this.activity = activity;
        }

        public void bindActivity(StreamChooserActivity activity) {
            this.activity = activity;
            if (ready) {
                activity.onPostExecute(res);
            }
        }

        @Override
        protected ExceptionAndVideoInfo doInBackground(String... params) {
            Log.d(TAG, "task started");
            ExceptionAndVideoInfo res;
            try {
                res = new ExceptionAndVideoInfo(null,
                        VideoPageURLProcessor.getVideoStreams(params[0]));
            } catch (Exception e) {
                return new ExceptionAndVideoInfo(e, null);
            }
            SQLiteDatabase db = HistoryDBSQLiteOpenHelper.getInstance(activity).getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(HistoryDBSQLiteOpenHelper.URL, params[0]);
            db.insert(HistoryDBSQLiteOpenHelper.TABLE_HISTORY, null, values);
            return res;
        }

        @Override
        protected void onPostExecute(ExceptionAndVideoInfo arg) {
            ready = true;
            res = arg;
            activity.onPostExecute(arg);
        }
    }

    private static final String TAG = "StreamChooserActivity";

}
