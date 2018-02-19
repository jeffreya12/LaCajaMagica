package cr.ac.tec.la_caja_magica.GameObjects;


import android.graphics.Bitmap;

public class Enemigo {

  // Variables de Posicionamiento y Dimension -----
  private int pos_x;
  private int pos_y;
  private int dim_x;
  private int dim_y;
  // Variables de Sprite (imagen) y Tiempo -----
  private Bitmap sprite_a;
  private Bitmap sprite_b;
  private long ultimoTiempoDeSprite;
  private Bitmap spriteActual;

  public Enemigo(int pos_x, int pos_y, int dim_x, int dim_y, Bitmap sprite_a, Bitmap sprite_b) {
    this.pos_x = pos_x;
    this.pos_y = pos_y;
    this.dim_x = dim_x;
    this.dim_y = dim_y;
    this.sprite_a = sprite_a;
    this.sprite_b = sprite_b;
    ultimoTiempoDeSprite = System.currentTimeMillis();
    spriteActual = sprite_a;
  }

  // Getters -----
  public int getPos_x() {
    return pos_x;
  }

  public void setPos_x(int x) {
    pos_x = x;
  }

  public int getPos_y() {
    return pos_y;
  }

  public int getDim_x() {
    return dim_x;
  }

  public int getDim_y() {
    return dim_y;
  }

  public Bitmap getSprite() {
    if (System.currentTimeMillis() >= ultimoTiempoDeSprite + 175) {
      if (spriteActual == sprite_a) {
        spriteActual = sprite_b;
      } else if (spriteActual == sprite_b) {
        spriteActual = sprite_a;
      }
      ultimoTiempoDeSprite = System.currentTimeMillis();
    }
    return spriteActual;
  }

  // Funcion de Movimiento -----
  public void movimiento(int ancho) {
    pos_x -= ancho / 480;
  }
}
