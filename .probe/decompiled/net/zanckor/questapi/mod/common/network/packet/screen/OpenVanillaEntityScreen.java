package net.zanckor.questapi.mod.common.network.packet.screen;

import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.zanckor.questapi.util.Util;

public class OpenVanillaEntityScreen {

    UUID entityUUID;

    public OpenVanillaEntityScreen(UUID entityUUID) {
        this.entityUUID = entityUUID;
    }

    public OpenVanillaEntityScreen(FriendlyByteBuf buffer) {
        this.entityUUID = buffer.readUUID();
    }

    public void encodeBuffer(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.entityUUID);
    }

    public static void handler(OpenVanillaEntityScreen msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            Player player = ((NetworkEvent.Context) ctx.get()).getSender();
            Entity entity = Util.getEntityByUUID(((NetworkEvent.Context) ctx.get()).getSender().serverLevel(), msg.entityUUID);
            player.m_20260_(true);
            player.interactOn(entity, InteractionHand.MAIN_HAND);
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}