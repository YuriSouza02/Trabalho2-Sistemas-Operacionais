
import java.util.concurrent.Semaphore;

// ============================================================================
// CLASSE SEGURA (COM SINCRONIZAÇÃO)
// ============================================================================
class BufferSeguro {

    private final int[] buffer; // Onde guardamos os itens
    private int in = 0;         // Aponta para a posição onde o Produtor vai guardar o próximo item
    private int out = 0;        // Aponta para a posição de onde o Consumidor vai tirar o próximo item

    // 1. Mutex (Semaforo): Garante que apenas 1 pessoa mexa no buffer por vez.
    private final Semaphore mutex = new Semaphore(1);

    // 2. Vazios: Conta quantas vagas existem no buffer.
    private final Semaphore vazios;

    // 3. Cheios: Conta quantos "itens" estão prontos para serem pegos.
    private final Semaphore cheios = new Semaphore(0);

    public BufferSeguro(int tamanho) {
        buffer = new int[tamanho];       // Cria o buffer com o tamanho definido
        vazios = new Semaphore(tamanho); // No início, todas as posições são "vagas vazias"
    }

    // Função auxiliar apenas para desenhar o vetor na tela e facilitar o entendimento visual
    private void imprimirEstado(String acao) {
        System.out.print(String.format("%-38s | Buffer: [", acao));
        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i] == 0) {
                System.out.print(" _ ");
            } else {
                System.out.print(String.format("%2d ", buffer[i]));
            }
        }
        System.out.println("]");
    }

    // ------------------- AÇÃO DO PRODUTOR -------------------
    public void produzir(int item) throws InterruptedException {
        // PASSO 1: Tenta produzir. Se não tiver (buffer cheio), ele dorme aqui esperando.
        vazios.acquire();

        // PASSO 2: Gerencia a zona critica
        mutex.acquire();

        // PASSO 3: Secção Crítica
        buffer[in] = item; // Guarda o item no vetor
        imprimirEstado("[Seguro] Produtor guardou " + item + " na pos " + in);
        in = (in + 1) % buffer.length;

        // PASSO 4: libera a zona crítica.
        mutex.release();

        // PASSO 5: Avisa que há um item cheio para os consumidores.
        cheios.release();
    }

    // ------------------- AÇÃO DO CONSUMIDOR -------------------
    public int consumir() throws InterruptedException {
        // PASSO 1: Tenta pegar um item cheio. Se não tiver nada (buffer vazio), ele dorme aqui.
        cheios.acquire();

        // PASSO 2: Tranca o cadeado.
        mutex.acquire();

        // PASSO 3: Secção Crítica (O trabalho em si)
        int item = buffer[out]; // Pega o item do vetor
        buffer[out] = 0;        // Substitui por 0 apenas para o nosso "print" mostrar que ficou vazio visualmente
        imprimirEstado("[Seguro] Consumidor pegou " + item + " da pos " + out);
        out = (out + 1) % buffer.length; // Anda com o ponteiro para a frente (circularmente)

        // PASSO 4: Destranca o cadeado.
        mutex.release();

        // PASSO 5: Avisa aos produtores: "Olha, acabei de libertar uma vaga vazia!"
        vazios.release();

        return item;
    }
}

// ============================================================================
// CLASSE INSEGURA (SEM SINCRONIZAÇÃO NENHUMA)
// ============================================================================
class BufferInseguro {

    private final int[] buffer;
    private int in = 0, out = 0;

    public BufferInseguro(int tamanho) {
        buffer = new int[tamanho];
    }

    private void imprimirEstado(String acao) {
        System.out.print(String.format("%-38s | Buffer: [", acao));
        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i] == 0) {
                System.out.print(" _ ");
            } else {
                System.out.print(String.format("%2d ", buffer[i]));
            }
        }
        System.out.println("]");
    }

    // Como não há semáforos, os produtores atropelam-se uns aos outros e sobrescrevem dados!
    public void produzir(int item) throws InterruptedException {
        buffer[in] = item;
        imprimirEstado("[INSEGURO] Produtor guardou " + item + " na pos " + in);
        in = (in + 1) % buffer.length;
    }

    // Como não há semáforos, os consumidores podem tentar ler posições que ainda estão vazias!
    public int consumir() throws InterruptedException {
        int item = buffer[out];
        buffer[out] = 0;
        imprimirEstado("[INSEGURO] Consumidor pegou " + item + " da pos " + out);
        out = (out + 1) % buffer.length;
        return item;
    }
}

// ============================================================================
// CLASSE PRINCIPAL (ONDE O PROGRAMA RODA)
// ============================================================================
public class Q2 {

    static void bufferSeguro() {
        System.out.println("\n--------------------------- TESTE SEGURO ---------------------------");
        BufferSeguro buffer = new BufferSeguro(5); // Cria um buffer com 5 espaços

        // Define a tarefa (receita) do Produtor: Gerar 5 números
        Runnable produtor = () -> {
            for (int i = 1; i <= 5; i++) {
                try {
                    buffer.produzir(i);
                    // O Math.random() faz a thread pausar por um tempo aleatório, simulando lentidão real do sistema
                    Thread.sleep((int) (Math.random() * 50));
                } catch (InterruptedException e) {
                }
            }
        };

        // Define a tarefa (receita) do Consumidor: Consumir 5 números
        Runnable consumidor = () -> {
            for (int i = 1; i <= 5; i++) {
                try {
                    buffer.consumir();
                    Thread.sleep((int) (Math.random() * 80));
                } catch (InterruptedException e) {
                }
            }
        };

        iniciarThreads(produtor, consumidor);
    }

    static void bufferInseguro() {
        System.out.println("\n-------------------------- TESTE INSEGURO --------------------------");
        BufferInseguro buffer = new BufferInseguro(5);

        // A mesma tarefa, mas agora interagindo com o buffer que não tem cadeados
        Runnable produtor = () -> {
            for (int i = 1; i <= 5; i++) {
                try {
                    buffer.produzir(i);
                    Thread.sleep((int) (Math.random() * 50));
                } catch (InterruptedException e) {
                }
            }
        };

        Runnable consumidor = () -> {
            for (int i = 1; i <= 5; i++) {
                try {
                    buffer.consumir();
                    Thread.sleep((int) (Math.random() * 80));
                } catch (InterruptedException e) {
                }
            }
        };

        iniciarThreads(produtor, consumidor);
    }

    // Método responsável por criar os "trabalhadores" (Threads) e mandá-los trabalhar ao mesmo tempo
    static void iniciarThreads(Runnable tarefaProdutor, Runnable tarefaConsumidor) {
        // Criamos 2 produtores e 2 consumidores
        Thread tP1 = new Thread(tarefaProdutor);
        Thread tP2 = new Thread(tarefaProdutor);
        Thread tC1 = new Thread(tarefaConsumidor);
        Thread tC2 = new Thread(tarefaConsumidor);

        // O comando .start() faz todos correrem juntos
        tP1.start();
        tP2.start();
        tC1.start();
        tC2.start();

        // O bloco .join() faz esperar todos terminarem antes de continuar
        try {
            tP1.join();
            tP2.join();
            tC1.join();
            tC2.join();
        } catch (InterruptedException e) {
        }
    }

    public static void main(String[] args) {
        // 1. Roda o teste que funciona corretamente
        bufferSeguro();

        // 2. Dá uma pausa de 1 segundo para não misturar os textos na tela
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        // 3. Roda o teste defeituoso para vermos os erros a acontecerem
        bufferInseguro();
    }
}
