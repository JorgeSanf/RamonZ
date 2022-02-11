import java.io.Serializable;

public class Virus extends Thread implements Serializable  {

    private final String nombre;
    private final int danyo;
    private boolean vacunada = false;
    private final int descanso;
    private static boolean primera = true;
    private static int vida = 1;
    private static int vidaOrig = 1;
    //public static final int serialVersioUID=1;


    public Virus (String nombre, int danyo){
        this.nombre = nombre;
        this.danyo = danyo;
        this.descanso=Virus.setSleep(nombre);
    }

    private void estaVacunado(boolean vacunado){
        this.vacunada=vacunado;
    }

    public int getSleep(){
        return this.descanso;
    }

    public static int setSleep(String nombre){
        int suenyo;

        if (nombre.equalsIgnoreCase("Omicron")){
            suenyo = 333;
        } else {
            suenyo = 1000;
        }
        return suenyo;
    }

    public String getNombre(){
        return this.nombre;
    }

    public static int getVida(){
        return vida;
    }

    public static int getVidaOrig(){
        return vidaOrig;
    }

    public void run(){

        if(vida>0) {
            Contagio contagio = new Contagio(nombre, danyo);
            contagio.establecerConexion();
            estaVacunado(contagio.vacunacion());

            if (vacunada) {
                System.out.println(nombre + ": Vacunada");
                contagio.cerrarConexion();
            } else {
                vida = contagio.infeccion();
                System.out.println("Infección de " + nombre);

                if(primera){
                    vidaOrig=vida+danyo;
                    primera=false;
                }
            }
        }
    }

}
