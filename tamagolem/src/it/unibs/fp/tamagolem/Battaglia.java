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
    private int numPietrePerGolem;
    Allenatore allenatoreA;
    Allenatore allenatoreB;

    MatriceDiEquilibrio matriceDiEquilibrio;

    public Battaglia(int numElementi, int numPietre, Allenatore allenatoreA, Allenatore allenatoreB) {

        numPietrePerGolem = numElementi;
        this.allenatoreA = allenatoreA;
        this.allenatoreB = allenatoreB;

        for (int i = 0; i < numElementi; i++) {
            elementiADisposizione.add(TamagolemMain.NOME_ELEMENTI[i]);
        }
        // Viene generata la matrice che mette in equilibrio gli elementi selezionati
        matriceDiEquilibrio = new MatriceDiEquilibrio(elementiADisposizione, numElementi);


        // Generazione della scorta di pietre comune tramite HashMap
        aggiuntaPietreScorta(allenatoreA.getNumTamagolem(), numPietrePerGolem);

    }

    /**
     * gestione della battaglia tra i due allenatori
     * @param numPietre il numero di pietre che ogni tamagolem può avere
     * @param allenatoreA il primo allenatore creato
     * @param allenatoreB il secondo allenatore creato
     */
    public void eseguiBattaglia(Scanner scanner) {
        int danno;
        int turno = 1;
        Tamagolem tamagolemA = evocaTamagolem(allenatoreA, numPietrePerGolem);
        Tamagolem tamagolemB;

        do {
            tamagolemB = evocaTamagolem(allenatoreB, numPietrePerGolem);
        } while(checkStessePietre(tamagolemB, tamagolemA, numPietrePerGolem));

        while ((allenatoreA.getNumTamagolem() > 0 && allenatoreB.getNumTamagolem() > 0)
                || (allenatoreA.getNumTamagolem() == 0 && tamagolemA.getVitaAttuale() > 0 && !(allenatoreB.getNumTamagolem() == 0 && tamagolemB.getVitaAttuale() <= 0))
                || (allenatoreB.getNumTamagolem() == 0 && tamagolemB.getVitaAttuale() > 0) && !(allenatoreA.getNumTamagolem() == 0 && tamagolemA.getVitaAttuale() <= 0)) {

            if (tamagolemA.getVitaAttuale() <= 0) {
                do {
                    tamagolemA = evocaTamagolem(allenatoreA, numPietrePerGolem);
                } while(checkStessePietre(tamagolemA, tamagolemB, numPietrePerGolem));
            }

            if (tamagolemB.getVitaAttuale() <= 0) {
                do {
                    tamagolemB = evocaTamagolem(allenatoreB, numPietrePerGolem);
                } while(checkStessePietre(tamagolemB, tamagolemA, numPietrePerGolem));
            }

            while (tamagolemA.getVitaAttuale() > 0 && tamagolemB.getVitaAttuale() > 0) {

                danno = matriceDiEquilibrio.getPotenzaTraDueElementi(tamagolemA.getPietra(), tamagolemB.getPietra());

                do {
                    // mostra dettagli lotta
                    MenuTamagolem.mostraDettagliBattaglia(allenatoreA, allenatoreB, danno, turno);
                } while (!scanner.nextLine().isEmpty());

                tamagolemA.pietraSuccessiva();
                tamagolemB.pietraSuccessiva();

                turno++;

            }
        }


    }

    /**
     * controllo del vincitore a seconda dei tamagolem rimanenti degli allenatori
     * @param allenatoreA il primo allenatore creato
     * @param allenatoreB il secondo allenatore creato
     * @return il nome dell'allenatore nel caso di vittoria
     */
    public String getVincitore() {

        Tamagolem tamagolemA = allenatoreA.getTamagolemADisposizione();
        Tamagolem tamagolemB = allenatoreB.getTamagolemADisposizione();
        if (allenatoreA.getNumTamagolem() <= 0 && tamagolemB.getVitaAttuale() > 0) {
            return allenatoreB.getNome();

        } else if (allenatoreB.getNumTamagolem() <= 0 && tamagolemA.getVitaAttuale() > 0) {
            return allenatoreA.getNome();
        }else return null;

    }

    /**
     * controllo per evitare di entrare in un loop infinito durante la battaglia, nel quale gli elementi
     * possono coincidere all'infinito se selezionati nello stesso ordine
     * @param tamagolemDaEvocare il tamagolem nel quale vengono caricate le pietre
     * @param tamagolemEvocato il tamagolem già presente in campo
     * @param numPietre il numero di pietre che i tamagolem possono tenere
     * @return true se i tamagolem hanno lo stesso ordine di pietre, false altrimenti
     */
    private boolean checkStessePietre(Tamagolem tamagolemDaEvocare, Tamagolem tamagolemEvocato, int numPietre) {
        //Viene creato un secondo indice per il tamagolem già evocato, per tener conto della pietra che lancerà
        //all'inizio del turno
        int indiceTamagolemEvocato = tamagolemEvocato.getPietraDaLanciare();

        for(int i = 0; i < numPietre; i++) {
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

    /**
     * vengono ripristinate le pietre nel caso la scelta non sia valida (metodo checkStessePietre, caso true)
     * @param tamagolem il tamagolem appena evocato, usato per recuperare le pietre appena selezionate
     */
    private void reinserimentoPietreScortaComune(Tamagolem tamagolem) {
        //Si accede al valore della hashmap grazie all'arraylist di pietre appena selezionate
        //il valore iniziale
        for(String s : tamagolem.getPietreSelezionate()) {
            int numPietreRimaste = (scortaComunePietre.get(s) + 1);
            scortaComunePietre.put(s, numPietreRimaste);
        }
    }

    /**
     * ad ogni allenatore viene assegnato un tamagolem all'inizio della partita o nel caso uno venga sconfitto
     * @param allenatore l'allenatore alla quale viene associato il tamagolem
     * @param numPietre il numero di pietre che il tamagolem può tenere
     * @return istanza di tamagolem
     */
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


    /**
     * a seconda degli elementi a disposizione viene calcolata e riempita la scorta comune
     * @param numTamagolem il numero di tamagolem che ogni allenatore ha a disposizione
     * @param numPietre il numero di pietre che ogni tamagolem ha a disposizione
     */
    private void aggiuntaPietreScorta(int numTamagolem, int numPietre) {
        // la formula è leggermente diversa perchè in certi casi la scorta comune non bastava
        // per i tamagolem di ogni allenatore
        int numPietrePerElemento = ((3 * numTamagolem * numPietre) / elementiADisposizione.size());
        for (String s : elementiADisposizione) {
            // Le pietre vengono messe nell'hashmap insieme al numero iniziale
            scortaComunePietre.put(s, numPietrePerElemento);
        }

    }

    /**
     * Mappa che assegna ad ogni elemento un indice, per ordinare la hashmap e facilitare la selezione
     * delle pietre
     * @return la hashmap con gli elementi indicizzati
     */
    private Map<Integer, String> generaIndiceScorta() {
        Map<Integer, String> indicePietre = new HashMap<>();
        int counter = 1;
        for (String str : scortaComunePietre.keySet()) {
            indicePietre.put(counter, str);
            counter++;
        }
        return indicePietre;
    }

    /**
     * visualizzazione della scorta comune per poter scegliere dalle pietre disponibili
     * @param indicePietre la mappa generata con gli indici associati agli elementi
     * @param allenatore l'allenatore che sta selezionando le pietre
     * @param tamagolem il tamagolem alla quale si stanno aggiungendo le pietre
     */
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

    /**
     * a seconda dell'indice selezionato vengono scelte le pietre desiderate
     * @param indicePietre la hashmap con gli indici degli elementi nella scorta
     * @param allenatore l'allenatore che sta selezionando le pietre
     * @param tamagolem il tamagolem alla quale si stanno aggiungendo le pietre
     * @return il nome dell'elemento della pietra scelta dall'utente
     */
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

    /**
     * Visualizzazione finale dell'equilibrio generato ad inizio partita tramite matrice
     */
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
