import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class Abastos extends Thread implements Serializable {

    private static final Object lockOmicron = new Object(), lockDelta = new Object(),
            lockEbola = new Object(), lockMarburg = new Object();
    private static String mensajeFinal = "";

    public static void main(String[] args) throws InterruptedException {

        ArrayList<Virus> listaVirus = Abastos.menuVirus();
        CountDownLatch latch = new CountDownLatch(listaVirus.size());

        for (Virus virus : listaVirus) {
            Object lock = switch (virus.getNombre()) {
                case "Omicron" -> lockOmicron;
                case "Delta" -> lockDelta;
                case "Ebola" -> lockEbola;
                case "Marburg" -> lockMarburg;
                default -> null;
            };
            new Aerosol(virus, lock, latch).start();
        }

        latch.await();
        Abastos.estaVivo();
        System.out.println("\n" + mensajeFinal);
    }

    protected static void estaVivo() {
        int vida = Virus.getVida();

        if (vida > 0) {
            if (vida < Virus.getVidaOrig() / 10) {
                mensajeFinal = "Persona zombie";
            } else {
                mensajeFinal = "Resistencia restante: " + vida;
            }
        } else {
            mensajeFinal = "Persona muerta";
        }
    }

    private static ArrayList<Virus> menuVirus() {

        ArrayList<Virus> viruses = new ArrayList<>();
        int delta, omicron, ebola, marburg;

        Scanner entrada = new Scanner(System.in);
        System.out.println("Elige la cantidad de virus de cada tipo");

        System.out.println("Delta");
        delta = entrada.nextInt();
        System.out.println("Omicron");
        omicron = entrada.nextInt();
        System.out.println("Ebola");
        ebola = entrada.nextInt();
        System.out.println("Marburg");
        marburg = entrada.nextInt();

        entrada.close();

        for (int i = 0; i < delta; i++) {
            viruses.add(new Virus("Delta", 30));
        }
        for (int i = 0; i < omicron; i++) {
            viruses.add(new Virus("Omicron", 10));
        }
        for (int i = 0; i < ebola; i++) {
            viruses.add(new Virus("Ebola", 40));
        }
        for (int i = 0; i < marburg; i++) {
            viruses.add(new Virus("Marburg", 60));
        }

        Collections.shuffle(viruses);
        return viruses;
    }
}

class Aerosol extends Thread implements Serializable {

    private final Virus virus;
    private final Object lock;
    private final CountDownLatch latch;

    public Aerosol(Virus virus, Object lock, CountDownLatch latch) {
        this.virus = virus;
        this.lock = lock;
        this.latch = latch;
    }

    public void run() {

        try {
            synchronized (lock) {
                if (Virus.getVida() > 0) {
                    virus.start();
                    Thread.sleep(virus.getSleep());
                }
                latch.countDown();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
