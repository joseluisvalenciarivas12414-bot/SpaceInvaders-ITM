/**
 * CLASE BASE (Padre) — Personaje
 *
 * Representa cualquier entidad del juego que tiene posición y vidas.
 * Todas las demás entidades (Nave, Enemigo) HEREDAN de esta clase.
 *
 * Conceptos POO aplicados:
 *   - Encapsulación: atributos private con getters/setters
 *   - Herencia: clase padre de Nave y Enemigo
 *   - Polimorfismo: el método atacar() es sobreescrito por las subclases
 */
public class Personaje {

    // ── Atributos privados (Encapsulación) ──────────────────────────────
    private int x;          // Posición horizontal en el tablero
    private int y;          // Posición vertical en el tablero
    private int vidas;      // Cantidad de vidas que le quedan
    private boolean activo; // true = sigue en juego, false = eliminado

    // ── Constructor ─────────────────────────────────────────────────────
    /**
     * Inicializa un personaje con posición y vidas.
     * @param x     columna inicial
     * @param y     fila inicial
     * @param vidas cantidad de vidas
     */
    public Personaje(int x, int y, int vidas) {
        this.x      = x;
        this.y      = y;
        this.vidas  = vidas;
        this.activo = true;   // Nace activo
    }

    // ── Getters ─────────────────────────────────────────────────────────
    public int     getX()      { return x; }
    public int     getY()      { return y; }
    public int     getVidas()  { return vidas; }
    public boolean isActivo()  { return activo; }

    // ── Setters ─────────────────────────────────────────────────────────
    public void setX(int x)           { this.x      = x; }
    public void setY(int y)           { this.y      = y; }
    public void setVidas(int vidas)   { this.vidas  = vidas; }
    public void setActivo(boolean a)  { this.activo = a; }

    // ── Método polimórfico ──────────────────────────────────────────────
    /**
     * Crea un disparo. Cada subclase sobreescribe este método
     * para disparar en una dirección diferente.
     *
     * @return un objeto Disparo, o null si no aplica
     */
    public Disparo atacar() {
        return null; // La clase base no dispara por sí sola
    }

    // ── toString ────────────────────────────────────────────────────────
    @Override
    public String toString() {
        return "Personaje[x=" + x + ", y=" + y + ", vidas=" + vidas + ", activo=" + activo + "]";
    }
}
