
public class Disparo {

    
    private int     x;          
    private int     y;         
    private int     direccion;  
    private boolean activo;     

    
    public Disparo(int x, int y, int direccion) {
        this.x         = x;
        this.y         = y;
        this.direccion = direccion;
        this.activo    = true; // Nace activo
    }

    
    public int     getX()         { return x; }
    public int     getY()         { return y; }
    public int     getDireccion() { return direccion; }
    public boolean isActivo()     { return activo; }

  
    public void setX(int x)           { this.x      = x; }
    public void setY(int y)           { this.y      = y; }
    public void setActivo(boolean a)  { this.activo = a; }

   
    public void mover() {
        this.y += this.direccion; // Suma -1 (sube) o +1 (baja)
    }

    
    public String toString() {
        String dir = (direccion == -1) ? "ARRIBA (jugador)" : "ABAJO (enemigo)";
        return "Disparo[x=" + x + ", y=" + y + ", dir=" + dir + ", activo=" + activo + "]";
    }
}
