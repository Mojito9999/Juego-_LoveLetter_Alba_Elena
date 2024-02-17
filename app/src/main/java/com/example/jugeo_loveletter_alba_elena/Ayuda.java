package com.example.jugeo_loveletter_alba_elena;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Ayuda extends AppCompatActivity {

    Button volver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda);

        volver = findViewById(R.id.volver);
    }

    public void volverJuego(View view) {

            Log.d("MiApp", "Iniciando MainActivity2");
            Intent intent = new Intent(this, MainActivity2.class);
            startActivity(intent);

    }
}