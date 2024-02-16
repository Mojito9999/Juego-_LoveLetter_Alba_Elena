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
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
public class HasGanado extends AppCompatActivity {

    private ActivityResultLauncher<Intent> launcher;
    ImageView imagen;

    Jugador jugador = new Jugador();
    Bot bot = new Bot();
    Robo robo = new Robo();
    private final int REQUEST_PERMISSIONS = 1000;
    TextView lblGanador;
    MediaPlayer media;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_has_ganado);

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    HasGanado.this,
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

        resolverGanador();

    }
    public void playLocal(){
        if(media==null){
            media= MediaPlayer.create(this,R.raw.bob_esponja_tonos);
        }
        if(!media.isPlaying()){
            media.start();
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

    public void resolverGanador() {

        try {
            if (robo.finMazoRobo()) {
                // si se llega a final del mazo de robo se comparan las cartas en mano y quien
                // tenga la carta mas alta gana
                //this.vista.panelPartida.setVisible(false);
                //this.vista.panelGanador.setVisible(true);

                if (jugador.getMano().getNumCarta() < bot.getMano().getNumCarta()) {
                    jugador.setPerdedor(true);
                } else if (jugador.getMano().getNumCarta() < bot.getMano().getNumCarta()) {
                    bot.setPerdedor(true);
                } else {
                    lblGanador.setText("Empate");
                }
            } else if (jugador.isPerdedor()) {
                //this.vista.panelPartida.setVisible(false);
                //this.vista.panelGanador.setVisible(true);
                lblGanador.setText("El Bot ha ganado");
                // SONIDO
                playLocal();
                //Clip sonido = AudioSystem.getClip();
                //sonido.open(AudioSystem.getAudioInputStream(new File("resources/sonidos/perder.wav")));

                //sonido.start();
            } else if (bot.isPerdedor()) {
                //this.vista.panelPartida.setVisible(false);
                //this.vista.panelGanador.setVisible(true);
                lblGanador.setText("Has ganado la partida");
                // SONIDO
                playLocal();
                //Clip sonido = AudioSystem.getClip();
                //sonido.open(AudioSystem.getAudioInputStream(new File("resources/sonidos/ganar.wav")));
                //sonido.start();
            } else { // empate sonido
                playLocal();
                //Clip sonido = AudioSystem.getClip();
                //sonido.open(AudioSystem.getAudioInputStream(new File("resources/sonidos/ganar.wav")));
                //sonido.start();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }
}