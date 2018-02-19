package cr.ac.tec.la_caja_magica.GameLogic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import charlie.la_caja_magica.R;

public class GameStart extends AppCompatActivity {

  private GameView vista;
  private FrameLayout game;// Sort of "holder" for everything we are placing
  private RelativeLayout GameButtons;//Holder for the buttons

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();
    String genero = intent.getStringExtra(DifficultySelection.EXTRA_MESSAGE_GENERO);
    String dificultad = intent.getStringExtra(DifficultySelection.EXTRA_MESSAGE_DIFICULTAD);
    System.out.println(dificultad);
    System.out.println(genero);
    vista = new GameView(this, genero, dificultad);
//        setContentView(vista);

    game = new FrameLayout(this);
    GameButtons = new RelativeLayout(this);
    final ImageButton butOne = new ImageButton(this);
    butOne.setBackground(getResources().getDrawable(R.drawable.pause));
    butOne.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        if (vista.isPlaying()) {
          butOne.setBackground(getResources().getDrawable(R.drawable.play));
          vista.pause();
        } else {
          butOne.setBackground(getResources().getDrawable(R.drawable.pause));
          vista.resume();
        }

      }
    });
    RelativeLayout.LayoutParams b1 = new RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.WRAP_CONTENT,
        RelativeLayout.LayoutParams.WRAP_CONTENT);
    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.FILL_PARENT,
        RelativeLayout.LayoutParams.FILL_PARENT);
    GameButtons.setLayoutParams(params);

    GameButtons.addView(butOne);

    b1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
    b1.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);

    butOne.setLayoutParams(b1);

    game.addView(vista);
    game.addView(GameButtons);
    setContentView(game);
  }

  @Override
  protected void onResume() {
    super.onResume();
    vista.resume();
  }

  @Override
  protected void onPause() {
    super.onPause();
    vista.pause();
  }
}
