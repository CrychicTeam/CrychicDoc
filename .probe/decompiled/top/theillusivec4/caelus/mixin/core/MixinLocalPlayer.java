package top.theillusivec4.caelus.mixin.core;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.caelus.mixin.util.ClientMixinHooks;

@Mixin({ LocalPlayer.class })
public class MixinLocalPlayer {

    @Unique
    private boolean caelus$flag = false;

    @Inject(at = { @At(value = "INVOKE_ASSIGN", target = "net/minecraft/client/player/LocalPlayer.getItemBySlot(Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/world/item/ItemStack;") }, method = { "aiStep" })
    public void caelus$checkFlight(CallbackInfo cb) {
        this.caelus$flag = ClientMixinHooks.checkFlight();
    }

    @ModifyVariable(at = @At(value = "INVOKE_ASSIGN", target = "net/minecraft/client/player/LocalPlayer.getItemBySlot(Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/world/item/ItemStack;"), method = { "aiStep" })
    public ItemStack caelus$affixEmptyStack(ItemStack stack) {
        return this.caelus$flag ? stack : ItemStack.EMPTY;
    }
}