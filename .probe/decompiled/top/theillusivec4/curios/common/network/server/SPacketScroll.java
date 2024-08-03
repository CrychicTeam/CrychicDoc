package top.theillusivec4.curios.common.network.server;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.client.gui.CuriosScreen;
import top.theillusivec4.curios.common.inventory.container.CuriosContainer;

public class SPacketScroll {

    private int windowId;

    private int index;

    public SPacketScroll(int windowId, int index) {
        this.windowId = windowId;
        this.index = index;
    }

    public static void encode(SPacketScroll msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.windowId);
        buf.writeInt(msg.index);
    }

    public static SPacketScroll decode(FriendlyByteBuf buf) {
        return new SPacketScroll(buf.readInt(), buf.readInt());
    }

    public static void handle(SPacketScroll msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer clientPlayer = mc.player;
            Screen screen = mc.screen;
            if (clientPlayer != null) {
                AbstractContainerMenu container = clientPlayer.f_36096_;
                if (container instanceof CuriosContainer && container.containerId == msg.windowId) {
                    ((CuriosContainer) container).scrollToIndex(msg.index);
                }
            }
            if (screen instanceof CuriosScreen) {
                ((CuriosScreen) screen).updateRenderButtons();
            }
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}