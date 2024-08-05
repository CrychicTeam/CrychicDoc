package org.violetmoon.quark.base.item.boat;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.base.handler.WoodSetHandler;

public class QuarkBoatDispenseItemBehavior extends DefaultDispenseItemBehavior {

    private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

    private final String type;

    private final boolean chest;

    public QuarkBoatDispenseItemBehavior(String type, boolean chest) {
        this.type = type;
        this.chest = chest;
    }

    @NotNull
    @Override
    public ItemStack execute(BlockSource world, @NotNull ItemStack stack) {
        Direction direction = (Direction) world.getBlockState().m_61143_(DispenserBlock.FACING);
        Level level = world.getLevel();
        double boatX = world.x() + (double) ((float) direction.getStepX() * 1.125F);
        double boatY = world.y() + (double) ((float) direction.getStepY() * 1.125F);
        double boatZ = world.z() + (double) ((float) direction.getStepZ() * 1.125F);
        BlockPos pos = world.getPos().relative(direction);
        double offset;
        if (level.getFluidState(pos).is(FluidTags.WATER)) {
            offset = 1.0;
        } else {
            if (!level.getBlockState(pos).m_60795_() || !level.getFluidState(pos.below()).is(FluidTags.WATER)) {
                return this.defaultDispenseItemBehavior.dispense(world, stack);
            }
            offset = 0.0;
        }
        Boat boat = (Boat) (this.chest ? new QuarkChestBoat(level, boatX, boatY + offset, boatZ) : new QuarkBoat(level, boatX, boatY + offset, boatZ));
        ((IQuarkBoat) boat).setQuarkBoatTypeObj(WoodSetHandler.getQuarkBoatType(this.type));
        boat.m_146922_(direction.toYRot());
        level.m_7967_(boat);
        stack.shrink(1);
        return stack;
    }

    @Override
    protected void playSound(BlockSource world) {
        world.getLevel().m_46796_(1000, world.getPos(), 0);
    }
}