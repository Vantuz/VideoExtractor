package com.vantuz.video_extractor;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.vantuz.video_extractor.db.HistoryDBSQLiteOpenHelper;

import java.net.URL;

public class HistoryActivity extends Activity {
    private RecyclerView mRecyclerView;
    private CursorRecyclerViewAdapter<ViewHolder> mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.history_activity_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new RecylcerDividersDecorator(Color.DKGRAY));

        Cursor cursor = HistoryDBSQLiteOpenHelper.getInstance(this)
                .getReadableDatabase()
                .query(HistoryDBSQLiteOpenHelper.TABLE_HISTORY, null, null, null, null, null, null);
        mAdapter = new CursorRecyclerViewAdapter<ViewHolder>(this, cursor) {
            @Override
            public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
                viewHolder.mTextView.setText(cursor.getString(0));
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(HistoryActivity.this).inflate(R.layout.list_item, viewGroup, false);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_TEXT, ((TextView) v).getText());
                        intent.setClass(HistoryActivity.this, StreamChooserActivity.class);
                        startActivity(intent);
                    }
                });
                ViewHolder vh = new ViewHolder((TextView) view);
                return vh;
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }
}