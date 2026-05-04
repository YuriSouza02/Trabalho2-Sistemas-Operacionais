import java.util.ArrayList;
import java.util.List;

class PonteInadequada {
    // Esta ponte permite inanição
    private int carrosSentidoA = 0;
    private int carrosSentidoB = 0;

    public synchronized void entraCarroA() throws InterruptedException {
        while (carrosSentidoB > 0) {
            wait(); // Espera se houver carros no sentido oposto
        }
        carrosSentidoA++;
        System.out.println("Carro A entrou.");
    }

    public synchronized void saiCarroA() {
        carrosSentidoA--;
        if (carrosSentidoA == 0)
            notifyAll(); // Libera a ponte
        System.out.println("Carro A saiu.");
    }

    public synchronized void entraCarroB() throws InterruptedException {
        while (carrosSentidoA > 0) {
            wait(); // Espera se houver carros no sentido oposto
        }
        carrosSentidoB++;
        System.out.println("Carro B entrou.");
    }

    public synchronized void saiCarroB() {
        carrosSentidoB--;
        if (carrosSentidoB == 0)
            notifyAll(); // Libera a ponte
        System.out.println("Carro B saiu.");
    }
}

class PonteAdequada {
    // Esta ponte não permite inanição, pois, limita a quantidade de carros que podem passar em uma direção por vez
    private int carrosSentidoA = 0;
    private int carrosSentidoB = 0;

    // Contadores para impedir um fluxo unidirecional
    private int passaramA = 0;
    private int passaramB = 0;
    private final int LIMITE_CONSECUTIVO = 3;

    public synchronized void entraCarroA() throws InterruptedException {
        while (carrosSentidoB > 0 || passaramA >= LIMITE_CONSECUTIVO) {
            wait(); // Espera se houver carros no sentido oposto ou se já passou o limite de carros desse sentido
        }
        carrosSentidoA++;
        passaramA++;
        passaramB = 0;
        System.out.println("Carro A entrou. (Total A na ponte = " + carrosSentidoA + ")");
    }

    public synchronized void saiCarroA() {
        carrosSentidoA--;
        System.out.println("Carro A saiu. (Total A na ponte: " + carrosSentidoA + ")");

        if (carrosSentidoA == 0 || passaramA >= LIMITE_CONSECUTIVO) // Libera a ponte
            notifyAll();
    }

    public synchronized void entraCarroB() throws InterruptedException {
        while (carrosSentidoA > 0 || passaramB >= LIMITE_CONSECUTIVO) {
            wait();
        }
        carrosSentidoB++;
        passaramB++;
        passaramA = 0;
        System.out.println("Carro B entrou. (Total B na ponte = " + carrosSentidoB + ")");
    }

    public synchronized void saiCarroB() {
        carrosSentidoB--;
        System.out.println("Carro B saiu. (Total B na ponte: " + carrosSentidoB + ")");

        if (carrosSentidoB == 0 || passaramB >= LIMITE_CONSECUTIVO)
            notifyAll();
    }
}

public class Q6 {
    static void usaPonteInadequada() throws InterruptedException {
        PonteInadequada ponte = new PonteInadequada();
        List<Thread> threads = new ArrayList<>();

        // Lançando 10 carros de A e 10 de B simultaneamente.
        for (int i = 1; i <= 10; i++) {
            Thread ta = new Thread(() -> {
                try {
                    ponte.entraCarroA();
                    Thread.sleep(200);
                    ponte.saiCarroA();
                } catch (InterruptedException e) { }
            });

            Thread tb = new Thread(() -> {
                try {
                    ponte.entraCarroB();
                    Thread.sleep(200);
                    ponte.saiCarroB();
                } catch (InterruptedException e) { }
            });

            ta.start();
            tb.start();
            threads.add(ta);
            threads.add(tb);
        }

        // Aguarda todas as threads deste teste terminarem antes de prosseguir
        for (Thread t : threads) {
            t.join();
        }
    }

    static void usaPonteAdequada() throws InterruptedException {
        PonteAdequada ponte = new PonteAdequada();
        List<Thread> threads = new ArrayList<>();

        // Lançando 10 carros de A e 10 de B simultaneamente.
        for (int i = 1; i <= 10; i++) {
            Thread ta = new Thread(() -> {
                try {
                    ponte.entraCarroA();
                    Thread.sleep(200);
                    ponte.saiCarroA();
                } catch (InterruptedException e) { }
            });

            Thread tb = new Thread(() -> {
                try {
                    ponte.entraCarroB();
                    Thread.sleep(200);
                    ponte.saiCarroB();
                } catch (InterruptedException e) { }
            });

            ta.start();
            tb.start();
            threads.add(ta);
            threads.add(tb);
        }

        // Aguarda todas as threads deste teste terminarem
        for (Thread t : threads) {
            t.join();
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println("--- INICIANDO TESTES ---");

            System.out.println("\n--- PONTE INADEQUADA ---");
            usaPonteInadequada();

            System.out.println("\n--- PONTE ADEQUADA ---");
            usaPonteAdequada();

            System.out.println("\n--- TESTES FINALIZADOS ---");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}