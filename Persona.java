import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Persona extends Thread {

    protected static final String HOST = "localhost";
    protected static final int PORT = 5000;
    private static int resistencia;
    private static final boolean[] vacunas = new boolean[4];
    public static int talla = 0;

    public static void main(String[] arg) {

        Persona.setVacunas();
        Persona.setResistencia();

        try {
            ServerSocket socketServidor = new ServerSocket(PORT);
            System.out.println("Escoltant al port: " + PORT);

            while (Persona.getResistencia() > 0) {
                Socket socketCli = socketServidor.accept();
                talla++;
                ServidorClient sct = new ServidorClient(socketCli);
                sct.start();
                sct.join();
            }

            System.out.println("\nLa persona ha muerto");
            sleep(talla);
            socketServidor.close();
            System.exit(0);

        } catch (Exception e) {
            System.out.println("1" + e);
        }

    }

    private static void setResistencia() {
        resistencia = (int) Math.round(Math.random() * 1999 + 1);
        System.out.println("Resistencia: " + resistencia + "\n");
    }

    protected static int getResistencia() {

        return resistencia;
    }

    protected static synchronized int alterarResistencia(int num) {

        resistencia -= num;
        if (resistencia < 0) {
            resistencia = 0;
        }
        System.out.println("Resistencia: " + resistencia);
        return resistencia;
    }

    public static void setVacunas() {

        int dosFalsos;
        Random random = new Random();

        do {
            dosFalsos = 0;
            for (int i = 0; i < vacunas.length; i++) {
                vacunas[i] = random.nextBoolean();
            }

            for (boolean vacuna : vacunas) {
                if (vacuna) {
                    dosFalsos++;
                }
            }

        } while (dosFalsos != 2);
    }

    public static boolean getVacunado(String nombre) {

        boolean vacunado = false;

        switch (nombre) {
            case "Delta":
                if (vacunas[0]) {
                    vacunado = true;
                }
                break;
            case "Omicron":
                if (vacunas[1]) {
                    vacunado = true;
                }
                break;
            case "Ebola":
                if (vacunas[2]) {
                    vacunado = true;
                }
                break;
            case "Marburg":
                if (vacunas[3]) {
                    vacunado = true;
                }
                break;
        }

        return vacunado;
    }
}

class ServidorClient extends Thread {

    Socket serverClient;
    private ObjectInputStream inStream = null;
    private ObjectOutputStream outStream = null;

    ServidorClient(Socket serverClient) {
        this.serverClient = serverClient;
    }

    public void run() {

        if (!vacunacion()) {
            infeccion();
        }
        scCerrar();
    }

    private boolean vacunacion() {

        boolean vacunado = false;
        try {
            inStream = new ObjectInputStream(serverClient.getInputStream());
            String nombre = (String) inStream.readObject();

            vacunado = Persona.getVacunado(nombre);

            outStream = new ObjectOutputStream(serverClient.getOutputStream());
            outStream.writeObject(vacunado);

        } catch (IOException | ClassNotFoundException ceio) {
            System.out.println(ceio);
        }

        return vacunado;
    }

    private void infeccion() {

        boolean vivo;
        int vida = Persona.getResistencia();
        vivo = vida > 0;

        try {
            inStream = new ObjectInputStream(serverClient.getInputStream());
            Infeccion infeccion = (Infeccion) inStream.readObject();

            if (vivo) {
                vida = Persona.alterarResistencia(infeccion.danyo);
            }

            outStream = new ObjectOutputStream(serverClient.getOutputStream());
            outStream.writeObject(vida);

        } catch (IOException | ClassNotFoundException ceio) {
            System.out.println(ceio);
        }

    }

    private void scCerrar() {

        try {
            inStream.close();
            outStream.close();
            serverClient.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}