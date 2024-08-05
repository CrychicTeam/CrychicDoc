package net.zanckor.questapi.mod.common.network.packet.dialogoption;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.zanckor.questapi.mod.common.network.handler.ClientHandler;

public class CloseDialog {

    public CloseDialog() {
    }

    public CloseDialog(FriendlyByteBuf buffer) {
    }

    public void encodeBuffer(FriendlyByteBuf buffer) {
    }

    public static void handler(CloseDialog msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientHandler::closeDialog));
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}