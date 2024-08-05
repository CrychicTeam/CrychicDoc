package com.mna.network.messages.to_client;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import com.mna.tools.loot.LootDrop;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class RespondLootTableItems extends BaseMessage {

    ResourceLocation lootTableID;

    List<LootDrop> lootDrops;

    public RespondLootTableItems(ResourceLocation lootTableID, List<LootDrop> drops) {
        this.lootTableID = lootTableID;
        this.lootDrops = drops;
        this.messageIsValid = true;
    }

    public RespondLootTableItems() {
        this.messageIsValid = false;
    }

    public ResourceLocation getLootTableID() {
        return this.lootTableID;
    }

    public List<LootDrop> getLootDrops() {
        return this.lootDrops;
    }

    public static RespondLootTableItems decode(FriendlyByteBuf buf) {
        RespondLootTableItems msg = new RespondLootTableItems();
        try {
            msg.lootTableID = buf.readResourceLocation();
            int numDrops = buf.readInt();
            msg.lootDrops = new ArrayList();
            for (int i = 0; i < numDrops; i++) {
                msg.lootDrops.add(LootDrop.readFrom(buf));
            }
        } catch (IndexOutOfBoundsException | IllegalArgumentException var4) {
            ManaAndArtifice.LOGGER.error("Exception while reading RespondLootTableItems: " + var4);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(RespondLootTableItems msg, FriendlyByteBuf buf) {
        buf.writeResourceLocation(msg.getLootTableID());
        buf.writeInt(msg.getLootDrops().size());
        for (int i = 0; i < msg.getLootDrops().size(); i++) {
            ((LootDrop) msg.getLootDrops().get(i)).writeTo(buf);
        }
    }
}