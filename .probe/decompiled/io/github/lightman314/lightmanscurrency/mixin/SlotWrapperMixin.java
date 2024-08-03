package io.github.lightman314.lightmanscurrency.mixin;

import io.github.lightman314.lightmanscurrency.LCConfig;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import io.github.lightman314.lightmanscurrency.common.menus.slots.WalletSlot;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ CreativeModeInventoryScreen.SlotWrapper.class })
public class SlotWrapperMixin {

    @Unique
    private CreativeModeInventoryScreen.SlotWrapper self() {
        return (CreativeModeInventoryScreen.SlotWrapper) this;
    }

    @Inject(at = { @At("TAIL") }, method = { "<init>" })
    public void init(Slot target, int index, int x, int y, CallbackInfo callbackInfo) {
        if (target instanceof WalletSlot) {
            ScreenPosition pos = LCConfig.CLIENT.walletSlotCreative.get();
            this.self().f_40220_ = pos.x + 1;
            this.self().f_40221_ = pos.y + 1;
        }
    }
}