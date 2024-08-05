package net.minecraft.world.level.chunk;

public class MissingPaletteEntryException extends RuntimeException {

    public MissingPaletteEntryException(int int0) {
        super("Missing Palette entry for index " + int0 + ".");
    }
}