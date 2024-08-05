package snownee.kiwi.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import snownee.kiwi.KiwiClientConfig;
import snownee.kiwi.config.KiwiConfigManager;

@Mixin({ Screen.class })
public class ScreenMixin {

    @Inject(method = { "handleComponentClicked" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyboardHandler;setClipboard(Ljava/lang/String;)V") }, cancellable = true)
    private void kiwi$handleComponentClicked(Style style, CallbackInfoReturnable<Boolean> ci) {
        ClickEvent clickEvent = style.getClickEvent();
        if (clickEvent != null && clickEvent.getAction() == ClickEvent.Action.COPY_TO_CLIPBOARD && "@kiwi disable debugTooltip".equals(clickEvent.getValue())) {
            if (KiwiClientConfig.tagsTooltip || KiwiClientConfig.nbtTooltip) {
                KiwiClientConfig.tagsTooltip = false;
                KiwiClientConfig.nbtTooltip = false;
                KiwiConfigManager.getHandler(KiwiClientConfig.class).save();
            }
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                player.displayClientMessage(Component.translatable("tip.kiwi.debug_tooltip.success"), false);
            }
            ci.setReturnValue(true);
        }
    }
}