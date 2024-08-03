package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSourceImpl;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.DropperBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class DropperBlock extends DispenserBlock {

    private static final DispenseItemBehavior DISPENSE_BEHAVIOUR = new DefaultDispenseItemBehavior();

    public DropperBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    protected DispenseItemBehavior getDispenseMethod(ItemStack itemStack0) {
        return DISPENSE_BEHAVIOUR;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new DropperBlockEntity(blockPos0, blockState1);
    }

    @Override
    protected void dispenseFrom(ServerLevel serverLevel0, BlockPos blockPos1) {
        BlockSourceImpl $$2 = new BlockSourceImpl(serverLevel0, blockPos1);
        DispenserBlockEntity $$3 = $$2.getEntity();
        int $$4 = $$3.getRandomSlot(serverLevel0.f_46441_);
        if ($$4 < 0) {
            serverLevel0.m_46796_(1001, blockPos1, 0);
        } else {
            ItemStack $$5 = $$3.m_8020_($$4);
            if (!$$5.isEmpty()) {
                Direction $$6 = (Direction) serverLevel0.m_8055_(blockPos1).m_61143_(f_52659_);
                Container $$7 = HopperBlockEntity.getContainerAt(serverLevel0, blockPos1.relative($$6));
                ItemStack $$8;
                if ($$7 == null) {
                    $$8 = DISPENSE_BEHAVIOUR.dispense($$2, $$5);
                } else {
                    $$8 = HopperBlockEntity.addItem($$3, $$7, $$5.copy().split(1), $$6.getOpposite());
                    if ($$8.isEmpty()) {
                        $$8 = $$5.copy();
                        $$8.shrink(1);
                    } else {
                        $$8 = $$5.copy();
                    }
                }
                $$3.m_6836_($$4, $$8);
            }
        }
    }
}