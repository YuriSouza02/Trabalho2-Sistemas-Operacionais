class ContadorSeguro {
    private int valor = 0;

    // O monitor (synchronized) garante que apenas uma thread acesse por vez
    public synchronized void incrementar() {
        valor++;
    }

    public synchronized void decrementar() {
        valor--;
    }

    public int getValor() {
        return valor;
    }
}

class ContadorInseguro {
    private int valor = 0;

    // O monitor (synchronized) garante que apenas uma thread acesse por vez
    public void incrementar() {
        valor++;
    }

    public void decrementar() {
        valor--;
    }

    public int getValor() {
        return valor;
    }
}

public class Q1 {
    static void contadorSeguro() {
        ContadorSeguro contador = new ContadorSeguro();

        Runnable Experimento = () -> {
            for (int i = 0; i < 10000; i++) {
                contador.incrementar();
            }
        };

        Thread Tredi1 = new Thread(Experimento);
        Thread Tredi2 = new Thread(Experimento);

        // 3: Dispara as duas ao MESMO TEMPO
        Tredi1.start();
        Tredi2.start();

        try {
            // Espera as duas terminarem
            Tredi1.join();
            Tredi2.join();
        } catch (InterruptedException e) {
            System.out.println("Erro ao esperar as threads: " + e.getMessage());
        }
        System.out.println("Seguro   -> Valor final (esperado 20000): " + contador.getValor());
    }

    static void contadorInseguro() throws InterruptedException {
        ContadorInseguro contador = new ContadorInseguro();

        Runnable Experimento = () -> {
            for (int i = 0; i < 10000; i++) {
                contador.incrementar();
            }
        };

        Thread Tredi1 = new Thread(Experimento);
        Thread Tredi2 = new Thread(Experimento);

        // 3: Dispara as duas ao MESMO TEMPO
        Tredi1.start();
        Tredi2.start();

        try {
            // Espera as duas terminarem
            Tredi1.join();
            Tredi2.join();
        } catch (InterruptedException e) {
            System.out.println("Erro ao esperar as threads: " + e.getMessage());
        }

        System.out.println("Inseguro -> Valor final (esperado 20000): " + contador.getValor());
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("--- INICIANDO TESTES ---");
        contadorSeguro();
        contadorInseguro();
    }
}