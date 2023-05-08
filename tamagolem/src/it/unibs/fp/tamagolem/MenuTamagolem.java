package it.unibs.fp.tamagolem;

import it.kibo.fp.lib.InputData;
import it.kibo.fp.lib.Menu;
import it.kibo.fp.lib.PrettyStrings;

import java.util.Random;

import static it.unibs.fp.tamagolem.TextConstants.*;

public class MenuTamagolem {

    public static int menuSceltaDifficolta() {
        System.out.println(PrettyStrings.frame(BENVENUTO, 30, true, false));
        Menu menuDifficoltà = new Menu(SCELTA_DIFFICOLTA, VOCI_MENU_PRINCIPALE, false, true, true);
        int scelta = menuDifficoltà.choose();

        return scelta;
    }

    public static String inserimentoNomeAllenatore(int numAllenatore) {
        System.out.println("ALLENATORE " + numAllenatore);
        return "Allenatore " + InputData.readNonEmptyString("inserisci il tuo nome: ", false);
    }

}
