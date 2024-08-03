package org.violetmoon.quark.mixin.mixins.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.client.module.LongRangePickBlockModule;
import org.violetmoon.quark.content.experimental.module.VariantSelectorModule;

@Mixin({ Minecraft.class })
public class MinecraftMixin {

    @Shadow
    @Nullable
    public HitResult hitResult;

    @Shadow
    @javax.annotation.Nullable
    public LocalPlayer player;

    @Inject(method = { "pickBlock" }, at = { @At("HEAD") })
    private void pickBlockHead(CallbackInfo ci, @Share("hitResult") LocalRef<HitResult> hitResult) {
        Minecraft self = (Minecraft) this;
        hitResult.set(self.hitResult);
        self.hitResult = Quark.ZETA.modules.<LongRangePickBlockModule>get(LongRangePickBlockModule.class).transformHitResult(self.hitResult);
    }

    @Inject(method = { "pickBlock" }, at = { @At("RETURN") })
    private void pickBlockReturn(CallbackInfo ci, @Share("hitResult") LocalRef<HitResult> hitResult) {
        Minecraft self = (Minecraft) this;
        self.hitResult = (HitResult) hitResult.get();
    }

    @Inject(method = { "pickBlock" }, cancellable = true, at = { @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/world/entity/player/Inventory;findSlotMatchingItem(Lnet/minecraft/world/item/ItemStack;)I") })
    private void pickBlockPick(CallbackInfo ci, @Local LocalRef<ItemStack> stack) {
        ItemStack oldPick = (ItemStack) stack.get();
        ItemStack newPick = VariantSelectorModule.Client.onPickBlock(this.player, oldPick);
        if (newPick == oldPick) {
            ci.cancel();
        } else if (newPick != null) {
            stack.set(newPick);
        }
    }
}