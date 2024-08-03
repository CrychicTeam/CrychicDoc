package dev.xkmc.l2backpack.network;

import dev.xkmc.l2backpack.events.TooltipUpdateEvents;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.UUID;
import net.minecraft.world.item.Item;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class RespondTooltipUpdateEvent extends SerialPacketBase {

    @SerialField
    public Item item;

    @SerialField
    public UUID id;

    @SerialField
    public int count;

    @Deprecated
    public RespondTooltipUpdateEvent() {
    }

    public RespondTooltipUpdateEvent(Item item, UUID id, int count) {
        this.item = item;
        this.id = id;
        this.count = count;
    }

    public void handle(NetworkEvent.Context context) {
        TooltipUpdateEvents.updateInfo(this.item, this.id, this.count);
    }
}