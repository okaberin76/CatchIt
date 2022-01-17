package com.example.catchit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * @Author: Maxence BOURGEAUX
 * @Year: Master 2 IWOCS 2021 - 2022
 * @Group: Group B
 */

public class HomePage extends AppCompatActivity {
    private TextView lastGame;

    private String nickname;
    private int lastScore, bestScore;
    private String imageHunter, imageTarget, imageCorpse;

    /**
     * Create the Home menu of the application
     * @param savedInstanceState the bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        // TextView for the last game infos
        lastGame = findViewById(R.id.lastGame);

        // Check if the bundle is not null otherwise it will create a new instance instead of restoring it)
        if(savedInstanceState != null)
            saveBundleInfo(savedInstanceState);
    }

    public void saveBundleInfo(Bundle bundle) {
        bundle.putString("nickname", nickname);
        bundle.putInt("lastScore", lastScore);
        bundle.putInt("bestScore", bestScore);
        bundle.putString("hunter", imageHunter);
        bundle.putString("target", imageTarget);
        bundle.putString("corpse", imageCorpse);
    }

    /**
     * We use this method to save the state information of the bundle (it's called after onStart())
     * @param memory the bundle
     */
    public void onSaveInstanceState(Bundle memory) {
        super.onSaveInstanceState(memory);
        saveBundleInfo(memory);
    }

    /**
     * We use this method to restore the state information of the bundle (it's called after onDestroy())
     * @param memory the bundle
     */
    public void onRestoreInstanceState(Bundle memory) {
        nickname = memory.getString("nickname");
        lastScore = memory.getInt("lastScore");
        bestScore = memory.getInt("bestScore");
        imageHunter = memory.getString("imageHunter");
        imageTarget = memory.getString("imageTarget");
        imageCorpse = memory.getString("imageCorpse");

        // Update screen display
        if(nickname != null) {
            if(lastScore == 0)
                lastGame.setText(nickname + " will  play ...");
            else
                lastGame.setText("Last game : " + nickname + " | Score : " + lastScore);
        }
    }

    /**
     * Start the game activity
     * @param v the view
     */
    public void play(View v) {
        Intent intentPlay = new Intent(this, Play.class);
        if(nickname == null)
            nickname = "Default user";
        intentPlay.putExtra("nickname", nickname);
        intentPlay.putExtra("bestScore", bestScore);
        if(imageHunter == null)
            imageHunter = "cat";
        intentPlay.putExtra("imageHunter", imageHunter);
        if(imageTarget == null)
            imageTarget = "mouse";
        intentPlay.putExtra("imageTarget", imageTarget);
        if(imageCorpse == null)
            imageCorpse = "blood";
        intentPlay.putExtra("imageCorpse", imageCorpse);
        startActivityForResult(intentPlay, 1);
    }

    /**
     * Launch the parameters menu
     * @param v the view
     */
    public void parameters(View v) {
        Intent intentParameters = new Intent(this, Parameters.class);
        startActivityForResult(intentParameters,2);
    }

    /**
     * Launch the score menu
     * @param v the view
     */
    public void highScores(View v) {
        Intent intentHighScores = new Intent(this, HighScores.class);
        startActivity(intentHighScores);
    }

    /**
     *
     * @param code 1 for the game, 2 for the parameters
     * @param status RESULT_OK if everything is all right
     * @param intent get the results from the activity
     */
    public void onActivityResult(int code, int status, Intent intent) {
        super.onActivityResult(code, status, intent);
        // Play activity
        if(code == 1) {
            if(status == RESULT_OK) {
                nickname = intent.getStringExtra("nickname");
                lastScore = intent.getIntExtra("lastScore",0);
                // Update the best score
                if(lastScore > bestScore)
                    bestScore = lastScore;
                lastGame.setText("Last game : " + nickname + " | Score : " + lastScore);
            }
        }
        // Parameters activity
        if(code == 2) {
            if(status == RESULT_OK) {
                nickname = intent.getStringExtra("resultNickname");
                imageHunter = intent.getStringExtra("resultChoiceHunter");
                imageTarget = intent.getStringExtra("resultChoiceTarget");
                imageCorpse = intent.getStringExtra("resultChoiceCorpse");
                lastGame.setText(nickname + " will  play ...");
            }
        }
    }

    /**
     * Exit the application
     * @param v the view
     */
    public void finish(View v) {
        this.finish();
    }

    public void onStart() {
        super.onStart();
    }

    public void onRestart() {
        super.onRestart();
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    public void onStop() {
        super.onStop();
    }

    public void onDestroy() {
        super.onDestroy();
    }
}