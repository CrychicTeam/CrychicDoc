package com.mna.entities.summon;

import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.tools.MATags;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.effects.EffectInit;
import com.mna.entities.EntityInit;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.network.NetworkHooks;

public class AnimusBlock extends Mob implements Enemy, PlayerRideableJumping {

    protected static final EntityDataAccessor<BlockPos> ORIGIN = SynchedEntityData.defineId(AnimusBlock.class, EntityDataSerializers.BLOCK_POS);

    protected static final EntityDataAccessor<Integer> BLOCK_STATE_ID = SynchedEntityData.defineId(AnimusBlock.class, EntityDataSerializers.INT);

    protected static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(AnimusBlock.class, EntityDataSerializers.INT);

    protected static final EntityDataAccessor<Float> RIDER_OFFSET_HEIGHT = SynchedEntityData.defineId(AnimusBlock.class, EntityDataSerializers.FLOAT);

    protected static final EntityDataAccessor<Boolean> RIDER_SIT = SynchedEntityData.defineId(AnimusBlock.class, EntityDataSerializers.BOOLEAN);

    protected static final EntityDataAccessor<Boolean> CAN_FLY = SynchedEntityData.defineId(AnimusBlock.class, EntityDataSerializers.BOOLEAN);

    public float squishAmount;

    public float squishFactor;

    public float prevSquishFactor;

    private boolean wasOnGround;

    private boolean fireImmune = false;

    protected float jumpPower;

    private boolean clearOrigin = true;

    private BlockState fallTile = Blocks.SAND.defaultBlockState();

    public AnimusBlock(EntityType<? extends Mob> type, Level worldIn) {
        super(type, worldIn);
        this.f_21342_ = new AnimusBlock.MoveHelperController(this);
    }

    public AnimusBlock(Level worldIn, BlockState state, BlockPos pos, int duration) {
        this(EntityInit.ANIMUS_BLOCK.get(), worldIn);
        this.m_6034_((double) ((float) pos.m_123341_() + 0.5F), (double) pos.m_123342_(), (double) ((float) pos.m_123343_() + 0.5F));
        this.fallTile = state;
        this.f_19850_ = true;
        if (!this.m_9236_().isClientSide()) {
            VoxelShape collisionShape = state.m_60808_(this.m_9236_(), pos);
            this.f_19804_.set(RIDER_OFFSET_HEIGHT, (float) collisionShape.max(Direction.Axis.Y));
            this.f_19804_.set(RIDER_SIT, state.m_60838_(this.m_9236_(), pos));
            this.f_19804_.set(BLOCK_STATE_ID, Block.getId(this.fallTile));
            this.f_19804_.set(DURATION, duration);
            this.f_19804_.set(ORIGIN, pos);
            if (MATags.isBlockIn(state.m_60734_(), MATags.Blocks.ANIMUS_FLYING)) {
                this.f_19804_.set(CAN_FLY, true);
            }
        }
        this.fireImmune = this.fallTile.getFireSpreadSpeed(worldIn, pos, Direction.DOWN) == 0;
    }

    @Override
    public boolean fireImmune() {
        return this.fireImmune;
    }

    public AnimusBlock doNotClearOrigin() {
        this.clearOrigin = false;
        return this;
    }

    @Override
    public void tick() {
        if (this.f_19797_ == 1 && this.clearOrigin && !this.m_9236_().isClientSide()) {
            BlockPos origin = this.f_19804_.get(ORIGIN);
            if (!this.m_9236_().m_46859_(origin)) {
                this.m_9236_().setBlock(origin, Blocks.AIR.defaultBlockState(), 3);
            }
        }
        this.squishFactor = this.squishFactor + (this.squishAmount - this.squishFactor) * 0.5F;
        this.prevSquishFactor = this.squishFactor;
        super.tick();
        if (this.m_20096_() && !this.wasOnGround && this.getControllingPassenger() == null) {
            int i = 1;
            for (int j = 0; j < i * 8; j++) {
                float f = this.f_19796_.nextFloat() * (float) (Math.PI * 2);
                float f1 = this.f_19796_.nextFloat() * 0.5F + 0.5F;
                float f2 = Mth.sin(f) * (float) i * 0.5F * f1;
                float f3 = Mth.cos(f) * (float) i * 0.5F * f1;
                this.m_9236_().addParticle(this.getSquishParticle(), this.m_20185_() + (double) f2, this.m_20186_(), this.m_20189_() + (double) f3, 0.0, 0.0, 0.0);
            }
            this.m_5496_(this.getSquishSound(), this.m_6121_(), ((this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            this.squishAmount = -0.5F;
        } else if (!this.m_20096_() && this.wasOnGround) {
            this.squishAmount = 1.0F;
        }
        this.wasOnGround = this.m_20096_();
        this.alterSquishAmount();
        if (!this.m_9236_().isClientSide()) {
            if (this.m_20068_() && this.getControllingPassenger() instanceof Player player) {
                this.m_183634_();
                if (!this.m_20072_() && !this.m_21023_(EffectInit.GRAVITY_WELL.get())) {
                    player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
                        float amount = 2.0F;
                        if (!m.getCastingResource().hasEnough(player, amount)) {
                            this.m_20153_();
                            this.m_20242_(false);
                        } else {
                            m.getCastingResource().consume(player, amount);
                        }
                    });
                } else {
                    this.m_20153_();
                    this.m_20242_(false);
                }
            }
            if (this.f_19797_ >= this.f_19804_.get(DURATION) && this.m_20197_().size() == 0 && (this.m_20096_() || this.m_20072_())) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
                BlockState target = this.m_9236_().getBlockState(this.m_20183_());
                Player playerx = FakePlayerFactory.getMinecraft((ServerLevel) this.m_9236_());
                ItemStack stack = new ItemStack(this.getFeetBlockState().m_60734_());
                if (!this.m_9236_().m_46859_(this.m_20183_()) && !target.m_60629_(new BlockPlaceContext(playerx, InteractionHand.MAIN_HAND, stack, new BlockHitResult(this.m_20182_(), this.m_6374_(), this.m_20183_(), true)))) {
                    this.m_19983_(stack);
                } else {
                    this.m_9236_().setBlockAndUpdate(this.m_20183_(), this.getFeetBlockState());
                    this.getFeetBlockState().m_60734_().setPlacedBy(this.m_9236_(), this.m_20183_(), this.getFeetBlockState(), this, stack);
                }
            }
        }
        if (this.m_9236_().isClientSide()) {
            int numParticles = this.getControllingPassenger() != null && this.m_20068_() ? 10 : 2;
            for (int i = 0; i < numParticles; i++) {
                this.m_9236_().addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_VELOCITY.get()).setColor(255, 215, 0), this.m_20185_() - 0.5 + Math.random(), this.m_20186_() + Math.random() * (double) this.f_19804_.get(RIDER_OFFSET_HEIGHT).floatValue(), this.m_20189_() - 0.5 + Math.random(), 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void onEffectAdded(MobEffectInstance pEffectInstance, Entity pEntity) {
        super.m_142540_(pEffectInstance, pEntity);
        MobEffect effect = pEffectInstance.getEffect();
        if (effect == EffectInit.LEVITATION.get() || effect == MobEffects.LEVITATION || effect == EffectInit.SOAR.get()) {
            this.m_20242_(true);
        }
    }

    @Override
    protected void onEffectRemoved(MobEffectInstance pEffectInstance) {
        super.m_7285_(pEffectInstance);
        MobEffect effect = pEffectInstance.getEffect();
        if ((effect == EffectInit.LEVITATION.get() || effect == MobEffects.LEVITATION || effect == EffectInit.SOAR.get()) && (this.fallTile == null || !MATags.isBlockIn(this.fallTile.m_60734_(), MATags.Blocks.ANIMUS_FLYING))) {
            this.m_20242_(false);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(ORIGIN, BlockPos.ZERO);
        this.f_19804_.define(BLOCK_STATE_ID, Block.getId(Blocks.SAND.defaultBlockState()));
        this.f_19804_.define(DURATION, 10);
        this.f_19804_.define(RIDER_OFFSET_HEIGHT, 1.0F);
        this.f_19804_.define(RIDER_SIT, true);
        this.f_19804_.define(CAN_FLY, false);
    }

    public void setOrigin(BlockPos origin) {
        this.f_19804_.set(ORIGIN, origin);
    }

    @OnlyIn(Dist.CLIENT)
    public BlockPos getOrigin() {
        return this.f_19804_.get(ORIGIN);
    }

    protected void alterSquishAmount() {
        this.squishAmount *= 0.6F;
    }

    protected SoundEvent getSquishSound() {
        return this.getFeetBlockState().m_60827_().getPlaceSound();
    }

    protected SoundEvent getJumpSound() {
        return this.getFeetBlockState().m_60827_().getBreakSound();
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public BlockState getFeetBlockState() {
        return Block.stateById(this.f_19804_.get(BLOCK_STATE_ID));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.fallTile = NbtUtils.readBlockState(this.m_9236_().m_246945_(Registries.BLOCK), compound.getCompound("BlockState"));
        if (this.fallTile.m_60795_()) {
            this.fallTile = Blocks.SAND.defaultBlockState();
        }
        if (compound.contains("Origin")) {
            this.f_19804_.set(ORIGIN, NbtUtils.readBlockPos(compound.getCompound("Origin")));
        }
        if (compound.contains("StateID")) {
            this.f_19804_.set(BLOCK_STATE_ID, compound.getInt("StateID"));
        }
        if (compound.contains("Duration")) {
            this.f_19804_.set(DURATION, compound.getInt("Duration"));
        }
        super.readAdditionalSaveData(compound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.put("BlockState", NbtUtils.writeBlockState(this.fallTile));
        compound.put("Origin", NbtUtils.writeBlockPos(this.f_19804_.get(ORIGIN)));
        compound.putInt("StateID", this.f_19804_.get(BLOCK_STATE_ID));
        compound.putInt("Duration", this.f_19804_.get(DURATION));
        super.addAdditionalSaveData(compound);
    }

    protected ParticleOptions getSquishParticle() {
        return new BlockParticleOption(ParticleTypes.BLOCK, this.getFeetBlockState());
    }

    protected int getJumpDelay() {
        return this.f_19796_.nextInt(20) + 10;
    }

    public static AttributeSupplier.Builder getGlobalAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.ATTACK_DAMAGE, 5.0);
    }

    @Override
    public void push(Entity entityIn) {
        super.m_7334_(entityIn);
        if (entityIn == this.m_5448_()) {
            if (!this.m_9236_().isClientSide()) {
                if (this.getFeetBlockState() == Blocks.TNT.defaultBlockState()) {
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                    this.m_9236_().explode(this, this.m_20185_(), this.m_20186_(), this.m_20189_(), 4.0F, Level.ExplosionInteraction.TNT);
                    return;
                }
                if (this.getFeetBlockState().m_60734_() instanceof FireBlock) {
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                    entityIn.hurt(entityIn.damageSources().onFire(), 5.0F);
                    entityIn.setSecondsOnFire(10);
                    return;
                }
            }
            if (MATags.isBlockIn(this.getFeetBlockState().m_60734_(), MATags.Blocks.ANIMUS_TANGLES)) {
                ((LivingEntity) entityIn).addEffect(new MobEffectInstance(EffectInit.ENTANGLE.get(), 100, 0));
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            } else if (MATags.isBlockIn(this.getFeetBlockState().m_60734_(), MATags.Blocks.ANIMUS_POISONCLOUD)) {
                AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.m_9236_(), this.m_20185_(), this.m_20186_(), this.m_20189_());
                areaeffectcloud.setRadius(3.0F);
                areaeffectcloud.setRadiusOnUse(-0.5F);
                areaeffectcloud.setWaitTime(10);
                areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float) areaeffectcloud.getDuration());
                areaeffectcloud.setPotion(new Potion(new MobEffectInstance(MobEffects.POISON, 1200, 0)));
                this.m_9236_().m_7967_(areaeffectcloud);
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            } else {
                this.dealDamage((LivingEntity) entityIn);
                if (MATags.isBlockIn(this.getFeetBlockState().m_60734_(), MATags.Blocks.ANIMUS_GLOW)) {
                    ((LivingEntity) entityIn).addEffect(new MobEffectInstance(MobEffects.GLOWING, 600, 0));
                }
                if (MATags.isBlockIn(this.getFeetBlockState().m_60734_(), MATags.Blocks.ANIMUS_SLOWS)) {
                    ((LivingEntity) entityIn).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 2));
                }
                if (MATags.isBlockIn(this.getFeetBlockState().m_60734_(), MATags.Blocks.ANIMUS_KNOCKBACK)) {
                    float mX = (float) (this.m_20185_() - entityIn.getX());
                    float mZ = (float) (this.m_20189_() - entityIn.getZ());
                    ((LivingEntity) entityIn).knockback(0.5, (double) mX, (double) mZ);
                }
            }
        }
    }

    protected void dealDamage(LivingEntity entityIn) {
        if (this.m_6084_()) {
            int i = 3;
            if (this.m_20280_(entityIn) < 0.6 * (double) i * 0.6 * (double) i && this.m_21574_().hasLineOfSight(entityIn) && entityIn.hurt(this.m_269291_().mobAttack(this), this.getAttackDamage())) {
                this.m_5496_(SoundEvents.SLIME_ATTACK, 1.0F, (this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.2F + 1.0F);
                this.m_19970_(this, entityIn);
            }
        }
    }

    @Override
    public void setJumping(boolean jumping) {
        this.f_20899_ = jumping;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onPlayerJump(int jumpPowerIn) {
        if (jumpPowerIn < 0) {
            jumpPowerIn = 0;
        }
        MobEffectInstance effect = this.m_21124_(MobEffects.JUMP);
        if (jumpPowerIn >= 90) {
            this.jumpPower = 1.0F;
        } else {
            this.jumpPower = 0.4F + 0.4F * (float) jumpPowerIn / 90.0F;
        }
        if (effect != null) {
            this.jumpPower = (float) ((double) this.jumpPower + 0.15 * (double) effect.getAmplifier());
        }
    }

    @Override
    public boolean canJump() {
        return !this.m_20068_();
    }

    @Override
    public void handleStartJump(int pJumpPower) {
    }

    @Override
    public void handleStopJump() {
    }

    public boolean canRiderInteract() {
        return true;
    }

    @Override
    public LivingEntity getControllingPassenger() {
        Entity entity = this.m_146895_();
        return entity instanceof Player ? (Player) entity : null;
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.m_6084_()) {
            LivingEntity livingentity = this.getControllingPassenger();
            if (this.m_20160_() && this.canRiderInteract() && livingentity != null) {
                this.m_146922_(livingentity.m_146908_());
                this.f_19859_ = this.m_146908_();
                this.m_146926_(livingentity.m_146909_() * 0.5F);
                this.m_19915_(this.m_146908_(), this.m_146909_());
                this.f_20883_ = this.m_146908_();
                this.f_20885_ = this.f_20883_;
                float f = livingentity.xxa * 0.5F;
                float f1 = livingentity.zza;
                if (f1 <= 0.0F) {
                    f1 *= 0.25F;
                }
                boolean jump = this.jumpPower > 0.0F && !this.f_20899_ && this.m_20096_();
                if (jump) {
                    double d0 = (double) (this.jumpPower * this.m_20098_());
                    double d1;
                    if (this.m_21023_(MobEffects.JUMP)) {
                        d1 = d0 + (double) ((float) (this.m_21124_(MobEffects.JUMP).getAmplifier() + 1) * 0.1F);
                    } else {
                        d1 = d0;
                    }
                    float horizontalScale = 3.0F;
                    Vec3 vector3d = this.m_20184_();
                    this.m_20334_(vector3d.x * (double) horizontalScale, d1, vector3d.z * (double) horizontalScale);
                    this.setJumping(true);
                    this.f_19812_ = true;
                    ForgeHooks.onLivingJump(this);
                    if (f1 > 0.0F) {
                        float f2 = Mth.sin(this.m_146908_() * (float) (Math.PI / 180.0));
                        float f3 = Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0));
                        this.m_20256_(this.m_20184_().add((double) (-0.4F * f2 * this.jumpPower), 0.0, (double) (0.4F * f3 * this.jumpPower)));
                    }
                    this.jumpPower = 0.0F;
                } else if (this.m_20096_()) {
                    f = 0.0F;
                    f1 = 0.0F;
                }
                if (this.m_20068_()) {
                    travelVector = livingentity.m_20156_().normalize().scale(travelVector.length());
                }
                if (!this.isControlledByLocalInstance() || this.m_20096_() && !jump && !this.m_20068_()) {
                    if (livingentity instanceof Player) {
                        this.m_20256_(Vec3.ZERO);
                    }
                } else {
                    this.m_7910_((float) this.m_21133_(Attributes.MOVEMENT_SPEED));
                    super.m_7023_(new Vec3((double) f, travelVector.y * (double) livingentity.zza, (double) f1));
                }
                if (this.m_20096_()) {
                    this.jumpPower = 0.0F;
                    this.setJumping(false);
                }
                this.m_267651_(false);
            } else {
                this.jumpPower = 0.02F;
                super.m_7023_(travelVector);
            }
        }
    }

    @Override
    public boolean isControlledByLocalInstance() {
        return !(this.getControllingPassenger() instanceof Player player) ? this.m_21515_() : player.isLocalPlayer() && (player.f_20900_ > 0.0F || player.f_20902_ > 0.0F || !this.m_20096_() || this.f_20899_);
    }

    @Override
    public void aiStep() {
        if (this.f_20954_ > 0) {
            this.f_20954_--;
        }
        if (this.isControlledByLocalInstance()) {
            this.f_20903_ = 0;
            this.m_217006_(this.m_20185_(), this.m_20186_(), this.m_20189_());
        }
        if (this.f_20903_ > 0) {
            double d0 = this.m_20185_() + (this.f_20904_ - this.m_20185_()) / (double) this.f_20903_;
            double d2 = this.m_20186_() + (this.f_20905_ - this.m_20186_()) / (double) this.f_20903_;
            double d4 = this.m_20189_() + (this.f_20906_ - this.m_20189_()) / (double) this.f_20903_;
            double d6 = Mth.wrapDegrees(this.f_20907_ - (double) this.m_146908_());
            this.m_146922_(this.m_146908_() + (float) d6 / (float) this.f_20903_);
            this.m_146926_(this.m_146909_() + (float) (this.f_20908_ - (double) this.m_146909_()) / (float) this.f_20903_);
            this.f_20903_--;
            this.m_6034_(d0, d2, d4);
            this.m_19915_(this.m_146908_(), this.m_146909_());
        } else if (!this.m_21515_()) {
            this.m_20256_(this.m_20184_().scale(0.98));
        }
        if (this.f_20934_ > 0) {
            this.f_20885_ = this.f_20885_ + (float) Mth.wrapDegrees(this.f_20933_ - (double) this.f_20885_) / (float) this.f_20934_;
            this.f_20934_--;
        }
        Vec3 vec31 = this.m_20184_();
        double d1 = vec31.x;
        double d3 = vec31.y;
        double d5 = vec31.z;
        if (Math.abs(vec31.x) < 0.003) {
            d1 = 0.0;
        }
        if (Math.abs(vec31.y) < 0.003) {
            d3 = 0.0;
        }
        if (Math.abs(vec31.z) < 0.003) {
            d5 = 0.0;
        }
        this.m_20334_(d1, d3, d5);
        this.m_9236_().getProfiler().push("ai");
        if (this.m_6107_()) {
            this.f_20899_ = false;
            this.f_20900_ = 0.0F;
            this.f_20902_ = 0.0F;
        } else if (this.m_21515_()) {
            this.m_9236_().getProfiler().push("newAi");
            this.m_6140_();
            this.m_9236_().getProfiler().pop();
        }
        this.m_9236_().getProfiler().pop();
        this.m_9236_().getProfiler().push("jump");
        if (this.f_20899_ && this.m_6129_()) {
            FluidType fluidType = this.getMaxHeightFluidType();
            double d7;
            if (!fluidType.isAir()) {
                d7 = this.getFluidTypeHeight(fluidType);
            } else if (this.m_20077_()) {
                d7 = this.m_204036_(FluidTags.LAVA);
            } else {
                d7 = this.m_204036_(FluidTags.WATER);
            }
            boolean flag = this.m_20069_() && d7 > 0.0;
            double d8 = this.m_20204_();
            if (!flag || this.m_20096_() && !(d7 > d8)) {
                if (!this.m_20077_() || this.m_20096_() && !(d7 > d8)) {
                    if (fluidType.isAir() || this.m_20096_() && !(d7 > d8)) {
                        if ((this.m_20096_() || flag && d7 <= d8) && this.f_20954_ == 0) {
                            this.m_6135_();
                            this.f_20954_ = 10;
                        }
                    } else {
                        this.jumpInFluid(fluidType);
                    }
                } else {
                    this.jumpInFluid(ForgeMod.LAVA_TYPE.get());
                }
            } else {
                this.jumpInFluid(ForgeMod.WATER_TYPE.get());
            }
        } else {
            this.f_20954_ = 0;
        }
        this.m_9236_().getProfiler().pop();
        this.m_9236_().getProfiler().push("travel");
        this.f_20900_ *= 0.98F;
        this.f_20902_ *= 0.98F;
        this.updateFallFlying();
        AABB aabb = this.m_20191_();
        Vec3 vec3 = new Vec3((double) this.f_20900_, (double) this.f_20901_, (double) this.f_20902_);
        if (this.m_21023_(MobEffects.SLOW_FALLING) || this.m_21023_(MobEffects.LEVITATION)) {
            this.m_183634_();
        }
        if (this.getControllingPassenger() instanceof Player player) {
            if (this.m_6084_()) {
                this.travelRidden(player, vec3);
            }
        } else {
            this.travel(vec3);
        }
        this.m_9236_().getProfiler().pop();
        this.m_9236_().getProfiler().push("freezing");
        if (!this.m_9236_().isClientSide() && !this.m_21224_()) {
            int i = this.m_146888_();
            if (this.f_146808_ && this.m_142079_()) {
                this.m_146917_(Math.min(this.m_146891_(), i + 1));
            } else {
                this.m_146917_(Math.max(0, i - 2));
            }
        }
        this.m_147225_();
        this.m_147226_();
        if (!this.m_9236_().isClientSide() && this.f_19797_ % 40 == 0 && this.m_146890_() && this.m_142079_()) {
            this.m_6469_(this.m_269291_().freeze(), 1.0F);
        }
        this.m_9236_().getProfiler().pop();
        this.m_9236_().getProfiler().push("push");
        if (this.f_20938_ > 0) {
            this.f_20938_--;
            this.m_21071_(aabb, this.m_20191_());
        }
        this.m_6138_();
        this.m_9236_().getProfiler().pop();
        if (!this.m_9236_().isClientSide() && this.m_6126_() && this.m_20071_()) {
            this.m_6469_(this.m_269291_().drown(), 1.0F);
        }
    }

    private void travelRidden(Player pPlayer, Vec3 pTravelVector) {
        Vec3 vec3 = this.m_274312_(pPlayer, pTravelVector);
        this.m_274498_(pPlayer, vec3);
        if (this.isControlledByLocalInstance()) {
            this.m_7910_(this.m_245547_(pPlayer));
            this.travel(vec3);
        } else {
            this.travel(vec3);
        }
    }

    private void updateFallFlying() {
        boolean flag = this.m_20291_(7);
        if (flag && !this.m_20096_() && !this.m_20159_() && !this.m_21023_(MobEffects.LEVITATION)) {
            ItemStack itemstack = this.m_6844_(EquipmentSlot.CHEST);
            flag = itemstack.canElytraFly(this) && itemstack.elytraFlightTick(this, this.f_20937_);
        } else {
            flag = false;
        }
        if (!this.m_9236_().isClientSide()) {
            this.m_20115_(7, flag);
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (this.m_7310_(player)) {
            this.mountTo(player);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    protected void mountTo(Player player) {
        if (!this.m_9236_().isClientSide()) {
            player.m_146926_(this.m_146909_());
            player.m_146922_(this.m_146908_());
            player.m_20329_(this);
            if (this.f_19804_.get(CAN_FLY)) {
                this.m_20242_(true);
            }
        }
    }

    @Override
    protected void removePassenger(Entity pPassenger) {
        super.m_20351_(pPassenger);
        if (!this.m_217005_()) {
            this.m_20242_(false);
        }
    }

    public boolean shouldRiderSit() {
        return this.f_19804_.get(RIDER_SIT);
    }

    public boolean shouldRiderFaceForward(Player player) {
        return true;
    }

    @Override
    public double getPassengersRidingOffset() {
        return this.f_19804_.get(RIDER_SIT) ? (double) this.f_19804_.get(RIDER_OFFSET_HEIGHT).floatValue() * 0.75 : (double) this.f_19804_.get(RIDER_OFFSET_HEIGHT).floatValue() + 0.35;
    }

    @Override
    protected void updateControlFlags() {
        boolean flag = !(this.getControllingPassenger() instanceof Mob);
        boolean flag1 = !(this.m_20202_() instanceof Boat);
        this.f_21345_.setControlFlag(Goal.Flag.MOVE, flag);
        this.f_21345_.setControlFlag(Goal.Flag.JUMP, flag && flag1);
        this.f_21345_.setControlFlag(Goal.Flag.LOOK, flag);
    }

    protected float getAttackDamage() {
        float amt = (float) this.m_21133_(Attributes.ATTACK_DAMAGE);
        if (MATags.isBlockIn(this.getFeetBlockState().m_60734_(), MATags.Blocks.ANIMUS_DAMAGEBOOST)) {
            amt += 2.0F;
        }
        return amt;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new AnimusBlock.FloatGoal(this));
        this.f_21345_.addGoal(2, new AnimusBlock.AttackGoal(this));
        this.f_21345_.addGoal(3, new AnimusBlock.FaceRandomGoal(this));
        this.f_21345_.addGoal(5, new AnimusBlock.HopGoal(this));
        this.f_21346_.addGoal(1, new NearestAttackableTargetGoal(this, Mob.class, 10, true, false, e -> !(e instanceof AnimusBlock) && e instanceof Enemy && Math.abs(e.m_20186_() - this.m_20186_()) <= 4.0));
    }

    static class AttackGoal extends Goal {

        private final AnimusBlock slime;

        private int growTieredTimer;

        public AttackGoal(AnimusBlock slimeIn) {
            this.slime = slimeIn;
            this.m_7021_(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity livingentity = this.slime.m_5448_();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else {
                return livingentity instanceof Player && ((Player) livingentity).getAbilities().invulnerable ? false : this.slime.m_21566_() instanceof AnimusBlock.MoveHelperController;
            }
        }

        @Override
        public void start() {
            this.growTieredTimer = 300;
            super.start();
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity livingentity = this.slime.m_5448_();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else {
                return livingentity instanceof Player && ((Player) livingentity).getAbilities().invulnerable ? false : --this.growTieredTimer > 0;
            }
        }

        @Override
        public void tick() {
            this.slime.m_21391_(this.slime.m_5448_(), 10.0F, 10.0F);
            ((AnimusBlock.MoveHelperController) this.slime.m_21566_()).setDirection(this.slime.m_146908_(), true);
        }
    }

    static class FaceRandomGoal extends Goal {

        private final AnimusBlock slime;

        private float chosenDegrees;

        private int nextRandomizeTime;

        public FaceRandomGoal(AnimusBlock slimeIn) {
            this.slime = slimeIn;
            this.m_7021_(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return this.slime.m_5448_() == null && (this.slime.m_20096_() || this.slime.m_20069_() || this.slime.m_20077_() || this.slime.m_21023_(MobEffects.LEVITATION)) && this.slime.m_21566_() instanceof AnimusBlock.MoveHelperController;
        }

        @Override
        public void tick() {
            if (--this.nextRandomizeTime <= 0) {
                this.nextRandomizeTime = 40 + this.slime.m_217043_().nextInt(60);
                this.chosenDegrees = (float) this.slime.m_217043_().nextInt(360);
            }
            ((AnimusBlock.MoveHelperController) this.slime.m_21566_()).setDirection(this.chosenDegrees, false);
        }
    }

    static class FloatGoal extends Goal {

        private final AnimusBlock slime;

        public FloatGoal(AnimusBlock slimeIn) {
            this.slime = slimeIn;
            this.m_7021_(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
            slimeIn.m_21573_().setCanFloat(true);
        }

        @Override
        public boolean canUse() {
            return (this.slime.m_20069_() || this.slime.m_20077_()) && this.slime.m_21566_() instanceof AnimusBlock.MoveHelperController;
        }

        @Override
        public void tick() {
            if (this.slime.m_217043_().nextFloat() < 0.8F) {
                this.slime.m_21569_().jump();
            }
            ((AnimusBlock.MoveHelperController) this.slime.m_21566_()).setSpeed(1.2);
        }
    }

    static class HopGoal extends Goal {

        private final AnimusBlock slime;

        public HopGoal(AnimusBlock slimeIn) {
            this.slime = slimeIn;
            this.m_7021_(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return !this.slime.m_20159_();
        }

        @Override
        public void tick() {
            ((AnimusBlock.MoveHelperController) this.slime.m_21566_()).setSpeed(1.0);
        }
    }

    static class MoveHelperController extends MoveControl {

        private float yRot;

        private int jumpDelay;

        private final AnimusBlock slime;

        private boolean isAggressive;

        public MoveHelperController(AnimusBlock slimeIn) {
            super(slimeIn);
            this.slime = slimeIn;
            this.yRot = 180.0F * slimeIn.m_146908_() / (float) Math.PI;
        }

        public void setDirection(float yRotIn, boolean aggressive) {
            this.yRot = yRotIn;
            this.isAggressive = aggressive;
        }

        public void setSpeed(double speedIn) {
            this.f_24978_ = speedIn;
            this.f_24981_ = MoveControl.Operation.MOVE_TO;
        }

        @Override
        public void tick() {
            this.f_24974_.m_146922_(this.m_24991_(this.f_24974_.m_146908_(), this.yRot, 90.0F));
            this.f_24974_.f_20885_ = this.f_24974_.m_146908_();
            this.f_24974_.f_20883_ = this.f_24974_.m_146908_();
            if (this.f_24981_ != MoveControl.Operation.MOVE_TO) {
                this.f_24974_.setZza(0.0F);
            } else {
                this.f_24981_ = MoveControl.Operation.WAIT;
                if (this.f_24974_.m_20096_()) {
                    this.f_24974_.setSpeed((float) (this.f_24978_ * this.f_24974_.m_21133_(Attributes.MOVEMENT_SPEED)));
                    if (this.jumpDelay-- <= 0) {
                        this.jumpDelay = this.slime.getJumpDelay();
                        if (this.isAggressive) {
                            this.jumpDelay /= 3;
                        }
                        this.slime.m_21569_().jump();
                        this.slime.m_5496_(this.slime.getJumpSound(), this.slime.m_6121_(), 1.0F);
                    } else {
                        this.slime.f_20900_ = 0.0F;
                        this.slime.f_20902_ = 0.0F;
                        this.f_24974_.setSpeed(0.0F);
                    }
                } else {
                    this.f_24974_.setSpeed((float) (this.f_24978_ * this.f_24974_.m_21133_(Attributes.MOVEMENT_SPEED)));
                }
            }
        }
    }
}