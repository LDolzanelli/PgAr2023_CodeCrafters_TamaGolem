package it.unibs.fp.tamagolem;

import it.kibo.fp.lib.InputData;
import it.kibo.fp.lib.PrettyStrings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Battaglia {

    private final Map<String, Integer> scortaComunePietre = new HashMap<>();
    private final ArrayList<String> elementiADisposizione = new ArrayList<>();

    public Battaglia(int numElementi) {
        //A seconda della difficoltà scelta, vengono selezionati gli elementi da usare nella battaglia
        for(int i = 0; i < numElementi; i++) {
            elementiADisposizione.add(TamagolemMain.NOME_ELEMENTI[i]);
        }
        //Viene generata la matrice che mette in equilibrio gli elementi selezionati
        MatriceDiEquilibrio matriceDiEquilibrio = new MatriceDiEquilibrio(elementiADisposizione, numElementi);

        //Genera il numero di pietre che i Tamagolem possono tenere
        int numPietre = generaNumPietre(numElementi);

        //Creazione allenatori
        Allenatore allenatoreA = new Allenatore(numElementi, numPietre, "Allenatore Ash");
        Allenatore allenatoreB = new Allenatore(numElementi, numPietre, "Allenatore Gary");

        //Generazione della scorta di pietre comune tramite HashMap
        aggiuntaPietreScorta(elementiADisposizione, allenatoreA.getNumTamagolem(), numPietre);

        //Evocazione di Tamagolem finchè gli allenatori non ne finiscono
        //sarà da cambiare una volta che gestiamo la lotta vera e propria
        while(allenatoreA.getTamagolemADisposizione().size() > 0 && allenatoreB.getTamagolemADisposizione().size() > 0) {
            Tamagolem tamagolemA = evocaTamagolem(allenatoreA);
            Tamagolem tamagolemB = evocaTamagolem(allenatoreB);

        }
    }

    private int generaNumPietre(int numElementi) {
        return (((numElementi + 1) / 3) + 1);
    }

    private void aggiuntaPietreScorta(ArrayList<String> listaElementi, int numGolem, int numPietre) {
        //Algoritmo che calcola quante pietre ci sono nella scorta comune a seconda del numero di elementi (e di conseguenza pietre, golem..)
        int numPietrePerElemento = ((2 * numGolem * numPietre) / listaElementi.size());
        for (String s : listaElementi) {
            //Le pietre vengono messe nell'hashmap insieme al numero iniziale
            scortaComunePietre.put(s, numPietrePerElemento);
        }

    }


    private Map<Integer, String> generaIndiceScorta() {
        //Mappa temporanea che assegna ad ogni elemento un indice, torna utile nella selezione della pietra desiderata
        Map<Integer, String> indicePietre = new HashMap<>();
        int counter = 1;
        for (String str : scortaComunePietre.keySet()) {
            indicePietre.put(counter, str);
            counter++;
        }
        return indicePietre;
    }
    private void visualizzaScortaComune(Map<Integer, String> indicePietre, Allenatore allenatore) {
        //Visualizza la scorta con l'indice assegnato all'elemento e la quantità di pietre rimanenti
        System.out.println(TamagolemMain.FLUSH);
        String nomeAllenatore = PrettyStrings.frame(allenatore.getNome() +
                ": Seleziona le pietre per il tuo Tamagolem!", 100, true, false);
        System.out.println(nomeAllenatore);
        System.out.println(PrettyStrings.center("Scorta di pietre rimanenti: ", 30));

        //Loop dell'hashmap temporaneo per visualizzare un menu con indice, nome elemento e numero di pietre che rimangono
        for (Integer indice : indicePietre.keySet()) {
            String nomeElemento = indicePietre.get(indice);
            Integer numPietre = scortaComunePietre.get(nomeElemento);
            System.out.println(indice + "- " + "Elemento: " + nomeElemento + "   Pietre: " + numPietre);
        }
    }

    private String sceltaPietra(Map<Integer, String> indicePietre, Allenatore allenatore) {
        visualizzaScortaComune(indicePietre, allenatore);
        String pietraSelezionata;
        do {
            int scelta = InputData.readIntegerBetween("Selezionare la pietra desiderata: ", 1, indicePietre.size());
            pietraSelezionata = indicePietre.get(scelta);
            //Error check nel caso si provi a selezionare una pietra esaurita
            if(scortaComunePietre.get(pietraSelezionata) < 1) {
                System.out.println("Pietra esaurita, selezionarne un'altra");
            }
        } while (scortaComunePietre.get(pietraSelezionata) < 1);

        //Una volta selezionata la pietra viene scalato il totale
        int pietreRimanenti = scortaComunePietre.get(pietraSelezionata) - 1;
        scortaComunePietre.put(pietraSelezionata, pietreRimanenti);

        return pietraSelezionata;
    }


    private Tamagolem evocaTamagolem(Allenatore allenatore) {
        //Viene pescato il primo Tamagolem già creato con la creazione dell'allenatore
        Tamagolem tamagolem = allenatore.getTamagolemADisposizione().get(0);

        Map<Integer, String> indicePietre = generaIndiceScorta();

        //Vengono aggiunte pietre finchè il tamagolem non raggiunge il limite generato
        while(tamagolem.getPietreSelezionate().size() < tamagolem.getNumPietre()) {
            System.out.println("Selezione pietra numero " );
            String pietraSelezionata = sceltaPietra(indicePietre, allenatore);
            tamagolem.getPietreSelezionate().add(pietraSelezionata);
        }
        return tamagolem;
    }

}
