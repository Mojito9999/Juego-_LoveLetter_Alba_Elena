package com.example.jugeo_loveletter_alba_elena;

import java.util.ArrayList;

public class Robo extends Mazo {
    private boolean esVacio;
    private boolean esEFA;

    // tenemos que pasar si o si por parametros la inicializacion, si lo seteamos se
    // nos pasarian las cartas de las dos versiones al mazo
    public Robo() {
        super();
        this.esVacio = true;
            crearMazoCartasInicioPartidaNormal();


    }

    public boolean isEsVacio() {
        return esVacio;
    }

    public void setEsVacio(boolean esVacio) {
        this.esVacio = esVacio;
    }

    public boolean isEsEFA() {
        return esEFA;
    }

    public void setEsEFA(boolean esEFA) {
        this.esEFA = esEFA;
    }

    private void crearMazoCartasInicioPartidaNormal() {
        this.cartas = new ArrayList<>();

        try {
            Carta princesa = new Carta(1, "Princesa", 9, "@drawable/princesa");
            Carta condesa = new Carta(1, "Condesa", 8, "@drawable/condesa");
            Carta rey = new Carta(1, "Rey", 7, "@drawable/rey");
            Carta principe = new Carta(2, "Principe", 5, "@drawable/principe");
            Carta doncella = new Carta(2, "Doncella", 4, "@drawable/doncella");
            Carta baron = new Carta(2, "Baron", 3, "@drawable/baron");
            Carta sacerdote = new Carta(2, "Sacerdote", 2, "@drawable/sacerdote");
            Carta guardia = new Carta(5, "Guardia", 1, "@drawable/guardia");

            // CREACION DEL MAZO CON TODAS LAS CARTA
            // Princesa

            cartas.add(princesa);

            // Condesa

            cartas.add(condesa);

            // Rey

            cartas.add(rey);

            // Principe
            for (int i = 0; i < principe.getCantidadCartas(); i++) {
                cartas.add(principe);
            }
            // Docella
            for (int i = 0; i < doncella.getCantidadCartas(); i++) {
                cartas.add(doncella);
            }
            // Baron
            for (int i = 0; i < baron.getCantidadCartas(); i++) {
                cartas.add(baron);
            }
            // Sacerdote
            for (int i = 0; i < sacerdote.getCantidadCartas(); i++) {
                cartas.add(sacerdote);
            }
            // Guardian
            for (int i = 0; i < guardia.getCantidadCartas(); i++) {
                cartas.add(guardia);
            }

            // Barajar cartas
            this.cartas = barajarMazo(cartas);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    private void crearMazoCartasInicioPartidaEFA() {
        this.cartas = new ArrayList<>();

        try {
            Carta princesa = new Carta(1, "Princesa", 9, "resources/cartas_efa/princesa.jpg");
            Carta condesa = new Carta(1, "Condesa", 8, "resources/cartas_efa/condesa.jpg");
            Carta rey = new Carta(1, "Rey", 7, "resources/cartas_efa/el_king.jpg");
            Carta principe = new Carta(2, "Principe", 5, "resources/cartas_efa/principe.jpg");
            Carta doncella = new Carta(2, "Doncella", 4, "resources/cartas_efa/doncella_en_apuros.jpg");
            Carta baron = new Carta(2, "Baron", 3, "resources/cartas_efa/baron.jpg");
            Carta sacerdote = new Carta(2, "Sacerdote", 2, "resources/cartas_efa/sacerdote.jpg");
            Carta guardia = new Carta(5, "Guardia", 1, "resources/cartas_efa/guardias.jpg");

            // CREACION DEL MAZO CON TODAS LAS CARTA
            // Princesa

            cartas.add(princesa);

            // Condesa

            cartas.add(condesa);

            // Rey

            cartas.add(rey);

            // Principe
            for (int i = 0; i < principe.getCantidadCartas(); i++) {
                cartas.add(principe);
            }
            // Docella
            for (int i = 0; i < doncella.getCantidadCartas(); i++) {
                cartas.add(doncella);
            }
            // Baron
            for (int i = 0; i < baron.getCantidadCartas(); i++) {
                cartas.add(baron);
            }
            // Sacerdote
            for (int i = 0; i < sacerdote.getCantidadCartas(); i++) {
                cartas.add(sacerdote);
            }
            // Guardian
            for (int i = 0; i < guardia.getCantidadCartas(); i++) {
                cartas.add(guardia);
            }

            // Barajar cartas
            this.cartas = barajarMazo(cartas);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    private ArrayList<Carta> barajarMazo(ArrayList<Carta> cartas) {
        ArrayList<Carta> cartasBarajadas = null;
        int random;
        try {
            cartasBarajadas = new ArrayList<>();

            // Nos barajamos el mazo de manera aleatoria.
            while (!cartas.isEmpty()) {
                random = (int) (0 + Math.random() * cartas.size());
                cartasBarajadas.add(cartas.get(random));
                cartas.remove(random);
            }

            // aqui ya tenemos cartas
            this.esVacio = false;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return cartasBarajadas;

    }

    public Carta robarCarta() {

        Carta carta = new Carta();

        try {
            carta = this.cartas.get(0);
            this.cartas.remove(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return carta;

    }

    public boolean finMazoRobo() {

        try {
            if (this.cartas.size() == 0) {
                this.esVacio = true;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return esVacio;

    }
}
