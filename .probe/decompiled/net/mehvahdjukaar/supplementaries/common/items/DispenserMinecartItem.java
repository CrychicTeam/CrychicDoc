package net.mehvahdjukaar.supplementaries.common.items;

import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.mehvahdjukaar.supplementaries.common.entities.dispenser_minecart.DispenserMinecartEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.gameevent.GameEvent;

public class DispenserMinecartItem extends Item {

    public static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {

        private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

        @Override
        public ItemStack execute(BlockSource pSource, ItemStack pStack) {
            Direction direction = (Direction) pSource.getBlockState().m_61143_(DispenserBlock.FACING);
            Level level = pSource.getLevel();
            double d0 = pSource.x() + (double) direction.getStepX() * 1.125;
            double d1 = Math.floor(pSource.y()) + (double) direction.getStepY();
            double d2 = pSource.z() + (double) direction.getStepZ() * 1.125;
            BlockPos blockpos = pSource.getPos().relative(direction);
            BlockState blockstate = level.getBlockState(blockpos);
            RailShape railshape = blockstate.m_60734_() instanceof BaseRailBlock railBlock ? ForgeHelper.getRailDirection(railBlock, blockstate, level, blockpos, null) : RailShape.NORTH_SOUTH;
            double d3;
            if (blockstate.m_204336_(BlockTags.RAILS)) {
                if (railshape.isAscending()) {
                    d3 = 0.6;
                } else {
                    d3 = 0.1;
                }
            } else {
                if (!blockstate.m_60795_() || !level.getBlockState(blockpos.below()).m_204336_(BlockTags.RAILS)) {
                    return this.defaultDispenseItemBehavior.dispense(pSource, pStack);
                }
                BlockState blockstate1 = level.getBlockState(blockpos.below());
                RailShape railshape1 = blockstate1.m_60734_() instanceof BaseRailBlock railBlockx ? (RailShape) blockstate1.m_61143_(railBlockx.getShapeProperty()) : RailShape.NORTH_SOUTH;
                if (direction != Direction.DOWN && railshape1.isAscending()) {
                    d3 = -0.4;
                } else {
                    d3 = -0.9;
                }
            }
            AbstractMinecart abstractminecart = new DispenserMinecartEntity(level, d0, d1 + d3, d2);
            if (pStack.hasCustomHoverName()) {
                abstractminecart.m_6593_(pStack.getHoverName());
            }
            level.m_7967_(abstractminecart);
            pStack.shrink(1);
            return pStack;
        }

        @Override
        protected void playSound(BlockSource blockSource) {
            blockSource.getLevel().m_46796_(1000, blockSource.getPos(), 0);
        }
    };

    public DispenserMinecartItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);
        if (!blockstate.m_204336_(BlockTags.RAILS)) {
            return InteractionResult.FAIL;
        } else {
            ItemStack itemstack = pContext.getItemInHand();
            if (!level.isClientSide) {
                RailShape railshape = blockstate.m_60734_() instanceof BaseRailBlock railBlock ? ForgeHelper.getRailDirection(railBlock, blockstate, level, blockpos, null) : RailShape.NORTH_SOUTH;
                double d0 = 0.0;
                if (railshape.isAscending()) {
                    d0 = 0.5;
                }
                AbstractMinecart abstractminecart = new DispenserMinecartEntity(level, (double) blockpos.m_123341_() + 0.5, (double) blockpos.m_123342_() + 0.0625 + d0, (double) blockpos.m_123343_() + 0.5);
                if (itemstack.hasCustomHoverName()) {
                    abstractminecart.m_6593_(itemstack.getHoverName());
                }
                level.m_7967_(abstractminecart);
                level.m_142346_(pContext.getPlayer(), GameEvent.ENTITY_PLACE, blockpos);
            }
            itemstack.shrink(1);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }
}