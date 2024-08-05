package com.mna.api.blocks;

import com.mna.api.recipes.IManaweavePattern;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IManaweaveNotifiable {

    boolean notify(Level var1, BlockPos var2, BlockState var3, List<IManaweavePattern> var4, @Nullable LivingEntity var5);
}