package team.lodestar.lodestone.systems.blockentity;

import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

public abstract class ItemHolderBlockEntity extends LodestoneBlockEntity {

    public LodestoneBlockEntityInventory inventory;

    public ItemHolderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public InteractionResult onUse(Player player, InteractionHand hand) {
        this.inventory.interact(player.m_9236_(), player, hand);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onBreak(@Nullable Player player) {
        this.inventory.dumpItems(this.f_58857_, this.f_58858_);
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        this.inventory.save(compound);
        super.m_183515_(compound);
    }

    @Override
    public void load(CompoundTag compound) {
        this.inventory.load(compound);
        super.load(compound);
    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        return cap == ForgeCapabilities.ITEM_HANDLER ? this.inventory.inventoryOptional.cast() : super.getCapability(cap);
    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        return cap == ForgeCapabilities.ITEM_HANDLER ? this.inventory.inventoryOptional.cast() : super.getCapability(cap, side);
    }
}