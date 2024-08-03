package io.github.edwinmindcraft.origins.common.power;

import io.github.apace100.origins.power.OriginsPowerTypes;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.origins.common.power.configuration.NoSlowdownConfiguration;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;

public class NoSlowdownPower extends PowerFactory<NoSlowdownConfiguration> {

    public static boolean isActive(Entity player, BlockState state) {
        return IPowerContainer.getPowers(player, OriginsPowerTypes.NO_SLOWDOWN.get()).stream().anyMatch(x -> ((NoSlowdownConfiguration) ((ConfiguredPower) x.value()).getConfiguration()).test(state));
    }

    public NoSlowdownPower() {
        super(NoSlowdownConfiguration.CODEC);
    }
}