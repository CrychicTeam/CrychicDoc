package com.mna.entities.boss;

import com.mna.api.affinity.Affinity;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.entities.IFactionEnemy;
import com.mna.api.faction.IFaction;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.collections.Components;
import com.mna.api.spells.collections.Shapes;
import com.mna.api.tools.RLoc;
import com.mna.blocks.BlockInit;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.effects.EffectInit;
import com.mna.entities.EntityInit;
import com.mna.entities.boss.attacks.ThrownWeapon;
import com.mna.entities.boss.effects.DemonReturnPortal;
import com.mna.entities.sorcery.targeting.SpellProjectile;
import com.mna.factions.Factions;
import com.mna.items.ItemInit;
import com.mna.spells.components.ComponentFling;
import com.mna.spells.crafting.SpellRecipe;
import com.mna.tools.EntityUtil;
import com.mna.tools.math.MathUtils;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ToolActions;
import org.apache.commons.lang3.mutable.MutableBoolean;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.keyframe.event.CustomInstructionKeyframeEvent;
import software.bernie.geckolib.core.object.PlayState;

public class DemonLord extends BossMonster<DemonLord> implements IFactionEnemy<DemonLord> {

    private final ServerBossEvent bossEvent = (ServerBossEvent) new ServerBossEvent(this.m_5446_(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_20).setDarkenScreen(true);

    protected static final EntityDataAccessor<String> SUMMONER_UUID = SynchedEntityData.defineId(DemonLord.class, EntityDataSerializers.STRING);

    protected static final EntityDataAccessor<Boolean> IS_JOINING = SynchedEntityData.defineId(DemonLord.class, EntityDataSerializers.BOOLEAN);

    protected static final EntityDataAccessor<Byte> PHASE = SynchedEntityData.defineId(DemonLord.class, EntityDataSerializers.BYTE);

    protected static final EntityDataAccessor<Integer> INCINERATE_ID = SynchedEntityData.defineId(DemonLord.class, EntityDataSerializers.INT);

    protected static final EntityDataAccessor<BlockPos> PULL_POS = SynchedEntityData.defineId(DemonLord.class, EntityDataSerializers.BLOCK_POS);

    private static final float LIFT_SPEED = 0.055F;

    private static final int COOLDOWN_LEAP_ID = 1024;

    private static final int COOLDOWN_SPARKS_ID = 1025;

    private static final int COOLDOWN_THUNDER_ID = 1026;

    private static final int COOLDOWN_GRASP_OF_FIRE_ID = 1027;

    private static final int COOLDOWN_HELLBALL_ID = 1028;

    private Player summonedBy;

    private LivingEntity _incinerateTarget;

    @Nullable
    private BlockPos vortexTarget = null;

    private int demonDeathTime;

    private float forceYRot = 0.0F;

    private int globalSpellCooldown = 0;

    private int queuedPhaseTransition = 0;

    public DemonLord(EntityType<? extends DemonLord> type, Level worldIn) {
        super(type, worldIn);
        this.setWeaponState(DemonLord.WeaponState.AXE);
    }

    public DemonLord(Level worldIn) {
        this(EntityInit.DEMON_LORD.get(), worldIn);
    }

    public DemonLord(Level level, Vec3 position) {
        this(level);
        this.m_146884_(position);
    }

    private void lockTargetInPlace(LivingEntity target, boolean raise) {
        int duration = 200;
        target.removeEffect(EffectInit.LIFT.get());
        target.addEffect(new MobEffectInstance(EffectInit.LIFT.get(), duration, raise ? 1 : 0));
        if (raise && target instanceof Player) {
            EntityUtil.SetLiftSpeed((Player) target, 0.055F);
        }
    }

    private void setupPhase2Transition() {
        this.m_20331_(true);
        this.m_6710_(null);
        this.setAction(DemonLord.Action.PHASE_2_TRANSITION);
        this.setTimer("remove_weapon", 10, () -> {
            this.setWeaponState(DemonLord.WeaponState.NOTHING);
            if (!this.m_9236_().isClientSide()) {
                ThrownWeapon thrown = new ThrownWeapon(EntityInit.THROWN_WEAPON.get(), this, this.m_9236_(), new ItemStack(ItemInit.ALLFATHER_AXE.get()));
                thrown.setScale(2.0F);
                Vec3 direction = Vec3.directionFromRotation(new Vec2(0.0F, this.m_146908_()));
                thrown.shoot(direction.x, direction.y, direction.z, 2.5F, 0.0F);
                this.m_9236_().m_7967_(thrown);
            }
        });
        this.setTimer("set_swords", 38, () -> {
            this.setWeaponState(DemonLord.WeaponState.SWORDS);
            this.m_5496_(SFX.Entity.DemonLord.DRAW_SWORDS, 1.0F, 1.0F);
        });
        this.setTimer("fire_shield", 80, () -> {
            this.m_7292_(new MobEffectInstance(EffectInit.FIRE_SHIELD.get(), 99999, 2));
            this.m_5496_(SFX.Spell.Buff.FIRE, 1.0F, (float) (0.6 + Math.random() * 0.8));
            this.m_5496_(SFX.Entity.DemonLord.SCREAM, 1.0F, (float) (0.6 + Math.random() * 0.8));
        });
    }

    private void setupPhase3Transition() {
        this.m_20331_(true);
        this.setAction(DemonLord.Action.PHASE_3_TRANSITION);
        this.m_6710_(null);
        this.setTimer("remove_weapon", 10, () -> {
            this.setWeaponState(DemonLord.WeaponState.NOTHING);
            if (!this.m_9236_().isClientSide()) {
                float degreeDifference = -25.0F;
                for (int i = 0; i < 2; i++) {
                    ThrownWeapon thrown = new ThrownWeapon(EntityInit.THROWN_WEAPON.get(), this, this.m_9236_(), new ItemStack(ItemInit.ALLFATHER_AXE.get()));
                    thrown.setScale(2.0F);
                    Vec3 direction = Vec3.directionFromRotation(new Vec2(0.0F, this.m_146908_() + degreeDifference));
                    thrown.shoot(direction.x, direction.y, direction.z, 2.5F, 0.0F);
                    this.m_9236_().m_7967_(thrown);
                    degreeDifference += 50.0F;
                }
            }
        });
        this.setTimer("set_staff", 64, () -> {
            this.setWeaponState(DemonLord.WeaponState.STAFF);
            this.m_5496_(SFX.Entity.DemonLord.SUMMON_STAFF, 1.0F, 1.0F);
        });
    }

    @Nullable
    private LivingEntity getIncinerateTarget() {
        int id = this.f_19804_.get(INCINERATE_ID);
        if (this._incinerateTarget == null) {
            if (id > -1) {
                Entity e = this.m_9236_().getEntity(id);
                if (e != null && e instanceof LivingEntity) {
                    this._incinerateTarget = (LivingEntity) e;
                }
            }
        } else if (id == -1) {
            this._incinerateTarget = null;
        }
        return this._incinerateTarget;
    }

    @Nullable
    private Vec3 getPullPos() {
        BlockPos pos = this.f_19804_.get(PULL_POS);
        return pos != null && !BlockPos.ZERO.equals(pos) ? Vec3.atCenterOf(pos) : null;
    }

    private UUID getSummonerGuid() {
        try {
            return UUID.fromString(this.f_19804_.get(SUMMONER_UUID));
        } catch (Throwable var2) {
            return null;
        }
    }

    private Player getSummonedBy() {
        if (this.summonedBy == null && this.getSummonerGuid() != null) {
            this.summonedBy = this.m_9236_().m_46003_(this.getSummonerGuid());
        }
        return this.summonedBy;
    }

    private boolean naturalSpawn() {
        return !this.f_19804_.get(IS_JOINING);
    }

    public DemonLord.WeaponState getWeaponState() {
        int ordinal = this.getStateFlag() & 3;
        return ordinal < DemonLord.WeaponState.values().length ? DemonLord.WeaponState.values()[ordinal] : DemonLord.WeaponState.NOTHING;
    }

    private DemonLord.Action getAction() {
        int ordinal = (this.getStateFlag() & 2040) >> 3 & 0xFF;
        return ordinal >= DemonLord.Action.values().length ? DemonLord.Action.IDLE : DemonLord.Action.values()[ordinal];
    }

    private DemonLord.AttackAction getAttackAction() {
        int ordinal = (this.getStateFlag() & 65535) >> 12 & 15;
        return ordinal >= DemonLord.AttackAction.values().length ? DemonLord.AttackAction.NONE : DemonLord.AttackAction.values()[ordinal];
    }

    private boolean isPhaseTransitioning() {
        return this.getAction() == DemonLord.Action.PHASE_2_TRANSITION || this.getAction() == DemonLord.Action.PHASE_3_TRANSITION;
    }

    public void setSummoner(Player player) {
        if (player != null && player.getGameProfile() != null) {
            this.f_19804_.set(IS_JOINING, true);
            this.f_19804_.set(SUMMONER_UUID, player.getGameProfile().getId().toString());
            this.summonedBy = player;
            this.m_20331_(true);
            this.f_21345_.availableGoals.clear();
            this.f_21346_.availableGoals.clear();
            this.setAction(DemonLord.Action.FJ_WAITING_ITEM);
        }
    }

    private void setWeaponState(DemonLord.WeaponState state) {
        byte ordinal = (byte) (state.ordinal() & 3);
        int flag = this.getStateFlag();
        flag &= -4;
        flag |= ordinal;
        this.setState(new int[] { flag });
    }

    private void setAction(DemonLord.Action action) {
        byte ordinal = (byte) ((action.ordinal() & 0xFF) << 3);
        int flag = this.getStateFlag();
        flag &= -2041;
        flag |= ordinal;
        this.setState(new int[] { flag });
    }

    private void setAttackAction(DemonLord.AttackAction action) {
        int ordinal = (action.ordinal() & 15) << 12;
        int flag = this.getStateFlag();
        flag &= -61441;
        flag |= ordinal;
        this.setState(new int[] { flag });
    }

    private void setIncinerateTarget(@Nullable LivingEntity target) {
        this._incinerateTarget = target;
        if (!this.m_9236_().isClientSide()) {
            this.f_19804_.set(INCINERATE_ID, target != null ? target.m_19879_() : -1);
        }
    }

    private void setPullPos(@Nullable BlockPos pos) {
        this.f_19804_.set(PULL_POS, pos == null ? BlockPos.ZERO : pos);
    }

    @Override
    protected void spawnParticles() {
        for (int i = 0; i < 10; i++) {
            LivingEntity incinerateTarget = this.getIncinerateTarget();
            if (incinerateTarget != null) {
                float bbOffset = incinerateTarget.m_20205_();
                float bbHeightOffset = incinerateTarget.m_20206_() / 2.0F;
                this.m_9236_().addParticle(new MAParticleType(ParticleInit.HELLFIRE.get()), incinerateTarget.m_20185_() - (double) (bbOffset / 2.0F) + Math.random() * (double) bbOffset, incinerateTarget.m_20186_() - (double) bbHeightOffset, incinerateTarget.m_20189_() - (double) (bbOffset / 2.0F) + Math.random() * (double) bbOffset, 0.0, 0.165 + Math.random() * 0.165, 0.0);
            }
        }
        if (this.getAction() == DemonLord.Action.GRASP_OF_FIRE) {
            Vec3 graspPos = this.getPullPos();
            if (graspPos != null) {
                for (int ix = 0; ix < 10; ix++) {
                    Vec3 pos = graspPos.add(-2.0 + Math.random() * 4.0, -2.0 + Math.random() * 4.0, -2.0 + Math.random() * 4.0);
                    this.m_9236_().addParticle(new MAParticleType(ParticleInit.AIR_LERP.get()).setScale(0.2F).setColor(10, 10, 10).setMaxAge(5 + (int) (Math.random() * 5.0)), pos.x, pos.y, pos.z, graspPos.x, graspPos.y, graspPos.z);
                }
            }
        }
    }

    @Override
    public void setupSpawn() {
        this.setInvulnerableTicks(80);
        this.m_21153_(this.m_21233_() / 3.0F);
    }

    @Override
    protected int defaultState() {
        return DemonLord.WeaponState.AXE.ordinal() & 3;
    }

    @Override
    public ResourceLocation getArenaStructureID() {
        return RLoc.create("faction/boss/demon_arena");
    }

    @Override
    public int getArenaStructureSegment() {
        return 0;
    }

    @Override
    public void tick() {
        if (this.m_21223_() < 1.0F && (this.queuedPhaseTransition > 0 || this.getAction() == DemonLord.Action.PHASE_2_TRANSITION || this.getAction() == DemonLord.Action.PHASE_3_TRANSITION)) {
            this.m_20331_(true);
            this.m_21153_(1.0F);
        }
        if (this.getAction() == DemonLord.Action.IDLE) {
            if (this.queuedPhaseTransition == 2) {
                this.setupPhase2Transition();
                this.queuedPhaseTransition = -1;
            } else if (this.queuedPhaseTransition == 3) {
                this.setupPhase3Transition();
                this.queuedPhaseTransition = -1;
            }
        }
        super.tick();
        if (this.globalSpellCooldown > 0) {
            this.globalSpellCooldown--;
        }
        if (this.getAction() == DemonLord.Action.DEATH) {
            this.m_146922_(this.forceYRot);
        }
        boolean remove = false;
        if (this._incinerateTarget != null && !this.m_9236_().isClientSide() && this.m_9236_().getGameTime() % 10L == 0L) {
            float incinerateDamage = this.naturalSpawn() ? 6.0F : (float) (6 + 2 * this.f_19804_.get(PHASE));
            this._incinerateTarget.hurt(this.m_269291_().inFire(), incinerateDamage);
        }
        if (!this.naturalSpawn()) {
            if (this.getSummonedBy() == null) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
                return;
            }
            this.m_7618_(EntityAnchorArgument.Anchor.FEET, this.getSummonedBy().m_146892_());
            if (!this.getSummonedBy().m_21224_() && !(this.getSummonedBy().m_20280_(this) > 256.0)) {
                remove = this.f_19797_ > 700;
            } else {
                remove = true;
            }
            if (remove && !this.m_9236_().isClientSide()) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        } else if (this.getAction() == DemonLord.Action.PHASE_2_TRANSITION || this.getAction() == DemonLord.Action.PHASE_3_TRANSITION) {
            this.m_21573_().stop();
            this.m_20256_(Vec3.ZERO);
            if (this.m_5448_() != null) {
                this.m_7618_(EntityAnchorArgument.Anchor.EYES, this.m_5448_().m_20182_());
            }
            this.m_5634_(2.5F);
            if (this.m_21223_() == this.m_21233_()) {
                this.f_19804_.set(PHASE, (byte) (this.f_19804_.get(PHASE) + 1));
                this.m_20331_(false);
                this.setAction(DemonLord.Action.IDLE);
            }
        } else if (this.getAction() == DemonLord.Action.IDLE && this.m_20147_()) {
            this.m_20331_(false);
        }
        if (this.m_21223_() < 0.0F) {
            this.setAction(DemonLord.Action.DEATH);
        }
        if (this.getAction() == DemonLord.Action.ATTACKING && this.m_5448_() != null) {
            this.m_7618_(EntityAnchorArgument.Anchor.FEET, this.m_5448_().m_20182_());
        }
    }

    @Override
    protected void registerGoals() {
        ItemStack sparks = new ItemStack(ItemInit.SPELL.get());
        ItemStack thunder = new ItemStack(ItemInit.SPELL.get());
        ItemStack hellball = new ItemStack(ItemInit.SPELL.get());
        SpellRecipe sparksSpell = new SpellRecipe();
        sparksSpell.setShape(Shapes.PROJECTILE);
        sparksSpell.getShape().setValue(Attribute.MAGNITUDE, 5.0F);
        sparksSpell.addComponent(Components.LIGHTNING_DAMAGE);
        sparksSpell.addComponent(Components.LIVING_BOMB);
        sparksSpell.getComponent(0).setValue(Attribute.DAMAGE, 10.0F);
        sparksSpell.getComponent(1).setValue(Attribute.DURATION, 5.0F);
        sparksSpell.getComponent(1).setValue(Attribute.MAGNITUDE, 3.0F);
        sparksSpell.writeToNBT(sparks.getOrCreateTag());
        SpellRecipe thunderSpell = new SpellRecipe();
        thunderSpell.setShape(Shapes.SMITE);
        thunderSpell.getShape().setValue(Attribute.HEIGHT, 3.0F);
        thunderSpell.getShape().setValue(Attribute.RADIUS, 5.0F);
        thunderSpell.addComponent(Components.LIGHTNING_DAMAGE);
        thunderSpell.getComponent(0).setValue(Attribute.DAMAGE, 20.0F);
        thunderSpell.getComponent(0).setValue(Attribute.LESSER_MAGNITUDE, 10.0F);
        thunderSpell.getComponent(0).setValue(Attribute.RADIUS, 10.0F);
        thunderSpell.addComponent(Components.GRAVITY_WELL);
        thunderSpell.getComponent(1).setValue(Attribute.DURATION, 5.0F);
        thunderSpell.writeToNBT(thunder.getOrCreateTag());
        SpellRecipe hellballSpell = new SpellRecipe();
        hellballSpell.setShape(Shapes.PROJECTILE);
        hellballSpell.getShape().setValue(Attribute.RADIUS, 10.0F);
        hellballSpell.addComponent(Components.EXPLOSION);
        hellballSpell.getComponent(0).setValue(Attribute.DAMAGE, 20.0F);
        hellballSpell.getComponent(0).setValue(Attribute.RADIUS, 10.0F);
        hellballSpell.getComponent(0).setValue(Attribute.PRECISION, 2.0F);
        hellballSpell.addComponent(Components.FLING);
        hellballSpell.getComponent(1).setValue(Attribute.SPEED, 3.0F);
        hellballSpell.setOverrideAffinity(Affinity.HELLFIRE);
        hellballSpell.writeToNBT(hellball.getOrCreateTag());
        double moveSpeed = this.m_21133_(Attributes.MOVEMENT_SPEED);
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(0, new DemonLord.PhaseTransitionGoal(this));
        this.f_21345_.addGoal(1, new DemonLord.HellballGoal(hellball));
        this.f_21345_.addGoal(2, new DemonLord.GraspOfFireGoal());
        this.f_21345_.addGoal(3, new BossMonster.CastSpellAtTargetGoal(sparks, moveSpeed, 300, 32.0F, 8.0F, true, 1025, 10, 20, null, () -> {
            this.setAction(DemonLord.Action.IDLE);
            this.setAttackAction(DemonLord.AttackAction.NONE);
            if (this.f_19804_.get(PHASE) == 3) {
                this.globalSpellCooldown = 10 + (int) (Math.random() * 30.0);
            } else {
                this.globalSpellCooldown = 40 + (int) (Math.random() * 60.0);
            }
        }, me -> {
            switch(this.f_19804_.get(PHASE)) {
                case 3:
                    if (Math.random() < 0.5) {
                        this.setAttackAction(DemonLord.AttackAction.STAFF_CAST_1);
                    } else {
                        this.setAttackAction(DemonLord.AttackAction.STAFF_CAST_2);
                    }
                    break;
                default:
                    this.setAttackAction(DemonLord.AttackAction.CAST_1);
            }
            this.setAction(DemonLord.Action.ATTACKING);
            return true;
        }).setUsePredicate(e -> this.getAction() == DemonLord.Action.IDLE && this.globalSpellCooldown == 0));
        this.f_21345_.addGoal(3, new BossMonster.CastSpellAtTargetGoal(thunder, moveSpeed, 300, 16.0F, 4.0F, true, 1026, 10, 20, null, () -> {
            this.setAction(DemonLord.Action.IDLE);
            this.setAttackAction(DemonLord.AttackAction.NONE);
            if (this.f_19804_.get(PHASE) == 3) {
                this.globalSpellCooldown = 10 + (int) (Math.random() * 30.0);
            } else {
                this.globalSpellCooldown = 40 + (int) (Math.random() * 60.0);
            }
        }, me -> {
            switch(this.f_19804_.get(PHASE)) {
                case 3:
                    this.setAttackAction(DemonLord.AttackAction.STAFF_CAST_2);
                    break;
                default:
                    this.setAttackAction(DemonLord.AttackAction.CAST_2);
            }
            this.setAction(DemonLord.Action.ATTACKING);
            return true;
        }).setUsePredicate(e -> this.getAction() == DemonLord.Action.IDLE && this.globalSpellCooldown == 0));
        this.f_21345_.addGoal(4, new DemonLord.LerpLeap(4.0F, 32.0F, 5));
        this.f_21345_.addGoal(5, new MeleeAttackGoal(this, moveSpeed, true));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21346_.addGoal(1, new BossMonster.ThreatTableHurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, le -> true));
        this.f_21346_.addGoal(4, new NearestAttackableTargetGoal(this, Mob.class, 10, true, false, e -> e instanceof IFactionEnemy && ((IFactionEnemy) e).getFaction() != this.getFaction()));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(SUMMONER_UUID, "");
        this.f_19804_.define(PHASE, (byte) 1);
        this.f_19804_.define(INCINERATE_ID, -1);
        this.f_19804_.define(PULL_POS, BlockPos.ZERO);
        this.f_19804_.define(IS_JOINING, false);
    }

    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
        if (!this.naturalSpawn() && this.m_9236_().isClientSide()) {
            for (int i = 0; i < 75; i++) {
                this.m_9236_().addParticle(new MAParticleType(ParticleInit.HELLFIRE.get()), this.m_20185_() - 0.5 + Math.random(), this.m_20186_() + Math.random() * 1.5, this.m_20189_() - 0.5 + Math.random(), -0.01 + Math.random() * 0.02, 0.15F, -0.01 + Math.random() * 0.02);
            }
            Player player = this.getSummonedBy();
            if (player != null && player.m_20270_(this) < 32.0F) {
                for (int i = 0; i < 75; i++) {
                    this.m_9236_().addParticle(new MAParticleType(ParticleInit.HELLFIRE.get()), player.m_20185_(), player.m_20186_() + 1.0, player.m_20189_(), -0.25 + Math.random() * 0.5, 0.15F, -0.25 + Math.random() * 0.5);
                }
            }
        }
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {
        if (this.naturalSpawn()) {
            return InteractionResult.FAIL;
        } else if (player != this.getSummonedBy()) {
            return InteractionResult.FAIL;
        } else if (this.getAction() == DemonLord.Action.FJ_WAITING_ITEM && player.m_21120_(hand).getItem() == ItemInit.MOTE_FIRE.get()) {
            EntityUtil.SetLiftSpeed(player, 0.055F);
            if (!this.m_9236_().isClientSide()) {
                player.m_21211_().shrink(1);
                this.setAction(DemonLord.Action.FJ_IMBUING);
                this.m_9236_().playSound(null, this.m_20183_(), SFX.Event.Ritual.DEMON_SUMMON_CHANNEL, SoundSource.PLAYERS, 1.0F, 1.0F);
                this.lockTargetInPlace(player, false);
                this.setTimer("raise", 10, () -> this.lockTargetInPlace(player, true));
                this.setTimer("advance", 202, () -> {
                    IPlayerProgression progression = (IPlayerProgression) player.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
                    if (progression != null && progression.getTier() < 5) {
                        if (progression.getAlliedFaction() == null) {
                            progression.setAlliedFaction(Factions.DEMONS, player);
                            player.m_213846_(Component.translatable("event.mna.faction_ally_demons"));
                        }
                        if (progression.getAlliedFaction() == Factions.DEMONS) {
                            progression.setTier(progression.getTier() + 1, player);
                            player.m_213846_(Component.translatable("mna:progresscondition.advanced", progression.getTier()));
                        }
                    }
                    player.m_21195_(EffectInit.LIFT.get());
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                });
            }
            this.setTimer("incinerate", 62, () -> {
                this.lockTargetInPlace(player, false);
                this.setIncinerateTarget(player);
            });
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.FAIL;
        }
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (!this.isOnCooldown(1) && this.getAction() == DemonLord.Action.IDLE) {
            ArrayList<DemonLord.AttackAction> actionPool = new ArrayList();
            byte phase = this.f_19804_.get(PHASE);
            if (phase == 1 || phase == 3) {
                actionPool.add(DemonLord.AttackAction.LEFT_OVERHEAD);
                actionPool.add(DemonLord.AttackAction.LEFT_SWEEP);
                actionPool.add(DemonLord.AttackAction.TWOHAND_OVERHEAD);
            } else if (phase == 2) {
                actionPool.add(DemonLord.AttackAction.LEFT_OVERHEAD);
                actionPool.add(DemonLord.AttackAction.LEFT_SWEEP);
                actionPool.add(DemonLord.AttackAction.RIGHT_OVERHEAD);
                actionPool.add(DemonLord.AttackAction.RIGHT_SWEEP);
                actionPool.add(DemonLord.AttackAction.DUALWIELD_OVERHEAD);
                actionPool.add(DemonLord.AttackAction.DUALWIELD_SWEEP);
            }
            DemonLord.AttackAction selected = (DemonLord.AttackAction) actionPool.get((int) ((double) actionPool.size() * Math.random()));
            this.setAction(DemonLord.Action.ATTACKING);
            this.setAttackAction(selected);
            this.setTimer("", selected.animLength, () -> {
                this.damageEntity(entityIn);
                this.m_5496_(SFX.Entity.Odin.ATTACK, 1.0F, (float) (0.6 + Math.random() * 0.8));
                this.m_5496_(SFX.Entity.DemonLord.ATTACK, 1.0F, (float) (0.6 + Math.random() * 0.8));
            });
            this.setTimer("resetattack", selected.resetTime, () -> {
                this.setAttackAction(DemonLord.AttackAction.NONE);
                this.setAction(DemonLord.Action.IDLE);
                this.setCooldown(1, phase == 2 ? 10 : 20);
            });
            return true;
        } else {
            return true;
        }
    }

    public static AttributeSupplier.Builder getGlobalAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 375.0).add(Attributes.MOVEMENT_SPEED, 0.7F).add(Attributes.FOLLOW_RANGE, 40.0).add(Attributes.ARMOR, 14.0).add(Attributes.ATTACK_DAMAGE, 20.0).add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    public boolean damageEntity(Entity entityIn) {
        float f = (float) this.m_21133_(Attributes.ATTACK_DAMAGE);
        float f1 = (float) this.m_21133_(Attributes.ATTACK_KNOCKBACK);
        ArrayList<Entity> targets = new ArrayList();
        if (this.getAttackAction() == DemonLord.AttackAction.LEFT_SWEEP || this.getAttackAction() == DemonLord.AttackAction.RIGHT_SWEEP || this.getAttackAction() == DemonLord.AttackAction.DUALWIELD_SWEEP) {
            EntityUtil.getEntitiesWithinCone(this.m_9236_(), this.m_20182_(), Vec3.directionFromRotation(this.m_146909_(), this.m_6080_()), 6.0F, -60.0F, 60.0F, e -> e instanceof LivingEntity && e.isAlive() && e != this && e != entityIn);
        }
        targets.add(entityIn);
        MutableBoolean hurtTarget = new MutableBoolean(false);
        targets.forEach(t -> {
            boolean hurt = t.hurt(this.m_269291_().mobAttack(this), f);
            if (hurt) {
                hurtTarget.setTrue();
                if (f1 > 0.0F && t instanceof LivingEntity) {
                    ((LivingEntity) t).knockback((double) (f1 * 0.5F), (double) Mth.sin(this.m_146908_() * (float) (Math.PI / 180.0)), (double) (-Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0))));
                    this.m_20256_(this.m_20184_().multiply(0.6, 1.0, 0.6));
                }
                if (t instanceof Player playerentity) {
                    this.maybeDisableShield(playerentity, playerentity.m_6117_() ? playerentity.m_21211_() : ItemStack.EMPTY);
                }
                this.m_21335_(t);
                if (this.f_19796_.nextFloat() < 0.4F && t instanceof Mob) {
                    ((Mob) t).setTarget(this);
                }
            }
        });
        return hurtTarget.getValue();
    }

    private void maybeDisableShield(Player player, ItemStack playerItem) {
        if (!playerItem.isEmpty() && playerItem.getItem().canPerformAction(playerItem, ToolActions.SHIELD_BLOCK) && this.f_19804_.get(PHASE) == 1) {
            player.getCooldowns().addCooldown(Items.SHIELD, 100);
            this.m_9236_().broadcastEntityEvent(player, (byte) 30);
        }
    }

    @Override
    public void die(DamageSource pCause) {
        if (pCause.is(DamageTypes.FELL_OUT_OF_WORLD) && pCause.getEntity() == null) {
            super.die(pCause);
        } else if (this.queuedPhaseTransition > 0) {
            if (this.m_21223_() < 1.0F) {
                this.m_21153_(1.0F);
            }
        } else {
            int phase = this.f_19804_.get(PHASE);
            if (phase == 1) {
                this.m_20331_(true);
                this.m_21153_(1.0F);
                this.queuedPhaseTransition = 2;
            } else if (phase == 2) {
                this.m_20331_(true);
                this.m_21153_(1.0F);
                this.queuedPhaseTransition = 3;
            } else {
                this.setAction(DemonLord.Action.DEATH);
                if (!this.m_9236_().isClientSide()) {
                    this.forceYRot = this.m_146908_();
                    Vec3 direction = Vec3.directionFromRotation(0.0F, this.m_6080_()).normalize().scale(-11.5);
                    Vec3 portalPos = this.m_20182_().add(direction.x, 0.25, direction.z);
                    this.setTimer("death_portal", 30, () -> {
                        this.m_5496_(SFX.Entity.DemonLord.DEATH, 1.0F, 1.0F);
                        DemonReturnPortal portal = new DemonReturnPortal(EntityInit.DEMON_RETURN_PORTAL.get(), this.m_9236_());
                        portal.m_146884_(portalPos);
                        portal.setFocus(this);
                        this.m_9236_().m_7967_(portal);
                    });
                    BlockPos myPos = this.m_20183_();
                    for (int i = -4; i <= 4; i++) {
                        for (int j = -2; j <= 2; j++) {
                            for (int k = -4; k <= 4; k++) {
                                BlockPos offsetPos = myPos.offset(i, j, k);
                                BlockState bs = this.m_9236_().getBlockState(offsetPos);
                                Block block = bs.m_60734_();
                                if (block == BlockInit.HELLFIRE.get() || block == Blocks.FIRE) {
                                    this.m_9236_().setBlock(offsetPos, Blocks.AIR.defaultBlockState(), 3);
                                }
                            }
                        }
                    }
                    this.m_9236_().m_45556_(this.m_20191_().inflate(5.0)).forEach(bsx -> {
                    });
                }
                this.m_21219_();
                super.die(pCause);
            }
        }
    }

    @Override
    protected void tickDeath() {
        this.demonDeathTime++;
        if (this.demonDeathTime == 155 && !this.m_9236_().isClientSide()) {
            this.m_142687_(Entity.RemovalReason.KILLED);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SFX.Entity.DemonLord.IDLE;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SFX.Entity.DemonLord.HIT;
    }

    @Override
    public boolean hurt(DamageSource type, float amount) {
        if (type.is(DamageTypes.FELL_OUT_OF_WORLD) || this.getAction() != DemonLord.Action.PHASE_2_TRANSITION && this.getAction() != DemonLord.Action.PHASE_3_TRANSITION && this.queuedPhaseTransition <= 0) {
            if (type.is(DamageTypeTags.IS_FIRE)) {
                this.m_7292_(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 300, 2));
                if (this.m_9236_().getGameTime() % 5L == 0L) {
                    this.m_5634_(amount);
                }
                return false;
            } else {
                return super.hurt(type, amount);
            }
        } else {
            if (this.m_21223_() < 1.0F) {
                this.m_21153_(1.0F);
            }
            return false;
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putByte("phase", this.f_19804_.get(PHASE));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setAction(DemonLord.Action.IDLE);
        this.f_19804_.set(PHASE, (byte) MathUtils.clamp(nbt.getByte("phase"), 1, 3));
    }

    @Override
    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    protected ServerBossEvent getBossEvent() {
        return this.bossEvent;
    }

    @Override
    protected PlayState handleAnimState(AnimationState<? extends BossMonster<?>> state) {
        RawAnimation builder = RawAnimation.begin();
        if (!this.naturalSpawn()) {
            if (this.getAction() == DemonLord.Action.FJ_WAITING_ITEM) {
                builder.thenLoop("animation.DemonLord.item_wait");
            } else if (this.getAction() == DemonLord.Action.FJ_IMBUING) {
                builder.thenPlay("animation.DemonLord.grasp_of_fire_no_throw");
                builder.thenLoop("animation.DemonLord.grasp_of_fire_no_throw_end_loop");
            }
        } else if (this.getAction() == DemonLord.Action.DEATH) {
            builder.thenPlay("animation.DemonLord.death");
        } else if (this.getAction() == DemonLord.Action.PHASE_2_TRANSITION) {
            builder.thenPlay("animation.DemonLord.phase_transition_2");
            builder.thenLoop("animation.DemonLord.regenerating");
        } else if (this.getAction() == DemonLord.Action.PHASE_3_TRANSITION) {
            builder.thenPlay("animation.DemonLord.phase_transition_3");
            builder.thenLoop("animation.DemonLord.regenerating");
        } else if (this.getAction() == DemonLord.Action.LEAPING) {
            builder.thenPlay("animation.DemonLord.axe_leap");
            builder.thenLoop("animation.DemonLord.axe_leap_loop");
        } else if (this.getAction() == DemonLord.Action.LEAP_LANDING) {
            builder.thenPlay("animation.DemonLord.axe_leap_land");
        } else if (this.getAction() == DemonLord.Action.SUMMON) {
            builder.thenPlay("animation.DemonLord.summon");
        } else if (this.getAction() == DemonLord.Action.GRASP_OF_FIRE) {
            builder.thenPlay("animation.DemonLord.grasp_of_fire");
        } else if (this.getAction() == DemonLord.Action.ATTACKING) {
            switch(this.getAttackAction()) {
                case CAST_1:
                    builder.thenPlay("animation.DemonLord.cast");
                    break;
                case CAST_2:
                    builder.thenPlay("animation.DemonLord.cast_2");
                    break;
                case DUALWIELD_OVERHEAD:
                    builder.thenPlay("animation.DemonLord.dual_wield_overhead");
                    break;
                case DUALWIELD_SWEEP:
                    builder.thenPlay("animation.DemonLord.dual_weapon_sweep");
                    break;
                case LEFT_OVERHEAD:
                    builder.thenPlay("animation.DemonLord.left_weapon_overhead");
                    break;
                case LEFT_SWEEP:
                    builder.thenPlay("animation.DemonLord.left_weapon_sweep");
                    break;
                case RIGHT_OVERHEAD:
                    builder.thenPlay("animation.DemonLord.right_weapon_overhead");
                    break;
                case RIGHT_SWEEP:
                    builder.thenPlay("animation.DemonLord.right_weapon_sweep");
                    break;
                case STAFF_CAST_1:
                    builder.thenPlay("animation.DemonLord.staff_cast_1");
                    break;
                case STAFF_CAST_2:
                    builder.thenPlay("animation.DemonLord.staff_cast_2");
                    break;
                case STAFF_CAST_3:
                    builder.thenPlay("animation.DemonLord.staff_cast_3");
                    break;
                case TWOHAND_OVERHEAD:
                    builder.thenPlay("animation.DemonLord.two_handed_overhead");
                    break;
                case NONE:
                default:
                    builder.thenLoop("animation.DemonLord.idle");
            }
        } else {
            double movementSpeed = this.m_20184_().add(0.0, -this.m_20184_().y, 0.0).length();
            if (movementSpeed > 0.1) {
                builder.thenLoop("animation.DemonLord.run");
            } else if (movementSpeed > 0.02) {
                builder.thenLoop("animation.DemonLord.walk");
            } else {
                builder.thenLoop("animation.DemonLord.idle");
            }
        }
        return state.setAndContinue(builder);
    }

    @Override
    public void handle(CustomInstructionKeyframeEvent<BossMonster<?>> event) {
        String instruction = event.getKeyframeData().getInstructions();
        if (instruction != "axe_throw" || this.getAction() != DemonLord.Action.PHASE_2_TRANSITION && this.getAction() != DemonLord.Action.PHASE_3_TRANSITION) {
            if (instruction == "sword_draw") {
                this.setWeaponState(DemonLord.WeaponState.SWORDS);
            } else if (instruction == "staff_draw") {
                this.setWeaponState(DemonLord.WeaponState.STAFF);
            }
        } else {
            this.setWeaponState(DemonLord.WeaponState.NOTHING);
        }
    }

    @Override
    public HashMap<String, Integer> getDamageResists() {
        return new HashMap();
    }

    @Override
    public IFaction getFaction() {
        return Factions.DEMONS;
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
        FJ_WAITING_ITEM,
        FJ_IMBUING,
        ATTACKING,
        LEAPING,
        LEAP_LANDING,
        GRASP_OF_FIRE,
        PHASE_2_TRANSITION,
        PHASE_3_TRANSITION,
        SUMMON,
        DEATH
    }

    public static enum AttackAction {

        NONE(0, 0),
        LEFT_OVERHEAD(10, 14),
        RIGHT_OVERHEAD(10, 14),
        LEFT_SWEEP(12, 16),
        RIGHT_SWEEP(12, 16),
        TWOHAND_OVERHEAD(10, 14),
        DUALWIELD_OVERHEAD(10, 14),
        DUALWIELD_SWEEP(10, 14),
        CAST_1(25, 30),
        CAST_2(16, 20),
        STAFF_CAST_1(22, 26),
        STAFF_CAST_2(16, 20),
        STAFF_CAST_3(72, 80);

        public final int animLength;

        public final int resetTime;

        private AttackAction(int animLength, int resetTime) {
            this.animLength = animLength;
            this.resetTime = resetTime;
        }
    }

    public class GraspOfFireGoal extends Goal {

        private static final float pullStrength = 0.2F;

        private Optional<LivingEntity> grabbed = Optional.empty();

        private int tickCount = 0;

        public GraspOfFireGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public boolean canUse() {
            boolean isCorrectPhase = DemonLord.this.f_19804_.get(DemonLord.PHASE) == 1 || DemonLord.this.f_19804_.get(DemonLord.PHASE) == 3;
            boolean isOffCD = !DemonLord.this.isOnCooldown(1027);
            boolean isIdle = DemonLord.this.getAction() == DemonLord.Action.IDLE;
            boolean hasTarget = DemonLord.this.m_5448_() != null;
            boolean onGround = DemonLord.this.m_20096_();
            return isOffCD && isIdle && hasTarget && onGround && isCorrectPhase;
        }

        @Override
        public boolean canContinueToUse() {
            boolean actionIsGraspOfFire = DemonLord.this.getAction() == DemonLord.Action.GRASP_OF_FIRE;
            boolean hasTarget = DemonLord.this.m_5448_() != null;
            return actionIsGraspOfFire && hasTarget;
        }

        @Override
        public boolean isInterruptable() {
            return !DemonLord.this.isPhaseTransitioning();
        }

        @Override
        public void start() {
            LivingEntity target = DemonLord.this.m_5448_();
            BlockPos targetPos = BlockPos.containing(target.m_146892_());
            DemonLord.this.setPullPos(targetPos);
            DemonLord.this.setAction(DemonLord.Action.GRASP_OF_FIRE);
            DemonLord.this.m_21573_().stop();
            this.tickCount = 0;
            this.grabbed = Optional.empty();
        }

        @Override
        public void stop() {
            if (!DemonLord.this.isPhaseTransitioning()) {
                DemonLord.this.setAction(DemonLord.Action.IDLE);
            }
            DemonLord.this.setCooldown(1027, 300);
        }

        @Override
        public void tick() {
            if (DemonLord.this.m_5448_() != null) {
                this.tickCount++;
                DemonLord.this.m_7618_(EntityAnchorArgument.Anchor.FEET, DemonLord.this.m_5448_().m_20182_());
                if (this.tickCount < 46) {
                    this.pullEntities();
                } else if (this.tickCount == 46 && this.grabbed.isEmpty()) {
                    this.grabbed = this.tryGrab();
                    DemonLord.this.setPullPos(null);
                    if (this.grabbed.isEmpty()) {
                        this.stop();
                        return;
                    }
                    LivingEntity living = (LivingEntity) this.grabbed.get();
                    DemonLord.this.lockTargetInPlace(living, true);
                    DemonLord.this.setTimer("burn", 10, () -> DemonLord.this.setIncinerateTarget(living));
                } else if (this.tickCount == 108) {
                    LivingEntity living = (LivingEntity) this.grabbed.get();
                    living.removeEffect(EffectInit.LIFT.get());
                    Vec3 direction = Vec3.directionFromRotation(new Vec2(0.0F, DemonLord.this.m_146908_())).cross(new Vec3(0.0, 1.0, 0.0)).normalize();
                    ComponentFling.flingTarget(living, direction, 3.0F, 3.0F, 0.0F);
                    DemonLord.this.setIncinerateTarget(null);
                } else if (this.tickCount == 118) {
                    this.stop();
                }
            }
        }

        private void pullEntities() {
            Vec3 graspPoint = DemonLord.this.getPullPos();
            if (graspPoint != null) {
                DemonLord.this.m_9236_().getEntities(DemonLord.this, new AABB(BlockPos.containing(graspPoint)).inflate(256.0), e -> e instanceof LivingEntity && e.isAlive() && !(e.distanceToSqr(graspPoint) > 256.0) ? !(e instanceof Player) || !((Player) e).isCreative() && !((Player) e).isSpectator() : false).stream().map(e -> (LivingEntity) e).forEach(e -> {
                    Vec3 delta = graspPoint.subtract(e.m_20182_()).normalize();
                    ComponentFling.flingTarget(e, delta, 0.04F, 0.2F, 0.5F);
                });
            }
        }

        private Optional<LivingEntity> tryGrab() {
            Vec3 graspPoint = DemonLord.this.getPullPos();
            return graspPoint == null ? Optional.empty() : DemonLord.this.m_9236_().getEntities(DemonLord.this, new AABB(BlockPos.containing(graspPoint)).inflate(16.0), e -> {
                if (!(e instanceof LivingEntity) || !e.isAlive() || e.distanceToSqr(graspPoint) > 6.25) {
                    return false;
                } else {
                    return !DemonLord.this.m_21574_().hasLineOfSight(e) ? false : DemonLord.this.m_5448_() == e || DemonLord.this.threat.isOn((LivingEntity) e);
                }
            }).stream().map(e -> (LivingEntity) e).sorted((a, b) -> (int) (a.m_20238_(graspPoint) - b.m_20238_(graspPoint))).findFirst();
        }
    }

    public class HellballGoal extends com.mna.api.entities.ai.CastSpellAtTargetGoal<DemonLord> {

        private boolean casting = false;

        private int warmupCounter = 0;

        private int resetCounter = 0;

        private ItemStack hellball;

        public HellballGoal(ItemStack hellball) {
            super(DemonLord.this, hellball, 0.6, 300, 32.0F, false);
            this.hellball = hellball;
            this.setPrecastCallback(e -> {
                this.casting = true;
                e.setAction(DemonLord.Action.ATTACKING);
                e.setAttackAction(DemonLord.AttackAction.STAFF_CAST_3);
                return false;
            });
        }

        @Override
        public boolean canUse() {
            return this.entity.f_19804_.get(DemonLord.PHASE) == 3 && !this.entity.isOnCooldown(1028) && this.entity.globalSpellCooldown <= 0 ? super.canUse() : false;
        }

        @Override
        public void start() {
            super.start();
            this.target = this.entity.getRandomThreatTableTarget(32.0);
            if (this.target == null) {
                this.target = this.entity.m_5448_();
            }
            this.casting = false;
            this.warmupCounter = 0;
            this.resetCounter = 0;
        }

        private void fireHellball() {
            SpellProjectile projectile = new SpellProjectile(this.entity, DemonLord.this.m_9236_());
            projectile.setSpellRecipe(this.hellball.getTag());
            projectile.setHellball(this.target, 0.1F);
            projectile.shoot(this.entity, this.entity.m_20156_(), 0.1F, 0.0F);
            DemonLord.this.m_9236_().m_7967_(projectile);
        }

        @Override
        public void tick() {
            if (!this.casting) {
                super.tick();
            } else {
                this.warmupCounter++;
                if (this.warmupCounter == 55) {
                    this.fireHellball();
                } else if (this.warmupCounter > 55 && this.resetCounter++ >= 20) {
                    this.entity.setAction(DemonLord.Action.IDLE);
                    this.entity.setAttackAction(DemonLord.AttackAction.NONE);
                    this.entity.setCooldown(1028, 600);
                    this.hasCast = true;
                }
            }
        }
    }

    public class LerpLeap extends Goal {

        private float minDist;

        private float maxDist;

        private int lerpTicks;

        private int initialDelay;

        private int phaseTicks = 0;

        private int phase = 0;

        private Vec3 start;

        private Vec3 end;

        private Vec3 control_1;

        private Vec3 control_2;

        public LerpLeap(float minDist, float maxDist, int initialDelay) {
            this.m_7021_(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.LOOK, Goal.Flag.MOVE));
            this.minDist = minDist;
            this.maxDist = maxDist;
            this.initialDelay = initialDelay;
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
        public boolean canUse() {
            if (DemonLord.this.m_5448_() != null && DemonLord.this.m_6084_() && DemonLord.this.m_5448_().isAlive() && !DemonLord.this.isOnCooldown(1024) && DemonLord.this.f_19804_.get(DemonLord.PHASE) != 3 && DemonLord.this.getAction() == DemonLord.Action.IDLE) {
                double dist = DemonLord.this.m_20280_(DemonLord.this.m_5448_());
                return dist >= (double) (this.minDist * this.minDist) && dist <= (double) (this.maxDist * this.maxDist);
            } else {
                return false;
            }
        }

        @Override
        public void stop() {
            this.setPhase(0);
            DemonLord.this.m_20242_(false);
            DemonLord.this.setCooldown(1024, 300);
            if (!DemonLord.this.isPhaseTransitioning()) {
                DemonLord.this.setAction(DemonLord.Action.IDLE);
            }
        }

        @Override
        public boolean canContinueToUse() {
            return DemonLord.this.m_5448_() != null && DemonLord.this.getAction() != DemonLord.Action.IDLE && this.phase < 4;
        }

        @Override
        public void start() {
            DemonLord.this.m_20242_(true);
            DemonLord.this.m_21573_().stop();
            DemonLord.this.m_20256_(Vec3.ZERO);
            DemonLord.this.setAction(DemonLord.Action.LEAPING);
            this.setPhase(0);
        }

        private void setPhase(int phase) {
            this.phase = phase;
            this.phaseTicks = 0;
        }

        @Override
        public void tick() {
            if (DemonLord.this.m_5448_() != null) {
                this.phaseTicks++;
                switch(this.phase) {
                    case 0:
                        if (this.phaseTicks >= this.initialDelay) {
                            Vec3 direction = DemonLord.this.m_5448_().m_20182_().subtract(DemonLord.this.m_20182_()).normalize();
                            this.start = DemonLord.this.m_20182_();
                            this.end = DemonLord.this.m_5448_().m_20182_().subtract(direction);
                            float speed = 2.0F;
                            double distance = this.start.distanceTo(this.end);
                            this.lerpTicks = (int) (distance / (double) speed);
                            Vec3 difference = this.end.subtract(this.start);
                            this.control_1 = this.start.add(difference.scale(0.3)).add(0.0, 3.0, 0.0);
                            this.control_2 = this.start.add(difference.scale(0.6)).add(0.0, 3.0, 0.0);
                            DemonLord.this.m_5496_(SFX.Entity.DemonLord.LEAP, 1.0F, (float) (0.9 + Math.random() * 0.2));
                            this.setPhase(1);
                        }
                        break;
                    case 1:
                        if (this.phaseTicks <= this.lerpTicks) {
                            if (this.lerpTicks < 5 || this.phaseTicks == this.lerpTicks - 5) {
                                DemonLord.this.setAction(DemonLord.Action.LEAP_LANDING);
                            }
                            float lerpPct = (float) this.phaseTicks / (float) this.lerpTicks;
                            Vec3 position = MathUtils.bezierVector3d(this.start, this.end, this.control_1, this.control_2, lerpPct);
                            DemonLord.this.m_7618_(EntityAnchorArgument.Anchor.FEET, this.end);
                            DemonLord.this.m_6034_(position.x, position.y, position.z);
                        } else {
                            this.setPhase(2);
                        }
                        break;
                    case 2:
                        double dist = DemonLord.this.m_20280_(DemonLord.this.m_5448_());
                        if (dist < 16.0) {
                            DemonLord.this.damageEntity(DemonLord.this.m_5448_());
                        }
                        this.setPhase(3);
                        break;
                    case 3:
                        if (this.phaseTicks >= 10) {
                            this.setPhase(4);
                        }
                }
            }
        }
    }

    private class PhaseTransitionGoal extends Goal {

        private DemonLord entity;

        public PhaseTransitionGoal(DemonLord entity) {
            this.entity = entity;
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }

        @Override
        public boolean canUse() {
            return this.entity.getAction() == DemonLord.Action.PHASE_2_TRANSITION || this.entity.getAction() == DemonLord.Action.PHASE_3_TRANSITION;
        }

        @Override
        public boolean canContinueToUse() {
            return this.entity.m_21223_() < this.entity.m_21233_();
        }
    }

    public static enum WeaponState {

        AXE, NOTHING, SWORDS, STAFF
    }
}