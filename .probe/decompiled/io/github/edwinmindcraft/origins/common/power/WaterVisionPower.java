package io.github.edwinmindcraft.origins.common.power;

import io.github.apace100.origins.power.OriginsPowerTypes;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.origins.common.power.configuration.WaterVisionConfiguration;
import java.util.Optional;
import net.minecraft.world.entity.LivingEntity;

public class WaterVisionPower extends PowerFactory<WaterVisionConfiguration> {

    public static Optional<Float> getWaterVisionStrength(LivingEntity living) {
        return !OriginsPowerTypes.WATER_VISION.isPresent() ? Optional.empty() : IPowerContainer.getPowers(living, OriginsPowerTypes.WATER_VISION.get()).stream().map(x -> ((WaterVisionConfiguration) ((ConfiguredPower) x.value()).getConfiguration()).strength()).max(Float::compareTo);
    }

    public WaterVisionPower() {
        super(WaterVisionConfiguration.CODEC);
    }
}