class Enemigo extends Personaje {
    private int puntos;

    Enemigo(int x, int y, int puntos) {
        super(x, y, 1);
        this.puntos = puntos;
    }

    // Getter / Setter propio
    public int getPuntos()       { return puntos; }
    public void setPuntos(int p) { this.puntos = p; }

    // Polimorfismo: dispara hacia ABAJO
    @Override
    public Disparo atacar() {
        return new Disparo(getX(), getY() + 1, 1);
    }
}