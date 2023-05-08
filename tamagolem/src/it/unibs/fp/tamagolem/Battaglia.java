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

    MatriceDiEquilibrio matriceDiEquilibrio;

    public Battaglia(int numElementi, int numPietre, Allenatore allenatoreA, Allenatore allenatoreB) {
        // A seconda della difficoltà scelta, vengono selezionati gli elementi da usare
        // nella battaglia
        for (int i = 0; i < numElementi; i++) {
            elementiADisposizione.add(TamagolemMain.NOME_ELEMENTI[i]);
        }
        // Viene generata la matrice che mette in equilibrio gli elementi selezionati
        matriceDiEquilibrio = new MatriceDiEquilibrio(elementiADisposizione, numElementi);

        // Genera il numero di pietre che i Tamagolem possono tenere
        



        // Generazione della scorta di pietre comune tramite HashMap
        aggiuntaPietreScorta(allenatoreA.getNumTamagolem(), numPietre);

        int danno;
        int turno = 1;
        String statoPietraA = null;
        String statoPietraB = null;
        String mostraVitaA = null;
        String mostraVitaB = null;
        Scanner scanner = new Scanner(System.in);

        // LOTTA

        Tamagolem tamagolemA = evocaTamagolem(allenatoreA, numPietre);
        Tamagolem tamagolemB;

        do {
            tamagolemB = evocaTamagolem(allenatoreB, numPietre);
        } while(checkStessePietre(tamagolemB, tamagolemA, numPietre));

        while ((allenatoreA.getNumTamagolem() > 0 && allenatoreB.getNumTamagolem() > 0)
                || (allenatoreA.getNumTamagolem() == 0 && tamagolemA.getVitaAttuale() > 0 && !(allenatoreB.getNumTamagolem() == 0 && tamagolemB.getVitaAttuale() <= 0))
                || (allenatoreB.getNumTamagolem() == 0 && tamagolemB.getVitaAttuale() > 0) && !(allenatoreA.getNumTamagolem() == 0 && tamagolemA.getVitaAttuale() <= 0)) {

            if (tamagolemA.getVitaAttuale() <= 0) {
                do {
                    tamagolemA = evocaTamagolem(allenatoreA, numPietre);
                } while(checkStessePietre(tamagolemA, tamagolemB, numPietre));
            }

            if (tamagolemB.getVitaAttuale() <= 0) {
                do {
                    tamagolemB = evocaTamagolem(allenatoreB, numPietre);
                } while(checkStessePietre(tamagolemB, tamagolemA, numPietre));
            }

            while (tamagolemA.getVitaAttuale() > 0 && tamagolemB.getVitaAttuale() > 0) {

                danno = matriceDiEquilibrio.getPotenzaTraDueElementi(tamagolemA.getPietra(), tamagolemB.getPietra());

                if (danno > 0) {
                    tamagolemB.subireDanno(danno);
                    statoPietraA = PIETRA_FORTE;
                    statoPietraB = PIETRA_DEBOLE;

                } else if (danno < 0) {
                    tamagolemA.subireDanno(Math.abs(danno));
                    statoPietraB = PIETRA_FORTE;
                    statoPietraA = PIETRA_DEBOLE;
                } else {
                    statoPietraB = STESSA_PIETRA;
                    statoPietraA = STESSA_PIETRA;
                }

                if (tamagolemA.getVitaAttuale() > 0) {
                    mostraVitaA = String.valueOf(tamagolemA.getVitaAttuale());
                } else {
                    mostraVitaA = MORTO;
                }

                if (tamagolemB.getVitaAttuale() > 0) {
                    mostraVitaB = String.valueOf(tamagolemB.getVitaAttuale());
                } else {
                    mostraVitaB = MORTO;
                }

                do {
                    // mostra dettagli lotta
                    System.out.println(FLUSH);

                    System.out.println(PrettyStrings.frame(String.format(TURNO_X, turno), 11, true, true));
                    System.out.println(TAMAGOLEM_A_DISPOSIZIONE);
                    System.out.println(allenatoreA.getNome() + DUE_PUNTI + allenatoreA.getNumTamagolem());
                    System.out.println(allenatoreB.getNome() + DUE_PUNTI + allenatoreB.getNumTamagolem());
                    System.out.println();

                    System.out.println(PIETRE);

                    System.out.println(allenatoreA.getNome() + DUE_PUNTI + tamagolemA.getPietra() + FRECCIA + statoPietraA);
                    System.out.println(allenatoreB.getNome() + DUE_PUNTI + tamagolemB.getPietra() + FRECCIA + statoPietraB);

                    System.out.println(DANNO + Math.abs(danno));
                    System.out.println();

                    System.out.println(VITA_TAMAGOLEM);
                    System.out.println(VITA_TAMAGOLEM_DELL + allenatoreA.getNome() + DUE_PUNTI + mostraVitaA);
                    System.out.println(VITA_TAMAGOLEM_DELL + allenatoreB.getNome() + DUE_PUNTI + mostraVitaB);

                    System.out.println(INVIO_PER_CONTINUARE);

                } while (!scanner.nextLine().isEmpty());

                tamagolemA.pietraSuccessiva();
                tamagolemB.pietraSuccessiva();

                turno++;

            }
        }

        if (allenatoreA.getNumTamagolem() <= 0 && tamagolemB.getVitaAttuale() > 0) {
            System.out.println(FLUSH);
            System.out
                    .println(PrettyStrings.frame(VINTO + allenatoreB.getNome().toUpperCase(), 80, true, true));
        } else if (allenatoreB.getNumTamagolem() <= 0 && tamagolemA.getVitaAttuale() > 0) {
            System.out.println(FLUSH);
            System.out.println(PrettyStrings.frame(VINTO + allenatoreA.getNome().toUpperCase(), 80, true, true));
        }

    }

    /**
     * controllo per evitare di entrare in un loop infinito durante la battaglia, nel quale gli elementi
     * possono coincidere all'infinito se selezionati nello stesso ordine
     * @param tamagolemDaEvocare
     * @param tamagolemEvocato
     * @param numPietre
     * @return
     */
    private boolean checkStessePietre(Tamagolem tamagolemDaEvocare, Tamagolem tamagolemEvocato, int numPietre) {
        int i;
        //Viene creato un secondo indice per il tamagolem già evocato, per tener conto della pietra che lancerà
        //all'inizio del turno
        int indiceTamagolemEvocato = tamagolemEvocato.getPietraDaLanciare();

        for(i = 0; i < numPietre; i++) {
            //Se l'indice del tamagolem evocato va in overflow, viene resettato a 0
            if(indiceTamagolemEvocato > numPietre - 1) {
                indiceTamagolemEvocato = 0;
            }
            //Vengono paragonate tutte le pietre in ordine, alla prima eccezione il gioco può continuare
            if(!tamagolemDaEvocare.getPietreSelezionate().get(i).equals
                    (tamagolemEvocato.getPietreSelezionate().get(indiceTamagolemEvocato))) {
                return false;
            }
            indiceTamagolemEvocato++;
        }
        //Vengono rimesse nella scorta le pietre selezionate ma non utilizzate
        reinserimentoPietreScortaComune(tamagolemDaEvocare);
        System.out.println();
        System.out.println(ATTENZIONE_PAREGGIO);
        System.out.println(INSERIRE_NUOVAMENTE_PIETRE);
        return true;
    }

    private void reinserimentoPietreScortaComune(Tamagolem tamagolem) {
        //Si accede al valore della hashmap grazie all'arraylist di pietre appena selezionate. viene ristabilito
        //il valore iniziale
        for(String s : tamagolem.getPietreSelezionate()) {
            int numPietreRimaste = (scortaComunePietre.get(s) + 1);
            scortaComunePietre.put(s, numPietreRimaste);
        }
    }

    private Tamagolem evocaTamagolem(Allenatore allenatore, int numPietre) {
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



    private void aggiuntaPietreScorta(int numGolem, int numPietre) {
        // Algoritmo che calcola quante pietre ci sono nella scorta comune a seconda del
        // numero di elementi (e di conseguenza pietre, golem..)
        // la formula è leggermente diversa perchè in certi casi la scorta comune non bastava
        // per i tamagolem di ogni allenatore
        int numPietrePerElemento = ((3 * numGolem * numPietre) / elementiADisposizione.size());
        for (String s : elementiADisposizione) {
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
        System.out.println(FLUSH);
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
            System.out.println(indice + ELEMENTO_SCORTA + nomeElemento + PIETRE_SCORTA + numPietre);
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

    public void visualizzaEquilibrio() {
        //viene caricata la matrice
        int[][] matrice = matriceDiEquilibrio.getMatrice();
        String messaggioEquilibrio = PrettyStrings.frame(MESSAGGIO_EQUILIBRIO, 80, true, true);
        System.out.println(messaggioEquilibrio);
        //stampa l'indice delle colonne
        for(int i = 0; i < elementiADisposizione.size(); i++) {
            if(i == 0) {
                System.out.print(String.format(FORMAT_EQUILIBRIO, " "));
            }
            System.out.print(String.format(FORMAT_EQUILIBRIO, elementiADisposizione.get(i)));
        }
        System.out.println();
        for(int i = 0; i < elementiADisposizione.size(); i++) {
            for(int j = 0; j < elementiADisposizione.size(); j++) {
                //stampa l'indice per ogni riga
                if(j == 0) {
                    System.out.print(String.format(FORMAT_EQUILIBRIO, elementiADisposizione.get(i)) + " ");
                }
                //rende più pulita la tabella centrando meglio i numeri negativi
                if(matrice[i][j] < 0) {
                    System.out.print(String.format(FORMAT_EQUILIBRIO, PrettyStrings.center(String.valueOf(matrice[i][j]), 10)));
                }
                else {
                    System.out.print(String.format(FORMAT_EQUILIBRIO, PrettyStrings.center(String.valueOf(matrice[i][j]), 8)));
                }

            }
            System.out.println();
        }
    }
}
