// ============================================================================
// CLASSE SEGURA (com synchronized)
// ============================================================================

class ContadorSeguro {

    // A nossa variável compartilhada.
    private int valor = 0;

    // A palavra-chave "synchronized" age para controlar a região.
    public synchronized void incrementar() {
        valor++;
    }

    // O synchronized também se aplica aqui.
    public synchronized void decrementar() {
        valor--;
    }

    // Apenas lê o valor atual do placar. Não precisa de tanto bloqueio só para espreitar o número.
    public int getValor() {
        return valor;
    }
}

// ============================================================================
// CLASSE INSEGURA (sem synchronized)
// ============================================================================
class ContadorInseguro {

    // A variável compartilhada.
    private int valor = 0;

    // Aqui não tem o synchronized.
    public void incrementar() {
        valor++;
    }

    // Aqui também não tem o synchronized.
    public void decrementar() {
        valor--;
    }

    // Apenas lê o valor atual do placar.
    public int getValor() {
        return valor;
    }
}

// ============================================================================
// CLASSE PRINCIPAL
// ============================================================================
public class Q1 {

    // ---------------------------------------------------------
    // TESTE 1: TESTE SEGURO
    // ---------------------------------------------------------
    static void contadorSeguro() {
        ContadorSeguro contador = new ContadorSeguro();

        // Apertar o botão de somar 10.000 vezes seguidas o mais rápido que puder.
        Runnable experimento = () -> {
            for (int i = 0; i < 10000; i++) {
                contador.incrementar();
            }
        };

        // Contratamos dois trabalhadores (Threads) e damos a mesma tarefa aos dois.
        Thread t1 = new Thread(experimento); // Trabalhador 1
        Thread t2 = new Thread(experimento); // Trabalhador 2

        // Começam a trabalhar EXATAMENTE ao mesmo tempo
        t1.start();
        t2.start();

        // O bloco "join()" faz o nosso programa principal esperar tudo terminar.
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Erro ao aguardar threads: " + e.getMessage());
        }

        // O resultado será SEMPRE 20.000 cravado.
        System.out.println("Seguro   -> Valor final (esperado 20000): " + contador.getValor());
    }

    // ---------------------------------------------------------
    // TESTE 2: TESTE INSEGURO
    // ---------------------------------------------------------
    static void contadorInseguro() {
        ContadorInseguro contador = new ContadorInseguro();

        // A mesma tarefa: somar 10.000 vezes.
        Runnable experimento = () -> {
            for (int i = 0; i < 10000; i++) {
                contador.incrementar();
            }
        };

        // As mesmas 2 threads.
        Thread t1 = new Thread(experimento);
        Thread t2 = new Thread(experimento);

        // Começam a trabalhar ao mesmo tempo.
        t1.start();
        t2.start();

        // Esperamos as duas terminarem.
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Erro ao aguardar threads: " + e.getMessage());
        }

        // O valor QUASE NUNCA será 20.000.
        System.out.println("Inseguro -> Valor final (esperado 20000): " + contador.getValor());
    }

    public static void main(String[] args) {
        System.out.println("--- INICIANDO TESTES ---");
        contadorSeguro();
        contadorInseguro();
    }
}
