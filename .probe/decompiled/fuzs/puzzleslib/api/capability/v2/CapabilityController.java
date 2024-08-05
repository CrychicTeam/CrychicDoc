package fuzs.puzzleslib.api.capability.v2;

import com.google.common.collect.Maps;
import fuzs.puzzleslib.api.capability.v2.data.CapabilityComponent;
import fuzs.puzzleslib.api.capability.v2.data.CapabilityKey;
import fuzs.puzzleslib.api.capability.v2.data.PlayerCapabilityKey;
import fuzs.puzzleslib.api.capability.v2.data.PlayerRespawnCopyStrategy;
import fuzs.puzzleslib.api.capability.v2.data.SyncStrategy;
import fuzs.puzzleslib.impl.core.ModContext;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.ApiStatus.Internal;

public interface CapabilityController {

    @Internal
    Map<ResourceLocation, CapabilityKey<?>> CAPABILITY_KEY_REGISTRY = Maps.newConcurrentMap();

    @Internal
    Set<Class<?>> VALID_CAPABILITY_TYPES = Set.of(Entity.class, BlockEntity.class, LevelChunk.class, Level.class);

    @Internal
    static <T extends CapabilityComponent> void submit(CapabilityKey<T> capabilityKey) {
        if (CAPABILITY_KEY_REGISTRY.put(capabilityKey.getId(), capabilityKey) != null) {
            throw new IllegalStateException("Duplicate capability %s".formatted(capabilityKey.getId()));
        }
    }

    @Internal
    static CapabilityKey<?> retrieve(ResourceLocation id) {
        CapabilityKey<?> capabilityKey = (CapabilityKey<?>) CAPABILITY_KEY_REGISTRY.get(id);
        if (capabilityKey != null) {
            return capabilityKey;
        } else {
            throw new IllegalStateException("No capability registered for id %s".formatted(id));
        }
    }

    static CapabilityController from(String modId) {
        return ModContext.get(modId).getCapabilityController();
    }

    <T extends Entity, C extends CapabilityComponent> CapabilityKey<C> registerEntityCapability(String var1, Class<C> var2, Function<T, C> var3, Class<T> var4);

    <C extends CapabilityComponent> PlayerCapabilityKey<C> registerPlayerCapability(String var1, Class<C> var2, Function<Player, C> var3, PlayerRespawnCopyStrategy var4);

    <C extends CapabilityComponent> PlayerCapabilityKey<C> registerPlayerCapability(String var1, Class<C> var2, Function<Player, C> var3, PlayerRespawnCopyStrategy var4, SyncStrategy var5);

    <T extends BlockEntity, C extends CapabilityComponent> CapabilityKey<C> registerBlockEntityCapability(String var1, Class<C> var2, Function<T, C> var3, Class<T> var4);

    <C extends CapabilityComponent> CapabilityKey<C> registerLevelChunkCapability(String var1, Class<C> var2, Function<ChunkAccess, C> var3);

    <C extends CapabilityComponent> CapabilityKey<C> registerLevelCapability(String var1, Class<C> var2, Function<Level, C> var3);
}