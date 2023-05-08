package it.unibs.fp.tamagolem;

import it.kibo.fp.lib.InputData;
import it.kibo.fp.lib.Menu;
import it.kibo.fp.lib.PrettyStrings;


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
        System.out.println(ALLENATORE.toUpperCase() + numAllenatore);
        return ALLENATORE + InputData.readNonEmptyString("inserisci il tuo nome: ", false);
    }


    public static void mostraDettagliBattaglia(Allenatore allenatoreA, Allenatore allenatoreB, int danno, int turno, Tamagolem tamagolemA, Tamagolem tamagolemB) {

        String statoPietraA;
        String statoPietraB;
        String mostraVitaA;
        String mostraVitaB;

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


        System.out.println(TamagolemMain.FLUSH);
    
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
    }

 

}
