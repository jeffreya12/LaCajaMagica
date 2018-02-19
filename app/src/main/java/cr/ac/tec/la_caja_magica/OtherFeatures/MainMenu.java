package cr.ac.tec.la_caja_magica.OtherFeatures;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import charlie.la_caja_magica.R;
import cr.ac.tec.la_caja_magica.GameLogic.CharacterSelection;
import cr.ac.tec.la_caja_magica.GameTutorial.TutorialSelection;

public class MainMenu extends AppCompatActivity {

  //Musica
  private static MediaPlayer mediaPlayer;
  private static int highScore;
  private static int lastScore;
  private static boolean mute;
  private static SharedPreferences.Editor editor;
  private Typeface font;
  private Typeface fontLarge;
  private TextView title;
  private Button playBtn;
  private Button tutorialBtn;
  private Button virtuesBtn;
  private Button aboutBtn;
  private Button parkBtn;
  private CheckBox muteCheckBox;
  private SharedPreferences sharedPref;

  public static int getScore() {
    return highScore;
  }

  public static void setScore(int newScore) {
    editor.putInt("high_score", newScore).commit();
    highScore = newScore;
  }

  public static int getLastScore() {
    return lastScore;
  }

  public static void setLastScore(int newScore) {
    editor.putInt("last_score", newScore).commit();
    lastScore = newScore;
  }

  public static void pauseMusic() {
    mediaPlayer.pause();
  }

  public static void playMusic() {
    mediaPlayer.start();
  }

  public static boolean isMusicPlaying() {
    return mediaPlayer.isPlaying();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_menu_principal);

    font = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.font_path));

    fontLarge = Typeface
        .createFromAsset(getAssets(), getResources().getString(R.string.fontLarge_path));

    title = (TextView) this.findViewById(R.id.textViewTituloPrincipal);
    title.setTypeface(fontLarge);

    playBtn = (Button) this.findViewById(R.id.buttonJugar);
    playBtn.setTypeface(font);

    tutorialBtn = (Button) this.findViewById(R.id.buttonTutorial);
    tutorialBtn.setTypeface(font);

    virtuesBtn = (Button) this.findViewById(R.id.buttonValores);
    virtuesBtn.setTypeface(font);

    aboutBtn = (Button) this.findViewById(R.id.buttonAbout);
    aboutBtn.setTypeface(font);

    parkBtn = (Button) this.findViewById(R.id.buttonVerParque);
    parkBtn.setTypeface(font);

    //High Score
    sharedPref = getSharedPreferences("SCORES", Context.MODE_PRIVATE);
    editor = sharedPref.edit();
    highScore = sharedPref.getInt("high_score", 0);
    lastScore = sharedPref.getInt("last_score", 0);

    mute = sharedPref.getBoolean("mute", false);

    mediaPlayer = MediaPlayer.create(this, R.raw.loop);

    mediaPlayer.setLooping(true);
    mediaPlayer.start();

    muteCheckBox = (CheckBox) findViewById(R.id.checkbox_mute);
    muteCheckBox.setTypeface(font);
    muteCheckBox.setChecked(mute);
    if (muteCheckBox.isChecked()) {
      mediaPlayer.setVolume(0, 0);
    } else {
      mediaPlayer.setVolume(1, 1);
    }
  }

  public void muteMusic(View view) {
    if (!muteCheckBox.isChecked()) {
      // mediaplayer is already muted, so needs be to unmuted
      mediaPlayer.setVolume(1, 1);
      editor.putBoolean("mute", false).commit();
      mute = false;
    } else {
      // mute media player
      mediaPlayer.setVolume(0, 0);
      editor.putBoolean("mute", true).commit();
      mute = true;
    }
  }

  public void selectCharacter(View view) {
    Intent intent = new Intent(MainMenu.this, CharacterSelection.class);
    startActivity(intent);
  }

  public void seeVirtues(View view) {
    Intent intent = new Intent(MainMenu.this, Virtues.class);
    startActivity(intent);
  }

  public void seeParkInfo(View view) {
    Intent intent = new Intent(MainMenu.this, Park.class);
    startActivity(intent);
  }

  public void playTutorial(View view) {
    Intent intent = new Intent(this, TutorialSelection.class);
    startActivity(intent);
  }

  public void dialogAbout(View view) {

    Dialog mDialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
    mDialog.getWindow()
        .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    mDialog.setContentView(R.layout.dialog_about);

    TextView descripcion = (TextView) mDialog.findViewById(R.id.textViewAbout);

    descripcion.setTypeface(font);

    descripcion.setText(Html.fromHtml(getResources().getString(R.string.about)));

    mDialog.show();
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (!muteCheckBox.isChecked()) {
      playMusic();
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    pauseMusic();
  }

}

