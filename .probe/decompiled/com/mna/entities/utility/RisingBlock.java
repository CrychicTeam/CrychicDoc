package com.mna.entities.utility;

import com.mna.entities.EntityInit;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.CrashReportCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

public class RisingBlock extends Entity implements IEntityAdditionalSpawnData {

    private static final EntityDataAccessor<Integer> MAXAGE = SynchedEntityData.defineId(RisingBlock.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Float> SPEED = SynchedEntityData.defineId(RisingBlock.class, EntityDataSerializers.FLOAT);

    protected static final EntityDataAccessor<BlockPos> DATA_START_POS = SynchedEntityData.defineId(RisingBlock.class, EntityDataSerializers.BLOCK_POS);

    public BlockState blockState = Blocks.SAND.defaultBlockState();

    public int time;

    public boolean dropItem = true;

    private long removeAtMillis;

    @Nullable
    public CompoundTag blockData;

    public RisingBlock(Level worldIn, double x, double y, double z, BlockState fallingBlockState) {
        super(EntityInit.RISING_BLOCK_ENTITY.get(), worldIn);
        this.f_19850_ = true;
        this.m_6034_(x, y + (double) ((1.0F - this.m_20206_()) / 2.0F), z);
        this.f_19854_ = x;
        this.f_19855_ = y;
        this.f_19856_ = z;
        this.setStartPos(this.m_20183_());
        this.blockState = fallingBlockState;
        this.m_20242_(true);
        this.m_20334_(0.0, 0.5, 0.0);
        this.f_19812_ = true;
    }

    public RisingBlock(EntityType<? extends RisingBlock> type, Level world) {
        super(type, world);
    }

    @Override
    public void tick() {
        if (this.blockState.m_60795_()) {
            this.m_146870_();
        } else if (this.m_9236_().isClientSide() && this.removeAtMillis > 0L) {
            if (System.currentTimeMillis() >= this.removeAtMillis) {
                super.setRemoved(Entity.RemovalReason.DISCARDED);
            }
        } else {
            Block block = this.blockState.m_60734_();
            if (this.time++ == 0) {
                BlockPos blockpos = this.m_20183_();
                if (this.m_9236_().getBlockState(blockpos).m_60713_(block)) {
                    if (block instanceof DoorBlock) {
                        BlockState below = this.m_9236_().getBlockState(blockpos.below());
                        if (below.m_60734_() == block) {
                            this.m_9236_().setBlock(blockpos.below(), Blocks.AIR.defaultBlockState(), 16);
                        }
                        this.m_9236_().setBlock(blockpos, Blocks.AIR.defaultBlockState(), 16);
                    } else {
                        this.m_9236_().m_46961_(blockpos, false);
                    }
                } else if (!this.m_9236_().isClientSide()) {
                    this.m_146870_();
                    return;
                }
            }
            List<Entity> entitiesAbove = this.m_9236_().m_45933_(this, this.m_20191_().expandTowards(0.0, 1.0, 0.0));
            entitiesAbove.forEach(p -> this.moveEntity(p));
            this.m_6478_(MoverType.SELF, this.m_20184_());
            if (!this.m_9236_().isClientSide()) {
                BlockPos myPosition = this.m_20183_();
                if (this.time > this.f_19804_.get(MAXAGE)) {
                    this.f_19863_ = true;
                }
                if (!this.f_19863_) {
                    if (!this.m_9236_().isClientSide() && (this.time > 100 && (myPosition.m_123342_() < 1 || myPosition.m_123342_() > 256) || this.time > 600)) {
                        if (this.dropItem && this.m_9236_().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                            this.m_19998_(block);
                        }
                        this.m_146870_();
                    }
                } else {
                    this.restoreBlock(myPosition, block);
                }
            }
        }
    }

    private void moveEntity(Entity p) {
        p.setOnGround(true);
        if (p.getDeltaMovement().y < this.m_20184_().y) {
            p.setDeltaMovement(p.getDeltaMovement().x, this.m_20184_().y, p.getDeltaMovement().z);
        }
    }

    private void restoreBlock(BlockPos myPosition, Block block) {
        BlockState blockstate = this.m_9236_().getBlockState(myPosition);
        this.m_20256_(this.m_20184_().multiply(0.7, 0.5, 0.7));
        if (!blockstate.m_60713_(Blocks.MOVING_PISTON)) {
            this.m_146870_();
            if (block instanceof DoorBlock && this.blockState.m_61138_(DoorBlock.HALF)) {
                this.blockState = (BlockState) this.blockState.m_61124_(DoorBlock.HALF, DoubleBlockHalf.LOWER);
            }
            boolean flag2 = blockstate.m_60629_(new DirectionalPlaceContext(this.m_9236_(), myPosition, Direction.UP, ItemStack.EMPTY, Direction.UP));
            boolean flag4 = this.blockState.m_60710_(this.m_9236_(), myPosition);
            if (flag2 && flag4) {
                if (this.blockState.m_61138_(BlockStateProperties.WATERLOGGED) && this.m_9236_().getFluidState(myPosition).getType() == Fluids.WATER) {
                    this.blockState = (BlockState) this.blockState.m_61124_(BlockStateProperties.WATERLOGGED, true);
                }
                if (this.m_9236_().setBlock(myPosition, this.blockState, 3)) {
                    if (this.blockData != null && this.blockState.m_60734_() instanceof EntityBlock) {
                        BlockEntity tileentity = this.m_9236_().getBlockEntity(myPosition);
                        if (tileentity != null) {
                            CompoundTag compoundnbt = tileentity.saveWithFullMetadata();
                            for (String s : this.blockData.getAllKeys()) {
                                Tag inbt = this.blockData.get(s);
                                if (!"x".equals(s) && !"y".equals(s) && !"z".equals(s)) {
                                    compoundnbt.put(s, inbt.copy());
                                }
                            }
                            tileentity.load(compoundnbt);
                            tileentity.setChanged();
                        }
                    }
                } else if (this.dropItem && this.m_9236_().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                    this.m_19998_(block);
                }
            } else if (this.dropItem && this.m_9236_().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                this.m_19998_(block);
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(DATA_START_POS, BlockPos.ZERO);
        this.f_19804_.define(MAXAGE, 50);
        this.f_19804_.define(SPEED, 0.05F);
    }

    public void setMaxAge(int age) {
        this.f_19804_.set(MAXAGE, age);
    }

    public void setSpeed(float speed) {
        this.f_19804_.set(SPEED, speed);
        this.m_20334_(0.0, (double) speed, 0.0);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt(Block.getId(this.getBlockState()));
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        int stateId = additionalData.readInt();
        this.blockState = Block.stateById(stateId);
        if (this.blockState == null) {
            this.blockState = Blocks.SAND.defaultBlockState();
        }
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
    protected void addAdditionalSaveData(CompoundTag compoundTag0) {
        compoundTag0.put("BlockState", NbtUtils.writeBlockState(this.blockState));
        compoundTag0.putInt("Time", this.time);
        compoundTag0.putBoolean("DropItem", this.dropItem);
        if (this.blockData != null) {
            compoundTag0.put("TileEntityData", this.blockData);
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag0) {
        this.blockState = NbtUtils.readBlockState(this.m_9236_().m_246945_(Registries.BLOCK), compoundTag0.getCompound("BlockState"));
        this.time = compoundTag0.getInt("Time");
        if (compoundTag0.contains("DropItem", 99)) {
            this.dropItem = compoundTag0.getBoolean("DropItem");
        }
        if (compoundTag0.contains("TileEntityData", 10)) {
            this.blockData = compoundTag0.getCompound("TileEntityData");
        }
        if (this.blockState.m_60795_()) {
            this.blockState = Blocks.SAND.defaultBlockState();
        }
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
    public boolean onlyOpCanSetNbt() {
        return true;
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket clientboundAddEntityPacket0) {
        super.recreateFromPacket(clientboundAddEntityPacket0);
        this.blockState = Block.stateById(clientboundAddEntityPacket0.getData());
        this.f_19850_ = true;
        double d0 = clientboundAddEntityPacket0.getX();
        double d1 = clientboundAddEntityPacket0.getY();
        double d2 = clientboundAddEntityPacket0.getZ();
        this.m_6034_(d0, d1 + (double) ((1.0F - this.m_20206_()) / 2.0F), d2);
        this.setStartPos(this.m_20183_());
    }
}