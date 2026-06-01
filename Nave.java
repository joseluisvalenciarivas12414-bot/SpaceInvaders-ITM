/**
 * CLASE HIJA — Nave (el jugador)
 *
 * Hereda de Personaje y representa la nave controlada por el jugador.
 * Se mueve horizontalmente y dispara hacia ARRIBA.
 *
 * Conceptos POO aplicados:
 *   - Herencia: extends Personaje + super() en el constructor
 *   - Polimorfismo: @Override en atacar() → dispara hacia arriba
 *   - Encapsulación: atributos propios private con getters
 */
public class Nave extends Personaje {

    // ── Atributos propios de la Nave ─────────────────────────────────────
    private String nombreJugador; // Nombre de quien juega
    private int    ancho;         // Límite derecho del tablero (para no salirse)

    // ── Constructor ──────────────────────────────────────────────────────
    /**
     * Crea la nave del jugador en el centro inferior del tablero.
     * Llama a super() para inicializar posición y vidas heredadas.
     *
     * @param nombreJugador nombre del jugador
     * @param ancho         ancho total del tablero
     */
    public Nave(String nombreJugador, int ancho) {
        super(ancho / 2, Juego.ALTO - 1, 3); // posición x central, fila inferior, 3 vidas
        this.nombreJugador = nombreJugador;
        this.ancho         = ancho;
    }

    // ── Getters propios ──────────────────────────────────────────────────
    public String getNombreJugador() { return nombreJugador; }
    public int    getAncho()         { return ancho; }

    // ── Métodos de movimiento ────────────────────────────────────────────

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

    // ── Polimorfismo: @Override ──────────────────────────────────────────
    /**
     * La nave dispara hacia ARRIBA (dirección -1).
     * Sale desde la posición actual de la nave.
     *
     * @return un Disparo con dirección hacia arriba
     */
    @Override
    public Disparo atacar() {
        return new Disparo(getX(), getY() - 1, -1); // -1 = sube
    }

    // ── toString ────────────────────────────────────────────────────────
    @Override
    public String toString() {
        return "Nave[jugador=" + nombreJugador + ", x=" + getX() + ", vidas=" + getVidas() + "]";
    }
}
