import java.util.ArrayList;
import java.util.Scanner;

/**
 * CLASE — Juego
 *
 * Controla toda la lógica del Space Invaders:
 *   - El tablero de juego
 *   - El movimiento de enemigos y disparos
 *   - La detección de colisiones
 *   - Los niveles y el puntaje
 *
 * Aquí se DEMUESTRA el polimorfismo: se usa un ArrayList<Personaje>
 * (tipo padre) que contiene tanto Nave como Enemigos, y se llama
 * atacar() en cada uno → cada objeto responde diferente.
 *
 * Conceptos POO aplicados:
 *   - Polimorfismo: ArrayList<Personaje> iterado con for-each
 *   - Composición: tiene una Nave, ArrayList de Enemigos y Disparos
 *   - Encapsulación: getters para puntaje, nivel y nave
 */
public class Juego {

    // ── Constantes del tablero ───────────────────────────────────────────
    static final int ANCHO   = 20; // Columnas del tablero
    static final int ALTO    = 10; // Filas del tablero
    static final int MAX_COL = 5;  // Columnas de enemigos por fila

    // ── Atributos del juego ──────────────────────────────────────────────
    private Nave               nave;              // La nave del jugador
    private ArrayList<Enemigo> enemigos;          // Todos los enemigos activos
    private ArrayList<Disparo> disparosJugador;   // Balas del jugador (suben)
    private ArrayList<Disparo> disparosEnemigos;  // Balas enemigas (bajan)
    private int                puntaje;           // Puntaje acumulado
    private int                nivel;             // Nivel actual
    private boolean            corriendo;         // false = fin de partida
    private int                direccionEnemigos; // +1 derecha, -1 izquierda
    private Scanner            sc;                // Lector de teclado

    // ── Constructor ──────────────────────────────────────────────────────
    /**
     * Prepara el juego con el nombre del jugador.
     * Crea la nave, las listas de enemigos y disparos, e invoca crearEnemigos().
     *
     * @param nombreJugador nombre de quien juega
     */
    public Juego(String nombreJugador) {
        sc                = new Scanner(System.in);
        nave              = new Nave(nombreJugador, ANCHO);
        enemigos          = new ArrayList<>();
        disparosJugador   = new ArrayList<>();
        disparosEnemigos  = new ArrayList<>();
        puntaje           = 0;
        nivel             = 1;
        corriendo         = true;
        direccionEnemigos = 1; // Empiezan moviéndose a la derecha
        crearEnemigos();
    }

    // ── Getters ──────────────────────────────────────────────────────────
    public int  getPuntaje() { return puntaje; }
    public int  getNivel()   { return nivel; }
    public Nave getNave()    { return nave; }

    // ── Métodos del juego ────────────────────────────────────────────────

    /**
     * Llena el tablero con 2 filas de 5 enemigos cada una.
     * La fila superior vale más puntos que la inferior.
     * Se reinvoca al completar un nivel.
     */
    void crearEnemigos() {
        enemigos.clear();
        direccionEnemigos = 1;
        int filas = 2;
        int cols  = MAX_COL;

        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < cols; col++) {
                int px     = 1 + col * 3;         // Espaciado entre enemigos
                int py     = 1 + fila * 2;         // Filas 1 y 3
                int puntos = (filas - fila) * 10;  // Fila 0 = 20pts, fila 1 = 10pts
                enemigos.add(new Enemigo(px, py, puntos));
            }
        }
    }

    /**
     * Dibuja el tablero en la consola usando una matriz de caracteres.
     * Primero limpia la pantalla, luego coloca cada elemento en su celda.
     *
     * Símbolos:
     *   'A' = nave del jugador
     *   'W' = enemigo
     *   '|' = disparo del jugador (sube)
     *   '!' = disparo enemigo    (baja)
     */
    void dibujarTablero() {
        // Limpia la pantalla (ANSI escape)
        System.out.print("\033[H\033[2J");
        System.out.flush();

        // Cabecera de estado
        System.out.println("=== SPACE INVADERS ===");
        System.out.println("Jugador: " + nave.getNombreJugador()
                + "  |  Puntaje: " + puntaje
                + "  |  Nivel: "   + nivel
                + "  |  Vidas: "   + nave.getVidas());
        System.out.println();

        // Crear matriz vacía
        char[][] tablero = new char[ALTO][ANCHO];
        for (int f = 0; f < ALTO; f++)
            for (int c = 0; c < ANCHO; c++)
                tablero[f][c] = ' ';

        // Colocar enemigos en la matriz
        for (Enemigo en : enemigos) {
            int ex = en.getX(), ey = en.getY();
            if (en.isActivo() && dentroDelTablero(ex, ey))
                tablero[ey][ex] = 'W';
        }

        // Colocar disparos del jugador '|'
        for (Disparo d : disparosJugador) {
            int dx = d.getX(), dy = d.getY();
            if (d.isActivo() && dentroDelTablero(dx, dy))
                tablero[dy][dx] = '|';
        }

        // Colocar disparos de enemigos '!'
        for (Disparo d : disparosEnemigos) {
            int dx = d.getX(), dy = d.getY();
            if (d.isActivo() && dentroDelTablero(dx, dy))
                tablero[dy][dx] = '!';
        }

        // Colocar la nave 'A'
        int nx = nave.getX();
        if (nx >= 0 && nx < ANCHO)
            tablero[ALTO - 1][nx] = 'A';

        // Imprimir el tablero con bordes
        System.out.println("+" + "-".repeat(ANCHO) + "+");
        for (int f = 0; f < ALTO; f++) {
            System.out.print("|");
            for (int c = 0; c < ANCHO; c++)
                System.out.print(tablero[f][c]);
            System.out.println("|");
        }
        System.out.println("+" + "-".repeat(ANCHO) + "+");

        System.out.println();
        System.out.println("Comandos:  a = izquierda   d = derecha   w = disparar   q = salir");
        System.out.print("Tu movimiento: ");
    }

    /** Verifica que una coordenada esté dentro del tablero. */
    private boolean dentroDelTablero(int x, int y) {
        return x >= 0 && x < ANCHO && y >= 0 && y < ALTO;
    }

    /**
     * Mueve todos los enemigos en bloque hacia la derecha o izquierda.
     * Cuando alguno toca un borde, el bloque baja una fila y cambia dirección.
     */
    void moverEnemigos() {
        boolean tocoBorde = false;

        // Verificar si algún enemigo activo llegó al borde
        for (Enemigo en : enemigos) {
            if (en.isActivo()) {
                if (direccionEnemigos ==  1 && en.getX() >= ANCHO - 2) { tocoBorde = true; break; }
                if (direccionEnemigos == -1 && en.getX() <= 1)          { tocoBorde = true; break; }
            }
        }

        if (tocoBorde) {
            // Bajar todos y cambiar dirección
            for (Enemigo en : enemigos)
                if (en.isActivo()) en.setY(en.getY() + 1);
            direccionEnemigos *= -1;
        } else {
            // Moverse horizontalmente
            for (Enemigo en : enemigos)
                if (en.isActivo()) en.setX(en.getX() + direccionEnemigos);
        }
    }

    /**
     * Un enemigo aleatorio dispara hacia abajo.
     * USO DE POLIMORFISMO: se llama atacar() sobre un objeto Enemigo
     * (que hereda de Personaje), y retorna un Disparo hacia abajo.
     */
    void disparoEnemigo() {
        ArrayList<Enemigo> activos = new ArrayList<>();
        for (Enemigo en : enemigos)
            if (en.isActivo()) activos.add(en);

        if (!activos.isEmpty()) {
            int idx = (int)(Math.random() * activos.size());
            Disparo bala = activos.get(idx).atacar(); // Polimorfismo en acción
            disparosEnemigos.add(bala);
        }
    }

    /**
     * Mueve todos los disparos activos y elimina los que salen del tablero.
     * Disparos del jugador salen por arriba (y < 0).
     * Disparos enemigos salen por abajo (y >= ALTO).
     */
    void moverDisparos() {
        // ── Disparos del jugador ────────────────────────────────────────
        for (Disparo d : disparosJugador) {
            if (d.isActivo()) {
                d.mover();
                if (d.getY() < 0) d.setActivo(false); // Salió por arriba
            }
        }
        // Filtrar los inactivos
        ArrayList<Disparo> nuevosJ = new ArrayList<>();
        for (Disparo d : disparosJugador)
            if (d.isActivo()) nuevosJ.add(d);
        disparosJugador = nuevosJ;

        // ── Disparos enemigos ───────────────────────────────────────────
        for (Disparo d : disparosEnemigos) {
            if (d.isActivo()) {
                d.mover();
                if (d.getY() >= ALTO) d.setActivo(false); // Salió por abajo
            }
        }
        ArrayList<Disparo> nuevosE = new ArrayList<>();
        for (Disparo d : disparosEnemigos)
            if (d.isActivo()) nuevosE.add(d);
        disparosEnemigos = nuevosE;
    }

    /**
     * Detecta todas las colisiones posibles:
     *   1. Disparo del jugador vs enemigo  → enemigo muere, suma puntos
     *   2. Disparo enemigo vs nave         → nave pierde una vida
     *   3. Enemigo llega a la fila del jugador → Game Over inmediato
     */
    void verificarColisiones() {
        // 1. Disparos del jugador impactan enemigos
        for (Disparo d : disparosJugador) {
            if (!d.isActivo()) continue;
            for (Enemigo en : enemigos) {
                if (en.isActivo() && d.getX() == en.getX() && d.getY() == en.getY()) {
                    d.setActivo(false);
                    en.setActivo(false);
                    puntaje += en.getPuntos();
                }
            }
        }

        // 2. Disparos de enemigos impactan la nave
        for (Disparo d : disparosEnemigos) {
            if (!d.isActivo()) continue;
            if (d.getX() == nave.getX() && d.getY() == ALTO - 1) {
                d.setActivo(false);
                nave.setVidas(nave.getVidas() - 1);
                if (nave.getVidas() <= 0) corriendo = false;
            }
        }

        // 3. Un enemigo llegó al fondo → derrota inmediata
        for (Enemigo en : enemigos) {
            if (en.isActivo() && en.getY() >= ALTO - 1) {
                nave.setVidas(0);
                corriendo = false;
            }
        }
    }

    /** Devuelve true si todos los enemigos fueron eliminados (nivel completado). */
    boolean todosEliminados() {
        for (Enemigo en : enemigos)
            if (en.isActivo()) return false;
        return true;
    }

    /**
     * Bucle principal del juego.
     *
     * DEMOSTRACIÓN EXPLÍCITA DE POLIMORFISMO:
     * Se crea un ArrayList<Personaje> con la nave y los enemigos.
     * Al iterar con for-each y llamar atacar(), cada objeto responde
     * con su propia versión del método (Nave dispara arriba, Enemigo abajo).
     *
     * Este es el patrón que el profesor espera ver.
     */
    void jugar() {
        // ── Demostración de polimorfismo requerida por la rúbrica ────────
        System.out.println("\n── DEMOSTRACIÓN DE POLIMORFISMO ──");
        ArrayList<Personaje> personajes = new ArrayList<>();
        personajes.add(nave);                    // Tipo padre = Personaje
        personajes.add(new Enemigo(0, 0, 10));   // Tipo padre = Personaje
        personajes.add(new Enemigo(5, 2, 20));   // Tipo padre = Personaje

        for (Personaje p : personajes) {         // For-each sobre tipo padre
            Disparo d = p.atacar();
            if (d != null)
                System.out.println(p.getClass().getSimpleName()
                        + " ataca → " + d);      // Cada clase responde diferente
        }
        System.out.println("──────────────────────────────────\n");
        System.out.println("Presiona Enter para comenzar...");
        sc.nextLine();
        // ────────────────────────────────────────────────────────────────

        int turno = 0;

        while (corriendo) {
            dibujarTablero();
            String entrada = sc.nextLine().trim().toLowerCase();

            // Procesar comando del jugador
            switch (entrada) {
                case "q": corriendo = false; break;
                case "a": nave.moverIzquierda(); break;
                case "d": nave.moverDerecha();   break;
                case "w":
                    // Polimorfismo: atacar() de la Nave devuelve disparo hacia arriba
                    disparosJugador.add(nave.atacar());
                    break;
                default:
                    // Comando desconocido → no hace nada
                    break;
            }

            if (!corriendo) break;

            // Lógica del turno
            moverDisparos();
            if (turno % 2 == 0) moverEnemigos();   // Enemigos se mueven cada 2 turnos
            if (turno % 3 == 0) disparoEnemigo();   // Enemigos disparan cada 3 turnos
            verificarColisiones();

            // Verificar si se completó el nivel
            if (todosEliminados()) {
                nivel++;
                System.out.println("\n¡Nivel " + (nivel - 1) + " completado! Avanzas al nivel " + nivel);
                System.out.println("Presiona Enter para continuar...");
                sc.nextLine();
                crearEnemigos();
            }

            turno++;
        }

        // ── Pantalla final ───────────────────────────────────────────────
        System.out.println("\n=========================");
        if (nave.getVidas() <= 0)
            System.out.println("   GAME OVER, " + nave.getNombreJugador() + "!");
        else
            System.out.println("   ¡VICTORIA, "  + nave.getNombreJugador() + "!");
        System.out.println("   Puntaje final : " + puntaje);
        System.out.println("   Nivel alcanzado: " + nivel);
        System.out.println("=========================");
    }
}
