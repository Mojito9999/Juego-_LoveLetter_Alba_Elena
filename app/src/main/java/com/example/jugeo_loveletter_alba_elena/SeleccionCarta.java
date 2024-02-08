package com.example.jugeo_loveletter_alba_elena;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;

import android.widget.AdapterView;

import android.widget.ArrayAdapter;

import android.widget.Spinner;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SeleccionCarta extends AppCompatActivity {
    private Spinner spinnerCartas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_carta);

        spinnerCartas=findViewById(R.id.spinnerCartas);


        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this, R.array.cartas, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        spinnerCartas.setAdapter(adapter);
    }
}