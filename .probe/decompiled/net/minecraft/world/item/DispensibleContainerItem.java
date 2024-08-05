package net.minecraft.world.item;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public interface DispensibleContainerItem {

    default void checkExtraContent(@Nullable Player player0, Level level1, ItemStack itemStack2, BlockPos blockPos3) {
    }

    boolean emptyContents(@Nullable Player var1, Level var2, BlockPos var3, @Nullable BlockHitResult var4);
}