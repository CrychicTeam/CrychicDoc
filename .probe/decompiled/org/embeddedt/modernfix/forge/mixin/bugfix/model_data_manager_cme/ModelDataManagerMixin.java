package org.embeddedt.modernfix.forge.mixin.bugfix.model_data_manager_cme;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.client.model.data.ModelDataManager;
import org.embeddedt.modernfix.annotation.ClientOnlyMixin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ ModelDataManager.class })
@ClientOnlyMixin
public abstract class ModelDataManagerMixin {

    @Shadow
    @Final
    private Map<ChunkPos, Set<BlockPos>> needModelDataRefresh;

    @Shadow
    protected abstract void refreshAt(ChunkPos var1);

    @ModifyArg(method = { "requestRefresh" }, at = @At(value = "INVOKE", target = "Ljava/util/Map;computeIfAbsent(Ljava/lang/Object;Ljava/util/function/Function;)Ljava/lang/Object;", ordinal = 0), index = 1, remap = false)
    private Function<ChunkPos, Set<BlockPos>> changeTypeOfSetUsed(Function<ChunkPos, Set<BlockPos>> mappingFunction) {
        return pos -> Collections.newSetFromMap(new ConcurrentHashMap());
    }

    @Redirect(method = { "getAt(Lnet/minecraft/world/level/ChunkPos;)Ljava/util/Map;" }, at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/model/data/ModelDataManager;refreshAt(Lnet/minecraft/world/level/ChunkPos;)V"), remap = false)
    private void onlyRefreshOnMainThread(ModelDataManager instance, ChunkPos pos) {
        if (Minecraft.getInstance().m_18695_() && !this.needModelDataRefresh.isEmpty()) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    this.refreshAt(new ChunkPos(pos.x + x, pos.z + z));
                }
            }
        }
    }
}