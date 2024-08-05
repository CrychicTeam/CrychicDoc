package io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob;

import com.google.common.collect.Maps;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.spells.ender.TeleportSpell;
import io.redspace.ironsspellbooks.spells.fire.BurningDashSpell;
import java.util.HashMap;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class AbstractSpellCastingMob extends PathfinderMob implements GeoEntity, IMagicEntity {

    public static final ResourceLocation modelResource = new ResourceLocation("irons_spellbooks", "geo/abstract_casting_mob.geo.json");

    public static final ResourceLocation textureResource = new ResourceLocation("irons_spellbooks", "textures/entity/abstract_casting_mob/abstract_casting_mob.png");

    public static final ResourceLocation animationInstantCast = new ResourceLocation("irons_spellbooks", "animations/casting_animations.json");

    private static final EntityDataAccessor<Boolean> DATA_CANCEL_CAST = SynchedEntityData.defineId(AbstractSpellCastingMob.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> DATA_DRINKING_POTION = SynchedEntityData.defineId(AbstractSpellCastingMob.class, EntityDataSerializers.BOOLEAN);

    private final MagicData playerMagicData = new MagicData(true);

    private static final AttributeModifier SPEED_MODIFIER_DRINKING = new AttributeModifier(UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E"), "Drinking speed penalty", -0.15, AttributeModifier.Operation.MULTIPLY_TOTAL);

    @Nullable
    private SpellData castingSpell;

    private final HashMap<String, AbstractSpell> spells = Maps.newHashMap();

    private int drinkTime;

    public boolean hasUsedSingleAttack;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private AbstractSpell lastCastSpellType = SpellRegistry.none();

    private AbstractSpell instantCastSpellType = SpellRegistry.none();

    private boolean cancelCastAnimation = false;

    private boolean animatingLegs = false;

    private final RawAnimation idle = RawAnimation.begin().thenLoop("blank");

    private final AnimationController animationControllerOtherCast = new AnimationController<>(this, "other_casting", 0, this::otherCastingPredicate);

    private final AnimationController animationControllerInstantCast = new AnimationController<>(this, "instant_casting", 0, this::instantCastingPredicate);

    private final AnimationController animationControllerLongCast = new AnimationController<>(this, "long_casting", 0, this::longCastingPredicate);

    protected AbstractSpellCastingMob(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.playerMagicData.setSyncedData(new SyncedSpellData(this));
        this.f_21365_ = this.createLookControl();
    }

    @Override
    public boolean getHasUsedSingleAttack() {
        return this.hasUsedSingleAttack;
    }

    @Override
    public void setHasUsedSingleAttack(boolean hasUsedSingleAttack) {
        this.hasUsedSingleAttack = hasUsedSingleAttack;
    }

    @Override
    public double getMyRidingOffset() {
        return -0.5;
    }

    @Override
    public void rideTick() {
        super.m_6083_();
        if (this.m_20202_() instanceof PathfinderMob pathfindermob) {
            pathfindermob.f_20883_ = this.f_20883_;
        }
    }

    protected LookControl createLookControl() {
        return new LookControl(this) {

            @Override
            protected boolean resetXRotOnTick() {
                return AbstractSpellCastingMob.this.m_5448_() == null;
            }
        };
    }

    @Override
    public MagicData getMagicData() {
        return this.playerMagicData;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_CANCEL_CAST, false);
        this.f_19804_.define(DATA_DRINKING_POTION, false);
    }

    @Override
    public boolean isDrinkingPotion() {
        return this.f_19804_.get(DATA_DRINKING_POTION);
    }

    protected void setDrinkingPotion(boolean drinkingPotion) {
        this.f_19804_.set(DATA_DRINKING_POTION, drinkingPotion);
    }

    @Override
    public boolean canBeLeashed(Player pPlayer) {
        return false;
    }

    @Override
    public void startDrinkingPotion() {
        if (!this.f_19853_.isClientSide) {
            this.setDrinkingPotion(true);
            this.drinkTime = 35;
            AttributeInstance attributeinstance = this.m_21051_(Attributes.MOVEMENT_SPEED);
            attributeinstance.removeModifier(SPEED_MODIFIER_DRINKING);
            attributeinstance.addTransientModifier(SPEED_MODIFIER_DRINKING);
        }
    }

    private void finishDrinkingPotion() {
        this.setDrinkingPotion(false);
        this.m_5634_(Math.min(Math.max(10.0F, this.m_21233_() / 10.0F), this.m_21233_() / 4.0F));
        this.m_21051_(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_DRINKING);
        if (!this.m_20067_()) {
            this.f_19853_.playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.WITCH_DRINK, this.m_5720_(), 1.0F, 0.8F + this.f_19796_.nextFloat() * 0.4F);
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        super.m_7350_(pKey);
        if (this.f_19853_.isClientSide) {
            if (pKey.getId() == DATA_CANCEL_CAST.getId()) {
                this.cancelCast();
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.m_7380_(pCompound);
        this.playerMagicData.getSyncedData().saveNBTData(pCompound);
        pCompound.putBoolean("usedSpecial", this.hasUsedSingleAttack);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.m_7378_(pCompound);
        SyncedSpellData syncedSpellData = new SyncedSpellData(this);
        syncedSpellData.loadNBTData(pCompound);
        if (syncedSpellData.isCasting()) {
            AbstractSpell spell = SpellRegistry.getSpell(syncedSpellData.getCastingSpellId());
            this.initiateCastSpell(spell, syncedSpellData.getCastingSpellLevel());
        }
        this.playerMagicData.setSyncedData(syncedSpellData);
        this.hasUsedSingleAttack = pCompound.getBoolean("usedSpecial");
    }

    @Override
    public void cancelCast() {
        if (this.isCasting()) {
            if (this.f_19853_.isClientSide) {
                this.cancelCastAnimation = true;
            } else {
                this.f_19804_.set(DATA_CANCEL_CAST, !this.f_19804_.get(DATA_CANCEL_CAST));
            }
            this.castComplete();
        }
    }

    @Override
    public void castComplete() {
        if (!this.f_19853_.isClientSide) {
            if (this.castingSpell != null) {
                this.castingSpell.getSpell().onServerCastComplete(this.f_19853_, this.castingSpell.getLevel(), this, this.playerMagicData, false);
            }
        } else {
            this.playerMagicData.resetCastingState();
        }
        this.castingSpell = null;
    }

    public void startAutoSpinAttack(int pAttackTicks) {
        this.f_20938_ = pAttackTicks;
        if (!this.f_19853_.isClientSide) {
            this.m_21155_(4, true);
        }
        this.m_146922_((float) (Math.atan2(this.m_20184_().x, this.m_20184_().z) * 180.0F / (float) Math.PI));
    }

    @Override
    public void setSyncedSpellData(SyncedSpellData syncedSpellData) {
        if (this.f_19853_.isClientSide) {
            boolean isCasting = this.playerMagicData.isCasting();
            this.playerMagicData.setSyncedData(syncedSpellData);
            this.castingSpell = this.playerMagicData.getCastingSpell();
            if (this.castingSpell != null) {
                if (!this.playerMagicData.isCasting() && isCasting) {
                    this.castComplete();
                } else if (this.playerMagicData.isCasting() && !isCasting) {
                    AbstractSpell spell = this.playerMagicData.getCastingSpell().getSpell();
                    this.initiateCastSpell(spell, this.playerMagicData.getCastingSpellLevel());
                    if (this.castingSpell.getSpell().getCastType() == CastType.INSTANT) {
                        this.instantCastSpellType = this.castingSpell.getSpell();
                        this.castingSpell.getSpell().onClientPreCast(this.f_19853_, this.castingSpell.getLevel(), this, InteractionHand.MAIN_HAND, this.playerMagicData);
                        this.castComplete();
                    }
                }
            }
        }
    }

    @Override
    protected void customServerAiStep() {
        super.m_8024_();
        if (this.isDrinkingPotion()) {
            if (this.drinkTime-- <= 0) {
                this.finishDrinkingPotion();
            } else if (this.drinkTime % 4 == 0 && !this.m_20067_()) {
                this.f_19853_.playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.GENERIC_DRINK, this.m_5720_(), 1.0F, Utils.random.nextFloat() * 0.1F + 0.9F);
            }
        }
        if (this.castingSpell != null) {
            this.playerMagicData.handleCastDuration();
            if (this.playerMagicData.isCasting()) {
                this.castingSpell.getSpell().onServerCastTick(this.f_19853_, this.castingSpell.getLevel(), this, this.playerMagicData);
            }
            this.forceLookAtTarget(this.m_5448_());
            if (this.playerMagicData.getCastDurationRemaining() <= 0) {
                if (this.castingSpell.getSpell().getCastType() == CastType.LONG || this.castingSpell.getSpell().getCastType() == CastType.INSTANT) {
                    this.castingSpell.getSpell().onCast(this.f_19853_, this.castingSpell.getLevel(), this, CastSource.MOB, this.playerMagicData);
                }
                this.castComplete();
            } else if (this.castingSpell.getSpell().getCastType() == CastType.CONTINUOUS && (this.playerMagicData.getCastDurationRemaining() + 1) % 10 == 0) {
                this.castingSpell.getSpell().onCast(this.f_19853_, this.castingSpell.getLevel(), this, CastSource.MOB, this.playerMagicData);
            }
        }
    }

    @Override
    public void initiateCastSpell(AbstractSpell spell, int spellLevel) {
        if (spell == SpellRegistry.none()) {
            this.castingSpell = null;
        } else {
            if (this.f_19853_.isClientSide) {
                this.cancelCastAnimation = false;
            }
            this.castingSpell = new SpellData(spell, spellLevel);
            if (this.m_5448_() != null) {
                this.forceLookAtTarget(this.m_5448_());
            }
            if (!this.f_19853_.isClientSide && !this.castingSpell.getSpell().checkPreCastConditions(this.f_19853_, spellLevel, this, this.playerMagicData)) {
                this.castingSpell = null;
            } else {
                if (spell == SpellRegistry.TELEPORT_SPELL.get() || spell == SpellRegistry.FROST_STEP_SPELL.get()) {
                    this.setTeleportLocationBehindTarget(10);
                } else if (spell == SpellRegistry.BLOOD_STEP_SPELL.get()) {
                    this.setTeleportLocationBehindTarget(3);
                } else if (spell == SpellRegistry.BURNING_DASH_SPELL.get()) {
                    this.setBurningDashDirectionData();
                }
                this.playerMagicData.initiateCast(this.castingSpell.getSpell(), this.castingSpell.getLevel(), this.castingSpell.getSpell().getEffectiveCastTime(this.castingSpell.getLevel(), this), CastSource.MOB, SpellSelectionManager.MAINHAND);
                if (!this.f_19853_.isClientSide) {
                    this.castingSpell.getSpell().onServerPreCast(this.f_19853_, this.castingSpell.getLevel(), this, this.playerMagicData);
                }
            }
        }
    }

    @Override
    public void notifyDangerousProjectile(Projectile projectile) {
    }

    @Override
    public boolean isCasting() {
        return this.playerMagicData.isCasting();
    }

    @Override
    public boolean setTeleportLocationBehindTarget(int distance) {
        LivingEntity target = this.m_5448_();
        boolean valid = false;
        if (target != null) {
            Vec3 rotation = target.m_20154_().normalize().scale((double) (-distance));
            Vec3 pos = target.m_20182_();
            Vec3 teleportPos = rotation.add(pos);
            for (int i = 0; i < 24; i++) {
                Vec3 randomness = Utils.getRandomVec3((double) (0.15F * (float) i)).multiply(1.0, 0.0, 1.0);
                Vec3 var10 = Utils.moveToRelativeGroundLevel(this.f_19853_, target.m_20182_().subtract(new Vec3(0.0, 0.0, (double) ((float) distance / (float) (i / 7 + 1))).yRot(-(target.m_146908_() + (float) (i * 45)) * (float) (Math.PI / 180.0))).add(randomness), 5);
                teleportPos = new Vec3(var10.x, var10.y + 0.1F, var10.z);
                AABB reposBB = this.m_20191_().move(teleportPos.subtract(this.m_20182_()));
                if (!this.f_19853_.m_186437_(this, reposBB.inflate(-0.05F))) {
                    valid = true;
                    break;
                }
            }
            if (valid) {
                this.playerMagicData.setAdditionalCastData(new TeleportSpell.TeleportData(teleportPos));
            } else {
                this.playerMagicData.setAdditionalCastData(new TeleportSpell.TeleportData(this.m_20182_()));
            }
        } else {
            this.playerMagicData.setAdditionalCastData(new TeleportSpell.TeleportData(this.m_20182_()));
        }
        return valid;
    }

    @Override
    public void setBurningDashDirectionData() {
        this.playerMagicData.setAdditionalCastData(new BurningDashSpell.BurningDashDirectionOverrideCastData());
    }

    private void forceLookAtTarget(LivingEntity target) {
        if (target != null) {
            double d0 = target.m_20185_() - this.m_20185_();
            double d2 = target.m_20189_() - this.m_20189_();
            double d1 = target.m_20188_() - this.m_20188_();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            float f = (float) (Mth.atan2(d2, d0) * 180.0F / (float) Math.PI) - 90.0F;
            float f1 = (float) (-(Mth.atan2(d1, d3) * 180.0F / (float) Math.PI));
            this.m_146926_(f1 % 360.0F);
            this.m_146922_(f % 360.0F);
        }
    }

    private void addClientSideParticles() {
        double d0 = 0.4;
        double d1 = 0.3;
        double d2 = 0.35;
        float f = this.f_20883_ * (float) (Math.PI / 180.0) + Mth.cos((float) this.f_19797_ * 0.6662F) * 0.25F;
        float f1 = Mth.cos(f);
        float f2 = Mth.sin(f);
        this.f_19853_.addParticle(ParticleTypes.ENTITY_EFFECT, this.m_20185_() + (double) f1 * 0.6, this.m_20186_() + 1.8, this.m_20189_() + (double) f2 * 0.6, d0, d1, d2);
        this.f_19853_.addParticle(ParticleTypes.ENTITY_EFFECT, this.m_20185_() - (double) f1 * 0.6, this.m_20186_() + 1.8, this.m_20189_() - (double) f2 * 0.6, d0, d1, d2);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void triggerAnim(@org.jetbrains.annotations.Nullable String controllerName, String animName) {
        GeoEntity.super.triggerAnim(controllerName, animName);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(this.animationControllerOtherCast);
        controllerRegistrar.add(this.animationControllerInstantCast);
        controllerRegistrar.add(this.animationControllerLongCast);
        controllerRegistrar.add(new AnimationController<>(this, "idle", 0, this::idlePredicate));
    }

    private PlayState idlePredicate(AnimationState event) {
        event.getController().setAnimation(this.idle);
        return PlayState.STOP;
    }

    private PlayState instantCastingPredicate(AnimationState event) {
        if (this.cancelCastAnimation) {
            return PlayState.STOP;
        } else {
            AnimationController controller = event.getController();
            if (this.instantCastSpellType != SpellRegistry.none() && controller.getAnimationState() == AnimationController.State.STOPPED) {
                this.setStartAnimationFromSpell(controller, this.instantCastSpellType);
                this.instantCastSpellType = SpellRegistry.none();
            }
            return PlayState.CONTINUE;
        }
    }

    private PlayState longCastingPredicate(AnimationState event) {
        AnimationController controller = event.getController();
        if (!this.cancelCastAnimation && (controller.getAnimationState() != AnimationController.State.STOPPED || this.isCasting() && this.castingSpell != null && this.castingSpell.getSpell().getCastType() == CastType.LONG)) {
            if (this.isCasting()) {
                if (controller.getAnimationState() == AnimationController.State.STOPPED) {
                    this.setStartAnimationFromSpell(controller, this.castingSpell.getSpell());
                }
            } else if (this.lastCastSpellType.getCastType() == CastType.LONG) {
                this.setFinishAnimationFromSpell(controller, this.lastCastSpellType);
            }
            return PlayState.CONTINUE;
        } else {
            return PlayState.STOP;
        }
    }

    private PlayState otherCastingPredicate(AnimationState event) {
        if (this.cancelCastAnimation) {
            return PlayState.STOP;
        } else {
            AnimationController controller = event.getController();
            if (this.isCasting() && this.castingSpell != null && controller.getAnimationState() == AnimationController.State.STOPPED) {
                if (this.castingSpell.getSpell().getCastType() == CastType.CONTINUOUS) {
                    this.setStartAnimationFromSpell(controller, this.castingSpell.getSpell());
                }
                return PlayState.CONTINUE;
            } else {
                return this.isCasting() ? PlayState.CONTINUE : PlayState.STOP;
            }
        }
    }

    private void setStartAnimationFromSpell(AnimationController controller, AbstractSpell spell) {
        spell.getCastStartAnimation().getForMob().ifPresentOrElse(animationBuilder -> {
            controller.forceAnimationReset();
            controller.setAnimation(animationBuilder);
            this.lastCastSpellType = spell;
            this.cancelCastAnimation = false;
            this.animatingLegs = spell.getCastStartAnimation().animatesLegs;
        }, () -> this.cancelCastAnimation = true);
    }

    private void setFinishAnimationFromSpell(AnimationController controller, AbstractSpell spell) {
        if (spell.getCastFinishAnimation().isPass) {
            this.cancelCastAnimation = false;
        } else {
            spell.getCastFinishAnimation().getForMob().ifPresentOrElse(animationBuilder -> {
                controller.forceAnimationReset();
                controller.setAnimation(animationBuilder);
                this.lastCastSpellType = SpellRegistry.none();
                this.cancelCastAnimation = false;
            }, () -> this.cancelCastAnimation = true);
        }
    }

    public boolean isAnimating() {
        return this.isCasting() || this.animationControllerLongCast.getAnimationState() != AnimationController.State.STOPPED || this.animationControllerOtherCast.getAnimationState() != AnimationController.State.STOPPED || this.animationControllerInstantCast.getAnimationState() != AnimationController.State.STOPPED;
    }

    public boolean shouldBeExtraAnimated() {
        return true;
    }

    public boolean shouldAlwaysAnimateHead() {
        return true;
    }

    public boolean shouldAlwaysAnimateLegs() {
        return !this.animatingLegs;
    }

    public boolean shouldPointArmsWhileCasting() {
        return true;
    }

    public boolean bobBodyWhileWalking() {
        return true;
    }

    public boolean shouldSheathSword() {
        return false;
    }
}