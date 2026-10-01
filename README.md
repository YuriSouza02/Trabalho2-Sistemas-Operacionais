# Exercícios de Programação Concorrente e Sincronização em Java

Este repositório contém um conjunto de exemplos práticos em Java que abordam **programação concorrente**, **exclusão mútua**, **gerenciamento de regiões críticas** e os **problemas clássicos de sincronização de sistemas operacionais**.

Cada questão compara ou demonstra implementações seguras (*thread-safe*) e inseguras para evidenciar o surgimento e a resolução de problemas como **condições de corrida** (*race conditions*), **deadlock** (impasse) e **starvation** (inanição).

---

## 🛠️ Tecnologias e Mecanismos Utilizados

* **Linguagem:** Java (JDK 8 ou superior)
* **APIs de Concorrência (`java.util.concurrent`):**
  * `synchronized` / `wait()` / `notifyAll()` — Sincronização intrínseca e monitores.
  * `Semaphore` — Semáforos para contagem de recursos e exclusão mútua.
  * `ReentrantReadWriteLock` — Bloqueio de leitura/escrita para alta concorrência de leitura.
  * `ReentrantLock(boolean fair)` — Trava reentrante configurada com política de equidade (*Fair Lock*).

---

## 📁 Estrutura do Projeto

| Arquivo | Problema Abordado | Mecanismo de Sincronização | Conceitos Demonstrados |
| :--- | :--- | :--- | :--- |
| **`Q1.java`** | Incremetador / Contador Concorrente | Bloco/Método `synchronized` | Região crítica e Condição de Corrida |
| **`Q2.java`** | Produtor - Consumidor (Buffer Limitado) | `Semaphore` (*mutex*, *vazios*, *cheios*) | Buffer circular e bloqueio condicional |
| **`Q3.java`** | Leitores e Escritores | `ReentrantReadWriteLock` | Leituras simultâneas vs. Escrita exclusiva |
| **`Q4.java`** | Jantar dos Filósofos | `Semaphore` | Prevenção de *Deadlock* (assimetria na posse de garfos) |
| **`Q5.java`** | Barbeiro Adormecido (*Sleeping Barber*) | `Semaphore` e `mutex` | Capacidade limitada de espera e acordar/dormir threads |
| **`Q6.java`** | Travessia em Ponte de Via Única | `synchronized`, `wait()`, `notifyAll()` | Prevenção de *Starvation* por limite de carros consecutivos |
| **`Q7.java`** | Controle de Vagas em Estacionamento | `Semaphore` | Gerenciamento de pool de recursos limitados |
| **`Q8.java`** | Impressora Concorrente (*Spooler*) | `ReentrantLock(true)` | Fila FIFO justa (*Fairness*) para evitar inanição |

---

## ⚙️ Detalhamento dos Exercícios

### 1️⃣ Q1 — Contador Concorrente
* **Objetivo:** Demonstra a ocorrência de condições de corrida em operações não atômicas (como `valor++`).
* **Implementação:**
  * **Insegura:** Duas threads incrementam um contador 10.000 vezes sem sincronização, resultando em perdas de atualizações e valores imprevisíveis.
  * **Segura:** Utiliza `synchronized` para garantir a atomicidade das operações de incremento e decremento.

---

### 2️⃣ Q2 — Produtor / Consumidor com Buffer Circular
* **Objetivo:** Resolver o problema do buffer limitado entre threads produtoras e consumidoras.
* **Implementação:**
  * Utiliza 3 semáforos: `mutex` (exclusão mútua), `vazios` (quantidade de posições livres) e `cheios` (quantidade de dados disponíveis).
  * O consumidor adormece se o buffer estiver vazio; o produtor adormece se o buffer estiver cheio.

---

### 3️⃣ Q3 — Leitores e Escritores
* **Objetivo:** Permitir que múltiplos leitores acessem um recurso simultaneamente, exigindo exclusão mútua total apenas para escrita.
* **Implementação:**
  * Utiliza `ReentrantReadWriteLock`.
  * `readLock()` permite múltiplos acessos concorrentes.
  * `writeLock()` bloqueia leitores e outros escritores enquanto a alteração ocorre.

---

### 4️⃣ Q4 — O Jantar dos Filósofos
* **Objetivo:** Evitar *deadlock* e garantir que todos os filósofos consigam comer.
* **Implementação:**
  * **Insegura:** Todos os filósofos tentam pegar primeiro o garfo esquerdo e depois o direito, o que gera um impasse fatal se todos pegarem o esquerdo ao mesmo tempo.
  * **Segura:** Quebra a simetria na alocação de recursos (filósofos pares pegam primeiro a esquerda; ímpares pegam primeiro a direita).

---

### 5️⃣ Q5 — O Barbeiro Adormecido
* **Objetivo:** Modelar um sistema com sala de espera finita e um trabalhador que adormece quando não há demanda.
* **Implementação:**
  * Utiliza semáforos para coordenar clientes na sala de espera, notificação do barbeiro e bloqueio quando as cadeiras se esgotam.

---

### 6️⃣ Q6 — Ponte de Via Única (Prevenção de Starvation)
* **Objetivo:** Controlar o tráfego em uma ponte onde carros passam em apenas um sentido por vez.
* **Implementação:**
  * **Inadequada:** Permite o fluxo contínuo de um sentido, podendo causar inanição (*starvation*) nos veículos do sentido oposto.
  * **Adequada:** Define um limite de carros consecutivos (`LIMITE_CONSECUTIVO`) permitidos em uma mesma direção antes de inverter a preferência.

---

### 7️⃣ Q7 — Estacionamento Concorrente
* **Objetivo:** Controlar a entrada e saída de veículos em um estacionamento com vagas limitadas.
* **Implementação:**
  * Utiliza um `Semaphore` onde o número de permissões é igual ao total de vagas ativas. Se o estacionamento estiver cheio, os novos veículos aguardam na fila até a liberação.

---

### 8️⃣ Q8 — Impressora Concorrente e Fair Lock
* **Objetivo:** Garantir a ordem justa de atendimento às solicitações de impressão.
* **Implementação:**
  * Utiliza `ReentrantLock(true)`. A propriedade de *fairness* garante que as threads sejam atendidas rigorosamente na ordem de chegada (FIFO), evitando que uma thread fique indefinidamente sem imprimir.

---

## 🚀 Como Compilar e Executar

### Pré-requisitos
* Java Development Kit (JDK) 8 ou superior instalado.

### 1. Compilar uma questão específica
No terminal, navegue até a pasta raiz do projeto e execute:

```bash
javac Q1.java
```

*(Substitua `Q1.java` pelo arquivo desejado, como `Q2.java`, `Q3.java`, etc.)*

### 2. Executar a questão
Após a compilação, execute a classe principal:

```bash
java Q1
```

---

## 📊 Tabela de Comandos Rápidos

```bash
# Executar a demonstração do Contador (Q1)
javac Q1.java && java Q1

# Executar a demonstração do Produtor/Consumidor (Q2)
javac Q2.java && java Q2

# Executar a demonstração do Jantar dos Filósofos (Q4)
javac Q4.java && java Q4

# Executar a demonstração do Barbeiro Adormecido (Q5)
javac Q5.java && java Q5
```
