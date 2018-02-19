package cr.ac.tec.la_caja_magica.GameLogic;

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

public class DifficultySelection extends AppCompatActivity {

  public final static String EXTRA_MESSAGE_GENERO = "com.example.myfirstapp.MESSAGE_A";
  public final static String EXTRA_MESSAGE_DIFICULTAD = "com.example.myfirstapp.MESSAGE_B";

  private Button easyBtn;
  private Button mediumBtn;
  private Button hardBtn;
  private TextView difficultyTitle;
  private String gender;
  private Typeface font;
  private Typeface fontLarge;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_seleccionar_dificultad);

    font = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.font_path));
    fontLarge = Typeface
        .createFromAsset(getAssets(), getResources().getString(R.string.fontLarge_path));

    Intent intent = getIntent();
    this.gender = intent.getStringExtra(CharacterSelection.EXTRA_MESSAGE);
    System.out.println("Genero: " + this.gender);

    this.easyBtn = (Button) this.findViewById(R.id.buttonFacil);
    this.easyBtn.setTypeface(font);

    this.mediumBtn = (Button) this.findViewById(R.id.buttonIntermedio);
    this.mediumBtn.setTypeface(font);

    this.hardBtn = (Button) this.findViewById(R.id.buttonDificil);
    this.hardBtn.setTypeface(font);

    this.difficultyTitle = (TextView) this.findViewById(R.id.textViewDificultad);
    this.difficultyTitle.setTypeface(fontLarge);

  }

  public void startEasyGame(View view) {
    startGame("Facil");
  }

  public void startMediumGame(View view) {
    startGame("Intermedio");
  }

  public void startHardGame(View view) {
    startGame("Dificil");
  }

  public void startGame(String dificultad) {
    Intent intent = new Intent(this, GameStart.class);
    intent.putExtra(EXTRA_MESSAGE_GENERO, gender);
    intent.putExtra(EXTRA_MESSAGE_DIFICULTAD, dificultad);
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
