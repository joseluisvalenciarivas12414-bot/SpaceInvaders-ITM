public class Enemigo extends Personaje {

    private int puntos;

    public Enemigo(int x, int y, int puntos) {
        super(x, y, 1);
        this.puntos = puntos;
    }

    public int  getPuntos()      { return puntos; }
    public void setPuntos(int p) { this.puntos = p; }

    @Override
    public Disparo atacar() {
        return new Disparo(getX(), getY() + 1, 1);
    }
}