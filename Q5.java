import java.util.concurrent.Semaphore;

class Barbearia {
    private int cadeirasEspera;
    private int esperando = 0;
    private Semaphore clientes = new Semaphore(0);
    private Semaphore barbeiros = new Semaphore(0);
    private Semaphore mutex = new Semaphore(1);

    public Barbearia(int cadeiras) {
        this.cadeirasEspera = cadeiras;
    }

    public void clienteChega() throws InterruptedException {
        mutex.acquire();
        if (esperando < cadeirasEspera) {
            esperando++;
            clientes.release(); // Acorda o barbeiro
            mutex.release();
            barbeiros.acquire(); // Espera ser atendido
        } else {
            mutex.release(); // Vai embora (sem cadeiras)
        }
    }

    public void barbeiroTrabalha() throws InterruptedException {
        while (true) {
            clientes.acquire(); // Dorme se não há clientes
            mutex.acquire();
            esperando--;
            barbeiros.release(); // Chama o cliente
            mutex.release();
            // Corta o cabelo...
        }
    }
}

public class Q5 {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
