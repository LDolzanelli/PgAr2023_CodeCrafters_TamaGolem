package it.unibs.fp.tamagolem;

import it.kibo.fp.lib.Menu;
import it.kibo.fp.lib.PrettyStrings;

import java.util.Random;

public class MenuTamagolem {


    private static final String BENVENUTO = "BENVENUTO IN TAMAGOLEM!";
    private static final String SCELTA_DIFFICOLTA = "Scegli la difficoltà";
    private static final String[] VOCI_MENU_PRINCIPALE = {"Facile", "Medio", "Difficile"};


    public static void menuPrincipale() {
        System.out.println(PrettyStrings.frame(BENVENUTO, 30, true, false));
        Menu menuPrincipale = new Menu(SCELTA_DIFFICOLTA, VOCI_MENU_PRINCIPALE, false, true, true);
        int scelta = menuPrincipale.choose();
        int numeroElementi = generaNumeroElementi(scelta);

        switch(scelta){
            case 0:
                break;

            case 1:
                System.out.println(numeroElementi);
                break;

            case 2:
                System.out.println(numeroElementi);
                break;

            case 3:
                System.out.println(numeroElementi);
                break;

        }
    }

    public static int generaNumeroElementi(int difficoltaSelezionata) {
        Random random = new Random();
        int numeroElementi = 0;
        switch (difficoltaSelezionata) {
            case 0:
                break;

            case 1:
                numeroElementi = random.nextInt(3) + 3;
                break;

            case 2:
                numeroElementi = random.nextInt(3) + 6;
                break;

            case 3:
                numeroElementi = random.nextInt(2) + 9;
                break;
        }

        return numeroElementi;
    }

    /*
            if (difficoltaSelezionata == 1) {
            return (random.nextInt(3) + 3);
        }
        else if(difficoltaSelezionata == 2) {
            return (random.nextInt(3) + 6);
        }
        else {
            return (random.nextInt(2) + 9);
        }

     */
}
