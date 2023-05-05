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
        int numElementi = generaNumeroElementi(scelta);

        Battaglia nuovaBattaglia = new Battaglia(numElementi);


    }

    public static int generaNumeroElementi(int difficoltaSelezionata) {
        Random random = new Random();
        int numeroElementi = 0;
        //L'elemento in input è un numero tra 0 e 3, forzato dalla scelta del menu
        switch (difficoltaSelezionata) {
            case 0:
                break;

            case 1:
                //Difficoltà facile = da 4 a 5 elementi
                numeroElementi = random.nextInt(2) + 4;
                break;

            case 2:
                //Difficoltà media = da 6 a 8 elementi
                numeroElementi = random.nextInt(3) + 6;
                break;

            case 3:
                //Difficoltà difficile = da 9 a 10 elementi
                numeroElementi = random.nextInt(2) + 9;
                break;
        }

        return numeroElementi;
    }

}
