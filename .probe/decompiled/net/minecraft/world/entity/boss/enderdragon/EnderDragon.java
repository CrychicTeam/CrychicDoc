package net.minecraft.world.entity.boss.enderdragon;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhaseManager;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import net.minecraft.world.level.pathfinder.BinaryHeap;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

public class EnderDragon extends Mob implements Enemy {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final EntityDataAccessor<Integer> DATA_PHASE = SynchedEntityData.defineId(EnderDragon.class, EntityDataSerializers.INT);

    private static final TargetingConditions CRYSTAL_DESTROY_TARGETING = TargetingConditions.forCombat().range(64.0);

    private static final int GROWL_INTERVAL_MIN = 200;

    private static final int GROWL_INTERVAL_MAX = 400;

    private static final float SITTING_ALLOWED_DAMAGE_PERCENTAGE = 0.25F;

    private static final String DRAGON_DEATH_TIME_KEY = "DragonDeathTime";

    private static final String DRAGON_PHASE_KEY = "DragonPhase";

    public final double[][] positions = new double[64][3];

    public int posPointer = -1;

    private final EnderDragonPart[] subEntities;

    public final EnderDragonPart head;

    private final EnderDragonPart neck;

    private final EnderDragonPart body;

    private final EnderDragonPart tail1;

    private final EnderDragonPart tail2;

    private final EnderDragonPart tail3;

    private final EnderDragonPart wing1;

    private final EnderDragonPart wing2;

    public float oFlapTime;

    public float flapTime;

    public boolean inWall;

    public int dragonDeathTime;

    public float yRotA;

    @Nullable
    public EndCrystal nearestCrystal;

    @Nullable
    private EndDragonFight dragonFight;

    private BlockPos fightOrigin = BlockPos.ZERO;

    private final EnderDragonPhaseManager phaseManager;

    private int growlTime = 100;

    private float sittingDamageReceived;

    private final Node[] nodes = new Node[24];

    private final int[] nodeAdjacency = new int[24];

    private final BinaryHeap openSet = new BinaryHeap();

    public EnderDragon(EntityType<? extends EnderDragon> entityTypeExtendsEnderDragon0, Level level1) {
        super(EntityType.ENDER_DRAGON, level1);
        this.head = new EnderDragonPart(this, "head", 1.0F, 1.0F);
        this.neck = new EnderDragonPart(this, "neck", 3.0F, 3.0F);
        this.body = new EnderDragonPart(this, "body", 5.0F, 3.0F);
        this.tail1 = new EnderDragonPart(this, "tail", 2.0F, 2.0F);
        this.tail2 = new EnderDragonPart(this, "tail", 2.0F, 2.0F);
        this.tail3 = new EnderDragonPart(this, "tail", 2.0F, 2.0F);
        this.wing1 = new EnderDragonPart(this, "wing", 4.0F, 2.0F);
        this.wing2 = new EnderDragonPart(this, "wing", 4.0F, 2.0F);
        this.subEntities = new EnderDragonPart[] { this.head, this.neck, this.body, this.tail1, this.tail2, this.tail3, this.wing1, this.wing2 };
        this.m_21153_(this.m_21233_());
        this.f_19794_ = true;
        this.f_19811_ = true;
        this.phaseManager = new EnderDragonPhaseManager(this);
    }

    public void setDragonFight(EndDragonFight endDragonFight0) {
        this.dragonFight = endDragonFight0;
    }

    public void setFightOrigin(BlockPos blockPos0) {
        this.fightOrigin = blockPos0;
    }

    public BlockPos getFightOrigin() {
        return this.fightOrigin;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 200.0);
    }

    @Override
    public boolean isFlapping() {
        float $$0 = Mth.cos(this.flapTime * (float) (Math.PI * 2));
        float $$1 = Mth.cos(this.oFlapTime * (float) (Math.PI * 2));
        return $$1 <= -0.3F && $$0 >= -0.3F;
    }

    @Override
    public void onFlap() {
        if (this.m_9236_().isClientSide && !this.m_20067_()) {
            this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.ENDER_DRAGON_FLAP, this.getSoundSource(), 5.0F, 0.8F + this.f_19796_.nextFloat() * 0.3F, false);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.m_20088_().define(DATA_PHASE, EnderDragonPhase.HOVERING.getId());
    }

    public double[] getLatencyPos(int int0, float float1) {
        if (this.m_21224_()) {
            float1 = 0.0F;
        }
        float1 = 1.0F - float1;
        int $$2 = this.posPointer - int0 & 63;
        int $$3 = this.posPointer - int0 - 1 & 63;
        double[] $$4 = new double[3];
        double $$5 = this.positions[$$2][0];
        double $$6 = Mth.wrapDegrees(this.positions[$$3][0] - $$5);
        $$4[0] = $$5 + $$6 * (double) float1;
        $$5 = this.positions[$$2][1];
        $$6 = this.positions[$$3][1] - $$5;
        $$4[1] = $$5 + $$6 * (double) float1;
        $$4[2] = Mth.lerp((double) float1, this.positions[$$2][2], this.positions[$$3][2]);
        return $$4;
    }

    @Override
    public void aiStep() {
        this.m_146874_();
        if (this.m_9236_().isClientSide) {
            this.m_21153_(this.m_21223_());
            if (!this.m_20067_() && !this.phaseManager.getCurrentPhase().isSitting() && --this.growlTime < 0) {
                this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.ENDER_DRAGON_GROWL, this.getSoundSource(), 2.5F, 0.8F + this.f_19796_.nextFloat() * 0.3F, false);
                this.growlTime = 200 + this.f_19796_.nextInt(200);
            }
        }
        if (this.dragonFight == null && this.m_9236_() instanceof ServerLevel $$0) {
            EndDragonFight $$1 = $$0.getDragonFight();
            if ($$1 != null && this.m_20148_().equals($$1.getDragonUUID())) {
                this.dragonFight = $$1;
            }
        }
        this.oFlapTime = this.flapTime;
        if (this.m_21224_()) {
            float $$2 = (this.f_19796_.nextFloat() - 0.5F) * 8.0F;
            float $$3 = (this.f_19796_.nextFloat() - 0.5F) * 4.0F;
            float $$4 = (this.f_19796_.nextFloat() - 0.5F) * 8.0F;
            this.m_9236_().addParticle(ParticleTypes.EXPLOSION, this.m_20185_() + (double) $$2, this.m_20186_() + 2.0 + (double) $$3, this.m_20189_() + (double) $$4, 0.0, 0.0, 0.0);
        } else {
            this.checkCrystals();
            Vec3 $$5 = this.m_20184_();
            float $$6 = 0.2F / ((float) $$5.horizontalDistance() * 10.0F + 1.0F);
            $$6 *= (float) Math.pow(2.0, $$5.y);
            if (this.phaseManager.getCurrentPhase().isSitting()) {
                this.flapTime += 0.1F;
            } else if (this.inWall) {
                this.flapTime += $$6 * 0.5F;
            } else {
                this.flapTime += $$6;
            }
            this.m_146922_(Mth.wrapDegrees(this.m_146908_()));
            if (this.m_21525_()) {
                this.flapTime = 0.5F;
            } else {
                if (this.posPointer < 0) {
                    for (int $$7 = 0; $$7 < this.positions.length; $$7++) {
                        this.positions[$$7][0] = (double) this.m_146908_();
                        this.positions[$$7][1] = this.m_20186_();
                    }
                }
                if (++this.posPointer == this.positions.length) {
                    this.posPointer = 0;
                }
                this.positions[this.posPointer][0] = (double) this.m_146908_();
                this.positions[this.posPointer][1] = this.m_20186_();
                if (this.m_9236_().isClientSide) {
                    if (this.f_20903_ > 0) {
                        double $$8 = this.m_20185_() + (this.f_20904_ - this.m_20185_()) / (double) this.f_20903_;
                        double $$9 = this.m_20186_() + (this.f_20905_ - this.m_20186_()) / (double) this.f_20903_;
                        double $$10 = this.m_20189_() + (this.f_20906_ - this.m_20189_()) / (double) this.f_20903_;
                        double $$11 = Mth.wrapDegrees(this.f_20907_ - (double) this.m_146908_());
                        this.m_146922_(this.m_146908_() + (float) $$11 / (float) this.f_20903_);
                        this.m_146926_(this.m_146909_() + (float) (this.f_20908_ - (double) this.m_146909_()) / (float) this.f_20903_);
                        this.f_20903_--;
                        this.m_6034_($$8, $$9, $$10);
                        this.m_19915_(this.m_146908_(), this.m_146909_());
                    }
                    this.phaseManager.getCurrentPhase().doClientTick();
                } else {
                    DragonPhaseInstance $$12 = this.phaseManager.getCurrentPhase();
                    $$12.doServerTick();
                    if (this.phaseManager.getCurrentPhase() != $$12) {
                        $$12 = this.phaseManager.getCurrentPhase();
                        $$12.doServerTick();
                    }
                    Vec3 $$13 = $$12.getFlyTargetLocation();
                    if ($$13 != null) {
                        double $$14 = $$13.x - this.m_20185_();
                        double $$15 = $$13.y - this.m_20186_();
                        double $$16 = $$13.z - this.m_20189_();
                        double $$17 = $$14 * $$14 + $$15 * $$15 + $$16 * $$16;
                        float $$18 = $$12.getFlySpeed();
                        double $$19 = Math.sqrt($$14 * $$14 + $$16 * $$16);
                        if ($$19 > 0.0) {
                            $$15 = Mth.clamp($$15 / $$19, (double) (-$$18), (double) $$18);
                        }
                        this.m_20256_(this.m_20184_().add(0.0, $$15 * 0.01, 0.0));
                        this.m_146922_(Mth.wrapDegrees(this.m_146908_()));
                        Vec3 $$20 = $$13.subtract(this.m_20185_(), this.m_20186_(), this.m_20189_()).normalize();
                        Vec3 $$21 = new Vec3((double) Mth.sin(this.m_146908_() * (float) (Math.PI / 180.0)), this.m_20184_().y, (double) (-Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0)))).normalize();
                        float $$22 = Math.max(((float) $$21.dot($$20) + 0.5F) / 1.5F, 0.0F);
                        if (Math.abs($$14) > 1.0E-5F || Math.abs($$16) > 1.0E-5F) {
                            float $$23 = Mth.clamp(Mth.wrapDegrees(180.0F - (float) Mth.atan2($$14, $$16) * (180.0F / (float) Math.PI) - this.m_146908_()), -50.0F, 50.0F);
                            this.yRotA *= 0.8F;
                            this.yRotA = this.yRotA + $$23 * $$12.getTurnSpeed();
                            this.m_146922_(this.m_146908_() + this.yRotA * 0.1F);
                        }
                        float $$24 = (float) (2.0 / ($$17 + 1.0));
                        float $$25 = 0.06F;
                        this.m_19920_(0.06F * ($$22 * $$24 + (1.0F - $$24)), new Vec3(0.0, 0.0, -1.0));
                        if (this.inWall) {
                            this.m_6478_(MoverType.SELF, this.m_20184_().scale(0.8F));
                        } else {
                            this.m_6478_(MoverType.SELF, this.m_20184_());
                        }
                        Vec3 $$26 = this.m_20184_().normalize();
                        double $$27 = 0.8 + 0.15 * ($$26.dot($$21) + 1.0) / 2.0;
                        this.m_20256_(this.m_20184_().multiply($$27, 0.91F, $$27));
                    }
                }
                this.f_20883_ = this.m_146908_();
                Vec3[] $$28 = new Vec3[this.subEntities.length];
                for (int $$29 = 0; $$29 < this.subEntities.length; $$29++) {
                    $$28[$$29] = new Vec3(this.subEntities[$$29].m_20185_(), this.subEntities[$$29].m_20186_(), this.subEntities[$$29].m_20189_());
                }
                float $$30 = (float) (this.getLatencyPos(5, 1.0F)[1] - this.getLatencyPos(10, 1.0F)[1]) * 10.0F * (float) (Math.PI / 180.0);
                float $$31 = Mth.cos($$30);
                float $$32 = Mth.sin($$30);
                float $$33 = this.m_146908_() * (float) (Math.PI / 180.0);
                float $$34 = Mth.sin($$33);
                float $$35 = Mth.cos($$33);
                this.tickPart(this.body, (double) ($$34 * 0.5F), 0.0, (double) (-$$35 * 0.5F));
                this.tickPart(this.wing1, (double) ($$35 * 4.5F), 2.0, (double) ($$34 * 4.5F));
                this.tickPart(this.wing2, (double) ($$35 * -4.5F), 2.0, (double) ($$34 * -4.5F));
                if (!this.m_9236_().isClientSide && this.f_20916_ == 0) {
                    this.knockBack(this.m_9236_().getEntities(this, this.wing1.m_20191_().inflate(4.0, 2.0, 4.0).move(0.0, -2.0, 0.0), EntitySelector.NO_CREATIVE_OR_SPECTATOR));
                    this.knockBack(this.m_9236_().getEntities(this, this.wing2.m_20191_().inflate(4.0, 2.0, 4.0).move(0.0, -2.0, 0.0), EntitySelector.NO_CREATIVE_OR_SPECTATOR));
                    this.hurt(this.m_9236_().getEntities(this, this.head.m_20191_().inflate(1.0), EntitySelector.NO_CREATIVE_OR_SPECTATOR));
                    this.hurt(this.m_9236_().getEntities(this, this.neck.m_20191_().inflate(1.0), EntitySelector.NO_CREATIVE_OR_SPECTATOR));
                }
                float $$36 = Mth.sin(this.m_146908_() * (float) (Math.PI / 180.0) - this.yRotA * 0.01F);
                float $$37 = Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0) - this.yRotA * 0.01F);
                float $$38 = this.getHeadYOffset();
                this.tickPart(this.head, (double) ($$36 * 6.5F * $$31), (double) ($$38 + $$32 * 6.5F), (double) (-$$37 * 6.5F * $$31));
                this.tickPart(this.neck, (double) ($$36 * 5.5F * $$31), (double) ($$38 + $$32 * 5.5F), (double) (-$$37 * 5.5F * $$31));
                double[] $$39 = this.getLatencyPos(5, 1.0F);
                for (int $$40 = 0; $$40 < 3; $$40++) {
                    EnderDragonPart $$41 = null;
                    if ($$40 == 0) {
                        $$41 = this.tail1;
                    }
                    if ($$40 == 1) {
                        $$41 = this.tail2;
                    }
                    if ($$40 == 2) {
                        $$41 = this.tail3;
                    }
                    double[] $$42 = this.getLatencyPos(12 + $$40 * 2, 1.0F);
                    float $$43 = this.m_146908_() * (float) (Math.PI / 180.0) + this.rotWrap($$42[0] - $$39[0]) * (float) (Math.PI / 180.0);
                    float $$44 = Mth.sin($$43);
                    float $$45 = Mth.cos($$43);
                    float $$46 = 1.5F;
                    float $$47 = (float) ($$40 + 1) * 2.0F;
                    this.tickPart($$41, (double) (-($$34 * 1.5F + $$44 * $$47) * $$31), $$42[1] - $$39[1] - (double) (($$47 + 1.5F) * $$32) + 1.5, (double) (($$35 * 1.5F + $$45 * $$47) * $$31));
                }
                if (!this.m_9236_().isClientSide) {
                    this.inWall = this.checkWalls(this.head.m_20191_()) | this.checkWalls(this.neck.m_20191_()) | this.checkWalls(this.body.m_20191_());
                    if (this.dragonFight != null) {
                        this.dragonFight.updateDragon(this);
                    }
                }
                for (int $$48 = 0; $$48 < this.subEntities.length; $$48++) {
                    this.subEntities[$$48].f_19854_ = $$28[$$48].x;
                    this.subEntities[$$48].f_19855_ = $$28[$$48].y;
                    this.subEntities[$$48].f_19856_ = $$28[$$48].z;
                    this.subEntities[$$48].f_19790_ = $$28[$$48].x;
                    this.subEntities[$$48].f_19791_ = $$28[$$48].y;
                    this.subEntities[$$48].f_19792_ = $$28[$$48].z;
                }
            }
        }
    }

    private void tickPart(EnderDragonPart enderDragonPart0, double double1, double double2, double double3) {
        enderDragonPart0.m_6034_(this.m_20185_() + double1, this.m_20186_() + double2, this.m_20189_() + double3);
    }

    private float getHeadYOffset() {
        if (this.phaseManager.getCurrentPhase().isSitting()) {
            return -1.0F;
        } else {
            double[] $$0 = this.getLatencyPos(5, 1.0F);
            double[] $$1 = this.getLatencyPos(0, 1.0F);
            return (float) ($$0[1] - $$1[1]);
        }
    }

    private void checkCrystals() {
        if (this.nearestCrystal != null) {
            if (this.nearestCrystal.m_213877_()) {
                this.nearestCrystal = null;
            } else if (this.f_19797_ % 10 == 0 && this.m_21223_() < this.m_21233_()) {
                this.m_21153_(this.m_21223_() + 1.0F);
            }
        }
        if (this.f_19796_.nextInt(10) == 0) {
            List<EndCrystal> $$0 = this.m_9236_().m_45976_(EndCrystal.class, this.m_20191_().inflate(32.0));
            EndCrystal $$1 = null;
            double $$2 = Double.MAX_VALUE;
            for (EndCrystal $$3 : $$0) {
                double $$4 = $$3.m_20280_(this);
                if ($$4 < $$2) {
                    $$2 = $$4;
                    $$1 = $$3;
                }
            }
            this.nearestCrystal = $$1;
        }
    }

    private void knockBack(List<Entity> listEntity0) {
        double $$1 = (this.body.m_20191_().minX + this.body.m_20191_().maxX) / 2.0;
        double $$2 = (this.body.m_20191_().minZ + this.body.m_20191_().maxZ) / 2.0;
        for (Entity $$3 : listEntity0) {
            if ($$3 instanceof LivingEntity) {
                double $$4 = $$3.getX() - $$1;
                double $$5 = $$3.getZ() - $$2;
                double $$6 = Math.max($$4 * $$4 + $$5 * $$5, 0.1);
                $$3.push($$4 / $$6 * 4.0, 0.2F, $$5 / $$6 * 4.0);
                if (!this.phaseManager.getCurrentPhase().isSitting() && ((LivingEntity) $$3).getLastHurtByMobTimestamp() < $$3.tickCount - 2) {
                    $$3.hurt(this.m_269291_().mobAttack(this), 5.0F);
                    this.m_19970_(this, $$3);
                }
            }
        }
    }

    private void hurt(List<Entity> listEntity0) {
        for (Entity $$1 : listEntity0) {
            if ($$1 instanceof LivingEntity) {
                $$1.hurt(this.m_269291_().mobAttack(this), 10.0F);
                this.m_19970_(this, $$1);
            }
        }
    }

    private float rotWrap(double double0) {
        return (float) Mth.wrapDegrees(double0);
    }

    private boolean checkWalls(AABB aABB0) {
        int $$1 = Mth.floor(aABB0.minX);
        int $$2 = Mth.floor(aABB0.minY);
        int $$3 = Mth.floor(aABB0.minZ);
        int $$4 = Mth.floor(aABB0.maxX);
        int $$5 = Mth.floor(aABB0.maxY);
        int $$6 = Mth.floor(aABB0.maxZ);
        boolean $$7 = false;
        boolean $$8 = false;
        for (int $$9 = $$1; $$9 <= $$4; $$9++) {
            for (int $$10 = $$2; $$10 <= $$5; $$10++) {
                for (int $$11 = $$3; $$11 <= $$6; $$11++) {
                    BlockPos $$12 = new BlockPos($$9, $$10, $$11);
                    BlockState $$13 = this.m_9236_().getBlockState($$12);
                    if (!$$13.m_60795_() && !$$13.m_204336_(BlockTags.DRAGON_TRANSPARENT)) {
                        if (this.m_9236_().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && !$$13.m_204336_(BlockTags.DRAGON_IMMUNE)) {
                            $$8 = this.m_9236_().removeBlock($$12, false) || $$8;
                        } else {
                            $$7 = true;
                        }
                    }
                }
            }
        }
        if ($$8) {
            BlockPos $$14 = new BlockPos($$1 + this.f_19796_.nextInt($$4 - $$1 + 1), $$2 + this.f_19796_.nextInt($$5 - $$2 + 1), $$3 + this.f_19796_.nextInt($$6 - $$3 + 1));
            this.m_9236_().m_46796_(2008, $$14, 0);
        }
        return $$7;
    }

    public boolean hurt(EnderDragonPart enderDragonPart0, DamageSource damageSource1, float float2) {
        if (this.phaseManager.getCurrentPhase().getPhase() == EnderDragonPhase.DYING) {
            return false;
        } else {
            float2 = this.phaseManager.getCurrentPhase().onHurt(damageSource1, float2);
            if (enderDragonPart0 != this.head) {
                float2 = float2 / 4.0F + Math.min(float2, 1.0F);
            }
            if (float2 < 0.01F) {
                return false;
            } else {
                if (damageSource1.getEntity() instanceof Player || damageSource1.is(DamageTypeTags.ALWAYS_HURTS_ENDER_DRAGONS)) {
                    float $$3 = this.m_21223_();
                    this.reallyHurt(damageSource1, float2);
                    if (this.m_21224_() && !this.phaseManager.getCurrentPhase().isSitting()) {
                        this.m_21153_(1.0F);
                        this.phaseManager.setPhase(EnderDragonPhase.DYING);
                    }
                    if (this.phaseManager.getCurrentPhase().isSitting()) {
                        this.sittingDamageReceived = this.sittingDamageReceived + $$3 - this.m_21223_();
                        if (this.sittingDamageReceived > 0.25F * this.m_21233_()) {
                            this.sittingDamageReceived = 0.0F;
                            this.phaseManager.setPhase(EnderDragonPhase.TAKEOFF);
                        }
                    }
                }
                return true;
            }
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        return !this.m_9236_().isClientSide ? this.hurt(this.body, damageSource0, float1) : false;
    }

    protected boolean reallyHurt(DamageSource damageSource0, float float1) {
        return super.m_6469_(damageSource0, float1);
    }

    @Override
    public void kill() {
        this.m_142687_(Entity.RemovalReason.KILLED);
        this.m_146850_(GameEvent.ENTITY_DIE);
        if (this.dragonFight != null) {
            this.dragonFight.updateDragon(this);
            this.dragonFight.setDragonKilled(this);
        }
    }

    @Override
    protected void tickDeath() {
        if (this.dragonFight != null) {
            this.dragonFight.updateDragon(this);
        }
        this.dragonDeathTime++;
        if (this.dragonDeathTime >= 180 && this.dragonDeathTime <= 200) {
            float $$0 = (this.f_19796_.nextFloat() - 0.5F) * 8.0F;
            float $$1 = (this.f_19796_.nextFloat() - 0.5F) * 4.0F;
            float $$2 = (this.f_19796_.nextFloat() - 0.5F) * 8.0F;
            this.m_9236_().addParticle(ParticleTypes.EXPLOSION_EMITTER, this.m_20185_() + (double) $$0, this.m_20186_() + 2.0 + (double) $$1, this.m_20189_() + (double) $$2, 0.0, 0.0, 0.0);
        }
        boolean $$3 = this.m_9236_().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT);
        int $$4 = 500;
        if (this.dragonFight != null && !this.dragonFight.hasPreviouslyKilledDragon()) {
            $$4 = 12000;
        }
        if (this.m_9236_() instanceof ServerLevel) {
            if (this.dragonDeathTime > 150 && this.dragonDeathTime % 5 == 0 && $$3) {
                ExperienceOrb.award((ServerLevel) this.m_9236_(), this.m_20182_(), Mth.floor((float) $$4 * 0.08F));
            }
            if (this.dragonDeathTime == 1 && !this.m_20067_()) {
                this.m_9236_().globalLevelEvent(1028, this.m_20183_(), 0);
            }
        }
        this.m_6478_(MoverType.SELF, new Vec3(0.0, 0.1F, 0.0));
        if (this.dragonDeathTime == 200 && this.m_9236_() instanceof ServerLevel) {
            if ($$3) {
                ExperienceOrb.award((ServerLevel) this.m_9236_(), this.m_20182_(), Mth.floor((float) $$4 * 0.2F));
            }
            if (this.dragonFight != null) {
                this.dragonFight.setDragonKilled(this);
            }
            this.m_142687_(Entity.RemovalReason.KILLED);
            this.m_146850_(GameEvent.ENTITY_DIE);
        }
    }

    public int findClosestNode() {
        if (this.nodes[0] == null) {
            for (int $$0 = 0; $$0 < 24; $$0++) {
                int $$1 = 5;
                int $$3;
                int $$4;
                if ($$0 < 12) {
                    $$3 = Mth.floor(60.0F * Mth.cos(2.0F * ((float) -Math.PI + (float) (Math.PI / 12) * (float) $$0)));
                    $$4 = Mth.floor(60.0F * Mth.sin(2.0F * ((float) -Math.PI + (float) (Math.PI / 12) * (float) $$0)));
                } else if ($$0 < 20) {
                    int $$2 = $$0 - 12;
                    $$3 = Mth.floor(40.0F * Mth.cos(2.0F * ((float) -Math.PI + (float) (Math.PI / 8) * (float) $$2)));
                    $$4 = Mth.floor(40.0F * Mth.sin(2.0F * ((float) -Math.PI + (float) (Math.PI / 8) * (float) $$2)));
                    $$1 += 10;
                } else {
                    int var7 = $$0 - 20;
                    $$3 = Mth.floor(20.0F * Mth.cos(2.0F * ((float) -Math.PI + (float) (Math.PI / 4) * (float) var7)));
                    $$4 = Mth.floor(20.0F * Mth.sin(2.0F * ((float) -Math.PI + (float) (Math.PI / 4) * (float) var7)));
                }
                int $$9 = Math.max(this.m_9236_().getSeaLevel() + 10, this.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new BlockPos($$3, 0, $$4)).m_123342_() + $$1);
                this.nodes[$$0] = new Node($$3, $$9, $$4);
            }
            this.nodeAdjacency[0] = 6146;
            this.nodeAdjacency[1] = 8197;
            this.nodeAdjacency[2] = 8202;
            this.nodeAdjacency[3] = 16404;
            this.nodeAdjacency[4] = 32808;
            this.nodeAdjacency[5] = 32848;
            this.nodeAdjacency[6] = 65696;
            this.nodeAdjacency[7] = 131392;
            this.nodeAdjacency[8] = 131712;
            this.nodeAdjacency[9] = 263424;
            this.nodeAdjacency[10] = 526848;
            this.nodeAdjacency[11] = 525313;
            this.nodeAdjacency[12] = 1581057;
            this.nodeAdjacency[13] = 3166214;
            this.nodeAdjacency[14] = 2138120;
            this.nodeAdjacency[15] = 6373424;
            this.nodeAdjacency[16] = 4358208;
            this.nodeAdjacency[17] = 12910976;
            this.nodeAdjacency[18] = 9044480;
            this.nodeAdjacency[19] = 9706496;
            this.nodeAdjacency[20] = 15216640;
            this.nodeAdjacency[21] = 13688832;
            this.nodeAdjacency[22] = 11763712;
            this.nodeAdjacency[23] = 8257536;
        }
        return this.findClosestNode(this.m_20185_(), this.m_20186_(), this.m_20189_());
    }

    public int findClosestNode(double double0, double double1, double double2) {
        float $$3 = 10000.0F;
        int $$4 = 0;
        Node $$5 = new Node(Mth.floor(double0), Mth.floor(double1), Mth.floor(double2));
        int $$6 = 0;
        if (this.dragonFight == null || this.dragonFight.getCrystalsAlive() == 0) {
            $$6 = 12;
        }
        for (int $$7 = $$6; $$7 < 24; $$7++) {
            if (this.nodes[$$7] != null) {
                float $$8 = this.nodes[$$7].distanceToSqr($$5);
                if ($$8 < $$3) {
                    $$3 = $$8;
                    $$4 = $$7;
                }
            }
        }
        return $$4;
    }

    @Nullable
    public Path findPath(int int0, int int1, @Nullable Node node2) {
        for (int $$3 = 0; $$3 < 24; $$3++) {
            Node $$4 = this.nodes[$$3];
            $$4.closed = false;
            $$4.f = 0.0F;
            $$4.g = 0.0F;
            $$4.h = 0.0F;
            $$4.cameFrom = null;
            $$4.heapIdx = -1;
        }
        Node $$5 = this.nodes[int0];
        Node $$6 = this.nodes[int1];
        $$5.g = 0.0F;
        $$5.h = $$5.distanceTo($$6);
        $$5.f = $$5.h;
        this.openSet.clear();
        this.openSet.insert($$5);
        Node $$7 = $$5;
        int $$8 = 0;
        if (this.dragonFight == null || this.dragonFight.getCrystalsAlive() == 0) {
            $$8 = 12;
        }
        while (!this.openSet.isEmpty()) {
            Node $$9 = this.openSet.pop();
            if ($$9.equals($$6)) {
                if (node2 != null) {
                    node2.cameFrom = $$6;
                    $$6 = node2;
                }
                return this.reconstructPath($$5, $$6);
            }
            if ($$9.distanceTo($$6) < $$7.distanceTo($$6)) {
                $$7 = $$9;
            }
            $$9.closed = true;
            int $$10 = 0;
            for (int $$11 = 0; $$11 < 24; $$11++) {
                if (this.nodes[$$11] == $$9) {
                    $$10 = $$11;
                    break;
                }
            }
            for (int $$12 = $$8; $$12 < 24; $$12++) {
                if ((this.nodeAdjacency[$$10] & 1 << $$12) > 0) {
                    Node $$13 = this.nodes[$$12];
                    if (!$$13.closed) {
                        float $$14 = $$9.g + $$9.distanceTo($$13);
                        if (!$$13.inOpenSet() || $$14 < $$13.g) {
                            $$13.cameFrom = $$9;
                            $$13.g = $$14;
                            $$13.h = $$13.distanceTo($$6);
                            if ($$13.inOpenSet()) {
                                this.openSet.changeCost($$13, $$13.g + $$13.h);
                            } else {
                                $$13.f = $$13.g + $$13.h;
                                this.openSet.insert($$13);
                            }
                        }
                    }
                }
            }
        }
        if ($$7 == $$5) {
            return null;
        } else {
            LOGGER.debug("Failed to find path from {} to {}", int0, int1);
            if (node2 != null) {
                node2.cameFrom = $$7;
                $$7 = node2;
            }
            return this.reconstructPath($$5, $$7);
        }
    }

    private Path reconstructPath(Node node0, Node node1) {
        List<Node> $$2 = Lists.newArrayList();
        Node $$3 = node1;
        $$2.add(0, node1);
        while ($$3.cameFrom != null) {
            $$3 = $$3.cameFrom;
            $$2.add(0, $$3);
        }
        return new Path($$2, new BlockPos(node1.x, node1.y, node1.z), true);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        compoundTag0.putInt("DragonPhase", this.phaseManager.getCurrentPhase().getPhase().getId());
        compoundTag0.putInt("DragonDeathTime", this.dragonDeathTime);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        if (compoundTag0.contains("DragonPhase")) {
            this.phaseManager.setPhase(EnderDragonPhase.getById(compoundTag0.getInt("DragonPhase")));
        }
        if (compoundTag0.contains("DragonDeathTime")) {
            this.dragonDeathTime = compoundTag0.getInt("DragonDeathTime");
        }
    }

    @Override
    public void checkDespawn() {
    }

    public EnderDragonPart[] getSubEntities() {
        return this.subEntities;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENDER_DRAGON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.ENDER_DRAGON_HURT;
    }

    @Override
    protected float getSoundVolume() {
        return 5.0F;
    }

    public float getHeadPartYOffset(int int0, double[] double1, double[] double2) {
        DragonPhaseInstance $$3 = this.phaseManager.getCurrentPhase();
        EnderDragonPhase<? extends DragonPhaseInstance> $$4 = $$3.getPhase();
        double $$7;
        if ($$4 == EnderDragonPhase.LANDING || $$4 == EnderDragonPhase.TAKEOFF) {
            BlockPos $$5 = this.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.getLocation(this.fightOrigin));
            double $$6 = Math.max(Math.sqrt($$5.m_203193_(this.m_20182_())) / 4.0, 1.0);
            $$7 = (double) int0 / $$6;
        } else if ($$3.isSitting()) {
            $$7 = (double) int0;
        } else if (int0 == 6) {
            $$7 = 0.0;
        } else {
            $$7 = double2[1] - double1[1];
        }
        return (float) $$7;
    }

    public Vec3 getHeadLookVector(float float0) {
        DragonPhaseInstance $$1 = this.phaseManager.getCurrentPhase();
        EnderDragonPhase<? extends DragonPhaseInstance> $$2 = $$1.getPhase();
        Vec3 $$8;
        if ($$2 == EnderDragonPhase.LANDING || $$2 == EnderDragonPhase.TAKEOFF) {
            BlockPos $$3 = this.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.getLocation(this.fightOrigin));
            float $$4 = Math.max((float) Math.sqrt($$3.m_203193_(this.m_20182_())) / 4.0F, 1.0F);
            float $$5 = 6.0F / $$4;
            float $$6 = this.m_146909_();
            float $$7 = 1.5F;
            this.m_146926_(-$$5 * 1.5F * 5.0F);
            $$8 = this.m_20252_(float0);
            this.m_146926_($$6);
        } else if ($$1.isSitting()) {
            float $$9 = this.m_146909_();
            float $$10 = 1.5F;
            this.m_146926_(-45.0F);
            $$8 = this.m_20252_(float0);
            this.m_146926_($$9);
        } else {
            $$8 = this.m_20252_(float0);
        }
        return $$8;
    }

    public void onCrystalDestroyed(EndCrystal endCrystal0, BlockPos blockPos1, DamageSource damageSource2) {
        Player $$3;
        if (damageSource2.getEntity() instanceof Player) {
            $$3 = (Player) damageSource2.getEntity();
        } else {
            $$3 = this.m_9236_().m_45941_(CRYSTAL_DESTROY_TARGETING, (double) blockPos1.m_123341_(), (double) blockPos1.m_123342_(), (double) blockPos1.m_123343_());
        }
        if (endCrystal0 == this.nearestCrystal) {
            this.hurt(this.head, this.m_269291_().explosion(endCrystal0, $$3), 10.0F);
        }
        this.phaseManager.getCurrentPhase().onCrystalDestroyed(endCrystal0, blockPos1, damageSource2, $$3);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor0) {
        if (DATA_PHASE.equals(entityDataAccessor0) && this.m_9236_().isClientSide) {
            this.phaseManager.setPhase(EnderDragonPhase.getById(this.m_20088_().get(DATA_PHASE)));
        }
        super.m_7350_(entityDataAccessor0);
    }

    public EnderDragonPhaseManager getPhaseManager() {
        return this.phaseManager;
    }

    @Nullable
    public EndDragonFight getDragonFight() {
        return this.dragonFight;
    }

    @Override
    public boolean addEffect(MobEffectInstance mobEffectInstance0, @Nullable Entity entity1) {
        return false;
    }

    @Override
    protected boolean canRide(Entity entity0) {
        return false;
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket clientboundAddEntityPacket0) {
        super.m_141965_(clientboundAddEntityPacket0);
        EnderDragonPart[] $$1 = this.getSubEntities();
        for (int $$2 = 0; $$2 < $$1.length; $$2++) {
            $$1[$$2].m_20234_($$2 + clientboundAddEntityPacket0.getId());
        }
    }

    @Override
    public boolean canAttack(LivingEntity livingEntity0) {
        return livingEntity0.canBeSeenAsEnemy();
    }

    @Override
    public double getPassengersRidingOffset() {
        return (double) this.body.m_20206_();
    }
}