package io.redspace.ironsspellbooks.network;

import io.redspace.ironsspellbooks.api.util.Utils;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class ServerboundCast {

    public ServerboundCast() {
    }

    public ServerboundCast(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) supplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer serverPlayer = ctx.getSender();
            Utils.serverSideInitiateCast(serverPlayer);
        });
        return true;
    }
}