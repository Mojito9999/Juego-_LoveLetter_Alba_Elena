package com.example.jugeo_loveletter_alba_elena;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    private ActivityResultLauncher<Intent> launcher;
    ImageView imagen;
    MediaPlayer media;
    Jugador jugador = new Jugador();
    Bot bot = new Bot();
    Robo robo = new Robo();
    Descarte descarte = new Descarte();
    Partida partida = new Partida();
    Carta carta = new Carta();
    TextView textViewNombreJugador;
    EditText ediTTextNombre;
    View view;
    ImageButton btnMano1,btnMano2;
    TextView lblRetroBot;
    Button btnRetroalimentacion;
    ImageView imageViewDescarte,lblCartaBot;
    private final int REQUEST_PERMISSIONS = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        btnMano1=findViewById(R.id.btnMano1);
        btnMano2=findViewById(R.id.btnMano2);

        lblRetroBot = findViewById(R.id.lblRetroBot);

        imageViewDescarte= findViewById(R.id.imageViewDescarte);
        btnRetroalimentacion = findViewById(R.id.btnRetroalimentacion);

       // lblCartaBot=findViewById(R.id.lblCartaBot);

        Button btnAbrirPopUpTabla = findViewById(R.id.btnAbrirPopUpTabla);


        btnAbrirPopUpTabla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abre la actividad PopUpTabla
                Intent miIntento = new Intent(getApplicationContext(), Ayuda.class);
                startActivity(miIntento);
            }
        });


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("selectedCarta")) {
            String selectedCarta = intent.getStringExtra("selectedCarta");
            if (selectedCarta != null) {
                if (selectedCarta.equals(bot.getMano().getTipoCarta())) {
                    lblRetroBot.setText("Has adivinado la carta del bot");
                } else {
                    lblRetroBot.setText("No has adivinado la carta del bot");
                }
            } else {

                lblRetroBot.setText("No se proporcionó una carta válida");
            }
        } else {

            lblRetroBot.setText("No se proporcionó una carta");
        }

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MainActivity2.this,
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

        // empezamos la partida
        partida.empezarPartida(jugador, bot, robo);


        btnMano1.setImageDrawable(ResourcesCompat.getDrawable(getResources(), getResources().getIdentifier(jugador.getMano().getRutaImagen(), "drawable", getPackageName()), null));

        // comprobamos quien empieza y fijamos el primer robo del jugador
        if (!partida.isEsEmpiezaBot()) {
            btnMano2.setImageDrawable(ResourcesCompat.getDrawable(getResources(), getResources().getIdentifier(jugador.getMano().getRutaImagen(), "drawable", getPackageName()), null));
        } else {
            btnMano1.setEnabled(false);
            btnMano2.setEnabled(false);
            // Juega el bot
            jugarBot();
        }

    }
    public void procesarTurno(View view) {
        // Cambiar el turno
        if (jugador.getRobo() == null && !partida.isEsFin()) {
            btnMano1.setEnabled(false);
            btnMano2.setEnabled(false);
            jugarBot();
        } else if (bot.getRobo() == null && !partida.isEsFin()) {
            btnMano1.setEnabled(true);
            btnMano2.setEnabled(true);

            btnMano2.setImageDrawable(ResourcesCompat.getDrawable(getResources(), getResources().getIdentifier(jugador.getMano().getRutaImagen(), "drawable", getPackageName()), null));
        }

        // Comprobar el final de la partida y manejar según sea necesario
        if (partida.isEsFin()) {
            resolverGanador();
            lblRetroBot.setText("La partida ha terminado, pulsa aceptar");
        } else if (bot.isPerdedor() || jugador.isPerdedor() || robo.finMazoRobo()) {
            partida.setEsFin(true);
            lblRetroBot.setText("La partida ha terminado, pulsa aceptar");
        }
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

    public void onClickBtnMano1(View view) {
        // Actualizamos las imagenes al jugar la carta
        imageViewDescarte.setImageResource(getResources().getIdentifier(jugador.getMano().getRutaImagen(), "drawable", getPackageName()));
        btnMano1.setImageResource(getResources().getIdentifier(jugador.getRobo().getRutaImagen(), "drawable", getPackageName()));
        btnMano2.setImageResource(getResources().getIdentifier("fondo_carta", "drawable", getPackageName()));
        btnMano1.setEnabled(false);
        btnMano2.setEnabled(false);

        // FUNCIONALIDAD DE LA CARTA
        resolverJugador(jugador.getMano());
        partida.reorganizarManoJugador(jugador);
        jugador.setRobo(null);
        btnRetroalimentacion.setEnabled(true);
        bot.setRobo(robo.robarCarta());
    }

    public void onClickBtnMano2(View view) {
        // Actualizamos las imagenes al jugar la carta
        imageViewDescarte.setImageResource(getResources().getIdentifier(jugador.getRobo().getRutaImagen(), "drawable", getPackageName()));
        btnMano2.setImageDrawable(null);
        btnMano1.setEnabled(false);
        btnMano2.setEnabled(false);

        // FUNCIONALIDAD DE LA CARTA
        resolverJugador(jugador.getRobo());
        jugador.setRobo(null);
        btnRetroalimentacion.setEnabled(true);
        bot.setRobo(robo.robarCarta());
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
        btnRetroalimentacion.setEnabled(true);

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
                           lblRetroBot.setText("El Bot te ha adivinado la carta");

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
                            lblRetroBot.setText("El Bot ha dicho " + adivinaGuardia.get(random).getTipoCarta());
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
                        lblRetroBot.setText("El Bot juega Sacerdote y ve tu carta");
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
                            lblRetroBot.setText("Bot tiene " + bot.getMano().getTipoCarta() + ". Pierde");

                            // System.out.println("Ha ganado el jugador");
                        } else if (bot.getMano().getNumCarta() > jugador.getMano().getNumCarta()) {
                            jugador.setPerdedor(true);
                            lblRetroBot.setText("Bot tiene " + bot.getMano().getTipoCarta() + ". Gana");
                            // System.out.println("Ha ganado el bot");
                        } else {
                            lblRetroBot.setText("La carta del Bot es igual que tu carta");
                            // Si es empate no pasa nada
                        }
                    }
                    // System.out.println("BOT JUEGA BARON");
                    break;
                case "Doncella":// 4
                    lblRetroBot.setText("El Bot se protege con una doncella");
                    // System.out.println("BOT JUEGA DONCELLA");
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
                        btnMano1.setImageDrawable(ResourcesCompat.getDrawable(getResources(), getResources().getIdentifier(jugador.getMano().getRutaImagen(), "drawable", getPackageName()), null));

                        bot.setMano(cartaAux);

                        lblRetroBot.setText("Intercambías las cartas");
                    }
                    // System.out.println("BOT JUEGA REY");
                    break;
                case "Condesa":// 8
                    lblRetroBot.setText("El Bot juega a la Condesa");
                    // System.out.println("BOT JUEGA CONDESA");
                    break;
                // condesa no tiene comportamiento
            }

            // annadimos la carta al descarte
            descarte.getCartas().add(carta);
            imageViewDescarte.setImageDrawable(ResourcesCompat.getDrawable(getResources(), getResources().getIdentifier(jugador.getMano().getRutaImagen(), "drawable", getPackageName()), null));

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

            switch (carta.getTipoCarta()) {

                case "Guardia":// 1
                    // si bajas el guardian puedes adivinar la carta del contrincante, si se adivina
                    // ganas
                    // muestras panel de adivinar
                    // pulsan boton aceotar, recoges la info del combobox
                    // comparas la info del combo con la mano del contrario

                    // Comprobamos si hay doncella activa
                    if (!partida.hayDoncellaDescartes(descarte)) {
                        Intent intent = new Intent(MainActivity2.this, SeleccionCarta.class);
                        startActivity(intent);
                        finish();
                        btnRetroalimentacion.setEnabled(false);
                    } else {
                        lblRetroBot.setText("El bot esta protegido");
                    }

                    // System.out.println("JUGADOR JUEGA GUARDIA");

                    break;
                case "Sacerdote":// 2
                    // al bajar el sacerdote miras la carta del jugador contrario
                    // marcar la carta del contrario
                    if (!partida.hayDoncellaDescartes(descarte)) {
                        //lblCartaBot.setImageDrawable(ResourcesCompat.getDrawable(getResources(), getResources().getIdentifier(bot.getMano().getRutaImagen(), "drawable", getPackageName()), null));
                        lblRetroBot.setText("El bot tiene " + bot.getMano().getTipoCarta());
                    } else {
                        lblRetroBot.setText("El bot esta protegido");
                    }

                    // System.out.println("JUGADOR JUEGA SACERDOTE");
                    break;
                case "Baron":// 3
                    // al bajar el baron los jugadores muestran las cartas y el que tenga el numero
                    // mas alto gana
                    // comparar el numero de la carta de la mano del bot con el numero de la carta
                    // de tu mano
                    if (!partida.hayDoncellaDescartes(descarte)) {
                        //lblCartaBot.setImageDrawable(ResourcesCompat.getDrawable(getResources(), getResources().getIdentifier(bot.getMano().getRutaImagen(), "drawable", getPackageName()), null));
                        partida.reorganizarManoJugador(jugador);
                        if (jugador.getMano().getNumCarta() > bot.getMano().getNumCarta()) {
                            bot.setPerdedor(true);
                            lblRetroBot.setText("Tu carta es más alta");
                            // System.out.println("Ha ganado el jugador");
                        } else if (bot.getMano().getNumCarta() > jugador.getMano().getNumCarta()) {
                            jugador.setPerdedor(true);
                            lblRetroBot.setText("La carta del bot es más alta");
                            // System.out.println("Ha ganado el bot");
                        } else {
                            lblRetroBot.setText("Empate. Continua la partida");// Si es empate no pasa nada
                        }
                    } else {
                        lblRetroBot.setText("El bot esta protegido");
                    }
                    // System.out.println("JUGADOR JUEGA BARON");
                    break;
                case "Doncella":// 4
                    lblRetroBot.setText("Te proteges con una doncella");
                    break;
                case "Rey"://
                    // si bajas el rey intercambias la carta qeu te queda en mano con la carta de
                    // mano del bot

                    if (!partida.hayDoncellaDescartes(descarte)) {
                        Carta cartaAux = new Carta();
                        cartaAux = jugador.getMano();
                        jugador.setMano(bot.getMano());
                        jugador.getMano().setEsMarcada100(true);
                        btnMano1.setImageResource(getResources().getIdentifier(jugador.getRobo().getRutaImagen(), "drawable", getPackageName()));

                        bot.setMano(cartaAux);

                        lblRetroBot.setText("Intercambías las cartas");
                    } else {
                        lblRetroBot.setText("El bot esta protegido");
                    }
                    // System.out.println("JUGADOR JUEGA REY");
                    break;
                case "Condesa"://// 8
                    lblRetroBot.setText("Juegas a la Condesa");
                    break;
                case "Princesa":// 9
                    // si bajas al princesa pierdes
                    jugador.setPerdedor(true);
                    partida.setEsFin(true);
                    lblRetroBot.setText("Juegas a la Princesa. Has perdido");
                    break;
                // condesa no tiene comportamiento
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        // annadimos la carta al descarte
        descarte.getCartas().add(carta);
        imageViewDescarte.setImageDrawable(ResourcesCompat.getDrawable(getResources(), getResources().getIdentifier(bot.getMano().getRutaImagen(), "drawable", getPackageName()), null));

    }

    public void playLocal(int soundResourceId) {
        if (media == null) {
            media = MediaPlayer.create(this, soundResourceId);
        }
        if (!media.isPlaying()) {
            media.start();
        }
    }

    public void resolverGanador() {

        try {
            if (robo.finMazoRobo()) {
                // si se llega a final del mazo de robo se comparan las cartas en mano y quien
                // tenga la carta mas alta gana


                if (jugador.getMano().getNumCarta() < bot.getMano().getNumCarta()) {
                    jugador.setPerdedor(true);
                } else if (jugador.getMano().getNumCarta() < bot.getMano().getNumCarta()) {
                    bot.setPerdedor(true);
                } else {

                    Toast.makeText(getApplicationContext(), "Empate", Toast.LENGTH_SHORT).show();

                }
            } else if (jugador.isPerdedor()) {

                Toast.makeText(getApplicationContext(), "El Bot ha ganado", Toast.LENGTH_SHORT).show();

                // SONIDO
                playLocal(R.raw.perder);

            } else if (bot.isPerdedor()) {

                Toast.makeText(getApplicationContext(), "Has ganado la partida", Toast.LENGTH_SHORT).show();

                // SONIDO
                playLocal(R.raw.ganar);

            } else { // empate sonido
                playLocal(R.raw.ganar);

            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }
}