package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class MAPFXSyncRequestMessage extends BaseMessage {

    private int entityID;

    private boolean aura;

    private MAPFXSyncRequestMessage() {
        this.messageIsValid = false;
    }

    public MAPFXSyncRequestMessage(int entityID, boolean aura) {
        this();
        this.entityID = entityID;
        this.aura = aura;
        this.messageIsValid = true;
    }

    public int getEntityID() {
        return this.entityID;
    }

    public boolean isForAura() {
        return this.aura;
    }

    public static MAPFXSyncRequestMessage decode(FriendlyByteBuf buf) {
        MAPFXSyncRequestMessage msg = new MAPFXSyncRequestMessage();
        try {
            msg.entityID = buf.readInt();
            msg.aura = buf.readBoolean();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading MAPFXSyncRequestMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(MAPFXSyncRequestMessage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.getEntityID());
        buf.writeBoolean(msg.isForAura());
    }

    public static MAPFXSyncRequestMessage fromConstruct(LivingEntity entity, boolean diagnosticsOnly) {
        return new MAPFXSyncRequestMessage(entity.m_19879_(), false);
    }

    public static MAPFXSyncRequestMessage forAura(Player player) {
        return new MAPFXSyncRequestMessage(player.m_19879_(), true);
    }
}