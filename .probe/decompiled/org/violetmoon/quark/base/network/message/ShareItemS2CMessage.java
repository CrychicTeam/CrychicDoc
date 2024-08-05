package org.violetmoon.quark.base.network.message;

import java.util.UUID;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.violetmoon.quark.content.management.module.ItemSharingModule;
import org.violetmoon.zeta.network.IZetaMessage;
import org.violetmoon.zeta.network.IZetaNetworkEventContext;

public class ShareItemS2CMessage implements IZetaMessage {

    public UUID senderUuid;

    public Component senderName;

    public ItemStack stack;

    public ShareItemS2CMessage() {
    }

    public ShareItemS2CMessage(UUID senderUuid, Component senderName, ItemStack stack) {
        this.senderUuid = senderUuid;
        this.senderName = senderName;
        this.stack = stack;
    }

    @Override
    public boolean receive(IZetaNetworkEventContext context) {
        context.enqueueWork(() -> {
            if (!Minecraft.getInstance().isBlocked(this.senderUuid)) {
                Minecraft.getInstance().gui.getChat().addMessage(Component.translatable("chat.type.text", this.senderName, ItemSharingModule.createStackComponent(this.stack)), null, new GuiMessageTag(14595203, null, null, "Quark shared item"));
            }
        });
        return true;
    }
}