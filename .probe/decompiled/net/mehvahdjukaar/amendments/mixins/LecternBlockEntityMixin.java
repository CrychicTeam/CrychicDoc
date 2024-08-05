package net.mehvahdjukaar.amendments.mixins;

import net.mehvahdjukaar.amendments.common.LecternEditMenu;
import net.mehvahdjukaar.amendments.configs.CommonConfigs;
import net.mehvahdjukaar.amendments.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.WritableBookItem;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ LecternBlockEntity.class })
public abstract class LecternBlockEntityMixin extends BlockEntity implements Container {

    @Shadow
    @Final
    private ContainerData dataAccess;

    @Shadow
    @Final
    private Container bookAccess;

    @Shadow
    ItemStack book;

    @Shadow
    private int pageCount;

    protected LecternBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Shadow
    public abstract ItemStack getBook();

    @Shadow
    public abstract void setBook(ItemStack var1);

    @Shadow
    public abstract boolean hasBook();

    @Inject(method = { "createMenu" }, at = { @At("HEAD") }, cancellable = true)
    public void createEditMenu(int i, Inventory inventory, Player player, CallbackInfoReturnable<AbstractContainerMenu> cir) {
        if (this.getBook().getItem() instanceof WritableBookItem && (Boolean) CommonConfigs.LECTERN_STUFF.get()) {
            cir.setReturnValue(new LecternEditMenu(i, (LecternBlockEntity) this, this.dataAccess));
        }
    }

    @Inject(method = { "setPage" }, at = { @At("HEAD") })
    public void setPage(int page, CallbackInfo ci) {
        if (page >= this.pageCount && page <= 100 && this.book.getItem() instanceof WritableBookItem && (Boolean) CommonConfigs.LECTERN_STUFF.get()) {
            if (this.pageCount == 0) {
                this.pageCount += 2;
            } else {
                this.pageCount++;
            }
        }
    }

    @Override
    public int getContainerSize() {
        return this.bookAccess.getContainerSize();
    }

    @Override
    public boolean isEmpty() {
        return this.bookAccess.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.bookAccess.getItem(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return this.bookAccess.removeItem(slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return this.bookAccess.removeItemNoUpdate(slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.setBook(stack);
    }

    @Override
    public boolean stillValid(Player player) {
        return this.bookAccess.stillValid(player);
    }

    @Override
    public void clearContent() {
        this.bookAccess.m_6211_();
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return stack.is(ModTags.GOES_IN_LECTERN);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.f_58857_ != null) {
            BlockState state = this.m_58900_();
            if ((Boolean) state.m_61143_(LecternBlock.HAS_BOOK) != this.hasBook()) {
                LecternBlock.resetBookState(null, this.f_58857_, this.f_58858_, this.m_58900_(), this.hasBook());
            }
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}