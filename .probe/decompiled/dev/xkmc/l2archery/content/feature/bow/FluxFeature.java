package dev.xkmc.l2archery.content.feature.bow;

import dev.xkmc.l2archery.content.feature.BowArrowFeature;
import dev.xkmc.l2archery.init.data.LangData;
import java.util.List;
import net.minecraft.network.chat.MutableComponent;

public record FluxFeature(int maxEnergy, int extract, int receive, int perUsed) implements BowArrowFeature {

    public static FluxFeature DEFAULT = new FluxFeature(100000, 10000, 10000, 500);

    @Override
    public void addTooltip(List<MutableComponent> list) {
        list.add(LangData.FEATURE_FLUX_UP.get());
    }
}