import java.util.concurrent.locks.ReentrantLock;

class Impressora {
    // Parâmetro 'true' garante que a impressora atenda na ordem de chegada (FIFO)
    private ReentrantLock lock = new ReentrantLock(true);

    public void imprimir(String documento) {
        lock.lock(); // Exclusividade do dispositivo
        try {
            System.out.println("Imprimindo: " + documento);
            Thread.sleep(1000); // Simulando o tempo de impressão
        } catch (InterruptedException e) {
        } finally {
            lock.unlock(); // Libera a impressora
        }
    }
}

public class Q8 {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}