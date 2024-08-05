package se.mickelus.tetra.module.schematic;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;
import javax.annotation.ParametersAreNonnullByDefault;
import se.mickelus.tetra.module.data.GlyphData;
import se.mickelus.tetra.module.data.MaterialMultiplier;
import se.mickelus.tetra.module.schematic.requirement.CraftingRequirement;

@ParametersAreNonnullByDefault
public class SchematicDefinition {

    private static final SchematicDefinition defaultValues = new SchematicDefinition();

    public boolean replace = false;

    public String localizationKey;

    public String[] slots = new String[0];

    public String[] keySuffixes = new String[0];

    public int materialSlotCount = 0;

    public boolean repair = true;

    public boolean hone = false;

    public CraftingRequirement requirement = CraftingRequirement.any;

    public int materialRevealSlot = -1;

    public SchematicType displayType = SchematicType.other;

    public SchematicRarity rarity = SchematicRarity.basic;

    public GlyphData glyph = new GlyphData();

    public MaterialMultiplier translation;

    public String[] applicableMaterials;

    public OutcomeDefinition[] outcomes = new OutcomeDefinition[0];

    public String[] sources = new String[0];

    public String key;

    public static void copyFields(SchematicDefinition from, SchematicDefinition to) {
        to.slots = (String[]) Stream.concat(Arrays.stream(to.slots), Arrays.stream(from.slots)).distinct().toArray(String[]::new);
        to.keySuffixes = (String[]) Stream.concat(Arrays.stream(to.keySuffixes), Arrays.stream(from.keySuffixes)).distinct().toArray(String[]::new);
        if (!Objects.equals(from.localizationKey, defaultValues.localizationKey)) {
            to.localizationKey = from.localizationKey;
        }
        if (!Objects.equals(from.requirement, defaultValues.requirement)) {
            to.requirement = from.requirement;
        }
        if (from.materialSlotCount != defaultValues.materialSlotCount) {
            to.materialSlotCount = from.materialSlotCount;
        }
        if (from.repair != defaultValues.repair) {
            to.repair = from.repair;
        }
        if (from.hone != defaultValues.hone) {
            to.hone = from.hone;
        }
        if (!from.requirement.equals(defaultValues.requirement)) {
            to.requirement = from.requirement;
        }
        if (from.materialRevealSlot != defaultValues.materialRevealSlot) {
            to.materialRevealSlot = from.materialRevealSlot;
        }
        if (from.displayType != defaultValues.displayType) {
            to.displayType = from.displayType;
        }
        if (from.rarity != defaultValues.rarity) {
            to.rarity = from.rarity;
        }
        if (!from.glyph.equals(defaultValues.glyph)) {
            to.glyph = from.glyph;
        }
        if (to.applicableMaterials != null && from.applicableMaterials != null) {
            to.applicableMaterials = (String[]) Stream.concat(Arrays.stream(to.applicableMaterials), Arrays.stream(from.applicableMaterials)).toArray(String[]::new);
        } else if (from.applicableMaterials != null) {
            to.applicableMaterials = from.applicableMaterials;
        }
        to.outcomes = (OutcomeDefinition[]) Stream.concat(Arrays.stream(to.outcomes), Arrays.stream(from.outcomes)).toArray(OutcomeDefinition[]::new);
        to.sources = (String[]) Stream.concat(Arrays.stream(from.sources), Arrays.stream(to.sources)).distinct().toArray(String[]::new);
    }
}