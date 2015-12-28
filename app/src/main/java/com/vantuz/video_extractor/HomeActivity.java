package com.vantuz.video_extractor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.vantuz.video_extractor.db.MyDBSQLiteOpenHelper;

public class HomeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_layout);
        findViewById(R.id.buttonHistory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.buttonClearHistory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDBSQLiteOpenHelper.getInstance(HomeActivity.this).getWritableDatabase()
                        .delete(MyDBSQLiteOpenHelper.TABLE_HISTORY, null ,null);
                Toast toast = Toast.makeText(
                        HomeActivity.this, R.string.history_cleared,Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        findViewById(R.id.buttonFavourites).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FavouritesActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.buttonClearFavourites).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDBSQLiteOpenHelper.getInstance(HomeActivity.this).getWritableDatabase()
                        .delete(MyDBSQLiteOpenHelper.TABLE_FAVORITES, null ,null);
                Toast toast = Toast.makeText(
                        HomeActivity.this, R.string.favourites_cleared,Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
