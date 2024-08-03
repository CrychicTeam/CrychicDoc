package dev.xkmc.l2archery.content.feature.materials;

import dev.xkmc.l2archery.content.feature.types.FlightControlFeature;
import java.util.List;
import net.minecraft.network.chat.MutableComponent;

public class PoseiditeArrowFeature extends FlightControlFeature {

    public PoseiditeArrowFeature() {
        this.water_inertia = this.inertia;
    }

    @Override
    public void addTooltip(List<MutableComponent> list) {
    }
}