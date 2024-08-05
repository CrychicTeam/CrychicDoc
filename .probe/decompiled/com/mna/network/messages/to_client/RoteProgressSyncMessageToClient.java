package com.mna.network.messages.to_client;

import com.mna.ManaAndArtifice;
import com.mna.api.capabilities.IPlayerRoteSpells;
import com.mna.api.spells.base.ISpellComponent;
import com.mna.network.messages.BaseMessage;
import java.util.HashMap;
import java.util.Map.Entry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class RoteProgressSyncMessageToClient extends BaseMessage {

    private HashMap<ResourceLocation, Float> roteSpellProgress = new HashMap();

    private HashMap<ResourceLocation, Float> spellMastery = new HashMap();

    private RoteProgressSyncMessageToClient() {
        this.messageIsValid = false;
    }

    public RoteProgressSyncMessageToClient(HashMap<ISpellComponent, Float> roteSpellProgress, HashMap<ISpellComponent, Float> masteryProgress) {
        this();
        for (Entry<ISpellComponent, Float> e : roteSpellProgress.entrySet()) {
            if (e != null) {
                this.roteSpellProgress.put(((ISpellComponent) e.getKey()).getRegistryName(), (Float) e.getValue());
            }
        }
        for (Entry<ISpellComponent, Float> ex : masteryProgress.entrySet()) {
            if (ex != null) {
                this.spellMastery.put(((ISpellComponent) ex.getKey()).getRegistryName(), (Float) ex.getValue());
            }
        }
        this.messageIsValid = true;
    }

    public HashMap<ResourceLocation, Float> getRoteProgress() {
        return this.roteSpellProgress;
    }

    public HashMap<ResourceLocation, Float> getSpellMastery() {
        return this.spellMastery;
    }

    public static RoteProgressSyncMessageToClient decode(FriendlyByteBuf buf) {
        RoteProgressSyncMessageToClient msg = new RoteProgressSyncMessageToClient();
        try {
            int roteCount = buf.readInt();
            for (int i = 0; i < roteCount; i++) {
                ResourceLocation rLoc = buf.readResourceLocation();
                float xp = buf.readFloat();
                msg.getRoteProgress().put(rLoc, xp);
            }
            int masteryCount = buf.readInt();
            for (int i = 0; i < masteryCount; i++) {
                ResourceLocation rLoc = buf.readResourceLocation();
                float mastery = buf.readFloat();
                msg.getSpellMastery().put(rLoc, mastery);
            }
        } catch (IndexOutOfBoundsException | IllegalArgumentException var7) {
            ManaAndArtifice.LOGGER.error("Exception while reading RoteSyncMessageToClient: " + var7);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(RoteProgressSyncMessageToClient msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.getRoteProgress().size());
        for (Entry<ResourceLocation, Float> e : msg.getRoteProgress().entrySet()) {
            buf.writeResourceLocation((ResourceLocation) e.getKey());
            buf.writeFloat((Float) e.getValue());
        }
        buf.writeInt(msg.getSpellMastery().size());
        for (Entry<ResourceLocation, Float> e : msg.getSpellMastery().entrySet()) {
            buf.writeResourceLocation((ResourceLocation) e.getKey());
            buf.writeFloat((Float) e.getValue());
        }
    }

    public static RoteProgressSyncMessageToClient fromCapability(IPlayerRoteSpells capability) {
        return new RoteProgressSyncMessageToClient(capability.getRoteData(), capability.getMasteryData());
    }
}