package net.minecraft.world.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.gameevent.GameEvent;

public class MinecartItem extends Item {

    private static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {

        private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

        @Override
        public ItemStack execute(BlockSource p_42949_, ItemStack p_42950_) {
            Direction $$2 = (Direction) p_42949_.getBlockState().m_61143_(DispenserBlock.FACING);
            Level $$3 = p_42949_.getLevel();
            double $$4 = p_42949_.x() + (double) $$2.getStepX() * 1.125;
            double $$5 = Math.floor(p_42949_.y()) + (double) $$2.getStepY();
            double $$6 = p_42949_.z() + (double) $$2.getStepZ() * 1.125;
            BlockPos $$7 = p_42949_.getPos().relative($$2);
            BlockState $$8 = $$3.getBlockState($$7);
            RailShape $$9 = $$8.m_60734_() instanceof BaseRailBlock ? (RailShape) $$8.m_61143_(((BaseRailBlock) $$8.m_60734_()).getShapeProperty()) : RailShape.NORTH_SOUTH;
            double $$10;
            if ($$8.m_204336_(BlockTags.RAILS)) {
                if ($$9.isAscending()) {
                    $$10 = 0.6;
                } else {
                    $$10 = 0.1;
                }
            } else {
                if (!$$8.m_60795_() || !$$3.getBlockState($$7.below()).m_204336_(BlockTags.RAILS)) {
                    return this.defaultDispenseItemBehavior.dispense(p_42949_, p_42950_);
                }
                BlockState $$12 = $$3.getBlockState($$7.below());
                RailShape $$13 = $$12.m_60734_() instanceof BaseRailBlock ? (RailShape) $$12.m_61143_(((BaseRailBlock) $$12.m_60734_()).getShapeProperty()) : RailShape.NORTH_SOUTH;
                if ($$2 != Direction.DOWN && $$13.isAscending()) {
                    $$10 = -0.4;
                } else {
                    $$10 = -0.9;
                }
            }
            AbstractMinecart $$17 = AbstractMinecart.createMinecart($$3, $$4, $$5 + $$10, $$6, ((MinecartItem) p_42950_.getItem()).type);
            if (p_42950_.hasCustomHoverName()) {
                $$17.m_6593_(p_42950_.getHoverName());
            }
            $$3.m_7967_($$17);
            p_42950_.shrink(1);
            return p_42950_;
        }

        @Override
        protected void playSound(BlockSource p_42947_) {
            p_42947_.getLevel().m_46796_(1000, p_42947_.getPos(), 0);
        }
    };

    final AbstractMinecart.Type type;

    public MinecartItem(AbstractMinecart.Type abstractMinecartType0, Item.Properties itemProperties1) {
        super(itemProperties1);
        this.type = abstractMinecartType0;
        DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext0) {
        Level $$1 = useOnContext0.getLevel();
        BlockPos $$2 = useOnContext0.getClickedPos();
        BlockState $$3 = $$1.getBlockState($$2);
        if (!$$3.m_204336_(BlockTags.RAILS)) {
            return InteractionResult.FAIL;
        } else {
            ItemStack $$4 = useOnContext0.getItemInHand();
            if (!$$1.isClientSide) {
                RailShape $$5 = $$3.m_60734_() instanceof BaseRailBlock ? (RailShape) $$3.m_61143_(((BaseRailBlock) $$3.m_60734_()).getShapeProperty()) : RailShape.NORTH_SOUTH;
                double $$6 = 0.0;
                if ($$5.isAscending()) {
                    $$6 = 0.5;
                }
                AbstractMinecart $$7 = AbstractMinecart.createMinecart($$1, (double) $$2.m_123341_() + 0.5, (double) $$2.m_123342_() + 0.0625 + $$6, (double) $$2.m_123343_() + 0.5, this.type);
                if ($$4.hasCustomHoverName()) {
                    $$7.m_6593_($$4.getHoverName());
                }
                $$1.m_7967_($$7);
                $$1.m_220407_(GameEvent.ENTITY_PLACE, $$2, GameEvent.Context.of(useOnContext0.getPlayer(), $$1.getBlockState($$2.below())));
            }
            $$4.shrink(1);
            return InteractionResult.sidedSuccess($$1.isClientSide);
        }
    }
}