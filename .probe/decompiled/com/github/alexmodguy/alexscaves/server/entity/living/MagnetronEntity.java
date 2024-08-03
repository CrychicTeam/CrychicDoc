package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.HeartOfIronBlock;
import com.github.alexmodguy.alexscaves.server.entity.util.MagnetronJoint;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;

public class MagnetronEntity extends Monster {

    private static final EntityDataAccessor<CompoundTag> BLOCKSTATES = SynchedEntityData.defineId(MagnetronEntity.class, EntityDataSerializers.COMPOUND_TAG);

    private static final EntityDataAccessor<CompoundTag> BLOCK_POSES = SynchedEntityData.defineId(MagnetronEntity.class, EntityDataSerializers.COMPOUND_TAG);

    private static final EntityDataAccessor<Boolean> FORMED = SynchedEntityData.defineId(MagnetronEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> ATTACK_POSE = SynchedEntityData.defineId(MagnetronEntity.class, EntityDataSerializers.INT);

    private static final float FORM_TIME = 40.0F;

    private static final int BLOCK_COUNT = MagnetronJoint.values().length * 2;

    public float clientRoll;

    private float prevWheelRot;

    private float wheelRot;

    private float prevWheelYaw;

    private float wheelYaw;

    private float prevRollLeanProgress;

    private float rollLeanProgress;

    private float prevFormProgress;

    private float formProgress;

    private boolean gravityFlag;

    private MagnetronEntity.AttackPose prevAttackPose = MagnetronEntity.AttackPose.NONE;

    private float prevAttackPoseProgress = 0.0F;

    private float attackPoseProgress = 0.0F;

    public final MagnetronPartEntity[] allParts = new MagnetronPartEntity[BLOCK_COUNT];

    public Vec3[] lightningAnimOffsets = new Vec3[6];

    private int syncCooldown = 0;

    private boolean isLandNavigator;

    private boolean hasFormedAttributes = false;

    private boolean droppedHeart = false;

    private int movingSoundTimer;

    public MagnetronEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.buildParts();
        this.prevFormProgress = this.formProgress = 40.0F;
        this.switchMoveController(false);
        Arrays.fill(this.lightningAnimOffsets, Vec3.ZERO);
        this.f_21364_ = 13;
    }

    private void switchMoveController(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = this.m_6037_(this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new MagnetronEntity.MoveController();
            this.f_21344_ = this.createFormedNavigation(this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    private PathNavigation createFormedNavigation(Level level) {
        return new FlyingPathNavigation(this, level) {

            @Override
            public boolean isStableDestination(BlockPos pos) {
                Vec3 vec3 = Vec3.atBottomCenterOf(pos.atY((int) MagnetronEntity.this.m_20186_()));
                return ACMath.getGroundBelowPosition(this.f_26495_, vec3).distanceTo(vec3) < 6.0;
            }
        };
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(BLOCKSTATES, new CompoundTag());
        this.f_19804_.define(BLOCK_POSES, new CompoundTag());
        this.f_19804_.define(FORMED, false);
        this.f_19804_.define(ATTACK_POSE, 0);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 2.0).add(Attributes.ARMOR, 6.0).add(Attributes.MOVEMENT_SPEED, 0.2).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.MAX_HEALTH, 30.0);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new MagnetronEntity.MeleeGoal());
        this.f_21345_.addGoal(2, new RandomStrollGoal(this, 1.0, 45));
        this.f_21345_.addGoal(3, new LookAtPlayerGoal(this, Player.class, 15.0F));
        this.f_21345_.addGoal(3, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true, false));
    }

    public void removeParts() {
        if (this.allParts != null) {
            for (PartEntity part : this.allParts) {
                part.m_142687_(Entity.RemovalReason.KILLED);
            }
        }
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public void buildParts() {
        int i = 0;
        for (MagnetronJoint joint : MagnetronJoint.values()) {
            this.allParts[i] = new MagnetronPartEntity(this, joint, false);
            this.allParts[i + 1] = new MagnetronPartEntity(this, joint, true);
            i += 2;
        }
    }

    public boolean isFunctionallyMultipart() {
        return this.isFormed() && !this.m_213877_();
    }

    public boolean isMultipartEntity() {
        return true;
    }

    public PartEntity<?>[] getParts() {
        return this.allParts;
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevWheelRot = this.wheelRot;
        this.prevWheelYaw = this.wheelYaw;
        this.prevRollLeanProgress = this.rollLeanProgress;
        this.prevFormProgress = this.formProgress;
        this.prevAttackPoseProgress = this.attackPoseProgress;
        boolean wheelSpinning = false;
        double speed = this.m_20184_().horizontalDistance();
        if (speed > 0.01) {
            if (this.isFormed()) {
                float f = (float) Math.cos((double) (this.f_267362_.position() * 0.4F - 1.5F));
                if (this.f_267362_.speed() > 0.2F && Math.abs(f) < 0.2F && this.movingSoundTimer == 0) {
                    this.movingSoundTimer = 5;
                    this.m_216990_(ACSoundRegistry.MAGNETRON_STEP.get());
                }
                if (this.movingSoundTimer > 0) {
                    this.movingSoundTimer--;
                }
            } else {
                wheelSpinning = true;
                this.wheelRot = (float) ((double) this.wheelRot + Math.max(speed * 10.0, 1.0) * 15.0);
                if (this.movingSoundTimer++ > 20) {
                    this.m_216990_(ACSoundRegistry.MAGNETRON_ROLL.get());
                    this.movingSoundTimer = 0;
                }
            }
        }
        if (!wheelSpinning && Mth.wrapDegrees(this.wheelRot) != 0.0F) {
            this.wheelRot = Mth.approachDegrees(this.wheelRot, 0.0F, 15.0F);
        }
        if (!this.m_9236_().isClientSide && !this.isFormed()) {
            LivingEntity target = this.m_5448_();
            if (target instanceof Player && target.isAlive() && this.m_20270_(target) < 8.0F) {
                this.startForming();
            }
        }
        if (wheelSpinning || this.isFormed()) {
            this.wheelYaw = Mth.approachDegrees(this.wheelYaw, this.f_20883_, 15.0F);
        }
        MagnetronEntity.AttackPose attackPose = this.getAttackPose();
        if (this.prevAttackPose != attackPose) {
            if (this.attackPoseProgress < 10.0F) {
                this.attackPoseProgress++;
            } else if (this.attackPoseProgress >= 10.0F) {
                if (attackPose == MagnetronEntity.AttackPose.SLAM) {
                    this.spawnGroundEffects();
                }
                this.prevAttackPose = attackPose;
            }
        } else {
            this.attackPoseProgress = 10.0F;
        }
        if (this.isFormed() && this.formProgress < 40.0F) {
            if (this.formProgress == 0.0F) {
                this.m_216990_(ACSoundRegistry.MAGNETRON_ASSEMBLE.get());
            }
            this.formProgress++;
        }
        if (!this.isFormed() && this.formProgress > 0.0F) {
            this.formProgress = 0.0F;
        }
        if (wheelSpinning && this.rollLeanProgress < 5.0F) {
            this.rollLeanProgress++;
        }
        if (!wheelSpinning && this.rollLeanProgress > 0.0F) {
            this.rollLeanProgress--;
        }
        if (!this.m_9236_().isClientSide) {
            if (this.isFormed() && this.isLandNavigator) {
                this.switchMoveController(false);
            }
            if (!this.isFormed() && !this.isLandNavigator) {
                this.switchMoveController(true);
            }
        }
        this.tickMultipart();
        if (this.m_9236_().isClientSide) {
            for (int i = 0; i < this.lightningAnimOffsets.length; i++) {
                this.lightningAnimOffsets[i] = new Vec3((double) (this.f_19796_.nextFloat() - 0.5F), (double) (this.f_19796_.nextFloat() - 0.5F), (double) (this.f_19796_.nextFloat() - 0.5F)).scale(0.3F);
            }
        }
        if (this.syncCooldown > 0) {
            this.syncCooldown--;
        } else {
            this.syncCooldown = 200;
        }
        if (!this.m_6084_() && this.shouldDropBlocks()) {
            if (this.isFormed()) {
                for (MagnetronPartEntity part : this.allParts) {
                    if (part.getBlockState() != null && this.m_9236_().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                        BlockPos placeAt = part.m_20183_();
                        while (!this.m_9236_().getBlockState(placeAt).m_60795_() && placeAt.m_123342_() < this.m_9236_().m_151558_()) {
                            placeAt = placeAt.above();
                        }
                        FallingBlockEntity.fall(this.m_9236_(), placeAt, part.getBlockState());
                        part.setBlockState(null);
                    }
                }
            }
            if (!this.droppedHeart) {
                this.droppedHeart = true;
                FallingBlockEntity.fall(this.m_9236_(), this.m_20183_(), (BlockState) ACBlockRegistry.HEART_OF_IRON.get().defaultBlockState().m_61124_(HeartOfIronBlock.f_55923_, this.m_6350_().getAxis()));
            }
            this.removeParts();
        }
        if (this.isFormed() && !this.hasFormedAttributes) {
            this.hasFormedAttributes = true;
            this.m_6210_();
            this.m_21051_(Attributes.MAX_HEALTH).setBaseValue(80.0);
            this.m_21051_(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0);
            this.m_5634_(80.0F);
        }
        if (!this.isFormed() && !this.m_6162_() && this.hasFormedAttributes) {
            this.hasFormedAttributes = false;
            this.m_6210_();
            this.m_21051_(Attributes.MAX_HEALTH).setBaseValue(30.0);
            this.m_21051_(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0.0);
            this.m_5634_(30.0F);
        }
    }

    private boolean shouldDropBlocks() {
        DamageSource lastDamageSource = this.m_21225_();
        return lastDamageSource == null ? false : lastDamageSource.getEntity() != null || lastDamageSource.getDirectEntity() != null || this.f_20888_ != null;
    }

    private List<BlockPos> findBlocksForTransformation() {
        List<BlockPos> all = new ArrayList();
        List<BlockPos> weapons = this.findBlocksMatching(state -> state.m_204336_(ACTagRegistry.MAGNETRON_WEAPONS), pos -> false, 2, 1.0F);
        List<BlockPos> magnetic = this.findBlocksMatching(state -> state.m_204336_(ACTagRegistry.MAGNETIC_BLOCKS), weapons::contains, BLOCK_COUNT - 2, 1.0F);
        all.addAll(weapons);
        all.addAll(magnetic);
        if (weapons.size() + magnetic.size() < BLOCK_COUNT) {
            List<BlockPos> everything = this.findBlocksMatching(state -> !state.m_60795_() && !state.m_204336_(ACTagRegistry.RESISTS_MAGNETRON_BODY_BUILDING), all::contains, BLOCK_COUNT - weapons.size() - magnetic.size(), 0.3F);
            all.addAll(everything);
        }
        return all;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.setFormed(compound.getBoolean("Formed"));
        if (compound.contains("Blockstates", 10)) {
            this.f_19804_.set(BLOCKSTATES, compound.getCompound("Blockstates"));
        }
        if (compound.contains("BlockPoses", 10)) {
            this.f_19804_.set(BLOCK_POSES, compound.getCompound("BlockPoses"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putBoolean("Formed", this.isFormed());
        compound.put("Blockstates", this.f_19804_.get(BLOCKSTATES));
        compound.put("BlockPoses", this.f_19804_.get(BLOCK_POSES));
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        boolean prev = super.m_6469_(damageSource, f);
        if (prev && damageSource.getEntity() instanceof Player player && !player.isCreative() && !this.isFormed()) {
            this.startForming();
        }
        return prev;
    }

    private List<BlockPos> findBlocksMatching(Predicate<BlockState> blockMatch, Predicate<BlockPos> ignoreMatch, int maxCount, float rngDiscard) {
        List<BlockPos> list = new ArrayList();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int searchY = 4; searchY > -4; searchY--) {
            for (int searchWidth = 0; searchWidth < 15; searchWidth++) {
                for (int searchX = 0; searchX <= searchWidth; searchX = searchX > 0 ? -searchX : 1 - searchX) {
                    for (int searchZ = searchX < searchWidth && searchX > -searchWidth ? searchWidth : 0; searchZ <= searchWidth; searchZ = searchZ > 0 ? -searchZ : 1 - searchZ) {
                        mutableBlockPos.setWithOffset(this.m_20183_(), searchX, searchY - 1, searchZ);
                        if (blockMatch.test(this.m_9236_().getBlockState(mutableBlockPos)) && !list.contains(mutableBlockPos.immutable()) && !ignoreMatch.test(mutableBlockPos.immutable()) && this.f_19796_.nextFloat() < rngDiscard) {
                            list.add(mutableBlockPos.immutable());
                            if (list.size() > maxCount) {
                                return list;
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    private void tickMultipart() {
        Vec3[] avector3d = new Vec3[this.allParts.length];
        double minimumYPart = Double.MAX_VALUE;
        int lowestPartIndex = 0;
        for (int j = 0; j < this.allParts.length; j++) {
            double y = this.allParts[j].m_20186_();
            avector3d[j] = new Vec3(this.allParts[j].m_20185_(), y, this.allParts[j].m_20189_());
            if (y < minimumYPart) {
                minimumYPart = y;
                lowestPartIndex = j;
            }
            if (this.isFunctionallyMultipart()) {
                this.allParts[j].positionMultipart(this);
            } else {
                this.allParts[j].m_20359_(this);
            }
        }
        if (this.isFunctionallyMultipart()) {
            double idealDistance = this.m_20182_().y - this.allParts[lowestPartIndex].getLowPoint();
            Vec3 bottom = new Vec3(this.m_20185_(), this.m_20191_().minY, this.m_20189_());
            Vec3 ground = ACMath.getGroundBelowPosition(this.m_9236_(), new Vec3(this.m_20185_(), this.m_20191_().maxY, this.m_20189_()));
            Vec3 aboveGround = ground.add(0.0, idealDistance, 0.0);
            Vec3 diff = aboveGround.subtract(bottom);
            this.gravityFlag = true;
            if (this.m_6084_() && bottom.distanceTo(ground) < 7.0 && ground.y > (double) this.m_9236_().m_141937_()) {
                if (diff.length() > 1.0) {
                    diff = diff.normalize();
                }
                Vec3 delta = new Vec3(this.m_20184_().x * 0.98, this.m_20184_().y * 0.7F + diff.y * 0.25, this.m_20184_().z * 0.98);
                if (this.getAttackPose() != MagnetronEntity.AttackPose.NONE) {
                    delta = new Vec3(0.0, delta.y, 0.0);
                }
                this.m_20256_(delta);
            } else {
                this.m_20256_(this.m_20184_().add(0.0, -0.08, 0.0));
            }
        }
        for (int l = 0; l < this.allParts.length; l++) {
            this.allParts[l].f_19854_ = avector3d[l].x;
            this.allParts[l].f_19855_ = avector3d[l].y;
            this.allParts[l].f_19856_ = avector3d[l].z;
            this.allParts[l].f_19790_ = avector3d[l].x;
            this.allParts[l].f_19791_ = avector3d[l].y;
            this.allParts[l].f_19792_ = avector3d[l].z;
        }
    }

    public boolean isFormed() {
        return this.f_19804_.get(FORMED);
    }

    public void setFormed(boolean formed) {
        this.f_19804_.set(FORMED, formed);
    }

    private List<BlockState> getAllBlockStates() {
        List<BlockState> list = new ArrayList();
        CompoundTag data = this.f_19804_.get(BLOCKSTATES);
        if (data.contains("BlockData")) {
            ListTag listTag = data.getList("BlockData", 10);
            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag innerTag = listTag.getCompound(i);
                list.add(NbtUtils.readBlockState(this.m_9236_().m_246945_(Registries.BLOCK), innerTag));
            }
        }
        return list;
    }

    private void setAllBlockStates(List<BlockState> list) {
        CompoundTag tag = new CompoundTag();
        ListTag listTag = new ListTag();
        for (BlockState state : list) {
            listTag.add(NbtUtils.writeBlockState(state));
        }
        tag.put("BlockData", listTag);
        this.f_19804_.set(BLOCKSTATES, tag);
    }

    private List<BlockPos> getAllBlockPos() {
        List<BlockPos> list = new ArrayList();
        CompoundTag data = this.f_19804_.get(BLOCK_POSES);
        if (data.contains("BlockPos")) {
            ListTag listTag = data.getList("BlockPos", 10);
            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag innerTag = listTag.getCompound(i);
                list.add(NbtUtils.readBlockPos(innerTag));
            }
        }
        return list;
    }

    private void setAllBlockPos(List<BlockPos> list) {
        CompoundTag tag = new CompoundTag();
        ListTag listTag = new ListTag();
        for (BlockPos pos : list) {
            listTag.add(NbtUtils.writeBlockPos(pos));
        }
        tag.put("BlockPos", listTag);
        this.f_19804_.set(BLOCK_POSES, tag);
    }

    public float getRollPosition(float partialTicks) {
        return this.prevWheelRot + (this.wheelRot - this.prevWheelRot) * partialTicks;
    }

    public float getWheelYaw(float partialTicks) {
        return this.prevWheelYaw + (this.wheelYaw - this.prevWheelYaw) * partialTicks;
    }

    public float getRollLeanProgress(float partialTicks) {
        return (this.prevRollLeanProgress + (this.rollLeanProgress - this.prevRollLeanProgress) * partialTicks) / 5.0F;
    }

    public MagnetronEntity.AttackPose getAttackPose() {
        return MagnetronEntity.AttackPose.get(this.f_19804_.get(ATTACK_POSE));
    }

    public void setAttackPose(MagnetronEntity.AttackPose animation) {
        this.f_19804_.set(ATTACK_POSE, animation.ordinal());
    }

    public MagnetronEntity.AttackPose getPrevAttackPose() {
        return this.prevAttackPose;
    }

    public float getAttackPoseProgress(float partialTick) {
        return (this.prevAttackPoseProgress + (this.attackPoseProgress - this.prevAttackPoseProgress) * partialTick) / 10.0F;
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return this.isFormed() ? this.m_20191_().inflate(3.5, 5.0, 3.5) : this.m_20191_();
    }

    private void startForming() {
        if (!this.m_9236_().isClientSide) {
            List<BlockPos> poses = this.findBlocksForTransformation();
            if (poses.size() >= BLOCK_COUNT) {
                MagnetronPartEntity rightHand = this.allParts[0];
                MagnetronPartEntity leftHand = this.allParts[0];
                List<MagnetronPartEntity> needsABlock = new ArrayList();
                for (MagnetronPartEntity entity : this.allParts) {
                    if (entity.getJoint() == MagnetronJoint.HAND) {
                        if (entity.left) {
                            leftHand = entity;
                        } else {
                            rightHand = entity;
                        }
                    } else {
                        needsABlock.add(entity);
                    }
                }
                rightHand.setStartsAt((BlockPos) poses.get(0));
                leftHand.setStartsAt((BlockPos) poses.get(1));
                for (int i = 2; i < BLOCK_COUNT; i++) {
                    ((MagnetronPartEntity) needsABlock.get(i - 2)).setStartsAt((BlockPos) poses.get(i));
                }
            }
            this.setFormed(true);
            this.setAllBlockPos(poses);
            List<BlockState> saved = new ArrayList();
            for (MagnetronPartEntity entityx : this.allParts) {
                if (entityx.getBlockState() == null && entityx.getStartPosition() != null) {
                    BlockState state = this.m_9236_().getBlockState(entityx.getStartPosition());
                    saved.add(state);
                    if (this.m_9236_().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && !this.m_9236_().isClientSide) {
                        this.m_9236_().m_46961_(entityx.getStartPosition(), false);
                    }
                }
            }
            this.setAllBlockStates(saved);
        }
    }

    @Override
    public boolean isNoGravity() {
        return this.isFormed() && this.gravityFlag && this.m_6084_() || super.m_20068_();
    }

    public float getFormProgress(float partialTicks) {
        return (this.prevFormProgress + (this.formProgress - this.prevFormProgress) * partialTicks) / 40.0F;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor) {
        super.m_7350_(entityDataAccessor);
        if (BLOCKSTATES.equals(entityDataAccessor)) {
            this.syncBlockStatesWithMultipart();
        }
        if (BLOCK_POSES.equals(entityDataAccessor)) {
            this.syncBlockPosesWithMultipart();
        }
        if (ATTACK_POSE.equals(entityDataAccessor)) {
            this.prevAttackPoseProgress = 0.0F;
            this.attackPoseProgress = 0.0F;
        }
    }

    private void syncBlockPosesWithMultipart() {
        List<BlockPos> poses = this.getAllBlockPos();
        if (poses.size() >= BLOCK_COUNT) {
            MagnetronPartEntity rightHand = this.allParts[0];
            MagnetronPartEntity leftHand = this.allParts[0];
            List<MagnetronPartEntity> needsABlock = new ArrayList();
            for (MagnetronPartEntity entity : this.allParts) {
                if (entity.getJoint() == MagnetronJoint.HAND) {
                    if (entity.left) {
                        leftHand = entity;
                    } else {
                        rightHand = entity;
                    }
                } else {
                    needsABlock.add(entity);
                }
            }
            rightHand.setStartsAt((BlockPos) poses.get(0));
            leftHand.setStartsAt((BlockPos) poses.get(1));
            for (int i = 2; i < BLOCK_COUNT; i++) {
                ((MagnetronPartEntity) needsABlock.get(i - 2)).setStartsAt((BlockPos) poses.get(i));
            }
        }
    }

    private void syncBlockStatesWithMultipart() {
        List<BlockState> listStates = this.getAllBlockStates();
        if (listStates.size() >= this.allParts.length) {
            for (int i = 0; i < this.allParts.length; i++) {
                this.allParts[i].setBlockState((BlockState) listStates.get(i));
            }
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return 0.55F * dimensions.height;
    }

    public void spawnGroundEffects() {
        float radius = 2.0F;
        for (int i = 0; i < 4; i++) {
            for (int i1 = 0; i1 < 20 + this.f_19796_.nextInt(12); i1++) {
                double motionX = this.m_217043_().nextGaussian() * 0.07;
                double motionY = this.m_217043_().nextGaussian() * 0.07;
                double motionZ = this.m_217043_().nextGaussian() * 0.07;
                float angle = (float) (Math.PI / 180.0) * this.f_20883_ + (float) i1;
                double extraX = (double) (radius * Mth.sin((float) (Math.PI + (double) angle)));
                double extraY = 0.8F;
                double extraZ = (double) (radius * Mth.cos(angle));
                Vec3 center = this.m_20182_().add(new Vec3(0.0, 0.0, 2.0).yRot((float) Math.toRadians((double) (-this.f_20883_))));
                BlockPos ground = BlockPos.containing(ACMath.getGroundBelowPosition(this.m_9236_(), new Vec3((double) Mth.floor(center.x + extraX), (double) (Mth.floor(center.y + extraY) - 1), (double) Mth.floor(center.z + extraZ))));
                BlockState state = this.m_9236_().getBlockState(ground);
                if (state.m_280296_() && this.m_9236_().isClientSide) {
                    this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), true, center.x + extraX, (double) ground.m_123342_() + extraY, center.z + extraZ, motionX, motionY, motionZ);
                }
            }
        }
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effectInstance) {
        return super.m_7301_(effectInstance) && effectInstance.getEffect() != ACEffectRegistry.MAGNETIZING.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ACSoundRegistry.MAGNETRON_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ACSoundRegistry.MAGNETRON_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSoundRegistry.MAGNETRON_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    public static enum AttackPose {

        NONE, RIGHT_PUNCH, LEFT_PUNCH, SLAM;

        public static MagnetronEntity.AttackPose get(int i) {
            return i <= 0 ? NONE : values()[Math.min(values().length - 1, i)];
        }

        public boolean isRotatedJoint(MagnetronJoint joint, boolean left) {
            if (joint == MagnetronJoint.HAND) {
                if (this == LEFT_PUNCH) {
                    return left;
                } else {
                    return this == RIGHT_PUNCH ? !left : this == SLAM;
                }
            } else {
                return false;
            }
        }
    }

    private class MeleeGoal extends Goal {

        private int punchCooldown = 0;

        public MeleeGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = MagnetronEntity.this.m_5448_();
            return target != null && target.isAlive();
        }

        @Override
        public void start() {
            this.punchCooldown = 0;
        }

        @Override
        public void stop() {
            if (MagnetronEntity.this.getAttackPose() != MagnetronEntity.AttackPose.NONE) {
                MagnetronEntity.this.setAttackPose(MagnetronEntity.AttackPose.NONE);
            }
        }

        @Override
        public void tick() {
            LivingEntity target = MagnetronEntity.this.m_5448_();
            if (this.punchCooldown > 0) {
                this.punchCooldown--;
            }
            if (target != null) {
                double xzDist = MagnetronEntity.this.m_20275_(target.m_20185_(), MagnetronEntity.this.m_20186_(), target.m_20189_());
                double yDist = Math.abs(MagnetronEntity.this.m_20186_() - target.m_20186_());
                float trueDist = (float) Math.max(yDist * 0.75, Math.sqrt((double) ((float) xzDist)));
                if (xzDist < 10.0 && !MagnetronEntity.this.isFormed()) {
                    MagnetronEntity.this.startForming();
                }
                if (MagnetronEntity.this.isFormed()) {
                    float progress = MagnetronEntity.this.getFormProgress(1.0F);
                    if (progress >= 1.0F) {
                        MagnetronEntity.this.m_21573_().moveTo(target, 1.0);
                        if (MagnetronEntity.this.getAttackPose() == MagnetronEntity.AttackPose.NONE && MagnetronEntity.this.getAttackPoseProgress(1.0F) >= 1.0F) {
                            if (trueDist < 5.0F && this.punchCooldown <= 0) {
                                MagnetronEntity.AttackPose set = this.getPoseForHand();
                                MagnetronEntity.this.m_216990_(ACSoundRegistry.MAGNETRON_ATTACK.get());
                                MagnetronEntity.this.setAttackPose(set);
                                this.punchCooldown = set == MagnetronEntity.AttackPose.SLAM ? 15 : 10;
                            }
                        } else if (trueDist < 7.5F && MagnetronEntity.this.getAttackPoseProgress(1.0F) >= 0.9F) {
                            this.dealDamage(target, MagnetronEntity.this.getAttackPose());
                        }
                    }
                }
                MagnetronEntity.this.m_21573_().moveTo(target, 1.0);
                Vec3 lookDist = target.m_146892_().subtract(MagnetronEntity.this.m_146892_());
                float targetXRot = (float) (-(Mth.atan2(lookDist.y, lookDist.horizontalDistance()) * 180.0F / (float) Math.PI));
                float targetYRot = (float) (-Mth.atan2(lookDist.x, lookDist.z) * 180.0F / (float) Math.PI);
                MagnetronEntity.this.m_146926_(targetXRot);
                MagnetronEntity.this.m_146922_(targetYRot);
            }
            if (MagnetronEntity.this.getAttackPose() != MagnetronEntity.AttackPose.NONE && this.punchCooldown == 0) {
                MagnetronEntity.this.setAttackPose(MagnetronEntity.AttackPose.NONE);
            }
        }

        private int getHandDamageValueAdd(boolean left) {
            BlockState state = this.getStateForHand(left);
            if (state.m_204336_(BlockTags.ANVIL)) {
                return 6;
            } else if (state.m_60713_(Blocks.STONECUTTER)) {
                return 4;
            } else {
                return state.m_204336_(ACTagRegistry.MAGNETRON_WEAPONS) ? 2 : 0;
            }
        }

        private void dealDamage(LivingEntity target, MagnetronEntity.AttackPose attackPose) {
            int leftDmg = this.getHandDamageValueAdd(true);
            int rightDmg = this.getHandDamageValueAdd(true);
            if (attackPose == MagnetronEntity.AttackPose.SLAM) {
                AABB bashBox = new AABB(-5.0, -1.0, -5.0, 5.0, 2.0, 5.0);
                Vec3 ground = ACMath.getGroundBelowPosition(MagnetronEntity.this.m_9236_(), MagnetronEntity.this.m_20182_());
                bashBox = bashBox.move(ground.add(new Vec3(0.0, 0.0, 2.0).yRot((float) Math.toRadians((double) (-MagnetronEntity.this.f_20883_)))));
                for (Entity entity : MagnetronEntity.this.m_9236_().m_45976_(LivingEntity.class, bashBox)) {
                    if (!MagnetronEntity.this.m_7307_(entity) && !(entity instanceof MagnetronEntity)) {
                        entity.hurt(MagnetronEntity.this.m_269291_().mobAttack(MagnetronEntity.this), (float) (2 + leftDmg + rightDmg));
                        this.launch(entity, true);
                    }
                }
            } else if (attackPose == MagnetronEntity.AttackPose.LEFT_PUNCH) {
                BlockState state = this.getStateForHand(true);
                boolean magnet = state.m_60713_(ACBlockRegistry.AZURE_MAGNET.get()) || state.m_60713_(ACBlockRegistry.SCARLET_MAGNET.get());
                target.hurt(MagnetronEntity.this.m_269291_().mobAttack(MagnetronEntity.this), (float) (2 + leftDmg));
                this.launch(target, magnet);
            } else if (attackPose == MagnetronEntity.AttackPose.RIGHT_PUNCH) {
                BlockState state = this.getStateForHand(false);
                boolean magnet = state.m_60713_(ACBlockRegistry.AZURE_MAGNET.get()) || state.m_60713_(ACBlockRegistry.SCARLET_MAGNET.get());
                target.hurt(MagnetronEntity.this.m_269291_().mobAttack(MagnetronEntity.this), (float) (2 + rightDmg));
                this.launch(target, magnet);
            }
        }

        private void launch(Entity e, boolean huge) {
            if (e.onGround()) {
                double d0 = e.getX() - MagnetronEntity.this.m_20185_();
                double d1 = e.getZ() - MagnetronEntity.this.m_20189_();
                double d2 = Math.max(d0 * d0 + d1 * d1, 0.001);
                float f = huge ? 1.0F : 0.5F;
                e.push(d0 / d2 * (double) f, huge ? 0.5 : 0.2F, d1 / d2 * (double) f);
            }
        }

        private BlockState getStateForHand(boolean left) {
            if (MagnetronEntity.this.allParts != null) {
                for (MagnetronPartEntity part : MagnetronEntity.this.allParts) {
                    if (part.getJoint() == MagnetronJoint.HAND && part.left == left && part.getBlockState() != null) {
                        return part.getBlockState();
                    }
                }
            }
            return Blocks.IRON_BLOCK.defaultBlockState();
        }

        private MagnetronEntity.AttackPose getPoseForHand() {
            RandomSource rand = MagnetronEntity.this.m_217043_();
            int leftDmg = this.getHandDamageValueAdd(true);
            int rightDmg = this.getHandDamageValueAdd(true);
            boolean dual = rightDmg != 0 && leftDmg != 0;
            float overrideSlamChance = dual ? 0.4F : 0.6F;
            if (rand.nextFloat() < overrideSlamChance) {
                if (dual || rightDmg == 0 && leftDmg == 0) {
                    return rand.nextBoolean() ? MagnetronEntity.AttackPose.LEFT_PUNCH : MagnetronEntity.AttackPose.RIGHT_PUNCH;
                }
                if (rightDmg != 0) {
                    return MagnetronEntity.AttackPose.RIGHT_PUNCH;
                }
                if (leftDmg != 0) {
                    return MagnetronEntity.AttackPose.LEFT_PUNCH;
                }
            }
            return MagnetronEntity.AttackPose.SLAM;
        }
    }

    class MoveController extends MoveControl {

        private final Mob parentEntity = MagnetronEntity.this;

        public MoveController() {
            super(MagnetronEntity.this);
        }

        @Override
        public void tick() {
            int maxTurn = 15;
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                this.f_24981_ = MoveControl.Operation.WAIT;
                this.f_24974_.m_20242_(true);
                double d0 = this.f_24975_ - this.f_24974_.m_20185_();
                double d1 = this.f_24976_ - this.f_24974_.m_20186_();
                double d2 = this.f_24977_ - this.f_24974_.m_20189_();
                double d3 = d0 * d0 + d2 * d2;
                if (d3 < 0.01F) {
                    this.f_24974_.setZza(0.0F);
                    return;
                }
                if (d3 > (double) this.f_24974_.m_20205_()) {
                    float f = (float) (Mth.atan2(d2, d0) * 180.0F / (float) Math.PI) - 90.0F;
                    this.f_24974_.m_146922_(this.m_24991_(this.f_24974_.m_146908_(), f, (float) maxTurn));
                }
                float f1 = (float) (this.f_24978_ * this.f_24974_.m_21133_(Attributes.MOVEMENT_SPEED)) * 3.0F;
                this.f_24974_.setSpeed(f1);
                double d4 = Math.sqrt(d0 * d0 + d2 * d2);
                if (Math.abs(d1) > 1.0E-5F || Math.abs(d4) > 1.0E-5F) {
                    float f2 = (float) (-(Mth.atan2(d1, d4) * 180.0F / (float) Math.PI));
                    this.f_24974_.m_146926_(this.m_24991_(this.f_24974_.m_146909_(), f2, (float) maxTurn));
                }
            } else {
                this.f_24974_.setYya(0.0F);
                this.f_24974_.setZza(0.0F);
            }
        }
    }
}