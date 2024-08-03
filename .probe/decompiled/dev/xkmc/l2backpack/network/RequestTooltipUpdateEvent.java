package dev.xkmc.l2backpack.network;

import dev.xkmc.l2backpack.content.remote.common.DrawerAccess;
import dev.xkmc.l2backpack.init.L2Backpack;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.UUID;
import net.minecraft.world.item.Item;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class RequestTooltipUpdateEvent extends SerialPacketBase {

    @SerialField
    public Item item;

    @SerialField
    public UUID id;

    @Deprecated
    public RequestTooltipUpdateEvent() {
    }

    public RequestTooltipUpdateEvent(Item item, UUID id) {
        this.item = item;
        this.id = id;
    }

    public void handle(NetworkEvent.Context context) {
        if (context.getSender() != null) {
            int count = DrawerAccess.of(context.getSender().m_9236_(), this.id, this.item).getCount();
            L2Backpack.HANDLER.toClientPlayer(new RespondTooltipUpdateEvent(this.item, this.id, count), context.getSender());
        }
    }
}