package dev.xkmc.l2library.serial.config;

import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class SyncPacket extends SerialPacketBase {

    @SerialField
    public ResourceLocation id;

    @SerialField
    public ArrayList<PacketHandlerWithConfig.ConfigInstance> map;

    @Deprecated
    public SyncPacket() {
    }

    SyncPacket(PacketHandlerWithConfig handler, List<PacketHandlerWithConfig.ConfigInstance> map) {
        this.id = handler.CHANNEL_NAME;
        this.map = new ArrayList(map);
    }

    public void handle(NetworkEvent.Context ctx) {
        if (this.map != null) {
            PacketHandlerWithConfig handler = (PacketHandlerWithConfig) PacketHandlerWithConfig.INTERNAL.get(this.id);
            handler.listener.apply(this.map);
        }
    }
}