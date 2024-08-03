package dev.latvian.mods.kubejs.platform.forge;

import dev.latvian.mods.kubejs.block.entity.BlockEntityInfo;
import dev.latvian.mods.kubejs.block.entity.BlockEntityJS;
import dev.latvian.mods.kubejs.core.InventoryKJS;
import dev.latvian.mods.kubejs.platform.LevelPlatformHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public class LevelForgeHelper implements LevelPlatformHelper {

    @Nullable
    @Override
    public InventoryKJS getInventoryFromBlockEntity(BlockEntity tileEntity, Direction facing) {
        IItemHandler handler = (IItemHandler) tileEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, facing).orElse(null);
        return handler instanceof InventoryKJS ? (InventoryKJS) handler : null;
    }

    @Override
    public boolean areCapsCompatible(ItemStack a, ItemStack b) {
        return a.areCapsCompatible(b);
    }

    @Override
    public double getReachDistance(LivingEntity livingEntity) {
        return livingEntity.getAttribute(ForgeMod.ENTITY_REACH.get()).getValue();
    }

    @Override
    public BlockEntityJS createBlockEntity(BlockPos pos, BlockState state, BlockEntityInfo info) {
        return new BlockEntityJS(pos, state, info);
    }
}