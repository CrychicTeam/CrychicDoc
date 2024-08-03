package com.mna.entities.faction;

import com.mna.api.faction.IFaction;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.api.spells.targeting.SpellTargetHelper;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.effects.EffectInit;
import com.mna.entities.EntityInit;
import com.mna.entities.faction.base.BaseFlyingFactionMob;
import com.mna.factions.Factions;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.keyframe.event.SoundKeyframeEvent;
import software.bernie.geckolib.core.object.PlayState;

public class Chatterer extends BaseFlyingFactionMob<Chatterer> implements AnimationController.SoundKeyframeHandler<Chatterer> {

    private static final EntityDataAccessor<Integer> TARGET_ID = SynchedEntityData.defineId(Chatterer.class, EntityDataSerializers.INT);

    private static final int CHATTER_ID = 1000;

    private static final int PARALYZE_DIST = 10;

    public Chatterer(EntityType<Chatterer> type, Level world) {
        super(type, world);
        this.randomFlyCooldown = 220;
    }

    public Chatterer(Level world) {
        this(EntityInit.CHATTERER.get(), world);
    }

    private void checkForParalysis() {
        if (!this.m_9236_().isClientSide()) {
            ArrayList<Player> players = new ArrayList();
            this.m_9236_().m_6907_().forEach(px -> {
                double dist = px.m_20280_(this);
                boolean isBlind = px.m_21023_(MobEffects.BLINDNESS);
                if (!isBlind) {
                    if (dist < 100.0 && px.m_6084_()) {
                        if (this.getRaidTarget() == px) {
                            players.add(px);
                        } else if (!px.isCreative() && !px.isSpectator()) {
                            px.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(pr -> {
                                if (pr.getAlliedFaction() != Factions.UNDEAD) {
                                    players.add(px);
                                }
                            });
                        }
                    }
                }
            });
            for (Player p : players) {
                EntityHitResult hitResult = SpellTargetHelper.rayTraceEntities(this.m_9236_(), p, p.m_146892_(), p.m_146892_().add(p.m_20156_().scale(10.0)), p.m_20191_().inflate(10.0), e -> e == this);
                if (p.m_21023_(MobEffects.GLOWING) || hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {
                    this.setAction(Chatterer.Action.PARALYZED);
                    this.m_7292_(new MobEffectInstance(EffectInit.GRAVITY_WELL.get(), 60));
                    this.setTimer("paralyze", 60, () -> {
                        this.setAction(Chatterer.Action.IDLE);
                        this.checkForParalysis();
                    }, false);
                    return;
                }
            }
            if (this.getAction() == Chatterer.Action.PARALYZED) {
                this.setAction(Chatterer.Action.IDLE);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.m_9236_().getGameTime() % 10L == 0L) {
            this.checkForParalysis();
        }
        if (this.m_9236_().isClientSide() && this.getAction() != Chatterer.Action.PARALYZED) {
            RandomSource random = this.m_9236_().getRandom();
            Vec3 pos = this.m_20182_().add(this.m_20156_().scale(-0.25));
            for (int i = 0; i < 30; i++) {
                int color = random.nextInt(50);
                this.m_9236_().addParticle(new MAParticleType(ParticleInit.ENDER_VELOCITY.get()).setColor(color, color, color).setMaxAge(10 + random.nextInt(10)).setScale(0.05F + (float) (random.nextGaussian() * 0.05)), pos.x - 0.1 + random.nextGaussian() * 0.2 + this.m_20184_().x, pos.y - 0.35 + random.nextGaussian() * 0.2 + this.m_20184_().y, pos.z - 0.1 + random.nextGaussian() * 0.2 + this.m_20184_().z, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.f_21345_.addGoal(2, new Chatterer.SummonReinforcements());
        this.f_21345_.addGoal(3, new Chatterer.Chatter());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(TARGET_ID, -1);
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    public static AttributeSupplier.Builder getGlobalAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 12.0).add(Attributes.MOVEMENT_SPEED, 0.55F).add(Attributes.ATTACK_DAMAGE, 1.0).add(Attributes.ATTACK_SPEED, 40.0).add(Attributes.ATTACK_KNOCKBACK, 0.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.KNOCKBACK_RESISTANCE, -1.0);
    }

    public static boolean canSpawnPredicate(EntityType<Chatterer> type, ServerLevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        return worldIn.m_46791_() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(worldIn, pos, randomIn) && pos.m_123342_() < worldIn.m_141937_() + 64 && m_217057_(type, worldIn, reason, pos, randomIn);
    }

    @Override
    public IFaction getFaction() {
        return Factions.UNDEAD;
    }

    @Override
    public CompoundTag getPacketData() {
        return new CompoundTag();
    }

    @Override
    public void handlePacketData(CompoundTag nbt) {
    }

    private Chatterer.Action getAction() {
        int ordinal = (this.getStateFlag() & 2040) >> 3 & 0xFF;
        return ordinal >= Chatterer.Action.values().length ? Chatterer.Action.IDLE : Chatterer.Action.values()[ordinal];
    }

    private void setAction(Chatterer.Action action) {
        byte ordinal = (byte) ((action.ordinal() & 0xFF) << 3);
        int flag = this.getStateFlag();
        flag &= -2041;
        flag |= ordinal;
        this.setState(new int[] { flag });
    }

    @Override
    protected float keepDistanceFromAttackTarget() {
        return 6.0F;
    }

    @Override
    protected void addControllerListeners(AnimationController<Chatterer> controller) {
        controller.transitionLength(2);
        controller.setSoundKeyframeHandler(this);
    }

    @Override
    protected PlayState handleAnimState(AnimationState<? extends BaseFlyingFactionMob<?>> state) {
        RawAnimation builder = RawAnimation.begin();
        if (this.getAction() != Chatterer.Action.IDLE) {
            switch(this.getAction()) {
                case CHATTERING:
                    builder.thenPlay("animation.chatterer.chatter");
                    break;
                case DESTRUCTING:
                    builder.thenPlayAndHold("animation.chatterer.destruct");
                    break;
                case PARALYZED:
                    builder.thenPlay("animation.chatterer.paralyzed");
            }
        }
        if (builder.getAnimationStages().size() == 0) {
            if (this.m_20184_().length() > 0.1F) {
                builder.thenLoop("animation.chatterer.move");
            } else if (this.m_5448_() != null && this.m_5448_().isAlive()) {
                builder.thenLoop("animation.chatterer.idle_hostile");
            } else {
                builder.thenLoop("animation.chatterer.idle");
            }
        }
        return state.setAndContinue(builder);
    }

    @Override
    public void handle(SoundKeyframeEvent<Chatterer> event) {
        if (event.getKeyframeData().getSound().contains("click")) {
            this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), SFX.Entity.Chatterer.CLACK, this.m_5720_(), 1.0F, (float) (0.95 + Math.random() * 0.1), false);
        }
    }

    public static enum Action {

        IDLE, CHATTERING, PARALYZED, DESTRUCTING
    }

    private class Chatter extends Goal {

        @Override
        public boolean canUse() {
            if (!Chatterer.this.isOnCooldown(1000) && Chatterer.this.m_5448_() != null && Chatterer.this.getAction() == Chatterer.Action.IDLE) {
                LivingEntity target = Chatterer.this.m_5448_();
                if (Chatterer.this.m_20270_(target) > 8.0F) {
                    return false;
                } else if (!target.hasEffect(MobEffects.BLINDNESS)) {
                    return true;
                } else if (Chatterer.this.getTier() > 0 && !target.hasEffect(EffectInit.FORTIFICATION.get())) {
                    return true;
                } else {
                    return Chatterer.this.getTier() == 1 && !target.hasEffect(MobEffects.POISON) ? true : Chatterer.this.getTier() == 2 && !target.hasEffect(MobEffects.WITHER);
                }
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            Chatterer.this.setCooldown(1000, 60);
            Chatterer.this.setAction(Chatterer.Action.CHATTERING);
            LivingEntity target = Chatterer.this.m_5448_();
            Chatterer.this.setTimer("chatter", 36, () -> {
                if (Chatterer.this.getAction() == Chatterer.Action.CHATTERING) {
                    Chatterer.this.setAction(Chatterer.Action.IDLE);
                    if (!(Chatterer.this.m_20270_(target) > 8.0F)) {
                        if (!target.hasEffect(MobEffects.BLINDNESS)) {
                            Chatterer.this.setCooldown(1000, 80);
                            target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 300, 0));
                        } else if (Chatterer.this.getTier() > 0 && !target.hasEffect(EffectInit.FORTIFICATION.get())) {
                            Chatterer.this.setCooldown(1000, 100);
                            target.addEffect(new MobEffectInstance(EffectInit.FORTIFICATION.get(), 200, 0));
                        } else if (Chatterer.this.getTier() == 1 && !target.hasEffect(MobEffects.POISON)) {
                            Chatterer.this.setCooldown(1000, 100);
                            target.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 1));
                        } else if (Chatterer.this.getTier() == 2 && !target.hasEffect(MobEffects.WITHER)) {
                            Chatterer.this.setCooldown(1000, 100);
                            target.addEffect(new MobEffectInstance(MobEffects.WITHER, 200, 1));
                        }
                    }
                }
            });
        }
    }

    private class SummonReinforcements extends Goal {

        @Override
        public boolean canUse() {
            if (Chatterer.this.m_5448_() != null && Chatterer.this.getAction() == Chatterer.Action.IDLE) {
                LivingEntity target = Chatterer.this.m_5448_();
                if (Chatterer.this.m_20270_(target) > 16.0F) {
                    return false;
                } else if (!target.hasEffect(MobEffects.BLINDNESS)) {
                    return false;
                } else if (Chatterer.this.getTier() > 0 && !target.hasEffect(EffectInit.FORTIFICATION.get())) {
                    return false;
                } else {
                    return Chatterer.this.getTier() == 1 && !target.hasEffect(MobEffects.POISON) ? false : Chatterer.this.getTier() != 2 || target.hasEffect(MobEffects.WITHER);
                }
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            Chatterer.this.setAction(Chatterer.Action.DESTRUCTING);
            if (!Chatterer.this.m_9236_().isClientSide()) {
                Chatterer.this.setTimer("destruct", 75, () -> {
                    int tier = Chatterer.this.getTier();
                    int numSkeletons = 0;
                    int numZombies = 0;
                    ArrayList<Mob> spawns = new ArrayList();
                    spawns.add(new SkeletonAssassin(Chatterer.this.m_9236_()));
                    if (tier == 0) {
                        numZombies = 1 + (int) (Math.random() * 2.0);
                    } else if (tier == 1) {
                        numZombies = 1 + (int) (Math.random() * 2.0);
                        numSkeletons = 1 + (int) (Math.random() * 1.0);
                    } else {
                        numZombies = 1 + (int) (Math.random() * 3.0);
                        numSkeletons = 1 + (int) (Math.random() * 2.0);
                        if (Math.random() < 0.5) {
                            spawns.add(new HulkingZombie(Chatterer.this.m_9236_()));
                        }
                    }
                    for (int i = 0; i < numZombies; i++) {
                        spawns.add(new Zombie(Chatterer.this.m_9236_()));
                    }
                    for (int i = 0; i < numSkeletons; i++) {
                        spawns.add(new Skeleton(EntityType.SKELETON, Chatterer.this.m_9236_()));
                    }
                    spawns.forEach(z -> {
                        z.m_146884_(Chatterer.this.m_20182_().add(-1.0 + Math.random() * 2.0, 0.0, -1.0 + Math.random() * 2.0));
                        z.setTarget(Chatterer.this.m_5448_());
                        Chatterer.this.m_9236_().m_7967_(z);
                    });
                    Chatterer.this.m_146870_();
                });
            }
        }
    }
}