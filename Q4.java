import java.util.concurrent.Semaphore;

class Filosofo extends Thread {
    private int id;
    private Semaphore garfoEsquerdo, garfoDireito;

    public Filosofo(int id, Semaphore esquerdo, Semaphore direito) {
        this.id = id;
        this.garfoEsquerdo = esquerdo;
        this.garfoDireito = direito;
    }

    public void run() {
        try {
            while (true) {
                // Pensa...
                if (id % 2 == 0) {
                    garfoEsquerdo.acquire();
                    garfoDireito.acquire();
                } else {
                    garfoDireito.acquire();
                    garfoEsquerdo.acquire();
                }
                // Come...
                garfoEsquerdo.release();
                garfoDireito.release();
            }
        } catch (InterruptedException e) {
        }
    }
}

public class Q4 {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
