import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

class ImpressoraInadequada {
    public void imprimir(String documento) {
        try {
            System.out.println("Imprimindo: " + documento);
            Thread.sleep(1000); // Simulando o tempo de impressão
            System.out.println("Impressão Concluída.");
        } catch (InterruptedException e) {}
    }
}

class ImpressoraAdequada {
    // Parâmetro 'true' garante que a impressora atenda quem aguarda a mais tempo (preservando a ordem de chegada).
    private ReentrantLock lock = new ReentrantLock(true);

    public void imprimir(String documento) {
        // Bloqueia a impressora. Se alguém estiver usando, entra na fila de espera.
        lock.lock();
        try {
            System.out.println("Imprimindo: " + documento);
            Thread.sleep(1000); // Simulando o tempo de impressão
            System.out.println("Impressão Concluída.");
        } catch (InterruptedException e) {
        } finally {
            // Libera a impressora
            lock.unlock();
        }
    }
}

public class Q8 {

    static void testaInadequada() throws InterruptedException {
        ImpressoraInadequada impressora = new ImpressoraInadequada();
        List<Thread> threads = new ArrayList<>();

        for(int i = 0; i < 3; i++) {
            String nomeDoc = "Documento " + i;
            Thread t = new Thread(() -> {
                impressora.imprimir(nomeDoc);
            });
            t.start();
            threads.add(t);
        }

        for(Thread t : threads)
            t.join();
    }

    static void testaAdequada() throws InterruptedException {
        ImpressoraAdequada impressora = new ImpressoraAdequada();
        List<Thread> threads = new ArrayList<>();

        for(int i = 0; i < 3; i++) {
            String nomeDoc = "Documento " + i;
            Thread t = new Thread(() -> {
                impressora.imprimir(nomeDoc);
            });
            t.start();
            threads.add(t);
        }

        for(Thread t : threads)
            t.join();
    }

    public static void main(String[] args) {
        try {
            System.out.println("--- INICIANDO TESTES ---");

            System.out.println("\n--- IMPRESSORA INADEQUADA ---");
            testaInadequada();

            System.out.println("\n--- IMPRESSORA ADEQUADA ---");
            testaAdequada();

            System.out.println("\n--- TESTES FINALIZADOS ---");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}