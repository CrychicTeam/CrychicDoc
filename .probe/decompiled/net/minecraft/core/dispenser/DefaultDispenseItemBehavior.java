package net.minecraft.core.dispenser;

import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class DefaultDispenseItemBehavior implements DispenseItemBehavior {

    @Override
    public final ItemStack dispense(BlockSource blockSource0, ItemStack itemStack1) {
        ItemStack $$2 = this.execute(blockSource0, itemStack1);
        this.playSound(blockSource0);
        this.playAnimation(blockSource0, (Direction) blockSource0.getBlockState().m_61143_(DispenserBlock.FACING));
        return $$2;
    }

    protected ItemStack execute(BlockSource blockSource0, ItemStack itemStack1) {
        Direction $$2 = (Direction) blockSource0.getBlockState().m_61143_(DispenserBlock.FACING);
        Position $$3 = DispenserBlock.getDispensePosition(blockSource0);
        ItemStack $$4 = itemStack1.split(1);
        spawnItem(blockSource0.getLevel(), $$4, 6, $$2, $$3);
        return itemStack1;
    }

    public static void spawnItem(Level level0, ItemStack itemStack1, int int2, Direction direction3, Position position4) {
        double $$5 = position4.x();
        double $$6 = position4.y();
        double $$7 = position4.z();
        if (direction3.getAxis() == Direction.Axis.Y) {
            $$6 -= 0.125;
        } else {
            $$6 -= 0.15625;
        }
        ItemEntity $$8 = new ItemEntity(level0, $$5, $$6, $$7, itemStack1);
        double $$9 = level0.random.nextDouble() * 0.1 + 0.2;
        $$8.m_20334_(level0.random.triangle((double) direction3.getStepX() * $$9, 0.0172275 * (double) int2), level0.random.triangle(0.2, 0.0172275 * (double) int2), level0.random.triangle((double) direction3.getStepZ() * $$9, 0.0172275 * (double) int2));
        level0.m_7967_($$8);
    }

    protected void playSound(BlockSource blockSource0) {
        blockSource0.getLevel().m_46796_(1000, blockSource0.getPos(), 0);
    }

    protected void playAnimation(BlockSource blockSource0, Direction direction1) {
        blockSource0.getLevel().m_46796_(2000, blockSource0.getPos(), direction1.get3DDataValue());
    }
}