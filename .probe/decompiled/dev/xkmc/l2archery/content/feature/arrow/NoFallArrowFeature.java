package dev.xkmc.l2archery.content.feature.arrow;

import dev.xkmc.l2archery.content.feature.types.FlightControlFeature;
import dev.xkmc.l2archery.init.data.LangData;
import java.util.List;
import net.minecraft.network.chat.MutableComponent;

public class NoFallArrowFeature extends FlightControlFeature {

    public NoFallArrowFeature(int life) {
        this.gravity = 0.0F;
        this.inertia = 1.0F;
        this.water_inertia = 1.0F;
        this.life = life;
    }

    @Override
    public boolean allowDuplicate() {
        return true;
    }

    @Override
    public void addTooltip(List<MutableComponent> list) {
        list.add(LangData.FEATURE_NO_FALL.get((double) this.life / 20.0));
    }
}