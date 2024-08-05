package snownee.jade.api.view;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.ui.IDisplayHelper;

public class EnergyView {

    public String current;

    public String max;

    public float ratio;

    @Nullable
    public Component overrideText;

    @Nullable
    public static EnergyView read(CompoundTag tag, String unit) {
        long capacity = tag.getLong("Capacity");
        if (capacity <= 0L) {
            return null;
        } else {
            long cur = tag.getLong("Cur");
            EnergyView energyView = new EnergyView();
            energyView.current = IDisplayHelper.get().humanReadableNumber((double) cur, unit, false);
            energyView.max = IDisplayHelper.get().humanReadableNumber((double) capacity, unit, false);
            energyView.ratio = (float) cur / (float) capacity;
            return energyView;
        }
    }

    public static CompoundTag of(long current, long capacity) {
        CompoundTag tag = new CompoundTag();
        tag.putLong("Capacity", capacity);
        tag.putLong("Cur", current);
        return tag;
    }
}