package com.example.jugeo_loveletter_alba_elena;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import android.widget.AdapterView;

import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.Spinner;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SeleccionCarta extends AppCompatActivity {
    private Spinner spinnerCartas;
    Button btnAceptarAdivinar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_carta);

        spinnerCartas=findViewById(R.id.spinnerCartas);

        btnAceptarAdivinar = findViewById(R.id.btnAceptarAdivinar);

        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this, R.array.cartas, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        spinnerCartas.setAdapter(adapter);
        btnAceptarAdivinar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedCarta = spinnerCartas.getSelectedItem().toString();

                // Crear un Intent para pasar datos a MainActivity2
                Intent intent = new Intent(SeleccionCarta.this, MainActivity2.class);
                intent.putExtra("selectedCarta", selectedCarta);
                startActivity(intent);

                // Cambiar la vista al MainActivity2
                startActivity(new Intent(SeleccionCarta.this, MainActivity2.class));
            }
        });

    }



}