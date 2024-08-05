package me.jellysquid.mods.lithium.mixin.world.combined_heightmap_update;

import java.util.function.Predicate;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ Heightmap.class })
public interface HeightmapAccessor {

    @Invoker
    void callSet(int var1, int var2, int var3);

    @Accessor("blockPredicate")
    Predicate<BlockState> getBlockPredicate();
}