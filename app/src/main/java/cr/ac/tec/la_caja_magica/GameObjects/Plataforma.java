package cr.ac.tec.la_caja_magica.GameObjects;


import android.graphics.Bitmap;

public class Plataforma {

  private int pos_x;
  private int pos_y;
  private int dim_x;
  private int dim_y;

  private boolean secure;
  private Bitmap sprite;

  public Plataforma(int pos_x, int pos_y, int dim_x, int dim_y, boolean secure, Bitmap sprite) {
    this.pos_x = pos_x;
    this.pos_y = pos_y;
    this.dim_x = dim_x;
    this.dim_y = dim_y;
    this.secure = secure;
    this.sprite = sprite;
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
    return sprite;
  }

  // Funciones de Movimiento -----
  public void movimiento(int ancho) {
    pos_x -= ancho / 480;
  }

}
