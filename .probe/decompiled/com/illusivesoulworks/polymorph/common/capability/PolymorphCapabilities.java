package com.illusivesoulworks.polymorph.common.capability;

import com.illusivesoulworks.polymorph.api.common.capability.IBlockEntityRecipeData;
import com.illusivesoulworks.polymorph.api.common.capability.IPlayerRecipeData;
import com.illusivesoulworks.polymorph.api.common.capability.IStackRecipeData;
import com.illusivesoulworks.polymorph.platform.Services;
import java.util.Optional;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class PolymorphCapabilities {

    public static Optional<? extends IPlayerRecipeData> getRecipeData(Player player) {
        return Services.PLATFORM.getRecipeData(player);
    }

    public static Optional<? extends IBlockEntityRecipeData> getRecipeData(BlockEntity blockEntity) {
        return Services.PLATFORM.getRecipeData(blockEntity);
    }

    public static Optional<? extends IStackRecipeData> getRecipeData(ItemStack stack) {
        return Services.PLATFORM.getRecipeData(stack);
    }
}