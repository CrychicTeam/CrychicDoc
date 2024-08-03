package se.mickelus.tetra.blocks;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import se.mickelus.mutil.util.TileEntityOptional;

@ParametersAreNonnullByDefault
public class TetraBlock extends Block implements InitializableBlock {

    public TetraBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public static void dropBlockInventory(Block thisBlock, Level world, BlockPos pos, BlockState newState) {
        if (!thisBlock.equals(newState.m_60734_())) {
            ((LazyOptional) TileEntityOptional.from(world, pos, BlockEntity.class).map(te -> te.getCapability(ForgeCapabilities.ITEM_HANDLER)).orElse(LazyOptional.empty())).ifPresent(cap -> {
                for (int i = 0; i < cap.getSlots(); i++) {
                    ItemStack itemStack = cap.getStackInSlot(i);
                    if (!itemStack.isEmpty()) {
                        Containers.dropItemStack(world, (double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), itemStack.copy());
                    }
                }
            });
            TileEntityOptional.from(world, pos, BlockEntity.class).ifPresent(BlockEntity::m_7651_);
        }
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> getTicker(BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
        return expectedType == givenType ? ticker : null;
    }
}