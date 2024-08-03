package me.jellysquid.mods.lithium.mixin.collections.mob_spawning;

import me.jellysquid.mods.lithium.common.world.PotentialSpawnsExtended;
import net.minecraft.core.BlockPos;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.level.LevelEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({ ForgeEventFactory.class })
public class ForgeEventFactoryMixin {

    @Inject(method = { "getPotentialSpawns" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/Pool;of(Ljava/util/List;)Lnet/minecraft/util/collection/Pool;") }, locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private static void reusePoolIfPossible(LevelAccessor level, MobCategory category, BlockPos pos, WeightedRandomList<MobSpawnSettings.SpawnerData> oldList, CallbackInfoReturnable<WeightedRandomList<MobSpawnSettings.SpawnerData>> cir, LevelEvent.PotentialSpawns event) {
        if (!((PotentialSpawnsExtended) event).radium$wasListModified()) {
            cir.setReturnValue(oldList);
        }
    }
}