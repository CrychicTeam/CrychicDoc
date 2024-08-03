package com.mna.mixins;

import com.mna.enchantments.framework.EnchantmentInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.api.CuriosApi;

@Mixin({ Inventory.class })
public class InventorySoulboundDrop {

    @Inject(at = { @At("HEAD") }, method = { "dropAll" }, cancellable = false)
    public void saveSoulboundItems(CallbackInfo callbackInfo) {
        Inventory self = (Inventory) this;
        CompoundTag inventory = new CompoundTag();
        CompoundTag curios = new CompoundTag();
        for (int i = 0; i < self.getContainerSize(); i++) {
            ItemStack stackInSlot = self.getItem(i);
            if (!stackInSlot.isEmpty() && stackInSlot.getEnchantmentLevel(EnchantmentInit.SOULBOUND.get()) > 0) {
                CompoundTag item = new CompoundTag();
                stackInSlot.save(item);
                inventory.put("slot_" + i, item);
                self.setItem(i, ItemStack.EMPTY);
            }
        }
        CuriosApi.getCuriosHelper().getCuriosHandler(self.player).ifPresent(p -> p.getCurios().forEach((identifier, stackHandler) -> {
            CompoundTag tagForIdentifier = new CompoundTag();
            for (int ix = 0; ix < stackHandler.getSlots(); ix++) {
                ItemStack stackInSlotx = stackHandler.getStacks().getStackInSlot(ix);
                if (!stackInSlotx.isEmpty() && stackInSlotx.getEnchantmentLevel(EnchantmentInit.SOULBOUND.get()) > 0) {
                    CompoundTag itemx = new CompoundTag();
                    stackInSlotx.save(itemx);
                    tagForIdentifier.put("slot_" + ix, itemx);
                    stackHandler.getStacks().setStackInSlot(ix, ItemStack.EMPTY);
                }
            }
            curios.put(identifier, tagForIdentifier);
        }));
        self.player.getPersistentData().put("mna_soulbound_inventory", inventory);
        self.player.getPersistentData().put("mna_soulbound_curios", curios);
    }
}