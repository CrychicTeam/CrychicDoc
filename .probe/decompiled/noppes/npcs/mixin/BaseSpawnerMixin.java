package noppes.npcs.mixin;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ BaseSpawner.class })
public interface BaseSpawnerMixin {

    @Invoker
    void callSetNextSpawnData(@Nullable Level var1, BlockPos var2, SpawnData var3);
}