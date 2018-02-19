package cr.ac.tec.la_caja_magica.GameObjects;


import android.graphics.Bitmap;
import java.util.ArrayList;

public class Player {

    // Variables de Dimension y Posicion -----
    private int pos_x;
    private int pos_y;
    private int dim_x;
    private int dim_y;
    // Estado del Personaje -----
    private boolean vivo;
    // Variables de Sprite (imagen) y Tiempo -----
    private long ultimoTiempoDeSprite;
    private int spriteActual;
    private ArrayList <Bitmap> sprites;
    // Variables de Colicion -----
    private boolean cayendo;
    private boolean saltando;
    private int techo;
    private int suelo;

  public Player(int pos_x, int pos_y, int dim_x, int dim_y, ArrayList<Bitmap> sprites) {
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.dim_x = dim_x;
        this.dim_y = dim_y;
        this.sprites = sprites;
        spriteActual = 0;
        cayendo = false;
        saltando = false;
        ultimoTiempoDeSprite = System.currentTimeMillis();
        suelo = pos_y;
        vivo = true;
    }

    // Setters -----
    public void reiniciarJugador(int ancho_pantalla, int alto_pantalla){
        vivo = true;
        pos_x = ancho_pantalla / 10;
        pos_y = alto_pantalla - (alto_pantalla / 3);
        spriteActual = 0;
    }

    // Getters -----
    public int getPos_x(){return pos_x;}
    public int getPos_y(){return pos_y;}
    public boolean getVivo(){return vivo;}
    public Bitmap getSprite(long actualTime){
        if (saltando == true || cayendo == true){spriteActual = 4;}
        else if (actualTime >= ultimoTiempoDeSprite + 175){
            ultimoTiempoDeSprite = actualTime;
            spriteActual += 1;
            if (spriteActual > 3){spriteActual = 0;}
        }
        return sprites.get(spriteActual);
    }

    // Funciones de Movimieno -----
    public void saltar(int alto_pantalla){
        if (saltando == false && cayendo == false){
            saltando = true;
            techo = pos_y - (alto_pantalla / 5);
        }
    }

  // Funcion de Movimiento - 'Movimiento del Player'
    public void movimiento(ArrayList <Plataforma> mapa, int alto_pantalla){
        suelo = alto_pantalla;
        Plataforma plataforma;
        for (int i = 0; i < mapa.size(); i++){
            plataforma = mapa.get(i);
            if ((pos_x + dim_x) >= plataforma.getPos_x() && pos_x <= (plataforma.getPos_x() + plataforma.getDim_x()) && (pos_y + dim_y) <= plataforma.getPos_y()
                    && (pos_y + dim_y) >= plataforma.getPos_y() - (alto_pantalla / 54)){
                suelo = plataforma.getPos_y();
                break;
            }
        }
        if (saltando == true && cayendo == false){
            pos_y -= (alto_pantalla / 135);
            if (pos_y <= techo){
                cayendo = true;
            }
        } else{
            cayendo = true;
            pos_y += (alto_pantalla / 135);
            if (suelo == alto_pantalla){
                if (pos_y >= alto_pantalla){
                    vivo = false;
                }
            }
            else if ((pos_y + dim_y) >= suelo){
                pos_y = suelo - dim_y;
                cayendo = false;
                saltando = false;
            }
        }
    }

  // Funcion de Colicion - 'Colicion del Player con los enemigos'
    public void colicionEnemigos(ArrayList <Enemigo> enemigos){
        for (int i = 0; i < enemigos.size(); i++) {
            if ((pos_x + dim_x) >= enemigos.get(i).getPos_x() && pos_x <= (enemigos.get(i).getPos_x() + enemigos.get(i).getDim_x())){
                if ((pos_y + dim_y) >= enemigos.get(i).getPos_y( ) && pos_y <= (enemigos.get(i).getPos_y() + enemigos.get(i).getDim_y())){
                    vivo = false;
                    break;
                }
            }
        }
    }

  // Funcion de Recoleccion - 'El Player recolecta valores'
  public boolean colicionValores(ArrayList<Virtue> valores) {
        for (int i = 0; i < valores.size(); i++){
            if (((pos_x + dim_x) >= valores.get(i).getPos_x()) &&
                 (pos_x <= (valores.get(i).getPos_x() + valores.get(i).getDim_x())) &&
                ((pos_y + dim_y) >= valores.get(i).getPos_y()) &&
                 (pos_y <= (valores.get(i).getPos_y() + valores.get(i).getDim_y()))){
                    valores.remove(valores.get(i));
                    return true;
            }
        }
        return false;
    }

}
