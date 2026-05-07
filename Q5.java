import java.util.concurrent.Semaphore;

class BarbeariaSegura {
    private int cadeirasEspera;
    private int esperando = 0;
    
    private Semaphore clientes = new Semaphore(0); 
    private Semaphore barbeiros = new Semaphore(0); 
    private Semaphore mutex = new Semaphore(1); 

    public BarbeariaSegura(int cadeiras) {
        this.cadeirasEspera = cadeiras;
    }

    public void clienteChega(int id) throws InterruptedException {
        mutex.acquire(); // entra na região crítica 
        if (esperando < cadeirasEspera) {
            esperando++;
            System.out.println("Cliente " + id + " chegou e sentou na sala de espera. (Esperando: " + esperando + ")");
            clientes.release(); // acorda o barbeiro se necessário 
            mutex.release(); 
            barbeiros.acquire(); // vai dormir se o número de barbeiros livres for 0 
            System.out.println("Cliente " + id + " está cortando o cabelo.");
        } else {
            mutex.release(); // a barbearia está cheia; não espere 
            System.out.println("Cliente " + id + " foi embora pois a barbearia estava cheia.");
        }
    }

    public void barbeiroTrabalha() throws InterruptedException {
        try{
            while (true) {
                clientes.acquire(); // vai dormir se o número de clientes for 0 
                mutex.acquire(); // obtém acesso a 'waiting' (esperando) 
                esperando--; // decresce de um o contador de clientes à espera 
                System.out.println("Barbeiro chamou o próximo cliente. (Esperando: " + esperando + ")");
                barbeiros.release(); // um barbeiro está agora pronto para cortar cabelo 
                mutex.release(); // libera 'waiting' (esperando) 
                
                // Corta o cabelo (fora da região crítica) 
                Thread.sleep(1000); // Simulando o tempo do corte
            }
        } catch (InterruptedException e) {
            System.out.println("Expediente encerrado!");
        }
    }
}

public class Q5 {
    public static void main(String[] args) {
        BarbeariaSegura barbearia = new BarbeariaSegura(3);

        Thread barbeiro = new Thread(() -> {
            System.out.println("Barbeiro abriu a loja e está esperando clientes...");
            try {
                barbearia.barbeiroTrabalha();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Barbeiro foi interrompido.");
            }
        });
        barbeiro.start();

        int totalClientes = 6;
        Thread[] threadsClientes = new Thread[totalClientes];

        // Cria e inicia os clientes
        for (int i = 0; i < totalClientes; i++) {
            final int id = i + 1;
            threadsClientes[i] = new Thread(() -> {
                try {
                    Thread.sleep((long) (Math.random() * 2000));
                    barbearia.clienteChega(id);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Cliente " + id + " foi interrompido.");
                }
            });
            threadsClientes[i].start();
        }

        // A thread main aguarda pacientemente a execução de todos os clientes acabar
        for (int i = 0; i < totalClientes; i++) {
            try {
                threadsClientes[i].join(); // Espera a thread deste cliente morrer
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\nNão há mais clientes previstos para hoje.");
        barbeiro.interrupt(); // Envia o sinal para interromper o barbeiro
    }
}