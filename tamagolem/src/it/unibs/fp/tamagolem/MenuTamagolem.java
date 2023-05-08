package it.unibs.fp.tamagolem;

import it.kibo.fp.lib.InputData;
import it.kibo.fp.lib.Menu;
import it.kibo.fp.lib.PrettyStrings;

import java.util.Random;

import static it.unibs.fp.tamagolem.TextConstants.*;

public class MenuTamagolem {

    /**
     * menu per la scelta della difficoltà, utilizzato per generare il numero di elementi disponibili
     * @return la scelta della difficoltà (1 facile, 2 medio, 3 difficile)
     */
    public static int menuSceltaDifficolta() {
        System.out.println(PrettyStrings.frame(BENVENUTO, 30, true, false));
        Menu menuDifficolta = new Menu(SCELTA_DIFFICOLTA, VOCI_MENU_PRINCIPALE, false, true, true);

        return menuDifficolta.choose();
    }

    /**
     * ad ogni allenatore viene dato un nome scelto dall'utente
     * @param numAllenatore il numero di allenatore che si sta creando
     * @return il nome dell'allenatore
     */
    public static String inserimentoNomeAllenatore(int numAllenatore) {
        System.out.println("ALLENATORE " + numAllenatore);
        return "Allenatore " + InputData.readNonEmptyString("inserisci il tuo nome: ", false);
    }

}
