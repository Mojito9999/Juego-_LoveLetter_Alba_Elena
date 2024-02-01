package com.example.jugeo_loveletter_alba_elena;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Juego extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);
    }
    public Controlador(Vista vista) {
        this.vista = vista;
        this.vista.btnAceptarInicio.addActionListener(this);
        this.vista.btnAceptarAdivinar.addActionListener(this);
        this.vista.btnAceptarPrincipe.addActionListener(this);
        this.vista.btnRetroalimentacion.addActionListener(this);
        this.vista.btnMano1.addActionListener(this);
        this.vista.btnMano2.addActionListener(this);
        this.vista.tglbtnCambiarVersion.addActionListener(this);

        // inicializacion
        this.partida = new Partida();
        this.bot = new Bot();
        this.jugador = new Jugador();
        this.descarte = new Descarte();

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

        try {

            // PANEL INICIO
            // version
            if (e.getSource() == vista.tglbtnCambiarVersion) {

                if (vista.tglbtnCambiarVersion.getText().equals("Clásica")) {
                    vista.tglbtnCambiarVersion.setText("EFA");

                } else {
                    vista.tglbtnCambiarVersion.setText("Clásica");

                }

            }

            if (e.getSource() == vista.btnAceptarInicio) {
                // musica= new Musica("resources/sonidos/perder.mp3");

                // version
                if (vista.tglbtnCambiarVersion.getText().equals("EFA")) {
                    robo = new Robo(true);

                } else {
                    robo = new Robo(false);
                }

                // nombre jugador
                jugador.setNombre(this.vista.textNombreJugador.getText());

                this.vista.lblNombreJugador.setText(jugador.getNombre());

                vista.panelInicial.setVisible(false);
                vista.panelPartida.setVisible(true);

                // empezamos la partida
                partida.empezarPartida(jugador, bot, robo);
                vista.btnMano1.setIcon(new ImageIcon(jugador.getMano().getRutaImagen()));
                // comprobamos quien empieza y fijamos el primer robo del jugador
                if (!partida.isEsEmpiezaBot()) {
                    vista.btnMano2.setIcon(new ImageIcon(jugador.getRobo().getRutaImagen()));
                } else {
                    vista.btnMano1.setEnabled(false);
                    vista.btnMano2.setEnabled(false);
                    // Juega el bot
                    jugarBot();
                }

            }

            // Jugando el jugador su carta de mano
            if (e.getSource() == vista.btnMano1) {
                // Actualizamos las imagenes al jugar la carta
                vista.lblDescartes.setIcon(new ImageIcon(jugador.getMano().getRutaImagen()));
                vista.btnMano1.setIcon(new ImageIcon(jugador.getRobo().getRutaImagen()));
                vista.btnMano2.setIcon(new ImageIcon("resources/fondo_carta.jpg"));// CAMBIAR
                vista.btnMano1.setEnabled(false);
                vista.btnMano2.setEnabled(false);

                // FUNCIONALIDAD DE LA CARTA
                resolverJugador(jugador.getMano());
                partida.reorganizarManoJugador(jugador);
                jugador.setRobo(null);
                vista.btnRetroalimentacion.setEnabled(true);
                bot.setRobo(robo.robarCarta());

            }

            // Jugando el jugador su carta de robo
            if (e.getSource() == vista.btnMano2) {
                // Actualizamos las imagenes al jugar la carta
                vista.lblDescartes.setIcon(new ImageIcon(jugador.getRobo().getRutaImagen()));
                vista.btnMano2.setIcon(null);// CAMBIAR
                vista.btnMano1.setEnabled(false);
                vista.btnMano2.setEnabled(false);

                // FUNCIONALIDAD DE LA CARTA
                resolverJugador(jugador.getRobo());
                jugador.setRobo(null);
                vista.btnRetroalimentacion.setEnabled(true);
                bot.setRobo(robo.robarCarta());

            }
            // Accion del guardia
            if (e.getSource() == vista.btnAceptarAdivinar) {
                // Desactivamos el panel del guardia
                vista.panelAdivinar.setVisible(false);
                // Habilitamos el botón para continuar
                vista.btnRetroalimentacion.setEnabled(true);

                // Comprobamos si el guardia ha acertado
                if (vista.comboBoxAdivinar.getSelectedItem().toString().equals(bot.getMano().getTipoCarta())) {
                    bot.setPerdedor(true);
                    vista.lblRetroBot.setText("Has adivinado la carta del bot");
                    // System.out.println("Fin de partida, gana el jugador");
                    // partida finalizada
                } else {
                    vista.lblRetroBot.setText("No has adivinado la carta del bot");
                    // System.out.println("Suerte en la proxima");
                }
            }

            // Accion del principe
            if (e.getSource() == vista.btnAceptarPrincipe) {

                vista.panelPrincipe.setVisible(false);
                vista.btnRetroalimentacion.setEnabled(true);

                if (vista.comboBoxPrincipe.getSelectedIndex() == 0) {
                    vista.lblRetroBot.setText("El com.example.jugeo_loveletter_alba_elena.MainActivity.Bot ha descartado " + bot.getMano().getTipoCarta());
                    vista.lblDescartes.setIcon(new ImageIcon(bot.getMano().getRutaImagen()));
                    if (bot.getMano().getTipoCarta().equals("Princesa")) {
                        descarte.getCartas().add(bot.getMano());
                    }
                    if (!robo.finMazoRobo()) {
                        bot.setMano(robo.robarCarta());
                    } else {// COMPROBAR QUIEN TIENE LA CARTA MAS ALTA
                        partida.setEsFin(true);
                    }

                } else {
                    vista.lblRetroBot.setText("Descartas tu carta");
                    vista.lblDescartes.setIcon(new ImageIcon(jugador.getMano().getRutaImagen()));
                    if (jugador.getMano().getTipoCarta().equals("Princesa")) {
                        descarte.getCartas().add(jugador.getMano());
                    }
                    if (!robo.finMazoRobo()) {
                        jugador.setMano(robo.robarCarta());
                    } else {// COMPROBAR QUIEN TIENE LA CARTA MAS ALTA
                        partida.setEsFin(true);
                    }
                }

                // Comprobamos si alguien ha descartado la princisea en tal caso la partida
                // acaba
                if (partida.hayPrincesaDescartes(descarte)) {
                    partida.setEsFin(true);
                    // si es el bot el que ha descartado, pierde el bot, sino el jugador
                    if (vista.comboBoxPrincipe.getSelectedIndex() == 0) {
                        bot.setPerdedor(true);
                        partida.setEsFin(true);
                    } else {
                        jugador.setPerdedor(true);
                        partida.setEsFin(true);
                    }
                }
            }
            // cambiamos el turno
            if (e.getSource() == vista.btnRetroalimentacion) {

                // Comprobamos si se finaliza la partida
                if (partida.isEsFin()) {
                    resolverGanador();
                }

                if (bot.isPerdedor() || jugador.isPerdedor() || robo.finMazoRobo()) {
                    partida.setEsFin(true);
                    this.vista.lblRetroBot.setText("La partida ha terminado, pulsa aceptar");
                }

                // Comprobamos a quien le toca jugar
                if (jugador.getRobo() == null && !partida.isEsFin()) {
                    vista.btnMano1.setEnabled(false);
                    vista.btnMano2.setEnabled(false);
                    jugarBot();

                } else if (bot.getRobo() == null && !partida.isEsFin()) {
                    vista.btnMano1.setEnabled(true);
                    vista.btnMano2.setEnabled(true);

                    vista.lblCartaBot.setIcon(new ImageIcon("resources/trasera.jpg"));
                    vista.btnMano2.setIcon(new ImageIcon(jugador.getRobo().getRutaImagen()));

                }

            }
        } catch (Exception e2) {
            // TODO: handle exception
            e2.printStackTrace();
        }

    }

    public void jugarBot() {

        try {

            // actualizar barra progreso
            vista.progressBar.setValue(robo.getCartas().size());

            // COMPORTAMIENTO CONDESA
            if (bot.getMano().getTipoCarta().equals("Condesa") && (bot.getRobo().getTipoCarta().equals("Principe")
                    || bot.getRobo().getTipoCarta().equals("Rey"))) {
                resolverBot(bot.getMano());
                partida.reorganizarManoBot(bot);
                bot.setRobo(null);
            } else if (bot.getRobo().getTipoCarta().equals("Condesa")
                    && (bot.getMano().getTipoCarta().equals("Principe")
                    || bot.getMano().getTipoCarta().equals("Rey"))) {
                resolverBot(bot.getRobo());
                bot.setRobo(null);
            } else {
                if (bot.getMano().getNumCarta() > bot.getRobo().getNumCarta()
                        || bot.getMano().getNumCarta() == bot.getRobo().getNumCarta()) {
                    resolverBot(bot.getRobo());
                    bot.setRobo(null);
                } else {
                    resolverBot(bot.getMano());
                    partida.reorganizarManoBot(bot);
                    bot.setRobo(null);

                }
            }

            jugador.setRobo(robo.robarCarta());
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    // resolvemos la jugadora del jugador
    public void resolverJugador(Carta carta) {
        // comportamiento provocado cuando el jugador baja una carta de la mano a
        // descarte

        try {

            // actualizar barra progreso
            vista.progressBar.setValue(robo.getCartas().size());

            switch (carta.getTipoCarta()) {

                case "Guardia":// 1
                    // si bajas el guardian puedes adivinar la carta del contrincante, si se adivina
                    // ganas
                    // muestras panel de adivinar
                    // pulsan boton aceotar, recoges la info del combobox
                    // comparas la info del combo con la mano del contrario

                    // Comprobamos si hay doncella activa
                    if (!partida.hayDoncellaDescartes(descarte)) {
                        vista.panelAdivinar.setVisible(true);
                        vista.btnRetroalimentacion.setEnabled(false);
                    } else {
                        vista.lblRetroBot.setText("El bot esta protegido");
                    }

                    // System.out.println("JUGADOR JUEGA GUARDIA");

                    break;
                case "Sacerdote":// 2
                    // al bajar el sacerdote miras la carta del jugador contrario
                    // marcar la carta del contrario
                    if (!partida.hayDoncellaDescartes(descarte)) {
                        vista.lblCartaBot.setIcon(new ImageIcon(bot.getMano().getRutaImagen()));
                        vista.lblRetroBot.setText("El bot tiene " + bot.getMano().getTipoCarta());
                    } else {
                        vista.lblRetroBot.setText("El bot esta protegido");
                    }

                    // System.out.println("JUGADOR JUEGA SACERDOTE");
                    break;
                case "Baron":// 3
                    // al bajar el baron los jugadores muestran las cartas y el que tenga el numero
                    // mas alto gana
                    // comparar el numero de la carta de la mano del bot con el numero de la carta
                    // de tu mano
                    if (!partida.hayDoncellaDescartes(descarte)) {
                        vista.lblCartaBot.setIcon(new ImageIcon(bot.getMano().getRutaImagen()));
                        partida.reorganizarManoJugador(jugador);
                        if (jugador.getMano().getNumCarta() > bot.getMano().getNumCarta()) {
                            bot.setPerdedor(true);
                            vista.lblRetroBot.setText("Tu carta es más alta");
                            // System.out.println("Ha ganado el jugador");
                        } else if (bot.getMano().getNumCarta() > jugador.getMano().getNumCarta()) {
                            jugador.setPerdedor(true);
                            vista.lblRetroBot.setText("La carta del bot es más alta");
                            // System.out.println("Ha ganado el bot");
                        } else {
                            vista.lblRetroBot.setText("Empate. Continua la partida");// Si es empate no pasa nada
                        }
                    } else {
                        vista.lblRetroBot.setText("El bot esta protegido");
                    }
                    // System.out.println("JUGADOR JUEGA BARON");
                    break;
                case "Doncella":// 4
                    vista.lblRetroBot.setText("Te proteges con una doncella");
                    break;
                case "Principe":// 5
                    // si bajas principe el jugador contrario a que juega pone su carta de mano en
                    // descartes y roba una carta del mazo
                    if (!partida.hayDoncellaDescartes(descarte)) {
                        vista.panelPrincipe.setVisible(true);
                        vista.btnRetroalimentacion.setEnabled(false);
                    } else {
                        vista.lblRetroBot.setText("Descartas tu carta");
                        if (jugador.getMano().getTipoCarta().equals("Princesa")) {
                            descarte.getCartas().add(jugador.getMano());
                            jugador.setPerdedor(true);
                            partida.setEsFin(true);
                        }
                        jugador.setMano(null);
                        if (!robo.finMazoRobo()) {
                            jugador.setMano(robo.robarCarta());
                            vista.btnMano1.setIcon(new ImageIcon(jugador.getMano().getRutaImagen()));
                        } else {// COMPROBAR QUIEN TIENE LA CARTA MAS ALTA
                            partida.setEsFin(true);
                        }

                    }
                    // System.out.println("JUGADOR JUEGA PRINCIPE");
                    break;
                case "Rey"://
                    // si bajas el rey intercambias la carta qeu te queda en mano con la carta de
                    // mano del bot

                    if (!partida.hayDoncellaDescartes(descarte)) {
                        Carta cartaAux = new Carta();
                        cartaAux = jugador.getMano();
                        jugador.setMano(bot.getMano());
                        jugador.getMano().setEsMarcada100(true);
                        vista.btnMano1.setIcon(new ImageIcon(bot.getMano().getRutaImagen()));

                        bot.setMano(cartaAux);

                        vista.lblRetroBot.setText("Intercambías las cartas");
                    } else {
                        vista.lblRetroBot.setText("El bot esta protegido");
                    }
                    // System.out.println("JUGADOR JUEGA REY");
                    break;
                case "Condesa"://// 8
                    vista.lblRetroBot.setText("Juegas a la Condesa");
                    break;
                case "Princesa":// 9
                    // si bajas al princesa pierdes
                    jugador.setPerdedor(true);
                    partida.setEsFin(true);
                    vista.lblRetroBot.setText("Juegas a la Princesa. Has perdido");
                    break;
                // condesa no tiene comportamiento
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        // annadimos la carta al descarte
        descarte.getCartas().add(carta);
        vista.lblDescartes.setIcon(new ImageIcon(carta.getRutaImagen()));

    }

    public void resolverBot(Carta carta) {
        vista.btnRetroalimentacion.setEnabled(true);

        // comportamiento provocado cuando el jugador baja una carta de la mano a
        // descarte

        try {
            switch (carta.getTipoCarta()) {

                case "Guardia":// 1
                    // si bajas el guardian puedes adivinar la carta del contrincante, si se adivina
                    // ganas
                    // muestras panel de adivinar
                    // pulsan boton aceotar, recoges la info del combobox
                    // comparas la info del combo con la mano del contrario

                    // Comprobamos si hay doncella activa
                    if (!partida.hayDoncellaDescartes(descarte)) {
                        if (jugador.getMano().isEsMarcada100() && !jugador.getMano().getTipoCarta().equals("Guardia")) {
                            jugador.setPerdedor(true);
                            this.vista.lblRetroBot.setText("El com.example.jugeo_loveletter_alba_elena.MainActivity.Bot te ha adivinado la carta");

                        } else {
                            ArrayList<Carta> adivinar = new ArrayList<>();
                            adivinar = robo.getCartas();
                            adivinar.add(jugador.getMano());

                            ArrayList<Carta> adivinaGuardia = new ArrayList<>();

                            // recorremos el mazo de adivinar para quitar los guardias, los guardias no se
                            // pueden decir al adivinar
                            for (int i = 0; i < adivinar.size(); i++) {
                                if (!adivinar.get(i).getTipoCarta().equals("Guardia")) {
                                    adivinaGuardia.add(adivinar.get(i));
                                }
                            }

                            int random = (int) (0 + Math.random() * adivinaGuardia.size());
                            this.vista.lblRetroBot.setText("El com.example.jugeo_loveletter_alba_elena.MainActivity.Bot ha dicho " + adivinaGuardia.get(random).getTipoCarta());
                            if (adivinaGuardia.get(random).getTipoCarta().equals(jugador.getMano().getTipoCarta())) {
                                jugador.setPerdedor(true);
                                // System.out.println("Gana el bot");
                            }
                        }

                    }
                    // System.out.println("BOT JUEGA GUARDIA");
                    break;
                case "Sacerdote":// 2
                    // al bajar el sacerdote miras la carta del jugador contrario
                    // marcar la carta del contrario
                    if (!partida.hayDoncellaDescartes(descarte)) {
                        this.vista.lblRetroBot.setText("El com.example.jugeo_loveletter_alba_elena.MainActivity.Bot juega Sacerdote y ve tu carta");
                        jugador.getMano().setEsMarcada100(true);
                    }
                    // System.out.println("BOT JUEGA SACERDOTE");
                    break;
                case "Baron":// 3
                    // al bajar el baron los jugadores muestran las cartas y el que tenga el numero
                    // mas alto gana
                    // comparar el numero de la carta de la mano del bot con el numero de la carta
                    // de tu mano
                    if (!partida.hayDoncellaDescartes(descarte)) {
                        jugador.getMano().setEsMarcada100(true);
                        // comprobamos que carta se debe de mostrar en el label
                        if (bot.getMano().getTipoCarta().equals(carta.getTipoCarta())) {
                            bot.setMano(carta);
                        }

                        if (jugador.getMano().getNumCarta() > bot.getMano().getNumCarta()) {
                            bot.setPerdedor(true);
                            vista.lblRetroBot.setText("com.example.jugeo_loveletter_alba_elena.MainActivity.Bot tiene " + bot.getMano().getTipoCarta() + ". Pierde");

                            // System.out.println("Ha ganado el jugador");
                        } else if (bot.getMano().getNumCarta() > jugador.getMano().getNumCarta()) {
                            jugador.setPerdedor(true);
                            vista.lblRetroBot.setText("com.example.jugeo_loveletter_alba_elena.MainActivity.Bot tiene " + bot.getMano().getTipoCarta() + ". Gana");
                            // System.out.println("Ha ganado el bot");
                        } else {
                            vista.lblRetroBot.setText("La carta del com.example.jugeo_loveletter_alba_elena.MainActivity.Bot es igual que tu carta");
                            // Si es empate no pasa nada
                        }
                    }
                    // System.out.println("BOT JUEGA BARON");
                    break;
                case "Doncella":// 4
                    vista.lblRetroBot.setText("El com.example.jugeo_loveletter_alba_elena.MainActivity.Bot se protege con una doncella");
                    // System.out.println("BOT JUEGA DONCELLA");
                    break;
                case "Principe":// 5
                    // si bajas principe el jugador contrario a que juega pone su carta de mano en
                    // descartes y roba una carta del mazo
                    if (!partida.hayDoncellaDescartes(descarte)) {

                        vista.lblRetroBot.setText("El com.example.jugeo_loveletter_alba_elena.MainActivity.Bot juega al principe y descarta tu carta");
                        vista.lblDescartes.setIcon(new ImageIcon(jugador.getMano().getRutaImagen()));
                        if (jugador.getMano().getTipoCarta().equals("Princesa")) {
                            descarte.getCartas().add(jugador.getMano());
                            jugador.setPerdedor(true);
                            partida.setEsFin(true);
                        }
                        jugador.setMano(null);
                        if (!robo.finMazoRobo()) {
                            jugador.setMano(robo.robarCarta());
                            vista.btnMano1.setIcon(new ImageIcon(jugador.getMano().getRutaImagen()));
                        } else {// COMPROBAR QUIEN TIENE LA CARTA MAS ALTA
                            partida.setEsFin(true);
                        }
                    } else {

                        vista.lblRetroBot.setText("El com.example.jugeo_loveletter_alba_elena.MainActivity.Bot juega al principe y descarta su carta");
                        vista.lblDescartes.setIcon(new ImageIcon(bot.getMano().getRutaImagen()));
                        if (bot.getMano().getTipoCarta().equals("Princesa")) {
                            descarte.getCartas().add(bot.getMano());
                            bot.setPerdedor(true);
                            partida.setEsFin(true);
                        }
                        if (!robo.finMazoRobo()) {
                            bot.setMano(robo.robarCarta());
                        } else {// COMPROBAR QUIEN TIENE LA CARTA MAS ALTA
                            partida.setEsFin(true);
                        }

                    }
                    // System.out.println("BOT JUEGA PRINCIPE");
                    break;
                case "Rey":// 7
                    // si bajas el rey intercambias la carta qeu te queda en mano con la carta de
                    // mano del bot

                    if (!partida.hayDoncellaDescartes(descarte)) {
                        Carta cartaAux = new Carta();
                        cartaAux = jugador.getMano();
                        if (carta.getTipoCarta().equals("Rey")) {
                            jugador.setMano(bot.getRobo());
                        } else {
                            jugador.setMano(bot.getMano());
                        }

                        jugador.getMano().setEsMarcada100(true);
                        vista.btnMano1.setIcon(new ImageIcon(jugador.getMano().getRutaImagen()));

                        bot.setMano(cartaAux);

                        vista.lblRetroBot.setText("Intercambías las cartas");
                    }
                    // System.out.println("BOT JUEGA REY");
                    break;
                case "Condesa":// 8
                    vista.lblRetroBot.setText("El com.example.jugeo_loveletter_alba_elena.MainActivity.Bot juega a la Condesa");
                    // System.out.println("BOT JUEGA CONDESA");
                    break;
                // condesa no tiene comportamiento
            }

            // annadimos la carta al descarte
            descarte.getCartas().add(carta);
            vista.lblDescartes.setIcon(new ImageIcon(carta.getRutaImagen()));
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    public void resolverGanador() throws LineUnavailableException, IOException, UnsupportedAudioFileException {

        try {
            if (robo.finMazoRobo()) {
                // si se llega a final del mazo de robo se comparan las cartas en mano y quien
                // tenga la carta mas alta gana
                this.vista.panelPartida.setVisible(false);
                this.vista.panelGanador.setVisible(true);

                if (jugador.getMano().getNumCarta() < bot.getMano().getNumCarta()) {
                    jugador.setPerdedor(true);
                } else if (jugador.getMano().getNumCarta() < bot.getMano().getNumCarta()) {
                    bot.setPerdedor(true);
                } else {
                    this.vista.lblGanador.setText("Empate");
                }
            } else if (jugador.isPerdedor()) {
                this.vista.panelPartida.setVisible(false);
                this.vista.panelGanador.setVisible(true);
                this.vista.lblGanador.setText("El com.example.jugeo_loveletter_alba_elena.MainActivity.Bot ha ganado");
                // SONIDO
                Clip sonido = AudioSystem.getClip();
                sonido.open(AudioSystem.getAudioInputStream(new File("resources/sonidos/perder.wav")));
                sonido.start();
            } else if (bot.isPerdedor()) {
                this.vista.panelPartida.setVisible(false);
                this.vista.panelGanador.setVisible(true);
                this.vista.lblGanador.setText("Has ganado la partida");
                // SONIDO
                Clip sonido = AudioSystem.getClip();
                sonido.open(AudioSystem.getAudioInputStream(new File("resources/sonidos/ganar.wav")));
                sonido.start();
            } else { // empate sonido
                Clip sonido = AudioSystem.getClip();
                sonido.open(AudioSystem.getAudioInputStream(new File("resources/sonidos/ganar.wav")));
                sonido.start();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

}