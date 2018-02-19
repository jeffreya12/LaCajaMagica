package cr.ac.tec.la_caja_magica.GameLogic;

import android.content.Intent;
import android.graphics.Color;
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

public class GameOver extends AppCompatActivity {

  private static int finalScore;
  private static int highScore;

  private Typeface font;
  private Typeface fontLarge;

  private TextView title;
  private TextView showScore;
  private Button resetBtn;
  private Button backBtn;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_game_over);

    font = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.font_path));

    fontLarge = Typeface
        .createFromAsset(getAssets(), getResources().getString(R.string.fontLarge_path));

    finalScore = MainMenu.getLastScore();

    highScore = MainMenu.getScore();

    resetBtn = (Button) this.findViewById(R.id.buttonRetry);
    resetBtn.setTypeface(font);

    backBtn = (Button) this.findViewById(R.id.buttonMenu);
    backBtn.setTypeface(font);

    title = (TextView) this.findViewById(R.id.textViewTituloGameOver);
    title.setTypeface(fontLarge);
    title.setTextColor(Color.RED);

    showScore = (TextView) this.findViewById(R.id.textViewScores);
    showScore.setTypeface(fontLarge);
    showScore.setTextColor(Color.BLACK);

    String scoreDisplayText =
        "Marcador final: " + Integer.toString(finalScore) + "\n" + "Record: " + Integer
            .toString(highScore);
    showScore.setText(scoreDisplayText);

  }

  public void returnToMenu(View view) {
    Intent intent = new Intent(GameOver.this, MainMenu.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent);

    finish();
  }

  public void restart(View view) {
    Intent intent = new Intent(GameOver.this, CharacterSelection.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent);

    finish();
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

  @Override
  public void onBackPressed() {
    Intent intent = new Intent(GameOver.this, MainMenu.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent);

    finish();
  }


}
