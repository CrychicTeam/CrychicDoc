package com.github.alexmodguy.alexscaves.server.block.fluid;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

public class AcidFluidType extends FluidType {

    public static final ResourceLocation FLUID_STILL = new ResourceLocation("alexscaves:block/acid_still");

    public static final ResourceLocation FLUID_FLOWING = new ResourceLocation("alexscaves:block/acid_flowing");

    public static final ResourceLocation OVERLAY = new ResourceLocation("alexscaves:textures/misc/under_acid.png");

    public AcidFluidType(FluidType.Properties properties) {
        super(properties);
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {

            @Override
            public ResourceLocation getStillTexture() {
                return AcidFluidType.FLUID_STILL;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return AcidFluidType.FLUID_FLOWING;
            }

            @Override
            public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                return AcidFluidType.OVERLAY;
            }
        });
    }

    @Override
    public boolean move(FluidState state, LivingEntity entity, Vec3 movementVector, double gravity) {
        double d9 = entity.m_20186_();
        float f4 = 0.8F;
        float f5 = 0.02F;
        float f6 = (float) EnchantmentHelper.getDepthStrider(entity);
        double d0 = 0.08;
        boolean flag = entity.m_20184_().y <= 0.0;
        if (f6 > 3.0F) {
            f6 = 3.0F;
        }
        if (!entity.m_20096_()) {
            f6 *= 0.5F;
        }
        if (f6 > 0.0F) {
            f4 += (0.54600006F - f4) * f6 / 3.0F;
            f5 += (entity.getSpeed() - f5) * f6 / 3.0F;
        }
        if (entity.hasEffect(MobEffects.DOLPHINS_GRACE)) {
            f4 = 0.96F;
        }
        f5 *= (float) entity.getAttribute(ForgeMod.SWIM_SPEED.get()).getValue();
        entity.m_19920_(f5, movementVector);
        entity.m_6478_(MoverType.SELF, entity.m_20184_());
        Vec3 vec36 = entity.m_20184_();
        if (entity.f_19862_ && entity.onClimbable()) {
            vec36 = new Vec3(vec36.x, 0.2, vec36.z);
        }
        entity.m_20256_(vec36.multiply((double) f4, 0.8F, (double) f4));
        Vec3 vec32 = entity.getFluidFallingAdjustedMovement(d0, flag, entity.m_20184_());
        entity.m_20256_(vec32);
        if (entity.f_19862_ && entity.m_20229_(vec32.x, vec32.y + 0.6F - entity.m_20186_() + d9, vec32.z)) {
            entity.m_20334_(vec32.x, 0.3F, vec32.z);
        }
        return true;
    }

    @Override
    public boolean isVaporizedOnPlacement(Level level, BlockPos pos, FluidStack stack) {
        return level.dimensionType().ultraWarm();
    }

    @Override
    public void onVaporize(@Nullable Player player, Level level, BlockPos pos, FluidStack stack) {
        SoundEvent sound = this.getSound(player, level, pos, SoundActions.FLUID_VAPORIZE);
        level.playSound(player, pos, sound != null ? sound : SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (level.random.nextFloat() - level.random.nextFloat()) * 0.8F);
        for (int l = 0; l < 8; l++) {
            level.addAlwaysVisibleParticle(ACParticleRegistry.RADGILL_SPLASH.get(), (double) pos.m_123341_() + Math.random(), (double) pos.m_123342_() + Math.random(), (double) pos.m_123343_() + Math.random(), (Math.random() - 0.5) * 0.25, Math.random() * 0.25, (Math.random() - 0.5) * 0.25);
        }
        level.setBlockAndUpdate(pos, ACBlockRegistry.UNREFINED_WASTE.get().defaultBlockState());
    }
}