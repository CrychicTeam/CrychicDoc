package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class WizardLabSelectSpellComponentMessage extends BaseMessage {

    private ResourceLocation spellComponent;

    public WizardLabSelectSpellComponentMessage() {
        this.messageIsValid = false;
    }

    public WizardLabSelectSpellComponentMessage(ResourceLocation spellComponent) {
        this.spellComponent = spellComponent;
        this.messageIsValid = true;
    }

    public final ResourceLocation getSpellComponent() {
        return this.spellComponent;
    }

    public static final WizardLabSelectSpellComponentMessage decode(FriendlyByteBuf buf) {
        WizardLabSelectSpellComponentMessage msg = new WizardLabSelectSpellComponentMessage();
        try {
            msg.spellComponent = buf.readResourceLocation();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading WizardLabSelectSpellComponentMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static final void encode(WizardLabSelectSpellComponentMessage msg, FriendlyByteBuf buf) {
        buf.writeResourceLocation(msg.spellComponent);
    }
}