package io.github.edwinmindcraft.origins.api.origin;

import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import net.minecraft.world.entity.Entity;

public interface IOriginCallbackPower<T extends IDynamicFeatureConfiguration> {

    default <F extends PowerFactory<T>> void onChosen(ConfiguredPower<T, F> power, Entity living, boolean isOrb) {
        if (power.getFactory() instanceof IOriginCallbackPower) {
            ((IOriginCallbackPower) power.getFactory()).onChosen(power.getConfiguration(), living, isOrb);
        }
    }

    default <F extends PowerFactory<T>> void prepare(ConfiguredPower<T, F> power, Entity living, boolean isOrb) {
        if (power.getFactory() instanceof IOriginCallbackPower) {
            ((IOriginCallbackPower) power.getFactory()).prepare(power.getConfiguration(), living, isOrb);
        }
    }

    default <F extends PowerFactory<T>> boolean isReady(ConfiguredPower<T, F> power, Entity living, boolean isOrb) {
        return power.getFactory() instanceof IOriginCallbackPower ? ((IOriginCallbackPower) power.getFactory()).isReady(power.getConfiguration(), living, isOrb) : false;
    }

    void onChosen(T var1, Entity var2, boolean var3);

    default boolean isReady(T configuration, Entity entity, boolean isOrb) {
        return true;
    }

    default void prepare(T configuration, Entity entity, boolean isOrb) {
    }
}