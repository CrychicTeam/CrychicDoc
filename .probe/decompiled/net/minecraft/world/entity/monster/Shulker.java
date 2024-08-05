package net.minecraft.world.entity.monster;

import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class Shulker extends AbstractGolem implements VariantHolder<Optional<DyeColor>>, Enemy {

    private static final UUID COVERED_ARMOR_MODIFIER_UUID = UUID.fromString("7E0292F2-9434-48D5-A29F-9583AF7DF27F");

    private static final AttributeModifier COVERED_ARMOR_MODIFIER = new AttributeModifier(COVERED_ARMOR_MODIFIER_UUID, "Covered armor bonus", 20.0, AttributeModifier.Operation.ADDITION);

    protected static final EntityDataAccessor<Direction> DATA_ATTACH_FACE_ID = SynchedEntityData.defineId(Shulker.class, EntityDataSerializers.DIRECTION);

    protected static final EntityDataAccessor<Byte> DATA_PEEK_ID = SynchedEntityData.defineId(Shulker.class, EntityDataSerializers.BYTE);

    protected static final EntityDataAccessor<Byte> DATA_COLOR_ID = SynchedEntityData.defineId(Shulker.class, EntityDataSerializers.BYTE);

    private static final int TELEPORT_STEPS = 6;

    private static final byte NO_COLOR = 16;

    private static final byte DEFAULT_COLOR = 16;

    private static final int MAX_TELEPORT_DISTANCE = 8;

    private static final int OTHER_SHULKER_SCAN_RADIUS = 8;

    private static final int OTHER_SHULKER_LIMIT = 5;

    private static final float PEEK_PER_TICK = 0.05F;

    static final Vector3f FORWARD = Util.make(() -> {
        Vec3i $$0 = Direction.SOUTH.getNormal();
        return new Vector3f((float) $$0.getX(), (float) $$0.getY(), (float) $$0.getZ());
    });

    private float currentPeekAmountO;

    private float currentPeekAmount;

    @Nullable
    private BlockPos clientOldAttachPosition;

    private int clientSideTeleportInterpolation;

    private static final float MAX_LID_OPEN = 1.0F;

    public Shulker(EntityType<? extends Shulker> entityTypeExtendsShulker0, Level level1) {
        super(entityTypeExtendsShulker0, level1);
        this.f_21364_ = 5;
        this.f_21365_ = new Shulker.ShulkerLookControl(this);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0F, 0.02F, true));
        this.f_21345_.addGoal(4, new Shulker.ShulkerAttackGoal());
        this.f_21345_.addGoal(7, new Shulker.ShulkerPeekGoal());
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this, this.getClass()).setAlertOthers());
        this.f_21346_.addGoal(2, new Shulker.ShulkerNearestAttackGoal(this));
        this.f_21346_.addGoal(3, new Shulker.ShulkerDefenseAttackGoal(this));
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SHULKER_AMBIENT;
    }

    @Override
    public void playAmbientSound() {
        if (!this.isClosed()) {
            super.m_8032_();
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SHULKER_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return this.isClosed() ? SoundEvents.SHULKER_HURT_CLOSED : SoundEvents.SHULKER_HURT;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_ATTACH_FACE_ID, Direction.DOWN);
        this.f_19804_.define(DATA_PEEK_ID, (byte) 0);
        this.f_19804_.define(DATA_COLOR_ID, (byte) 16);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0);
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new Shulker.ShulkerBodyRotationControl(this);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7378_(compoundTag0);
        this.setAttachFace(Direction.from3DDataValue(compoundTag0.getByte("AttachFace")));
        this.f_19804_.set(DATA_PEEK_ID, compoundTag0.getByte("Peek"));
        if (compoundTag0.contains("Color", 99)) {
            this.f_19804_.set(DATA_COLOR_ID, compoundTag0.getByte("Color"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7380_(compoundTag0);
        compoundTag0.putByte("AttachFace", (byte) this.getAttachFace().get3DDataValue());
        compoundTag0.putByte("Peek", this.f_19804_.get(DATA_PEEK_ID));
        compoundTag0.putByte("Color", this.f_19804_.get(DATA_COLOR_ID));
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (!this.m_9236_().isClientSide && !this.m_20159_() && !this.canStayAt(this.m_20183_(), this.getAttachFace())) {
            this.findNewAttachment();
        }
        if (this.updatePeekAmount()) {
            this.onPeekAmountChange();
        }
        if (this.m_9236_().isClientSide) {
            if (this.clientSideTeleportInterpolation > 0) {
                this.clientSideTeleportInterpolation--;
            } else {
                this.clientOldAttachPosition = null;
            }
        }
    }

    private void findNewAttachment() {
        Direction $$0 = this.findAttachableSurface(this.m_20183_());
        if ($$0 != null) {
            this.setAttachFace($$0);
        } else {
            this.teleportSomewhere();
        }
    }

    @Override
    protected AABB makeBoundingBox() {
        float $$0 = getPhysicalPeek(this.currentPeekAmount);
        Direction $$1 = this.getAttachFace().getOpposite();
        float $$2 = this.m_6095_().getWidth() / 2.0F;
        return getProgressAabb($$1, $$0).move(this.m_20185_() - (double) $$2, this.m_20186_(), this.m_20189_() - (double) $$2);
    }

    private static float getPhysicalPeek(float float0) {
        return 0.5F - Mth.sin((0.5F + float0) * (float) Math.PI) * 0.5F;
    }

    private boolean updatePeekAmount() {
        this.currentPeekAmountO = this.currentPeekAmount;
        float $$0 = (float) this.getRawPeekAmount() * 0.01F;
        if (this.currentPeekAmount == $$0) {
            return false;
        } else {
            if (this.currentPeekAmount > $$0) {
                this.currentPeekAmount = Mth.clamp(this.currentPeekAmount - 0.05F, $$0, 1.0F);
            } else {
                this.currentPeekAmount = Mth.clamp(this.currentPeekAmount + 0.05F, 0.0F, $$0);
            }
            return true;
        }
    }

    private void onPeekAmountChange() {
        this.m_20090_();
        float $$0 = getPhysicalPeek(this.currentPeekAmount);
        float $$1 = getPhysicalPeek(this.currentPeekAmountO);
        Direction $$2 = this.getAttachFace().getOpposite();
        float $$3 = $$0 - $$1;
        if (!($$3 <= 0.0F)) {
            for (Entity $$5 : this.m_9236_().getEntities(this, getProgressDeltaAabb($$2, $$1, $$0).move(this.m_20185_() - 0.5, this.m_20186_(), this.m_20189_() - 0.5), EntitySelector.NO_SPECTATORS.and(p_149771_ -> !p_149771_.isPassengerOfSameVehicle(this)))) {
                if (!($$5 instanceof Shulker) && !$$5.noPhysics) {
                    $$5.move(MoverType.SHULKER, new Vec3((double) ($$3 * (float) $$2.getStepX()), (double) ($$3 * (float) $$2.getStepY()), (double) ($$3 * (float) $$2.getStepZ())));
                }
            }
        }
    }

    public static AABB getProgressAabb(Direction direction0, float float1) {
        return getProgressDeltaAabb(direction0, -1.0F, float1);
    }

    public static AABB getProgressDeltaAabb(Direction direction0, float float1, float float2) {
        double $$3 = (double) Math.max(float1, float2);
        double $$4 = (double) Math.min(float1, float2);
        return new AABB(BlockPos.ZERO).expandTowards((double) direction0.getStepX() * $$3, (double) direction0.getStepY() * $$3, (double) direction0.getStepZ() * $$3).contract((double) (-direction0.getStepX()) * (1.0 + $$4), (double) (-direction0.getStepY()) * (1.0 + $$4), (double) (-direction0.getStepZ()) * (1.0 + $$4));
    }

    @Override
    public double getMyRidingOffset() {
        EntityType<?> $$0 = this.m_20202_().getType();
        return !(this.m_20202_() instanceof Boat) && $$0 != EntityType.MINECART ? super.m_6049_() : 0.1875 - this.m_20202_().getPassengersRidingOffset();
    }

    @Override
    public boolean startRiding(Entity entity0, boolean boolean1) {
        if (this.m_9236_().isClientSide()) {
            this.clientOldAttachPosition = null;
            this.clientSideTeleportInterpolation = 0;
        }
        this.setAttachFace(Direction.DOWN);
        return super.m_7998_(entity0, boolean1);
    }

    @Override
    public void stopRiding() {
        super.m_8127_();
        if (this.m_9236_().isClientSide) {
            this.clientOldAttachPosition = this.m_20183_();
        }
        this.f_20884_ = 0.0F;
        this.f_20883_ = 0.0F;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor0, DifficultyInstance difficultyInstance1, MobSpawnType mobSpawnType2, @Nullable SpawnGroupData spawnGroupData3, @Nullable CompoundTag compoundTag4) {
        this.m_146922_(0.0F);
        this.f_20885_ = this.m_146908_();
        this.m_146867_();
        return super.m_6518_(serverLevelAccessor0, difficultyInstance1, mobSpawnType2, spawnGroupData3, compoundTag4);
    }

    @Override
    public void move(MoverType moverType0, Vec3 vec1) {
        if (moverType0 == MoverType.SHULKER_BOX) {
            this.teleportSomewhere();
        } else {
            super.m_6478_(moverType0, vec1);
        }
    }

    @Override
    public Vec3 getDeltaMovement() {
        return Vec3.ZERO;
    }

    @Override
    public void setDeltaMovement(Vec3 vec0) {
    }

    @Override
    public void setPos(double double0, double double1, double double2) {
        BlockPos $$3 = this.m_20183_();
        if (this.m_20159_()) {
            super.m_6034_(double0, double1, double2);
        } else {
            super.m_6034_((double) Mth.floor(double0) + 0.5, (double) Mth.floor(double1 + 0.5), (double) Mth.floor(double2) + 0.5);
        }
        if (this.f_19797_ != 0) {
            BlockPos $$4 = this.m_20183_();
            if (!$$4.equals($$3)) {
                this.f_19804_.set(DATA_PEEK_ID, (byte) 0);
                this.f_19812_ = true;
                if (this.m_9236_().isClientSide && !this.m_20159_() && !$$4.equals(this.clientOldAttachPosition)) {
                    this.clientOldAttachPosition = $$3;
                    this.clientSideTeleportInterpolation = 6;
                    this.f_19790_ = this.m_20185_();
                    this.f_19791_ = this.m_20186_();
                    this.f_19792_ = this.m_20189_();
                }
            }
        }
    }

    @Nullable
    protected Direction findAttachableSurface(BlockPos blockPos0) {
        for (Direction $$1 : Direction.values()) {
            if (this.canStayAt(blockPos0, $$1)) {
                return $$1;
            }
        }
        return null;
    }

    boolean canStayAt(BlockPos blockPos0, Direction direction1) {
        if (this.isPositionBlocked(blockPos0)) {
            return false;
        } else {
            Direction $$2 = direction1.getOpposite();
            if (!this.m_9236_().loadedAndEntityCanStandOnFace(blockPos0.relative(direction1), this, $$2)) {
                return false;
            } else {
                AABB $$3 = getProgressAabb($$2, 1.0F).move(blockPos0).deflate(1.0E-6);
                return this.m_9236_().m_45756_(this, $$3);
            }
        }
    }

    private boolean isPositionBlocked(BlockPos blockPos0) {
        BlockState $$1 = this.m_9236_().getBlockState(blockPos0);
        if ($$1.m_60795_()) {
            return false;
        } else {
            boolean $$2 = $$1.m_60713_(Blocks.MOVING_PISTON) && blockPos0.equals(this.m_20183_());
            return !$$2;
        }
    }

    protected boolean teleportSomewhere() {
        if (!this.m_21525_() && this.m_6084_()) {
            BlockPos $$0 = this.m_20183_();
            for (int $$1 = 0; $$1 < 5; $$1++) {
                BlockPos $$2 = $$0.offset(Mth.randomBetweenInclusive(this.f_19796_, -8, 8), Mth.randomBetweenInclusive(this.f_19796_, -8, 8), Mth.randomBetweenInclusive(this.f_19796_, -8, 8));
                if ($$2.m_123342_() > this.m_9236_().m_141937_() && this.m_9236_().m_46859_($$2) && this.m_9236_().getWorldBorder().isWithinBounds($$2) && this.m_9236_().m_45756_(this, new AABB($$2).deflate(1.0E-6))) {
                    Direction $$3 = this.findAttachableSurface($$2);
                    if ($$3 != null) {
                        this.m_19877_();
                        this.setAttachFace($$3);
                        this.m_5496_(SoundEvents.SHULKER_TELEPORT, 1.0F, 1.0F);
                        this.setPos((double) $$2.m_123341_() + 0.5, (double) $$2.m_123342_(), (double) $$2.m_123343_() + 0.5);
                        this.m_9236_().m_220407_(GameEvent.TELEPORT, $$0, GameEvent.Context.of(this));
                        this.f_19804_.set(DATA_PEEK_ID, (byte) 0);
                        this.m_6710_(null);
                        return true;
                    }
                }
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public void lerpTo(double double0, double double1, double double2, float float3, float float4, int int5, boolean boolean6) {
        this.f_20903_ = 0;
        this.setPos(double0, double1, double2);
        this.m_19915_(float3, float4);
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        if (this.isClosed()) {
            Entity $$2 = damageSource0.getDirectEntity();
            if ($$2 instanceof AbstractArrow) {
                return false;
            }
        }
        if (!super.m_6469_(damageSource0, float1)) {
            return false;
        } else {
            if ((double) this.m_21223_() < (double) this.m_21233_() * 0.5 && this.f_19796_.nextInt(4) == 0) {
                this.teleportSomewhere();
            } else if (damageSource0.is(DamageTypeTags.IS_PROJECTILE)) {
                Entity $$3 = damageSource0.getDirectEntity();
                if ($$3 != null && $$3.getType() == EntityType.SHULKER_BULLET) {
                    this.hitByShulkerBullet();
                }
            }
            return true;
        }
    }

    private boolean isClosed() {
        return this.getRawPeekAmount() == 0;
    }

    private void hitByShulkerBullet() {
        Vec3 $$0 = this.m_20182_();
        AABB $$1 = this.m_20191_();
        if (!this.isClosed() && this.teleportSomewhere()) {
            int $$2 = this.m_9236_().getEntities(EntityType.SHULKER, $$1.inflate(8.0), Entity::m_6084_).size();
            float $$3 = (float) ($$2 - 1) / 5.0F;
            if (!(this.m_9236_().random.nextFloat() < $$3)) {
                Shulker $$4 = EntityType.SHULKER.create(this.m_9236_());
                if ($$4 != null) {
                    $$4.setVariant(this.getVariant());
                    $$4.m_20219_($$0);
                    this.m_9236_().m_7967_($$4);
                }
            }
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.m_6084_();
    }

    public Direction getAttachFace() {
        return this.f_19804_.get(DATA_ATTACH_FACE_ID);
    }

    private void setAttachFace(Direction direction0) {
        this.f_19804_.set(DATA_ATTACH_FACE_ID, direction0);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor0) {
        if (DATA_ATTACH_FACE_ID.equals(entityDataAccessor0)) {
            this.m_20011_(this.makeBoundingBox());
        }
        super.m_7350_(entityDataAccessor0);
    }

    private int getRawPeekAmount() {
        return this.f_19804_.get(DATA_PEEK_ID);
    }

    void setRawPeekAmount(int int0) {
        if (!this.m_9236_().isClientSide) {
            this.m_21051_(Attributes.ARMOR).removeModifier(COVERED_ARMOR_MODIFIER);
            if (int0 == 0) {
                this.m_21051_(Attributes.ARMOR).addPermanentModifier(COVERED_ARMOR_MODIFIER);
                this.m_5496_(SoundEvents.SHULKER_CLOSE, 1.0F, 1.0F);
                this.m_146850_(GameEvent.CONTAINER_CLOSE);
            } else {
                this.m_5496_(SoundEvents.SHULKER_OPEN, 1.0F, 1.0F);
                this.m_146850_(GameEvent.CONTAINER_OPEN);
            }
        }
        this.f_19804_.set(DATA_PEEK_ID, (byte) int0);
    }

    public float getClientPeekAmount(float float0) {
        return Mth.lerp(float0, this.currentPeekAmountO, this.currentPeekAmount);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return 0.5F;
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket clientboundAddEntityPacket0) {
        super.m_141965_(clientboundAddEntityPacket0);
        this.f_20883_ = 0.0F;
        this.f_20884_ = 0.0F;
    }

    @Override
    public int getMaxHeadXRot() {
        return 180;
    }

    @Override
    public int getMaxHeadYRot() {
        return 180;
    }

    @Override
    public void push(Entity entity0) {
    }

    @Override
    public float getPickRadius() {
        return 0.0F;
    }

    public Optional<Vec3> getRenderPosition(float float0) {
        if (this.clientOldAttachPosition != null && this.clientSideTeleportInterpolation > 0) {
            double $$1 = (double) ((float) this.clientSideTeleportInterpolation - float0) / 6.0;
            $$1 *= $$1;
            BlockPos $$2 = this.m_20183_();
            double $$3 = (double) ($$2.m_123341_() - this.clientOldAttachPosition.m_123341_()) * $$1;
            double $$4 = (double) ($$2.m_123342_() - this.clientOldAttachPosition.m_123342_()) * $$1;
            double $$5 = (double) ($$2.m_123343_() - this.clientOldAttachPosition.m_123343_()) * $$1;
            return Optional.of(new Vec3(-$$3, -$$4, -$$5));
        } else {
            return Optional.empty();
        }
    }

    public void setVariant(Optional<DyeColor> optionalDyeColor0) {
        this.f_19804_.set(DATA_COLOR_ID, (Byte) optionalDyeColor0.map(p_262566_ -> (byte) p_262566_.getId()).orElse((byte) 16));
    }

    public Optional<DyeColor> getVariant() {
        return Optional.ofNullable(this.getColor());
    }

    @Nullable
    public DyeColor getColor() {
        byte $$0 = this.f_19804_.get(DATA_COLOR_ID);
        return $$0 != 16 && $$0 <= 15 ? DyeColor.byId($$0) : null;
    }

    class ShulkerAttackGoal extends Goal {

        private int attackTime;

        public ShulkerAttackGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity $$0 = Shulker.this.m_5448_();
            return $$0 != null && $$0.isAlive() ? Shulker.this.m_9236_().m_46791_() != Difficulty.PEACEFUL : false;
        }

        @Override
        public void start() {
            this.attackTime = 20;
            Shulker.this.setRawPeekAmount(100);
        }

        @Override
        public void stop() {
            Shulker.this.setRawPeekAmount(0);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (Shulker.this.m_9236_().m_46791_() != Difficulty.PEACEFUL) {
                this.attackTime--;
                LivingEntity $$0 = Shulker.this.m_5448_();
                if ($$0 != null) {
                    Shulker.this.m_21563_().setLookAt($$0, 180.0F, 180.0F);
                    double $$1 = Shulker.this.m_20280_($$0);
                    if ($$1 < 400.0) {
                        if (this.attackTime <= 0) {
                            this.attackTime = 20 + Shulker.this.f_19796_.nextInt(10) * 20 / 2;
                            Shulker.this.m_9236_().m_7967_(new ShulkerBullet(Shulker.this.m_9236_(), Shulker.this, $$0, Shulker.this.getAttachFace().getAxis()));
                            Shulker.this.m_5496_(SoundEvents.SHULKER_SHOOT, 2.0F, (Shulker.this.f_19796_.nextFloat() - Shulker.this.f_19796_.nextFloat()) * 0.2F + 1.0F);
                        }
                    } else {
                        Shulker.this.m_6710_(null);
                    }
                    super.tick();
                }
            }
        }
    }

    static class ShulkerBodyRotationControl extends BodyRotationControl {

        public ShulkerBodyRotationControl(Mob mob0) {
            super(mob0);
        }

        @Override
        public void clientTick() {
        }
    }

    static class ShulkerDefenseAttackGoal extends NearestAttackableTargetGoal<LivingEntity> {

        public ShulkerDefenseAttackGoal(Shulker shulker0) {
            super(shulker0, LivingEntity.class, 10, true, false, p_33501_ -> p_33501_ instanceof Enemy);
        }

        @Override
        public boolean canUse() {
            return this.f_26135_.m_5647_() == null ? false : super.canUse();
        }

        @Override
        protected AABB getTargetSearchArea(double double0) {
            Direction $$1 = ((Shulker) this.f_26135_).getAttachFace();
            if ($$1.getAxis() == Direction.Axis.X) {
                return this.f_26135_.m_20191_().inflate(4.0, double0, double0);
            } else {
                return $$1.getAxis() == Direction.Axis.Z ? this.f_26135_.m_20191_().inflate(double0, double0, 4.0) : this.f_26135_.m_20191_().inflate(double0, 4.0, double0);
            }
        }
    }

    class ShulkerLookControl extends LookControl {

        public ShulkerLookControl(Mob mob0) {
            super(mob0);
        }

        @Override
        protected void clampHeadRotationToBody() {
        }

        @Override
        protected Optional<Float> getYRotD() {
            Direction $$0 = Shulker.this.getAttachFace().getOpposite();
            Vector3f $$1 = $$0.getRotation().transform(new Vector3f(Shulker.FORWARD));
            Vec3i $$2 = $$0.getNormal();
            Vector3f $$3 = new Vector3f((float) $$2.getX(), (float) $$2.getY(), (float) $$2.getZ());
            $$3.cross($$1);
            double $$4 = this.f_24941_ - this.f_24937_.m_20185_();
            double $$5 = this.f_24942_ - this.f_24937_.m_20188_();
            double $$6 = this.f_24943_ - this.f_24937_.m_20189_();
            Vector3f $$7 = new Vector3f((float) $$4, (float) $$5, (float) $$6);
            float $$8 = $$3.dot($$7);
            float $$9 = $$1.dot($$7);
            return !(Math.abs($$8) > 1.0E-5F) && !(Math.abs($$9) > 1.0E-5F) ? Optional.empty() : Optional.of((float) (Mth.atan2((double) (-$$8), (double) $$9) * 180.0F / (float) Math.PI));
        }

        @Override
        protected Optional<Float> getXRotD() {
            return Optional.of(0.0F);
        }
    }

    class ShulkerNearestAttackGoal extends NearestAttackableTargetGoal<Player> {

        public ShulkerNearestAttackGoal(Shulker shulker0) {
            super(shulker0, Player.class, true);
        }

        @Override
        public boolean canUse() {
            return Shulker.this.m_9236_().m_46791_() == Difficulty.PEACEFUL ? false : super.canUse();
        }

        @Override
        protected AABB getTargetSearchArea(double double0) {
            Direction $$1 = ((Shulker) this.f_26135_).getAttachFace();
            if ($$1.getAxis() == Direction.Axis.X) {
                return this.f_26135_.m_20191_().inflate(4.0, double0, double0);
            } else {
                return $$1.getAxis() == Direction.Axis.Z ? this.f_26135_.m_20191_().inflate(double0, double0, 4.0) : this.f_26135_.m_20191_().inflate(double0, 4.0, double0);
            }
        }
    }

    class ShulkerPeekGoal extends Goal {

        private int peekTime;

        @Override
        public boolean canUse() {
            return Shulker.this.m_5448_() == null && Shulker.this.f_19796_.nextInt(m_186073_(40)) == 0 && Shulker.this.canStayAt(Shulker.this.m_20183_(), Shulker.this.getAttachFace());
        }

        @Override
        public boolean canContinueToUse() {
            return Shulker.this.m_5448_() == null && this.peekTime > 0;
        }

        @Override
        public void start() {
            this.peekTime = this.m_183277_(20 * (1 + Shulker.this.f_19796_.nextInt(3)));
            Shulker.this.setRawPeekAmount(30);
        }

        @Override
        public void stop() {
            if (Shulker.this.m_5448_() == null) {
                Shulker.this.setRawPeekAmount(0);
            }
        }

        @Override
        public void tick() {
            this.peekTime--;
        }
    }
}