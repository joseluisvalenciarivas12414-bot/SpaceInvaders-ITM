import java.util.Scanner;

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

        Juego juego = new Juego(nombre);
        juego.jugar();

        sc.close();
    }
}
