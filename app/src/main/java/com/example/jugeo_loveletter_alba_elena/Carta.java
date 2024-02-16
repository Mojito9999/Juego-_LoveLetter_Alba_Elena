package com.example.jugeo_loveletter_alba_elena;


public class Carta {

    public int cantidadCartas;
    public String tipoCarta;
    public int numCarta;
    public boolean esMarcada100;

    public String rutaImagen;

    public Carta() {

    }

    public Carta(int cantidadCartas, String tipoCarta, int numCarta, String rutaImagen) {
        super();
        this.cantidadCartas = cantidadCartas;
        this.tipoCarta = tipoCarta;
        this.numCarta = numCarta;
        this.esMarcada100 = false;

        this.rutaImagen = rutaImagen;
    }

    public int getCantidadCartas() {
        return cantidadCartas;
    }

    public void setCantidadCartas(int cantidadCartas) {
        this.cantidadCartas = cantidadCartas;
    }

    public String getTipoCarta() {
        return tipoCarta;
    }

    public void setTipoCarta(String tipoCarta) {
        this.tipoCarta = tipoCarta;
    }

    public int getNumCarta() {
        return numCarta;
    }

    public void setNumCarta(int numCarta) {
        this.numCarta = numCarta;
    }

    public boolean isEsMarcada100() {
        return esMarcada100;
    }

    public void setEsMarcada100(boolean esMarcada100) {
        this.esMarcada100 = esMarcada100;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

}

