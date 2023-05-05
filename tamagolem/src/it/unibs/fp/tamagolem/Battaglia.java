package it.unibs.fp.tamagolem;

import it.kibo.fp.lib.Menu;

import java.util.ArrayList;
import java.util.HashMap;

public class Battaglia {

    private HashMap<String, Integer> scortaComunePietre = new HashMap<String, Integer>();
    private int numElementi;

    public Battaglia(int numElementi) {
        this.numElementi = numElementi;
        HashMap<String, Integer> scortaComunePietre = new HashMap<String, Integer>();
        ArrayList<String> elementiADisposizione = new ArrayList<>();
        for(int i = 0; i < numElementi; i++) {
            elementiADisposizione.add(TamagolemMain.NOME_ELEMENTI[i]);
        }
        MatriceDiEquilibrio matriceDiEquilibrio = new MatriceDiEquilibrio(elementiADisposizione, numElementi);
        int numPietre = generaNumPietre(numElementi);

        Allenatore allenatoreA = new Allenatore(numElementi, numPietre);
        Allenatore allenatoreB = new Allenatore(numElementi, numPietre);

        aggiuntaPietreScorta(elementiADisposizione, allenatoreA.numTamagolem, numPietre);
        evocaTamagolem(allenatoreA);
        //while(allenatoreA.tamagolemADisposizione.size() > 0 && allenatoreB.tamagolemADisposizione.size() > 0) {}
    }

    private int generaNumPietre(int numElementi) {
        int numPietre = (((numElementi + 1) / 3) + 1);
        return numPietre;
    }

    private void aggiuntaPietreScorta(ArrayList<String> listaElementi, int numGolem, int numPietre) {
        int numPietrePerElemento = ((2 * numGolem * numPietre) / listaElementi.size());
        for(int i = 0; i < listaElementi.size(); i++) {
            scortaComunePietre.put(listaElementi.get(i), numPietrePerElemento);
        }

    }

    private void visualizzaScortaComune() {
        for (String i : scortaComunePietre.keySet()) {
            System.out.println("Elemento: " + i + "   Pietre: " + scortaComunePietre.get(i));
        }
    }

    //private String sceltaPietra() {}

    private Tamagolem evocaTamagolem(Allenatore allenatore) {
        Tamagolem tamagolem = allenatore.tamagolemADisposizione.get(0);
        visualizzaScortaComune();

        tamagolem.aggiungiPietre("Prova");
        return tamagolem;
    }

}
