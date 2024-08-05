package dev.shadowsoffire.placebo.mixin.client;

import net.minecraft.client.gui.components.ChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin({ ChatComponent.class })
public class ChatComponentMixin {

    @ModifyConstant(method = { "logChatMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/client/GuiMessageTag;)V" })
    public String placebo_unEscapeChatLogNewlines(String old) {
        return old.equals("\\\\n") ? "\n" : old;
    }
}