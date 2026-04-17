import java.util.concurrent.Semaphore;

class Buffer {
    private int[] buffer;
    private int in = 0, out = 0;
    private Semaphore mutex = new Semaphore(1); // Exclusão mútua
    private Semaphore vazios; // Vagas disponíveis
    private Semaphore cheios = new Semaphore(0); // Itens produzidos

    public Buffer(int tamanho) {
        buffer = new int[tamanho];
        vazios = new Semaphore(tamanho);
    }

    public void produzir(int item) throws InterruptedException {
        vazios.acquire(); // Espera ter vaga
        mutex.acquire(); // Protege a seção crítica
        buffer[in] = item;
        in = (in + 1) % buffer.length;
        mutex.release();
        cheios.release(); // Sinaliza que há um novo item
    }

    public int consumir() throws InterruptedException {
        cheios.acquire(); // Espera ter item
        mutex.acquire();
        int item = buffer[out];
        out = (out + 1) % buffer.length;
        mutex.release();
        vazios.release(); // Libera uma vaga
        return item;
    }
}

public class Q2 {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
