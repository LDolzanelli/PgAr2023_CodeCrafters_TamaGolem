package it.unibs.fp.tamagolem;

import java.util.ArrayList;

public class Allenatore {
    ArrayList<Tamagolem> tamagolemADisposizione = new ArrayList<Tamagolem>();
    int numTamagolem;

    public Allenatore(int numElementi, int numPietre) {
        this.numTamagolem = generaNumTamagolem(numElementi, numPietre);
        aggiungiTamagolem(numTamagolem, numPietre);
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
