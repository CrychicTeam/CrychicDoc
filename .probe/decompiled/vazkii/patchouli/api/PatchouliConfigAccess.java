package vazkii.patchouli.api;

import java.util.List;

public interface PatchouliConfigAccess {

    boolean disableAdvancementLocking();

    List<String> noAdvancementBooks();

    boolean testingMode();

    String inventoryButtonBook();

    boolean useShiftForQuickLookup();

    PatchouliConfigAccess.TextOverflowMode overflowMode();

    int quickLookupTime();

    public static enum TextOverflowMode {

        OVERFLOW, TRUNCATE, RESIZE
    }
}