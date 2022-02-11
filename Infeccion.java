import java.io.Serializable;

public class Infeccion implements Serializable {
    
    String nombre;
    int danyo;
    //public static final int serialVersioUID=2;

    public Infeccion(String nombre, int danyo){
        this.nombre=nombre;
        this.danyo=danyo;
    }
}
