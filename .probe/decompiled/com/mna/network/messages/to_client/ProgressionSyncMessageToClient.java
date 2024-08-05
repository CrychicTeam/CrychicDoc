package com.mna.network.messages.to_client;

import com.mna.ManaAndArtifice;
import com.mna.Registries;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.faction.IFaction;
import com.mna.network.messages.BaseMessage;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

public class ProgressionSyncMessageToClient extends BaseMessage {

    private int _tier;

    private IFaction _faction;

    private int _factionStanding;

    private List<ResourceLocation> completedProgressionSteps;

    private ProgressionSyncMessageToClient() {
        this.completedProgressionSteps = new ArrayList();
        this.messageIsValid = false;
    }

    public ProgressionSyncMessageToClient(int tier, IFaction faction, int factionStanding, List<ResourceLocation> progressionCompletion) {
        this.completedProgressionSteps = progressionCompletion;
        this._tier = tier;
        this._faction = faction;
        this._factionStanding = factionStanding;
        this.messageIsValid = true;
    }

    public int getTier() {
        return this._tier;
    }

    public IFaction getFaction() {
        return this._faction;
    }

    public int getFactionStanding() {
        return this._factionStanding;
    }

    public List<ResourceLocation> getCompletedProgressionSteps() {
        return this.completedProgressionSteps;
    }

    public static ProgressionSyncMessageToClient decode(FriendlyByteBuf buf) {
        ProgressionSyncMessageToClient msg = new ProgressionSyncMessageToClient();
        try {
            msg._tier = buf.readInt();
            msg._factionStanding = buf.readInt();
            if (buf.readBoolean()) {
                msg._faction = (IFaction) ((IForgeRegistry) Registries.Factions.get()).getValue(new ResourceLocation(buf.readUtf()));
            }
            int count = buf.readInt();
            for (int i = 0; i < count; i++) {
                msg.completedProgressionSteps.add(buf.readResourceLocation());
            }
        } catch (IndexOutOfBoundsException | IllegalArgumentException var4) {
            ManaAndArtifice.LOGGER.error("Exception while reading MagicSyncMessageToClient: " + var4);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(ProgressionSyncMessageToClient msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.getTier());
        buf.writeInt(msg.getFactionStanding());
        buf.writeBoolean(msg.getFaction() != null);
        if (msg.getFaction() != null) {
            buf.writeUtf(((IForgeRegistry) Registries.Factions.get()).getKey(msg.getFaction()).toString());
        }
        buf.writeInt(msg.getCompletedProgressionSteps().size());
        for (ResourceLocation rLoc : msg.getCompletedProgressionSteps()) {
            buf.writeResourceLocation(rLoc);
        }
    }

    public static ProgressionSyncMessageToClient fromCapability(IPlayerProgression capability) {
        return new ProgressionSyncMessageToClient(capability.getTier(), capability.getAlliedFaction(), capability.getFactionStanding(), capability.getCompletedProgressionSteps());
    }
}