package se.mickelus.tetra.blocks.rack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ObjectHolder;

@ParametersAreNonnullByDefault
public class RackTile extends BlockEntity {

    public static final String unlocalizedName = "rack";

    public static final int inventorySize = 2;

    private static final String inventoryKey = "inv";

    @ObjectHolder(registryName = "block_entity_type", value = "tetra:rack")
    public static BlockEntityType<RackTile> type;

    private final LazyOptional<ItemStackHandler> handler = LazyOptional.of(() -> new ItemStackHandler(2) {

        @Override
        protected void onContentsChanged(int slot) {
            RackTile.this.m_6596_();
            RackTile.this.f_58857_.sendBlockUpdated(RackTile.this.f_58858_, RackTile.this.m_58900_(), RackTile.this.m_58900_(), 3);
        }
    });

    public RackTile(BlockPos blockPos0, BlockState blockState1) {
        super(type, blockPos0, blockState1);
    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == ForgeCapabilities.ITEM_HANDLER ? this.handler.cast() : super.getCapability(cap, side);
    }

    public void slotInteract(int slot, Player playerEntity, InteractionHand hand) {
        this.handler.ifPresent(handler -> {
            ItemStack slotStack = handler.getStackInSlot(slot);
            ItemStack heldStack = playerEntity.m_21120_(hand);
            if (slotStack.isEmpty()) {
                ItemStack remainder = handler.insertItem(slot, heldStack.copy(), false);
                playerEntity.m_21008_(hand, remainder);
                playerEntity.playSound(SoundEvents.WOOD_PLACE, 0.5F, 0.7F);
            } else {
                ItemStack extractedStack = handler.extractItem(slot, handler.getSlotLimit(slot), false);
                if (playerEntity.getInventory().add(extractedStack)) {
                    playerEntity.playSound(SoundEvents.ITEM_PICKUP, 0.5F, 1.0F);
                } else {
                    playerEntity.drop(extractedStack, false);
                }
            }
        });
    }

    public AABB getRenderBoundingBox() {
        return Shapes.block().bounds().move(this.f_58858_);
    }

    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.handler.ifPresent(handler -> handler.deserializeNBT(compound.getCompound("inv")));
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        this.handler.ifPresent(handler -> compound.put("inv", handler.serializeNBT()));
    }
}