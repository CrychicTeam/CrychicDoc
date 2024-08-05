package se.mickelus.tetra.items.modular.impl.holo;

public enum HoloPage {

    craft("CRFT"), structures("STRC"), system("SYST");

    public String label;

    private HoloPage(String label) {
        this.label = label;
    }
}