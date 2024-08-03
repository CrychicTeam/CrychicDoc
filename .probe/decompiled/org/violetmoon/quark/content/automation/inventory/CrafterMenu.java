package org.violetmoon.quark.content.automation.inventory;

import java.util.function.Function;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import org.violetmoon.quark.content.automation.block.be.CrafterBlockEntity;
import org.violetmoon.quark.content.automation.module.CrafterModule;

public class CrafterMenu extends AbstractContainerMenu {

    public final Container crafter;

    public final ContainerData delegate;

    private final ContainerLevelAccess access;

    public CrafterMenu(int syncId, Inventory player) {
        this(syncId, player, it -> new TransientCraftingContainer(it, 3, 3), new ResultContainer(), new SimpleContainerData(1), ContainerLevelAccess.NULL);
    }

    public CrafterMenu(int syncId, Inventory player, Function<CrafterMenu, CraftingContainer> crafterProvider, ResultContainer result, final ContainerData delegate, ContainerLevelAccess access) {
        super(CrafterModule.menuType, syncId);
        CraftingContainer crafter = (CraftingContainer) crafterProvider.apply(this);
        this.access = access;
        m_38869_(crafter, 9);
        this.crafter = crafter;
        crafter.m_5856_(player.player);
        this.delegate = delegate;
        this.m_38884_(delegate);
        this.m_38897_(new ResultSlot(player.player, crafter, result, 0, 134, 35) {

            @Override
            public boolean mayPickup(Player p_40228_) {
                return false;
            }
        });
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int index = j + i * 3;
                this.m_38897_(new Slot(crafter, index, 26 + j * 18, 17 + i * 18) {

                    @Override
                    public boolean mayPlace(@Nonnull ItemStack stack) {
                        return (delegate.get(0) & 1 << this.f_40219_) == 0;
                    }
                });
            }
        }
        for (int var11 = 0; var11 < 3; var11++) {
            for (int j = 0; j < 9; j++) {
                this.m_38897_(new Slot(player, j + var11 * 9 + 9, 8 + j * 18, 84 + var11 * 18));
            }
        }
        for (int var12 = 0; var12 < 9; var12++) {
            this.m_38897_(new Slot(player, var12, 8 + var12 * 18, 142));
        }
    }

    public static CrafterMenu fromNetwork(int windowId, Inventory playerInventory, FriendlyByteBuf buf) {
        return new CrafterMenu(windowId, playerInventory);
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id >= 0 && id < 9) {
            this.access.execute((level, pos) -> {
                if (!level.isClientSide && level.getBlockEntity(pos) instanceof CrafterBlockEntity cbe) {
                    cbe.blocked[id] = !cbe.blocked[id];
                    cbe.update();
                    cbe.m_6596_();
                }
            });
            return true;
        } else {
            return super.clickMenuButton(player, id);
        }
    }

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);
        this.access.execute((level, pos) -> {
            if (!level.isClientSide && level.getBlockEntity(pos) instanceof CrafterBlockEntity cbe) {
                cbe.update();
            }
        });
    }

    @Override
    public boolean stillValid(Player player) {
        return this.crafter.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.m_38853_(index);
        ItemStack original = ItemStack.EMPTY;
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            original = stack.copy();
            if (index == 0) {
                if (!this.m_38903_(stack, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, original);
            } else if (index < 10) {
                if (!this.m_38903_(stack, 10, 46, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_(stack, 1, 10, false)) {
                if (index < 37) {
                    if (!this.m_38903_(stack, 37, 46, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.m_38903_(stack, 10, 37, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (stack.getCount() == original.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, stack);
            if (index == 0) {
                player.drop(stack, false);
            }
        }
        return original;
    }
}