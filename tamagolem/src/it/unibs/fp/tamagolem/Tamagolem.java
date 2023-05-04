package it.unibs.fp.tamagolem;

import java.util.ArrayList;

public class Tamagolem {

    public static final int VITA = 8;
    private int numPietre;
    private ArrayList<String> pietreSelezionate = new ArrayList<>();
    private int counter = 0;

    public Tamagolem(int numElementi) {
        this.numPietre = generaNumPietre(numElementi);
    }

    private int generaNumPietre(int numElementi) {
        int numPietre = (((numElementi + 1) / 3) + 1);
        return numPietre;
    }

    public void aggiungiPietre(String nomePietraSelezionata) {
        pietreSelezionate.add(nomePietraSelezionata);
    }

    public String getPietra() {
        if(counter > numPietre - 1) {
            counter = 0;  
        }
        counter++;

        return pietreSelezionate.get(counter - 1);
        
    }
    

}