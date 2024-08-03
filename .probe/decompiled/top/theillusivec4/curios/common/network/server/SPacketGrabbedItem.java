package top.theillusivec4.curios.common.network.server;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class SPacketGrabbedItem {

    private final ItemStack stack;

    public SPacketGrabbedItem(ItemStack stackIn) {
        this.stack = stackIn;
    }

    public static void encode(SPacketGrabbedItem msg, FriendlyByteBuf buf) {
        buf.writeItem(msg.stack);
    }

    public static SPacketGrabbedItem decode(FriendlyByteBuf buf) {
        return new SPacketGrabbedItem(buf.readItem());
    }

    public static void handle(SPacketGrabbedItem msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            LocalPlayer clientPlayer = Minecraft.getInstance().player;
            if (clientPlayer != null) {
                clientPlayer.f_36096_.setCarried(msg.stack);
            }
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}