class Nave extends Personaje {
    private String nombreJugador;
    private int ancho;

    Nave(String nombreJugador, int ancho) {
        super(9, 0, 3);
        this.nombreJugador = nombreJugador;
        this.ancho = ancho;
    }

    // Getters / Setters propios
    public String getNombreJugador() { return nombreJugador; }
    public int getAncho()            { return ancho; }

    public void moverIzquierda() {
        if (getX() > 0) setX(getX() - 1);
    }

    public void moverDerecha() {
        if (getX() < ancho - 1) setX(getX() + 1);
    }

    // Polimorfismo: dispara hacia ARRIBA
    @Override
    public Disparo atacar() {
        return new Disparo(getX(), Juego.ALTO - 1, -1);
    }
}
