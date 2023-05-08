package it.unibs.fp.tamagolem;

import it.kibo.fp.lib.InputData;
import it.kibo.fp.lib.PrettyStrings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static it.unibs.fp.tamagolem.TextConstants.*;

public class Battaglia {

    private Map<String, Integer> scortaComunePietre = new HashMap<>();
    private ArrayList<String> elementiADisposizione = new ArrayList<>();

    public Battaglia(int numElementi, int numPietre, Allenatore allenatoreA, Allenatore allenatoreB) {

        for (int i = 0; i < numElementi; i++) {
            elementiADisposizione.add(TamagolemMain.NOME_ELEMENTI[i]);
        }
        // Viene generata la matrice che mette in equilibrio gli elementi selezionati
        MatriceDiEquilibrio matriceDiEquilibrio = new MatriceDiEquilibrio(elementiADisposizione, numElementi);

        // Generazione della scorta di pietre comune tramite HashMap
        aggiuntaPietreScorta(elementiADisposizione, allenatoreA.getNumTamagolem(), numPietre);

        int danno;
        int turno = 0;
        Scanner scanner = new Scanner(System.in);

        // LOTTA
        Tamagolem tamagolemA = evocaTamagolem(allenatoreA, numPietre);
        Tamagolem tamagolemB = evocaTamagolem(allenatoreB, numPietre);

        while ((allenatoreA.getNumTamagolem() > 0 && allenatoreB.getNumTamagolem() > 0)
                || (allenatoreA.getNumTamagolem() == 0 && tamagolemA.getVitaAttuale() > 0)
                || (allenatoreB.getNumTamagolem() == 0 && tamagolemB.getVitaAttuale() > 0)) {

            if (tamagolemA.getVitaAttuale() <= 0) {
                tamagolemA = evocaTamagolem(allenatoreA, numPietre);
            }

            if (tamagolemB.getVitaAttuale() <= 0) {
                tamagolemB = evocaTamagolem(allenatoreB, numPietre);
            }

            while (tamagolemA.getVitaAttuale() > 0 && tamagolemB.getVitaAttuale() > 0) {

                danno = matriceDiEquilibrio.getPotenzaTraDueElementi(tamagolemA.getPietra(), tamagolemB.getPietra());

                do {
                    // mostra dettagli lotta
                    MenuTamagolem.mostraDettagliBattaglia(allenatoreA, allenatoreB, danno, turno, tamagolemA, tamagolemB);
                } while (!scanner.nextLine().isEmpty());

                tamagolemA.pietraSuccessiva();
                tamagolemB.pietraSuccessiva();

                turno++;

            }
        }

        scanner.close();

        if (allenatoreA.getNumTamagolem() <= 0 && tamagolemB.getVitaAttuale() > 0) {
            System.out.println(TamagolemMain.FLUSH);
            System.out
                    .println(PrettyStrings.frame(VINTO + allenatoreB.getNome().toUpperCase(), 80, true, false));
        } else if (allenatoreB.getNumTamagolem() <= 0 && tamagolemA.getVitaAttuale() > 0) {
            System.out.println(TamagolemMain.FLUSH);
            System.out.println(PrettyStrings.frame(VINTO + allenatoreA.getNome().toUpperCase(), 80, true, false));
        }

    }



    private Tamagolem evocaTamagolem(Allenatore allenatore, int numPietre) {
        // Viene pescato il primo Tamagolem già creato con la creazione dell'allenatore
        Tamagolem tamagolem = new Tamagolem(numPietre);

        Map<Integer, String> indicePietre = generaIndiceScorta();

        // Vengono aggiunte pietre finchè il tamagolem non raggiunge il limite generato
        while (tamagolem.getPietreSelezionate().size() < tamagolem.getNumPietre()) {
            String pietraSelezionata = sceltaPietra(indicePietre, allenatore, tamagolem);
            tamagolem.getPietreSelezionate().add(pietraSelezionata);
        }

        allenatore.setTamagolemADisposizione(tamagolem);

        return tamagolem;
    }

    private void aggiuntaPietreScorta(ArrayList<String> listaElementi, int numGolem, int numPietre) {
        // Algoritmo che calcola quante pietre ci sono nella scorta comune a seconda del
        // numero di elementi (e di conseguenza pietre, golem..)
        int numPietrePerElemento = ((2 * numGolem * numPietre) / listaElementi.size());
        for (String s : listaElementi) {
            // Le pietre vengono messe nell'hashmap insieme al numero iniziale
            scortaComunePietre.put(s, numPietrePerElemento);
        }

    }

    private Map<Integer, String> generaIndiceScorta() {
        // Mappa temporanea che assegna ad ogni elemento un indice, torna utile nella
        // selezione della pietra desiderata
        Map<Integer, String> indicePietre = new HashMap<>();
        int counter = 1;
        for (String str : scortaComunePietre.keySet()) {
            indicePietre.put(counter, str);
            counter++;
        }
        return indicePietre;
    }

    private void visualizzaScortaComune(Map<Integer, String> indicePietre, Allenatore allenatore, Tamagolem tamagolem) {
        // Visualizza la scorta con l'indice assegnato all'elemento e la quantità di
        // pietre rimanenti
        System.out.println(TamagolemMain.FLUSH);
        String nomeAllenatore = PrettyStrings.frame(allenatore.getNome() +
                SELEZIONA_LE_PIETRE, 100, true, false);
        System.out.println(nomeAllenatore);
        System.out.println(SELEZIONE_PIETRA + (tamagolem.getPietreSelezionate().size() + 1));
        System.out.println(PIETRE_RIMANENTI);

        // Loop dell'hashmap temporaneo per visualizzare un menu con indice, nome
        // elemento e numero di pietre che rimangono
        for (Integer indice : indicePietre.keySet()) {
            String nomeElemento = indicePietre.get(indice);
            Integer numPietre = scortaComunePietre.get(nomeElemento);
            System.out.println(indice + "- " + "Elemento: " + nomeElemento + "   Pietre: " + numPietre);
        }
    }

    private String sceltaPietra(Map<Integer, String> indicePietre, Allenatore allenatore, Tamagolem tamagolem) {
        visualizzaScortaComune(indicePietre, allenatore, tamagolem);
        String pietraSelezionata;
        do {
            int scelta = InputData.readIntegerBetween(PIETRA_DESIDERATA, 1, indicePietre.size());
            pietraSelezionata = indicePietre.get(scelta);
            // Error check nel caso si provi a selezionare una pietra esaurita
            if (scortaComunePietre.get(pietraSelezionata) < 1) {
                System.out.println(PIETRA_ESAURITA);
            }
        } while (scortaComunePietre.get(pietraSelezionata) < 1);

        // Una volta selezionata la pietra viene scalato il totale
        int pietreRimanenti = scortaComunePietre.get(pietraSelezionata) - 1;
        scortaComunePietre.put(pietraSelezionata, pietreRimanenti);

        return pietraSelezionata;
    }

}
