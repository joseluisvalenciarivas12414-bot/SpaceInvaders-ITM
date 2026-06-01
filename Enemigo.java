/**
 * CLASE HIJA — Enemigo
 *
 * Hereda de Personaje y representa un invasor espacial.
 * Se mueve en bloque y dispara hacia ABAJO.
 *
 * Conceptos POO aplicados:
 *   - Herencia: extends Personaje + super() en el constructor
 *   - Polimorfismo: @Override en atacar() → dispara hacia abajo
 *   - Encapsulación: atributo propio private con getter/setter
 */
public class Enemigo extends Personaje {

    // ── Atributo propio del Enemigo ──────────────────────────────────────
    private int puntos; // Puntos que da al ser destruido

    // ── Constructor ──────────────────────────────────────────────────────
    /**
     * Crea un enemigo en la posición indicada con 1 sola vida.
     * Llama a super() para inicializar posición y vida heredadas.
     *
     * @param x      columna inicial
     * @param y      fila inicial
     * @param puntos puntos que otorga al morir
     */
    public Enemigo(int x, int y, int puntos) {
        super(x, y, 1); // Los enemigos solo tienen 1 vida
        this.puntos = puntos;
    }

    // ── Getter / Setter ──────────────────────────────────────────────────
    public int  getPuntos()       { return puntos; }
    public void setPuntos(int p)  { this.puntos = p; }

    // ── Polimorfismo: @Override ──────────────────────────────────────────
    /**
     * El enemigo dispara hacia ABAJO (dirección +1).
     * El disparo sale desde la posición actual del enemigo.
     *
     * @return un Disparo con dirección hacia abajo
     */
    @Override
    public Disparo atacar() {
        return new Disparo(getX(), getY() + 1, 1); // +1 = baja
    }

    // ── toString ────────────────────────────────────────────────────────
    @Override
    public String toString() {
        return "Enemigo[x=" + getX() + ", y=" + getY() + ", puntos=" + puntos + "]";
    }
}
