package net.mehvahdjukaar.moonlight.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface IRecolorable {

    boolean tryRecolor(Level var1, BlockPos var2, BlockState var3, @Nullable DyeColor var4);

    boolean isDefaultColor(Level var1, BlockPos var2, BlockState var3);
}