package org.violetmoon.quark.content.tweaks.client.item;

import java.util.Optional;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.content.tweaks.module.CompassesWorkEverywhereModule;
import org.violetmoon.zeta.util.ItemNBTHelper;

@OnlyIn(Dist.CLIENT)
public class CompassAnglePropertyFunction implements ItemPropertyFunction {

    private final CompassAnglePropertyFunction.Angle normalAngle = new CompassAnglePropertyFunction.Angle();

    private final CompassAnglePropertyFunction.Angle unknownAngle = new CompassAnglePropertyFunction.Angle();

    @OnlyIn(Dist.CLIENT)
    @Override
    public float call(@NotNull ItemStack stack, @Nullable ClientLevel worldIn, @Nullable LivingEntity entityIn, int id) {
        if (entityIn == null && !stack.isFramed()) {
            return 0.0F;
        } else if (!CompassesWorkEverywhereModule.enableCompassNerf || stack.hasTag() && ItemNBTHelper.getBoolean(stack, "quark:compass_calculated", false)) {
            boolean carried = entityIn != null;
            Entity entity = (Entity) (carried ? entityIn : stack.getFrame());
            if (entity == null) {
                return 0.0F;
            } else {
                if (worldIn == null && entity != null && entity.level() instanceof ClientLevel level) {
                    worldIn = level;
                }
                boolean calculate = false;
                BlockPos target = new BlockPos(0, 0, 0);
                ResourceLocation dimension = worldIn.m_46472_().location();
                boolean isLodestone = CompassItem.isLodestoneCompass(stack);
                BlockPos lodestonePos = isLodestone ? this.getLodestonePosition(worldIn, stack.getOrCreateTag()) : null;
                if (lodestonePos != null) {
                    calculate = true;
                    target = lodestonePos;
                } else if (!isLodestone) {
                    if (dimension.equals(LevelStem.END.location()) && CompassesWorkEverywhereModule.enableEnd) {
                        calculate = true;
                    } else if (dimension.equals(LevelStem.NETHER.location()) && CompassesWorkEverywhereModule.isCompassCalculated(stack) && CompassesWorkEverywhereModule.enableNether) {
                        boolean set = ItemNBTHelper.getBoolean(stack, "quark:compass_position_set", false);
                        if (set) {
                            int x = ItemNBTHelper.getInt(stack, "quark:nether_x", 0);
                            int z = ItemNBTHelper.getInt(stack, "quark:nether_z", 0);
                            calculate = true;
                            target = new BlockPos(x, 0, z);
                        }
                    } else if (worldIn.m_6042_().natural()) {
                        calculate = true;
                        target = this.getWorldSpawn(worldIn);
                    }
                }
                long gameTime = worldIn.m_46467_();
                double angle;
                if (calculate && target != null) {
                    double d1 = carried ? (double) entity.getYRot() : this.getFrameRotation((ItemFrame) entity);
                    d1 = Mth.positiveModulo(d1 / 360.0, 1.0);
                    double d2 = this.getAngleToPosition(entity, target) / (Math.PI * 2);
                    if (carried) {
                        if (this.normalAngle.needsUpdate(gameTime)) {
                            this.normalAngle.wobble(gameTime, 0.5 - (d1 - 0.25));
                        }
                        angle = d2 + this.normalAngle.rotation;
                    } else {
                        angle = 0.5 - (d1 - 0.25 - d2);
                    }
                } else {
                    if (this.unknownAngle.needsUpdate(gameTime)) {
                        this.unknownAngle.wobble(gameTime, Math.random());
                    }
                    angle = this.unknownAngle.rotation + (double) this.shift(id);
                }
                return Mth.positiveModulo((float) angle, 1.0F);
            }
        } else {
            return 0.0F;
        }
    }

    private double getFrameRotation(ItemFrame frame) {
        return (double) Mth.wrapDegrees(180.0F + frame.m_6350_().toYRot());
    }

    private double getAngleToPosition(Entity entity, BlockPos blockpos) {
        Vec3 pos = entity.position();
        return Math.atan2((double) blockpos.m_123343_() - pos.z, (double) blockpos.m_123341_() - pos.x);
    }

    private float shift(int id) {
        return (float) (id * 1327217883) / 2.1474836E9F;
    }

    @Nullable
    private BlockPos getLodestonePosition(Level world, CompoundTag tag) {
        boolean flag = tag.contains("LodestonePos");
        boolean flag1 = tag.contains("LodestoneDimension");
        if (flag && flag1) {
            Optional<ResourceKey<Level>> optional = CompassItem.getLodestoneDimension(tag);
            if (optional.isPresent() && world.dimension().equals(optional.get())) {
                return NbtUtils.readBlockPos(tag.getCompound("LodestonePos"));
            }
        }
        return null;
    }

    @Nullable
    private BlockPos getWorldSpawn(ClientLevel world) {
        return world.m_6042_().natural() ? world.m_220360_() : null;
    }

    @OnlyIn(Dist.CLIENT)
    private static class Angle {

        private double rotation;

        private double rota;

        private long lastUpdateTick;

        private boolean needsUpdate(long tick) {
            return this.lastUpdateTick != tick;
        }

        private void wobble(long gameTime, double angle) {
            this.lastUpdateTick = gameTime;
            double d0 = angle - this.rotation;
            d0 = Mth.positiveModulo(d0 + 0.5, 1.0) - 0.5;
            this.rota += d0 * 0.1;
            this.rota *= 0.8;
            this.rotation = Mth.positiveModulo(this.rotation + this.rota, 1.0);
        }
    }
}