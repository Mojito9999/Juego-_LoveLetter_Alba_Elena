package com.example.jugeo_loveletter_alba_elena;

import java.util.ArrayList;

public abstract class Mazo {
    protected ArrayList<Carta> cartas;

    public ArrayList<Carta> getCartas() {
        return cartas;
    }

    public void setCartas(ArrayList<Carta> cartas) {
        this.cartas = cartas;
    }
}
