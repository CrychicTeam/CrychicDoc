package snownee.kiwi.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ CreativeModeInventoryScreen.class })
public abstract class CreativeModeInventoryScreenMixin extends EffectRenderingInventoryScreen<CreativeModeInventoryScreen.ItemPickerMenu> {

    @Shadow
    private float scrollOffs;

    @Unique
    private static float persistentScrollOffs = 0.0F;

    @Unique
    private CreativeModeTab clickedTab;

    @Shadow
    protected abstract boolean checkTabClicked(CreativeModeTab var1, double var2, double var4);

    public CreativeModeInventoryScreenMixin(CreativeModeInventoryScreen.ItemPickerMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Inject(method = { "removed" }, at = { @At("HEAD") })
    private void kiwi$saveScrollOffs(CallbackInfo ci) {
        persistentScrollOffs = this.scrollOffs;
    }

    @Inject(method = { "init" }, at = { @At("TAIL") })
    private void kiwi$restoreScrollOffs(CallbackInfo ci) {
        this.scrollOffs = persistentScrollOffs;
        ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).scrollTo(this.scrollOffs);
    }

    @Inject(method = { "mouseClicked" }, at = { @At("HEAD") })
    private void kiwi$mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (button == 0) {
            this.clickedTab = null;
            double x = mouseX - (double) this.f_97735_;
            double y = mouseY - (double) this.f_97736_;
            CreativeModeInventoryScreen self = (CreativeModeInventoryScreen) this;
            for (CreativeModeTab tab : self.getCurrentPage().getVisibleTabs()) {
                if (this.checkTabClicked(tab, x, y)) {
                    this.clickedTab = tab;
                }
            }
        }
    }

    @Inject(method = { "mouseReleased" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/CreativeModeInventoryScreen;selectTab(Lnet/minecraft/world/item/CreativeModeTab;)V") }, cancellable = true)
    private void kiwi$mouseReleased(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> ci, @Local CreativeModeTab tab) {
        if (this.clickedTab != tab) {
            ci.setReturnValue(true);
        }
    }
}