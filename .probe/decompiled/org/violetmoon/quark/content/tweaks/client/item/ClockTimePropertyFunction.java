package org.violetmoon.quark.content.tweaks.client.item;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.content.tweaks.module.CompassesWorkEverywhereModule;

@OnlyIn(Dist.CLIENT)
public class ClockTimePropertyFunction implements ItemPropertyFunction {

    private double rotation;

    private double rota;

    private long lastUpdateTick;

    @OnlyIn(Dist.CLIENT)
    @Override
    public float call(@NotNull ItemStack stack, @Nullable ClientLevel worldIn, @Nullable LivingEntity entityIn, int id) {
        if (!CompassesWorkEverywhereModule.isClockCalculated(stack)) {
            return 0.0F;
        } else {
            boolean carried = entityIn != null;
            Entity entity = (Entity) (carried ? entityIn : stack.getFrame());
            if (worldIn == null && entity != null && entity.level() instanceof ClientLevel) {
                worldIn = (ClientLevel) entity.level();
            }
            if (worldIn == null) {
                return 0.0F;
            } else {
                double angle;
                if (worldIn.m_6042_().natural()) {
                    angle = (double) worldIn.m_46942_(1.0F);
                } else {
                    angle = Math.random();
                }
                angle = this.wobble(worldIn, angle);
                return (float) angle;
            }
        }
    }

    private double wobble(Level world, double time) {
        long gameTime = world.getGameTime();
        if (gameTime != this.lastUpdateTick) {
            this.lastUpdateTick = gameTime;
            double d0 = time - this.rotation;
            d0 = Mth.positiveModulo(d0 + 0.5, 1.0) - 0.5;
            this.rota += d0 * 0.1;
            this.rota *= 0.9;
            this.rotation = Mth.positiveModulo(this.rotation + this.rota, 1.0);
        }
        return this.rotation;
    }
}