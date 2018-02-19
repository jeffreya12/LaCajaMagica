package cr.ac.tec.la_caja_magica.GameLogic;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import charlie.la_caja_magica.R;
import cr.ac.tec.la_caja_magica.OtherFeatures.MainMenu;

public class CharacterSelection extends AppCompatActivity {

  public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
  private Typeface fontLarge;
  private TextView title;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    super.onCreate(savedInstanceState);
    //requestWindowFeature(Window.FEATURE_NO_TITLE);
    //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.seleccionar_personaje);

    fontLarge = Typeface
        .createFromAsset(getAssets(), getResources().getString(R.string.fontLarge_path));

    title = (TextView) this.findViewById(R.id.textViewTituloSeleccionarPersonajes);
    title.setTypeface(fontLarge);
  }

  public void selectMale(View view) {
    startDemo("masculino");
  }

  public void selectFemale(View view) {
    startDemo("femenino");
  }

  public void startDemo(String genero) {
    Intent intent = new Intent(this, DifficultySelection.class);
    intent.putExtra(EXTRA_MESSAGE, genero);
    startActivity(intent);
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (!MainMenu.isMusicPlaying()) {
      MainMenu.playMusic();
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    MainMenu.pauseMusic();
  }
}
