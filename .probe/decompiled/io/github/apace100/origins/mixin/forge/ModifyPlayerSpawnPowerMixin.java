package io.github.apace100.origins.mixin.forge;

import io.github.apace100.apoli.util.ApoliConfigs;
import io.github.edwinmindcraft.apoli.api.ApoliAPI;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.common.power.ModifyPlayerSpawnPower;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyPlayerSpawnConfiguration;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import io.github.edwinmindcraft.apoli.common.util.SpawnLookupUtil;
import io.github.edwinmindcraft.origins.api.origin.IOriginCallbackPower;
import java.util.Set;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ ModifyPlayerSpawnPower.class })
public abstract class ModifyPlayerSpawnPowerMixin implements IOriginCallbackPower<ModifyPlayerSpawnConfiguration> {

    @Shadow
    @Final
    private static Set<ServerPlayer> PLAYERS_TO_RESPAWN;

    @Shadow
    public abstract void teleportToModifiedSpawn(ConfiguredPower<?, ?> var1, Entity var2);

    @Shadow
    @Nullable
    public abstract Tuple<ResourceKey<ConfiguredPower<?, ?>>, Tuple<ServerLevel, Vec3>> getSpawn(ConfiguredPower<?, ?> var1, Entity var2, boolean var3);

    @Override
    public <F extends PowerFactory<ModifyPlayerSpawnConfiguration>> void onChosen(@NotNull ConfiguredPower<ModifyPlayerSpawnConfiguration, F> configuration, @NotNull Entity living, boolean isOrb) {
        if (configuration.getFactory() instanceof IOriginCallbackPower && !isOrb) {
            this.teleportToModifiedSpawn(configuration, living);
        }
    }

    @Override
    public <F extends PowerFactory<ModifyPlayerSpawnConfiguration>> boolean isReady(@NotNull ConfiguredPower<ModifyPlayerSpawnConfiguration, F> configuration, @NotNull Entity living, boolean isOrb) {
        return isOrb || !ApoliConfigs.SERVER.separateSpawnFindingThread.get() || (Boolean) ApoliAPI.getPowers().getResourceKey(configuration).map(SpawnLookupUtil::hasSpawnCached).orElse(true);
    }

    @Override
    public <F extends PowerFactory<ModifyPlayerSpawnConfiguration>> void prepare(@NotNull ConfiguredPower<ModifyPlayerSpawnConfiguration, F> configuration, @NotNull Entity living, boolean isOrb) {
        if (ApoliConfigs.SERVER.separateSpawnFindingThread.get() && configuration.getFactory() instanceof IOriginCallbackPower && living instanceof ServerPlayer sp && !PLAYERS_TO_RESPAWN.contains(sp)) {
            this.getSpawn(configuration, living, false);
            ((ModifyPlayerSpawnPower) ApoliPowers.MODIFY_PLAYER_SPAWN.get()).schedulePlayerToSpawn(sp);
        }
    }
}