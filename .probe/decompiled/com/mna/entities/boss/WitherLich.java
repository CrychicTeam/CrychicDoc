package com.mna.entities.boss;

import com.google.common.collect.Maps;
import com.mna.api.affinity.Affinity;
import com.mna.api.entities.DamageHelper;
import com.mna.api.entities.IFactionEnemy;
import com.mna.api.faction.IFaction;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.api.spells.SpellCastingResult;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.collections.Components;
import com.mna.api.spells.collections.Shapes;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.api.tools.RLoc;
import com.mna.effects.EffectInit;
import com.mna.entities.EntityInit;
import com.mna.entities.summon.InsectSwarm;
import com.mna.factions.Factions;
import com.mna.items.ItemInit;
import com.mna.spells.SpellCaster;
import com.mna.spells.crafting.SpellRecipe;
import com.mna.tools.EntityUtil;
import com.mna.tools.SummonUtils;
import com.mna.tools.math.MathUtils;
import com.mna.tools.math.Vector3;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class WitherLich extends BossMonster<WitherLich> implements IFactionEnemy<WitherLich> {

    private final SpellRecipe meleeSpell = new SpellRecipe();

    private final ItemStack melee;

    private final ServerBossEvent bossEvent = (ServerBossEvent) new ServerBossEvent(this.m_5446_(), BossEvent.BossBarColor.YELLOW, BossEvent.BossBarOverlay.NOTCHED_20).setDarkenScreen(true);

    private final ServerBossEvent powerWordDeathBar = (ServerBossEvent) new ServerBossEvent(Component.translatable("entity.mna.wither_lich.power_word_death"), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS).setDarkenScreen(true);

    protected static final EntityDataAccessor<Byte> SHIELD_COUNT = SynchedEntityData.defineId(WitherLich.class, EntityDataSerializers.BYTE);

    protected static final EntityDataAccessor<Byte> TEXTURE_VARIANT = SynchedEntityData.defineId(WitherLich.class, EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Optional<UUID>> FACTION_JOIN_PLAYER = SynchedEntityData.defineId(WitherLich.class, EntityDataSerializers.OPTIONAL_UUID);

    protected static final EntityDataAccessor<Boolean> IS_JOINING = SynchedEntityData.defineId(WitherLich.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> REACHING = SynchedEntityData.defineId(WitherLich.class, EntityDataSerializers.BOOLEAN);

    private Vector3 start;

    private Vector3 end;

    private int lerpTicks;

    private int lerpCount = 0;

    private int finishTicks = 55;

    private Player cachedPlayer;

    private int powerWordDeathTicks = 0;

    private int numSummons = 0;

    private int lichDeathTime;

    ArrayList<EntityType<? extends Mob>> summonQueue = new ArrayList();

    private static final int COOLDOWN_SUMMON_ID = 1024;

    private static final int COOLDOWN_FROSTBOLT_ID = 1025;

    private static final int COOLDOWN_WITHERBOLT_ID = 1026;

    private static final int COOLDOWN_FOSSILIZE_BOLT_ID = 1027;

    private static final int COOLDOWN_INSECT_SWARM_BREATH_ID = 1028;

    private static final int COOLDOWN_FROST_BREATH_ID = 1029;

    private boolean shouldShieldedStrike = false;

    private int globalSpellCooldown = 0;

    public WitherLich(EntityType<? extends WitherLich> type, Level worldIn) {
        super(type, worldIn);
        this.melee = new ItemStack(ItemInit.SPELL.get());
        this.meleeSpell.setOverrideAffinity(Affinity.ICE);
        this.meleeSpell.setShape(Shapes.TOUCH);
        this.meleeSpell.getShape().setValue(Attribute.RANGE, 8.0F);
        this.meleeSpell.addComponent(Components.LACERATE);
        this.meleeSpell.addComponent(Components.IMPALE);
        this.meleeSpell.addComponent(Components.LIFE_TAP);
        this.meleeSpell.getComponent(0).setValue(Attribute.DURATION, 5.0F);
        this.meleeSpell.getComponent(0).setValue(Attribute.MAGNITUDE, 3.0F);
        this.meleeSpell.getComponent(1).setValue(Attribute.DAMAGE, 15.0F);
        this.meleeSpell.getComponent(2).setValue(Attribute.DURATION, 15.0F);
        this.meleeSpell.getComponent(2).setValue(Attribute.LESSER_MAGNITUDE, 4.0F);
        this.meleeSpell.writeToNBT(this.melee.getOrCreateTag());
        SummonUtils.setBonusSummons(this, 99);
        this.m_20242_(true);
        this.f_21342_ = new WitherLich.MoveHelperController(this);
        this.getPersistentData().putFloat("life_tap_pct", 0.25F);
        this.f_19804_.set(SHIELD_COUNT, (byte) 5);
        this.setCooldown(1024, 0);
        this.powerWordDeathTicks = 1;
    }

    public WitherLich(Level worldIn) {
        this(EntityInit.LICH.get(), worldIn);
    }

    public WitherLich(Level level, Vec3 position) {
        this(level);
        this.m_146884_(position);
    }

    private Player getFactionJoinPlayer() {
        if (this.cachedPlayer == null && this.f_19804_.get(FACTION_JOIN_PLAYER).isPresent()) {
            this.cachedPlayer = this.m_9236_().m_46003_((UUID) this.f_19804_.get(FACTION_JOIN_PLAYER).get());
        }
        return this.cachedPlayer;
    }

    private int getDesiredSummonPower() {
        int baseline = 2;
        int shields = this.f_19804_.get(SHIELD_COUNT);
        baseline *= Math.max(5 - shields, 1);
        return baseline * Math.min(Math.max(this.threat.size(), 1), 10);
    }

    private void buildSummonList() {
        this.summonQueue.clear();
        int summonPower = 0;
        int desiredSummonPower = this.getDesiredSummonPower();
        ArrayList<WitherLich.SummonCandidate> summonPool = new ArrayList();
        summonPool.add(new WitherLich.SummonCandidate(EntityType.ZOMBIE, 1));
        summonPool.add(new WitherLich.SummonCandidate(EntityType.SKELETON, 1));
        summonPool.add(new WitherLich.SummonCandidate(EntityType.ZOMBIE_VILLAGER, 2));
        summonPool.add(new WitherLich.SummonCandidate(EntityType.DROWNED, 2));
        summonPool.add(new WitherLich.SummonCandidate(EntityType.STRAY, 2));
        summonPool.add(new WitherLich.SummonCandidate(EntityType.WITHER_SKELETON, 3));
        summonPool.add(new WitherLich.SummonCandidate(EntityType.ZOMBIFIED_PIGLIN, 2));
        summonPool.add(new WitherLich.SummonCandidate(EntityInit.SKELETON_ASSASSIN.get(), 5));
        summonPool.add(new WitherLich.SummonCandidate(EntityInit.HULKING_ZOMBIE.get(), 10));
        while (summonPower < desiredSummonPower) {
            int delta = desiredSummonPower - summonPower;
            List<WitherLich.SummonCandidate> filtered = (List<WitherLich.SummonCandidate>) summonPool.stream().filter(s -> s.weight <= delta).sorted((a, b) -> a.weight - b.weight).collect(Collectors.toList());
            if (filtered.size() == 0) {
                break;
            }
            WitherLich.SummonCandidate next = (WitherLich.SummonCandidate) filtered.get(filtered.size() - 1);
            this.summonQueue.add(next.type);
            summonPower += next.weight;
        }
    }

    private void createSummon() {
        if (this.summonQueue.size() != 0) {
            BlockPos center = this.m_20183_();
            boolean found = false;
            int yCount = 0;
            while (!found) {
                while (yCount < 10 && this.m_9236_().m_46859_(center)) {
                    center = center.below();
                    yCount++;
                }
                center = center.above();
                if (this.m_9236_().m_46859_(center)) {
                    found = true;
                } else {
                    center = this.m_20183_().offset(-5 + (int) (Math.random() * 10.0), 0, -5 + (int) (Math.random() * 10.0));
                }
            }
            if (!found) {
                center = this.m_20183_();
            }
            EntityType<? extends Mob> type = (EntityType<? extends Mob>) this.summonQueue.get(0);
            this.summonQueue.remove(0);
            Mob summon = type.create(this.m_9236_());
            SummonUtils.setSummon(summon, this, Integer.MAX_VALUE);
            int phase = this.f_19804_.get(SHIELD_COUNT);
            float pctHealthBoost = 1.0F + Math.abs(0.2F * (float) (5 - phase));
            float armorBoost = (float) (2 + 2 * (5 - phase));
            summon.m_21051_(Attributes.ARMOR).addPermanentModifier(new AttributeModifier("wither_lich_summon_armorboost", (double) armorBoost, AttributeModifier.Operation.ADDITION));
            summon.m_21051_(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("wither_lich_summon_healthboost", (double) pctHealthBoost, AttributeModifier.Operation.MULTIPLY_TOTAL));
            summon.m_21153_(summon.m_21233_());
            switch(phase) {
                case 0:
                case 1:
                    summon.m_7292_(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 2));
                    summon.m_7292_(new MobEffectInstance(MobEffects.REGENERATION, Integer.MAX_VALUE, 1));
                    summon.m_7292_(new MobEffectInstance(MobEffects.DAMAGE_BOOST, Integer.MAX_VALUE));
                    break;
                case 2:
                    summon.m_7292_(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
                    summon.m_7292_(new MobEffectInstance(MobEffects.REGENERATION, Integer.MAX_VALUE));
                    break;
                case 3:
                    summon.m_7292_(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, Integer.MAX_VALUE));
                    summon.m_7292_(new MobEffectInstance(MobEffects.REGENERATION, Integer.MAX_VALUE));
                    break;
                case 4:
                    summon.m_7292_(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, Integer.MAX_VALUE));
            }
            summon.m_146884_(Vec3.atBottomCenterOf(center));
            this.m_9236_().m_7967_(summon);
            SummonUtils.addTrackedEntity(this, summon);
            this.numSummons++;
        }
    }

    private void decrementShield() {
        this.shouldShieldedStrike = false;
        this.f_19804_.set(SHIELD_COUNT, (byte) MathUtils.clamp(this.f_19804_.get(SHIELD_COUNT) - 1, 0, 5));
        this.setCooldown(1024, 0);
    }

    public boolean damageEntity(Entity entityIn) {
        SpellCastingResult success = SpellCaster.Affect(this.melee, this.meleeSpell, this.m_9236_(), new SpellSource(this, InteractionHand.MAIN_HAND), new SpellTarget(entityIn));
        if (success.getCode().isConsideredSuccess()) {
            if (this.f_19796_.nextFloat() < 0.4F && entityIn instanceof Mob) {
                ((Mob) entityIn).setTarget(this);
            }
            return true;
        } else {
            return false;
        }
    }

    private void spellCastReset() {
        this.setAttackAction(WitherLich.AttackAction.NONE);
        this.setAction(WitherLich.Action.IDLE);
        this.globalSpellCooldown = 10 + (int) (Math.random() * 30.0);
    }

    private boolean spellCastPrecast(BossMonster<?> me) {
        this.setAction(WitherLich.Action.ATTACKING);
        this.setAttackAction(Math.random() < 0.5 ? WitherLich.AttackAction.CAST_1_LEFT : WitherLich.AttackAction.CAST_1_RIGHT);
        this.m_5496_(SFX.Entity.WitherLich.CAST, 1.0F, 0.8F + (float) (Math.random() * 0.4));
        return true;
    }

    private boolean spellCastPredicate(Entity target) {
        return this.getAction() == WitherLich.Action.IDLE && this.globalSpellCooldown == 0 && this.m_5448_() != null && this.m_5448_().isAlive();
    }

    public void decrementSummons() {
        this.numSummons--;
        if (this.numSummons <= 0) {
            this.shouldShieldedStrike = true;
            this.numSummons = 0;
        }
    }

    public Vec3 getLookVector() {
        return Vec3.directionFromRotation(this.m_146909_(), this.m_6080_()).normalize();
    }

    private int getPowerWordDeathTime() {
        return 1200 * Math.max(this.f_19804_.get(SHIELD_COUNT), 1);
    }

    private WitherLich.Action getAction() {
        int ordinal = (this.getStateFlag() & 2040) >> 3 & 0xFF;
        return ordinal >= WitherLich.Action.values().length ? WitherLich.Action.IDLE : WitherLich.Action.values()[ordinal];
    }

    private WitherLich.AttackAction getAttackAction() {
        int ordinal = (this.getStateFlag() & 65535) >> 12 & 15;
        return ordinal >= WitherLich.AttackAction.values().length ? WitherLich.AttackAction.NONE : WitherLich.AttackAction.values()[ordinal];
    }

    public int getTextureVariant() {
        return this.f_19804_.get(TEXTURE_VARIANT);
    }

    public WitherLich setFactionJoin(Player player, Vec3 start, Vec3 end, int lerpTicks) {
        this.cachedPlayer = player;
        this.f_19804_.set(FACTION_JOIN_PLAYER, Optional.of(player.getGameProfile().getId()));
        this.f_19804_.set(IS_JOINING, true);
        this.hideBossBar();
        this.setAction(WitherLich.Action.FJ_CONVERTING);
        this.start = new Vector3(start.x, start.y, start.z);
        this.end = new Vector3(end.x, end.y, end.z);
        this.lerpTicks = lerpTicks;
        this.powerWordDeathTicks = 0;
        return this;
    }

    private void setAction(WitherLich.Action action) {
        byte ordinal = (byte) ((action.ordinal() & 0xFF) << 3);
        int flag = this.getStateFlag();
        flag &= -2041;
        flag |= ordinal;
        this.setState(new int[] { flag });
    }

    private void setAttackAction(WitherLich.AttackAction action) {
        int ordinal = (action.ordinal() & 15) << 12;
        int flag = this.getStateFlag();
        flag &= -61441;
        flag |= ordinal;
        this.setState(new int[] { flag });
    }

    @Override
    public void tick() {
        if (!this.m_6084_()) {
            this.setAction(WitherLich.Action.DEATH);
        }
        if (this.powerWordDeathTicks > 0 && this.powerWordDeathTicks < this.getPowerWordDeathTime()) {
            this.powerWordDeathTicks++;
        }
        if (this.globalSpellCooldown > 0) {
            this.globalSpellCooldown--;
        }
        this.m_20242_(true);
        super.tick();
        if (this.m_9236_().isClientSide() || this.getAction() != WitherLich.Action.FJ_CONVERTING) {
            float pctHeal = 0.2F * (float) this.f_19804_.get(SHIELD_COUNT).byteValue();
            if (pctHeal > 0.0F && this.m_21223_() < this.m_21233_() * pctHeal) {
                this.m_5634_(2.5F);
            }
            if (!this.m_9236_().isClientSide() && this.m_9236_().getGameTime() % 100L == 0L && this.getAction() != WitherLich.Action.SUMMON) {
                int summons = (int) SummonUtils.getSummons(this).stream().filter(e -> !(e instanceof InsectSwarm)).count();
                while (this.numSummons > summons) {
                    this.decrementSummons();
                }
                this.numSummons = summons;
                if (this.numSummons == 0) {
                    this.setCooldown(1024, 0);
                }
            }
            if (this.m_5448_() != null && !this.m_5448_().isAlive()) {
                this.m_6710_(null);
            }
        } else if (this.getFactionJoinPlayer() == null) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        } else {
            this.lerpCount++;
            if (this.lerpCount <= this.lerpTicks) {
                float pct = (float) this.lerpCount / (float) this.lerpTicks;
                Vector3 pos = Vector3.lerp(this.start, this.end, pct);
                this.m_6034_((double) pos.x, (double) pos.y, (double) pos.z);
                if (this.lerpCount < 20) {
                    this.m_7618_(EntityAnchorArgument.Anchor.FEET, this.end.toVec3D());
                }
            } else {
                this.f_19804_.set(REACHING, true);
                if (this.lerpCount - this.lerpTicks > this.finishTicks) {
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                }
            }
        }
    }

    @Override
    protected void registerGoals() {
        double moveSpeed = this.m_21133_(Attributes.MOVEMENT_SPEED);
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(0, new WitherLich.WitherLichFly());
        this.f_21345_.addGoal(1, new WitherLich.ShieldedStrikeGoal());
        this.f_21345_.addGoal(2, new WitherLich.SummonMinionsGoal());
        this.f_21345_.addGoal(2, new WitherLich.PowerWordDeathGoal());
        ItemStack frostBolt = new ItemStack(ItemInit.SPELL.get());
        SpellRecipe frostBoltSpell = new SpellRecipe();
        frostBoltSpell.setShape(Shapes.PROJECTILE);
        frostBoltSpell.addComponent(Components.FROST_DAMAGE);
        frostBoltSpell.getComponent(0).setValue(Attribute.DAMAGE, 20.0F);
        frostBoltSpell.addComponent(Components.SLOW);
        frostBoltSpell.getComponent(1).setValue(Attribute.DURATION, 10.0F);
        frostBoltSpell.getComponent(1).setValue(Attribute.MAGNITUDE, 2.0F);
        frostBoltSpell.writeToNBT(frostBolt.getOrCreateTag());
        ItemStack witherBolt = new ItemStack(ItemInit.SPELL.get());
        SpellRecipe witherBoltSpell = new SpellRecipe();
        witherBoltSpell.setShape(Shapes.BOLT);
        witherBoltSpell.getShape().setValue(Attribute.RANGE, 24.0F);
        witherBoltSpell.addComponent(Components.WITHER);
        witherBoltSpell.getComponent(0).setValue(Attribute.DURATION, 10.0F);
        witherBoltSpell.getComponent(0).setValue(Attribute.MAGNITUDE, 3.0F);
        witherBoltSpell.writeToNBT(witherBolt.getOrCreateTag());
        ItemStack fossilizeBolt = new ItemStack(ItemInit.SPELL.get());
        SpellRecipe fossilizeSpell = new SpellRecipe();
        fossilizeSpell.setShape(Shapes.CHAIN);
        fossilizeSpell.getShape().setValue(Attribute.RANGE, 32.0F);
        fossilizeSpell.getShape().setValue(Attribute.RADIUS, 10.0F);
        fossilizeSpell.getShape().setValue(Attribute.MAGNITUDE, 100.0F);
        fossilizeSpell.addComponent(Components.FOSSILIZE);
        fossilizeSpell.getComponent(0).setValue(Attribute.DURATION, 10.0F);
        fossilizeSpell.writeToNBT(fossilizeBolt.getOrCreateTag());
        ItemStack insectSwarm = new ItemStack(ItemInit.SPELL.get());
        SpellRecipe insectSwarmSpell = new SpellRecipe();
        insectSwarmSpell.setShape(Shapes.SELF);
        insectSwarmSpell.addComponent(Components.INSECT_SWARM);
        insectSwarmSpell.writeToNBT(insectSwarm.getOrCreateTag());
        this.f_21345_.addGoal(4, new BossMonster.CastSpellAtTargetGoal(witherBolt, moveSpeed, 400, 20.0F, 0.0F, true, 1026, 8, 12, null, this::spellCastReset, this::spellCastPrecast).setUsePredicate(this::spellCastPredicate));
        this.f_21345_.addGoal(4, new BossMonster.CastSpellAtTargetGoal(fossilizeBolt, moveSpeed, 400, 26.0F, 0.0F, true, 1027, 8, 12, null, this::spellCastReset, this::spellCastPrecast).setUsePredicate(this::spellCastPredicate));
        this.f_21345_.addGoal(5, new BossMonster.CastSpellAtTargetGoal(frostBolt, moveSpeed, 60, 64.0F, 0.0F, true, 1025, 8, 12, null, this::spellCastReset, this::spellCastPrecast).setUsePredicate(this::spellCastPredicate));
        this.f_21345_.addGoal(3, new BossMonster.CastSpellAtTargetGoal(insectSwarm, moveSpeed, 800, 20.0F, 0.0F, false, 1028, 20, 55, null, this::spellCastReset, me -> {
            this.setAction(WitherLich.Action.ATTACKING);
            this.setAttackAction(WitherLich.AttackAction.BREATH_INSECTS);
            this.m_5496_(SFX.Entity.WitherLich.BREATH_ATTACK, 1.0F, 0.8F + (float) (Math.random() * 0.4));
            return true;
        }).setUninterruptable().setUsePredicate(this::spellCastPredicate));
        this.f_21345_.addGoal(3, new BossMonster.CastSpellAtTargetGoal(frostBolt, moveSpeed, 600, 20.0F, 0.0F, false, 1029, 20, 55, null, this::spellCastReset, me -> {
            this.setAction(WitherLich.Action.ATTACKING);
            this.setAttackAction(WitherLich.AttackAction.BREATH_FROST);
            this.m_5496_(SFX.Entity.WitherLich.BREATH_ATTACK, 1.0F, 0.8F + (float) (Math.random() * 0.4));
            int initialDelay = 10;
            int interval = 20;
            for (int i = 0; i < 3; i++) {
                int delay = initialDelay + i * interval;
                this.setTimer("frost_breath", delay, () -> EntityUtil.getEntitiesWithinCone(this.m_9236_(), this.m_20182_(), this.getLookVector(), 20.0F, -60.0F, 60.0F, e -> e != this && (!SummonUtils.isSummon(e) || SummonUtils.getSummoner(e) != this)).forEach(e -> e.hurt(DamageHelper.createSourcedType(DamageHelper.FROST, this.m_9236_().registryAccess(), this), 15.0F)));
            }
            return false;
        }).setUninterruptable().setUsePredicate(this::spellCastPredicate));
        this.f_21345_.addGoal(7, new MeleeAttackGoal(this, moveSpeed, true));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21346_.addGoal(1, new BossMonster.ThreatTableHurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, le -> true));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, Mob.class, 10, true, false, e -> e instanceof IFactionEnemy && ((IFactionEnemy) e).getFaction() != this.getFaction()));
    }

    @Override
    public boolean hurt(DamageSource type, float amount) {
        if (!type.is(DamageTypeTags.IS_FREEZING) && !type.is(DamageTypeTags.IS_FALL) && !type.is(DamageTypes.STARVE) && !type.is(DamageTypes.WITHER) && !type.is(DamageTypes.IN_WALL) && !type.is(DamageTypes.FLY_INTO_WALL)) {
            if (!type.is(DamageTypes.FELL_OUT_OF_WORLD)) {
                float healthTarget = 0.2F * (float) (this.f_19804_.get(SHIELD_COUNT) - 1);
                if (healthTarget > 0.0F && this.m_21223_() - amount < this.m_21233_() * healthTarget) {
                    if (this.numSummons == 0) {
                        this.setCooldown(1024, 0);
                    } else {
                        amount = this.m_21223_() - healthTarget;
                    }
                }
            }
            return super.hurt(type, amount);
        } else {
            return false;
        }
    }

    @Override
    public void die(DamageSource pCause) {
        if (!pCause.is(DamageTypes.FELL_OUT_OF_WORLD) && this.f_19804_.get(SHIELD_COUNT) > 0) {
            this.m_21153_(1.0F);
        } else {
            this.m_20334_(0.0, 0.0, 0.0);
            this.setAction(WitherLich.Action.DEATH);
            this.setAttackAction(WitherLich.AttackAction.NONE);
            this.m_21219_();
            super.die(pCause);
        }
    }

    @Override
    protected void tickDeath() {
        this.lichDeathTime++;
        if (this.lichDeathTime == 107) {
            if (!this.m_9236_().isClientSide()) {
                this.m_7292_(new MobEffectInstance(EffectInit.MIST_FORM.get(), 200));
            }
            this.f_19794_ = true;
        } else if (this.lichDeathTime > 120 && this.lichDeathTime <= 150) {
            this.m_20334_(0.0, 0.5, 0.0);
        } else if (this.lichDeathTime > 150) {
            this.m_142687_(Entity.RemovalReason.KILLED);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(SHIELD_COUNT, (byte) 5);
        this.f_19804_.define(TEXTURE_VARIANT, (byte) ((int) (Math.random() * 4.0)));
        this.f_19804_.define(FACTION_JOIN_PLAYER, Optional.empty());
        this.f_19804_.define(REACHING, false);
        this.f_19804_.define(IS_JOINING, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putByte("shieldCount", this.f_19804_.get(SHIELD_COUNT));
        nbt.putByte("textureVariant", this.f_19804_.get(TEXTURE_VARIANT));
        nbt.putInt("power_word_death", this.powerWordDeathTicks);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("shieldCount")) {
            this.f_19804_.set(SHIELD_COUNT, nbt.getByte("shieldCount"));
        }
        if (nbt.contains("textureVariant")) {
            this.f_19804_.set(TEXTURE_VARIANT, nbt.getByte("textureVariant"));
        }
        if (nbt.contains("power_word_death")) {
            this.powerWordDeathTicks = nbt.getInt("power_word_death");
        }
        this.setAction(WitherLich.Action.IDLE);
        this.setAttackAction(WitherLich.AttackAction.NONE);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.powerWordDeathTicks > 0) {
            this.powerWordDeathBar.setProgress((float) this.powerWordDeathTicks / (float) this.getPowerWordDeathTime());
        } else {
            this.powerWordDeathBar.setVisible(false);
        }
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        if (this.shouldShowBossBar()) {
            this.powerWordDeathBar.addPlayer(player);
        }
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        if (this.shouldShowBossBar()) {
            this.powerWordDeathBar.removePlayer(player);
        }
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (!this.isOnCooldown(1) && this.getAction() == WitherLich.Action.IDLE) {
            ArrayList<WitherLich.AttackAction> actionPool = new ArrayList();
            actionPool.add(WitherLich.AttackAction.SWING_LEFT);
            actionPool.add(WitherLich.AttackAction.SWING_RIGHT);
            WitherLich.AttackAction selected = (WitherLich.AttackAction) actionPool.get((int) ((double) actionPool.size() * Math.random()));
            this.setAction(WitherLich.Action.ATTACKING);
            this.setAttackAction(selected);
            this.m_5496_(SFX.Entity.Odin.ATTACK, 1.0F, (float) (0.6 + Math.random() * 0.8));
            this.m_5496_(SFX.Entity.WitherLich.ATTACK, 1.0F, (float) (0.6 + Math.random() * 0.8));
            this.setTimer("", selected.animLength, () -> this.damageEntity(entityIn));
            this.setTimer("resetattack", selected.resetTime, () -> {
                this.setAttackAction(WitherLich.AttackAction.NONE);
                this.setAction(WitherLich.Action.IDLE);
                this.setCooldown(1, 20);
            });
            return true;
        } else {
            return true;
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SFX.Entity.WitherLich.HIT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SFX.Entity.WitherLich.DEATH;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SFX.Entity.WitherLich.IDLE;
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    protected PlayState handleAnimState(AnimationState<? extends BossMonster<?>> state) {
        RawAnimation builder = RawAnimation.begin();
        if (this.getAction() == WitherLich.Action.FJ_CONVERTING) {
            if (!this.f_19804_.get(REACHING)) {
                builder.thenLoop("animation.WitherLich.move");
            } else {
                builder.thenPlay("animation.WitherLich.reach");
            }
            return state.setAndContinue(builder);
        } else {
            if (this.getAction() == WitherLich.Action.DEATH) {
                builder.thenPlay("animation.WitherLich.death");
            } else if (this.getAction() == WitherLich.Action.SHIELDED_STRIKE) {
                builder.thenPlay("animation.WitherLich.shielded_strike");
            } else if (this.getAction() == WitherLich.Action.POWER_WORD_DEATH) {
                builder.thenPlay("animation.WitherLich.point");
            } else if (this.getAction() == WitherLich.Action.SUMMON) {
                builder.thenPlay("animation.WitherLich.cast_2h_raise");
            } else if (this.getAction() == WitherLich.Action.ATTACKING) {
                switch(this.getAttackAction()) {
                    case BREATH_FROST:
                    case BREATH_INSECTS:
                        builder.thenLoop("animation.WitherLich.breath_attack");
                        break;
                    case CAST_1_LEFT:
                        builder.thenPlay("animation.WitherLich.cast_1_left");
                        break;
                    case CAST_1_RIGHT:
                        builder.thenPlay("animation.WitherLich.cast_1_right");
                        break;
                    case SWING_LEFT:
                        builder.thenPlay("animation.WitherLich.cast_2_left");
                        break;
                    case SWING_RIGHT:
                        builder.thenPlay("animation.WitherLich.cast_2_right");
                        break;
                    case NONE:
                    default:
                        builder.thenLoop("animation.WitherLich.idle");
                }
            } else {
                double movementSpeed = this.m_20184_().add(0.0, -this.m_20184_().y, 0.0).length();
                if (movementSpeed > 0.2) {
                    builder.thenLoop("animation.WitherLich.move");
                } else {
                    builder.thenLoop("animation.WitherLich.idle");
                }
            }
            return state.setAndContinue(builder);
        }
    }

    public static AttributeSupplier.Builder getGlobalAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 850.0).add(Attributes.MOVEMENT_SPEED, 0.7F).add(Attributes.FOLLOW_RANGE, 40.0).add(Attributes.ARMOR, 14.0).add(Attributes.ATTACK_DAMAGE, 20.0).add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    @Override
    protected ServerBossEvent getBossEvent() {
        return this.bossEvent;
    }

    @Override
    public void setupSpawn() {
        this.setInvulnerableTicks(80);
        this.m_21153_(this.m_21233_() / 3.0F);
    }

    @Override
    protected void spawnParticles() {
        if (this.getAttackAction() == WitherLich.AttackAction.BREATH_FROST) {
            this.spawnConeParticles(new MAParticleType(ParticleInit.FROST.get().setScale(0.2F)), 25);
        } else if (this.getAttackAction() == WitherLich.AttackAction.BREATH_INSECTS) {
            this.spawnConeParticles(new MAParticleType(ParticleInit.BLUE_SPARKLE_VELOCITY.get()).setColor(69, 66, 30), 10);
        }
    }

    private void spawnConeParticles(MAParticleType particle, int count) {
        Vec3 forward = Vec3.directionFromRotation(this.m_146909_(), this.m_6080_()).scale(0.25);
        Vec3 origin = this.m_20182_().add(forward.x, forward.y + 1.5, forward.z);
        for (int i = 0; i < count; i++) {
            Vector3f direction = new Vector3f((float) forward.x, (float) forward.y, (float) forward.z);
            direction.rotate(Axis.YP.rotationDegrees((float) (-15 + (int) (Math.random() * 30.0))));
            direction.rotate(Axis.ZN.rotationDegrees((float) (-15 + (int) (Math.random() * 30.0))));
            direction.normalize();
            direction.mul((float) (0.5 + Math.random()));
            this.m_9236_().addParticle(particle, origin.x + (double) direction.x(), origin.y + (double) direction.y(), origin.z + (double) direction.z(), (double) direction.x(), (double) direction.y(), (double) direction.z());
        }
    }

    @Override
    public ResourceLocation getArenaStructureID() {
        return RLoc.create("faction/boss/cathedral");
    }

    @Override
    public int getArenaStructureSegment() {
        return 2;
    }

    @Override
    public HashMap<String, Integer> getDamageResists() {
        return Maps.newHashMap();
    }

    @Override
    public IFaction getFaction() {
        return Factions.UNDEAD;
    }

    @Override
    public Player getRaidTarget() {
        return null;
    }

    @Override
    public int getTier() {
        return 3;
    }

    @Override
    public void setRaidTarget(Player player) {
    }

    @Override
    public void setTier(int tier) {
    }

    public static enum Action {

        IDLE,
        FJ_CONVERTING,
        ATTACKING,
        POWER_WORD_DEATH,
        SUMMON,
        SHIELDED_STRIKE,
        DEATH
    }

    public static enum AttackAction {

        NONE(0, 0),
        CAST_1_LEFT(10, 14),
        CAST_1_RIGHT(10, 14),
        SWING_LEFT(4, 8),
        SWING_RIGHT(4, 8),
        BREATH_FROST(48, 54),
        BREATH_INSECTS(48, 54);

        public final int animLength;

        public final int resetTime;

        private AttackAction(int animLength, int resetTime) {
            this.animLength = animLength;
            this.resetTime = resetTime;
        }
    }

    static class MoveHelperController extends MoveControl {

        private final WitherLich parentEntity;

        private int courseChangeCooldown;

        public MoveHelperController(WitherLich lich) {
            super(lich);
            this.parentEntity = lich;
        }

        @Override
        public void tick() {
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                float minMoveSpeed = 0.1F;
                float maxMoveSpeed = 0.6F;
                Vec3 v = new Vec3(this.f_24975_, this.f_24976_, this.f_24977_);
                double distSqr = v.distanceToSqr(this.parentEntity.m_20182_());
                if (distSqr < 16.0) {
                    double pct = distSqr / 16.0;
                    this.f_24978_ = (double) MathUtils.lerpf(minMoveSpeed, maxMoveSpeed, (float) pct);
                    this.parentEntity.m_20256_(this.parentEntity.m_20184_().scale(pct));
                    if (this.parentEntity.m_5448_() != null) {
                        this.parentEntity.m_7618_(EntityAnchorArgument.Anchor.FEET, this.parentEntity.m_5448_().m_146892_());
                    }
                } else {
                    this.f_24978_ = (double) maxMoveSpeed;
                }
                if (this.courseChangeCooldown-- <= 0) {
                    this.courseChangeCooldown = this.courseChangeCooldown + this.parentEntity.m_217043_().nextInt(5) + 2;
                    Vec3 vector3d = new Vec3(this.f_24975_ - this.parentEntity.m_20185_(), this.f_24976_ - this.parentEntity.m_20186_(), this.f_24977_ - this.parentEntity.m_20189_());
                    double d0 = vector3d.length();
                    vector3d = vector3d.normalize();
                    if (this.isCollided(vector3d, Mth.ceil(d0))) {
                        this.parentEntity.m_20256_(this.parentEntity.m_20184_().add(vector3d.scale(0.1)));
                    } else {
                        this.f_24981_ = MoveControl.Operation.WAIT;
                    }
                }
            }
        }

        private boolean isCollided(Vec3 p_220673_1_, int p_220673_2_) {
            AABB axisalignedbb = this.parentEntity.m_20191_();
            for (int i = 1; i < p_220673_2_; i++) {
                axisalignedbb = axisalignedbb.move(p_220673_1_);
                if (!this.parentEntity.m_9236_().m_45756_(this.parentEntity, axisalignedbb)) {
                    return false;
                }
            }
            return true;
        }
    }

    class PowerWordDeathGoal extends Goal {

        private int tickCount = 0;

        private LivingEntity target = null;

        public PowerWordDeathGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }

        @Override
        public boolean canUse() {
            return WitherLich.this.powerWordDeathTicks >= WitherLich.this.getPowerWordDeathTime() && WitherLich.this.m_5448_() != null;
        }

        @Override
        public boolean canContinueToUse() {
            boolean hasTarget = this.target != null;
            boolean timer = this.tickCount < 80;
            return hasTarget && timer;
        }

        @Override
        public void start() {
            WitherLich.this.m_21573_().stop();
            WitherLich.this.setAction(WitherLich.Action.POWER_WORD_DEATH);
            this.target = WitherLich.this.m_5448_();
            WitherLich.this.m_5496_(SFX.Entity.WitherLich.PW_DEATH, 1.0F, 1.0F);
        }

        @Override
        public void stop() {
            this.tickCount = 1;
            WitherLich.this.powerWordDeathTicks = 1;
            WitherLich.this.setAction(WitherLich.Action.IDLE);
            WitherLich.this.setAttackAction(WitherLich.AttackAction.NONE);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            this.tickCount++;
            WitherLich.this.setAction(WitherLich.Action.POWER_WORD_DEATH);
            WitherLich.this.m_7618_(EntityAnchorArgument.Anchor.FEET, this.target.m_146892_());
            if (this.tickCount == 44) {
                this.target.hurt(DamageHelper.createSourcedType(DamageTypes.FELL_OUT_OF_WORLD, WitherLich.this.m_9236_().registryAccess(), WitherLich.this), this.target.getMaxHealth() * 0.9F);
            }
        }
    }

    class ShieldedStrikeGoal extends Goal {

        private int tickCount = 0;

        @Override
        public boolean canUse() {
            return WitherLich.this.shouldShieldedStrike || WitherLich.this.getAction() == WitherLich.Action.SHIELDED_STRIKE;
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse() && this.tickCount < 33;
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void start() {
            this.tickCount = 0;
            WitherLich.this.setAction(WitherLich.Action.SHIELDED_STRIKE);
        }

        @Override
        public void stop() {
            WitherLich.this.setAttackAction(WitherLich.AttackAction.NONE);
            WitherLich.this.setAction(WitherLich.Action.IDLE);
            WitherLich.this.powerWordDeathTicks = 1;
        }

        @Override
        public void tick() {
            this.tickCount++;
            if (this.tickCount == 5) {
                WitherLich.this.decrementShield();
            }
        }
    }

    class SummonCandidate {

        public final EntityType<? extends Mob> type;

        public final int weight;

        public SummonCandidate(EntityType<? extends Mob> type, int weight) {
            this.type = type;
            this.weight = weight;
        }
    }

    class SummonMinionsGoal extends Goal {

        private static final int DURATION = 62;

        private int tickCount = 0;

        private int summonInterval = 1;

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public boolean canUse() {
            boolean noSummons = WitherLich.this.numSummons == 0;
            boolean offCooldown = !WitherLich.this.isOnCooldown(1024);
            boolean isIdle = WitherLich.this.getAction() == WitherLich.Action.IDLE || WitherLich.this.getAction() == WitherLich.Action.SUMMON;
            return noSummons && offCooldown && isIdle;
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return WitherLich.this.getAction() == WitherLich.Action.SUMMON || this.tickCount < 62;
        }

        @Override
        public void start() {
            WitherLich.this.setAction(WitherLich.Action.SUMMON);
            WitherLich.this.buildSummonList();
            this.summonInterval = Math.max(62 / WitherLich.this.summonQueue.size(), 1);
            WitherLich.this.m_5496_(SFX.Entity.WitherLich.SUMMON, 1.0F, 1.0F);
        }

        @Override
        public void stop() {
            WitherLich.this.setCooldown(1024, Integer.MAX_VALUE);
            WitherLich.this.setAttackAction(WitherLich.AttackAction.NONE);
            WitherLich.this.setAction(WitherLich.Action.IDLE);
            this.tickCount = 0;
        }

        @Override
        public void tick() {
            this.tickCount++;
            if (this.tickCount % this.summonInterval == 0) {
                WitherLich.this.createSummon();
            }
            if (this.tickCount > 62) {
                this.stop();
            }
        }
    }

    class WitherLichFly extends Goal {

        public WitherLichFly() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            MoveControl movementcontroller = WitherLich.this.m_21566_();
            if (!movementcontroller.hasWanted()) {
                return true;
            } else if (WitherLich.this.m_5448_() == null) {
                double d0 = movementcontroller.getWantedX() - WitherLich.this.m_20185_();
                double d1 = movementcontroller.getWantedY() - WitherLich.this.m_20186_();
                double d2 = movementcontroller.getWantedZ() - WitherLich.this.m_20189_();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                return d3 < 4.0 || d3 > 3600.0;
            } else {
                double dist = WitherLich.this.m_5448_().m_20280_(WitherLich.this);
                return !WitherLich.this.m_21574_().hasLineOfSight(WitherLich.this.m_5448_()) || dist > 400.0;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            if (WitherLich.this.m_5448_() != null) {
                Vec3 direction = WitherLich.this.m_20182_().subtract(WitherLich.this.m_5448_().m_20182_()).normalize();
                direction = direction.add(0.0, -direction.y, 0.0);
                Vec3 offset = direction.scale(10.0);
                Vec3 target = WitherLich.this.m_5448_().m_20182_().add(offset);
                if (WitherLich.this.m_5448_().m_20096_()) {
                    target = target.add(0.0, 1.5, 0.0);
                }
                WitherLich.this.m_21566_().setWantedPosition(target.x, target.y, target.z, 1.0);
            }
        }
    }
}