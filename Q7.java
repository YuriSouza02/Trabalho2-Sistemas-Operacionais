import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

class EstacionamentoInadequado {
    private int vagas;

    public EstacionamentoInadequado(int vagas) {
        this.vagas = vagas;
    }

    public void entrar() throws InterruptedException {
        if(this.vagas > 0) {
            this.vagas--;
            synchronized (this) {
                System.out.println("Carro entrou. Vagas restantes: " + this.vagas);
            }
        } else {
            synchronized (this) {
                System.out.println("Carro tentou entrar, mas está CHEIO!");
            }
        }
    }

    public void sair() {
        this.vagas++;
        synchronized (this) {
            System.out.println("Carro saiu. Vagas restantes: " + this.vagas);
        }
    }
}

class EstacionamentoAdequado {
    private Semaphore vagas;

    public EstacionamentoAdequado(int totalVagas) {
        this.vagas = new Semaphore(totalVagas); // Limite de this.vagas
    }

    public void entrar() throws InterruptedException {
        this.vagas.acquire(); // Ocupa uma vaga, bloqueia se estiver cheio
        synchronized (this) {
            System.out.println("Carro entrou. Vagas restantes: " + this.vagas.availablePermits());
        }
    }

    public void sair() {
        this.vagas.release(); // Libera a vaga
        synchronized (this) {
            System.out.println("Carro saiu. Vagas restantes: " + this.vagas.availablePermits());
        }
    }
}

public class Q7 {

    static void testaEstacionamentoInadequado() throws InterruptedException {
        EstacionamentoInadequado estacionamento = new EstacionamentoInadequado(2);
        List<Thread> threads = new ArrayList<>();

        for(int i = 0; i < 5; i++) {
            Thread t = new Thread(() -> {
               try {
                   estacionamento.entrar();
                   Thread.sleep(200); // simula tempo em que o carro fica estacionado
                    estacionamento.sair();
               } catch (InterruptedException e) { }
            });
            t.start();
            threads.add(t);
        }

        // Esperar acabar
        for(Thread t : threads) {
            t.join();
        }
    }

    static void testaEstacionamentoAdequado() throws InterruptedException {
        EstacionamentoAdequado estacionamento = new EstacionamentoAdequado(2);
        List<Thread> threads = new ArrayList<>();

        for(int i = 0; i < 5; i++) {
            Thread t = new Thread(() -> {
                try {
                    estacionamento.entrar();
                    Thread.sleep(200); // simula tempo em que o carro fica estacionado
                    estacionamento.sair();
                } catch (InterruptedException e) { }
            });
            t.start();
            threads.add(t);
        }

        // Esperar acabar
        for(Thread t : threads) {
            t.join();
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println("--- INICIANDO TESTES ---");

            System.out.println("\n--- ESTACIONAMENTO INADEQUADO ---");
            testaEstacionamentoInadequado();

            System.out.println("\n--- ESTACIONAMENTO ADEQUADO ---");
            testaEstacionamentoAdequado();

            System.out.println("\n--- TESTES FINALIZADOS ---");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}