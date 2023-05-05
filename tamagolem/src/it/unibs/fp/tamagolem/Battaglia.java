package it.unibs.fp.tamagolem;

import it.kibo.fp.lib.InputData;
import it.kibo.fp.lib.PrettyStrings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Battaglia {

    private Map<String, Integer> scortaComunePietre = new HashMap<>();
    private ArrayList<String> elementiADisposizione = new ArrayList<>();

    public Battaglia(int numElementi) {
        // A seconda della difficoltà scelta, vengono selezionati gli elementi da usare
        // nella battaglia
        for (int i = 0; i < numElementi; i++) {
            elementiADisposizione.add(TamagolemMain.NOME_ELEMENTI[i]);
        }
        // Viene generata la matrice che mette in equilibrio gli elementi selezionati
        MatriceDiEquilibrio matriceDiEquilibrio = new MatriceDiEquilibrio(elementiADisposizione, numElementi);

        // Genera il numero di pietre che i Tamagolem possono tenere
        int numPietre = generaNumPietre(numElementi);

        // Creazione allenatori
        Allenatore allenatoreA = new Allenatore(numElementi, numPietre, "Allenatore Ash");
        Allenatore allenatoreB = new Allenatore(numElementi, numPietre, "Allenatore Gary");

        // Generazione della scorta di pietre comune tramite HashMap
        aggiuntaPietreScorta(elementiADisposizione, allenatoreA.getNumTamagolem(), numPietre);

        int danno;
        int turno = 0;
        String statoPietraA = null;
        String statoPietraB = null;
        String mostraVitaA = null;
        String mostraVitaB = null;
        Scanner scanner = new Scanner(System.in);

        // LOTTA
        Tamagolem tamagolemA = evocaTamagolem(allenatoreA, numPietre);
        Tamagolem tamagolemB = evocaTamagolem(allenatoreB, numPietre);

        while (allenatoreA.getNumTamagolem() >= 0 && allenatoreB.getNumTamagolem() >= 0) {

            if (tamagolemA.getVitaAttuale() <= 0) {
                tamagolemA = evocaTamagolem(allenatoreA, numPietre);
            }

            if (tamagolemB.getVitaAttuale() <= 0) {
                tamagolemB = evocaTamagolem(allenatoreB, numPietre);
            }

            if (tamagolemA.getVitaAttuale() > 0) {
                mostraVitaA = String.valueOf(tamagolemA.getVitaAttuale());
            } else {
                mostraVitaA = "Morto";
            }

            if (tamagolemB.getVitaAttuale() > 0) {
                mostraVitaB = String.valueOf(tamagolemB.getVitaAttuale());
            } else {
                mostraVitaB = "Morto";
            }

            while (tamagolemA.getVitaAttuale() > 0 && tamagolemB.getVitaAttuale() > 0) {

                danno = matriceDiEquilibrio.getPotenzaTraDueElementi(tamagolemA.getPietra(), tamagolemB.getPietra());

                if (danno > 0) {
                    tamagolemB.subireDanno(danno);
                    statoPietraA = "Pietra Forte";
                    statoPietraB = "Pietra Debole";

                } else if (danno < 0) {
                    tamagolemA.subireDanno(Math.abs(danno));
                    statoPietraB = "Pietra Forte";
                    statoPietraA = "Pietra Debole";
                } else {
                    statoPietraB = "Stessa Pietra";
                    statoPietraA = "Stessa Pietra";
                }

                do {
                    // mostra dettagli lotta
                    System.out.println(TamagolemMain.FLUSH);

                    System.out.println(PrettyStrings.frame(String.format("TURNO %d", turno), 11, true, true));
                    System.out.println("TAMAGOLEM A DISPOSIZIONE");
                    System.out.println(allenatoreA.getNome() + ": " + allenatoreA.getNumTamagolem());
                    System.out.println(allenatoreB.getNome() + ": " + allenatoreB.getNumTamagolem());
                    System.out.println();

                    System.out.println("PIETRE");

                    System.out.println(allenatoreA.getNome() + ": " + tamagolemA.getPietra() + " -> " + statoPietraA);
                    System.out.println(allenatoreB.getNome() + ": " + tamagolemB.getPietra() + " -> " + statoPietraB);

                    System.out.println("Danno: " + Math.abs(danno));
                    System.out.println();

                    System.out.println("VITA TAMAGOLEM");
                    System.out.println(
                            "Vita Tamagolem dell'" + " " + allenatoreA.getNome() + ": " + mostraVitaA);
                    System.out.println(
                            "Vita Tamagolem dell'" + " " + allenatoreB.getNome() + ": " + mostraVitaB);

                    System.out.println("\nPremi invio per continuare");

                } while (!scanner.nextLine().isEmpty());

                tamagolemA.pietraSuccessiva();
                tamagolemB.pietraSuccessiva();

                turno++;

            }
        }

        if (allenatoreA.getNumTamagolem() < 0) {
            System.out.println(TamagolemMain.FLUSH);
            System.out.println(PrettyStrings.frame("HA VINTO L'" + allenatoreB.getNome(), 80, true, false));
        } else if (allenatoreB.getNumTamagolem() < 0) {
            System.out.println(TamagolemMain.FLUSH);
            System.out.println(PrettyStrings.frame("HA VINTO L'" + allenatoreA.getNome(), 80, true, false));
        }

    }

    private Tamagolem evocaTamagolem(Allenatore allenatore, int numPietre) {
        // Viene pescato il primo Tamagolem già creato con la creazione dell'allenatore
        Tamagolem tamagolem = new Tamagolem(numPietre);

        Map<Integer, String> indicePietre = generaIndiceScorta();

        // Vengono aggiunte pietre finchè il tamagolem non raggiunge il limite generato
        while (tamagolem.getPietreSelezionate().size() < tamagolem.getNumPietre()) {
            System.out.println("Selezione pietra numero ");
            String pietraSelezionata = sceltaPietra(indicePietre, allenatore);
            tamagolem.getPietreSelezionate().add(pietraSelezionata);
        }

        allenatore.setTamagolemADisposizione(tamagolem);

        return tamagolem;
    }

    private int generaNumPietre(int numElementi) {
        return (((numElementi + 1) / 3) + 1);
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

    private void visualizzaScortaComune(Map<Integer, String> indicePietre, Allenatore allenatore) {
        // Visualizza la scorta con l'indice assegnato all'elemento e la quantità di
        // pietre rimanenti
        System.out.println(TamagolemMain.FLUSH);
        String nomeAllenatore = PrettyStrings.frame(allenatore.getNome() +
                ": Seleziona le pietre per il tuo Tamagolem!", 100, true, false);
        System.out.println(nomeAllenatore);
        System.out.println(PrettyStrings.center("Scorta di pietre rimanenti: ", 30));

        // Loop dell'hashmap temporaneo per visualizzare un menu con indice, nome
        // elemento e numero di pietre che rimangono
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
            // Error check nel caso si provi a selezionare una pietra esaurita
            if (scortaComunePietre.get(pietraSelezionata) < 1) {
                System.out.println("Pietra esaurita, selezionarne un'altra");
            }
        } while (scortaComunePietre.get(pietraSelezionata) < 1);

        // Una volta selezionata la pietra viene scalato il totale
        int pietreRimanenti = scortaComunePietre.get(pietraSelezionata) - 1;
        scortaComunePietre.put(pietraSelezionata, pietreRimanenti);

        return pietraSelezionata;
    }

}
