package top.theillusivec4.caelus.common.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.caelus.api.CaelusApi;

public final class CPacketFlight {

    public static void encode(CPacketFlight msg, FriendlyByteBuf buf) {
    }

    public static CPacketFlight decode(FriendlyByteBuf buf) {
        return new CPacketFlight();
    }

    public static void handle(CPacketFlight msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            ServerPlayer sender = ((NetworkEvent.Context) ctx.get()).getSender();
            if (sender != null) {
                sender.m_36321_();
                if (!sender.m_20096_() && !sender.m_21255_() && !sender.m_20069_() && !sender.m_21023_(MobEffects.LEVITATION) && CaelusApi.getInstance().canFallFly(sender) != CaelusApi.TriState.DENY) {
                    sender.m_36320_();
                }
            }
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}