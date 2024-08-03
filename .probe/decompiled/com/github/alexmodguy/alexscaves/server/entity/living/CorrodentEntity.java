package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.entity.ai.CorrodentAttackGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.CorrodentDigRandomlyGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.CorrodentFearLightGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.MobTarget3DGoal;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.server.entity.collision.ICustomCollisions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.FlyNodeEvaluator;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.entity.PartEntity;

public class CorrodentEntity extends Monster implements ICustomCollisions, IAnimatedEntity {

    public static final int LIGHT_THRESHOLD = 7;

    private static final EntityDataAccessor<Boolean> DIGGING = SynchedEntityData.defineId(CorrodentEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> AFRAID = SynchedEntityData.defineId(CorrodentEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Float> DIG_PITCH = SynchedEntityData.defineId(CorrodentEntity.class, EntityDataSerializers.FLOAT);

    public static final Animation ANIMATION_BITE = Animation.create(15);

    public final CorrodentTailEntity tailPart;

    public final CorrodentTailEntity[] allParts;

    private float[][] trailTransformations = new float[64][2];

    protected boolean isLandNavigator;

    private int trailPointer = -1;

    private float prevDigPitch = 0.0F;

    private float fakeYRot = 0.0F;

    private float fearProgress;

    private float prevFearProgress;

    private float digProgress;

    private float prevDigProgress;

    public int timeDigging = 0;

    public int fleeLightFor = 0;

    private Vec3 surfacePosition;

    private Vec3 prevSurfacePosition;

    private Animation currentAnimation;

    private int animationTick;

    public CorrodentEntity(EntityType type, Level level) {
        super(type, level);
        this.switchNavigator(true);
        this.tailPart = new CorrodentTailEntity(this);
        this.allParts = new CorrodentTailEntity[] { this.tailPart };
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DIGGING, false);
        this.f_19804_.define(AFRAID, false);
        this.f_19804_.define(DIG_PITCH, 0.0F);
    }

    protected void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = this.m_6037_(this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new CorrodentEntity.DiggingMoveControl();
            this.f_21344_ = new CorrodentEntity.Navigator(this, this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new CorrodentFearLightGoal(this));
        this.f_21345_.addGoal(2, new CorrodentAttackGoal(this));
        this.f_21345_.addGoal(2, new CorrodentDigRandomlyGoal(this));
        this.f_21345_.addGoal(7, new RandomStrollGoal(this, 1.0, 20));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Player.class, 15.0F));
        this.f_21345_.addGoal(9, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new MobTarget3DGoal(this, Player.class, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.MAX_HEALTH, 16.0).add(Attributes.ARMOR, 2.0).add(Attributes.ATTACK_DAMAGE, 3.0);
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return this.m_20191_().inflate(1.0, 1.0, 1.0);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevDigPitch = this.getDigPitch();
        this.prevDigProgress = this.digProgress;
        this.prevFearProgress = this.fearProgress;
        this.f_20883_ = Mth.approachDegrees(this.f_20884_, this.f_20883_, 10.0F);
        if (!this.isDigging() || !this.isMoving()) {
            this.setDigPitch(Mth.approachDegrees(this.getDigPitch(), 0.0F, 10.0F));
        }
        this.tickMultipart();
        if (this.isDigging() && this.digProgress < 5.0F) {
            this.digProgress++;
        }
        if (!this.isDigging() && this.digProgress > 0.0F) {
            if (this.digProgress == 5.0F) {
                this.m_216990_(ACSoundRegistry.CORRODENT_DIG_STOP.get());
            }
            this.digProgress--;
        }
        if (this.isAfraid() && this.fearProgress < 5.0F) {
            this.fearProgress++;
        }
        if (!this.isAfraid() && this.fearProgress > 0.0F) {
            this.fearProgress--;
        }
        if (!this.m_9236_().isClientSide) {
            if (this.isDigging()) {
                this.timeDigging++;
                if (this.isLandNavigator) {
                    this.switchNavigator(false);
                    this.m_9236_().broadcastEntityEvent(this, (byte) 77);
                    this.setDigPitch(90.0F);
                }
                if (this.timeDigging > 40 && !this.m_5830_()) {
                    this.setDigging(false);
                    this.m_146884_(this.m_20182_().add(0.0, 1.0, 0.0));
                    this.m_20256_(this.m_20184_().add(0.0, 0.35, 0.0));
                }
                if (!isSafeDig(this.m_9236_(), this.m_20183_())) {
                    if (canDigBlock(this.m_9236_().getBlockState(this.m_20183_().above()))) {
                        this.m_20256_(this.m_20184_().add(0.0, 0.1, 0.0));
                    }
                    if (canDigBlock(this.m_9236_().getBlockState(this.m_20183_().below()))) {
                        this.m_20256_(this.m_20184_().add(0.0, -0.08, 0.0));
                    }
                }
                this.m_20242_(this.m_5830_());
            } else {
                this.timeDigging = 0;
                if (!this.isLandNavigator) {
                    this.switchNavigator(true);
                    this.m_9236_().broadcastEntityEvent(this, (byte) 77);
                    this.setDigPitch(-90.0F);
                }
                this.m_20242_(false);
            }
        } else if (this.isDigging() && this.m_6084_()) {
            AlexsCaves.PROXY.playWorldSound(this, (byte) 6);
        }
        this.prevSurfacePosition = this.surfacePosition;
        if (this.isMoving() || this.surfacePosition == null) {
            this.surfacePosition = this.calculateLightAbovePosition();
        }
        if (this.isDigging() && this.surfacePosition != null && this.m_9236_().isClientSide && this.isMoving()) {
            BlockState surfaceState = this.m_9236_().getBlockState(BlockPos.containing(this.surfacePosition).below());
            BlockState onState = this.m_146900_();
            if (surfaceState.m_280296_()) {
                Vec3 head = new Vec3(0.0, 0.0, 0.7F).yRot(-this.f_20883_ * (float) (Math.PI / 180.0)).add(this.m_20185_(), this.surfacePosition.y, this.m_20189_());
                this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, surfaceState), true, head.x, head.y, head.z, (double) (this.f_19796_.nextFloat() - 0.5F), (double) (this.f_19796_.nextFloat() + 0.5F), (double) (this.f_19796_.nextFloat() - 0.5F));
                for (int i = 0; i < 4 + this.f_19796_.nextInt(4); i++) {
                    float j = (float) Math.pow((double) i, 0.75);
                    Vec3 offset = new Vec3(i % 2 == 0 ? (double) (-j * 0.2F) : (double) (j * 0.2F), 0.0, (double) (-0.3F * (float) i)).yRot(-this.f_20883_ * (float) (Math.PI / 180.0));
                    this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, surfaceState), true, offset.x + head.x, offset.y + head.y, offset.z + head.z, (double) ((this.f_19796_.nextFloat() - 0.5F) * 0.2F) + offset.x, (double) ((this.f_19796_.nextFloat() - 0.5F) * 0.2F) + offset.y, (double) ((this.f_19796_.nextFloat() - 0.5F) * 0.2F) + offset.z);
                }
            }
            if (onState.m_280296_()) {
                for (int i = 0; i < 2 + this.f_19796_.nextInt(2); i++) {
                    this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, onState), true, this.m_20208_(0.8F), this.m_20187_(), this.m_20262_(0.8F), (double) ((this.f_19796_.nextFloat() - 0.5F) * 0.2F), (double) ((this.f_19796_.nextFloat() - 0.5F) * 0.2F), (double) ((this.f_19796_.nextFloat() - 0.5F) * 0.2F));
                }
            }
        }
        if (this.fleeLightFor > 0) {
            this.fleeLightFor--;
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b == 77) {
            float radius = 1.0F;
            float particleCount = (float) (20 + this.f_19796_.nextInt(12));
            for (int i1 = 0; (float) i1 < particleCount; i1++) {
                double motionX = (double) (this.m_217043_().nextFloat() - 0.5F) * 0.7;
                double motionY = (double) this.m_217043_().nextFloat() * 0.7 + 0.8F;
                double motionZ = (double) (this.m_217043_().nextFloat() - 0.5F) * 0.7;
                float angle = (float) (Math.PI / 180.0) * (this.f_20883_ + (float) i1 / particleCount * 360.0F);
                double extraX = (double) (radius * Mth.sin((float) (Math.PI + (double) angle)));
                double extraY = 1.2F;
                double extraZ = (double) (radius * Mth.cos(angle));
                BlockPos ground = BlockPos.containing(ACMath.getGroundBelowPosition(this.m_9236_(), new Vec3((double) Mth.floor(this.m_20185_() + extraX), (double) (Mth.floor(this.m_20186_() + extraY) + 2), (double) Mth.floor(this.m_20189_() + extraZ))));
                BlockState groundState = this.m_9236_().getBlockState(ground);
                if (groundState.m_280296_() && this.m_9236_().isClientSide) {
                    this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, groundState), true, this.m_20185_() + extraX, (double) ground.m_123342_() + extraY, this.m_20189_() + extraZ, motionX, motionY, motionZ);
                }
            }
        } else {
            super.m_7822_(b);
        }
    }

    public int getCorrosionAmount(BlockPos pos) {
        double distance = this.m_20275_((double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 0.5F), (double) ((float) pos.m_123343_() + 0.5F));
        if (distance <= 10.0) {
            BlockState state = this.m_9236_().getBlockState(pos);
            if (canDigBlock(state) && !state.m_60795_() && !state.m_247087_()) {
                return 10 - (int) distance;
            }
        }
        return -1;
    }

    public void setDigPitch(float pitch) {
        this.f_19804_.set(DIG_PITCH, pitch);
    }

    public float getDigPitch() {
        return this.f_19804_.get(DIG_PITCH);
    }

    private Vec3 calculateLightAbovePosition() {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        mutableBlockPos.set(this.m_146903_(), this.m_146904_(), this.m_146907_());
        while (mutableBlockPos.m_123342_() < this.m_9236_().m_151558_() && this.m_9236_().getBlockState(mutableBlockPos).m_60828_(this.m_9236_(), mutableBlockPos)) {
            mutableBlockPos.move(0, 1, 0);
        }
        return new Vec3(this.m_20185_(), (double) mutableBlockPos.m_123342_(), this.m_20189_());
    }

    private void tickMultipart() {
        float digPitch = this.getDigPitch();
        if (this.trailPointer == -1) {
            this.fakeYRot = this.f_20883_;
            for (int i = 0; i < this.trailTransformations.length; i++) {
                this.trailTransformations[i][0] = digPitch;
                this.trailTransformations[i][1] = this.fakeYRot;
            }
        }
        this.fakeYRot = Mth.approachDegrees(this.fakeYRot, this.f_20883_, 10.0F);
        if (++this.trailPointer == this.trailTransformations.length) {
            this.trailPointer = 0;
        }
        this.trailTransformations[this.trailPointer][0] = digPitch;
        this.trailTransformations[this.trailPointer][1] = this.fakeYRot;
        Vec3[] avector3d = new Vec3[this.allParts.length];
        for (int j = 0; j < this.allParts.length; j++) {
            avector3d[j] = new Vec3(this.allParts[j].m_20185_(), this.allParts[j].m_20186_(), this.allParts[j].m_20189_());
        }
        this.tailPart.setToTransformation(new Vec3(0.0, 0.0, -1.0), this.getTrailTransformation(5, 0, 1.0F), this.getTrailTransformation(5, 1, 1.0F));
        for (int l = 0; l < this.allParts.length; l++) {
            this.allParts[l].f_19854_ = avector3d[l].x;
            this.allParts[l].f_19855_ = avector3d[l].y;
            this.allParts[l].f_19856_ = avector3d[l].z;
            this.allParts[l].f_19790_ = avector3d[l].x;
            this.allParts[l].f_19791_ = avector3d[l].y;
            this.allParts[l].f_19792_ = avector3d[l].z;
        }
    }

    public boolean isMoving() {
        float f = (float) Mth.length(this.m_20185_() - this.f_19854_, this.m_20186_() - this.f_19855_, this.m_20189_() - this.f_19856_);
        return f > 0.1F;
    }

    public float getDigPitch(float partialTick) {
        return this.prevDigPitch + (this.getDigPitch() - this.prevDigPitch) * partialTick;
    }

    public float getDigAmount(float partialTick) {
        return (this.prevDigProgress + (this.digProgress - this.prevDigProgress) * partialTick) * 0.2F;
    }

    public float getAfraidAmount(float partialTick) {
        return (this.prevFearProgress + (this.fearProgress - this.prevFearProgress) * partialTick) * 0.2F;
    }

    public float getTrailTransformation(int pointer, int index, float partialTick) {
        if (this.m_213877_()) {
            partialTick = 1.0F;
        }
        int i = this.trailPointer - pointer & 63;
        int j = this.trailPointer - pointer - 1 & 63;
        float d0 = this.trailTransformations[j][index];
        float d1 = this.trailTransformations[i][index] - d0;
        return d0 + d1 * partialTick;
    }

    @Override
    public Vec3 collide(Vec3 vec3) {
        return !this.isDigging() ? super.m_20272_(vec3) : ICustomCollisions.getAllowedMovementForEntity(this, vec3);
    }

    @Override
    public void remove(Entity.RemovalReason removalReason) {
        AlexsCaves.PROXY.clearSoundCacheFor(this);
        super.m_142687_(removalReason);
        if (this.allParts != null) {
            for (PartEntity part : this.allParts) {
                part.m_142687_(Entity.RemovalReason.KILLED);
            }
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return super.m_6673_(damageSource) || damageSource.is(DamageTypes.IN_WALL);
    }

    public boolean isDigging() {
        return this.f_19804_.get(DIGGING);
    }

    public void setDigging(boolean bool) {
        this.f_19804_.set(DIGGING, bool);
    }

    public boolean isAfraid() {
        return this.f_19804_.get(AFRAID);
    }

    public void setAfraid(boolean bool) {
        this.f_19804_.set(AFRAID, bool);
    }

    public boolean isMultipartEntity() {
        return true;
    }

    public PartEntity<?>[] getParts() {
        return this.allParts;
    }

    @Override
    public boolean canPassThrough(BlockPos blockPos, BlockState blockState, VoxelShape voxelShape) {
        return this.isDigging() && canDigBlock(blockState);
    }

    @Override
    public boolean isColliding(BlockPos pos, BlockState blockstate) {
        return (!this.isDigging() || canDigBlock(blockstate)) && super.m_20039_(pos, blockstate);
    }

    @Override
    public Vec3 getLightProbePosition(float f) {
        if (this.surfacePosition != null && this.prevSurfacePosition != null) {
            Vec3 difference = this.surfacePosition.subtract(this.prevSurfacePosition);
            return this.prevSurfacePosition.add(difference.scale((double) f)).add(0.0, (double) this.m_20192_(), 0.0);
        } else {
            return super.m_7371_(f);
        }
    }

    public static boolean isSafeDig(BlockGetter level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        BlockState below = level.getBlockState(pos.below());
        return canDigBlock(state) && canDigBlock(below);
    }

    public static boolean canDigBlock(BlockState state) {
        return !state.m_204336_(ACTagRegistry.CORRODENT_BLOCKS_DIGGING) && state.m_60819_().isEmpty() && state.m_60815_();
    }

    @Override
    protected int calculateFallDamage(float f1, float f2) {
        return super.m_5639_(f1, f2) - 5;
    }

    public boolean canReach(BlockPos target) {
        Path path = this.m_21573_().createPath(target, 0);
        if (path == null) {
            return false;
        } else {
            Node node = path.getEndNode();
            if (node == null) {
                return false;
            } else {
                int i = node.x - target.m_123341_();
                int j = node.y - target.m_123342_();
                int k = node.z - target.m_123343_();
                return (double) (i * i + j * j + k * k) <= 3.0;
            }
        }
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
        if (!this.isDigging()) {
            super.m_7840_(y, onGroundIn, state, pos);
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (!this.isDigging()) {
            super.m_7355_(pos, state);
        }
    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        this.animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return this.currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_BITE };
    }

    public static boolean checkCorrodentSpawnRules(EntityType<? extends Monster> entityType, ServerLevelAccessor levelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        return m_219013_(entityType, levelAccessor, mobSpawnType, blockPos, randomSource);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ACSoundRegistry.CORRODENT_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ACSoundRegistry.CORRODENT_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSoundRegistry.CORRODENT_DEATH.get();
    }

    private class DiggingMoveControl extends MoveControl {

        public DiggingMoveControl() {
            super(CorrodentEntity.this);
        }

        @Override
        public void tick() {
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                Vec3 ed = this.f_24974_.getNavigation().getTargetPos().getCenter();
                Vec3 vector3d = new Vec3(this.f_24975_ - this.f_24974_.m_20185_(), this.f_24976_ - this.f_24974_.m_20186_(), this.f_24977_ - this.f_24974_.m_20189_());
                double d0 = vector3d.length();
                double width = this.f_24974_.m_20191_().getSize();
                float burySpeed = CorrodentEntity.this.timeDigging < 40 ? 0.25F : 1.0F;
                Vec3 vector3d1 = vector3d.scale(this.f_24978_ * (double) burySpeed * 0.025 / d0);
                if (CorrodentEntity.isSafeDig(CorrodentEntity.this.m_9236_(), BlockPos.containing(this.f_24975_, this.f_24976_, this.f_24977_))) {
                    this.f_24974_.m_20256_(this.f_24974_.m_20184_().add(vector3d1).scale(0.9F));
                } else {
                    this.f_24974_.m_20256_(this.f_24974_.m_20184_().add(0.0, 0.3, 0.0).scale(0.7F));
                    this.f_24981_ = MoveControl.Operation.WAIT;
                    this.f_24974_.getNavigation().stop();
                }
                if (d0 < width * 0.15F) {
                    this.f_24981_ = MoveControl.Operation.WAIT;
                } else if (d0 >= width) {
                    this.f_24974_.m_146922_(-((float) Mth.atan2(vector3d1.x, vector3d1.z)) * (180.0F / (float) Math.PI));
                    float f2 = (float) (-(Mth.atan2(vector3d1.y, vector3d1.horizontalDistance()) * 180.0F / (float) Math.PI));
                    CorrodentEntity.this.setDigPitch(Mth.approachDegrees(CorrodentEntity.this.getDigPitch(), f2, 10.0F));
                }
            }
        }

        private boolean isWalkable(Vec3 offset) {
            return true;
        }
    }

    static class DiggingNodeEvaluator extends FlyNodeEvaluator {

        @Override
        protected BlockPathTypes evaluateBlockPathType(BlockGetter level, BlockPos pos, BlockPathTypes typeIn) {
            BlockPathTypes def = m_77604_(level, pos.mutable());
            if (def != BlockPathTypes.LAVA && def != BlockPathTypes.OPEN && def != BlockPathTypes.WATER && def != BlockPathTypes.WATER_BORDER && def != BlockPathTypes.DANGER_OTHER && def != BlockPathTypes.DAMAGE_FIRE && def != BlockPathTypes.DANGER_POWDER_SNOW) {
                return CorrodentEntity.isSafeDig(level, pos) && pos.m_123342_() > level.m_141937_() ? BlockPathTypes.WALKABLE : BlockPathTypes.BLOCKED;
            } else {
                return BlockPathTypes.BLOCKED;
            }
        }
    }

    class Navigator extends FlyingPathNavigation {

        public Navigator(Mob mob, Level world) {
            super(mob, world);
        }

        @Override
        public boolean isStableDestination(BlockPos blockPos) {
            return !this.f_26495_.m_46859_(blockPos) && CorrodentEntity.isSafeDig(this.f_26495_, blockPos);
        }

        @Override
        protected PathFinder createPathFinder(int i) {
            this.f_26508_ = new CorrodentEntity.DiggingNodeEvaluator();
            return new PathFinder(this.f_26508_, i);
        }

        @Override
        protected double getGroundY(Vec3 vec3) {
            return vec3.y;
        }

        @Override
        protected boolean canUpdatePath() {
            return true;
        }

        @Override
        protected void followThePath() {
            Vec3 vector3d = this.m_7475_();
            this.f_26505_ = this.f_26494_.m_20205_();
            Vec3i vector3i = this.f_26496_.getNextNodePos();
            double d0 = Math.abs(this.f_26494_.m_20185_() - ((double) vector3i.getX() + 0.5));
            double d1 = Math.abs(this.f_26494_.m_20186_() - (double) vector3i.getY());
            double d2 = Math.abs(this.f_26494_.m_20189_() - ((double) vector3i.getZ() + 0.5));
            boolean flag = d0 < (double) this.f_26505_ && d2 < (double) this.f_26505_ && d1 <= 1.0;
            if (flag || this.m_264193_(this.f_26496_.getNextNode().type) && this.shouldTargetNextNodeInDirection(vector3d)) {
                this.f_26496_.advance();
            }
            this.m_6481_(vector3d);
        }

        @Override
        protected boolean canMoveDirectly(Vec3 vec3, Vec3 vec31) {
            Vec3 vector3d = new Vec3(vec31.x, vec31.y + (double) this.f_26494_.m_20206_() * 0.5, vec31.z);
            BlockHitResult result = this.f_26495_.m_45547_(new ClipContext(vec3, vector3d, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.f_26494_));
            return CorrodentEntity.isSafeDig(this.f_26495_, result.getBlockPos());
        }

        private boolean shouldTargetNextNodeInDirection(Vec3 currentPosition) {
            if (this.f_26496_.getNextNodeIndex() + 1 >= this.f_26496_.getNodeCount()) {
                return false;
            } else {
                Vec3 vector3d = Vec3.atBottomCenterOf(this.f_26496_.getNextNodePos());
                if (!currentPosition.closerThan(vector3d, 2.0)) {
                    return false;
                } else {
                    Vec3 vector3d1 = Vec3.atBottomCenterOf(this.f_26496_.getNodePos(this.f_26496_.getNextNodeIndex() + 1));
                    Vec3 vector3d2 = vector3d1.subtract(vector3d);
                    Vec3 vector3d3 = currentPosition.subtract(vector3d);
                    return vector3d2.dot(vector3d3) > 0.0;
                }
            }
        }
    }
}