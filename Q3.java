import java.util.concurrent.locks.ReentrantReadWriteLock;

class BancoDeDados {
    private int dado = 0;
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public void ler() {
        lock.readLock().lock(); // Bloqueio de leitura (compartilhado)
        try {
            System.out.println("Lendo dado: " + dado);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void escrever(int novoDado) {
        lock.writeLock().lock(); // Bloqueio de escrita (exclusivo)
        try {
            dado = novoDado;
            System.out.println("Escrevendo dado: " + dado);
        } finally {
            lock.writeLock().unlock();
        }
    }
}

public class Q3 {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
