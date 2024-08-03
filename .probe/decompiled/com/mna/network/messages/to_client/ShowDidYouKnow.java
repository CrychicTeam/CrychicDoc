package com.mna.network.messages.to_client;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ShowDidYouKnow extends BaseMessage {

    private String messageID;

    public ShowDidYouKnow(String messageID) {
        this.messageID = messageID;
        this.messageIsValid = true;
    }

    public ShowDidYouKnow() {
        this.messageIsValid = false;
    }

    public String getMessage() {
        return this.messageID;
    }

    public static ShowDidYouKnow decode(FriendlyByteBuf buf) {
        ShowDidYouKnow msg = new ShowDidYouKnow();
        try {
            msg.messageID = buf.readUtf();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading ShowDidYouKnow: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(ShowDidYouKnow msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.getMessage());
    }

    @OnlyIn(Dist.CLIENT)
    public void Handle() {
        ManaAndArtifice.instance.proxy.showDidYouKnow(null, this.getMessage());
    }

    public static class Messages {

        public static String LENS_CAPTURE = "helptip.mna.wellspring_lens";

        public static String BUFF_CACHE = "helptip.mna.buff_cache";

        public static String MODIFIER_SHIFT = "helptip.mna.modifier_shift_click";
    }
}