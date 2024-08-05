package com.simibubi.create.content.contraptions.actors.contraptionControls;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class ContraptionDisableActorPacket extends SimplePacketBase {

    private int entityID;

    private ItemStack filter;

    private boolean enable;

    public ContraptionDisableActorPacket(int entityID, ItemStack filter, boolean enable) {
        this.entityID = entityID;
        this.filter = filter;
        this.enable = enable;
    }

    public ContraptionDisableActorPacket(FriendlyByteBuf buffer) {
        this.entityID = buffer.readInt();
        this.enable = buffer.readBoolean();
        this.filter = buffer.readItem();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityID);
        buffer.writeBoolean(this.enable);
        buffer.writeItem(this.filter);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level.getEntity(this.entityID) instanceof AbstractContraptionEntity ace) {
                Contraption contraption = ace.getContraption();
                List disabledActors = contraption.getDisabledActors();
                if (this.filter.isEmpty()) {
                    disabledActors.clear();
                }
                if (!this.enable) {
                    disabledActors.add(this.filter);
                    contraption.setActorsActive(this.filter, false);
                } else {
                    Iterator<ItemStack> iterator = disabledActors.iterator();
                    while (iterator.hasNext()) {
                        ItemStack next = (ItemStack) iterator.next();
                        if (ContraptionControlsMovement.isSameFilter(next, this.filter) || next.isEmpty()) {
                            iterator.remove();
                        }
                    }
                    contraption.setActorsActive(this.filter, true);
                }
            }
        });
        return true;
    }
}