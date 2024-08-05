package noppes.npcs.packets.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noppes.npcs.shared.client.gui.listeners.IGuiData;
import noppes.npcs.shared.client.gui.listeners.IGuiInterface;
import noppes.npcs.shared.common.PacketBasic;

public class PacketGuiData extends PacketBasic {

    private final CompoundTag data;

    public PacketGuiData(CompoundTag data) {
        this.data = data;
    }

    public static void encode(PacketGuiData msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.data);
    }

    public static PacketGuiData decode(FriendlyByteBuf buf) {
        return new PacketGuiData(buf.readNbt(new NbtAccounter(Long.MAX_VALUE)));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle() {
        Screen gui = Minecraft.getInstance().screen;
        if (gui != null) {
            if (gui instanceof IGuiInterface && ((IGuiInterface) gui).hasSubGui()) {
                gui = ((IGuiInterface) gui).getSubGui();
            }
            if (gui instanceof IGuiData) {
                ((IGuiData) gui).setGuiData(this.data);
            }
        }
    }
}