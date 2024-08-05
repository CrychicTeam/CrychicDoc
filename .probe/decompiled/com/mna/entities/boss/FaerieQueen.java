package com.mna.entities.boss;

import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.entities.IFactionEnemy;
import com.mna.api.entities.ai.CastSpellOnSelfGoal;
import com.mna.api.faction.IFaction;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.collections.Components;
import com.mna.api.spells.collections.Shapes;
import com.mna.api.tools.RLoc;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.effects.EffectInit;
import com.mna.entities.EntityInit;
import com.mna.entities.ai.ThreatTable;
import com.mna.entities.boss.attacks.IllusionCreeper;
import com.mna.factions.Factions;
import com.mna.items.ItemInit;
import com.mna.particles.types.movers.ParticleOrbitMover;
import com.mna.spells.crafting.SpellRecipe;
import com.mna.tools.EntityUtil;
import com.mna.tools.ParticleConfigurations;
import com.mna.tools.SummonUtils;
import com.mna.tools.math.MathUtils;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Predicate;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class FaerieQueen extends BossMonster<FaerieQueen> implements IFactionEnemy<FaerieQueen> {

    public final Predicate<Entity> AURA_SELECTOR = e -> e.isAlive() && e instanceof LivingEntity && !(e instanceof FaerieQueen) && (!(e instanceof IFactionEnemy) || ((IFactionEnemy) e).getFaction() != Factions.FEY) && (!SummonUtils.isSummon(e) || !SummonUtils.isTargetFriendly(e, this)) && (!(e instanceof Player) || !((Player) e).isCreative() && !((Player) e).isSpectator());

    private final ServerBossEvent bossEventSummer = (ServerBossEvent) new ServerBossEvent(Component.translatable("entity.mna.faerie_queen.summer"), BossEvent.BossBarColor.PINK, BossEvent.BossBarOverlay.NOTCHED_20).setDarkenScreen(true);

    private final ServerBossEvent bossEventWinter = (ServerBossEvent) new ServerBossEvent(Component.translatable("entity.mna.faerie_queen.winter"), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.NOTCHED_20).setDarkenScreen(true);

    protected static final EntityDataAccessor<Byte> SEASON = SynchedEntityData.defineId(FaerieQueen.class, EntityDataSerializers.BYTE);

    protected static final EntityDataAccessor<String> SUMMONER_UUID = SynchedEntityData.defineId(FaerieQueen.class, EntityDataSerializers.STRING);

    protected static final EntityDataAccessor<Boolean> IS_JOINING = SynchedEntityData.defineId(FaerieQueen.class, EntityDataSerializers.BOOLEAN);

    private Player summonedBy;

    public int faerieDeathTime;

    private int globalSpellCooldown = 0;

    private static final int COOLDOWN_FLING_ID = 1025;

    private static final int COOLDOWN_HEAL_ID = 1026;

    private static final int COOLDOWN_DISJUNCTION_ID = 1027;

    private static final int COOLDOWN_MINDCONTROL_ID = 1028;

    private static final int COOLDOWN_ILLUSIONCREEPER_ID = 1029;

    private static final int COOLDOWN_WINTERICE_ID = 1030;

    private static final int COOLDOWN_CHILL_ID = 1031;

    private static final int COOLDOWN_ICESPIKE_ID = 1032;

    private static final int COOLDOWN_FRAILTY_ID = 1033;

    private static final int COOLDOWN_SUMMERFIRE_ID = 1034;

    private static final int COOLDOWN_HEATWAVE_ID = 1035;

    private static final int COOLDOWN_SUNDER_ID = 1036;

    private static final float LIFT_SPEED = 0.055F;

    public FaerieQueen(EntityType<? extends Monster> type, Level world) {
        super(type, world);
        this.setSeason(Math.random() < 0.5);
        this.m_20242_(true);
        this.f_21342_ = new FaerieQueen.MoveHelperController(this);
        this.setCooldown(1029, (int) (100.0 + Math.random() * 300.0));
        SummonUtils.setBonusSummons(this, 99);
        this.threat = new ThreatTable(e -> {
            if (e == this) {
                return false;
            } else {
                return !(e instanceof Player) || !((Player) e).isCreative() && !((Player) e).isSpectator() ? !(e instanceof FaerieQueen) : false;
            }
        });
    }

    public FaerieQueen(Level world, Vec3 pos, boolean winter) {
        this(EntityInit.FAERIE_QUEEN.get(), world);
        this.setSeason(winter);
        this.m_146884_(pos);
    }

    @Override
    public void tick() {
        if (this.globalSpellCooldown > 0) {
            this.globalSpellCooldown--;
        }
        if (this.m_5448_() instanceof FaerieQueen) {
            this.threat.remove(this.m_5448_());
            this.m_6710_(null);
        }
        if (this.m_21023_(EffectInit.GRAVITY_WELL.get())) {
            this.m_20256_(this.m_20184_().add(0.0, -0.2, 0.0));
        }
        this.m_20242_(true);
        super.tick();
        if (this.m_5448_() instanceof FaerieQueen) {
            this.m_6710_(null);
        }
        if (!this.isNaturalSpawn()) {
            if (this.getSummonedBy() == null) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
                return;
            }
            boolean remove = false;
            this.m_7618_(EntityAnchorArgument.Anchor.FEET, this.getSummonedBy().m_146892_());
            if (!this.getSummonedBy().m_21224_() && !(this.getSummonedBy().m_20280_(this) > 256.0)) {
                remove = this.f_19797_ > 700;
            } else {
                remove = true;
            }
            if (remove && !this.m_9236_().isClientSide()) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        } else {
            this.tickAuras();
        }
    }

    private void tickAuras() {
        if (!this.m_9236_().isClientSide() && this.m_9236_().getGameTime() % 60L == 0L) {
            this.m_9236_().getEntities(this, this.m_20191_().inflate(16.0), this.AURA_SELECTOR).stream().map(e -> (LivingEntity) e).forEach(e -> {
                if (this.isWinter()) {
                    if (!e.hasEffect(EffectInit.SOAKED.get())) {
                        e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
                    } else {
                        e.m_146917_(e.m_146888_() + 20);
                    }
                    e.addEffect(new MobEffectInstance(EffectInit.SNOWBLIND.get(), 100, 2));
                } else if (!e.m_20070_() && !e.hasEffect(EffectInit.SOAKED.get())) {
                    e.m_20254_(5);
                }
            });
        }
    }

    private void lockTargetInPlace(LivingEntity target, boolean raise) {
        int duration = 200;
        target.removeEffect(EffectInit.LIFT.get());
        target.addEffect(new MobEffectInstance(EffectInit.LIFT.get(), duration, raise ? 1 : 0));
        if (raise && target instanceof Player) {
            EntityUtil.SetLiftSpeed((Player) target, 0.055F);
        }
    }

    @Override
    protected void registerGoals() {
        double moveSpeed = this.m_21133_(Attributes.MOVEMENT_SPEED);
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(0, new FaerieQueen.FaerieQueenFly());
        ItemStack flingSpell = new SpellRecipe(Shapes.PROJECTILE, Components.FLING).setShapeModifier(Attribute.SPEED, 3.0F).setComponentModifier(0, Attribute.SPEED, 3.0F).createAsSpell();
        ItemStack healSpell = new SpellRecipe(Shapes.SELF, Components.HEAL).setComponentModifier(0, Attribute.MAGNITUDE, 20.0F).createAsSpell();
        ItemStack disjunctionSpell = new SpellRecipe(Shapes.SELF, Components.DISJUNCTION).setComponentModifier(0, Attribute.MAGNITUDE, 20.0F).createAsSpell();
        ItemStack mindControlSpell = new SpellRecipe(Shapes.BOLT, Components.MIND_CONTROL).setShapeModifier(Attribute.RANGE, 32.0F).setComponentModifier(0, Attribute.DURATION, 15.0F).createAsSpell();
        ItemStack winterIceSpell = new SpellRecipe(Shapes.PROJECTILE, Components.FROST_DAMAGE).setShapeModifier(Attribute.RADIUS, 2.0F).setShapeModifier(Attribute.SPEED, 2.0F).setComponentModifier(0, Attribute.DAMAGE, 10.0F).createAsSpell();
        ItemStack chillSpell = new SpellRecipe(Shapes.BOLT, Components.CHILL).setShapeModifier(Attribute.RANGE, 32.0F).setComponentModifier(0, Attribute.MAGNITUDE, 2.0F).setComponentModifier(0, Attribute.DURATION, 20.0F).setComponentModifier(0, Attribute.DAMAGE, 20.0F).setComponentModifier(0, Attribute.RADIUS, 10.0F).createAsSpell();
        ItemStack iceSpikeSpell = new SpellRecipe(Shapes.PROJECTILE, Components.ICE_SPIKE).setShapeModifier(Attribute.RADIUS, 3.0F).setShapeModifier(Attribute.SPEED, 2.0F).setComponentModifier(0, Attribute.DAMAGE, 20.0F).setComponentModifier(0, Attribute.DURATION, 20.0F).createAsSpell();
        ItemStack frailtySpell = new SpellRecipe(Shapes.CHAIN, Components.FRAILTY).setShapeModifier(Attribute.RANGE, 32.0F).setShapeModifier(Attribute.MAGNITUDE, 100.0F).setShapeModifier(Attribute.RADIUS, 5.0F).setComponentModifier(0, Attribute.DURATION, 10.0F).setComponentModifier(0, Attribute.MAGNITUDE, 3.0F).createAsSpell();
        ItemStack summerFireSpell = new SpellRecipe(Shapes.PROJECTILE, Components.FIRE_DAMAGE).setShapeModifier(Attribute.RADIUS, 2.0F).setShapeModifier(Attribute.SPEED, 2.0F).setComponentModifier(0, Attribute.DAMAGE, 10.0F).setComponentModifier(0, Attribute.PRECISION, 1.0F).createAsSpell();
        ItemStack heatwaveSpell = new SpellRecipe(Shapes.BOLT, Components.HEATWAVE).setShapeModifier(Attribute.RANGE, 32.0F).setComponentModifier(0, Attribute.MAGNITUDE, 2.0F).setComponentModifier(0, Attribute.DURATION, 20.0F).setComponentModifier(0, Attribute.DAMAGE, 20.0F).setComponentModifier(0, Attribute.RADIUS, 10.0F).createAsSpell();
        ItemStack sunderSpell = new SpellRecipe(Shapes.CHAIN, Components.SUNDER).setShapeModifier(Attribute.RANGE, 32.0F).setShapeModifier(Attribute.MAGNITUDE, 100.0F).setShapeModifier(Attribute.RADIUS, 5.0F).setComponentModifier(0, Attribute.DURATION, 10.0F).setComponentModifier(0, Attribute.MAGNITUDE, 3.0F).createAsSpell();
        this.f_21345_.addGoal(1, new CastSpellOnSelfGoal<>(this, healSpell, this::shouldHealSelf, this::snapSpellCastPrecast, me -> {
            this.spellCastReset();
            this.setCooldown(1026, 100);
        }, 21));
        this.f_21345_.addGoal(1, new FaerieQueen.FaerieQueenSummonIllusions());
        this.f_21345_.addGoal(1, new BossMonster.CastSpellAtTargetGoal(flingSpell, moveSpeed, 100, 10.0F, 0.0F, true, 1025, 8, 12, null, this::spellCastReset, this::simpleSpellCastPrecast).setUsePredicate(this::shouldFlingTarget));
        this.f_21345_.addGoal(2, new BossMonster.CastSpellAtTargetGoal(disjunctionSpell, moveSpeed, 400, 28.0F, 0.0F, true, 1027, 20, 28, null, this::spellCastReset, this::snapSpellCastPrecast).setUsePredicate(this::spellCastPredicate));
        this.f_21345_.addGoal(2, new BossMonster.CastSpellAtTargetGoal(mindControlSpell, moveSpeed, 500, 28.0F, 0.0F, true, 1028, 20, 28, null, this::spellCastReset, this::snapSpellCastPrecast).setUsePredicate(this::shouldMindControlTarget));
        if (this.isWinter()) {
            this.f_21345_.addGoal(1, new BossMonster.CastSpellAtTargetGoal(chillSpell, moveSpeed, 300, 28.0F, 0.0F, true, 1031, 5, 13, null, this::spellCastReset, this::spinSpellCastPrecast).setUsePredicate(this::shouldHighPriorityChillTarget));
            this.f_21345_.addGoal(1, new BossMonster.CastSpellAtTargetGoal(frailtySpell, moveSpeed, 500, 28.0F, 0.0F, true, 1033, 20, 28, null, this::spellCastReset, this::snapSpellCastPrecast).setUsePredicate(this::shouldHighPriorityFrailtyTarget));
            this.f_21345_.addGoal(5, new BossMonster.CastSpellAtTargetGoal(chillSpell, moveSpeed, 300, 28.0F, 0.0F, true, 1031, 5, 13, null, this::spellCastReset, this::spinSpellCastPrecast).setUsePredicate(this::spellCastPredicate));
            this.f_21345_.addGoal(5, new BossMonster.CastSpellAtTargetGoal(frailtySpell, moveSpeed, 500, 28.0F, 0.0F, true, 1033, 20, 28, null, this::spellCastReset, this::snapSpellCastPrecast).setUsePredicate(this::spellCastPredicate));
            this.f_21345_.addGoal(5, new BossMonster.CastSpellAtTargetGoal(iceSpikeSpell, moveSpeed, 300, 28.0F, 0.0F, true, 1032, 5, 13, null, this::spellCastReset, this::simpleSpellCastPrecast).setUsePredicate(this::spellCastPredicate));
            this.f_21345_.addGoal(6, new BossMonster.CastSpellAtTargetGoal(winterIceSpell, moveSpeed, 0, 28.0F, 0.0F, true, 1030, 5, 13, null, this::spellCastReset, this::simpleSpellCastPrecast).setUsePredicate(this::spellCastPredicate));
        } else {
            this.f_21345_.addGoal(1, new BossMonster.CastSpellAtTargetGoal(heatwaveSpell, moveSpeed, 300, 28.0F, 0.0F, true, 1035, 5, 13, null, this::spellCastReset, this::spinSpellCastPrecast).setUsePredicate(this::shouldHighPriorityHeatwaveTarget));
            this.f_21345_.addGoal(5, new BossMonster.CastSpellAtTargetGoal(heatwaveSpell, moveSpeed, 300, 28.0F, 0.0F, true, 1035, 5, 13, null, this::spellCastReset, this::spinSpellCastPrecast).setUsePredicate(this::spellCastPredicate));
            this.f_21345_.addGoal(5, new BossMonster.CastSpellAtTargetGoal(sunderSpell, moveSpeed, 500, 28.0F, 0.0F, true, 1036, 20, 28, null, this::spellCastReset, this::snapSpellCastPrecast).setUsePredicate(this::spellCastPredicate));
            this.f_21345_.addGoal(6, new BossMonster.CastSpellAtTargetGoal(summerFireSpell, moveSpeed, 0, 28.0F, 0.0F, true, 1034, 5, 13, null, this::spellCastReset, this::simpleSpellCastPrecast).setUsePredicate(this::spellCastPredicate));
        }
        if (this.f_21346_.availableGoals.size() == 0) {
            this.f_21346_.addGoal(1, new BossMonster.ThreatTableHurtByTargetGoal(this, FaerieQueen.class));
            this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, le -> true));
            this.f_21346_.addGoal(4, new NearestAttackableTargetGoal(this, Mob.class, 10, true, false, e -> e instanceof IFactionEnemy && ((IFactionEnemy) e).getFaction() != this.getFaction()));
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(SEASON, (byte) 0);
        this.f_19804_.define(SUMMONER_UUID, "");
        this.f_19804_.define(IS_JOINING, false);
    }

    @Override
    public void die(DamageSource pCause) {
        if (pCause.is(DamageTypes.FELL_OUT_OF_WORLD)) {
            super.die(pCause);
        } else {
            this.setAction(FaerieQueen.Action.DEATH);
            this.m_21219_();
            super.die(pCause);
        }
    }

    @Override
    protected void tickDeath() {
        this.faerieDeathTime++;
        if (!this.m_9236_().isClientSide()) {
            if (this.faerieDeathTime == 30) {
                this.m_5496_(SFX.Event.Ritual.FAERIE_BLOW_KISS, 1.0F, 1.0F);
            } else if (this.faerieDeathTime == 61) {
                this.m_142687_(Entity.RemovalReason.KILLED);
            }
        }
        if (this.faerieDeathTime >= 60 && this.m_9236_().isClientSide()) {
            ParticleConfigurations.ArcaneParticleBurst(this.m_9236_(), this.m_146892_());
        }
    }

    @Override
    protected boolean shouldDropLoot() {
        return this.m_9236_().m_45976_(FaerieQueen.class, this.m_20191_().inflate(32.0)).size() <= 1;
    }

    public static AttributeSupplier.Builder getGlobalAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 675.0).add(Attributes.MOVEMENT_SPEED, 0.7F).add(Attributes.FOLLOW_RANGE, 40.0).add(Attributes.ARMOR, 14.0).add(Attributes.ATTACK_DAMAGE, 5.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.5);
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {
        if (this.isNaturalSpawn()) {
            return InteractionResult.FAIL;
        } else if (player != this.getSummonedBy()) {
            return InteractionResult.FAIL;
        } else if (this.getAction() == FaerieQueen.Action.FJ_WAITING_ITEM && player.m_21120_(hand).getItem() == ItemInit.MOTE_AIR.get()) {
            EntityUtil.SetLiftSpeed(player, 0.055F);
            if (!this.m_9236_().isClientSide()) {
                player.m_21211_().shrink(1);
                this.setAction(FaerieQueen.Action.FJ_IMBUING);
                this.m_9236_().playSound(null, this.m_20183_(), SFX.Event.Ritual.FAERIE_IMBUE, SoundSource.PLAYERS, 1.0F, 1.0F);
                this.lockTargetInPlace(player, false);
                this.setTimer("raise", 10, () -> this.lockTargetInPlace(player, true));
                this.setTimer("kiss", 52, () -> {
                    this.m_9236_().playSound(null, this.m_20183_(), SFX.Event.Ritual.FAERIE_BLOW_KISS, SoundSource.PLAYERS, 1.0F, 1.0F);
                    this.lockTargetInPlace(player, false);
                });
                this.setTimer("leave_sound", 100, () -> this.m_9236_().playSound(null, this.m_20183_(), SFX.Event.Ritual.FAERIE_LEAVE, SoundSource.PLAYERS, 1.0F, 1.0F));
                this.setTimer("advance", 120, () -> {
                    IPlayerProgression progression = (IPlayerProgression) player.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
                    if (progression != null && progression.getTier() < 5) {
                        if (progression.getAlliedFaction() == null) {
                            progression.setAlliedFaction(Factions.FEY, player);
                            player.m_213846_(Component.translatable("event.mna.faction_ally_fey"));
                        }
                        if (progression.getAlliedFaction() == Factions.FEY) {
                            progression.setTier(progression.getTier() + 1, player);
                            player.m_213846_(Component.translatable("mna:progresscondition.advanced", progression.getTier()));
                        }
                    }
                    player.m_21195_(EffectInit.LIFT.get());
                });
                this.setTimer("despawn", 165, () -> this.m_146870_());
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.FAIL;
        }
    }

    private UUID getSummonerGuid() {
        try {
            return UUID.fromString(this.f_19804_.get(SUMMONER_UUID));
        } catch (Throwable var2) {
            return null;
        }
    }

    private FaerieQueen.Action getAction() {
        int ordinal = (this.getStateFlag() & 2040) >> 3 & 0xFF;
        return ordinal >= FaerieQueen.Action.values().length ? FaerieQueen.Action.IDLE : FaerieQueen.Action.values()[ordinal];
    }

    private FaerieQueen.AttackAction getAttackAction() {
        int ordinal = (this.getStateFlag() & 65535) >> 12 & 15;
        return ordinal >= FaerieQueen.AttackAction.values().length ? FaerieQueen.AttackAction.NONE : FaerieQueen.AttackAction.values()[ordinal];
    }

    private Player getSummonedBy() {
        if (this.summonedBy == null && this.getSummonerGuid() != null) {
            this.summonedBy = this.m_9236_().m_46003_(this.getSummonerGuid());
        }
        return this.summonedBy;
    }

    public boolean isExiting() {
        return this.isNaturalSpawn() && this.getAction() == FaerieQueen.Action.DEATH;
    }

    public boolean isWinter() {
        return this.f_19804_.get(SEASON) == 0;
    }

    public boolean isNaturalSpawn() {
        return !this.f_19804_.get(IS_JOINING);
    }

    public FaerieQueen setSeason(boolean winter) {
        if (this.isWinter() == winter) {
            return this;
        } else {
            this.f_19804_.set(SEASON, Byte.valueOf((byte) (winter ? 0 : 1)));
            this.f_21345_.removeAllGoals(g -> true);
            this.registerGoals();
            return this;
        }
    }

    public void setSummoner(Player player) {
        if (player != null && player.getGameProfile() != null) {
            this.f_19804_.set(SUMMONER_UUID, player.getGameProfile().getId().toString());
            this.f_19804_.set(IS_JOINING, true);
            this.hideBossBar();
            this.summonedBy = player;
            this.m_20331_(true);
            this.f_21345_.availableGoals.clear();
            this.f_21346_.availableGoals.clear();
            this.setAction(FaerieQueen.Action.FJ_WAITING_ITEM);
        }
    }

    private void setAction(FaerieQueen.Action action) {
        byte ordinal = (byte) ((action.ordinal() & 0xFF) << 3);
        int flag = this.getStateFlag();
        flag &= -2041;
        flag |= ordinal;
        this.setState(new int[] { flag });
    }

    private void setAttackAction(FaerieQueen.AttackAction action) {
        int ordinal = (action.ordinal() & 15) << 12;
        int flag = this.getStateFlag();
        flag &= -61441;
        flag |= ordinal;
        this.setState(new int[] { flag });
    }

    @Override
    protected ServerBossEvent getBossEvent() {
        return this.isWinter() ? this.bossEventWinter : this.bossEventSummer;
    }

    @Override
    public void setupSpawn() {
    }

    @Override
    protected void spawnParticles() {
        if (this.getAction() != FaerieQueen.Action.DEATH) {
            int numAuraParticles = 3;
            int numWingParticles = 2;
            Vec3 forward = this.m_20156_().normalize();
            Vec3 side = forward.cross(new Vec3(0.0, 1.0, 0.0)).normalize();
            forward = forward.scale(-0.5);
            if (this.isWinter()) {
                for (int i = 0; i < numAuraParticles; i++) {
                    Vec3 pos = new Vec3(this.m_20185_() - 0.2 + Math.random() * 0.4, this.m_20186_() + Math.random() * 0.5, this.m_20189_() - 0.2 + Math.random() * 0.4);
                    this.m_9236_().addParticle(new MAParticleType(ParticleInit.FROST.get()).setMover(new ParticleOrbitMover(pos.x, pos.y, pos.z, 0.01F + Math.random() * 0.01F, 0.01F + Math.random() * 0.01F, 1.0 + Math.random() * 3.0)), pos.x, pos.y, pos.z, 0.0, 0.0, 0.0);
                }
                for (int i = 0; i < numWingParticles; i++) {
                    this.m_9236_().addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_GRAVITY.get()).setColor(148, 242, 244), this.m_20185_() - side.x + forward.x + forward.x * Math.random() + Math.random() * side.x * 2.0, this.m_20186_() + Math.random() * (double) this.m_20192_(), this.m_20189_() - side.z + forward.z + forward.z * Math.random() + Math.random() * side.z * 2.0, 0.0, 0.0, 0.0);
                }
            } else {
                for (int i = 0; i < numAuraParticles; i++) {
                    Vec3 pos = new Vec3(this.m_20185_() - 0.2 + Math.random() * 0.4, this.m_20186_() + Math.random() * 0.5, this.m_20189_() - 0.2 + Math.random() * 0.4);
                    this.m_9236_().addParticle(new MAParticleType(ParticleInit.FLAME.get()).setMover(new ParticleOrbitMover(pos.x, pos.y, pos.z, 0.01F + Math.random() * 0.01F, 0.01F + Math.random() * 0.01F, 1.0 + Math.random() * 3.0)), pos.x, pos.y, pos.z, 0.0, 0.0, 0.0);
                }
                for (int i = 0; i < numWingParticles; i++) {
                    this.m_9236_().addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_GRAVITY.get()).setColor(229, 220, 84), this.m_20185_() - side.x + forward.x + forward.x * Math.random() + Math.random() * side.x * 2.0, this.m_20186_() + Math.random() * (double) this.m_20192_(), this.m_20189_() - side.z + forward.z + forward.z * Math.random() + Math.random() * side.z * 2.0, 0.0, 0.0, 0.0);
                }
            }
        }
    }

    @Override
    public ResourceLocation getArenaStructureID() {
        return RLoc.create("faction/boss/fey_tree");
    }

    @Override
    public int getArenaStructureSegment() {
        return 2;
    }

    @Override
    public HashMap<String, Integer> getDamageResists() {
        return new HashMap();
    }

    @Override
    public IFaction getFaction() {
        return Factions.FEY;
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

    @Override
    protected PlayState handleAnimState(AnimationState<? extends BossMonster<?>> state) {
        RawAnimation builder = RawAnimation.begin();
        switch(this.getAction()) {
            case DEATH:
                builder.thenPlay("animation.fey_queen.blow_kiss");
                break;
            case FJ_WAITING_ITEM:
                builder.thenLoop("animation.fey_queen.wait_gift");
                break;
            case FJ_IMBUING:
                builder.thenPlay("animation.fey_queen.infuse");
                break;
            case ATTACKING:
                switch(this.getAttackAction()) {
                    case CAST_2H_UP:
                        builder.thenPlay("animation.fey_queen.cast_5");
                        return state.setAndContinue(builder);
                    case CAST_SIMPLE_LEFT:
                        builder.thenPlay("animation.fey_queen.cast_2");
                        return state.setAndContinue(builder);
                    case CAST_SIMPLE_RIGHT:
                        builder.thenPlay("animation.fey_queen.cast_1");
                        return state.setAndContinue(builder);
                    case CAST_SNAP_LEFT:
                        builder.thenPlay("animation.fey_queen.cast_4");
                        return state.setAndContinue(builder);
                    case CAST_SNAP_RIGHT:
                        builder.thenPlay("animation.fey_queen.cast_3");
                        return state.setAndContinue(builder);
                    case CAST_SPIN_LEFT:
                        builder.thenPlay("animation.fey_queen.cast_7");
                        return state.setAndContinue(builder);
                    case CAST_SPIN_RIGHT:
                        builder.thenPlay("animation.fey_queen.cast_6");
                        return state.setAndContinue(builder);
                    default:
                        return state.setAndContinue(builder);
                }
            default:
                double movementSpeed = this.m_20184_().add(0.0, -this.m_20184_().y, 0.0).length();
                if (movementSpeed > 0.1) {
                    builder.thenLoop("animation.fey_queen.moving");
                } else {
                    builder.thenLoop("animation.fey_queen.idle");
                }
        }
        return state.setAndContinue(builder);
    }

    private void spellCastReset() {
        this.setAttackAction(FaerieQueen.AttackAction.NONE);
        this.setAction(FaerieQueen.Action.IDLE);
        this.globalSpellCooldown = 10 + (int) (Math.random() * 20.0);
    }

    private boolean simpleSpellCastPrecast(BossMonster<?> me) {
        this.setAction(FaerieQueen.Action.ATTACKING);
        this.setAttackAction(Math.random() < 0.5 ? FaerieQueen.AttackAction.CAST_SIMPLE_LEFT : FaerieQueen.AttackAction.CAST_SIMPLE_RIGHT);
        return true;
    }

    private boolean spinSpellCastPrecast(BossMonster<?> me) {
        this.setAction(FaerieQueen.Action.ATTACKING);
        this.setAttackAction(Math.random() < 0.5 ? FaerieQueen.AttackAction.CAST_SPIN_LEFT : FaerieQueen.AttackAction.CAST_SPIN_RIGHT);
        return true;
    }

    private boolean snapSpellCastPrecast(BossMonster<?> me) {
        this.setAction(FaerieQueen.Action.ATTACKING);
        this.setAttackAction(Math.random() < 0.5 ? FaerieQueen.AttackAction.CAST_SNAP_LEFT : FaerieQueen.AttackAction.CAST_SNAP_RIGHT);
        return true;
    }

    private boolean shouldFlingTarget(Entity target) {
        return this.spellCastPredicate(target) && target.distanceTo(this) < 4.0F;
    }

    private boolean shouldHighPriorityFrailtyTarget(Entity target) {
        return this.spellCastPredicate(target) && target instanceof LivingEntity && ((LivingEntity) target).hasEffect(MobEffects.DAMAGE_RESISTANCE);
    }

    private boolean shouldHighPriorityHeatwaveTarget(Entity target) {
        return this.spellCastPredicate(target) && target instanceof LivingEntity && ((LivingEntity) target).hasEffect(EffectInit.CHILL.get());
    }

    private boolean shouldHighPriorityChillTarget(Entity target) {
        return this.spellCastPredicate(target) && target instanceof LivingEntity && ((LivingEntity) target).hasEffect(EffectInit.HEATWAVE.get());
    }

    private boolean shouldMindControlTarget(Entity target) {
        return this.spellCastPredicate(target) && SummonUtils.isSummon(target) && !SummonUtils.isTargetFriendly(target, this);
    }

    private boolean spellCastPredicate(Entity target) {
        return this.getAction() == FaerieQueen.Action.IDLE && this.globalSpellCooldown == 0 && this.m_5448_() != null && this.m_5448_().isAlive();
    }

    private boolean shouldHealSelf(FaerieQueen me) {
        return this.getAction() == FaerieQueen.Action.IDLE && this.globalSpellCooldown == 0 && this.m_21223_() <= this.m_21233_() / 2.0F && !this.isOnCooldown(1026);
    }

    public static enum Action {

        IDLE,
        FJ_WAITING_ITEM,
        FJ_IMBUING,
        ATTACKING,
        ENRAGING,
        DEATH
    }

    public static enum AttackAction {

        NONE(0, 0),
        CAST_SIMPLE_RIGHT(13, 17),
        CAST_SIMPLE_LEFT(13, 17),
        CAST_SNAP_RIGHT(28, 32),
        CAST_SNAP_LEFT(28, 32),
        CAST_2H_UP(28, 32),
        CAST_SPIN_RIGHT(13, 17),
        CAST_SPIN_LEFT(13, 17);

        public final int animLength;

        public final int resetTime;

        private AttackAction(int animLength, int resetTime) {
            this.animLength = animLength;
            this.resetTime = resetTime;
        }
    }

    class FaerieQueenFly extends Goal {

        public FaerieQueenFly() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            MoveControl movementcontroller = FaerieQueen.this.m_21566_();
            if (!movementcontroller.hasWanted()) {
                return true;
            } else if (FaerieQueen.this.m_5448_() == null) {
                double d0 = movementcontroller.getWantedX() - FaerieQueen.this.m_20185_();
                double d1 = movementcontroller.getWantedY() - FaerieQueen.this.m_20186_();
                double d2 = movementcontroller.getWantedZ() - FaerieQueen.this.m_20189_();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                return d3 < 4.0 || d3 > 3600.0;
            } else {
                double dist = FaerieQueen.this.m_5448_().m_20280_(FaerieQueen.this);
                return !FaerieQueen.this.m_21574_().hasLineOfSight(FaerieQueen.this.m_5448_()) || dist > 400.0 || FaerieQueen.this.m_9236_().getGameTime() % 100L == 0L;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            if (FaerieQueen.this.m_5448_() != null) {
                Vec3 direction = FaerieQueen.this.m_20182_().subtract(FaerieQueen.this.m_5448_().m_20182_()).normalize();
                direction = direction.add(0.0, -direction.y - 1.0 + Math.random() * 2.0, 0.0);
                Vec3 offset = direction.scale(8.0 + Math.random() * 4.0);
                offset = offset.add(-2.0 + Math.random() * 4.0, 0.0, -2.0 + Math.random() * 4.0);
                Vec3 target = FaerieQueen.this.m_5448_().m_20182_().add(offset);
                if (FaerieQueen.this.m_5448_().m_20096_()) {
                    target = target.add(0.0, 1.5, 0.0);
                }
                BlockPos pos = BlockPos.containing(target.x, target.y, target.z);
                int count;
                for (count = 0; FaerieQueen.this.m_9236_().m_46859_(pos) && count < 16; pos = pos.below()) {
                    count++;
                }
                if (count >= 16) {
                    target = target.add(0.0, -10.0, 0.0);
                }
                FaerieQueen.this.m_21566_().setWantedPosition(target.x, target.y, target.z, 1.0);
            }
        }
    }

    class FaerieQueenSummonIllusions extends Goal {

        private static final int DURATION = 42;

        private static final int SUMMON_AT = 30;

        private int tickCount = 0;

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public boolean canUse() {
            boolean hasTarget = FaerieQueen.this.m_5448_() != null;
            boolean noSummons = FaerieQueen.this.m_9236_().m_45976_(IllusionCreeper.class, FaerieQueen.this.m_20191_().inflate(32.0)).size() == 0;
            boolean offCooldown = !FaerieQueen.this.isOnCooldown(1029);
            boolean isIdle = FaerieQueen.this.getAction() == FaerieQueen.Action.IDLE || FaerieQueen.this.getAction() == FaerieQueen.Action.ATTACKING && FaerieQueen.this.getAttackAction() == FaerieQueen.AttackAction.CAST_2H_UP;
            return hasTarget && noSummons && offCooldown && isIdle;
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return FaerieQueen.this.getAction() == FaerieQueen.Action.ATTACKING && FaerieQueen.this.getAttackAction() == FaerieQueen.AttackAction.CAST_2H_UP && this.tickCount < 42;
        }

        @Override
        public void start() {
            FaerieQueen.this.setAction(FaerieQueen.Action.ATTACKING);
            FaerieQueen.this.setAttackAction(FaerieQueen.AttackAction.CAST_2H_UP);
            FaerieQueen.this.m_5496_(SFX.Entity.WitherLich.SUMMON, 1.0F, 1.0F);
        }

        @Override
        public void stop() {
            FaerieQueen.this.setCooldown(1029, 600);
            FaerieQueen.this.setAttackAction(FaerieQueen.AttackAction.NONE);
            FaerieQueen.this.setAction(FaerieQueen.Action.IDLE);
            this.tickCount = 0;
        }

        @Override
        public void tick() {
            this.tickCount++;
            if (this.tickCount == 30) {
                for (int i = 0; i < 10; i++) {
                    this.summonCreeper(true);
                }
                this.summonCreeper(false);
            }
            if (this.tickCount >= 42) {
                this.stop();
            }
        }

        private void summonCreeper(boolean illusion) {
            Vec3 pos = FaerieQueen.this.m_20182_();
            pos = pos.add(-5.0 + Math.random() * 10.0, 0.0, -5.0 + Math.random() * 10.0);
            Creeper c = (Creeper) (illusion ? new IllusionCreeper(FaerieQueen.this.m_9236_()) : new Creeper(EntityType.CREEPER, FaerieQueen.this.m_9236_()));
            c.m_146884_(pos);
            c.m_7292_(new MobEffectInstance(MobEffects.SLOW_FALLING, 60));
            SummonUtils.setSummon(c, FaerieQueen.this, 400);
            FaerieQueen.this.m_9236_().m_7967_(c);
        }
    }

    static class MoveHelperController extends MoveControl {

        private final FaerieQueen parentEntity;

        private int courseChangeCooldown;

        public MoveHelperController(FaerieQueen queen) {
            super(queen);
            this.parentEntity = queen;
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
}