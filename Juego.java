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
                int px     = 1 + col * 3;         
                int py     = 1 + fila * 2;         
                int puntos = (filas - fila) * 10;  
                enemigos.add(new Enemigo(px, py, puntos));
            }
        }
    }

    
    void dibujarTablero() {
        // Limpia la pantalla (ANSI escape)
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }

        
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
           
            for (Enemigo en : enemigos)
                if (en.isActivo()) en.setY(en.getY() + 1);
            direccionEnemigos *= -1;
        } else {
           
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

   
    boolean todosEliminados() {
        for (Enemigo en : enemigos)
            if (en.isActivo()) return false;
        return true;
    }

   
    void jugar() {
       
        System.out.println("\n── DEMOSTRACIÓN DE POLIMORFISMO ──");
        ArrayList<Personaje> personajes = new ArrayList<>();
        personajes.add(nave);                    
        personajes.add(new Enemigo(0, 0, 10));   
        personajes.add(new Enemigo(5, 2, 20));   

        for (Personaje p : personajes) {         
            Disparo d = p.atacar();
            if (d != null)
                System.out.println(p.getClass().getSimpleName()
                        + " ataca → " + d);     
        }
        System.out.println("──────────────────────────────────\n");
        System.out.println("Presiona Enter para comenzar...");
        sc.nextLine();
        
        int turno = 0;

        while (corriendo) {
            dibujarTablero();
            String entrada = sc.nextLine().trim().toLowerCase();

           
            switch (entrada) {
                case "q": corriendo = false; break;
                case "a": nave.moverIzquierda(); break;
                case "d": nave.moverDerecha();   break;
                case "w":
                   
                    disparosJugador.add(nave.atacar());
                    break;
                default:
                   
                    break;
            }

            if (!corriendo) break;

            
            moverDisparos();
            if (turno % 2 == 0) moverEnemigos();   
            if (turno % 3 == 0) disparoEnemigo();   
            verificarColisiones();

            
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
            System.out.println("   ¡GRACIAS POR JUGAR, "  + nave.getNombreJugador() + "!");
        System.out.println("   Puntaje final : " + puntaje);
        System.out.println("   Nivel alcanzado: " + nivel);
        System.out.println("=========================");
    }
}
