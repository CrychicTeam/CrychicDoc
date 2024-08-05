package net.minecraft.world.entity.item;

import com.mojang.logging.LogUtils;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.CrashReportCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ConcretePowderBlock;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

public class FallingBlockEntity extends Entity {

    private static final Logger LOGGER = LogUtils.getLogger();

    private BlockState blockState = Blocks.SAND.defaultBlockState();

    public int time;

    public boolean dropItem = true;

    private boolean cancelDrop;

    private boolean hurtEntities;

    private int fallDamageMax = 40;

    private float fallDamagePerDistance;

    @Nullable
    public CompoundTag blockData;

    protected static final EntityDataAccessor<BlockPos> DATA_START_POS = SynchedEntityData.defineId(FallingBlockEntity.class, EntityDataSerializers.BLOCK_POS);

    public FallingBlockEntity(EntityType<? extends FallingBlockEntity> entityTypeExtendsFallingBlockEntity0, Level level1) {
        super(entityTypeExtendsFallingBlockEntity0, level1);
    }

    private FallingBlockEntity(Level level0, double double1, double double2, double double3, BlockState blockState4) {
        this(EntityType.FALLING_BLOCK, level0);
        this.blockState = blockState4;
        this.f_19850_ = true;
        this.m_6034_(double1, double2, double3);
        this.m_20256_(Vec3.ZERO);
        this.f_19854_ = double1;
        this.f_19855_ = double2;
        this.f_19856_ = double3;
        this.setStartPos(this.m_20183_());
    }

    public static FallingBlockEntity fall(Level level0, BlockPos blockPos1, BlockState blockState2) {
        FallingBlockEntity $$3 = new FallingBlockEntity(level0, (double) blockPos1.m_123341_() + 0.5, (double) blockPos1.m_123342_(), (double) blockPos1.m_123343_() + 0.5, blockState2.m_61138_(BlockStateProperties.WATERLOGGED) ? (BlockState) blockState2.m_61124_(BlockStateProperties.WATERLOGGED, false) : blockState2);
        level0.setBlock(blockPos1, blockState2.m_60819_().createLegacyBlock(), 3);
        level0.m_7967_($$3);
        return $$3;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    public void setStartPos(BlockPos blockPos0) {
        this.f_19804_.set(DATA_START_POS, blockPos0);
    }

    public BlockPos getStartPos() {
        return this.f_19804_.get(DATA_START_POS);
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(DATA_START_POS, BlockPos.ZERO);
    }

    @Override
    public boolean isPickable() {
        return !this.m_213877_();
    }

    @Override
    public void tick() {
        if (this.blockState.m_60795_()) {
            this.m_146870_();
        } else {
            Block $$0 = this.blockState.m_60734_();
            this.time++;
            if (!this.m_20068_()) {
                this.m_20256_(this.m_20184_().add(0.0, -0.04, 0.0));
            }
            this.m_6478_(MoverType.SELF, this.m_20184_());
            if (!this.m_9236_().isClientSide) {
                BlockPos $$1 = this.m_20183_();
                boolean $$2 = this.blockState.m_60734_() instanceof ConcretePowderBlock;
                boolean $$3 = $$2 && this.m_9236_().getFluidState($$1).is(FluidTags.WATER);
                double $$4 = this.m_20184_().lengthSqr();
                if ($$2 && $$4 > 1.0) {
                    BlockHitResult $$5 = this.m_9236_().m_45547_(new ClipContext(new Vec3(this.f_19854_, this.f_19855_, this.f_19856_), this.m_20182_(), ClipContext.Block.COLLIDER, ClipContext.Fluid.SOURCE_ONLY, this));
                    if ($$5.getType() != HitResult.Type.MISS && this.m_9236_().getFluidState($$5.getBlockPos()).is(FluidTags.WATER)) {
                        $$1 = $$5.getBlockPos();
                        $$3 = true;
                    }
                }
                if (this.m_20096_() || $$3) {
                    BlockState $$6 = this.m_9236_().getBlockState($$1);
                    this.m_20256_(this.m_20184_().multiply(0.7, -0.5, 0.7));
                    if (!$$6.m_60713_(Blocks.MOVING_PISTON)) {
                        if (!this.cancelDrop) {
                            boolean $$7 = $$6.m_60629_(new DirectionalPlaceContext(this.m_9236_(), $$1, Direction.DOWN, ItemStack.EMPTY, Direction.UP));
                            boolean $$8 = FallingBlock.isFree(this.m_9236_().getBlockState($$1.below())) && (!$$2 || !$$3);
                            boolean $$9 = this.blockState.m_60710_(this.m_9236_(), $$1) && !$$8;
                            if ($$7 && $$9) {
                                if (this.blockState.m_61138_(BlockStateProperties.WATERLOGGED) && this.m_9236_().getFluidState($$1).getType() == Fluids.WATER) {
                                    this.blockState = (BlockState) this.blockState.m_61124_(BlockStateProperties.WATERLOGGED, true);
                                }
                                if (this.m_9236_().setBlock($$1, this.blockState, 3)) {
                                    ((ServerLevel) this.m_9236_()).getChunkSource().chunkMap.broadcast(this, new ClientboundBlockUpdatePacket($$1, this.m_9236_().getBlockState($$1)));
                                    this.m_146870_();
                                    if ($$0 instanceof Fallable) {
                                        ((Fallable) $$0).onLand(this.m_9236_(), $$1, this.blockState, $$6, this);
                                    }
                                    if (this.blockData != null && this.blockState.m_155947_()) {
                                        BlockEntity $$10 = this.m_9236_().getBlockEntity($$1);
                                        if ($$10 != null) {
                                            CompoundTag $$11 = $$10.saveWithoutMetadata();
                                            for (String $$12 : this.blockData.getAllKeys()) {
                                                $$11.put($$12, this.blockData.get($$12).copy());
                                            }
                                            try {
                                                $$10.load($$11);
                                            } catch (Exception var15) {
                                                LOGGER.error("Failed to load block entity from falling block", var15);
                                            }
                                            $$10.setChanged();
                                        }
                                    }
                                } else if (this.dropItem && this.m_9236_().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                                    this.m_146870_();
                                    this.callOnBrokenAfterFall($$0, $$1);
                                    this.m_19998_($$0);
                                }
                            } else {
                                this.m_146870_();
                                if (this.dropItem && this.m_9236_().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                                    this.callOnBrokenAfterFall($$0, $$1);
                                    this.m_19998_($$0);
                                }
                            }
                        } else {
                            this.m_146870_();
                            this.callOnBrokenAfterFall($$0, $$1);
                        }
                    }
                } else if (!this.m_9236_().isClientSide && (this.time > 100 && ($$1.m_123342_() <= this.m_9236_().m_141937_() || $$1.m_123342_() > this.m_9236_().m_151558_()) || this.time > 600)) {
                    if (this.dropItem && this.m_9236_().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                        this.m_19998_($$0);
                    }
                    this.m_146870_();
                }
            }
            this.m_20256_(this.m_20184_().scale(0.98));
        }
    }

    public void callOnBrokenAfterFall(Block block0, BlockPos blockPos1) {
        if (block0 instanceof Fallable) {
            ((Fallable) block0).onBrokenAfterFall(this.m_9236_(), blockPos1, this);
        }
    }

    @Override
    public boolean causeFallDamage(float float0, float float1, DamageSource damageSource2) {
        if (!this.hurtEntities) {
            return false;
        } else {
            int $$3 = Mth.ceil(float0 - 1.0F);
            if ($$3 < 0) {
                return false;
            } else {
                Predicate<Entity> $$4 = EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(EntitySelector.LIVING_ENTITY_STILL_ALIVE);
                DamageSource $$6 = this.blockState.m_60734_() instanceof Fallable $$5 ? $$5.getFallDamageSource(this) : this.m_269291_().fallingBlock(this);
                float $$7 = (float) Math.min(Mth.floor((float) $$3 * this.fallDamagePerDistance), this.fallDamageMax);
                this.m_9236_().getEntities(this, this.m_20191_(), $$4).forEach(p_149649_ -> p_149649_.hurt($$6, $$7));
                boolean $$8 = this.blockState.m_204336_(BlockTags.ANVIL);
                if ($$8 && $$7 > 0.0F && this.f_19796_.nextFloat() < 0.05F + (float) $$3 * 0.05F) {
                    BlockState $$9 = AnvilBlock.damage(this.blockState);
                    if ($$9 == null) {
                        this.cancelDrop = true;
                    } else {
                        this.blockState = $$9;
                    }
                }
                return false;
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag0) {
        compoundTag0.put("BlockState", NbtUtils.writeBlockState(this.blockState));
        compoundTag0.putInt("Time", this.time);
        compoundTag0.putBoolean("DropItem", this.dropItem);
        compoundTag0.putBoolean("HurtEntities", this.hurtEntities);
        compoundTag0.putFloat("FallHurtAmount", this.fallDamagePerDistance);
        compoundTag0.putInt("FallHurtMax", this.fallDamageMax);
        if (this.blockData != null) {
            compoundTag0.put("TileEntityData", this.blockData);
        }
        compoundTag0.putBoolean("CancelDrop", this.cancelDrop);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag0) {
        this.blockState = NbtUtils.readBlockState(this.m_9236_().m_246945_(Registries.BLOCK), compoundTag0.getCompound("BlockState"));
        this.time = compoundTag0.getInt("Time");
        if (compoundTag0.contains("HurtEntities", 99)) {
            this.hurtEntities = compoundTag0.getBoolean("HurtEntities");
            this.fallDamagePerDistance = compoundTag0.getFloat("FallHurtAmount");
            this.fallDamageMax = compoundTag0.getInt("FallHurtMax");
        } else if (this.blockState.m_204336_(BlockTags.ANVIL)) {
            this.hurtEntities = true;
        }
        if (compoundTag0.contains("DropItem", 99)) {
            this.dropItem = compoundTag0.getBoolean("DropItem");
        }
        if (compoundTag0.contains("TileEntityData", 10)) {
            this.blockData = compoundTag0.getCompound("TileEntityData");
        }
        this.cancelDrop = compoundTag0.getBoolean("CancelDrop");
        if (this.blockState.m_60795_()) {
            this.blockState = Blocks.SAND.defaultBlockState();
        }
    }

    public void setHurtsEntities(float float0, int int1) {
        this.hurtEntities = true;
        this.fallDamagePerDistance = float0;
        this.fallDamageMax = int1;
    }

    public void disableDrop() {
        this.cancelDrop = true;
    }

    @Override
    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    public void fillCrashReportCategory(CrashReportCategory crashReportCategory0) {
        super.fillCrashReportCategory(crashReportCategory0);
        crashReportCategory0.setDetail("Immitating BlockState", this.blockState.toString());
    }

    public BlockState getBlockState() {
        return this.blockState;
    }

    @Override
    protected Component getTypeName() {
        return Component.translatable("entity.minecraft.falling_block_type", this.blockState.m_60734_().getName());
    }

    @Override
    public boolean onlyOpCanSetNbt() {
        return true;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this, Block.getId(this.getBlockState()));
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket clientboundAddEntityPacket0) {
        super.recreateFromPacket(clientboundAddEntityPacket0);
        this.blockState = Block.stateById(clientboundAddEntityPacket0.getData());
        this.f_19850_ = true;
        double $$1 = clientboundAddEntityPacket0.getX();
        double $$2 = clientboundAddEntityPacket0.getY();
        double $$3 = clientboundAddEntityPacket0.getZ();
        this.m_6034_($$1, $$2, $$3);
        this.setStartPos(this.m_20183_());
    }
}