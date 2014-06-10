/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.eb.ime.sisop.multilevelfeedback;

/**
 *
 * @author maiarab
 */
public class Processo {
    private int nIO;
    private final int surto;
    private int surtoRestante;
    private String nome;
    
    public Processo(int nIO, int surto, String nome){
        this.nIO = nIO;
        this.surto = surto;
        this.surtoRestante = surto;
        this.nome = nome;        
    }

    public int getnIO() {
        return nIO;
    }

    public int getSurto() {
        return surto;
    }

    public int getSurtoRestante() {
        return surtoRestante;
    }

    public void decSurtoRestante(int surtoADescrementar) {
        this.surtoRestante -= surtoADescrementar;
    }
    
    public void resetSurtoRestante() {
        this.surtoRestante = surto;
    }

    public String getNome() {
        return nome;
    }
    
    public int decrementaIO(){
        return this.nIO--;
    }
}
