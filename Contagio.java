import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class Contagio extends Thread implements Serializable {

    static final String HOST = "localhost";
    static final int PORT = 5000;
    Socket socketClient;
    private ObjectInputStream inStream = null;
    private ObjectOutputStream outStream = null;

    private final String nombre;
    private final int danyo;

    public Contagio(String nombre, int danyo) {
        this.nombre = nombre;
        this.danyo = danyo;
    }

    public void establecerConexion() {
        try {
            socketClient = new Socket(HOST, PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cerrarConexion(){
        try{
            inStream.close();
            outStream.close();
            socketClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean vacunacion() {

        boolean vacunacion = false;
        try {
            outStream = new ObjectOutputStream(socketClient.getOutputStream());
            outStream.writeObject(nombre);

            inStream = new ObjectInputStream(socketClient.getInputStream());
            vacunacion = (boolean) inStream.readObject();

        } catch (IOException | ClassNotFoundException ceio){
            System.out.println(ceio);
        }
        return vacunacion;
    }

    public int infeccion(){

        Infeccion infeccion = new Infeccion(nombre, danyo);
        int vida=1;
        try {
            outStream = new ObjectOutputStream(socketClient.getOutputStream());
            outStream.writeObject(infeccion);

            inStream = new ObjectInputStream(socketClient.getInputStream());
            vida = (int) inStream.readObject();

        } catch (IOException | ClassNotFoundException ceio){
            System.out.println(ceio);
        }
        return vida;
    }
}

