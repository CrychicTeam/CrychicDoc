package com.mna.entities.faction;

import com.mna.ManaAndArtifice;
import com.mna.api.entities.IFactionEnemy;
import com.mna.api.faction.IFaction;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.api.timing.DelayedEventQueue;
import com.mna.api.timing.TimedDelayedEvent;
import com.mna.entities.ai.FactionTierWrapperGoal;
import com.mna.entities.faction.base.BaseFactionMob;
import com.mna.factions.Factions;
import com.mna.network.ServerMessageDispatcher;
import com.mna.tools.SummonUtils;
import java.util.EnumSet;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.keyframe.event.CustomInstructionKeyframeEvent;
import software.bernie.geckolib.core.object.PlayState;

public class Deathcap extends BaseFactionMob<Deathcap> implements AnimationController.CustomKeyframeHandler<Deathcap> {

    private static final EntityDataAccessor<Integer> TARGET_ID = SynchedEntityData.defineId(Deathcap.class, EntityDataSerializers.INT);

    private static final String INSTRUCTION_DAMAGE = "damage";

    private static final String INSTRUCTION_FLING = "fling";

    private static final String INSTRUCTION_HEAL = "heal";

    private static final String INSTRUCTION_BURROW_POS = "burrow_position";

    private static final String INSTRUCTION_BURROW_FLING = "burrow_attack";

    private static final String INSTRUCTION_ROOTS_POISON = "roots_attack";

    private static final String INSTRUCTION_SPORE_BURST = "spore_burst";

    private static final int BURROW_CD = 300;

    private static final int HEAL_CD = 1200;

    private static final int ROOTS_CD = 400;

    private static final int SPORES_CD = 400;

    private Entity attackEntity;

    private int burrowCooldown = 0;

    private int rootsCooldown = 0;

    private int healCooldown = 0;

    private int sporesCooldown = 0;

    private Vec3 rootsPos = Vec3.ZERO;

    private boolean isActing = false;

    private boolean isMeleeAttacking = false;

    private boolean meleeLeftHand = false;

    private boolean isBurrowing = false;

    private boolean isSporeBursting = false;

    private boolean isRooting = false;

    private boolean isHealing = false;

    public Deathcap(EntityType<Deathcap> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(TARGET_ID, -1);
    }

    @Override
    protected void addControllerListeners(AnimationController<Deathcap> controller) {
        controller.transitionLength(2);
        controller.setCustomInstructionKeyframeHandler(this);
    }

    @Override
    protected PlayState handleAnimState(AnimationState<? extends BaseFactionMob<?>> state) {
        RawAnimation builder = RawAnimation.begin();
        if (this.isActing) {
            if (this.isRooting) {
                builder.thenPlay("animation.mushroom_soldier.roots");
            } else if (this.isBurrowing) {
                builder.thenPlay("animation.mushroom_soldier.burrow");
            } else if (this.isHealing) {
                builder.thenPlay("animation.mushroom_soldier.heal");
            } else if (this.isSporeBursting) {
                builder.thenPlay("animation.mushroom_soldier.spore_burst");
            } else if (this.meleeLeftHand) {
                builder.thenPlay("animation.mushroom_soldier.attack_left");
            } else {
                builder.thenPlay("animation.mushroom_soldier.attack_right");
            }
        }
        if (this.m_20184_().length() > 0.05F) {
            if (this.f_19804_.get(TARGET_ID) != -1) {
                builder.thenLoop("animation.mushroom_soldier.run");
            } else {
                builder.thenLoop("animation.mushroom_soldier.walk");
            }
        } else {
            builder.thenLoop("animation.mushroom_soldier.idle");
        }
        return state.setAndContinue(builder);
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.BAMBOO_SAPLING_PLACE;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.BAMBOO_SAPLING_PLACE;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ROOTS_BREAK;
    }

    @Override
    public IFaction getFaction() {
        return Factions.FEY;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isBurrowing && !source.is(DamageTypeTags.BYPASSES_ARMOR)) {
            return false;
        } else {
            if (source.is(DamageTypes.WITHER)) {
                amount *= 2.0F;
            }
            return super.hurt(source, amount);
        }
    }

    @Override
    public boolean canBeAffected(MobEffectInstance pPotioneffect) {
        return pPotioneffect.getEffect() != MobEffects.POISON;
    }

    @Override
    public void tick() {
        super.tick();
        this.burrowCooldown--;
        this.healCooldown--;
        this.rootsCooldown--;
        this.sporesCooldown--;
    }

    @Override
    public void setTarget(LivingEntity pLivingEntity) {
        super.m_6710_(pLivingEntity);
        if (pLivingEntity == null) {
            this.f_19804_.set(TARGET_ID, -1);
        } else {
            this.f_19804_.set(TARGET_ID, pLivingEntity.m_19879_());
        }
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new FactionTierWrapperGoal(1, this, new Deathcap.HealGoal()));
        this.f_21345_.addGoal(3, new FactionTierWrapperGoal(1, this, new Deathcap.SporeBurstGoal()));
        this.f_21345_.addGoal(2, new FactionTierWrapperGoal(2, this, new Deathcap.BurrowGoal()));
        this.f_21345_.addGoal(2, new FactionTierWrapperGoal(2, this, new Deathcap.RootsGoal()));
        this.f_21345_.addGoal(4, new MeleeAttackGoal(this, this.m_21133_(Attributes.MOVEMENT_SPEED) * 1.25, false));
        this.f_21345_.addGoal(5, new RandomStrollGoal(this, 0.3F));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, this::factionTargetPlayerPredicate));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, Mob.class, 10, true, false, this::factionTargetHelpPredicate));
        this.f_21346_.addGoal(4, new NearestAttackableTargetGoal(this, Mob.class, 10, true, false, e -> e instanceof IFactionEnemy && ((IFactionEnemy) e).getFaction() != this.getFaction()));
    }

    @Override
    public void swing(InteractionHand p_226292_1_, boolean p_226292_2_) {
        if (!this.isActing) {
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("resetattack", 40, "", this::handleDelayCallback));
            this.isMeleeAttacking = true;
            this.meleeLeftHand = Math.random() < 0.5;
            this.isActing = true;
            ServerMessageDispatcher.sendEntityStateMessage(this);
        }
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (!this.isMeleeAttacking) {
            return true;
        } else {
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("damage", 5, entityIn, this::handleDelayCallback));
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("resetattack", 15, "", this::handleDelayCallback));
            this.isActing = true;
            this.isMeleeAttacking = false;
            ServerMessageDispatcher.sendEntityStateMessage(this);
            this.attackEntity = entityIn;
            return true;
        }
    }

    public boolean damageEntity(Entity entityIn) {
        float f = (float) this.m_21133_(Attributes.ATTACK_DAMAGE);
        float f1 = (float) this.m_21133_(Attributes.ATTACK_KNOCKBACK);
        if (entityIn instanceof LivingEntity) {
            f += EnchantmentHelper.getDamageBonus(this.m_21205_(), ((LivingEntity) entityIn).getMobType());
            f1 += (float) EnchantmentHelper.getKnockbackBonus(this);
        }
        int i = EnchantmentHelper.getFireAspect(this);
        if (i > 0) {
            entityIn.setSecondsOnFire(i * 4);
        }
        boolean flag = entityIn.hurt(this.m_269291_().mobAttack(this), f);
        if (flag) {
            if (!this.m_9236_().isClientSide()) {
                this.m_5496_(SFX.Entity.Generic.WOOSH, 1.0F, (float) (0.9F + Math.random() * 0.2F));
            }
            if (f1 > 0.0F && entityIn instanceof LivingEntity) {
                ((LivingEntity) entityIn).knockback((double) (f1 * 0.5F), (double) Mth.sin(this.m_146908_() * (float) (Math.PI / 180.0)), (double) (-Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0))));
                this.m_20256_(this.m_20184_().multiply(0.6, 1.0, 0.6));
            }
            if (entityIn instanceof Player playerentity) {
                this.maybeDisableShield(playerentity, this.m_21205_(), playerentity.m_6117_() ? playerentity.m_21211_() : ItemStack.EMPTY);
            }
            this.m_19970_(this, entityIn);
            this.m_21335_(entityIn);
            if (this.f_19796_.nextFloat() < 0.2F && entityIn instanceof Mob) {
                ((Mob) entityIn).setTarget(this);
            }
        }
        return flag;
    }

    private void maybeDisableShield(Player p_233655_1_, ItemStack p_233655_2_, ItemStack p_233655_3_) {
        if (!p_233655_2_.isEmpty() && !p_233655_3_.isEmpty() && p_233655_2_.getItem() instanceof AxeItem && p_233655_3_.getItem() == Items.SHIELD) {
            float f = 0.25F + (float) EnchantmentHelper.getBlockEfficiency(this) * 0.05F;
            if (this.f_19796_.nextFloat() < f) {
                p_233655_1_.getCooldowns().addCooldown(Items.SHIELD, 100);
                this.m_9236_().broadcastEntityEvent(p_233655_1_, (byte) 30);
            }
        }
    }

    public static AttributeSupplier.Builder getGlobalAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.MOVEMENT_SPEED, 0.55F).add(Attributes.ATTACK_DAMAGE, 6.0).add(Attributes.ATTACK_SPEED, 40.0).add(Attributes.ATTACK_KNOCKBACK, 1.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.0);
    }

    private void handleDelayCallback(String identifier, Entity entity) {
        if (this.m_6084_()) {
            if (!this.m_9236_().isClientSide()) {
                switch(identifier) {
                    case "fling":
                        entity.push(0.0, 1.0, 0.0);
                        if (entity instanceof Player) {
                            ((ServerPlayer) entity).connection.send(new ClientboundSetEntityMotionPacket(entity));
                        }
                        this.m_5496_(SFX.Entity.Generic.WOOSH, 1.0F, (float) (0.9F + Math.random() * 0.2F));
                        break;
                    case "damage":
                        this.damageEntity(entity);
                        if (entity instanceof LivingEntity) {
                            ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.POISON, 60, 1));
                        }
                        break;
                    case "heal":
                        this.m_5634_(this.m_21233_() * 0.5F);
                        break;
                    case "burrow_position":
                        Vec3 posx = entity.position();
                        if (posx.distanceTo(this.m_20182_()) < 64.0) {
                            Vec3 newPos = this.m_20182_().subtract(posx).normalize();
                            this.m_6034_(posx.x + newPos.x, posx.y + newPos.y, posx.z + newPos.z);
                            this.m_7618_(EntityAnchorArgument.Anchor.FEET, posx);
                        }
                        break;
                    case "burrow_attack":
                        Vec3 pos = entity.position();
                        this.m_5496_(SFX.Spell.Impact.Single.EARTH, 1.0F, (float) (0.9F + Math.random() * 0.2F));
                        if (pos.distanceTo(this.m_20182_()) < 2.0) {
                            this.handleDelayCallback("fling", entity);
                        }
                        break;
                    case "roots_attack":
                        AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.m_9236_(), this.rootsPos.x, this.rootsPos.y, this.rootsPos.z);
                        areaeffectcloud.setOwner(this);
                        areaeffectcloud.setRadius(3.0F);
                        areaeffectcloud.setRadiusOnUse(-0.5F);
                        areaeffectcloud.setWaitTime(10);
                        areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float) areaeffectcloud.getDuration());
                        areaeffectcloud.setPotion(Potions.POISON);
                        this.m_9236_().m_7967_(areaeffectcloud);
                        break;
                    case "spore_burst":
                        this.m_9236_().getEntities(this, this.m_20191_().inflate(3.0), e -> e.isAlive() && e instanceof LivingEntity && !SummonUtils.isTargetFriendly(e, this)).stream().map(e -> (LivingEntity) e).forEach(e -> e.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 2)));
                }
            } else {
                switch(identifier) {
                    case "heal":
                        for (int i = 0; i < 5; i++) {
                            double d0 = this.f_19796_.nextGaussian() * 0.02;
                            double d1 = this.f_19796_.nextGaussian() * 0.02;
                            double d2 = this.f_19796_.nextGaussian() * 0.02;
                            this.m_9236_().addParticle(ParticleTypes.HEART, this.m_20208_(1.0), this.m_20187_() + 1.0, this.m_20262_(1.0), d0, d1, d2);
                        }
                        for (int i = 0; i < 5; i++) {
                            this.m_9236_().addParticle(new MAParticleType(ParticleInit.EARTH.get()), this.m_20208_(1.0), this.m_20187_() + 1.0, this.m_20262_(1.0), 0.18, 0.3, 0.03);
                        }
                        break;
                    case "burrow_position":
                        BlockPos posxx = this.m_20097_();
                        BlockState state = this.m_9236_().getBlockState(posxx);
                        for (int i = 0; i < 10; i++) {
                            this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), this.m_20185_() + Math.random(), this.m_20186_() + 0.5, this.m_20189_() + Math.random(), 0.0, 0.05 * Math.random(), 0.0);
                        }
                        SoundType soundType = this.m_146900_().m_60827_();
                        this.m_9236_().playSound(ManaAndArtifice.instance.proxy.getClientPlayer(), (double) posxx.m_123341_(), (double) posxx.m_123342_(), (double) posxx.m_123343_(), soundType.getBreakSound(), SoundSource.HOSTILE, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
                        break;
                    case "burrow_attack":
                        BlockPos posxx = this.m_20097_();
                        BlockState state = this.m_9236_().getBlockState(posxx);
                        for (int i = 0; i < 10; i++) {
                            this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), this.m_20185_() + Math.random(), this.m_20186_() + 0.5, this.m_20189_() + Math.random(), 0.0, 0.05 * Math.random(), 0.0);
                        }
                        break;
                    case "spore_burst":
                        Vec3 particlePos = this.m_146892_();
                        for (int i = 0; i < 50; i++) {
                            this.m_9236_().addParticle(new MAParticleType(ParticleInit.DUST.get()).setColor(54, 69, 30).setScale((float) (0.2F + Math.random() * 0.1)), particlePos.x(), particlePos.y() - Math.random(), particlePos.z, -0.5 + Math.random(), 0.0, -0.5 + Math.random());
                        }
                }
            }
        }
    }

    private void handleDelayCallback(String identifier, String data) {
        if (!this.m_9236_().isClientSide()) {
            this.isActing = false;
            if (this.isBurrowing) {
                this.burrowCooldown = 300;
                this.isBurrowing = false;
            } else if (this.isHealing) {
                this.healCooldown = 1200;
                this.isHealing = false;
            } else if (this.isRooting) {
                this.rootsCooldown = 400;
                this.isRooting = false;
            } else if (this.isSporeBursting) {
                this.sporesCooldown = 400;
                this.isSporeBursting = false;
            }
            ServerMessageDispatcher.sendEntityStateMessage(this);
        }
    }

    @Override
    public void handle(CustomInstructionKeyframeEvent<Deathcap> event) {
        if (this.attackEntity == ManaAndArtifice.instance.proxy.getClientPlayer() && event.getKeyframeData().getInstructions().indexOf("fling") > -1) {
            this.attackEntity.push(0.0, 1.0, 0.0);
        }
    }

    @Override
    public CompoundTag getPacketData() {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("attacking", this.isActing);
        nbt.putBoolean("burrowing", this.isBurrowing);
        nbt.putBoolean("rooting", this.isRooting);
        nbt.putBoolean("healing", this.isHealing);
        nbt.putBoolean("meleeLeftHand", this.meleeLeftHand);
        nbt.putBoolean("sporeBursting", this.isSporeBursting);
        return nbt;
    }

    @Override
    public void handlePacketData(CompoundTag nbt) {
        this.isActing = nbt.getBoolean("attacking");
        this.isBurrowing = nbt.getBoolean("burrowing");
        this.isHealing = nbt.getBoolean("healing");
        this.isRooting = nbt.getBoolean("rooting");
        this.meleeLeftHand = nbt.getBoolean("meleeLeftHand");
        this.isSporeBursting = nbt.getBoolean("sporeBursting");
        if (this.m_9236_().isClientSide()) {
            if (this.isHealing) {
                DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("heal", 60, this, this::handleDelayCallback));
            } else if (this.isBurrowing) {
                DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("burrow_position", 50, this, this::handleDelayCallback));
                DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("burrow_attack", 75, this, this::handleDelayCallback));
            } else if (this.isSporeBursting) {
                DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("spore_burst", 10, this, this::handleDelayCallback));
            }
        }
    }

    public class BurrowGoal extends Goal {

        public BurrowGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.LOOK, Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return Deathcap.this.burrowCooldown <= 0 && Deathcap.this.rootsCooldown > 0 && Deathcap.this.m_20096_() && Deathcap.this.m_9236_().loadedAndEntityCanStandOnFace(Deathcap.this.m_20183_().below(), Deathcap.this, Direction.UP) && Deathcap.this.m_5448_() != null && Deathcap.this.m_5448_().m_20096_() && Deathcap.this.m_5448_().m_20270_(Deathcap.this) > 8.0F && !Deathcap.this.isActing;
        }

        @Override
        public void start() {
            Deathcap.this.m_21573_().stop();
            Deathcap.this.isBurrowing = true;
            Deathcap.this.isActing = true;
            DelayedEventQueue.pushEvent(Deathcap.this.m_9236_(), new TimedDelayedEvent<>("burrow_position", 65, Deathcap.this.m_5448_(), Deathcap.this::handleDelayCallback));
            DelayedEventQueue.pushEvent(Deathcap.this.m_9236_(), new TimedDelayedEvent<>("burrow_attack", 80, Deathcap.this.m_5448_(), Deathcap.this::handleDelayCallback));
            DelayedEventQueue.pushEvent(Deathcap.this.m_9236_(), new TimedDelayedEvent<>("resetattack", 90, "", Deathcap.this::handleDelayCallback));
            ServerMessageDispatcher.sendEntityStateMessage(Deathcap.this);
        }

        @Override
        public boolean canContinueToUse() {
            return Deathcap.this.isBurrowing;
        }
    }

    public class HealGoal extends Goal {

        public HealGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.LOOK, Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return Deathcap.this.healCooldown <= 0 && Deathcap.this.m_21223_() < Deathcap.this.m_21233_() * 0.75F && !Deathcap.this.isActing;
        }

        @Override
        public void start() {
            Deathcap.this.m_21573_().stop();
            Deathcap.this.isHealing = true;
            Deathcap.this.isActing = true;
            DelayedEventQueue.pushEvent(Deathcap.this.m_9236_(), new TimedDelayedEvent<>("heal", 60, Deathcap.this, Deathcap.this::handleDelayCallback));
            DelayedEventQueue.pushEvent(Deathcap.this.m_9236_(), new TimedDelayedEvent<>("resetattack", 90, "", Deathcap.this::handleDelayCallback));
            ServerMessageDispatcher.sendEntityStateMessage(Deathcap.this);
        }

        @Override
        public boolean canContinueToUse() {
            return Deathcap.this.isHealing;
        }
    }

    public class RootsGoal extends Goal {

        public RootsGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.LOOK, Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return Deathcap.this.rootsCooldown <= 0 && Deathcap.this.m_20096_() && Deathcap.this.m_9236_().loadedAndEntityCanStandOnFace(Deathcap.this.m_20183_().below(), Deathcap.this, Direction.UP) && Deathcap.this.m_5448_() != null && Deathcap.this.m_5448_().m_20096_() && !Deathcap.this.isActing;
        }

        @Override
        public void start() {
            Deathcap.this.m_21573_().stop();
            Deathcap.this.isRooting = true;
            Deathcap.this.isActing = true;
            Deathcap.this.rootsPos = Deathcap.this.m_5448_().m_20182_();
            DelayedEventQueue.pushEvent(Deathcap.this.m_9236_(), new TimedDelayedEvent<>("roots_attack", 50, Deathcap.this.m_5448_(), Deathcap.this::handleDelayCallback));
            DelayedEventQueue.pushEvent(Deathcap.this.m_9236_(), new TimedDelayedEvent<>("resetattack", 95, "", Deathcap.this::handleDelayCallback));
            ServerMessageDispatcher.sendEntityStateMessage(Deathcap.this);
        }

        @Override
        public boolean canContinueToUse() {
            return Deathcap.this.isRooting;
        }
    }

    public class SporeBurstGoal extends Goal {

        public SporeBurstGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.LOOK, Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return Deathcap.this.sporesCooldown <= 0 && Deathcap.this.m_5448_() != null && Deathcap.this.m_5448_().m_20270_(Deathcap.this) <= 3.0F && !Deathcap.this.isActing;
        }

        @Override
        public void start() {
            Deathcap.this.m_21573_().stop();
            Deathcap.this.isSporeBursting = true;
            Deathcap.this.isActing = true;
            DelayedEventQueue.pushEvent(Deathcap.this.m_9236_(), new TimedDelayedEvent<>("spore_burst", 10, Deathcap.this.m_5448_(), Deathcap.this::handleDelayCallback));
            DelayedEventQueue.pushEvent(Deathcap.this.m_9236_(), new TimedDelayedEvent<>("resetattack", 60, "", Deathcap.this::handleDelayCallback));
            ServerMessageDispatcher.sendEntityStateMessage(Deathcap.this);
        }

        @Override
        public boolean canContinueToUse() {
            return Deathcap.this.isSporeBursting;
        }
    }
}