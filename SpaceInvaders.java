import java.util.Scanner;

/**
 * CLASE PRINCIPAL — SpaceInvaders (Main)
 *
 * Punto de entrada del programa.
 * Solo pide el nombre del jugador y lanza el juego.
 *
 * Para compilar y ejecutar desde la carpeta src/:
 *   javac *.java
 *   java SpaceInvaders
 */
public class SpaceInvaders {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("╔══════════════════════════╗");
        System.out.println("║     SPACE  INVADERS      ║");
        System.out.println("║   ITM — POO en Java      ║");
        System.out.println("╚══════════════════════════╝");
        System.out.println();
        System.out.print("Ingresa tu nombre: ");
        String nombre = sc.nextLine().trim();

        if (nombre.isEmpty()) nombre = "Jugador";

        // Crear el juego y comenzar
        Juego juego = new Juego(nombre);
        juego.jugar();

        sc.close();
    }
}
