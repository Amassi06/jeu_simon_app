package com.example.jeu_simon;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.media.MediaPlayer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private Button btnGreen, btnRed, btnYellow, btnBlue, btnStart;
    private TextView tvMessage, plr_name, Viewscore;

    private static int score = 0;
    private List<Integer> sequence = new ArrayList<>();
    private List<Integer> userSequence = new ArrayList<>();
    private Handler handler = new Handler();
    private Random random = new Random();
    private boolean isUserTurn = false;
    private static MediaPlayer[] sons;
    private String player_name;
    private static final long BASE_DELAY = 1000; // Base delay in milliseconds
    private static final float DELAY_MULTIPLIER = 0.9f; // Delay multiplier to decrease delay over time
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "player_scores"; // Nom du fichier de préférences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        sons = new MediaPlayer[]{
                MediaPlayer.create(this, R.raw.son1),
                MediaPlayer.create(this, R.raw.son2),
                MediaPlayer.create(this, R.raw.son3),
                MediaPlayer.create(this, R.raw.son4),
                MediaPlayer.create(this, R.raw.error)
        };

        plr_name = findViewById(R.id.playerNameGame);
        player_name = getIntent().getStringExtra("PLAYER_NAME");
        plr_name.setText(player_name);
        Viewscore = findViewById(R.id.Viewscore);
        Viewscore.setText(String.valueOf(score));

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        btnGreen = findViewById(R.id.btnGreen);
        btnRed = findViewById(R.id.btnRed);
        btnYellow = findViewById(R.id.btnYellow);
        btnBlue = findViewById(R.id.btnBlue);
        btnStart = findViewById(R.id.btnStart);
        tvMessage = findViewById(R.id.tvMessage);

        btnStart.setOnClickListener(v -> startGame());

        btnGreen.setOnClickListener(v -> onColorButtonClicked(0));
        btnRed.setOnClickListener(v -> onColorButtonClicked(1));
        btnYellow.setOnClickListener(v -> onColorButtonClicked(2));
        btnBlue.setOnClickListener(v -> onColorButtonClicked(3));
    }

    private void startGame() {
        ViewGroup btn = (ViewGroup) btnStart.getParent();
        btn.removeView(btnStart); // Enlever le bouton démarrer

        sequence.clear();
        userSequence.clear();
        isUserTurn = false;
        tvMessage.setText("Regardez la séquence...");
        addColorToSequence();
    }

    private void addColorToSequence() {
        sequence.add(random.nextInt(4));
        playSequence();
    }

    private void playSequence() {
        isUserTurn = false;
        userSequence.clear();
        long delay = (long) (BASE_DELAY * Math.pow(DELAY_MULTIPLIER, score));

        handler.postDelayed(() -> {
            for (int i = 0; i < sequence.size(); i++) {
                final int color = sequence.get(i);
                handler.postDelayed(() -> showColor(color), i * delay);
                handler.postDelayed(this::hideColors, i * delay + delay * 2 / 3); // Ajuster également le délai de masquage
            }
            handler.postDelayed(() -> {
                tvMessage.setText("Votre tour!");
                isUserTurn = true;
            }, sequence.size() * delay);
        }, 500);
    }

    private void showColor(int color) {
        switch (color) {
            case 0:
                sons[0].start();
                btnGreen.setBackgroundColor(ContextCompat.getColor(this, R.color.simon_green_pressed));
                break;
            case 1:
                sons[1].start();
                btnRed.setBackgroundColor(ContextCompat.getColor(this, R.color.simon_red_pressed));
                break;
            case 2:
                sons[2].start();
                btnYellow.setBackgroundColor(ContextCompat.getColor(this, R.color.simon_yellow_pressed));
                break;
            case 3:
                sons[3].start();
                btnBlue.setBackgroundColor(ContextCompat.getColor(this, R.color.simon_blue_pressed));
                break;
        }
    }

    private void hideColors() {
        btnGreen.setBackgroundColor(ContextCompat.getColor(this, R.color.simon_green));
        btnRed.setBackgroundColor(ContextCompat.getColor(this, R.color.simon_red));
        btnYellow.setBackgroundColor(ContextCompat.getColor(this, R.color.simon_yellow));
        btnBlue.setBackgroundColor(ContextCompat.getColor(this, R.color.simon_blue));
    }

    private void saveScore(int score) {
        String score1 = sharedPreferences.getString("SCORE_1", "");
        String score2 = sharedPreferences.getString("SCORE_2", "");
        String score3 = sharedPreferences.getString("SCORE_3", "");

        // Vérifier où insérer le nouveau score avec le nom du joueur
        if (score > extractScore(score1)) {
            sharedPreferences.edit().putString("SCORE_3", score2).apply();
            sharedPreferences.edit().putString("SCORE_2", score1).apply();
            sharedPreferences.edit().putString("SCORE_1", player_name + ": " + score).apply();
        } else if (score > extractScore(score2)) {
            sharedPreferences.edit().putString("SCORE_3", score2).apply();
            sharedPreferences.edit().putString("SCORE_2", player_name + ": " + score).apply();
        } else if (score > extractScore(score3)) {
            sharedPreferences.edit().putString("SCORE_3", player_name + ": " + score).apply();
        }
    }

    // Méthode pour extraire le score d'une chaîne de caractères
    private int extractScore(String scoreEntry) {
        if (scoreEntry.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(scoreEntry.split(": ")[1]);
        } catch (Exception e) {
            return 0;
        }
    }

    private void onColorButtonClicked(int color) {
        if (!isUserTurn) return;
        userSequence.add(color);

        switch (color) {
            case 0:
                sons[0].start();
                break;
            case 1:
                sons[1].start();
                break;
            case 2:
                sons[2].start();
                break;
            case 3:
                sons[3].start();
                break;
        }
        if (userSequence.size() == sequence.size()) {
            isUserTurn = false;
            if (userSequence.equals(sequence)) {
                tvMessage.setText("Bien joué! Prochaine séquence...");
                ++score;
                Viewscore.setText(String.valueOf(score));
                handler.postDelayed(this::addColorToSequence, 1000);
            } else {
                tvMessage.setText("Perdu! Réessayez.");
                sons[4].start();
                saveScore(score);
                Toast.makeText(this, "Séquence incorrecte!", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Perdu")
                        .setMessage("Malheureusement vous avez perdu, tentez votre chance une autre fois.\n" + player_name + " : " + score)
                        .setPositiveButton("Rejouer", (dialog, id) -> {
                            dialog.dismiss();
                            finish();
                            score = 0;
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }
}
