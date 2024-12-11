package com.example.jeu_simon;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Initialisation des widgets
        Button btnPlay = findViewById(R.id.btnGameStart);
        EditText editName = findViewById(R.id.playerName);
        ImageButton btnBack = findViewById(R.id.back);

        // Action du bouton pour commencer le jeu
        btnPlay.setOnClickListener(v -> {
            String playerName = editName.getText().toString().trim(); // trim() pour enlever les espaces au début et à la fin

            if (!playerName.isEmpty()) {
                // Démarrage de la nouvelle activité si le champ n'est pas vide
                Intent intent = new Intent(MainActivity2.this, GameActivity.class);
                intent.putExtra("PLAYER_NAME", playerName); // Envoi du nom du joueur à l'activité suivante
                startActivity(intent);
            } else {
                // Message d'erreur si le champ est vide
                Toast.makeText(MainActivity2.this, "Veuillez entrer votre nom, s'il vous plaît.", Toast.LENGTH_SHORT).show();
            }
        });

        // Action du bouton retour
        btnBack.setOnClickListener(v -> finish());
    }
}
