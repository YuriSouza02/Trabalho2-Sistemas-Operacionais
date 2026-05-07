import java.util.concurrent.Semaphore;

class JantarInseguro extends Thread{
    private int id;
    private Semaphore garfoEsquerdo, garfoDireito;

    public JantarInseguro(int id, Semaphore esquerdo, Semaphore direito) {
        this.id = id;
        this.garfoEsquerdo = esquerdo;
        this.garfoDireito = direito;
    }

    public void run() {
        try {
            int refeicoes = 0;
            int limiteRefeicoes = 5;

            while (refeicoes < limiteRefeicoes) {
                System.out.println("Filósofo " + id + " está pensando.");
                garfoEsquerdo.acquire(); // Todos pegam a esquerda
                garfoDireito.acquire();  // Todos tentam a direita e bloqueiam
                
                System.out.println("Filósofo " + id + " está comendo.");
                
                garfoEsquerdo.release();
                garfoDireito.release();
            }
        } catch (InterruptedException e) { }
    }
}

class JantarSeguro extends Thread{
    private int id;
    private Semaphore garfoEsquerdo, garfoDireito;

    public JantarSeguro(int id, Semaphore esquerdo, Semaphore direito) {
        this.id = id;
        this.garfoEsquerdo = esquerdo;
        this.garfoDireito = direito;
    }

    public void run() {
        try {
            int refeicoes = 0;
            int limiteRefeicoes = 10;

            while (refeicoes < limiteRefeicoes) {
                System.out.println("Filósofo " + id + " está pensando.");
                Thread.sleep((long) (Math.random() * 1000)); 
                
                if (id % 2 == 0) {
                    garfoEsquerdo.acquire();
                    garfoDireito.acquire();
                } else {
                    garfoDireito.acquire();
                    garfoEsquerdo.acquire();
                }
                
                System.out.println("Filósofo " + id + " está COMENDO (Refeição " + (refeicoes + 1) + ").");
                Thread.sleep((long) (Math.random() * 1000)); 
                
                garfoEsquerdo.release();
                garfoDireito.release();
                
                refeicoes++;
            }
            System.out.println(">>> Filósofo " + id + " terminou de jantar e saiu da mesa.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class Q4 {
    public static void main(String[] args) {
        int numeroDeFilosofos = 5;
        Semaphore[] garfos = new Semaphore[numeroDeFilosofos];
        JantarSeguro[] filosofos = new JantarSeguro[numeroDeFilosofos];
        
        //Inicializa os semáforos
        //Cada semáforo tem 1 permissão, representando a Exclusão Mútua do recurso
        for (int i = 0; i < numeroDeFilosofos; i++) {
            garfos[i] = new Semaphore(1);
        }

        // System.out.println("Execução jantar inseguro: ");
        // for (int i = 0; i < numeroDeFilosofos; i++) {
        //     Semaphore garfoEsquerdo = garfos[i];
        //     Semaphore garfoDireito = garfos[(i + 1) % numeroDeFilosofos];

        //     JantarInseguro j = new JantarInseguro(i, garfoEsquerdo, garfoDireito);
        //     j.start();
        // }

        System.out.println("Execução jantar seguro: ");
        for (int i = 0; i < numeroDeFilosofos; i++) {
            Semaphore garfoEsquerdo = garfos[i];
            Semaphore garfoDireito = garfos[(i + 1) % numeroDeFilosofos];

            filosofos[i] = new JantarSeguro(i, garfoEsquerdo, garfoDireito);
            filosofos[i].start();
        }
    }
}