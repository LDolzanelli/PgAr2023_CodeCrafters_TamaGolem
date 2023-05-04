package it.unibs.fp.tamagolem;

import java.util.Random;

public class MatriceDiEquilibrio
{

    final static int RANGE = 7;
    final static int NUM_PROVE_MAX = 10;
    int matrice[][];
    String[] elementi;



    public MatriceDiEquilibrio(String[] elementi)
    {


        matrice = generaMatrice(elementi.length);
        

    }


    public int[][] generaMatrice(int numElementi)
    {
        int matrice[][] = new int[numElementi][numElementi];

        boolean riprova = false;

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
                            matrice[i][j] = generaCasualeSenzaZero(RANGE);
                            somma += matrice[i][j];
                            numProve++;

                            if (Math.abs(somma) > RANGE) {
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

            if (i > 0 && (matrice[i][numElementi - 1] == 0 && i != numElementi - 1) && i < numElementi - 2) {
                i--;

            }

            if (i >= numElementi - 2
                    && ((matrice[i][numElementi - 1] == 0 && i != numElementi - 1) || Math.abs(matrice[i][numElementi - 2]) >= RANGE + RANGE / 2)) {
                i = 0;
            }
        }


        return matrice;

    }
    
    public int generaCasualeSenzaZero(int range) {
        Random rand = new Random();
        int casuale;

        do {
            casuale = rand.nextInt(range * 2 + 1) - range;
        } while (casuale == 0);

        return casuale;
    }

    public int getPotenzaTraDueElementi(String elementoA, String elementoB)
    {
        int indixElemA = 0, indixElemB = 0;
        for ( int i = 0; i<elementi.length; i++)
        {
            if (elementi[i].equals(elementoA))
            {
                indixElemA = i;
            }

            if(elementi[i].equals(elementoB))
            {
                indixElemB = i;
            }
        }

        return matrice[indixElemA][indixElemB];
    }

}