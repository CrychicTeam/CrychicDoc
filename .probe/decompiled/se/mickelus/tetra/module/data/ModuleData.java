package se.mickelus.tetra.module.data;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import se.mickelus.tetra.module.Priority;

@ParametersAreNonnullByDefault
public class ModuleData {

    private static final ModuleData defaultValues = new ModuleData();

    public String[] slots = new String[0];

    public String[] slotSuffixes = new String[0];

    public ResourceLocation type;

    public boolean replace = false;

    public Priority renderLayer = Priority.BASE;

    public Priority namePriority = Priority.BASE;

    public Priority prefixPriority = Priority.BASE;

    public ResourceLocation tweakKey;

    public boolean perk = false;

    public ResourceLocation[] improvements = new ResourceLocation[0];

    public VariantData[] variants = new VariantData[0];

    public static void copyFields(ModuleData from, ModuleData to) {
        to.slots = (String[]) Stream.concat(Arrays.stream(to.slots), Arrays.stream(from.slots)).distinct().toArray(String[]::new);
        to.slotSuffixes = (String[]) Stream.concat(Arrays.stream(to.slotSuffixes), Arrays.stream(from.slotSuffixes)).distinct().toArray(String[]::new);
        if (from.type != defaultValues.type) {
            to.type = from.type;
        }
        if (!Objects.equals(from.tweakKey, defaultValues.tweakKey)) {
            to.tweakKey = from.tweakKey;
        }
        if (from.renderLayer != defaultValues.renderLayer) {
            to.renderLayer = from.renderLayer;
        }
        if (from.namePriority != defaultValues.namePriority) {
            to.namePriority = from.namePriority;
        }
        if (from.prefixPriority != defaultValues.prefixPriority) {
            to.prefixPriority = from.prefixPriority;
        }
        if (from.perk != defaultValues.perk) {
            to.perk = from.perk;
        }
        to.improvements = (ResourceLocation[]) Stream.concat(Arrays.stream(to.improvements), Arrays.stream(from.improvements)).distinct().toArray(ResourceLocation[]::new);
        to.variants = (VariantData[]) Stream.concat(Arrays.stream(to.variants), Arrays.stream(from.variants)).toArray(VariantData[]::new);
    }

    public ModuleData shallowCopy() {
        ModuleData copy = new ModuleData();
        copyFields(this, copy);
        return copy;
    }
}