package cr.ac.tec.la_caja_magica.GameTutorial;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import charlie.la_caja_magica.R;
import cr.ac.tec.la_caja_magica.GameObjects.Enemigo;
import cr.ac.tec.la_caja_magica.GameObjects.Plataforma;
import cr.ac.tec.la_caja_magica.GameObjects.Player;
import cr.ac.tec.la_caja_magica.GameObjects.Virtue;
import cr.ac.tec.la_caja_magica.OtherFeatures.MainMenu;
import java.util.ArrayList;

public class TutorialView extends SurfaceView implements Runnable {


  //Fuentes
  private Typeface font;
  private Typeface fontLarge;

  private Player player;
  private long startingTime;
  private Thread thread;
  private SurfaceHolder surface;
  private volatile boolean playing;
  private Canvas canvas;
  private Paint paint;
  private int width;
  private int height;
  private ArrayList<Plataforma> currentMap;
  private ArrayList<Enemigo> enemies;
  private ArrayList<Virtue> virtues;
  private Bitmap tutorialPlatform;
  private Bitmap background;
  private String phase;

  private long handAnimationTime;
  private int currentHand;
  private Bitmap handA;
  private Bitmap handB;
  private Bitmap handC;
  private Bitmap phone;

  public TutorialView(Context context, String phase) {
    super(context);

    font = Typeface
        .createFromAsset(getContext().getAssets(), getResources().getString(R.string.font_path));
    fontLarge = Typeface.createFromAsset(getContext().getAssets(),
        getResources().getString(R.string.fontLarge_path));

    enemies = new ArrayList<>();
    virtues = new ArrayList<>();
    startGlobalValues();
    startPlayer();
    tutorialPlatform = adjustScreenImage(width / 16, height / 32,
        BitmapFactory.decodeResource(this.getResources(), R.drawable.plataforma));
    this.phase = phase;
    currentHand = 0;
    handAnimationTime = System.currentTimeMillis();
    phone = adjustScreenImage(width / 4, height / 4,
        BitmapFactory.decodeResource(this.getResources(), R.drawable.phone));
    handA = adjustScreenImage(width / 10, width / 10,
        BitmapFactory.decodeResource(this.getResources(), R.drawable.hand_a));
    handB = adjustScreenImage(width / 8, width / 8,
        BitmapFactory.decodeResource(this.getResources(), R.drawable.hand_b));
    handC = adjustScreenImage(width / 6, width / 6,
        BitmapFactory.decodeResource(this.getResources(), R.drawable.hand_c));
    startMap(phase);
  }

  @Override
  public void run() {
    while (playing) {
      refresh();
      draw();
    }
  }

  public boolean isPlaying() {
    return playing;
  }

  public void refresh() {
    if (System.currentTimeMillis() >= startingTime + 700) {
      for (int i = 0; i < currentMap.size(); i++) {
        currentMap.get(i).movimiento(width);
        if (currentMap.get(i).getPos_x() <= -(width / 16)) {
          currentMap.get(i).setPos_x(width);
        }
      }
      for (int i = 0; i < enemies.size(); i++) {
        enemies.get(i).movimiento(width);
        if (enemies.get(i).getPos_x() <= -(width / 16)) {
          enemies.get(i).setPos_x(width);
        }
      }
      for (int i = 0; i < virtues.size(); i++) {
        virtues.get(i).movimiento(width, height);
        if (virtues.get(i).getPos_x() <= -(100)) {
          virtues.get(i).setPos_x(width);
        }
      }
      player.movimiento(currentMap, height);
      player.colicionEnemigos(enemies);
      boolean colicion = player.colicionValores(virtues);
      if (player.getVivo() == false) {
        restart();
      }
      if (phase.equals("4") && virtues.size() == 0) {
        restart();
      }
    }
  }

  public void draw() {
    if (surface.getSurface().isValid()) {
      canvas = surface.lockCanvas();
      canvas.drawBitmap(background, 0, 0, paint);
      for (int i = 0; i < currentMap.size(); i++) {
        canvas.drawBitmap(currentMap.get(i).getSprite(), currentMap.get(i).getPos_x(),
            currentMap.get(i).getPos_y(), paint);
      }
      for (int i = 0; i < enemies.size(); i++) {
        canvas.drawBitmap(enemies.get(i).getSprite(), enemies.get(i).getPos_x(),
            enemies.get(i).getPos_y(), paint);
      }
      for (int i = 0; i < virtues.size(); i++) {
        canvas.drawBitmap(virtues.get(i).getSprite(), virtues.get(i).getPos_x(),
            virtues.get(i).getPos_y(), paint);
      }
      canvas.drawBitmap(player.getSprite(System.currentTimeMillis()), player.getPos_x(), player
          .getPos_y(), paint);
      if (System.currentTimeMillis() >= handAnimationTime + 200) {
        currentHand += 1;
        if (currentHand == 4) {
          currentHand = 0;
        }
        handAnimationTime = System.currentTimeMillis();
      }
      canvas.drawBitmap(phone, width - width / 4, height - height / 4, paint);
      if (currentHand == 0) {
        canvas.drawBitmap(handA, width - width / 5, height - height / 5, paint);
      }
      if (currentHand == 1) {
        canvas.drawBitmap(handB, width - width / 5, height - height / 5, paint);
      }
      if (currentHand == 2) {
        canvas.drawBitmap(handC, width - width / 5, height - height / 5, paint);
      }
      if (currentHand == 3) {
        canvas.drawBitmap(handB, width - width / 5, height - height / 5, paint);
      }
      switch (phase) {
        case "1":
          canvas.drawText(getResources().getString(R.string.tutorial_saltar), 30, 60, paint);
          break;
        case "2":
          canvas.drawText(getResources().getString(R.string.tutorial_plataformas), 30, 60, paint);
          break;
        case "3":
          canvas.drawText(getResources().getString(R.string.tutorial_caer), 30, 60, paint);
          break;
        case "4":
          canvas.drawText(getResources().getString(R.string.tutorial_enemigos), 30, 60, paint);
          break;
        case "5":
          canvas.drawText(getResources().getString(R.string.tutorial_valores), 30, 60, paint);
          break;
      }

      surface.unlockCanvasAndPost(canvas);
    }
  }

  public void pause() {
    playing = false;
    try {
      MainMenu.pauseMusic();
      thread.join();
    } catch (InterruptedException e) {
      Log.e("Error:", "joining thread");
    }
  }

  public void resume() {
    MainMenu.playMusic();
    playing = true;
    thread = new Thread(this);
    thread.start();
  }

  public void restart() {
    player.reiniciarJugador(width, height);
    startMap(phase);
  }


  @Override
  public boolean onTouchEvent(MotionEvent motionEvent) {
    switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
      case MotionEvent.ACTION_DOWN:
        player.saltar(height);
        break;
      case MotionEvent.ACTION_UP:
        break;
    }
    return true;
  }

  public void startGlobalValues() {
    surface = getHolder();
    paint = new Paint();
    width = Resources.getSystem().getDisplayMetrics().widthPixels;
    height = Resources.getSystem().getDisplayMetrics().heightPixels;
    startingTime = System.currentTimeMillis();

    paint.setColor(Color.BLACK);
    paint.setTextSize(40f);
    paint.setTypeface(fontLarge);
  }

  public void startPlayer() {
    Bitmap sprite_a = adjustScreenImage(width / 16, height / 8,
        BitmapFactory.decodeResource(this.getResources(), R.drawable.boy_a));
    Bitmap sprite_b = adjustScreenImage(width / 16, height / 8,
        BitmapFactory.decodeResource(this.getResources(), R.drawable.boy_b));
    Bitmap sprite_c = adjustScreenImage(width / 16, height / 8,
        BitmapFactory.decodeResource(this.getResources(), R.drawable.boy_c));
    Bitmap sprite_j = adjustScreenImage(width / 18, height / 8,
        BitmapFactory.decodeResource(this.getResources(), R.drawable.boy_j));
    ArrayList<Bitmap> sprites = new ArrayList<>();
    sprites.add(sprite_a);
    sprites.add(sprite_b);
    sprites.add(sprite_c);
    sprites.add(sprite_b);
    sprites.add(sprite_j);
    player = new Player(width / 10, height - (height / 3), width / 16, height / 8, sprites);
  }

  public void startMap(String fase) {
    ArrayList<Plataforma> mapa = new ArrayList<>();
    background = adjustScreenImage(width, height,
        BitmapFactory.decodeResource(this.getResources(), R.drawable.fondo_tutorial));
    switch (fase) {
      case "1":
        for (int i = 0; i <= width; i += width / 16) {
          Plataforma plataforma = new Plataforma(i, height - (height / 32), width / 16, height / 32,
              true,
              tutorialPlatform);
          mapa.add(plataforma);
        }
        currentMap = mapa;
//                background = adjustImageScreen(width, height, BitmapFactory.decodeResource(this.getResources(), R.drawable.fondo_tutorial_fase1));
        break;
      case "2":
        for (int i = 0; i <= width; i += width / 16) {
          Plataforma plataforma = new Plataforma(i, height - (height / 32), width / 16, height / 32,
              true,
              tutorialPlatform);
          mapa.add(plataforma);
        }
        Plataforma plataforma = new Plataforma(width, height - (height / 6), width / 16,
            height / 32,
            true, tutorialPlatform);
        Plataforma plataforma2 = new Plataforma(width + width / 16, height - (height / 6),
            width / 16,
            height / 32, true, tutorialPlatform);
        mapa.add(plataforma);
        mapa.add(plataforma2);
        currentMap = mapa;
//                background = adjustImageScreen(width, height, BitmapFactory.decodeResource(this.getResources(), R.drawable.fondo_tutorial_fase2));
        break;
      case "3":
        for (int i = 0; i <= width - (width / 16) * 2; i += width / 16) {
          Plataforma plataforma3 = new Plataforma(i, height - (height / 32), width / 16,
              height / 32,
              true, tutorialPlatform);
          mapa.add(plataforma3);
        }
        currentMap = mapa;
//                background = adjustImageScreen(width, height, BitmapFactory.decodeResource(this.getResources(), R.drawable.fondo_tutorial_fase3));
        break;
      case "4":
        virtues = new ArrayList<>();
        for (int i = 0; i <= width; i += width / 16) {
          Plataforma plataforma4 = new Plataforma(i, height - (height / 32), width / 16,
              height / 32,
              true, tutorialPlatform);
          mapa.add(plataforma4);
        }
        currentMap = mapa;
        Bitmap imgValor = adjustScreenImage(width / 20, width / 20,
            BitmapFactory.decodeResource(this.getResources(), R.drawable.amor));
        ArrayList<Bitmap> sprites = new ArrayList<>();
        sprites.add(imgValor);
        Virtue amor = new Virtue(width, height - (height / 3), width / 20, width / 20, height / 5,
            height - (height / 4), sprites, 1);
        virtues.add(amor);
//                background = adjustImageScreen(width, height, BitmapFactory.decodeResource(this.getResources(), R.drawable.fondo_menu_domo_4));
        break;
      case "5":
        enemies = new ArrayList<>();
        for (int i = 0; i <= width; i += width / 16) {
          Plataforma plataforma5 = new Plataforma(i, height - (height / 32), width / 16,
              height / 32,
              true, tutorialPlatform);
          mapa.add(plataforma5);
        }
        currentMap = mapa;
//                background = adjustImageScreen(width, height, BitmapFactory.decodeResource(this.getResources(), R.drawable.fondo_tutorial_fase5));
        Bitmap enemigoA = adjustScreenImage(width / 16, height / 5,
            BitmapFactory.decodeResource(this.getResources(), R.drawable.enemigo_a));
        Bitmap enemigoB = adjustScreenImage(width / 16, height / 5,
            BitmapFactory.decodeResource(this.getResources(), R.drawable.enemigo_b));
        Enemigo enemigo = new Enemigo(width, height - (height / 32) - (height / 5), width / 16,
            height
                / 5,
            enemigoA, enemigoB);
        enemies.add(enemigo);
        break;
    }
  }

  public Bitmap adjustScreenImage(int x, int y, Bitmap imagen) {
    imagen = Bitmap.createScaledBitmap(imagen, x, y, true);
    return imagen;
  }
}




