
public class Nave extends Personaje {

   
    private String nombreJugador;
    private int    ancho;        

   
    public Nave(String nombreJugador, int ancho) {
        super(ancho / 2, Juego.ALTO - 1, 3); 
        this.nombreJugador = nombreJugador;
        this.ancho         = ancho;
    }

    
    public String getNombreJugador() { return nombreJugador; }
    public int    getAncho()         { return ancho; }

    

  
    public void moverIzquierda() {
        if (getX() > 0) {
            setX(getX() - 1);
        }
    }

   
    public void moverDerecha() {
        if (getX() < ancho - 1) {
            setX(getX() + 1);
        }
    }

   
    @Override
    public Disparo atacar() {
        return new Disparo(getX(), getY() - 1, -1); 
    }

    
    @Override
    public String toString() {
        return "Nave[jugador=" + nombreJugador + ", x=" + getX() + ", vidas=" + getVidas() + "]";
    }
}
