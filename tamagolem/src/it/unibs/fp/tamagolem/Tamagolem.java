package it.unibs.fp.tamagolem;

import java.util.ArrayList;

public class Tamagolem {

    public static final int VITA = 8;
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

        if(counter > numPietre - 1) {
            counter = 0;  
        }
        counter++;

        return pietreSelezionate.get(counter - 1);
        
    }
    

}
