package com.mna.entities.faction;

import com.mna.api.affinity.Affinity;
import com.mna.api.entities.DamageHelper;
import com.mna.api.entities.ai.CastSpellOnSelfGoal;
import com.mna.api.faction.IFaction;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.collections.Components;
import com.mna.api.spells.collections.Shapes;
import com.mna.effects.EffectInit;
import com.mna.entities.ai.FactionTierWrapperGoal;
import com.mna.entities.faction.base.BaseFlyingFactionMob;
import com.mna.factions.Factions;
import com.mna.items.ItemInit;
import com.mna.network.ServerMessageDispatcher;
import com.mna.spells.crafting.SpellRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class Pixie extends BaseFlyingFactionMob<Pixie> {

    private static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(Pixie.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> TELEPORTING = SynchedEntityData.defineId(Pixie.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> AFFINITY = SynchedEntityData.defineId(Pixie.class, EntityDataSerializers.INT);

    public Pixie(EntityType<Pixie> type, Level worldIn) {
        super(type, worldIn);
    }

    public static boolean canSpawnPredicate(EntityType<Pixie> type, ServerLevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        return worldIn.m_46791_() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(worldIn, pos, randomIn) && m_217057_(type, worldIn, reason, pos, randomIn);
    }

    @Override
    public void tick() {
        if (this.m_9236_().isClientSide()) {
            this.spawnParticles();
        }
        super.tick();
    }

    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (!this.m_9236_().isClientSide()) {
            this.pickRandomAffinity();
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SFX.Entity.Pixie.HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SFX.Entity.Pixie.DEATH;
    }

    @Override
    public IFaction getFaction() {
        return Factions.FEY;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.getAffinity() == Affinity.FIRE && source.is(DamageTypeTags.IS_FIRE)) {
            return false;
        } else if (this.getAffinity() == Affinity.WATER && source.is(DamageTypeTags.IS_DROWNING)) {
            return false;
        } else {
            if (this.getAffinity() == Affinity.EARTH && !source.is(DamageTypeTags.BYPASSES_ARMOR)) {
                amount /= 2.0F;
            }
            return super.hurt(source, amount);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(ATTACKING, false);
        this.f_19804_.define(TELEPORTING, false);
        this.f_19804_.define(AFFINITY, Affinity.WIND.ordinal());
    }

    public boolean isActing() {
        return this.isAttacking() || this.isTeleporting();
    }

    public boolean isAttacking() {
        return this.f_19804_.get(ATTACKING);
    }

    public boolean isTeleporting() {
        return this.f_19804_.get(TELEPORTING);
    }

    public void setAttacking(boolean attacking) {
        this.f_19804_.set(ATTACKING, attacking);
    }

    public void setTeleporting(boolean teleporting) {
        this.f_19804_.set(TELEPORTING, teleporting);
    }

    public Affinity getAffinity() {
        return Affinity.values()[this.f_19804_.get(AFFINITY)];
    }

    private void pickRandomAffinity() {
        Affinity[] validAffinities = new Affinity[] { Affinity.FIRE, Affinity.EARTH, Affinity.WATER, Affinity.WIND };
        this.f_19804_.set(AFFINITY, validAffinities[(int) (Math.random() * (double) validAffinities.length)].ordinal());
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        CastSpellOnSelfGoal<?> teleportGoal = new CastSpellOnSelfGoal<>(this, this.createBlinkSpell(), pix -> pix.m_5448_() != null && pix.m_5448_().m_20280_(pix) < 16.0, pix -> {
            pix.setTeleporting(true);
            pix.m_21195_(EffectInit.GRAVITY_WELL.get());
        }, pix -> pix.setTeleporting(false), 10);
        this.f_21345_.addGoal(6, new FactionTierWrapperGoal(2, this, teleportGoal));
        this.f_21345_.addGoal(7, new Pixie.BoltAttackGoal(this));
    }

    private ItemStack createBlinkSpell() {
        ItemStack invisSpell = new ItemStack(ItemInit.SPELL.get());
        SpellRecipe invis = new SpellRecipe();
        invis.setShape(Shapes.SELF);
        invis.addComponent(Components.BLINK);
        invis.changeComponentAttributeValue(0, Attribute.RANGE, 12.0F);
        invis.writeToNBT(invisSpell.getOrCreateTag());
        return invisSpell;
    }

    public static AttributeSupplier.Builder getGlobalAttributes() {
        return FlyingMob.m_21552_().add(Attributes.MAX_HEALTH, 5.0).add(Attributes.MOVEMENT_SPEED, 0.45F).add(Attributes.ATTACK_DAMAGE, 1.0).add(Attributes.ATTACK_SPEED, 40.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.ATTACK_KNOCKBACK, 1.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.0);
    }

    @Override
    protected void addControllerListeners(AnimationController<Pixie> controller) {
        controller.transitionLength(2);
    }

    @Override
    protected PlayState handleAnimState(AnimationState<? extends BaseFlyingFactionMob<?>> state) {
        if (!this.isActing()) {
            if (this.m_20184_().length() > 0.1F) {
                return state.setAndContinue(RawAnimation.begin().thenLoop("animation.pixie.move"));
            }
        } else {
            if (this.isAttacking()) {
                return state.setAndContinue(RawAnimation.begin().thenPlay("animation.pixie.cast").thenLoop("animation.pixie.idle_hostile"));
            }
            if (this.isTeleporting()) {
                return state.setAndContinue(RawAnimation.begin().thenPlay("animation.pixie.teleport").thenLoop("animation.pixie.idle_hostile"));
            }
        }
        return this.m_5448_() != null && this.m_5448_().isAlive() ? state.setAndContinue(RawAnimation.begin().thenLoop("animation.pixie.idle_hostile")) : state.setAndContinue(RawAnimation.begin().thenLoop("animation.pixie.idle"));
    }

    private void spawnParticles() {
        int numParticles = 2;
        for (int i = 0; i < numParticles; i++) {
            this.m_9236_().addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_GRAVITY.get()).setColor(229, 220, 84), this.m_20185_() - 0.2 + Math.random() * 0.4, this.m_20186_() + Math.random() * 0.5, this.m_20189_() - 0.2 + Math.random() * 0.4, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public CompoundTag getPacketData() {
        return new CompoundTag();
    }

    @Override
    public void handlePacketData(CompoundTag nbt) {
    }

    static class BoltAttackGoal extends Goal {

        private final Pixie parentEntity;

        public int attackTimer;

        public BoltAttackGoal(Pixie pixie) {
            this.parentEntity = pixie;
        }

        @Override
        public boolean canUse() {
            return this.parentEntity.m_5448_() != null;
        }

        @Override
        public void start() {
            this.attackTimer = 0;
        }

        @Override
        public void stop() {
            this.parentEntity.setAttacking(false);
        }

        @Override
        public boolean canContinueToUse() {
            return this.parentEntity.m_5448_() != null && this.parentEntity.m_5448_().isAlive();
        }

        @Override
        public void tick() {
            LivingEntity livingentity = this.parentEntity.m_5448_();
            if (livingentity != null) {
                if (livingentity.m_20280_(this.parentEntity) < 144.0 && this.parentEntity.m_21574_().hasLineOfSight(livingentity)) {
                    this.attackTimer++;
                    if (this.attackTimer == 40) {
                        this.parentEntity.setAttacking(true);
                    }
                    if (this.attackTimer == 42) {
                        if (!this.parentEntity.m_20067_()) {
                            this.parentEntity.m_5496_(SFX.Entity.Pixie.ATTACK, 1.0F, (float) (0.9F + Math.random() * 0.2F));
                        }
                        boolean isMiss = Math.random() > (double) (0.3F + (float) this.parentEntity.getTier() * 0.5F);
                        Vec3 offset = new Vec3(0.0, 0.0, 0.0);
                        if (isMiss) {
                            offset = new Vec3(Math.random() - 0.5, Math.random() - 0.5, Math.random() * 0.5);
                        }
                        int color = 0;
                        ServerMessageDispatcher.sendParticleSpawn(this.parentEntity.m_20185_(), this.parentEntity.m_20186_(), this.parentEntity.m_20189_(), this.parentEntity.m_5448_().m_20185_() + offset.x(), this.parentEntity.m_5448_().m_20186_() + (double) this.parentEntity.m_5448_().m_20192_() + offset.y(), this.parentEntity.m_5448_().m_20189_() + offset.z(), switch(this.parentEntity.getAffinity()) {
                            case EARTH ->
                                -14126821;
                            case WIND ->
                                -3815995;
                            case FIRE ->
                                -3648221;
                            default ->
                                0;
                        }, 64.0F, this.parentEntity.m_9236_().dimension(), ParticleInit.LIGHTNING_BOLT.get());
                        if (!isMiss) {
                            switch(this.parentEntity.getAffinity()) {
                                case EARTH:
                                    this.applyEarthEffect();
                                    break;
                                case WIND:
                                default:
                                    this.applyAirEffect();
                                    break;
                                case FIRE:
                                    this.applyFireEffect();
                                    break;
                                case WATER:
                                    this.applyWaterEffect();
                            }
                        }
                    } else if (this.attackTimer >= 62) {
                        this.parentEntity.setAttacking(false);
                        this.attackTimer = 0;
                    }
                } else if (this.attackTimer > 0) {
                    this.attackTimer--;
                }
            }
        }

        private void applyAirEffect() {
            Entity tgt = this.parentEntity.m_5448_();
            if (tgt.onGround()) {
                tgt.push(-0.2F + Math.random() * 0.4F, 1.0, -0.2F + Math.random() * 0.4F);
                if (tgt instanceof Player) {
                    ((ServerPlayer) tgt).connection.send(new ClientboundSetEntityMotionPacket(tgt));
                }
            }
        }

        private void applyWaterEffect() {
            if (this.parentEntity.m_5448_().m_20069_()) {
                this.parentEntity.m_5448_().addEffect(new MobEffectInstance(EffectInit.WATERY_GRAVE.get(), 10));
            } else {
                this.parentEntity.m_5448_().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2));
            }
            this.parentEntity.m_5448_().hurt(DamageHelper.createSourcedType(DamageHelper.FROST, this.parentEntity.m_9236_().registryAccess(), this.parentEntity), (float) (2 * this.parentEntity.getTier()));
        }

        private void applyEarthEffect() {
            if (!this.parentEntity.m_5448_().m_20096_() && this.parentEntity.m_5448_().getEffect(EffectInit.GRAVITY_WELL.get()) == null) {
                this.parentEntity.m_5448_().addEffect(new MobEffectInstance(EffectInit.GRAVITY_WELL.get(), 100));
            }
            this.parentEntity.m_5448_().hurt(this.parentEntity.m_269291_().mobAttack(this.parentEntity), 4.0F);
        }

        private void applyFireEffect() {
            this.parentEntity.m_5448_().addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100));
            if (this.parentEntity.m_5448_() instanceof Player) {
                Player player = (Player) this.parentEntity.m_5448_();
                player.getArmorSlots().forEach(stack -> stack.hurtAndBreak(5, player, item -> {
                }));
            }
        }
    }
}