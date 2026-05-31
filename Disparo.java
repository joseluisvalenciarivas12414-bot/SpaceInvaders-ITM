/**
 * CLASE — Disparo
 *
 * Representa una bala en el tablero. Puede moverse hacia arriba (jugador)
 * o hacia abajo (enemigos) dependiendo de quién disparó.
 *
 * Conceptos POO aplicados:
 *   - Encapsulación: todos los atributos son private con getters/setters
 *   - Clase independiente con constructor propio
 */
public class Disparo {

    // ── Atributos privados ───────────────────────────────────────────────
    private int     x;          // Columna donde está el disparo
    private int     y;          // Fila donde está el disparo
    private int     direccion;  // -1 = sube (jugador), +1 = baja (enemigo)
    private boolean activo;     // false = el disparo debe eliminarse

    // ── Constructor ──────────────────────────────────────────────────────
    /**
     * Crea un disparo en la posición indicada con una dirección de movimiento.
     *
     * @param x         columna inicial
     * @param y         fila inicial
     * @param direccion -1 para subir, +1 para bajar
     */
    public Disparo(int x, int y, int direccion) {
        this.x         = x;
        this.y         = y;
        this.direccion = direccion;
        this.activo    = true; // Nace activo
    }

    // ── Getters ──────────────────────────────────────────────────────────
    public int     getX()         { return x; }
    public int     getY()         { return y; }
    public int     getDireccion() { return direccion; }
    public boolean isActivo()     { return activo; }

    // ── Setters ──────────────────────────────────────────────────────────
    public void setX(int x)           { this.x      = x; }
    public void setY(int y)           { this.y      = y; }
    public void setActivo(boolean a)  { this.activo = a; }

    // ── Método principal ─────────────────────────────────────────────────
    /**
     * Avanza el disparo un paso en su dirección.
     * Si sale del tablero, se desactiva en la clase Juego.
     */
    public void mover() {
        this.y += this.direccion; // Suma -1 (sube) o +1 (baja)
    }

    // ── toString ─────────────────────────────────────────────────────────
    @Override
    public String toString() {
        String dir = (direccion == -1) ? "ARRIBA (jugador)" : "ABAJO (enemigo)";
        return "Disparo[x=" + x + ", y=" + y + ", dir=" + dir + ", activo=" + activo + "]";
    }
}
