package com.example.jugeo_loveletter_alba_elena;

public class Bot {
    private Carta mano;
    private Carta robo;
    private boolean perdedor;

    public Bot() {

        this.perdedor = false;
    }

    public Carta getMano() {
        return mano;
    }

    public void setMano(Carta mano) {
        this.mano = mano;
    }

    public Carta getRobo() {
        return robo;
    }

    public void setRobo(Carta robo) {
        this.robo = robo;
    }

    public boolean isPerdedor() {
        return perdedor;
    }

    public void setPerdedor(boolean perdedor) {
        this.perdedor = perdedor;
    }

}
