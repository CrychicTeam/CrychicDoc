package dev.xkmc.l2archery.content.feature.materials;

import dev.xkmc.l2archery.content.entity.GenericArrowEntity;
import dev.xkmc.l2archery.content.feature.types.OnShootFeature;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;

public record TotemicArrowFeature(int heal) implements OnShootFeature {

    @Override
    public void addTooltip(List<MutableComponent> list) {
    }

    @Override
    public boolean onShoot(LivingEntity player, Consumer<Consumer<GenericArrowEntity>> entity) {
        player.heal((float) this.heal);
        return true;
    }
}