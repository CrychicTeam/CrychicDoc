package io.github.lightman314.lightmanscurrency.mixin;

import io.github.lightman314.lightmanscurrency.LCConfig;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.common.capability.wallet.WalletCapability;
import io.github.lightman314.lightmanscurrency.common.items.WalletItem;
import io.github.lightman314.lightmanscurrency.common.menus.slots.WalletSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ InventoryMenu.class })
public abstract class InventoryMenuMixin {

    @Unique
    private Slot walletSlot = null;

    @Unique
    protected InventoryMenu self() {
        return (InventoryMenu) this;
    }

    @Accessor("owner")
    protected abstract Player getPlayer();

    @Inject(at = { @At("TAIL") }, method = { "<init>" })
    protected void init(Inventory inventory, boolean active, Player player, CallbackInfo callbackInfo) {
        if (!LightmansCurrency.isCuriosLoaded()) {
            if (this.self() instanceof AbstractContainerMenuAccessor accessor) {
                this.walletSlot = accessor.addCustomSlot(new WalletSlot(player, WalletCapability.getWalletContainer(player), 0, LCConfig.CLIENT.walletSlot.get().x + 1, LCConfig.CLIENT.walletSlot.get().y + 1));
            }
        }
    }

    @Inject(at = { @At("HEAD") }, method = { "quickMoveStack" }, cancellable = true)
    protected void quickMoveStack(Player player, int slotIndex, CallbackInfoReturnable<ItemStack> callbackInfo) {
        if (slotIndex >= 9 && slotIndex < 45 && this.walletSlot != null) {
            Slot slot = (Slot) this.self().f_38839_.get(slotIndex);
            if (slot.hasItem() && WalletItem.isWallet(slot.getItem()) && !this.walletSlot.hasItem()) {
                this.walletSlot.set(slot.getItem().copy());
                slot.set(ItemStack.EMPTY);
                callbackInfo.setReturnValue(ItemStack.EMPTY);
            }
        }
    }
}