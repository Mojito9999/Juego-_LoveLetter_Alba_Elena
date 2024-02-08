package com.example.jugeo_loveletter_alba_elena;

/*public class Partida {

     Partida es la clase donde tenemos lo del anterior controlador

    private boolean esFin;
    private boolean esEmpiezaBot;


    public Partida() {
        super();
        this.esFin = false;

        int random = (int) (1 + Math.random() * 2);

        if (random == 1) {
            this.esEmpiezaBot = true;
        } else {
            this.esEmpiezaBot = false;
        }
    }

    public boolean isEsFin() {
        return esFin;
    }

    public boolean isEsEmpiezaBot() {
        return esEmpiezaBot;
    }

    public void setEsEmpiezaBot(boolean esEmpiezaBot) {
        this.esEmpiezaBot = esEmpiezaBot;
    }

    public void setEsFin(boolean esFin) {
        this.esFin = esFin;
    }

    public void empezarPartida(Jugador jugador, Bot bot, Robo mazo) {

        if(this.esEmpiezaBot) {
            bot.setMano(mazo.robarCarta());
            jugador.setMano(mazo.robarCarta());
            bot.setRobo(mazo.robarCarta());
        }else {
            jugador.setMano(mazo.robarCarta());
            bot.setMano(mazo.robarCarta());
            jugador.setRobo(mazo.robarCarta());
        }

    }

    public void reorganizarManoJugador(Jugador jugador) {
        jugador.setMano(jugador.getRobo());

    }

    public void reorganizarManoBot(Bot bot) {
        bot.setMano(bot.getRobo());

    }

    //Metodo para comprobar si es el final de la partida
    public boolean esFinPartida (Robo robo) {

        setEsFin(robo.finMazoRobo());

        return isEsFin();

    }


    public boolean hayPrincesaDescartes(Descarte descarte) {
        boolean princesa = false;
        try {
            if (descarte.getCartas().get((descarte.getCartas().size()) - 1).getTipoCarta().equals("Princesa")) {
                princesa = true;
            }
        } catch (Exception e) {
            // TODO: handle exception
            return princesa;
        }

        return princesa;

    }

    public boolean hayDoncellaDescartes(Descarte descarte) {
        boolean doncella = false;
        try {
            if (descarte.getCartas().get((descarte.getCartas().size()) - 1).getTipoCarta().equals("Doncella")) {
                doncella = true;
            }
        } catch (Exception e) {
            // TODO: handle exception
            return doncella;
        }

        return doncella;

    }
}*/
