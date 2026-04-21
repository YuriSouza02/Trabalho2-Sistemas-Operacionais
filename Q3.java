
import java.util.concurrent.locks.ReentrantReadWriteLock;

// ============================================================================
// CLASSE SEGURA
// ============================================================================
class BdSeguro {

    // A nossa variável partilhada
    private int dado = 0;

    // Este é o "fiscal". Ele consegue distinguir quem quer apenas LER e quem quer ESCREVER.
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    // ------------------- AÇÃO DO LEITOR -------------------
    public void ler(int idLeitor) {
        // Vários leitores podem ter este cadeado ao mesmo tempo
        lock.readLock().lock();
        try {
            System.out.println("[Seguro] Leitor " + idLeitor + " ENTROU e está lendo o dado: " + dado);

            // Finge que o leitor demora 100 milissegundos a ler tudo o que está no quadro
            Thread.sleep(100);

            System.out.println("[Seguro] Leitor " + idLeitor + " SAIU.");
        } catch (InterruptedException e) {
        } finally {
            // É OBRIGATÓRIO devolver o cadeado no "finally" para garantir que, 
            // mesmo que dê erro, a porta não fica trancada para sempre.
            lock.readLock().unlock();
        }
    }

    // ------------------- AÇÃO DO ESCRITOR -------------------
    public void escrever(int idEscritor, int novoDado) {
        // Pega no cadeado de ESCRITA. Este cadeado é EXCLUSIVO. Se o Escritor o pegar, 
        // TODOS os leitores e outros escritores ficam bloqueados do lado de fora à espera!
        lock.writeLock().lock();
        try {
            System.out.println("[Seguro] Escritor " + idEscritor + " ENTROU (Bloqueou tudo!) e vai escrever: " + novoDado);

            dado = novoDado; // Escreve a nova informação no quadro

            // Finge que ele demora 150 milissegundos a escrever
            Thread.sleep(150);

            System.out.println("[Seguro] Escritor " + idEscritor + " SAIU e libertou o acesso.");
        } catch (InterruptedException e) {
        } finally {
            // Devolve o cadeado exclusivo, permitindo que todos volte a ler
            lock.writeLock().unlock();
        }
    }
}

// ============================================================================
// CLASSE INSEGURA (SEM CADEADO NENHUM - PORTA ABERTA)
// ============================================================================
class BdInseguro {

    private int dado = 0;

    // Como não há bloqueio, o Leitor entra a qualquer momento.
    public void ler(int idLeitor) {
        System.out.println("[INSEGURO] Leitor " + idLeitor + " ENTROU e leu o dado: " + dado);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        System.out.println("[INSEGURO] Leitor " + idLeitor + " SAIU.");
    }

    // O Escritor também entra sem controle. Se outro Escritor estiver lá, 
    // eles vão atropelar-se e esmagar a informação um do outro.
    public void escrever(int idEscritor, int novoDado) {
        System.out.println("[INSEGURO] Escritor " + idEscritor + " ENTROU e vai escrever: " + novoDado);
        dado = novoDado;
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
        }
        System.out.println("[INSEGURO] Escritor " + idEscritor + " SAIU.");
    }
}

// ============================================================================
// CLASSE PRINCIPAL
// ============================================================================
public class Q3 {

    static void testeSeguro() {
        System.out.println("\n--------------------------- TESTE SEGURO ---------------------------");
        BdSeguro bd = new BdSeguro(); // Instancia o "quadro" protegido
        iniciarThreads(bd, null);
    }

    static void testeInseguro() {
        System.out.println("\n-------------------------- TESTE INSEGURO --------------------------");
        BdInseguro bd = new BdInseguro(); // Instancia o "quadro" desprotegido
        iniciarThreads(null, bd);
    }

    // Método responsável por contratar os nossos Leitores e Escritores (Threads) 
    // e mandá-los fazer o trabalho ao mesmo tempo.
    static void iniciarThreads(BdSeguro bdSeguro, BdInseguro bdInseguro) {

        // ---------------- 3 LEITORES ----------------
        Thread[] leitores = new Thread[3];
        for (int i = 0; i < 3; i++) {
            final int id = i + 1;

            // Receita de trabalho do Leitor: Vai ler 2 vezes seguidas
            leitores[i] = new Thread(() -> {
                for (int j = 0; j < 2; j++) {
                    if (bdSeguro != null) {
                        bdSeguro.ler(id);
                    } else {
                        bdInseguro.ler(id);
                    }

                    // Pausa um pouco antes de tentar ler novamente
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                    }
                }
            });
        }

        // ---------------- 2 ESCRITORES ----------------
        Thread[] escritores = new Thread[2];
        for (int i = 0; i < 2; i++) {
            final int id = i + 1;

            // Receita de trabalho do Escritor: Vai escrever 2 vezes seguidas
            escritores[i] = new Thread(() -> {
                for (int j = 0; j < 2; j++) {
                    int valorAleatorio = (int) (Math.random() * 100); // Cria um número para escrever

                    if (bdSeguro != null) {
                        bdSeguro.escrever(id, valorAleatorio);
                    } else {
                        bdInseguro.escrever(id, valorAleatorio);
                    }

                    // Pausa um pouco antes de tentar escrever novamente
                    try {
                        Thread.sleep(70);
                    } catch (InterruptedException e) {
                    }
                }
            });
        }

        // ---------------- DANDO O SINAL DE PARTIDA ----------------
        // Manda os Leitores e os Escritores começarem todos AGORA.
        for (Thread l : leitores) {
            l.start();
        }
        for (Thread e : escritores) {
            e.start();
        }

        // ---------------- ESPERANDO TODOS TERMINAREM ----------------
        // O programa principal "senta e espera" até que o último trabalhador termine
        try {
            for (Thread l : leitores) {
                l.join();
            }
            for (Thread e : escritores) {
                e.join();
            }
        } catch (InterruptedException e) {
        }
    }

    public static void main(String[] args) {
        // 1. Roda a versão perfeita com Cadeados Inteligentes
        testeSeguro();

        // Dá uma pausa de 1 segundo para não misturar os textos no ecrã (terminal)
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        // 2. Roda a versão defeituosa para observarmos as threads a atropelarem-se
        testeInseguro();
    }
}
