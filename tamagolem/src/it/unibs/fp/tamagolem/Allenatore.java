package it.unibs.fp.tamagolem;

public class Allenatore {
    private Tamagolem tamagolemADisposizione;
    private int numTamagolem;
    private final String nome;

    public Allenatore(int numElementi, int numPietre, String nome) {
        this.numTamagolem = generaNumTamagolem(numElementi, numPietre);
        this.nome = nome;
    }

    /**
     * viene assegnato il tamagolem appena evocato all'allenatore corretto, diminuendo il numero di
     * tamagolem a disposizione dell'allenatore
     * @param tamagolem l'istanza di tamagolem generata
     */
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

    /**
     * formula che genera il numero di tamagolem che l'allenatore ha a disposizione
     * @param numElementi il numero di elementi a disposizione a seconda della difficoltà
     * @param numPietre il numero di pietre che ogni tamagolem ha a disposizione
     * @return il numero di tamagolem a disposizione
     */
    private int generaNumTamagolem(int numElementi, int numPietre) {
        return (((numElementi - 1) * (numElementi - 2)) / (2 * numPietre));
    }

}
