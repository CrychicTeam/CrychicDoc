package net.minecraft.core.dispenser;

import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public abstract class AbstractProjectileDispenseBehavior extends DefaultDispenseItemBehavior {

    @Override
    public ItemStack execute(BlockSource blockSource0, ItemStack itemStack1) {
        Level $$2 = blockSource0.getLevel();
        Position $$3 = DispenserBlock.getDispensePosition(blockSource0);
        Direction $$4 = (Direction) blockSource0.getBlockState().m_61143_(DispenserBlock.FACING);
        Projectile $$5 = this.getProjectile($$2, $$3, itemStack1);
        $$5.shoot((double) $$4.getStepX(), (double) ((float) $$4.getStepY() + 0.1F), (double) $$4.getStepZ(), this.getPower(), this.getUncertainty());
        $$2.m_7967_($$5);
        itemStack1.shrink(1);
        return itemStack1;
    }

    @Override
    protected void playSound(BlockSource blockSource0) {
        blockSource0.getLevel().m_46796_(1002, blockSource0.getPos(), 0);
    }

    protected abstract Projectile getProjectile(Level var1, Position var2, ItemStack var3);

    protected float getUncertainty() {
        return 6.0F;
    }

    protected float getPower() {
        return 1.1F;
    }
}