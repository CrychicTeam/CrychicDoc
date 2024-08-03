package dev.xkmc.l2archery.content.feature;

import dev.xkmc.l2archery.content.item.IBowConfig;
import java.util.List;
import net.minecraft.network.chat.MutableComponent;

public interface BowArrowFeature {

    void addTooltip(List<MutableComponent> var1);

    default boolean allowDuplicate() {
        return false;
    }

    default boolean allow(IBowConfig config) {
        return true;
    }
}