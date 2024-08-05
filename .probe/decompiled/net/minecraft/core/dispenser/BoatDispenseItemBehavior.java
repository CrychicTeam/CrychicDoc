package net.minecraft.core.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class BoatDispenseItemBehavior extends DefaultDispenseItemBehavior {

    private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

    private final Boat.Type type;

    private final boolean isChestBoat;

    public BoatDispenseItemBehavior(Boat.Type boatType0) {
        this(boatType0, false);
    }

    public BoatDispenseItemBehavior(Boat.Type boatType0, boolean boolean1) {
        this.type = boatType0;
        this.isChestBoat = boolean1;
    }

    @Override
    public ItemStack execute(BlockSource blockSource0, ItemStack itemStack1) {
        Direction $$2 = (Direction) blockSource0.getBlockState().m_61143_(DispenserBlock.FACING);
        Level $$3 = blockSource0.getLevel();
        double $$4 = 0.5625 + (double) EntityType.BOAT.getWidth() / 2.0;
        double $$5 = blockSource0.x() + (double) $$2.getStepX() * $$4;
        double $$6 = blockSource0.y() + (double) ((float) $$2.getStepY() * 1.125F);
        double $$7 = blockSource0.z() + (double) $$2.getStepZ() * $$4;
        BlockPos $$8 = blockSource0.getPos().relative($$2);
        double $$9;
        if ($$3.getFluidState($$8).is(FluidTags.WATER)) {
            $$9 = 1.0;
        } else {
            if (!$$3.getBlockState($$8).m_60795_() || !$$3.getFluidState($$8.below()).is(FluidTags.WATER)) {
                return this.defaultDispenseItemBehavior.dispense(blockSource0, itemStack1);
            }
            $$9 = 0.0;
        }
        Boat $$12 = (Boat) (this.isChestBoat ? new ChestBoat($$3, $$5, $$6 + $$9, $$7) : new Boat($$3, $$5, $$6 + $$9, $$7));
        $$12.setVariant(this.type);
        $$12.m_146922_($$2.toYRot());
        $$3.m_7967_($$12);
        itemStack1.shrink(1);
        return itemStack1;
    }

    @Override
    protected void playSound(BlockSource blockSource0) {
        blockSource0.getLevel().m_46796_(1000, blockSource0.getPos(), 0);
    }
}