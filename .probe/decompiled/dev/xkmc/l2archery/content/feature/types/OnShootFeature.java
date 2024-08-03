package dev.xkmc.l2archery.content.feature.types;

import dev.xkmc.l2archery.content.entity.GenericArrowEntity;
import dev.xkmc.l2archery.content.feature.BowArrowFeature;
import java.util.function.Consumer;
import net.minecraft.world.entity.LivingEntity;

public interface OnShootFeature extends BowArrowFeature {

    boolean onShoot(LivingEntity var1, Consumer<Consumer<GenericArrowEntity>> var2);

    default void onClientShoot(GenericArrowEntity entity) {
    }
}