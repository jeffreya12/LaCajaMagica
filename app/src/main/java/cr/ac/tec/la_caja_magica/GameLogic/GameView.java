package cr.ac.tec.la_caja_magica.GameLogic;

import android.content.Context;
import android.content.Intent;
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
import cr.ac.tec.la_caja_magica.GameObjects.GameMap;
import cr.ac.tec.la_caja_magica.GameObjects.Plataforma;
import cr.ac.tec.la_caja_magica.GameObjects.Player;
import cr.ac.tec.la_caja_magica.GameObjects.Virtue;
import cr.ac.tec.la_caja_magica.OtherFeatures.MainMenu;
import java.util.ArrayList;
import java.util.Random;


public class GameView extends SurfaceView implements Runnable {

  //Context
  private static Context context;
  // GameObjects del GameMap
  // Fondos:
  private Bitmap exampleBackground;
  // Plataformas:
  private Bitmap forestPlatform;
  private long valuesTime;
  private long enemieTime;
  //Fuentes
  private Typeface font;
  private Typeface fontLarge;
  // GameObjects del Juego -----
  private Player player;
  private MapGenerator generator;
  private GameMap gameMap;
  // Virtues Funcionales -----
  private long startingTime;
  private Thread thread;
  private SurfaceHolder surface;
  private volatile boolean playing;
  private Canvas canvas;
  private Paint paint;
  //Scores
  private int score;
  private int renderedScore;
  private String renderedScoreString;
  private long scoreTime;
  // Dimensiones de Pantalla -----
  private int width;
  private int height;
  private Virtue virtue;
  private Bitmap virtueImage;
  private ArrayList<Plataforma> enemyPlatform;
  // Dificultad:
  private String difficulty;


  public GameView(Context context, String genero, String difficulty) {
    super(context);

    GameView.context = context;

    this.difficulty = difficulty;

    font = Typeface
        .createFromAsset(getContext().getAssets(), getResources().getString(R.string.font_path));
    fontLarge = Typeface.createFromAsset(getContext().getAssets(),
        getResources().getString(R.string.fontLarge_path));

    startGlobalValues();
    startPlayer(genero);
    loadSprites();
    startMap();

    enemyPlatform = new ArrayList<>();

    score = 0;
    scoreTime = System.currentTimeMillis();
    valuesTime = System.currentTimeMillis();
    enemieTime = System.currentTimeMillis();
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
    if (System.currentTimeMillis() >= startingTime + 600) {
      for (int i = 0; i < gameMap.getMapa().size(); i++) { // Movimiento de Plataformas
        gameMap.getMapa().get(i).movimiento(width);
        if (gameMap.getMapa().get(i).getPos_x() <= -(gameMap.getMapa().get(i).getDim_x()) * 2) {
          gameMap.eliminarPlataforma(gameMap.getMapa().get(i));
          mapGenerator();
//                    generateEasyPlatform();
//                    generateHardPlatform();
        }
      }
      for (int i = 0; i < enemyPlatform.size(); i++) {
        enemyPlatform.get(i).movimiento(width);
        if (enemyPlatform.get(i).getPos_x() <= -(enemyPlatform.get(i).getDim_x()) * 2) {
          enemyPlatform.remove(enemyPlatform.get(i));
        }
      }
      for (int i = 0; i < gameMap.getEnemigos().size(); i++) {
        gameMap.getEnemigos().get(i).movimiento(width);
        if (gameMap.getEnemigos().get(i).getPos_x()
            <= -(gameMap.getEnemigos().get(i).getDim_x()) * 2) {
          gameMap.eliminarEnemigo(gameMap.getEnemigos().get(i));
        }
      }
      for (int i = 0; i < gameMap.getValores().size(); i++) {
        gameMap.getValores().get(i).movimiento(width, height);
        if (gameMap.getValores().get(i).getPos_x()
            <= -(gameMap.getValores().get(i).getDim_x()) * 2) {
          gameMap.eliminarValor(gameMap.getValores().get(i));
        }
      }
      gameMap.movimientoFondo(width);
      player.movimiento(gameMap.getMapa(), height);
      player.colicionEnemigos(gameMap.getEnemigos());
      boolean colicion = player.colicionValores(gameMap.getValores());
      if (colicion == true) {
        score += 100;
      }
      if (System.currentTimeMillis() >= scoreTime + 1000) {
        score++;
        scoreTime = System.currentTimeMillis();
      }
      if (player.getVivo() == false) {
        restart();
      }
    }
  }

  public void draw() {
    if (surface.getSurface().isValid()) {
      canvas = surface.lockCanvas();
      canvas.drawBitmap(gameMap.getFondo(), gameMap.getFondo_pos_x(), 0, paint);
      canvas.drawBitmap(gameMap.getFondo(), gameMap.getFondo_pos_x() + width, 0, paint);
      for (int i = 0; i < gameMap.getMapa().size(); i++) {
        canvas.drawBitmap(gameMap.getMapa().get(i).getSprite(), gameMap.getMapa().get(i).getPos_x(),
            gameMap.getMapa().get(i).getPos_y(), paint);
      }
      for (int i = 0; i < gameMap.getEnemigos().size(); i++) {
        canvas
            .drawBitmap(gameMap.getEnemigos().get(i).getSprite(),
                gameMap.getEnemigos().get(i).getPos_x(),
                gameMap.getEnemigos().get(i).getPos_y(), paint);
      }
      for (int i = 0; i < gameMap.getValores().size(); i++) {
        canvas.drawBitmap(gameMap.getValores().get(i).getSprite(),
            gameMap.getValores().get(i).getPos_x(),
            gameMap.getValores().get(i).getPos_y(), paint);
      }
      if (enemyPlatform.size() > 0) {
        canvas
            .drawBitmap(enemyPlatform.get(0).getSprite(), enemyPlatform.get(0).getPos_x(),
                enemyPlatform.get(0).getPos_y(), paint);
      }
      canvas.drawBitmap(player.getSprite(System.currentTimeMillis()), player.getPos_x(),
          player.getPos_y(), paint);
      //Scores en pantalla
      if (this.score != this.renderedScore || this.renderedScoreString == null) {
        this.renderedScore = this.score;
        this.renderedScoreString = Integer.toString(this.renderedScore);
      }
      canvas.drawText(this.renderedScoreString, 30, 40, paint);

      surface.unlockCanvasAndPost(canvas);
    }
  }

  public void pause() {
    playing = false;
    try {
//            mediaPlayer.pause();
      MainMenu.pauseMusic();
      thread.join();
    } catch (InterruptedException e) {
      Log.e("Error:", "joining thread");
    }
  }

  public void resume() {
//        mediaPlayer.start();
    MainMenu.playMusic();
    playing = true;
    thread = new Thread(this);
    thread.start();
  }

  public void restart() {
    Log.d("lastScoresBefore", Integer.toString(MainMenu.getLastScore()));
    MainMenu.setLastScore(score);
    Log.d("LastScoresAfter", Integer.toString(MainMenu.getLastScore()));

    Log.d("highScoresBefore", Integer.toString(MainMenu.getScore()));
    if (score > MainMenu.getScore()) {
      MainMenu.setScore(score);
      Log.d("highScoresAfter", Integer.toString(MainMenu.getScore()));
    }
//        player.reiniciarJugador(width, height);
//        startMap();
//        score = 0;
//        valuesTime = System.currentTimeMillis();
//        mediaPlayer.start();
    Intent intent = new Intent(context, GameOver.class);
//        intent.putExtra("SCORE", renderedScoreString);
    context.startActivity(intent);
  }

  @Override
  public boolean onTouchEvent(MotionEvent motionEvent) {
    //player.saltar(height);
    switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
      case MotionEvent.ACTION_DOWN:
        player.saltar(height);
        break;
      case MotionEvent.ACTION_UP:
        break;
    }
    return true;
  }

  //Funciones de inicializacion -----
  public void startGlobalValues() {
    surface = getHolder();
    paint = new Paint();
    width = Resources.getSystem().getDisplayMetrics().widthPixels;
    height = Resources.getSystem().getDisplayMetrics().heightPixels;
    Bitmap enemigoA = adjustImageScreen(width / 16, height / 5,
        BitmapFactory.decodeResource(this.getResources(), R.drawable.enemigo_a));
    Bitmap enemigoB = adjustImageScreen(width / 16, height / 5,
        BitmapFactory.decodeResource(this.getResources(), R.drawable.enemigo_b));
    startingTime = System.currentTimeMillis();
    generator = new MapGenerator(width, height, enemigoA, enemigoB);

    //Text properties
    paint.setColor(Color.WHITE);
    paint.setTextSize(40f);
    paint.setTypeface(fontLarge);
  }

  public void startPlayer(String genero) {
    Bitmap sprite_a;
    Bitmap sprite_b;
    Bitmap sprite_c;
    Bitmap sprite_j;
    if (genero.equals("masculino")) {
      sprite_a = adjustImageScreen(width / 16, height / 8,
          BitmapFactory.decodeResource(this.getResources(), R.drawable.boy_a));
      sprite_b = adjustImageScreen(width / 16, height / 8,
          BitmapFactory.decodeResource(this.getResources(), R.drawable.boy_b));
      sprite_c = adjustImageScreen(width / 16, height / 8,
          BitmapFactory.decodeResource(this.getResources(), R.drawable.boy_c));
      sprite_j = adjustImageScreen(width / 18, height / 8,
          BitmapFactory.decodeResource(this.getResources(), R.drawable.boy_j));
    } else {
      sprite_a = adjustImageScreen(width / 16, height / 8,
          BitmapFactory.decodeResource(this.getResources(), R.drawable.girl_a));
      sprite_b = adjustImageScreen(width / 16, height / 8,
          BitmapFactory.decodeResource(this.getResources(), R.drawable.girl_b));
      sprite_c = adjustImageScreen(width / 16, height / 8,
          BitmapFactory.decodeResource(this.getResources(), R.drawable.girl_c));
      sprite_j = adjustImageScreen(width / 16, height / 8,
          BitmapFactory.decodeResource(this.getResources(), R.drawable.girl_j));
    }
    ArrayList<Bitmap> sprites = new ArrayList<>();
    sprites.add(sprite_a);
    sprites.add(sprite_b);
    sprites.add(sprite_c);
    sprites.add(sprite_b);
    sprites.add(sprite_j);
    player = new Player(width / 10, height - (height / 3), width / 18, height / 8, sprites);
  }

  public void startMap() {
    ArrayList<Plataforma> inicio = new ArrayList<>(); // Plataformas iniciales del largo de la pantalla.
    for (int i = 0; i <= width; i += width / 16) {
      Plataforma plataforma = new Plataforma(i, height - (height / 32), width / 16, height / 32,
          true,
          forestPlatform);
      inicio.add(plataforma);
    }
    gameMap = new GameMap(exampleBackground, inicio);
  }

  public Bitmap adjustImageScreen(int x, int y, Bitmap imagen) {
    imagen = Bitmap.createScaledBitmap(imagen, x, y, true);
    return imagen;
  }

  public void loadSprites() {
    int randomNum = 1 + (int) (Math.random() * 6);
    switch (randomNum) {
      case 1:
        exampleBackground = adjustImageScreen(width, height,
            BitmapFactory.decodeResource(this.getResources(), R.drawable.fondo_domo));
        break;
      case 2:
        exampleBackground = adjustImageScreen(width, height,
            BitmapFactory.decodeResource(this.getResources(), R.drawable.fondo_parque_patinar));
        break;
      case 3:
        exampleBackground = adjustImageScreen(width, height,
            BitmapFactory.decodeResource(this.getResources(), R.drawable.fondo_parque_bosque));
        break;
      case 4:
        exampleBackground = adjustImageScreen(width, height,
            BitmapFactory.decodeResource(this.getResources(), R.drawable.fondo_campo_ferial));
        break;
      case 5:
        exampleBackground = adjustImageScreen(width, height,
            BitmapFactory.decodeResource(this.getResources(), R.drawable.fondo_laberinto));
        break;
      case 6:
        exampleBackground = adjustImageScreen(width, height,
            BitmapFactory.decodeResource(this.getResources(), R.drawable.fondo_cancha));
        break;
    }
//        exampleBackground = adjustImageScreen(width, height, BitmapFactory.decodeResource(this.getResources(), R.drawable.fondo_parque_patinar));
    forestPlatform = adjustImageScreen(width / 16, height / 32,
        BitmapFactory.decodeResource(this.getResources(), R.drawable.plataforma));
  }

  public void mapGenerator() {
    switch (difficulty) {
      case "Facil":
        generateEasyPlatform();
        break;
      case "Intermedio":
        generateMediumPlatform();
        break;
      default:
        generateHardPlatform();
        break;
    }
  }

  public void generateEasyPlatform() {
    Plataforma vieja = gameMap.getMapa().get(gameMap.getMapa().size() - 1);
    Random randomGenerator = new Random();
    if (vieja.getPos_y() == height - vieja.getDim_y()) {
      int direccion = randomGenerator.nextInt(3) + 1;
      if (direccion == 1 || direccion == 3) {
        Plataforma nueva = new Plataforma(width, vieja.getPos_y(), width / 16, height / 32, true,
            forestPlatform);
        gameMap.addPlatform(nueva);
      } else {
        Plataforma nueva = new Plataforma(width, vieja.getPos_y() - ((height / 32) * 5), width / 16,
            height / 32, true, forestPlatform);
        gameMap.addPlatform(nueva);
      }
    } else if (vieja.getPos_y() <= height - vieja.getDim_y() - ((height / 32) * 5) * 2) {
      int direccion = randomGenerator.nextInt(3) + 1;
      if (direccion == 1 || direccion == 3) {
        Plataforma nueva = new Plataforma(width, vieja.getPos_y(), width / 16, height / 32, true,
            forestPlatform);
        gameMap.addPlatform(nueva);
      } else {
        Plataforma nueva = new Plataforma(width, vieja.getPos_y() + ((height / 32) * 5), width / 16,
            height / 32, true, forestPlatform);
        gameMap.addPlatform(nueva);
      }
    } else {
      int direccion = randomGenerator.nextInt(5) + 1;
      if (direccion == 1 || direccion == 3 || direccion == 5) { // Siga Recto ----
        Plataforma nueva = new Plataforma(width, vieja.getPos_y(), width / 16, height / 32, true,
            forestPlatform);
        gameMap.addPlatform(nueva);
      } else if (direccion == 2) {
        Plataforma nueva = new Plataforma(width, vieja.getPos_y() - ((height / 32) * 5), width / 16,
            height / 32, true, forestPlatform);
        gameMap.addPlatform(nueva);
      } else {
        Plataforma nueva = new Plataforma(width, vieja.getPos_y() + ((height / 32) * 5), width / 16,
            height / 32, true, forestPlatform);
        gameMap.addPlatform(nueva);
      }
    }

    if (System.currentTimeMillis() >= valuesTime + 5000) {
      Virtue nuevoVirtue = createVirtue(
          gameMap.getMapa().get(gameMap.getMapa().size() - 1).getPos_y());
      gameMap.agregarValor(nuevoVirtue);
      valuesTime = System.currentTimeMillis();
    }
  }

  public void generateMediumPlatform() {
    Plataforma vieja = gameMap.getMapa().get(gameMap.getMapa().size() - 1);
    Random randomGenerator = new Random();
    if (vieja.getPos_y() == height - vieja.getDim_y()) {
      int direccion = randomGenerator.nextInt(2) + 1;
      if (direccion == 1) {
        Plataforma nueva = new Plataforma(width, vieja.getPos_y(), width / 16, height / 32, true,
            forestPlatform);
        gameMap.addPlatform(nueva);
      } else {
        Plataforma nueva = new Plataforma(width, vieja.getPos_y() - ((height / 32) * 5), width / 16,
            height / 32, true, forestPlatform);
        gameMap.addPlatform(nueva);
      }
    } else if (vieja.getPos_y() <= height - vieja.getDim_y() - ((height / 32) * 5) * 3) {
      int direccion = randomGenerator.nextInt(2) + 1;
      if (direccion == 1) {
        Plataforma nueva = new Plataforma(width, vieja.getPos_y(), width / 16, height / 32, true,
            forestPlatform);
        gameMap.addPlatform(nueva);
      } else {
        Plataforma nueva = new Plataforma(width, vieja.getPos_y() + ((height / 32) * 5), width / 16,
            height / 32, true, forestPlatform);
        gameMap.addPlatform(nueva);
      }
    } else {
      int direccion = randomGenerator.nextInt(3) + 1;
      if (direccion == 1) { // Siga Recto ----
        Plataforma nueva = new Plataforma(width, vieja.getPos_y(), width / 16, height / 32, true,
            forestPlatform);
        gameMap.addPlatform(nueva);
      } else if (direccion == 2) {
        Plataforma nueva = new Plataforma(width, vieja.getPos_y() - ((height / 32) * 5), width / 16,
            height / 32, true, forestPlatform);
        gameMap.addPlatform(nueva);
      } else {
        Plataforma nueva = new Plataforma(width, vieja.getPos_y() + ((height / 32) * 5), width / 16,
            height / 32, true, forestPlatform);
        gameMap.addPlatform(nueva);
      }
    }

    if (System.currentTimeMillis() >= valuesTime + 5000) {
      Virtue nuevoVirtue = createVirtue(
          gameMap.getMapa().get(gameMap.getMapa().size() - 1).getPos_y());
      gameMap.agregarValor(nuevoVirtue);
      valuesTime = System.currentTimeMillis();
    }

    if (System.currentTimeMillis() >= enemieTime + 15000) {
      Enemigo nuevoEnemigo = createEnemy(
          gameMap.getMapa().get(gameMap.getMapa().size() - 1).getPos_y());
      gameMap.agregarEnemigo(nuevoEnemigo);
      enemieTime = System.currentTimeMillis();
    }
  }

  public void generateHardPlatform() {
    Plataforma vieja = gameMap.getMapa().get(gameMap.getMapa().size() - 1);
    Random randomGenerator = new Random();
    if (vieja.getPos_y() == height - vieja.getDim_y()) {
      int direccion = randomGenerator.nextInt(2) + 1;
      if (direccion == 1) {
        Plataforma nueva = new Plataforma(width, vieja.getPos_y(), width / 16, height / 32, true,
            forestPlatform);
        gameMap.addPlatform(nueva);
      } else {
        Plataforma nueva = new Plataforma(width, vieja.getPos_y() - ((height / 32) * 5), width / 16,
            height / 32, true, forestPlatform);
        gameMap.addPlatform(nueva);
      }
    } else if (vieja.getPos_y() <= height - vieja.getDim_y() - ((height / 32) * 5) * 5) {
      int direccion = randomGenerator.nextInt(2) + 1;
      if (direccion == 1) {
        Plataforma nueva = new Plataforma(width, vieja.getPos_y(), width / 16, height / 32, true,
            forestPlatform);
        gameMap.addPlatform(nueva);
      } else {
        Plataforma nueva = new Plataforma(width, vieja.getPos_y() + ((height / 32) * 5), width / 16,
            height / 32, true, forestPlatform);
        gameMap.addPlatform(nueva);
      }
    } else {
      int direccion = randomGenerator.nextInt(3) + 1;
      if (direccion == 1) { // Siga Recto ----
        Plataforma nueva = new Plataforma(width, vieja.getPos_y(), width / 16, height / 32, true,
            forestPlatform);
        gameMap.addPlatform(nueva);
      } else if (direccion == 2) {
        Plataforma nueva = new Plataforma(width, vieja.getPos_y() - ((height / 32) * 5), width / 16,
            height / 32, true, forestPlatform);
        gameMap.addPlatform(nueva);
      } else {
        Plataforma nueva = new Plataforma(width, vieja.getPos_y() + ((height / 32) * 5), width / 16,
            height / 32, true, forestPlatform);
        gameMap.addPlatform(nueva);
      }
    }

    if (System.currentTimeMillis() >= valuesTime + 3000) {
      Virtue nuevoVirtue = createVirtue(
          gameMap.getMapa().get(gameMap.getMapa().size() - 1).getPos_y());
      gameMap.agregarValor(nuevoVirtue);
      valuesTime = System.currentTimeMillis();
    }

    if (System.currentTimeMillis() >= enemieTime + 10000) {
      Enemigo nuevoEnemigo = createEnemy(
          gameMap.getMapa().get(gameMap.getMapa().size() - 1).getPos_y());
      gameMap.agregarEnemigo(nuevoEnemigo);
      enemieTime = System.currentTimeMillis();
    }
  }

  public Enemigo createEnemy(int pos_y) {
    Bitmap enemigo1 = adjustImageScreen(width / 16, height / 5,
        BitmapFactory.decodeResource(this.getResources(), R.drawable.enemigo_a));
    Bitmap enemigo2 = adjustImageScreen(width / 16, height / 5,
        BitmapFactory.decodeResource(this.getResources(), R.drawable.enemigo_b));
    if (pos_y == height - (height / 32)) {
      Plataforma nueva = new Plataforma(
          gameMap.getMapa().get(gameMap.getMapa().size() - 1).getPos_x(),
          pos_y - ((height / 32) * 11), width / 16, height / 32, true, forestPlatform);
      enemyPlatform.add(nueva);
      Enemigo enemigo = new Enemigo(nueva.getPos_x(), nueva.getPos_y() - height / 5, width / 16,
          height / 5, enemigo1, enemigo2);
      return enemigo;
    } else if (pos_y <= height - (height / 32) - ((height / 32) * 5) * 5) {
      Plataforma nueva = new Plataforma(
          gameMap.getMapa().get(gameMap.getMapa().size() - 1).getPos_x(),
          pos_y + ((height / 32) * 11), width / 16, height / 32, true, forestPlatform);
      enemyPlatform.add(nueva);
      Enemigo enemigo = new Enemigo(nueva.getPos_x(), nueva.getPos_y() - height / 5, width / 16,
          height / 5, enemigo1, enemigo2);
      return enemigo;
    } else {
      Random randomGenerator = new Random();
      int posicion = randomGenerator.nextInt(2) + 1;
      if (posicion == 1) {
        Plataforma nueva = new Plataforma(
            gameMap.getMapa().get(gameMap.getMapa().size() - 1).getPos_x(),
            pos_y - ((height / 32) * 11), width / 16, height / 32, true, forestPlatform);
        enemyPlatform.add(nueva);
        Enemigo enemigo = new Enemigo(nueva.getPos_x(), nueva.getPos_y() - height / 5, width / 16,
            height / 5, enemigo1, enemigo2);
        return enemigo;
      } else {
        Plataforma nueva = new Plataforma(
            gameMap.getMapa().get(gameMap.getMapa().size() - 1).getPos_x(),
            pos_y + ((height / 32) * 11), width / 16, height / 32, true, forestPlatform);
        enemyPlatform.add(nueva);
        Enemigo enemigo = new Enemigo(nueva.getPos_x(), nueva.getPos_y() - height / 5, width / 16,
            height / 5, enemigo1, enemigo2);
        return enemigo;
      }
    }
  }

  public Virtue createVirtue(int pos_y) {
    Random randomGenerator = new Random();
    int direccion = randomGenerator.nextInt(10) + 1;
    Virtue nuevoVirtue;
    switch (direccion) {
      case 1: // AMOR
        Bitmap amor = adjustImageScreen(width / 42, height / 24, BitmapFactory
            .decodeResource(this.getResources(),
                R.drawable.amor)); // Dimension de Imagen Aprox... 45 x 45
        ArrayList<Bitmap> sprites_amor = new ArrayList<>();
        sprites_amor.add(amor);
        nuevoVirtue = new Virtue(width, height - (height / 3), width / 42, height / 24, height / 5,
            height - (height / 4), sprites_amor, 1);
        return nuevoVirtue;
      case 2: // DIVERSIDAD
        Bitmap diversidad1 = adjustImageScreen(width / 15, height / 14,
            BitmapFactory.decodeResource(this.getResources(), R.drawable.diversidad_gameloop_1));
        Bitmap diversidad2 = adjustImageScreen(width / 15, height / 14,
            BitmapFactory.decodeResource(this.getResources(), R.drawable.diversidad_gameloop_2));
        ArrayList<Bitmap> sprites_diversidad = new ArrayList<>();
        sprites_diversidad.add(diversidad1);
        sprites_diversidad.add(diversidad2);
        nuevoVirtue = new Virtue(width, pos_y - height / 12, width / 15, height / 14, height / 5,
            height - (height / 4), sprites_diversidad, 2);
        return nuevoVirtue;
      case 3: // EMPATIA
        Bitmap empatia1 = adjustImageScreen(width / 18, height / 22,
            BitmapFactory.decodeResource(this.getResources(), R.drawable.empatia_gameloop_1));
        Bitmap empatia2 = adjustImageScreen(width / 18, height / 22,
            BitmapFactory.decodeResource(this.getResources(), R.drawable.empatia_gameloop_2));
        ArrayList<Bitmap> sprites_empatia = new ArrayList<>();
        sprites_empatia.add(empatia1);
        sprites_empatia.add(empatia2);
        nuevoVirtue = new Virtue(width, pos_y - height / 22, width / 18, height / 22, height / 5,
            height - (height / 4), sprites_empatia, 2);
        return nuevoVirtue;
      case 4: // EQUIDAD
        Bitmap equidad1 = adjustImageScreen(width / 23, height / 11,
            BitmapFactory.decodeResource(this.getResources(), R.drawable.equidad_gameloop_1));
        Bitmap equidad2 = adjustImageScreen(width / 23, height / 11,
            BitmapFactory.decodeResource(this.getResources(), R.drawable.equidad_gameloop_2));
        ArrayList<Bitmap> sprites_equidad = new ArrayList<>();
        sprites_equidad.add(equidad1);
        sprites_equidad.add(equidad2);
        nuevoVirtue = new Virtue(width, pos_y - height / 11, width / 23, height / 11, height / 5,
            height - (height / 4), sprites_equidad, 2);
        return nuevoVirtue;
      case 5: // HONESTIDAD
        Bitmap honestidad1 = adjustImageScreen(width / 21, width / 21,
            BitmapFactory.decodeResource(this.getResources(), R.drawable.honestidad_gameloop_1));
        Bitmap honestidad2 = adjustImageScreen(width / 21, width / 21,
            BitmapFactory.decodeResource(this.getResources(), R.drawable.honestidad_gameloop_2));
        ArrayList<Bitmap> sprites_honestidad = new ArrayList<>();
        sprites_honestidad.add(honestidad1);
        sprites_honestidad.add(honestidad2);
        nuevoVirtue = new Virtue(width, pos_y - width / 19, width / 21, width / 21, height / 5,
            height - (height / 4), sprites_honestidad, 2);
        return nuevoVirtue;
      case 6: // LIBERTAD
        Bitmap libertad1 = adjustImageScreen(width / 35, height / 21,
            BitmapFactory.decodeResource(this.getResources(), R.drawable.libertad_gameloop_1));
        Bitmap libertad2 = adjustImageScreen(width / 35, height / 21,
            BitmapFactory.decodeResource(this.getResources(), R.drawable.libertad_gameloop_2));
        ArrayList<Bitmap> sprites_libertad = new ArrayList<>();
        sprites_libertad.add(libertad1);
        sprites_libertad.add(libertad2);
        nuevoVirtue = new Virtue(width, height - (height / 3), width / 35, height / 21, height / 5,
            height - (height / 4), sprites_libertad, 1);
        return nuevoVirtue;
      case 7: // PAZ
        Bitmap paz1 = adjustImageScreen(width / 14, width / 14,
            BitmapFactory.decodeResource(this.getResources(), R.drawable.paz_gameloop_1));
        Bitmap paz2 = adjustImageScreen(width / 14, width / 14,
            BitmapFactory.decodeResource(this.getResources(), R.drawable.paz_gameloop_2));
        ArrayList<Bitmap> sprites_paz = new ArrayList<>();
        sprites_paz.add(paz1);
        sprites_paz.add(paz2);
        nuevoVirtue = new Virtue(width, pos_y - width / 12, width / 14, width / 14, height / 5,
            height - (height / 4), sprites_paz, 2);
        return nuevoVirtue;
      case 8: // RESPETO
        Bitmap respeto1 = adjustImageScreen(width / 16, height / 12,
            BitmapFactory.decodeResource(this.getResources(), R.drawable.respeto_gameloop_1));
        Bitmap respeto2 = adjustImageScreen(width / 16, height / 12,
            BitmapFactory.decodeResource(this.getResources(), R.drawable.respeto_gameloop_2));
        ArrayList<Bitmap> sprites_respeto = new ArrayList<>();
        sprites_respeto.add(respeto1);
        sprites_respeto.add(respeto2);
        nuevoVirtue = new Virtue(width, pos_y - height / 10, width / 16, height / 12, height / 5,
            height - (height / 4), sprites_respeto, 2);
        return nuevoVirtue;
      case 9: // RESPONSABILIDAD
        Bitmap responsabilidad1 = adjustImageScreen(width / 30, height / 13, BitmapFactory
            .decodeResource(this.getResources(), R.drawable.responsabilidad_gameloop_1));
        ArrayList<Bitmap> sprites_responsabilidad = new ArrayList<>();
        sprites_responsabilidad.add(responsabilidad1);
        nuevoVirtue = new Virtue(width, height - (height / 3), width / 30, height / 13, height / 5,
            height - (height / 4), sprites_responsabilidad, 1);
        return nuevoVirtue;
      case 10: // SOLIDARIDAD
        Bitmap solidaridad1 = adjustImageScreen(width / 16, height / 12,
            BitmapFactory.decodeResource(this.getResources(), R.drawable.solidaridad_gameloop_1));
        Bitmap solidaridad2 = adjustImageScreen(width / 16, height / 12,
            BitmapFactory.decodeResource(this.getResources(), R.drawable.solidaridad_gameloop_2));
        ArrayList<Bitmap> sprites_solidaridad = new ArrayList<>();
        sprites_solidaridad.add(solidaridad1);
        sprites_solidaridad.add(solidaridad2);
        nuevoVirtue = new Virtue(width, pos_y - height / 12, width / 16, height / 12, height / 5,
            height - (height / 4), sprites_solidaridad, 2);
        return nuevoVirtue;
    }
    return null;
  }

}

