package com.mna.entities.utility;

import com.mna.entities.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

public class StationaryBlock extends FallingBlockEntity implements IEntityAdditionalSpawnData {

    private static final EntityDataAccessor<Integer> MAXAGE = SynchedEntityData.defineId(StationaryBlock.class, EntityDataSerializers.INT);

    public StationaryBlock(Level worldIn, double x, double y, double z, BlockState fallingBlockState) {
        this(EntityInit.STATIONARY_BLOCK_ENTITY.get(), worldIn);
        this.f_19850_ = true;
        this.m_6034_(x, y + (double) ((1.0F - this.m_20206_()) / 2.0F), z);
        this.f_19854_ = x;
        this.f_19855_ = y;
        this.f_19856_ = z;
        this.m_31959_(this.m_20183_());
        this.f_31946_ = fallingBlockState;
        this.m_20242_(true);
        this.m_20334_(0.0, 0.0, 0.0);
    }

    public StationaryBlock(EntityType<? extends FallingBlockEntity> type, Level world) {
        super(type, world);
    }

    @Override
    public void tick() {
        if (this.f_31946_.m_60795_()) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        } else {
            Block block = this.f_31946_.m_60734_();
            if (this.f_31942_++ == 0) {
                BlockPos blockpos = this.m_20183_();
                if (this.m_9236_().getBlockState(blockpos).m_60713_(block)) {
                    this.m_9236_().removeBlock(blockpos, false);
                } else if (!this.m_9236_().isClientSide()) {
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                    return;
                }
            }
            if (this.f_31942_ > this.f_19804_.get(MAXAGE)) {
                BlockPos myPosition = this.m_20183_();
                this.restoreBlock(myPosition, block);
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    private void restoreBlock(BlockPos myPosition, Block block) {
        BlockState blockstate = this.m_9236_().getBlockState(myPosition);
        this.m_20256_(this.m_20184_().multiply(0.7, 0.5, 0.7));
        if (!blockstate.m_60713_(Blocks.MOVING_PISTON)) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
            boolean flag2 = blockstate.m_60629_(new DirectionalPlaceContext(this.m_9236_(), myPosition, Direction.UP, ItemStack.EMPTY, Direction.UP));
            boolean flag4 = this.f_31946_.m_60710_(this.m_9236_(), myPosition);
            if (flag2 && flag4) {
                if (this.f_31946_.m_61138_(BlockStateProperties.WATERLOGGED) && this.m_9236_().getFluidState(myPosition).getType() == Fluids.WATER) {
                    this.f_31946_ = (BlockState) this.f_31946_.m_61124_(BlockStateProperties.WATERLOGGED, true);
                }
                if (this.m_9236_().setBlock(myPosition, this.f_31946_, 3)) {
                    if (block instanceof FallingBlock) {
                        ((FallingBlock) block).m_48792_(this.m_9236_(), myPosition, this.f_31946_, blockstate, this);
                    }
                    if (this.f_31944_ != null && this.f_31946_.m_155947_()) {
                        BlockEntity tileentity = this.m_9236_().getBlockEntity(myPosition);
                        if (tileentity != null) {
                            CompoundTag compoundnbt = tileentity.saveWithFullMetadata();
                            for (String s : this.f_31944_.getAllKeys()) {
                                Tag inbt = this.f_31944_.get(s);
                                if (!"x".equals(s) && !"y".equals(s) && !"z".equals(s)) {
                                    compoundnbt.put(s, inbt.copy());
                                }
                            }
                            tileentity.load(compoundnbt);
                            tileentity.setChanged();
                        }
                    }
                } else if (this.f_31943_ && this.m_9236_().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                    this.m_19998_(block);
                }
            } else if (this.f_31943_ && this.m_9236_().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                this.m_19998_(block);
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(MAXAGE, 50);
    }

    public void setMaxAge(int age) {
        this.f_19804_.set(MAXAGE, age);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt(Block.getId(this.m_31980_()));
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        int stateId = additionalData.readInt();
        this.f_31946_ = Block.stateById(stateId);
        if (this.f_31946_ == null) {
            this.f_31946_ = Blocks.SAND.defaultBlockState();
        }
    }
}