package cr.ac.tec.la_caja_magica.GameLogic;

import android.graphics.Bitmap;
import cr.ac.tec.la_caja_magica.GameObjects.Enemigo;
import cr.ac.tec.la_caja_magica.GameObjects.GameMap;
import cr.ac.tec.la_caja_magica.GameObjects.Plataforma;
import java.util.ArrayList;

/**
 * Created by Charlie on 9/20/2016.
 */
public class MapGenerator {

  private int width;
  private int height;
  private int dim_x;
  private int dim_y;
  private Bitmap enemyA;
  private Bitmap enemyB;


  public MapGenerator(int ancho_pantalla, int alto_pantalla, Bitmap enemyA, Bitmap enemyB) {
    width = ancho_pantalla;
    height = alto_pantalla;
    dim_x = width / 16;
    dim_y = alto_pantalla / 32;
    this.enemyA = enemyA;
    this.enemyB = enemyB;
  }

  public GameMap getMapa_Prototipo(Bitmap imagen_plataforma, Bitmap fondo) {
    ArrayList<Plataforma> plataformas = new ArrayList<>();
    ArrayList<Enemigo> enemigos = new ArrayList<>();
    int x = 0;
//        while (x < width){
    for (int i = 0; i < 100; i++) {
      Plataforma plataforma = new Plataforma(x, height - (height / 6), dim_x, dim_y, true,
          imagen_plataforma);
      plataformas.add(plataforma);
      x += dim_x;
    }
    Plataforma plataforma1 = new Plataforma(x + dim_x, height - (height / 6) * 2, dim_x, dim_y,
        true, imagen_plataforma);
    Plataforma plataforma2 = new Plataforma(x + dim_x * 2, height - (height / 6) * 2, dim_x, dim_y,
        true, imagen_plataforma);
    Plataforma plataforma3 = new Plataforma(x + dim_x * 5, height - (height / 6) * 2, dim_x, dim_y,
        true, imagen_plataforma);
    Enemigo enemigo1 = new Enemigo(x + dim_x * 5, plataforma3.getPos_y() - height / 5, width / 16,
        height
            / 5, enemyA, enemyB);
    plataformas.add(plataforma1);
    plataformas.add(plataforma2);
    plataformas.add(plataforma3);
    enemigos.add(enemigo1);
    GameMap gameMap = new GameMap(fondo, plataformas);
    return gameMap;
  }

}
