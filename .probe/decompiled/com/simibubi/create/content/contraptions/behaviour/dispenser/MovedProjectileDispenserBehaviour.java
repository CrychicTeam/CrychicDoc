package com.simibubi.create.content.contraptions.behaviour.dispenser;

import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.foundation.mixin.accessor.AbstractProjectileDispenseBehaviorAccessor;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;

public abstract class MovedProjectileDispenserBehaviour extends MovedDefaultDispenseItemBehaviour {

    @Override
    protected ItemStack dispenseStack(ItemStack itemStack, MovementContext context, BlockPos pos, Vec3 facing) {
        double x = (double) pos.m_123341_() + facing.x * 0.7 + 0.5;
        double y = (double) pos.m_123342_() + facing.y * 0.7 + 0.5;
        double z = (double) pos.m_123343_() + facing.z * 0.7 + 0.5;
        Projectile projectile = this.getProjectileEntity(context.world, x, y, z, itemStack.copy());
        if (projectile == null) {
            return itemStack;
        } else {
            Vec3 effectiveMovementVec = facing.scale((double) this.getProjectileVelocity()).add(context.motion);
            projectile.shoot(effectiveMovementVec.x, effectiveMovementVec.y, effectiveMovementVec.z, (float) effectiveMovementVec.length(), this.getProjectileInaccuracy());
            context.world.m_7967_(projectile);
            itemStack.shrink(1);
            return itemStack;
        }
    }

    @Override
    protected void playDispenseSound(LevelAccessor world, BlockPos pos) {
        world.levelEvent(1002, pos, 0);
    }

    @Nullable
    protected abstract Projectile getProjectileEntity(Level var1, double var2, double var4, double var6, ItemStack var8);

    protected float getProjectileInaccuracy() {
        return 6.0F;
    }

    protected float getProjectileVelocity() {
        return 1.1F;
    }

    public static MovedProjectileDispenserBehaviour of(AbstractProjectileDispenseBehavior vanillaBehaviour) {
        final AbstractProjectileDispenseBehaviorAccessor accessor = (AbstractProjectileDispenseBehaviorAccessor) vanillaBehaviour;
        return new MovedProjectileDispenserBehaviour() {

            @Override
            protected Projectile getProjectileEntity(Level world, double x, double y, double z, ItemStack itemStack) {
                return accessor.create$callGetProjectile(world, new SimplePos(x, y, z), itemStack);
            }

            @Override
            protected float getProjectileInaccuracy() {
                return accessor.create$callGetUncertainty();
            }

            @Override
            protected float getProjectileVelocity() {
                return accessor.create$callGetPower();
            }
        };
    }
}