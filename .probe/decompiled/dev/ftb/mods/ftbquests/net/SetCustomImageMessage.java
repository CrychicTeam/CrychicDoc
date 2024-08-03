package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftblibrary.config.ImageResourceConfig;
import dev.ftb.mods.ftbquests.item.CustomIconItem;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;

public class SetCustomImageMessage extends BaseC2SMessage {

    private final InteractionHand hand;

    private final ResourceLocation texture;

    SetCustomImageMessage(FriendlyByteBuf buffer) {
        this.hand = buffer.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        this.texture = buffer.readResourceLocation();
    }

    public SetCustomImageMessage(InteractionHand h, ResourceLocation t) {
        this.hand = h;
        this.texture = t;
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.SET_CUSTOM_IMAGE;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.hand == InteractionHand.MAIN_HAND);
        buffer.writeResourceLocation(this.texture);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        if (context.getPlayer().m_21120_(this.hand).getItem() instanceof CustomIconItem) {
            if (this.texture.equals(ImageResourceConfig.NONE)) {
                context.getPlayer().m_21120_(this.hand).removeTagKey("Icon");
            } else {
                context.getPlayer().m_21120_(this.hand).addTagElement("Icon", StringTag.valueOf(this.texture.toString()));
            }
        }
    }
}