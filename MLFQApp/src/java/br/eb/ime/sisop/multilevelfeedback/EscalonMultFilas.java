/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.eb.ime.sisop.multilevelfeedback;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

/**
 *
 * @author maiarab
 */
public class EscalonMultFilas {
    
    public static final int duracaoIO = 20;
    public static final int quantumFila1 = 10;
    public static final int quantumFila2 = 20;
    
    public static int quantum2restante = quantumFila2;
    
    private int finalIO = 0;
    private int finalCPU = 0;
    
    private boolean terminado = false;
    private boolean ultimoAdd = false;
    private boolean IOociosa = true;
    
    private Queue<Processo> fila1;
    private Queue<Processo> fila2;
    private Queue<Processo> fila3;
    private Queue<Processo> filaIO;
    private ArrayList<NoCPU> usoCPU;
    private ArrayList<NoIO> usoIO;
    private Queue<Processo> procsTerminados;
    
    public List<NoCPU> getUsoCPU(){
        return usoCPU;
    }
    
    public List<NoIO> getUsoIO(){
        return usoIO;
    }
    
    //estrutura que sera armazenada pela fila de uso da CPU
    public class NoCPU{
        public Processo proc;
        public int finalProc;
        
        public int getFinalProc(){
            return finalProc;
        }
        public Processo getProc(){
            return proc;
        }
        
    }
    
    //estrutura que sera armazenada pela fila de uso do IO
    public class NoIO{
        public Processo proc;
        public int inicioProc;
        
        public int getInicioProc(){
            return inicioProc;
        }
        
        public Processo getProc(){
            return proc;
        }
        
    }
    
    public EscalonMultFilas(List<Processo> procs){
        usoCPU = new ArrayList<>();
        usoIO = new ArrayList<>();

        filaIO = new LinkedList<>();
        fila1 = new LinkedList<>();
        fila2 = new LinkedList<>();
        fila3 = new LinkedList<>();
        
        procsTerminados = new LinkedList<>();
        
        //Alocando os processos a fila 1
        if(procs != null){
            for(Processo p : procs){
                ((LinkedList)fila1).addLast(p);
            }
        }
    }
    
    public void escalonaIO(){
        if(filaIO.isEmpty()){
            if(!usoIO.isEmpty() && !ultimoAdd){
                NoIO ultimo = usoIO.get(usoIO.size()-1);
                Processo pSaindo = ultimo.proc;
                pSaindo.decrementaIO();
                pSaindo.resetSurtoRestante();
                ((LinkedList)fila1).addLast(pSaindo);
                ultimoAdd = true;
                IOociosa = true;
            }

            finalIO = finalCPU;
            escalonaCPU();
        }
        else{
            IOociosa = false; 
            //adicionando um No da fila de IO para o uso do IO
            Processo p = filaIO.remove();
            NoIO novo = new NoIO();
            novo.proc = p;
            //verificar quanto tempo o IO ficou ocioso antes de comecar a ser utilizado
            if(finalIO == 0){
                int fim = finalCPU;
                novo.inicioProc = fim;
                finalIO = fim + duracaoIO;
            }
            else{
                novo.inicioProc = finalIO;
                finalIO += duracaoIO;
            }
            //antes de adicionar o novo processo no uso do IO dar destino ao que esta saindo da fila de IO
            if(!usoIO.isEmpty() && !ultimoAdd){
                NoIO ultimo = usoIO.get(usoIO.size()-1);
                Processo pSaindo = ultimo.proc;
                pSaindo.decrementaIO();
                pSaindo.resetSurtoRestante();
                ((LinkedList)fila1).addLast(pSaindo);
            }
            ultimoAdd = false;
            usoIO.add(novo);
        }
    }
    
    void escalonaCPU(){
        if((fila1.isEmpty() && fila2.isEmpty() && fila3.isEmpty())==false){
            //PROCESSO NA FILA 1
            if(!fila1.isEmpty()){
                //remover o primeiro processo da fila1 e botar para o uso da CPU
                Processo p = fila1.remove();
                NoCPU  novo = new NoCPU();
                novo.proc = p;
                //verificar se o processo executa todo o burst e vai para o IO
                if(p.getSurtoRestante()<=quantumFila1){
                    finalCPU += p.getSurto(); 
                    //verifica se o processo ainda tem IOs para executar
                    //se tiver vai para a fila de IO
                    if(p.getnIO()>0){
                        if(filaIO.isEmpty() && finalIO<finalCPU){
                            finalIO = finalCPU;
                        }
                       ((LinkedList)filaIO).addLast(p); 
                    }
                    //se nao eh adicionado a fila de processos terminados
                    else{
                        ((LinkedList)procsTerminados).addLast(p);
                    }
                }
                //se nao, o processo vai para a proxima fila
                else{
                    p.decSurtoRestante(quantumFila1);
                    finalCPU += quantumFila1;
                    ((LinkedList)fila2).addLast(p);
                }
                novo.finalProc = finalCPU;
                usoCPU.add(novo);
            } 
            //PROCESSO NA FILA 2
            else{
                if(!fila2.isEmpty()){
                    //remover o primeiro processo da fila 2 e verificar por quanto tempo esse processo vai usar a CPU
                    Processo p = fila2.remove();
                    NoCPU novo = new NoCPU();
                    int tempo = p.getSurtoRestante();
                    //se o tempo que falta para acabar o burst de processo for menor que o quantum restante
                    if(tempo <= quantum2restante){
                        //esse processo pode ser preemptado por algum processo que acabou de usar o IO e foi para a fila 1
                        if(finalIO < finalCPU+tempo && !IOociosa){
                            p.decSurtoRestante(finalIO-finalCPU);
                            novo.finalProc = finalIO;
                            quantum2restante -= (finalIO-finalCPU);
                            finalCPU = finalIO;
                            ((LinkedList)fila2).addFirst(p);
                        }
                        //ou o processo pode terminar o burst e ir para a fila de IO ou terminar se nao tiver mais IOs para executar
                        else{
                            p.decSurtoRestante(tempo);
                            finalCPU += tempo;
                            novo.finalProc = finalCPU;
                            quantum2restante = quantumFila2;
                            //caso em que o processo vai para a fila de IO
                            if(p.getnIO()>0){
                                if(filaIO.isEmpty() && finalIO<finalCPU){
                                    finalIO = finalCPU;
                                }
                                ((LinkedList)filaIO).addLast(p);
                            }
                            //caso em que o processo eh terminado
                            else{
                                ((LinkedList)procsTerminados).addLast(p);
                            }
                        }
                    }
                    //se o tempo de burst restante do processo for maior que o quantum restante
                    else{
                        //o processo pode novamente ser preemptado por um processo que acabou de executar o IO e foi para a fila 1
                        if(finalIO < finalCPU+quantum2restante && !IOociosa){
                            p.decSurtoRestante(finalIO-finalCPU);
                            novo.finalProc = finalIO;
                            quantum2restante -= (finalIO-finalCPU);
                            finalCPU = finalIO;
                            ((LinkedList)fila2).addFirst(p);
                        }
                        //ou o processo pode atingir o quantum da fila 2 e vai para a fila tres para rodar o burst restante
                        else{
                            p.decSurtoRestante(quantum2restante);
                            finalCPU += quantum2restante;
                            novo.finalProc = finalCPU;
                            quantum2restante = quantumFila2;
                            ((LinkedList)fila3).addLast(p);
                        }
                    }
                    novo.proc = p;
                    usoCPU.add(novo);
                }
                //PROCESSO NA FILA 3
                else{
                    //processo eh removido da fila 3
                    Processo p = fila3.remove();
                    NoCPU novo = new NoCPU();
                    int tempo = p.getSurtoRestante();
                    //verifica se o processo pode ser preemptado por um processo que acabou de usar o IO e foi para a fila 1
                    //nesse caso o processo volta para o comeco da fila 3
                    if(finalIO < finalCPU+tempo && !IOociosa){
                        p.decSurtoRestante(finalIO-finalCPU);
                        novo.finalProc = finalIO;
                        finalCPU = finalIO;
                        ((LinkedList)fila3).addFirst(p);
                    }
                    //caso nao seja preemptado o processo vai para a fila de IO se ainda tiver IOs para executar
                    else{
                        p.decSurtoRestante(tempo);
                        finalCPU += tempo;
                        novo.finalProc = finalCPU;
                        if(p.getnIO()>0){
                            if(filaIO.isEmpty() && finalIO<finalCPU){
                                finalIO = finalCPU;
                            }
                            ((LinkedList)filaIO).addLast(p);
                        }
                        else{
                            ((LinkedList)procsTerminados).addLast(p);
                        }
                    }
                    novo.proc = p;
                    usoCPU.add(novo);
                }
            }
        }
        //caso todas as 3 filas estejam vazias
        else{
            //se ainda existirem processos na fila de IO
            //cria-se um processo de nome OCIOSO para verificar o tempo que a CPU ficou ociosa e chama o escalanaIO()
            if(!filaIO.isEmpty()){
                NoCPU novo = new NoCPU();
                Processo processo = new Processo(0,0,"OCIOSO");
                novo.proc = processo;
                finalCPU=finalIO;
                novo.finalProc = finalCPU;
                usoCPU.add(novo);
                escalonaIO();
            }
            //se tambem nao tiverem processos na fila de IO quer dizer que acabou o processamento
            else{
                terminado = true;
            }
        }
    }
    
    //esse metodo decide quando escalonar CPU ou IO
    //se o final do IO (final do ultimo processo sendo executado por IO) for maior que o final da CPU,
    //escalonaCPU ate que o final da cPU seja maior ou igual que o final do IO
    public void escalonar(){
        while(!terminado){
            if(finalCPU < finalIO){
                escalonaCPU();
            }
            else{
                escalonaIO();
            }
        }
    }   
    
    //imprime os processo que rodaram na CPU e em que tempo acabaram
    public void printCPU(){
        System.out.print("0 ");
        for(NoCPU no : usoCPU){
            System.out.print(no.proc.getNome()+" "+no.finalProc+" ");
        }
    }
   
    public static void main(String[] args) {
        int n;
        Scanner s = new Scanner(System.in);
        
        System.out.println("numero de processos: ");
        n = s.nextInt();
        
        ArrayList<Processo> procs = new ArrayList<>();
        int nIO, burst;
        
        for(int i=0; i<n; i++){
            System.out.println("Processo "+(i+1));
            System.out.println("Burst: ");
            burst = s.nextInt();
            System.out.println("Numero de IOs: ");
            nIO = s.nextInt();
            Processo p = new Processo(nIO,burst,"P"+(i+1));
            procs.add(p);
        }
        
        EscalonMultFilas escalonador = new EscalonMultFilas(procs);
        
        escalonador.escalonar();
        escalonador.printCPU();
        
    }
}
