package se.mickelus.tetra.module.schematic;

public enum SchematicRarity {

    temporary(16768938), hone(13553407), basic(16777215);

    public int tint;

    private SchematicRarity(int tint) {
        this.tint = tint;
    }
}