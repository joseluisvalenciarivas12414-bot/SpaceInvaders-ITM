
public class Personaje {

    
    private int x;          
    private int y;          
    private int vidas;     
    private boolean activo;

   
    public Personaje(int x, int y, int vidas) {
        this.x      = x;
        this.y      = y;
        this.vidas  = vidas;
        this.activo = true;   
    }

   
    public int     getX()      { return x; }
    public int     getY()      { return y; }
    public int     getVidas()  { return vidas; }
    public boolean isActivo()  { return activo; }

    
    public void setX(int x)           { this.x      = x; }
    public void setY(int y)           { this.y      = y; }
    public void setVidas(int vidas)   { this.vidas  = vidas; }
    public void setActivo(boolean a)  { this.activo = a; }

  
    public Disparo atacar() {
        return null; 
    }

    
    @Override
    public String toString() {
        return "Personaje[x=" + x + ", y=" + y + ", vidas=" + vidas + ", activo=" + activo + "]";
    }
}
