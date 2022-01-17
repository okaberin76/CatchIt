package com.example.catchit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HighScores extends AppCompatActivity {
    private RelativeLayout relativeLayout;
    private ListView listView;
    private Button buttonClear;

    private int size;
    private SharedPreferences preferences;
    private ArrayList<String> score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);

        relativeLayout = findViewById(R.id.highScores);
        listView = findViewById(R.id.listView);

        preferences = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        size = preferences.getInt("size",0);
        score = new ArrayList<>();

        if(size > 0) {
             for (int i = 0; i < size; i++)
                 score.add(i, preferences.getString("nickname_" + i, "Default user")
                         + " | " + preferences.getInt("lastScore_" + i, 0) + " | "
                         + preferences.getString("date_" + i, null));

             ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.activity_highscores, R.id.textView, score);
             listView.setAdapter(arrayAdapter);

             buttonClear = new Button(this);
             buttonClear.setText("Clear");
             buttonClear.setX(400);
             relativeLayout.addView(buttonClear);

             buttonClear.setOnClickListener(view -> {
                 SharedPreferences.Editor editor = preferences.edit();
                 editor.clear();
                 editor.apply();
                 finish();
             });
        }
    }
}