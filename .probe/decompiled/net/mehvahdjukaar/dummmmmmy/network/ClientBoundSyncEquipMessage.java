package net.mehvahdjukaar.dummmmmmy.network;

import net.mehvahdjukaar.dummmmmmy.common.TargetDummyEntity;
import net.mehvahdjukaar.moonlight.api.platform.network.ChannelHandler;
import net.mehvahdjukaar.moonlight.api.platform.network.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ClientBoundSyncEquipMessage implements Message {

    private final int entityID;

    private final int slotId;

    private final ItemStack itemstack;

    public ClientBoundSyncEquipMessage(FriendlyByteBuf buf) {
        this.entityID = buf.readInt();
        this.slotId = buf.readInt();
        this.itemstack = buf.readItem();
    }

    public ClientBoundSyncEquipMessage(int entityId, int slotId, @NotNull ItemStack itemstack) {
        this.entityID = entityId;
        this.slotId = slotId;
        this.itemstack = itemstack.copy();
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf buf) {
        buf.writeInt(this.entityID);
        buf.writeInt(this.slotId);
        buf.writeItem(this.itemstack);
    }

    @Override
    public void handle(ChannelHandler.Context context) {
        if (Minecraft.getInstance().level.getEntity(this.entityID) instanceof TargetDummyEntity dummy) {
            dummy.m_8061_(EquipmentSlot.byTypeAndIndex(EquipmentSlot.Type.ARMOR, this.slotId), this.itemstack);
        }
    }
}