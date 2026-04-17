class Ponte {
    private int carrosSentidoA = 0;
    private int carrosSentidoB = 0;

    public synchronized void entraCarroA() throws InterruptedException {
        while (carrosSentidoB > 0) {
            wait(); // Espera se houver carros no sentido oposto
        }
        carrosSentidoA++;
    }

    public synchronized void saiCarroA() {
        carrosSentidoA--;
        if (carrosSentidoA == 0) notifyAll(); // Libera a ponte
    }

    public synchronized void entraCarroB() throws InterruptedException {
        while (carrosSentidoA > 0) {
            wait();
        }
        carrosSentidoB++;
    }

    public synchronized void saiCarroB() {
        carrosSentidoB--;
        if (carrosSentidoB == 0) notifyAll();
    }
}

public class Q6 {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}