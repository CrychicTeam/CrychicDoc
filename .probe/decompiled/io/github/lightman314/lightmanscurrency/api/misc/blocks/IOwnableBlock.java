package io.github.lightman314.lightmanscurrency.api.misc.blocks;

import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public interface IOwnableBlock {

    boolean canBreak(@Nonnull Player var1, @Nonnull LevelAccessor var2, @Nonnull BlockPos var3, @Nonnull BlockState var4);
}