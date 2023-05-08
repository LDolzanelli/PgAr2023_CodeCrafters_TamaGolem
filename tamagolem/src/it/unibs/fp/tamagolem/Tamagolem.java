package it.unibs.fp.tamagolem;

import java.util.ArrayList;

public class Tamagolem {

    public static final int VITA_INIZIALE = 8;
    private int vitaAttuale = VITA_INIZIALE;
    private final int numPietre;
    private final ArrayList<String> pietreSelezionate = new ArrayList<>();
    private int counter = 0;

    public Tamagolem(int numPietre) {
        this.numPietre = numPietre;
    }

    public ArrayList<String> getPietreSelezionate() {
        return pietreSelezionate;
    }

    public int getNumPietre() {
        return numPietre;
    }

    public String getPietra() {

        return pietreSelezionate.get(counter);

    }

    public void pietraSuccessiva() {

        if (counter == pietreSelezionate.size() - 1) {
            counter = 0;
        }else
        {
            counter++;
        }

       
    }

    public int getVitaAttuale() {
        return vitaAttuale;
    }

    public void subireDanno(int danno) {

        vitaAttuale -= danno;

    }

}
