package com.company;

public class Professor extends Thread {
    // Criamos uma identificação para o professor
    private int ID;

    // Criamos padrões de comportamento para o professor
    final int PREPARANDOAULA = 0;
    final int FAMINTO = 1;
    final int COMENDO = 2;

    // Método construtor que recebe um nome para a classe e um código de
    // identificação do professor
    public Professor (String nome, int ID)
    {
        super(nome);
        this.ID = ID;
    }

    // Método para definir que o professor está com fome
    public void ComFome ()
    {
        // Seta o estado deste professor na classe Grade para FAMINTO
        Grade.estado[this.ID] = 1;
        // Exibe uma mensagem de controle na tela
        System.out.println("O professor " + getName() + " está FAMINTO!");
    }

    // Método para definir que o professor está comendo
    public void Come ()
    {
        // Seta o estado deste professor na classe Grade para COMENDO
        Grade.estado[this.ID] = 2;
        // Exibe uma mensagem de controle na tela
        System.out.println("O professor " + getName() + " está COMENDO!");

        // Será criado um controle para o professor permanecer comendo
        // durante certo período de tempo
        try
        {
            // Fica parado neste estado por 1000 milisegundos
            Thread.sleep(1000L);
        }
        catch (InterruptedException ex)
        {
            // Exibe uma mensagem de controle de erro
            System.out.println("ERROR>" + ex.getMessage());
        }
    }

    // Método para definir que o professor está preparando aula
    public void PreparaAula ()
    {
        // Seta o estado deste professor na classe Grade para PREPARANDOAULA
        Grade.estado[this.ID] = 0;
        // Exibe uma mensagem de controle na tela
        System.out.println("O professor " + getName() + " está PREPARANDO AULA!");

        // Será criado um controle para o professor permanecer preparando aula
        // durante certo período de tempo
        try
        {
            // Fica parado neste estado por 1000 milisegundos
            Thread.sleep(1000L);
        }
        catch (InterruptedException ex)
        {
            // Exibe uma mensagem de controle de erro
            System.out.println("ERROR>" + ex.getMessage());
        }
    }

    // Método para o professor soltar um garfo que ele pegou
    public void LargarGarfo ()
    {
        // Decrementa o semáforo mutex principal da classe, isso permite
        // informar que o atual método está operando na mesa dos professor
        Grade.mutex.decrementar();

        // Coloca o professor para preparar aula determinado tempo
        PreparaAula();

        // Após o professor preparar aula, ele vai informar para os seus vizinhos
        // que podem tentar pegar os garfos que já estão disponíveis
        Grade.professor[VizinhoEsquerda()].TentarObterGarfos();
        Grade.professor[VizinhoDireita()].TentarObterGarfos();

        // Após operar, volta o semáforo mutex para o estado normal
        // indicando que já realizou todos procedimentos na mesa
        Grade.mutex.incrementar();
    }

    // Método para o professor pegar um garfo na mesa
    public void PegarGarfo ()
    {
        // Decrementa o semáforo mutex principal da classe, isso permite
        // informar que o atual método está operando na mesa dos professores
        Grade.mutex.decrementar();

        // Deixa o professor faminto por determinado tempo
        ComFome();

        // Após o professor o período de fome, ele vai verificar com seus
        // vizinhos se ele pode pegar os garfos
        TentarObterGarfos();

        // Após operar, volta o semáforo mutex para o estado normal
        // indicando que já realizou todos procedimentos na mesa
        Grade.mutex.incrementar();
        // Decrementa seu semáforo
        Grade.semaforos[this.ID].decrementar();
    }

    // Método para verificar se o professor pode pegar um garfo disposto na mesa
    public void TentarObterGarfos()
    {

        // Verifica se este professor está com fome, e se o vizinho da esquerda
        // e da direita não estão comendo
        if (Grade.estado[this.ID] == 1 &&
                Grade.estado[VizinhoEsquerda()] != 2 &&
                Grade.estado[VizinhoDireita()] != 2)
        {
            // Então este professor pode comer
            Come();
            // E incrementa o seu semáforo
            Grade.semaforos[this.ID].incrementar();
        }

    }

    // Método de execução da classe, onde o ambiente do professor será rodado
    @Override
    public void run ()
    {

        try
        {
            // Coloca o professor para preparar aula
            PreparaAula();

            // Então realiza uma vida infinita para o professor onde inicialmente
            // ele executa os procedimentos de pergar os garfos da mesa, posteriormente
            // ele descansa um pouco, e por fim, ele largar os garfos que ele pegou
            do
            {
                PegarGarfo();
                Thread.sleep(1000L);
                LargarGarfo();
            }
            while (true);
        }
        catch (InterruptedException ex)
        {
            // Exibe uma mensagem de controle de erro
            System.out.println("ERROR>" + ex.getMessage());
            // E da um retorno de cancelamento
            return;
        }

    }

    // Método para obter o professor vizinho da direita
    public int VizinhoDireita ()
    {
        // Rationa o valor em 5 posições, ou seja, se o ID deste professor acrescentado
        // de um for maior que quatro, passa a ser zero
        return (this.ID + 1) % 5;
    }

    // Método para obter o professor vizinho da esquerda
    public int VizinhoEsquerda ()
    {
        if (this.ID == 0)
        {
            // Retorna a ultima posição
            return 4;
        }
        else
        {
            // Rationa o valor em 5 posições, ou seja, se o ID deste professor decrescido
            // de um for menor que zero, passa a ser quatro
            return (this.ID - 1) % 5;
        }
    }
}
