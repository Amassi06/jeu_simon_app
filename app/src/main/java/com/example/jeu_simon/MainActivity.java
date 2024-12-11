package com.example.jeu_simon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.content.SharedPreferences;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private TextView score1, score2, score3;
    private static final String PREFS_NAME = "player_scores"; // Nom du fichier de préférences


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_play = findViewById(R.id.app_play);
        ImageButton btn_exit = findViewById(R.id.exit);

        score1 = findViewById(R.id.first);
        score2 = findViewById(R.id.second);
        score3 = findViewById(R.id.third);

        // Initialiser les préférences partagées
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        btn_play.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            startActivity(intent);
        });

        loadScores();

        btn_exit.setOnClickListener(v -> finish());
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadScores(); // Load scores each time the activity is resumed
    }
    private void loadScores() {
        String scoreS1 = sharedPreferences.getString("SCORE_1", "Score 1: 0");
        String scoreS2 = sharedPreferences.getString("SCORE_2", "Score 2: 0");
        String scoreS3 = sharedPreferences.getString("SCORE_3", "Score 3: 0");

        score1.setText(scoreS1);
        score2.setText(scoreS2);
        score3.setText(scoreS3);
    }
}
