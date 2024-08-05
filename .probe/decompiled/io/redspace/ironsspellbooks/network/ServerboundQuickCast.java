package io.redspace.ironsspellbooks.network;

import io.redspace.ironsspellbooks.api.util.Utils;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class ServerboundQuickCast {

    private int slot;

    public ServerboundQuickCast(int slot) {
        this.slot = slot;
    }

    public ServerboundQuickCast(FriendlyByteBuf buf) {
        this.slot = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.slot);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) supplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer serverPlayer = ctx.getSender();
            Utils.serverSideInitiateQuickCast(serverPlayer, this.slot);
        });
        return true;
    }
}