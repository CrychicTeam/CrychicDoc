package io.github.lightman314.lightmanscurrency.common.menus;

import io.github.lightman314.lightmanscurrency.common.blockentity.CoinMintBlockEntity;
import io.github.lightman314.lightmanscurrency.common.core.ModMenus;
import io.github.lightman314.lightmanscurrency.common.menus.slots.OutputSlot;
import io.github.lightman314.lightmanscurrency.common.menus.slots.mint.MintSlot;
import io.github.lightman314.lightmanscurrency.common.menus.validation.EasyMenu;
import io.github.lightman314.lightmanscurrency.common.menus.validation.types.BlockEntityValidator;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MintMenu extends EasyMenu {

    public final CoinMintBlockEntity blockEntity;

    public MintMenu(int windowId, Inventory inventory, CoinMintBlockEntity blockEntity) {
        super(ModMenus.MINT.get(), windowId, inventory);
        this.blockEntity = blockEntity;
        this.addValidator(BlockEntityValidator.of(this.blockEntity));
        this.m_38897_(new MintSlot(this.blockEntity.getStorage(), 0, 56, 21, this.blockEntity));
        this.m_38897_(new OutputSlot(this.blockEntity.getStorage(), 1, 116, 21));
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.m_38897_(new Slot(inventory, x + y * 9 + 9, 8 + x * 18, 56 + y * 18));
            }
        }
        for (int x = 0; x < 9; x++) {
            this.m_38897_(new Slot(inventory, x, 8 + x * 18, 114));
        }
    }

    @Override
    public void removed(@Nonnull Player playerIn) {
        super.m_6877_(playerIn);
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(@Nonnull Player playerEntity, int index) {
        ItemStack clickedStack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(index);
        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            clickedStack = slotStack.copy();
            if (index < this.blockEntity.getStorage().getContainerSize()) {
                if (!this.m_38903_(slotStack, this.blockEntity.getStorage().getContainerSize(), this.f_38839_.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_(slotStack, 0, this.blockEntity.getStorage().getContainerSize() - 1, false)) {
                return ItemStack.EMPTY;
            }
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return clickedStack;
    }
}