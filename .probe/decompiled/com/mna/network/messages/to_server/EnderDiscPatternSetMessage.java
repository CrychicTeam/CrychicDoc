package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import java.util.ArrayList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class EnderDiscPatternSetMessage extends BaseMessage {

    private ArrayList<ResourceLocation> patterns;

    private ResourceLocation dimensionID;

    private int patternIndex;

    private String name;

    public EnderDiscPatternSetMessage(ArrayList<ResourceLocation> patterns, ResourceLocation dimensionID, int patternIndex, String name) {
        this.patterns = patterns;
        this.patternIndex = patternIndex;
        this.dimensionID = dimensionID;
        this.name = name;
        this.messageIsValid = true;
    }

    public EnderDiscPatternSetMessage() {
        this.messageIsValid = false;
    }

    public ArrayList<ResourceLocation> getPatterns() {
        return this.patterns;
    }

    public int getIndex() {
        return this.patternIndex;
    }

    public String getName() {
        return this.name;
    }

    public ResourceLocation getDimensionID() {
        return this.dimensionID;
    }

    public static EnderDiscPatternSetMessage decode(FriendlyByteBuf buf) {
        EnderDiscPatternSetMessage msg = new EnderDiscPatternSetMessage();
        try {
            msg.patternIndex = buf.readInt();
            msg.name = buf.readUtf(32767);
            msg.dimensionID = buf.readResourceLocation();
            int count = buf.readInt();
            msg.patterns = new ArrayList();
            for (int i = 0; i < count; i++) {
                msg.patterns.add(buf.readResourceLocation());
            }
        } catch (IndexOutOfBoundsException | IllegalArgumentException var4) {
            ManaAndArtifice.LOGGER.error("Exception while reading EnderDiscPatternSetMessage: " + var4);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(EnderDiscPatternSetMessage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.getIndex());
        buf.writeUtf(msg.getName());
        buf.writeResourceLocation(msg.getDimensionID());
        buf.writeInt(msg.getPatterns().size());
        for (ResourceLocation rLoc : msg.getPatterns()) {
            buf.writeResourceLocation(rLoc);
        }
    }
}