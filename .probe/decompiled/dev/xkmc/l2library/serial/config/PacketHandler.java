package dev.xkmc.l2library.serial.config;

import dev.xkmc.l2serial.network.BasePacketHandler;
import dev.xkmc.l2serial.network.BasePacketHandler.LoadedPacket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class PacketHandler extends BasePacketHandler {

    private static final List<PacketHandler> LIST = new ArrayList();

    public static void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            for (PacketHandler handler : LIST) {
                handler.registerPackets();
            }
        });
    }

    @SafeVarargs
    public PacketHandler(ResourceLocation id, int version, Function<BasePacketHandler, LoadedPacket<?>>... values) {
        super(id, version, values);
        synchronized (LIST) {
            LIST.add(this);
        }
    }
}