
public class Nave extends Personaje {

   
    private String nombreJugador;
    private int    ancho;        

   
    public Nave(String nombreJugador, int ancho) {
        super(ancho / 2, Juego.ALTO - 1, 3); // posición x central, fila inferior, 3 vidas
        this.nombreJugador = nombreJugador;
        this.ancho         = ancho;
    }

    
    public String getNombreJugador() { return nombreJugador; }
    public int    getAncho()         { return ancho; }

    

    /** Mueve la nave un paso a la izquierda (sin salirse del tablero). */
    public void moverIzquierda() {
        if (getX() > 0) {
            setX(getX() - 1);
        }
    }

    /** Mueve la nave un paso a la derecha (sin salirse del tablero). */
    public void moverDerecha() {
        if (getX() < ancho - 1) {
            setX(getX() + 1);
        }
    }

   
    @Override
    public Disparo atacar() {
        return new Disparo(getX(), getY() - 1, -1); // -1 = sube
    }

    
    @Override
    public String toString() {
        return "Nave[jugador=" + nombreJugador + ", x=" + getX() + ", vidas=" + getVidas() + "]";
    }
}
