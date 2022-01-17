package com.example.catchit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class Parameters extends Activity implements AdapterView.OnItemSelectedListener {
    EditText editText;

    Spinner spinHunter;
    Spinner spinTarget;
    Spinner spinResult;

    String[] choice = {"cat", "mouse", "blood", "police", "thief", "prisoner"};
    String choiceHunter;
    String choiceTarget;
    String choiceCorpse;

    /**
     * Create the editText for the nickname and the three spinners to choose the images
     * @param savedInstanceState the bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameters);
        // Get nickname
        editText = findViewById(R.id.nickname);

        // Getting the instance of Spinner and applying OnItemSelectedListener on it
        spinHunter = findViewById(R.id.hunter);
        spinHunter.setOnItemSelectedListener(this);

        spinTarget = findViewById(R.id.target);
        spinTarget.setOnItemSelectedListener(this);

        spinResult = findViewById(R.id.corpse);
        spinResult.setOnItemSelectedListener(this);

        // Creating the ArrayAdapter instance having the country list
        ArrayAdapter<String> aaHunter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, choice);
        aaHunter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Setting the ArrayAdapter data on the Spinner
        spinHunter.setAdapter(aaHunter);
        spinHunter.setSelection(0);

        ArrayAdapter<String> aaHunted = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, choice);
        aaHunted.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTarget.setAdapter(aaHunted);
        spinTarget.setSelection(1);

        ArrayAdapter<String> aaResult = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, choice);
        aaResult.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinResult.setAdapter(aaResult);
        spinResult.setSelection(2);
    }

    /**
     * Performing action onItemSelected
     */
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        if(arg0.getId() == R.id.hunter)
            choiceHunter = choice[position];
        if(arg0.getId() == R.id.target)
            choiceTarget = choice[position];
        if(arg0.getId() == R.id.corpse)
            choiceCorpse = choice[position];
    }

    /**
     * Default action
     * @param arg0 the arguments
     */
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        choiceHunter = choice[0];
        choiceTarget = choice[1];
        choiceCorpse = choice[2];
    }

    /**
     * Finish the Parameters activity and give the different choices to HomePage activity
     * @param v the view
     */
    public void validate(View v) {
        Intent result = new Intent();
        if(editText.getText().toString().equals(""))
            editText.setText("Default User");
        result.putExtra("resultNickname", editText.getText().toString());
        result.putExtra("resultChoiceHunter", choiceHunter);
        result.putExtra("resultChoiceTarget", choiceTarget);
        result.putExtra("resultChoiceCorpse", choiceCorpse);
        setResult(RESULT_OK, result);
        this.finish();
    }
}
