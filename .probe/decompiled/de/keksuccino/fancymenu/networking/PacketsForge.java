package de.keksuccino.fancymenu.networking;

import de.keksuccino.fancymenu.networking.bridge.BridgePacketHandlerForge;
import de.keksuccino.fancymenu.networking.bridge.BridgePacketMessageForge;
import de.keksuccino.fancymenu.networking.packets.Packets;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

public class PacketsForge {

    public static void init() {
        Packets.registerAll();
        registerForgeBridgePacket();
        PacketHandler.setSendToClientLogic((player, s) -> {
            BridgePacketMessageForge msg = new BridgePacketMessageForge();
            msg.direction = "client";
            msg.dataWithIdentifier = s;
            PacketHandlerForge.send(PacketDistributor.PLAYER.with(() -> player), msg);
        });
        PacketHandler.setSendToServerLogic(s -> {
            BridgePacketMessageForge msg = new BridgePacketMessageForge();
            msg.direction = "server";
            msg.dataWithIdentifier = s;
            PacketHandlerForge.sendToServer(msg);
        });
    }

    private static void registerForgeBridgePacket() {
        PacketHandlerForge.registerMessage(BridgePacketMessageForge.class, (msg, buf) -> {
            buf.writeUtf(msg.direction);
            buf.writeUtf(msg.dataWithIdentifier);
        }, buf -> {
            BridgePacketMessageForge msg = new BridgePacketMessageForge();
            msg.direction = buf.readUtf();
            msg.dataWithIdentifier = buf.readUtf();
            return msg;
        }, (msg, context) -> {
            ((NetworkEvent.Context) context.get()).enqueueWork(() -> {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    if (msg.direction.equals("server")) {
                        BridgePacketHandlerForge.handle(((NetworkEvent.Context) context.get()).getSender(), msg, PacketHandler.PacketDirection.TO_SERVER);
                    } else if (msg.direction.equals("client")) {
                        BridgePacketHandlerForge.handle(null, msg, PacketHandler.PacketDirection.TO_CLIENT);
                    }
                });
                DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> {
                    if (msg.direction.equals("server")) {
                        BridgePacketHandlerForge.handle(((NetworkEvent.Context) context.get()).getSender(), msg, PacketHandler.PacketDirection.TO_SERVER);
                    }
                });
            });
            ((NetworkEvent.Context) context.get()).setPacketHandled(true);
        });
    }
}