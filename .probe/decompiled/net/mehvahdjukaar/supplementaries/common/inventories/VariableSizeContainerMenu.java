package net.mehvahdjukaar.supplementaries.common.inventories;

import net.mehvahdjukaar.moonlight.api.misc.IContainerProvider;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class VariableSizeContainerMenu extends AbstractContainerMenu implements IContainerProvider {

    public final Container inventory;

    public final int unlockedSlots;

    private static final int[][] TARGET_RATIOS = new int[][] { { 1, 1 }, { 2, 2 }, { 3, 2 }, { 3, 3 }, { 4, 2 }, { 5, 2 }, { 6, 2 }, { 7, 2 }, { 5, 3 }, { 8, 2 }, { 6, 3 }, { 7, 3 }, { 8, 3 }, { 9, 3 } };

    public static <C extends BlockEntity & Container & MenuProvider> void openTileMenu(Player player, C tile) {
        PlatHelper.openCustomMenu((ServerPlayer) player, tile, p -> {
            p.writeBoolean(true);
            p.writeBlockPos(tile.getBlockPos());
            p.writeInt(tile.getContainerSize());
        });
    }

    public static <C extends Entity & Container & MenuProvider> void openEntityMenu(Player player, C entity) {
        PlatHelper.openCustomMenu((ServerPlayer) player, entity, p -> {
            p.writeBoolean(false);
            p.writeVarInt(entity.getId());
            p.writeInt(entity.getContainerSize());
        });
    }

    @Override
    public Container getContainer() {
        return this.inventory;
    }

    public VariableSizeContainerMenu(int id, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        this(id, playerInventory, getContainerFromPacket(playerInventory, packetBuffer), packetBuffer.readInt());
    }

    @NotNull
    private static Container getContainerFromPacket(Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        boolean isBlockPos = packetBuffer.readBoolean();
        Level level = playerInventory.player.m_9236_();
        if (isBlockPos) {
            BlockPos pos = packetBuffer.readBlockPos();
            BlockEntity c = level.getBlockEntity(pos);
            if (c instanceof Container) {
                return (Container) c;
            }
        } else {
            Entity e = level.getEntity(packetBuffer.readVarInt());
            if (e instanceof Container) {
                return (Container) e;
            }
            if (e instanceof IContainerProvider c) {
                return c.getContainer();
            }
        }
        throw new UnsupportedOperationException("Cannot find container associated with entity ");
    }

    public VariableSizeContainerMenu(int id, Inventory playerInventory, Container container, int unlockedSlots) {
        super((MenuType<?>) ModMenuTypes.VARIABLE_SIZE.get(), id);
        this.inventory = container;
        this.unlockedSlots = unlockedSlots;
        m_38869_(container, unlockedSlots);
        container.startOpen(playerInventory.player);
        int[] dims = getRatio(unlockedSlots);
        if (dims[0] > 9) {
            dims[0] = 9;
            dims[1] = (int) Math.ceil((double) ((float) unlockedSlots / 9.0F));
        }
        int yp = 44 - 9 * dims[1];
        int dimx = 0;
        for (int h = 0; h < dims[1]; h++) {
            int dimXPrev = dimx;
            dimx = Math.min(dims[0], unlockedSlots);
            int xp = 89 - dimx * 18 / 2;
            for (int j = 0; j < dimx; j++) {
                this.m_38897_(new DelegatingSlot(container, j + h * dimXPrev, xp + j * 18, yp + 18 * h, this));
            }
            unlockedSlots -= dims[0];
        }
        for (int si = 0; si < 3; si++) {
            for (int sj = 0; sj < 9; sj++) {
                this.m_38897_(new Slot(playerInventory, sj + (si + 1) * 9, 8 + sj * 18, 84 + si * 18));
            }
        }
        for (int si = 0; si < 9; si++) {
            this.m_38897_(new Slot(playerInventory, si, 8 + si * 18, 142));
        }
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return this.inventory.stillValid(playerIn);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(index);
        if (slot.hasItem()) {
            ItemStack item = slot.getItem();
            itemstack = item.copy();
            int activeSlots = (Integer) CommonConfigs.Functional.SACK_SLOTS.get();
            if (index < activeSlots) {
                if (!this.m_38903_(item, activeSlots, this.f_38839_.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_(item, 0, activeSlots, false)) {
                return ItemStack.EMPTY;
            }
            if (item.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        this.inventory.stopOpen(playerIn);
    }

    public static int[] getRatio(int maxSize) {
        int[] dims = new int[] { Math.min(maxSize, 23), Math.max(maxSize / 23, 1) };
        for (int[] testAgainst : TARGET_RATIOS) {
            if (testAgainst[0] * testAgainst[1] == maxSize) {
                dims = testAgainst;
                break;
            }
        }
        return dims;
    }
}