import java.util.ArrayList;
import java.util.Scanner;

// ===== CLASE JUEGO =====

class Juego {
    static final int ANCHO   = 20;
    static final int ALTO    = 10;
    static final int MAX_COL = 5;

    private Nave nave;
    private ArrayList<Enemigo> enemigos;
    private ArrayList<Disparo> disparosJugador;
    private ArrayList<Disparo> disparosEnemigos;
    private int puntaje;
    private int nivel;
    private boolean corriendo;
    private int direccionEnemigos;
    private Scanner sc;

    Juego(String nombreJugador) {
        sc = new Scanner(System.in);
        nave             = new Nave(nombreJugador, ANCHO);
        enemigos         = new ArrayList<>();
        disparosJugador  = new ArrayList<>();
        disparosEnemigos = new ArrayList<>();
        puntaje           = 0;
        nivel             = 1;
        corriendo         = true;
        direccionEnemigos = 1;
        crearEnemigos();
    }

    // Getters
    public int getPuntaje() { return puntaje; }
    public int getNivel()   { return nivel; }
    public Nave getNave()   { return nave; }

    void crearEnemigos() {
        enemigos.clear();
        direccionEnemigos = 1;
        int filas = 2;
        int cols  = MAX_COL;
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < cols; c++) {
                int px  = 1 + c * 3;
                int py  = 1 + f * 2;
                int pts = (filas - f) * 10;
                enemigos.add(new Enemigo(px, py, pts));
            }
        }
    }

    void dibujarTablero() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }

        System.out.println("=== SPACE INVADERS ===");
        System.out.println("Jugador: " + nave.getNombreJugador() +
                "  |  Puntaje: " + puntaje +
                "  |  Nivel: "   + nivel +
                "  |  Vidas: "   + nave.getVidas());
        System.out.println();

        char[][] tablero = new char[ALTO][ANCHO];
        for (int f = 0; f < ALTO; f++)
            for (int c = 0; c < ANCHO; c++)
                tablero[f][c] = ' ';

        // Colocar enemigos
        for (Enemigo en : enemigos) {
            int ex = en.getX(), ey = en.getY();
            if (en.isActivo() && ey >= 0 && ey < ALTO && ex >= 0 && ex < ANCHO)
                tablero[ey][ex] = 'W';
        }

        // Colocar disparos del jugador
        for (Disparo d : disparosJugador) {
            int dx = d.getX(), dy = d.getY();
            if (d.isActivo() && dy >= 0 && dy < ALTO && dx >= 0 && dx < ANCHO)
                tablero[dy][dx] = '|';
        }

        // Colocar disparos de enemigos
        for (Disparo d : disparosEnemigos) {
            int dx = d.getX(), dy = d.getY();
            if (d.isActivo() && dy >= 0 && dy < ALTO && dx >= 0 && dx < ANCHO)
                tablero[dy][dx] = '!';
        }

        // Colocar nave
        int nx = nave.getX();
        if (nx >= 0 && nx < ANCHO)
            tablero[ALTO - 1][nx] = 'A';

        System.out.print("+");
        for (int i = 0; i < ANCHO; i++) {
            System.out.print("-");
        }
        System.out.println("+");

        for (int f = 0; f < ALTO; f++) {
            System.out.print("|");
            for (int c = 0; c < ANCHO; c++)
                System.out.print(tablero[f][c]);
            System.out.println("|");
        }
        System.out.print("+");
        for (int i = 0; i < ANCHO; i++) {
            System.out.print("-");
        }
        System.out.println("+");
        System.out.println();
        System.out.println("Comandos: a=izquierda  d=derecha  w=disparar  q=salir");
        System.out.print("Tu movimiento: ");
    }

    void moverEnemigos() {
        boolean tocarBorde = false;
        for (Enemigo en : enemigos) {
            if (en.isActivo()) {
                if ((direccionEnemigos ==  1 && en.getX() >= ANCHO - 2) ||
                        (direccionEnemigos == -1 && en.getX() <= 1)) {
                    tocarBorde = true;
                    break;
                }
            }
        }
        if (tocarBorde) {
            for (Enemigo en : enemigos)
                if (en.isActivo()) en.setY(en.getY() + 1);
            direccionEnemigos *= -1;
        }else {
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
            // Polimorfismo: atacar() del enemigo dispara hacia abajo
            disparosEnemigos.add(activos.get(idx).atacar());
        }
    }

    void moverDisparos() {
        // Mover y limpiar disparos del jugador
        for (Disparo d : disparosJugador) {
            if (d.isActivo()) {
                d.mover();
                if (d.getY() < 0) d.setActivo(false);
            }
        }
        ArrayList<Disparo> nuevosJ = new ArrayList<>();
        for (Disparo d : disparosJugador)
            if (d.isActivo()) nuevosJ.add(d);
        disparosJugador = nuevosJ;

        // Mover y limpiar disparos de enemigos
        for (Disparo d : disparosEnemigos) {
            if (d.isActivo()) {
                d.mover();
                if (d.getY() >= ALTO) d.setActivo(false);
            }
        }
        ArrayList<Disparo> nuevosE = new ArrayList<>();
        for (Disparo d : disparosEnemigos)
            if (d.isActivo()) nuevosE.add(d);
        disparosEnemigos = nuevosE;
    }

    void verificarColisiones() {
        // Disparos del JUGADOR vs enemigos (única forma de eliminar enemigos)
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

        // Disparos de ENEMIGOS vs nave
        for (Disparo d : disparosEnemigos) {
            if (!d.isActivo()) continue;
            if (d.getX() == nave.getX() && d.getY() == ALTO - 1) {
                d.setActivo(false);
                nave.setVidas(nave.getVidas() - 1);
                if (nave.getVidas() <= 0) corriendo = false;
            }
        }

        // Enemigos llegan a la fila del jugador = Game Over
        for (Enemigo en : enemigos) {
            if (en.isActivo() && en.getY() >= ALTO - 1) {
                nave.setVidas(0);
                corriendo = false;
            }
        }
    }

    boolean todosEliminados() {
        for (Enemigo en : enemigos)
            if (en.isActivo()) return false;
        return true;
    }

    void jugar() {
        int turno = 0;
        while (corriendo) {
            dibujarTablero();
            String entrada = sc.nextLine().trim().toLowerCase();

            if (entrada.equals("q")) {
                corriendo = false;
                break;
            } else if (entrada.equals("a")) {
                nave.moverIzquierda();
            } else if (entrada.equals("d")) {
                nave.moverDerecha();
            } else if (entrada.equals("w")) {
                // Polimorfismo: atacar() de la nave dispara hacia arriba
                disparosJugador.add(nave.atacar());
            }

            moverDisparos();
            if (turno % 2 == 0) moverEnemigos();
            if (turno % 3 == 0) disparoEnemigo();
            verificarColisiones();

            // Al eliminar todos los enemigos, sube de nivel infinitamente
            if (todosEliminados()) {
                nivel++;
                disparosJugador.clear();
                disparosEnemigos.clear();
                System.out.println("\n¡Nivel " + (nivel - 1) + " completado! Avanzas al nivel " + nivel);
                System.out.println("Presiona Enter para continuar...");
                sc.nextLine();
                crearEnemigos();
            }

            turno++;
        }

        // Solo se llega aquí con Game Over (vidas = 0) o presionando q
        System.out.println("\n=========================");
        if (nave.getVidas() <= 0)
            System.out.println("   GAME OVER, " + nave.getNombreJugador() + "!");
        else
            System.out.println("   Gracias por jugar, " + nave.getNombreJugador() + "!");
        System.out.println("   Puntaje final: " + puntaje);
        System.out.println("   Nivel alcanzado: " + nivel);
        System.out.println("=========================");
    }
}