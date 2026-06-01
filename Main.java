import java.util.Scanner;

// ===== MAIN =====

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== SPACE INVADERS ===");
        System.out.print("Ingresa tu nombre: ");
        String nombre = sc.nextLine();

        Juego juego = new Juego(nombre);
        juego.jugar();
    }
}