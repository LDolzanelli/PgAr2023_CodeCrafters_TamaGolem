package it.unibs.fp.tamagolem;

import java.util.ArrayList;

public class Allenatore {
    private final ArrayList<Tamagolem> tamagolemADisposizione = new ArrayList<Tamagolem>();
    private final int numTamagolem;
    private final String nome;

    public Allenatore(int numElementi, int numPietre, String nome) {
        this.numTamagolem = generaNumTamagolem(numElementi, numPietre);
        aggiungiTamagolem(numTamagolem, numPietre);
        this.nome = nome;
    }

    public ArrayList<Tamagolem> getTamagolemADisposizione() {
        return tamagolemADisposizione;
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

    private void aggiungiTamagolem(int numTamagolem, int numPietre) {
        for(int i = 0; i < numTamagolem; i++) {
            tamagolemADisposizione.add(new Tamagolem(numPietre));
        }
    }
}
