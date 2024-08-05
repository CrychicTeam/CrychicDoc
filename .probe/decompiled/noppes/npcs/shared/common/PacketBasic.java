package noppes.npcs.shared.common;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public abstract class PacketBasic {

    public Player player;

    public Supplier<NetworkEvent.Context> ctx;

    public static void handle(PacketBasic msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            msg.ctx = ctx;
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> msg.handleClient());
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private void handleClient() {
        this.player = Minecraft.getInstance().player;
        this.handle();
    }

    public abstract void handle();
}