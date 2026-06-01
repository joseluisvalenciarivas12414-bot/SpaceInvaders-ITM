class Personaje {
    private int x;
    private int y;
    private int vidas;
    private boolean activo;

    Personaje(int x, int y, int vidas) {
        this.x = x;
        this.y = y;
        this.vidas = vidas;
        this.activo = true;
    }

    // Getters
    public int getX()         { return x; }
    public int getY()         { return y; }
    public int getVidas()     { return vidas; }
    public boolean isActivo() { return activo; }

    // Setters
    public void setX(int x)          { this.x = x; }
    public void setY(int y)          { this.y = y; }
    public void setVidas(int vidas)  { this.vidas = vidas; }
    public void setActivo(boolean a) { this.activo = a; }

    // Método base que cada subclase sobreescribe (Polimorfismo)
    public Disparo atacar() {
        return null;
    }
}
