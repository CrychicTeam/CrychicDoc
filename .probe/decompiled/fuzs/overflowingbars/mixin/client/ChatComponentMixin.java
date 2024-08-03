package fuzs.overflowingbars.mixin.client;

import fuzs.overflowingbars.OverflowingBars;
import fuzs.overflowingbars.client.helper.ChatOffsetHelper;
import fuzs.overflowingbars.config.ClientConfig;
import net.minecraft.client.gui.components.ChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({ ChatComponent.class })
abstract class ChatComponentMixin {

    @ModifyVariable(method = { "screenToChatY" }, at = @At("LOAD"), ordinal = 0)
    private double screenToChatY(double mouseY) {
        return !OverflowingBars.CONFIG.get(ClientConfig.class).moveChatAboveArmor ? mouseY : mouseY + (double) ChatOffsetHelper.getChatOffsetY();
    }
}