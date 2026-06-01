import java.util.ArrayList;
import java.util.Scanner;


public class Juego {

    
    static final int ANCHO   = 20; // Columnas del tablero
    static final int ALTO    = 10; // Filas del tablero
    static final int MAX_COL = 5;  // Columnas de enemigos por fila

    
    private Nave               nave;              // La nave del jugador
    private ArrayList<Enemigo> enemigos;          // Todos los enemigos activos
    private ArrayList<Disparo> disparosJugador;   // Balas del jugador (suben)
    private ArrayList<Disparo> disparosEnemigos;  // Balas enemigas (bajan)
    private int                puntaje;           // Puntaje acumulado
    private int                nivel;             // Nivel actual
    private boolean            corriendo;         // false = fin de partida
    private int                direccionEnemigos; // +1 derecha, -1 izquierda
    private Scanner            sc;                // Lector de teclado

    
    
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

    
    public int  getPuntaje() { return puntaje; }
    public int  getNivel()   { return nivel; }
    public Nave getNave()    { return nave; }

   
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

    
    void dibujarTablero() {
        // Limpia la pantalla (ANSI escape)
        System.out.print("\033[H\033[2J");
        System.out.flush();

        
        System.out.println("=== SPACE INVADERS ===");
        System.out.println("Jugador: " + nave.getNombreJugador()
                + "  |  Puntaje: " + puntaje
                + "  |  Nivel: "   + nivel
                + "  |  Vidas: "   + nave.getVidas());
        System.out.println();

        
        char[][] tablero = new char[ALTO][ANCHO];
        for (int f = 0; f < ALTO; f++)
            for (int c = 0; c < ANCHO; c++)
                tablero[f][c] = ' ';

      
        for (Enemigo en : enemigos) {
            int ex = en.getX(), ey = en.getY();
            if (en.isActivo() && dentroDelTablero(ex, ey))
                tablero[ey][ex] = 'W';
        }

       
        for (Disparo d : disparosJugador) {
            int dx = d.getX(), dy = d.getY();
            if (d.isActivo() && dentroDelTablero(dx, dy))
                tablero[dy][dx] = '|';
        }

        
        for (Disparo d : disparosEnemigos) {
            int dx = d.getX(), dy = d.getY();
            if (d.isActivo() && dentroDelTablero(dx, dy))
                tablero[dy][dx] = '!';
        }

       
        int nx = nave.getX();
        if (nx >= 0 && nx < ANCHO)
            tablero[ALTO - 1][nx] = 'A';

       
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

   
    private boolean dentroDelTablero(int x, int y) {
        return x >= 0 && x < ANCHO && y >= 0 && y < ALTO;
    }

   
    void moverEnemigos() {
        boolean tocoBorde = false;

       
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

    
    void moverDisparos() {
        
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
