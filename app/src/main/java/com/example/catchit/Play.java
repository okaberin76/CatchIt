package com.example.catchit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Play extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;

    private ImageView imageViewHunter, imageViewVictim;

    private int xHunter, yHunter;
    private int xVictim, yVictim;

    private RelativeLayout relativeLayout;

    private ArrayList<ImageView> arrayVictim, arrayDead;

    private TextView nicknameTV, scoreTV, bestScoreTV;

    private String nickname;
    private int count, bestScore;
    private String imageHunter, imageTarget, imageCorpse;

    private final Random random = new Random();

    /**
     * Create the game, set images and the sensor
     * @param savedInstanceState the bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        // Screen orientation and awake
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Sensor
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Layout
        relativeLayout = findViewById(R.id.layout);

        // Get the parameters to run the game
        Intent intent = getIntent();
        nickname = intent.getStringExtra("nickname");
        bestScore = intent.getIntExtra("bestScore", 0);
        imageHunter = intent.getStringExtra("imageHunter");
        imageTarget = intent.getStringExtra("imageTarget");
        imageCorpse = intent.getStringExtra("imageCorpse");

        // Score
        count = 0;

        // List of ImageView
        arrayVictim = new ArrayList<>();
        arrayDead = new ArrayList<>();

        // Display the hunter
        imageViewHunter = newHunter(imageHunter);
        // Display the hunted
        imageViewVictim = newRandomVictim(imageTarget);

        // Display TextView
        nicknameTV = newNickname(nickname);
        scoreTV = newScore(count);
        bestScoreTV = newBestScore(bestScore);
    }

    /**
     * Register the listener when the game start
     */
    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * Stop the listener if the game is paused
     */
    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    /**
     * @return width of the screen
     */
    public int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * @return height of the screen
     */
    public int getScreenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /**
     * @param imageV an ImageView
     * @return width of the ImageView given
     */
    public static int getImageViewWidth(ImageView imageV) {
        return imageV.getDrawable().getIntrinsicWidth();
    }

    /**
     * @param imageV an ImageView
     * @return height of the ImageView given
     */
    public static int getImageViewHeight(ImageView imageV) {
        return  imageV.getDrawable().getIntrinsicHeight();
    }

    /**
     * Set the image to the Image View
     * @param image the name of the image we want to apply
     * @param imageV the imageview with the correct image
     */
    public void setImage(String image, ImageView imageV) {
        switch (image) {
            case "mouse" :  imageV.setImageResource(R.drawable.mouse);
                break;
            case "blood" :  imageV.setImageResource(R.drawable.blood);
                break;
            case "police" : imageV.setImageResource(R.drawable.police);
                break;
            case "thief" : imageV.setImageResource(R.drawable.thief);
                break;
            case "prisoner" : imageV.setImageResource(R.drawable.prisoner);
                break;
            default : imageV.setImageResource(R.drawable.cat);
        }
    }

    /**
     * Create the Text View of the nickname
     * @param text the pseudo of the player (default : Default user)
     * @return the textview for the pseudo
     */
    public TextView newNickname(String text) {
        TextView nicknameTV = new TextView(this);

        nicknameTV.setText(text);
        nicknameTV.setX(5);
        nicknameTV.setY(15);
        nicknameTV.setTextColor(Color.parseColor("#000000"));
        relativeLayout.addView(nicknameTV);
        return nicknameTV;
    }

    /**
     * Create the Text View for the score
     * @param score the actual score (start at 0)
     * @return the textview for the actual score
     */
    public TextView newScore(int score) {
        TextView scoreTV = new TextView(this);
        scoreTV.setText("Score : " + score);
        scoreTV.setX(5);
        scoreTV.setY(75);
        scoreTV.setTextColor(Color.parseColor("#000000"));
        relativeLayout.addView(scoreTV);
        return scoreTV;
    }

    /**
     * Create the Text View for the best score to beat
     * @param score the best score to beat (in the session)
     * @return the textview for the best score
     */
    public TextView newBestScore(int score) {
        TextView bestScoreTV = new TextView(this);
        bestScoreTV.setText("Best score : " + score);
        bestScoreTV.setX(5);
        bestScoreTV.setY(135);
        bestScoreTV.setTextColor(Color.parseColor("#000000"));
        relativeLayout.addView(bestScoreTV);
        return bestScoreTV;
    }

    /**
     * Create the hunter
     * @return the hunter
     */
    public ImageView newHunter(String image) {
        ImageView imageV = new ImageView(this);

        // Center the position on the screen
        xHunter = getScreenWidth() / 2;
        yHunter = getScreenHeight() / 2;
        imageV.setX(xHunter);
        imageV.setY(yHunter);
        // Image options
        setImage(image, imageV);
        // Add to layout so the image will be displayed
        relativeLayout.addView(imageV);
        return imageV;
    }

    /**
     * Create a new target
     * @return the target / victim
     */
    public ImageView newRandomVictim(String image) {
        ImageView imageV = new ImageView(this);

        // Random position on the screen
        xVictim = random.nextInt(getScreenWidth() - (3 * getImageViewWidth(imageViewHunter)));
        yVictim = random.nextInt(getScreenHeight() - (3 * getImageViewHeight(imageViewHunter)));
        imageV.setX(xVictim);
        imageV.setY(yVictim);

        // Image options
        setImage(image, imageV);
        // Add to layout so the image will be displayed
        relativeLayout.addView(imageV);
        // Add to the list of victims
        arrayVictim.add(imageV);
        return imageV;
    }

    /**
     * Create the corpse at the position of the target after the hunter catch it
     * @param image the corpse
     */
    public void newVictimDead(String image) {
        ImageView imageV = new ImageView(this);

        // Position of the last victim
        imageV.setX(xVictim);
        imageV.setY(yVictim);
        // Image options
        setImage(image, imageV);
        // Add to layout so the image will be displayed
        relativeLayout.addView(imageV);
        // Add to the list of victims and make the victim invisible
        arrayDead.add(imageV);
        arrayVictim.get(count).setVisibility(View.GONE);
        // Score updated
        count++;
        scoreTV.setText("Score : " + count);
    }

    /**
     * @return check if the hunter touch the victim
     */
    public boolean checkCollision() {
        return (Math.abs(xHunter) - Math.abs(xVictim) > -getImageViewWidth(imageViewVictim) / 2
                && Math.abs(xHunter) - Math.abs(xVictim) < getImageViewWidth(imageViewVictim) / 2)
                &&  (Math.abs(yHunter) - Math.abs(yVictim) > -getImageViewHeight(imageViewVictim) / 2
                && Math.abs(yHunter) - Math.abs(yVictim) < getImageViewHeight(imageViewVictim) / 2);
    }

    /**
     * Conditions to finish the game
     * @return a boolean
     */
    public boolean checkGameOver() {
        for(int i = 0; i < arrayDead.size() - 1; i++) {
            if((Math.abs(xHunter) - Math.abs(arrayDead.get(i).getX()) > (float) -getImageViewWidth(imageViewVictim) / 2
                    && Math.abs(xHunter) - Math.abs(arrayDead.get(i).getX()) < (float) getImageViewWidth(imageViewVictim) / 2)
                    &&  (Math.abs(yHunter) - Math.abs(arrayDead.get(i).getY()) > (float) -getImageViewHeight(imageViewVictim) / 2
                    && Math.abs(yHunter) - Math.abs(arrayDead.get(i).getY()) < (float) getImageViewHeight(imageViewVictim) / 2)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Create two textview to show a game over screen
     */
    public void gameOverScreen() {
        TextView textViewGameOver = new TextView(this);
        textViewGameOver.setText("GAME OVER !");
        textViewGameOver.setTextSize(64);
        textViewGameOver.setTextColor(Color.BLACK);
        textViewGameOver.setX(500);
        textViewGameOver.setY(100);
        relativeLayout.addView(textViewGameOver);

        TextView textViewScore = new TextView(this);
        textViewScore.setText("Score : " + count);
        textViewScore.setTextSize(50);
        textViewScore.setTextColor(Color.BLACK);
        textViewScore.setX(800);
        textViewScore.setY(350);
        relativeLayout.addView(textViewScore);
    }

    /**
     * Create a button to return to the main menu
     */
    public void gameOverButton() {
        Button buttonMenu = new Button(this);
        buttonMenu.setWidth(500);
        buttonMenu.setX(850);
        buttonMenu.setY(600);
        buttonMenu.setText("Back to menu");
        relativeLayout.addView(buttonMenu);
        buttonMenu.setOnClickListener(view -> {
            Intent result = new Intent();
            result.putExtra("nickname", nickname);
            result.putExtra("lastScore", count);
            setResult(RESULT_OK, result);
            this.finish();
        });
    }

    /**
     * Save the data of the activity in a sharedPreferences
     */
    public void saveData() {
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        int n = prefs.getInt("size",0);
        editor.putInt("size", n + 1);
        editor.putString("nickname_" + n, nickname);
        editor.putInt("lastScore_" + n, count);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE);
        Date date = new Date();
        editor.putString("date_" + n, dateFormat.format(date));
        editor.apply();
    }

    /**
     * Called when there is a new event. Here, it update the location of the imageView.
     * @param event an event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        // When you move thanks to the accelerometer
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // * 2 to increase the speed of the hunter
            xHunter += (int) event.values[1] * 2;
            yHunter += (int) event.values[0] * 2;
            /*
                Limits of the screen. Jump to the other side of the screen when you are about to
                leave the screen. For example, if you leave the screen on the left, you will come
                back instantly via the right.
             */
            // Right
            if(xHunter > getScreenWidth())
                xHunter = -getImageViewWidth(imageViewHunter);
            // Left
            else if(xHunter < -getImageViewWidth(imageViewHunter))
                xHunter = getScreenWidth();
            // Top
            else if(yHunter < -getImageViewHeight(imageViewHunter))
                yHunter = getScreenHeight();
            // Bottom
            else if(yHunter > getScreenHeight())
                yHunter = -getImageViewHeight(imageViewHunter);

            // New value
            imageViewHunter.setY(yHunter);
            imageViewHunter.setX(xHunter);

            if(checkCollision()) {
                newVictimDead(imageCorpse);
                newRandomVictim(imageTarget);
            }

            // Save the data and end the acivity
            if(checkGameOver()) {
                sensorManager.unregisterListener(this);
                saveData();
                gameOverScreen();
                gameOverButton();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}