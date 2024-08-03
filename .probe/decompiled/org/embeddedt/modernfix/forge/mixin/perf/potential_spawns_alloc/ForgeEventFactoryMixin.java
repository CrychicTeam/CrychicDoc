package org.embeddedt.modernfix.forge.mixin.perf.potential_spawns_alloc;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.ForgeEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ ForgeEventFactory.class })
public class ForgeEventFactoryMixin {

    @Redirect(method = { "getPotentialSpawns" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/random/WeightedRandomList;create(Ljava/util/List;)Lnet/minecraft/util/random/WeightedRandomList;"))
    private static WeightedRandomList<MobSpawnSettings.SpawnerData> reuseOldList(List<MobSpawnSettings.SpawnerData> items, LevelAccessor level, MobCategory category, BlockPos pos, WeightedRandomList<MobSpawnSettings.SpawnerData> oldList) {
        return items == oldList.unwrap() ? oldList : WeightedRandomList.create(items);
    }
}