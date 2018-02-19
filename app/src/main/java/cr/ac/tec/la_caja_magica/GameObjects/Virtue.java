package cr.ac.tec.la_caja_magica.GameObjects;


import android.graphics.Bitmap;
import java.util.ArrayList;

public class Virtue {

  ArrayList<Bitmap> sprites;
    private int pos_x;
    private int pos_y;
    private int dim_x;
    private int dim_y;
    private int techo;
    private int suelo;
    private int direccion; // 0 hacia abajo 1 hacia arriba
    private int movementStyle;
    private long lastSprite;
    private int actualSprite;

  public Virtue(int pos_x, int pos_y, int dim_x, int dim_y, int techo, int suelo,
      ArrayList<Bitmap> sprites, int movementStyle) {
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.dim_x = dim_x;
        this.dim_y = dim_y;
        this.techo = techo;
        this.suelo = suelo;
        this.sprites = sprites;
        direccion = 1;
        lastSprite = System.currentTimeMillis();
        actualSprite = 0;
        this.movementStyle = movementStyle;
    }

    // Getters -----
    public int getPos_x(){return pos_x;}

  public void setPos_x(int x) {
    pos_x = x;
  }

    public int getPos_y(){return pos_y;}
    public int getDim_x(){return dim_x;}
    public int getDim_y(){return dim_y;}
    public Bitmap getSprite(){
        if (System.currentTimeMillis() >= lastSprite + 200){
            if (sprites.size() > 1) {
                if (actualSprite == 0) {actualSprite = 1;} else {actualSprite = 0;}
                lastSprite = System.currentTimeMillis();
            }
        }
        return sprites.get(actualSprite);
    }

    // Funcion de Movimiento -----
    public void movimiento(int ancho ,int alto){
        if (movementStyle == 1){
            if (direccion == 1){
                pos_y -= alto / 135;
                if (pos_y <= techo){
                    direccion = 0;
                }
            }
            else {
                pos_y += alto / 135;
                if (pos_y >= suelo){
                    direccion = 1;
                }
            }
        }
        pos_x -= ancho / 480;
    }
}
