package se.mickelus.tetra.blocks.forged.transfer;

import java.util.Optional;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.mutil.util.TileEntityOptional;
import se.mickelus.tetra.blocks.IHeatTransfer;
import se.mickelus.tetra.items.cell.ThermalCellItem;

@ParametersAreNonnullByDefault
public class TransferUnitBlockEntity extends BlockEntity implements IHeatTransfer {

    private static final int baseAmount = 8;

    public static RegistryObject<BlockEntityType<TransferUnitBlockEntity>> type;

    private ItemStack cell;

    private float efficiency = 1.0F;

    public TransferUnitBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(type.get(), blockPos0, blockState1);
        this.cell = ItemStack.EMPTY;
    }

    public static void writeCell(CompoundTag compound, ItemStack cell) {
        if (!cell.isEmpty()) {
            CompoundTag cellNBT = new CompoundTag();
            cell.save(cellNBT);
            compound.put("cell", cellNBT);
        }
    }

    @Override
    public boolean canRecieve() {
        return TransferUnitBlock.getEffectPowered(this.f_58857_, this.f_58858_, this.m_58900_()).equals(EnumTransferConfig.receive) && this.hasCell() && this.getCharge() < 128;
    }

    @Override
    public boolean canSend() {
        return TransferUnitBlock.getEffectPowered(this.f_58857_, this.f_58858_, this.m_58900_()).equals(EnumTransferConfig.send) && this.hasCell() && this.getCharge() > 0;
    }

    @Override
    public boolean isReceiving() {
        return TransferUnitBlock.isReceiving(this.m_58900_());
    }

    @Override
    public void setReceiving(boolean receiving) {
        TransferUnitBlock.setReceiving(this.f_58857_, this.f_58858_, this.m_58900_(), receiving);
    }

    @Override
    public boolean isSending() {
        return TransferUnitBlock.isSending(this.m_58900_());
    }

    @Override
    public void setSending(boolean sending) {
        TransferUnitBlock.setSending(this.f_58857_, this.f_58858_, this.m_58900_(), sending);
    }

    public boolean hasCell() {
        return !this.cell.isEmpty();
    }

    public ItemStack removeCell() {
        ItemStack removedCell = this.cell;
        this.cell = ItemStack.EMPTY;
        TransferUnitBlock.updateCellProp(this.f_58857_, this.f_58858_, this.hasCell(), this.getCharge());
        this.updateTransferState();
        return removedCell;
    }

    public ItemStack getCell() {
        return this.cell;
    }

    public boolean putCell(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ThermalCellItem) {
            this.cell = itemStack;
            TransferUnitBlock.updateCellProp(this.f_58857_, this.f_58858_, this.hasCell(), this.getCharge());
            this.updateTransferState();
            return true;
        } else {
            return false;
        }
    }

    private Optional<IHeatTransfer> getConnectedUnit() {
        return TileEntityOptional.from(this.f_58857_, this.f_58858_.relative(TransferUnitBlock.getFacing(this.m_58900_())), IHeatTransfer.class);
    }

    @Override
    public int getCharge() {
        return (Integer) CastOptional.cast(this.cell.getItem(), ThermalCellItem.class).map(item -> ThermalCellItem.getCharge(this.cell)).orElse(0);
    }

    @Override
    public float getEfficiency() {
        return TransferUnitBlock.hasPlate(this.m_58900_()) ? 1.0F : 0.9F;
    }

    @Override
    public int getReceiveLimit() {
        return 8;
    }

    @Override
    public int getSendLimit() {
        return 8;
    }

    @Override
    public int drain(int amount) {
        return (Integer) CastOptional.cast(this.cell.getItem(), ThermalCellItem.class).map(item -> {
            int drained = ThermalCellItem.drainCharge(this.cell, amount);
            if (ThermalCellItem.getCharge(this.cell) == 0) {
                TransferUnitBlock.updateCellProp(this.f_58857_, this.f_58858_, this.hasCell(), 0);
                this.runDrainedEffects();
            }
            return drained;
        }).orElse(0);
    }

    @Override
    public int fill(int amount) {
        return (Integer) CastOptional.cast(this.cell.getItem(), ThermalCellItem.class).map(item -> {
            int initialCharge = ThermalCellItem.getCharge(this.cell);
            int overfill = ThermalCellItem.recharge(this.cell, amount);
            if (ThermalCellItem.getCharge(this.cell) == 128) {
                this.runFilledEffects();
            }
            if (initialCharge == 0) {
                TransferUnitBlock.updateCellProp(this.f_58857_, this.f_58858_, this.hasCell(), this.getCharge());
            }
            this.m_6596_();
            return overfill;
        }).orElse(0);
    }

    public void serverTick(Level level, BlockPos pos, BlockState blockState) {
        if (level.getGameTime() % 5L == 0L && TransferUnitBlock.isSending(blockState)) {
            this.transfer();
        }
    }

    public void transfer() {
        if (this.canSend()) {
            this.getConnectedUnit().ifPresent(connected -> {
                if (connected.canRecieve()) {
                    int amount = this.drain(8);
                    int overfill = connected.fill((int) ((float) amount * this.efficiency));
                    if (overfill > 0) {
                        this.fill(overfill);
                    }
                    this.m_6596_();
                } else {
                    this.setSending(false);
                    connected.setReceiving(false);
                }
            });
        } else {
            this.getConnectedUnit().ifPresent(connected -> connected.setReceiving(false));
            this.setSending(false);
        }
    }

    private void runDrainedEffects() {
        if (this.f_58857_ instanceof ServerLevel) {
            ((ServerLevel) this.f_58857_).sendParticles(ParticleTypes.SMOKE, (double) this.f_58858_.m_123341_() + 0.5, (double) this.f_58858_.m_123342_() + 0.7, (double) this.f_58858_.m_123343_() + 0.5, 10, 0.0, 0.0, 0.0, 0.02F);
            this.f_58857_.playSound(null, this.f_58858_, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.2F, 1.0F);
        }
    }

    private void runFilledEffects() {
        if (this.f_58857_ instanceof ServerLevel) {
            ((ServerLevel) this.f_58857_).sendParticles(ParticleTypes.FLAME, (double) this.f_58858_.m_123341_() + 0.5, (double) this.f_58858_.m_123342_() + 0.7, (double) this.f_58858_.m_123343_() + 0.5, 5, 0.0, 0.0, 0.0, 0.02F);
            this.f_58857_.playSound(null, this.f_58858_, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.2F, 1.0F);
        }
    }

    @Override
    public void updateTransferState() {
        switch(TransferUnitBlock.getEffectPowered(this.f_58857_, this.f_58858_, this.m_58900_())) {
            case send:
                this.getConnectedUnit().ifPresent(connected -> {
                    boolean canTransfer = this.canSend() && connected.canRecieve();
                    this.setReceiving(false);
                    this.setSending(canTransfer);
                    connected.setReceiving(canTransfer);
                    this.efficiency = this.getEfficiency() * connected.getEfficiency();
                });
                break;
            case receive:
                this.getConnectedUnit().ifPresent(connected -> {
                    if (this.isSending()) {
                        this.setSending(false);
                    }
                    if (connected.canSend()) {
                        connected.updateTransferState();
                    }
                });
                break;
            case redstone:
                this.getConnectedUnit().ifPresent(connected -> {
                    connected.setSending(false);
                    connected.setReceiving(false);
                    this.setSending(false);
                    this.setReceiving(false);
                });
        }
        this.m_6596_();
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        if (compound.contains("cell")) {
            this.cell = ItemStack.of(compound.getCompound("cell"));
        } else {
            this.cell = ItemStack.EMPTY;
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        writeCell(compound, this.cell);
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
        super.onDataPacket(net, packet);
    }
}