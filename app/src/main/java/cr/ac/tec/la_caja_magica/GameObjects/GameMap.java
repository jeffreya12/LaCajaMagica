package cr.ac.tec.la_caja_magica.GameObjects;

import android.graphics.Bitmap;
import java.util.ArrayList;

/**
 * Created by Charlie on 9/20/2016.
 */
public class GameMap {

  private int fondo_pos_x;
  private Bitmap fondo;
  private ArrayList<cr.ac.tec.la_caja_magica.GameObjects.Plataforma> mapa;
  private ArrayList<Enemigo> enemigos;
  private ArrayList<Virtue> valores;

  public GameMap(Bitmap fondo, ArrayList<cr.ac.tec.la_caja_magica.GameObjects.Plataforma> mapa) {
    this.mapa = mapa;
    fondo_pos_x = 0;
    enemigos = new ArrayList<>();
    valores = new ArrayList<>();
    this.fondo = fondo;
  }

  public void addPlatform(cr.ac.tec.la_caja_magica.GameObjects.Plataforma nueva) {
    mapa.add(nueva);
  }

  public void eliminarPlataforma(cr.ac.tec.la_caja_magica.GameObjects.Plataforma vieja) {
    mapa.remove(vieja);
  }

  public void agregarValor(Virtue nuevo) {
    valores.add(nuevo);
  }

  public void eliminarValor(Virtue virtue) {
    valores.remove(virtue);
  }

  public void agregarEnemigo(Enemigo nuevo) {
    enemigos.add(nuevo);
  }

  public void eliminarEnemigo(Enemigo viejo) {
    enemigos.remove(viejo);
  }

  // Getters -----

  public Bitmap getFondo() {
    return fondo;
  }

  public int getFondo_pos_x() {
    return fondo_pos_x;
  }

  public ArrayList<cr.ac.tec.la_caja_magica.GameObjects.Plataforma> getMapa() {
    return mapa;
  }

  public ArrayList<Enemigo> getEnemigos() {
    return enemigos;
  }

  public ArrayList<Virtue> getValores() {
    return valores;
  }

  // Actualizar Fondo -----
  public void movimientoFondo(int ancho_pantalla) {
    fondo_pos_x -= 1;
    if (fondo_pos_x <= -ancho_pantalla) {
      fondo_pos_x = 0;
    }
  }
}
