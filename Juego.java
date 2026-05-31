import java.util.ArrayList;
import java.util.Scanner;

public class Juego {

    static final int ANCHO   = 20;
    static final int ALTO    = 10;
    static final int MAX_COL = 5;

    private Nave               nave;
    private ArrayList<Enemigo> enemigos;
    private ArrayList<Disparo> disparosJugador;
    private ArrayList<Disparo> disparosEnemigos;
    private int                puntaje;
    private int                nivel;
    private boolean            corriendo;
    private int                direccionEnemigos;
    private Scanner            sc;

    public Juego(String nombreJugador) {
        sc                = new Scanner(System.in);
        nave              = new Nave(nombreJugador, ANCHO);
        enemigos          = new ArrayList<>();
        disparosJugador   = new ArrayList<>();
        disparosEnemigos  = new ArrayList<>();
        puntaje           = 0;
        nivel             = 1;
        corriendo         = true;
        direccionEnemigos = 1;
        crearEnemigos();
    }

    public int  getPuntaje() { return puntaje; }
    public int  getNivel()   { return nivel; }
    public Nave getNave()    { return nave; }

    public void crearEnemigos() {
        enemigos.clear();
        direccionEnemigos = 1;
        for (int fila = 0; fila < 2; fila++) {
            for (int col = 0; col < MAX_COL; col++) {
                int px     = 1 + col * 3;
                int py     = 1 + fila * 2;
                int puntos = (2 - fila) * 10;
                enemigos.add(new Enemigo(px, py, puntos));
            }
        }
    }

    public void dibujarTablero() {
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

        for (Enemigo en : enemigos)
            if (en.isActivo() && dentro(en.getX(), en.getY()))
                tablero[en.getY()][en.getX()] = 'W';

        for (Disparo d : disparosJugador)
            if (d.isActivo() && dentro(d.getX(), d.getY()))
                tablero[d.getY()][d.getX()] = '|';

        for (Disparo d : disparosEnemigos)
            if (d.isActivo() && dentro(d.getX(), d.getY()))
                tablero[d.getY()][d.getX()] = '!';

        if (nave.getX() >= 0 && nave.getX() < ANCHO)
            tablero[ALTO - 1][nave.getX()] = 'A';

        System.out.println("+" + "-".repeat(ANCHO) + "+");
        for (int f = 0; f < ALTO; f++) {
            System.out.print("|");
            for (int c = 0; c < ANCHO; c++)
                System.out.print(tablero[f][c]);
            System.out.println("|");
        }
        System.out.println("+" + "-".repeat(ANCHO) + "+");
        System.out.println();
        System.out.println("a=izquierda  d=derecha  w=disparar  q=salir");
        System.out.print("Tu movimiento: ");
    }

    private boolean dentro(int x, int y) {
        return x >= 0 && x < ANCHO && y >= 0 && y < ALTO;
    }

    public void moverEnemigos() {
        boolean borde = false;
        for (Enemigo en : enemigos) {
            if (en.isActivo()) {
                if (direccionEnemigos ==  1 && en.getX() >= ANCHO - 2) { borde = true; break; }
                if (direccionEnemigos == -1 && en.getX() <= 1)          { borde = true; break; }
            }
        }
        if (borde) {
            for (Enemigo en : enemigos)
                if (en.isActivo()) en.setY(en.getY() + 1);
            direccionEnemigos *= -1;
        } else {
            for (Enemigo en : enemigos)
                if (en.isActivo()) en.setX(en.getX() + direccionEnemigos);
        }
    }

    public void disparoEnemigo() {
        ArrayList<Enemigo> activos = new ArrayList<>();
        for (Enemigo en : enemigos)
            if (en.isActivo()) activos.add(en);
        if (!activos.isEmpty()) {
            int idx = (int)(Math.random() * activos.size());
            disparosEnemigos.add(activos.get(idx).atacar());
        }
    }

    public void moverDisparos() {
        for (Disparo d : disparosJugador)
            if (d.isActivo()) { d.mover(); if (d.getY() < 0) d.setActivo(false); }
        ArrayList<Disparo> nj = new ArrayList<>();
        for (Disparo d : disparosJugador) if (d.isActivo()) nj.add(d);
        disparosJugador = nj;

        for (Disparo d : disparosEnemigos)
            if (d.isActivo()) { d.mover(); if (d.getY() >= ALTO) d.setActivo(false); }
        ArrayList<Disparo> ne = new ArrayList<>();
        for (Disparo d : disparosEnemigos) if (d.isActivo()) ne.add(d);
        disparosEnemigos = ne;
    }

    public void verificarColisiones() {
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
        for (Disparo d : disparosEnemigos) {
            if (!d.isActivo()) continue;
            if (d.getX() == nave.getX() && d.getY() == ALTO - 1) {
                d.setActivo(false);
                nave.setVidas(nave.getVidas() - 1);
                if (nave.getVidas() <= 0) corriendo = false;
            }
        }
        for (Enemigo en : enemigos) {
            if (en.isActivo() && en.getY() >= ALTO - 1) {
                nave.setVidas(0);
                corriendo = false;
            }
        }
    }

    public boolean todosEliminados() {
        for (Enemigo en : enemigos)
            if (en.isActivo()) return false;
        return true;
    }

    public void jugar() {
        System.out.println("\n── POLIMORFISMO EN ACCIÓN ──");
        ArrayList<Personaje> personajes = new ArrayList<>();
        personajes.add(nave);
        personajes.add(new Enemigo(0, 0, 10));
        for (Personaje p : personajes) {
            Disparo d = p.atacar();
            if (d != null)
                System.out.println(p.getClass().getSimpleName()
                        + " dispara direccion: " + d.getDireccion());
        }
        System.out.println("Nave=-1 (arriba)  |  Enemigo=+1 (abajo)");
        System.out.println("────────────────────────────");
        System.out.println("Presiona Enter para jugar...");
        sc.nextLine();

        int turno = 0;
        while (corriendo) {
            dibujarTablero();
            String entrada = sc.nextLine().trim().toLowerCase();

            switch (entrada) {
                case "q": corriendo = false; break;
                case "a": nave.moverIzquierda(); break;
                case "d": nave.moverDerecha();   break;
                case "w": disparosJugador.add(nave.atacar()); break;
            }

            if (!corriendo) break;

            moverDisparos();
            if (turno % 2 == 0) moverEnemigos();
            if (turno % 3 == 0) disparoEnemigo();
            verificarColisiones();

            if (todosEliminados()) {
                nivel++;
                System.out.println("\n¡Nivel " + (nivel-1) + " completado! → Nivel " + nivel);
                System.out.println("Presiona Enter...");
                sc.nextLine();
                crearEnemigos();
            }
            turno++;
        }

        System.out.println("\n=========================");
        System.out.println(nave.getVidas() <= 0
                ? "   GAME OVER, " + nave.getNombreJugador() + "!"
                : "   ¡VICTORIA, " + nave.getNombreJugador() + "!");
        System.out.println("   Puntaje: " + puntaje + "  |  Nivel: " + nivel);
        System.out.println("=========================");
    }
}