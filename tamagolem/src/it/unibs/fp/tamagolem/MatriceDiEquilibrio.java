package it.unibs.fp.tamagolem;

import java.util.ArrayList;
import java.util.Random;

public class MatriceDiEquilibrio
{

    private final int range;
    final static int NUM_PROVE_MAX = 10;
    private final int[][] matrice;
    ArrayList<String> elementi;



    public MatriceDiEquilibrio(ArrayList<String> elementi, int potenzaMassima)
    {
        this.elementi = elementi;
        this.range = potenzaMassima;
        matrice = generaMatrice(elementi.size());
        
    }

    /**
     * generazione della matrice di equilibrio con i valori specchiati per ogni elemento disponibile
     * @param numElementi il numero di elementi selezionati
     * @return la matrice di equilibrio
     */
    public int[][] generaMatrice(int numElementi)
    {
        int[][] matrice = new int[numElementi][numElementi];

        boolean riprova;

        for (int i = 0; i < numElementi; i++) {
            int somma = 0;
            int numProve = 0;
            riprova = false;

            for (int j = 0; j < i; j++) {
                somma += matrice[i][j];
            }
            for (int j = i; j < numElementi; j++) {

                if (i != j) {
                    if (j != numElementi - 1) {

                        do {
                            matrice[i][j] = generaCasualeSenzaZero(range);
                            somma += matrice[i][j];
                            numProve++;

                            if (Math.abs(somma) > range) {
                                somma -= matrice[i][j];
                                riprova = true;
                            } else {
                                riprova = false;
                            }

                        } while ((riprova && numProve <= NUM_PROVE_MAX));

                        if (numProve >= NUM_PROVE_MAX && i > 0) {
                            i -= 2;
                            break;
                        }

                        if (numProve >= NUM_PROVE_MAX && i == 0) {
                            i--;
                            break;
                        }

                        matrice[j][i] = -matrice[i][j];
                    } else {

                        matrice[i][j] = -somma;
                        matrice[j][i] = -matrice[i][j];
                    }
                }
            }

            if ((matrice[i][numElementi - 1] == 0 && i != numElementi - 1) && i < numElementi - 2) {
                i--;

            }

            if (i >= numElementi - 2 && ((matrice[i][numElementi - 1] == 0 && i != numElementi - 1) || Math.abs(matrice[i][numElementi - 2]) >= range + range / 2)) {
                i = 0;
            }
        }


        return matrice;

    }

    /**
     * genera un numero casuale in un range fornito, escludendo lo zero (ogni elemento deve avere interazione)
     * @param range il range predeterminato
     * @return il numero casuale
     */
    public int generaCasualeSenzaZero(int range) {
        Random rand = new Random();
        int casuale;

        do {
            casuale = rand.nextInt(range * 2 + 1) - range;
        } while (casuale == 0);

        return casuale;
    }


    /**
     * a seconda dell'indice della pietra lanciata dal primo tamagolem, viene paragonata la sua potenza rispetto
     * all'elemento della pietra lanciata dal secondo tamagolem
     * @param elementoA l'elemento lanciato dal primo tamagolem
     * @param elementoB l'elemento lanciato dal secondo tamagolem
     * @return la potenza dell'interazione dei due elementi secondo la matrice di equilibrio
     */
    public int getPotenzaTraDueElementi(String elementoA, String elementoB)
    {
        int indixElemA = 0, indixElemB = 0;
        for ( int i = 0; i<elementi.size(); i++)
        {
            if (elementi.get(i).equals(elementoA))
            {
                indixElemA = i;
            }

            if(elementi.get(i).equals(elementoB))
            {
                indixElemB = i;
            }
        }

        return matrice[indixElemA][indixElemB];
    }

    public int[][] getMatrice() {
        return matrice;
    }
}