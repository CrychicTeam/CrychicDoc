package vazkii.patchouli.forge.common;

import java.util.Collections;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;
import vazkii.patchouli.api.PatchouliConfigAccess;
import vazkii.patchouli.common.base.PatchouliConfig;

public class ForgePatchouliConfig {

    public static final ForgeConfigSpec.ConfigValue<Boolean> disableAdvancementLocking;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> noAdvancementBooks;

    public static final ForgeConfigSpec.ConfigValue<Boolean> testingMode;

    public static final ForgeConfigSpec.ConfigValue<String> inventoryButtonBook;

    public static final ForgeConfigSpec.ConfigValue<Boolean> useShiftForQuickLookup;

    public static final ForgeConfigSpec.EnumValue<PatchouliConfigAccess.TextOverflowMode> overflowMode;

    public static final ForgeConfigSpec.ConfigValue<Integer> quickLookupTime;

    private static final ForgeConfigSpec SPEC;

    public static void setup() {
        ModLoadingContext.get().registerConfig(Type.CLIENT, SPEC);
        PatchouliConfig.set(new PatchouliConfigAccess() {

            @Override
            public boolean disableAdvancementLocking() {
                return ForgePatchouliConfig.disableAdvancementLocking.get();
            }

            @Override
            public List<String> noAdvancementBooks() {
                return ForgePatchouliConfig.noAdvancementBooks.get();
            }

            @Override
            public boolean testingMode() {
                return ForgePatchouliConfig.testingMode.get();
            }

            @Override
            public String inventoryButtonBook() {
                return ForgePatchouliConfig.inventoryButtonBook.get();
            }

            @Override
            public boolean useShiftForQuickLookup() {
                return ForgePatchouliConfig.useShiftForQuickLookup.get();
            }

            @Override
            public PatchouliConfigAccess.TextOverflowMode overflowMode() {
                return (PatchouliConfigAccess.TextOverflowMode) ForgePatchouliConfig.overflowMode.get();
            }

            @Override
            public int quickLookupTime() {
                return ForgePatchouliConfig.quickLookupTime.get();
            }
        });
    }

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        disableAdvancementLocking = builder.comment("Set this to true to disable advancement locking for ALL books, making all entries visible at all times. Config Flag: advancements_disabled").define("disableAdvancementLocking", false);
        noAdvancementBooks = builder.comment("Granular list of Book ID's to disable advancement locking for, e.g. [ \"botania:lexicon\" ]. Config Flags: advancements_disabled_<bookid>").defineListAllowEmpty(List.of("noAdvancementBooks"), Collections::emptyList, o -> {
            if (o instanceof String s && ResourceLocation.tryParse(s) != null) {
                return true;
            }
            return false;
        });
        testingMode = builder.comment("Enable testing mode. By default this doesn't do anything, but you can use the config flag in your books if you want. Config Flag: testing_mode").define("testingMode", false);
        inventoryButtonBook = builder.comment("Set this to the ID of a book to have it show up in players' inventories, replacing the recipe book.").define("inventoryButtonBook", "");
        useShiftForQuickLookup = builder.comment("Set this to true to use Shift instead of Ctrl for the inventory quick lookup feature.").define("useShiftForQuickLookup", false);
        overflowMode = builder.comment("Set how text overflow should be coped with: overflow the text off the page, truncate overflowed text, or resize everything to fit. Relogin after changing.").defineEnum("textOverflowMode", PatchouliConfigAccess.TextOverflowMode.RESIZE);
        quickLookupTime = builder.comment("How long in ticks the quick lookup key needs to be pressed before the book opens").define("quickLookupTime", 10);
        SPEC = builder.build();
    }
}