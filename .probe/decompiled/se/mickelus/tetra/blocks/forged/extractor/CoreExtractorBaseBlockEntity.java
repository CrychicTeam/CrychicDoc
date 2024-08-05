package se.mickelus.tetra.blocks.forged.extractor;

import java.util.Optional;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ObjectHolder;
import se.mickelus.mutil.util.TileEntityOptional;
import se.mickelus.tetra.blocks.IHeatTransfer;

@ParametersAreNonnullByDefault
public class CoreExtractorBaseBlockEntity extends BlockEntity implements IHeatTransfer {

    private static final int sendLimit = 4;

    private static final String chargeKey = "charge";

    private static final int maxCharge = 128;

    private static final int drainAmount = 4;

    @ObjectHolder(registryName = "block_entity_type", value = "tetra:core_extractor")
    public static BlockEntityType<CoreExtractorBaseBlockEntity> type;

    private boolean isSending = false;

    private int currentCharge = 0;

    private float efficiency;

    public CoreExtractorBaseBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(type, blockPos0, blockState1);
    }

    public boolean canRefill() {
        return this.getPiston().isPresent() && CoreExtractorPipeBlock.isPowered(this.f_58857_, this.f_58858_.below());
    }

    @Override
    public boolean isReceiving() {
        return false;
    }

    @Override
    public void setReceiving(boolean receiving) {
        if (receiving) {
            this.isSending = false;
        }
        this.notifyBlockUpdate();
    }

    @Override
    public boolean canRecieve() {
        return false;
    }

    @Override
    public boolean isSending() {
        return this.isSending;
    }

    @Override
    public void setSending(boolean sending) {
        this.isSending = sending;
        this.notifyBlockUpdate();
    }

    @Override
    public boolean canSend() {
        return this.currentCharge > 0 || this.canRefill();
    }

    @Override
    public int getReceiveLimit() {
        return 0;
    }

    @Override
    public int getSendLimit() {
        return 4;
    }

    @Override
    public int drain(int amount) {
        if (amount > this.currentCharge) {
            int drained = this.currentCharge;
            this.currentCharge = 0;
            return drained;
        } else {
            this.currentCharge -= amount;
            return amount;
        }
    }

    @Override
    public int fill(int amount) {
        if (amount + this.currentCharge > 128) {
            int overfill = amount + this.currentCharge - 128;
            this.currentCharge = 128;
            return overfill;
        } else {
            this.currentCharge += amount;
            this.updateTransferState();
            return 0;
        }
    }

    @Override
    public int getCharge() {
        return this.currentCharge;
    }

    @Override
    public float getEfficiency() {
        return 1.0F;
    }

    @Override
    public void updateTransferState() {
        this.getConnectedUnit().ifPresent(connected -> {
            boolean canSend = this.currentCharge > 0;
            boolean canRecieve = connected.canRecieve();
            this.setSending(canSend && canRecieve);
            connected.setReceiving(canSend && canRecieve);
            this.efficiency = this.getEfficiency() * connected.getEfficiency();
            if (!canSend && canRecieve && this.canRefill()) {
                this.getPiston().ifPresent(CoreExtractorPistonBlockEntity::activate);
            }
        });
    }

    public void transfer() {
        this.getConnectedUnit().ifPresent(connected -> {
            if (connected.canRecieve()) {
                if (this.currentCharge > 0) {
                    int amount = this.drain(Math.min(this.getSendLimit(), connected.getReceiveLimit()));
                    int overfill = connected.fill((int) ((float) amount * this.efficiency));
                    if (overfill > 0) {
                        this.fill(overfill);
                    }
                    this.m_6596_();
                } else {
                    this.setSending(false);
                    connected.setReceiving(false);
                    this.notifyBlockUpdate();
                }
                if (this.canRefill()) {
                    this.getPiston().ifPresent(CoreExtractorPistonBlockEntity::activate);
                }
            } else {
                this.setSending(false);
                connected.setReceiving(false);
                this.notifyBlockUpdate();
            }
        });
    }

    private void notifyBlockUpdate() {
        this.m_6596_();
        BlockState state = this.f_58857_.getBlockState(this.f_58858_);
        this.f_58857_.sendBlockUpdated(this.f_58858_, state, state, 3);
    }

    public Direction getFacing() {
        return (Direction) this.m_58900_().m_61143_(CoreExtractorBaseBlock.facingProp);
    }

    private Optional<IHeatTransfer> getConnectedUnit() {
        return TileEntityOptional.from(this.f_58857_, this.f_58858_.relative(this.getFacing()), IHeatTransfer.class);
    }

    private Optional<CoreExtractorPistonBlockEntity> getPiston() {
        return TileEntityOptional.from(this.f_58857_, this.f_58858_.relative(Direction.UP), CoreExtractorPistonBlockEntity.class);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        if (compound.contains("charge")) {
            this.currentCharge = compound.getInt("charge");
        } else {
            this.currentCharge = 0;
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("charge", this.currentCharge);
    }

    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
        this.load(packet.getTag());
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (!level.isClientSide) {
            if (this.isSending) {
                if (level.getGameTime() % 5L == 0L) {
                    this.transfer();
                }
            } else if (this.currentCharge > 0 && level.getGameTime() % 20L == 0L) {
                this.currentCharge = Math.max(0, this.currentCharge - 4);
            }
        }
    }
}