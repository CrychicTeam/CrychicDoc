package io.redspace.ironsspellbooks.entity.spells.void_tentacle;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Collections;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class VoidTentacle extends LivingEntity implements GeoEntity, AntiMagicSusceptible {

    @Nullable
    private LivingEntity owner;

    @Nullable
    private UUID ownerUUID;

    private float damage;

    private int age;

    private final RawAnimation ANIMATION_RISE = RawAnimation.begin().thenPlay("rise");

    private final RawAnimation ANIMATION_RETREAT = RawAnimation.begin().thenPlay("retreat");

    private final RawAnimation ANIMATION_FLAIL = RawAnimation.begin().thenPlay("flail");

    private final RawAnimation ANIMATION_FLAIL2 = RawAnimation.begin().thenPlay("flail2");

    private final RawAnimation ANIMATION_FLAIL3 = RawAnimation.begin().thenPlay("flail3");

    private final RawAnimation ANIMATION_IDLE = RawAnimation.begin().thenLoop("idle");

    private final AnimationController controller = new AnimationController<>(this, "void_tentacle_controller", 20, this::animationPredicate);

    private final AnimationController riseController = new AnimationController<>(this, "void_tentacle_rise_controller", 0, this::risePredicate);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public VoidTentacle(EntityType<? extends VoidTentacle> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public VoidTentacle(Level level, LivingEntity owner, float damage) {
        this(EntityRegistry.SCULK_TENTACLE.get(), level);
        this.setOwner(owner);
        this.setDamage(damage);
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.f_19853_.isClientSide) {
            if (this.age > 300) {
                this.m_146870_();
            } else if (this.age < 280 && this.age % 20 == 0) {
                this.f_19853_.m_45976_(LivingEntity.class, this.m_20191_().inflate(1.2)).forEach(this::dealDamage);
                if (Utils.random.nextFloat() < 0.15F) {
                    this.m_5496_(SoundRegistry.VOID_TENTACLES_AMBIENT.get(), 1.5F, 0.5F + Utils.random.nextFloat() * 0.65F);
                }
            }
            if (this.age == 260 && Utils.random.nextFloat() < 0.3F) {
                this.m_5496_(SoundRegistry.VOID_TENTACLES_LEAVE.get(), 2.0F, 1.0F);
            }
        } else if (this.age < 280 && Utils.random.nextFloat() < 0.15F) {
            this.f_19853_.addParticle(ParticleHelper.VOID_TENTACLE_FOG, this.m_20185_() + Utils.getRandomScaled(0.5), this.m_20186_() + Utils.getRandomScaled(0.5) + 0.2F, this.m_20189_() + Utils.getRandomScaled(0.5), Utils.getRandomScaled(2.0), (double) (-this.f_19796_.nextFloat() * 0.5F), Utils.getRandomScaled(2.0));
        }
        this.age++;
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    public boolean dealDamage(LivingEntity target) {
        if (target != this.getOwner() && DamageSources.applyDamage(target, this.damage, SpellRegistry.SCULK_TENTACLES_SPELL.get().getDamageSource(this, this.getOwner()))) {
            target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100));
            return true;
        } else {
            return false;
        }
    }

    public void setOwner(@Nullable LivingEntity pOwner) {
        this.owner = pOwner;
        this.ownerUUID = pOwner == null ? null : pOwner.m_20148_();
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return !pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) ? false : super.hurt(pSource, pAmount);
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.f_19853_ instanceof ServerLevel) {
            Entity entity = ((ServerLevel) this.f_19853_).getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity) entity;
            }
        }
        return this.owner;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.age = pCompound.getInt("Age");
        if (pCompound.hasUUID("Owner")) {
            this.ownerUUID = pCompound.getUUID("Owner");
        }
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.singleton(ItemStack.EMPTY);
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot pSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Age", this.age);
        if (this.ownerUUID != null) {
            pCompound.putUUID("Owner", this.ownerUUID);
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    @Override
    public void onAntiMagic(MagicData playerMagicData) {
        MagicManager.spawnParticles(this.f_19853_, ParticleTypes.SMOKE, this.m_20185_(), this.m_20186_() + 1.0, this.m_20189_(), 50, 0.2, 1.25, 0.2, 0.08, false);
        this.m_146870_();
    }

    private PlayState animationPredicate(AnimationState event) {
        AnimationController controller = event.getController();
        if (this.age > 220 && Utils.random.nextFloat() < 0.04F) {
            controller.setAnimation(this.ANIMATION_RETREAT);
        } else if (controller.getAnimationState() == AnimationController.State.STOPPED) {
            controller.setAnimation(this.ANIMATION_IDLE);
        }
        return PlayState.CONTINUE;
    }

    private PlayState risePredicate(AnimationState event) {
        AnimationController controller = event.getController();
        if (this.age < 10) {
            controller.setAnimation(this.ANIMATION_RISE);
            return PlayState.CONTINUE;
        } else {
            return PlayState.STOP;
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(this.riseController);
        controllerRegistrar.add(this.controller);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}