package com.example.jugeo_loveletter_alba_elena;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> launcher;
    ImageView imagen;
    Juego juego = new Juego();
    Jugador jugador = new Jugador();
    Bot bot = new Bot();
    Robo robo = new Robo();
    Descarte descarte = new Descarte();
    Partida partida = new Partida();
    Carta carta = new Carta();
    TextView textViewNombreJugador;
    EditText ediTTextNombre;
    View view;
    Button btnMano1;
    private final int REQUEST_PERMISSIONS = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imagen = findViewById(R.id.imageView);


        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_PERMISSIONS);
        }
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            Bundle extras = data.getExtras();
                            Bitmap imgBitmap = (Bitmap) extras.get("data");
                            imagen.setImageBitmap(imgBitmap);
                        }
                    }
                }
        );
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_PERMISSIONS){
            for(int i=0; i< grantResults.length; i++){
                if(grantResults[i]== PackageManager.PERMISSION_DENIED){
                    Toast.makeText(this, "La app no tiene permisos", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void tomarFoto(View view) {
        Intent tomarFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        launcher.launch(tomarFoto);
    }
    public void siguiente(View view) {
        Intent intent = new Intent(this,MainActivity2.class);
        startActivity(intent);
    }

    public void aceptarInicio(){
        // nombre jugador
        jugador.setNombre(String.valueOf(this.textViewNombreJugador.getText()));

        this.ediTTextNombre.setText(jugador.getNombre());

        siguiente(view);

        // empezamos la partida
        partida.empezarPartida(jugador, bot, robo);

        btnMano1((jugador.getMano().getRutaImagen()));/////////////////////////////////////////////////////////////////////////////////////////////////////
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
    public void jugarBot() {

        try {

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
                            this.vista.lblRetroBot.setText("El Bot te ha adivinado la carta");

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
                            this.vista.lblRetroBot.setText("El Bot ha dicho " + adivinaGuardia.get(random).getTipoCarta());
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
                        this.vista.lblRetroBot.setText("El Bot juega Sacerdote y ve tu carta");
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
                            vista.lblRetroBot.setText("Bot tiene " + bot.getMano().getTipoCarta() + ". Pierde");

                            // System.out.println("Ha ganado el jugador");
                        } else if (bot.getMano().getNumCarta() > jugador.getMano().getNumCarta()) {
                            jugador.setPerdedor(true);
                            vista.lblRetroBot.setText("Bot tiene " + bot.getMano().getTipoCarta() + ". Gana");
                            // System.out.println("Ha ganado el bot");
                        } else {
                            vista.lblRetroBot.setText("La carta del Bot es igual que tu carta");
                            // Si es empate no pasa nada
                        }
                    }
                    // System.out.println("BOT JUEGA BARON");
                    break;
                case "Doncella":// 4
                    vista.lblRetroBot.setText("El Bot se protege con una doncella");
                    // System.out.println("BOT JUEGA DONCELLA");
                    break;
                case "Principe":// 5
                    // si bajas principe el jugador contrario a que juega pone su carta de mano en
                    // descartes y roba una carta del mazo
                    if (!partida.hayDoncellaDescartes(descarte)) {

                        vista.lblRetroBot.setText("El Bot juega al principe y descarta tu carta");
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

                        vista.lblRetroBot.setText("El Bot juega al principe y descarta su carta");
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
                    vista.lblRetroBot.setText("El Bot juega a la Condesa");
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
}