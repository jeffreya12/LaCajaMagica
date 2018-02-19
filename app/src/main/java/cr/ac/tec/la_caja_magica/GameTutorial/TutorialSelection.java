package cr.ac.tec.la_caja_magica.GameTutorial;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import charlie.la_caja_magica.R;
import cr.ac.tec.la_caja_magica.OtherFeatures.MainMenu;

public class TutorialSelection extends AppCompatActivity {

  public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
  private Typeface font;
  private Typeface fontLarge;
  private String fase;
  private Button jumpBtn;
  private Button platformBtn;
  private Button dropBtn;
  private Button enemiesBtn;
  private Button virtuesBtn;
  private TextView title;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_seleccionar_tutorial);

    font = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.font_path));

    fontLarge = Typeface
        .createFromAsset(getAssets(), getResources().getString(R.string.fontLarge_path));

    jumpBtn = (Button) this.findViewById(R.id.button_tuto_saltar);
    jumpBtn.setTypeface(font);

    platformBtn = (Button) this.findViewById(R.id.button_tuto_plataformas);
    platformBtn.setTypeface(font);

    virtuesBtn = (Button) this.findViewById(R.id.button_tuto_valores);
    virtuesBtn.setTypeface(font);

    dropBtn = (Button) this.findViewById(R.id.button_tuto_caer);
    dropBtn.setTypeface(font);

    enemiesBtn = (Button) this.findViewById(R.id.button_tuto_enemigos);
    enemiesBtn.setTypeface(font);

    title = (TextView) this.findViewById(R.id.textViewTituloTutorial);
    title.setTypeface(fontLarge);
  }

  public void selectJumpTutorial(View view) {
    fase = "1";
    Intent intent = new Intent(this, TutorialStart.class);
    intent.putExtra(EXTRA_MESSAGE, fase);
    startActivity(intent);
  }

  public void selectPlatformTutorial(View view) {
    fase = "2";
    Intent intent = new Intent(this, TutorialStart.class);
    intent.putExtra(EXTRA_MESSAGE, fase);
    startActivity(intent);
  }

  public void selectFallingTutorial(View view) {
    fase = "3";
    Intent intent = new Intent(this, TutorialStart.class);
    intent.putExtra(EXTRA_MESSAGE, fase);
    startActivity(intent);
  }

  public void selectVirtuesTutorial(View view) {
    fase = "4";
    Intent intent = new Intent(this, TutorialStart.class);
    intent.putExtra(EXTRA_MESSAGE, fase);
    startActivity(intent);
  }

  public void selectEnemiesTutorial(View view) {
    fase = "5";
    Intent intent = new Intent(this, TutorialStart.class);
    intent.putExtra(EXTRA_MESSAGE, fase);
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
