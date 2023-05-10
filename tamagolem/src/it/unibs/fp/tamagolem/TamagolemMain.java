package it.unibs.fp.tamagolem;

import it.kibo.fp.lib.InputData;

import java.util.Random;
import java.util.Scanner;

import static it.unibs.fp.tamagolem.TextConstants.*;

public class TamagolemMain {

    public static final String[] NOME_ELEMENTI = { "Lotta", "Volante", "Terra", "Spettro", "Acciaio",
            "Psico", "Ghiaccio", "Veleno", "Drago", "Buio" };

    public static void main(String[] args) {
        
        boolean nuovaPartita;
        Scanner scanner = new Scanner(System.in);

        do {

            int numElementi = generaNumeroElementi();
            int numPietre = generaNumPietre(numElementi);

            // Creazione allenatori
            Allenatore allenatoreA = new Allenatore(numElementi, numPietre, MenuTamagolem.inserimentoNomeAllenatore(1));
            Allenatore allenatoreB = new Allenatore(numElementi, numPietre, MenuTamagolem.inserimentoNomeAllenatore(2));

            //battaglia
            Battaglia battaglia = new Battaglia(numElementi, numPietre, allenatoreA, allenatoreB);
            battaglia.eseguiBattaglia(scanner);
            MenuTamagolem.mostraVincitore(battaglia.getVincitore());
            battaglia.visualizzaEquilibrio();

            nuovaPartita = InputData.readYesOrNo(NUOVA_PARTITA);
        } while (nuovaPartita);

        scanner.close();
    }

    /**
     * genera un numero degli elementi in base alla difficoltà
     */
    public static int generaNumeroElementi() {

        int difficoltaSelezionata = MenuTamagolem.menuSceltaDifficolta();
        int numeroElementi = 0;
        // L'elemento in input è un numero tra 1 e 3, forzato dalla scelta del menu
        switch (difficoltaSelezionata) {
            case 1:
                // Difficoltà facile = da 4 a 5 elementi
                numeroElementi = generaRandomTraDueNumeri(4, 5);
                break;

            case 2:
                // Difficoltà media = da 6 a 8 elementi
                numeroElementi = generaRandomTraDueNumeri(6, 8);
                break;

            case 3:
                // Difficoltà difficile = da 9 a 10 elementi
                numeroElementi = generaRandomTraDueNumeri(9, 10);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + difficoltaSelezionata);
        }

        return numeroElementi;
    }

    /**
     * genera un numero random tra due parametri, estremi inclusi
     * @param num1 il numero minimo
     * @param num2 il numero massimo
     * @return il numero generato casualmente
     */
    public static int generaRandomTraDueNumeri(int num1, int num2) {
        Random random = new Random();
        return random.nextInt(num2 - num1 + 1) + num1;
    }

    /**
     * formula per il calcolo del numero di pietre che un tamagolem può tenere
     * @param numElementi il numero di elementi a disposizione a seconda della difficoltà selezionata
     * @return il numero di pietre
     */
    public static int generaNumPietre(int numElementi) {
        return (((numElementi + 1) / 3) + 1);
    }

}
