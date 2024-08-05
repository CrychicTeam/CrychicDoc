package me.jellysquid.mods.lithium.mixin.experimental.spawning;

import me.jellysquid.mods.lithium.common.world.ChunkAwareEntityIterable;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ ServerChunkCache.class })
public class ServerChunkManagerMixin {

    @Redirect(method = { "tickChunks" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;iterateEntities()Ljava/lang/Iterable;"))
    private Iterable<Entity> iterateEntitiesChunkAware(ServerLevel serverWorld) {
        return ((ChunkAwareEntityIterable) ((ServerEntityManagerAccessor) ((ServerWorldAccessor) serverWorld).getEntityManager()).getCache()).lithiumIterateEntitiesInTrackedSections();
    }
}