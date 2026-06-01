class Disparo {
    private int x;
    private int y;
    private int direccion; // -1 = sube (jugador), 1 = baja (enemigo)
    private boolean activo;

    Disparo(int x, int y, int direccion) {
        this.x = x;
        this.y = y;
        this.direccion = direccion;
        this.activo = true;
    }

    // Getters
    public int getX()         { return x; }
    public int getY()         { return y; }
    public int getDireccion() { return direccion; }
    public boolean isActivo() { return activo; }

    // Setters
    public void setX(int x)          { this.x = x; }
    public void setY(int y)          { this.y = y; }
    public void setActivo(boolean a) { this.activo = a; }

    public void mover() {
        this.y += this.direccion;
    }
}