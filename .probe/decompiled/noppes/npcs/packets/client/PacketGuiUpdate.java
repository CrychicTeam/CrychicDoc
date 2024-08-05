package noppes.npcs.packets.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noppes.npcs.shared.client.gui.listeners.IGuiInterface;
import noppes.npcs.shared.common.PacketBasic;

public class PacketGuiUpdate extends PacketBasic {

    public static void encode(PacketGuiUpdate msg, FriendlyByteBuf buf) {
    }

    public static PacketGuiUpdate decode(FriendlyByteBuf buf) {
        return new PacketGuiUpdate();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle() {
        Screen gui = Minecraft.getInstance().screen;
        if (gui != null) {
            if (gui instanceof IGuiInterface igui) {
                igui.initGui();
            }
        }
    }
}