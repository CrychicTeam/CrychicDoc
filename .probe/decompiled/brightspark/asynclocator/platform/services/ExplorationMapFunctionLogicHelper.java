package brightspark.asynclocator.platform.services;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.maps.MapDecoration;

public interface ExplorationMapFunctionLogicHelper {

    void invalidateMap(ItemStack var1, ServerLevel var2, BlockPos var3);

    void updateMap(ItemStack var1, ServerLevel var2, BlockPos var3, int var4, MapDecoration.Type var5, BlockPos var6, Component var7);
}