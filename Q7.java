import java.util.concurrent.Semaphore;

class Estacionamento {
    private Semaphore vagas;

    public Estacionamento(int totalVagas) {
        vagas = new Semaphore(totalVagas); // Limite de vagas
    }

    public void entrar() throws InterruptedException {
        vagas.acquire(); // Ocupa uma vaga, bloqueia se estiver cheio
        System.out.println("Carro entrou. Vagas restantes: " + vagas.availablePermits());
    }

    public void sair() {
        vagas.release(); // Libera a vaga
        System.out.println("Carro saiu. Vagas restantes: " + vagas.availablePermits());
    }
}

public class Q7 {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}