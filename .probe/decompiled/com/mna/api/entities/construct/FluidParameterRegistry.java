package com.mna.api.entities.construct;

import com.mna.api.ManaAndArtificeMod;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.effects.MAEffects;
import com.mna.api.events.construct.FluidParameterRegistrationEvent;
import com.mna.api.faction.FactionIDs;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import java.util.HashMap;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import org.apache.commons.lang3.function.TriFunction;

@EventBusSubscriber(modid = "mna", bus = Bus.MOD)
public class FluidParameterRegistry {

    Function<Vec3, Vec3> ADJUST_DEFAULT = vel -> vel.yRot((float) (-0.25 * Math.random() + 0.5)).normalize().scale(0.35F);

    HashMap<Fluid, FluidParameterRegistry.FluidParameter> __registry = new HashMap();

    public static final FluidParameterRegistry INSTANCE = new FluidParameterRegistry();

    public void registerDefaults() {
        this.__registry.put(Fluids.EMPTY, new FluidParameterRegistry.FluidParameter(Fluids.EMPTY, false, false, SFX.Entity.Construct.SPRAY_GENERIC, new MAParticleType(ParticleInit.SPARKLE_VELOCITY.get()), 0.35F, 10, null, this.ADJUST_DEFAULT));
        this.__registry.put(Fluids.WATER, new FluidParameterRegistry.FluidParameter(Fluids.WATER, true, true, SFX.Entity.Construct.SPRAY_GENERIC, ParticleTypes.SPLASH, 0.5F, 20, (living, isFriendly, construct) -> {
            if (living == null) {
                return false;
            } else if (!isFriendly) {
                return living instanceof Creeper && ((Creeper) living).m_21023_(MAEffects.SOAKED) ? false : true;
            } else {
                if (living instanceof Player && !((Player) living).isCreative() && construct.getIntelligence() >= 16) {
                    IPlayerProgression progression = (IPlayerProgression) living.getCapability(ManaAndArtificeMod.getProgressionCapability()).orElse(null);
                    MobEffectInstance soaked = ((Player) living).m_21124_(MAEffects.SOAKED);
                    if (progression != null && progression.getAlliedFaction() != null && progression.getAlliedFaction().is(FactionIDs.UNDEAD) && living.level().m_45527_(living.blockPosition()) && (soaked == null || soaked.getDuration() < 20)) {
                        return true;
                    }
                }
                return living.isOnFire();
            }
        }, vel -> {
            Vec3 adjusted = vel.yRot((float) (-0.25 + Math.random() * 0.5)).normalize().scale(0.75 + Math.random() * 0.25);
            return adjusted.subtract(0.0, adjusted.y, 0.0);
        }));
        this.__registry.put(Fluids.LAVA, new FluidParameterRegistry.FluidParameter(Fluids.LAVA, false, true, SFX.Entity.Construct.SPRAY_FIRE, new MAParticleType(ParticleInit.FLAME.get()).setScale(0.13F).setPhysics(true), 0.35F, 10, null, null));
        this.__registry.put(ForgeMod.MILK.get(), new FluidParameterRegistry.FluidParameter(ForgeMod.MILK.get(), true, true, SFX.Entity.Construct.SPRAY_GENERIC, new MAParticleType(ParticleInit.DUST.get()).setScale(0.15F).setColor(250, 250, 250), 0.35F, 10, (entity, isFriendly, construct) -> {
            if (entity instanceof LivingEntity living) {
                return isFriendly ? living.getActiveEffects().stream().anyMatch(effect -> !effect.getEffect().isBeneficial() && effect.isCurativeItem(new ItemStack(Items.MILK_BUCKET))) : living.getActiveEffects().stream().anyMatch(effect -> effect.getEffect().isBeneficial() && effect.isCurativeItem(new ItemStack(Items.MILK_BUCKET)));
            } else {
                return false;
            }
        }, null));
    }

    public void register(FluidParameterRegistry.FluidParameter parameter) {
        this.__registry.put(parameter.fluid, parameter);
    }

    public FluidParameterRegistry.FluidParameter forFluid(Fluid fluid) {
        return (FluidParameterRegistry.FluidParameter) this.__registry.getOrDefault(fluid, (FluidParameterRegistry.FluidParameter) this.__registry.get(Fluids.EMPTY));
    }

    @SubscribeEvent
    public static void onLoadComplete(FMLLoadCompleteEvent event) {
        INSTANCE.registerDefaults();
        FluidParameterRegistrationEvent evt = new FluidParameterRegistrationEvent(INSTANCE);
        MinecraftForge.EVENT_BUS.post(evt);
    }

    public static class FluidParameter {

        final boolean targetsFriendly;

        final boolean targetsHostile;

        final Fluid fluid;

        final SoundEvent spraySound;

        @Nullable
        final TriFunction<Entity, Boolean, IConstruct<?>, Boolean> targetPredicate;

        final Function<Vec3, Vec3> adjustParticleVelocity;

        final ParticleOptions fluidParticle;

        final float particleVelocityScale;

        final int particleQuantity;

        public FluidParameter(Fluid forFluid, boolean targetAllies, boolean targetEnemies, SoundEvent spraySound, ParticleOptions type, float particleVelocityScale, int particleQuantity, @Nullable TriFunction<Entity, Boolean, IConstruct<?>, Boolean> targetPredicate, Function<Vec3, Vec3> adjustParticleVelocity) {
            this.fluid = forFluid;
            this.targetsFriendly = targetAllies;
            this.targetsHostile = targetEnemies;
            this.spraySound = spraySound;
            this.targetPredicate = targetPredicate;
            this.fluidParticle = type;
            this.particleVelocityScale = particleVelocityScale;
            this.adjustParticleVelocity = adjustParticleVelocity;
            this.particleQuantity = particleQuantity;
        }

        public boolean targetsFriendly() {
            return this.targetsFriendly;
        }

        public boolean targetsHostile() {
            return this.targetsHostile;
        }

        public Fluid fluid() {
            return this.fluid;
        }

        public boolean checkPredicate(Entity e, boolean isFriendly, IConstruct<?> construct) {
            if (this.targetPredicate == null) {
                return isFriendly ? this.targetsFriendly : this.targetsHostile;
            } else {
                return (Boolean) this.targetPredicate.apply(e, isFriendly, construct);
            }
        }

        public ParticleOptions particle() {
            return this.fluidParticle;
        }

        public SoundEvent spraySound() {
            return this.spraySound;
        }

        public float getParticleVelocityScale() {
            return this.particleVelocityScale;
        }

        public Vec3 adjustVelocity(Vec3 vel) {
            return this.adjustParticleVelocity != null ? (Vec3) this.adjustParticleVelocity.apply(vel) : vel;
        }

        public int getParticleQuantity() {
            return this.particleQuantity;
        }
    }
}