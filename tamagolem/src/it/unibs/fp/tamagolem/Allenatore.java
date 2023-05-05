package it.unibs.fp.tamagolem;

import java.util.ArrayList;

public class Allenatore {
    private Tamagolem tamagolemADisposizione;
    private int numTamagolem;
    private String nome;

    public Allenatore(int numElementi, int numPietre, String nome) {
        this.numTamagolem = generaNumTamagolem(numElementi, numPietre);
        this.nome = nome;
    }

    public void setTamagolemADisposizione(Tamagolem tamagolem) {
        tamagolemADisposizione = tamagolem;
        numTamagolem --;
    }

    public int getNumTamagolem() {
        return numTamagolem;
    }

    public String getNome() {
        return nome;
    }

    private int generaNumTamagolem(int numElementi, int numPietre) {
        return (((numElementi - 1) * (numElementi - 2)) / (2 * numPietre));
    }

}
