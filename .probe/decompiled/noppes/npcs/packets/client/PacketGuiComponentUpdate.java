package noppes.npcs.packets.client;

import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noppes.npcs.api.wrapper.gui.CustomGuiComponentWrapper;
import noppes.npcs.client.gui.custom.GuiCustom;
import noppes.npcs.client.gui.custom.interfaces.IGuiComponent;
import noppes.npcs.shared.common.PacketBasic;

public class PacketGuiComponentUpdate extends PacketBasic {

    private UUID id;

    private CompoundTag data;

    public PacketGuiComponentUpdate(UUID id, CompoundTag data) {
        this.id = id;
        this.data = data;
    }

    public static void encode(PacketGuiComponentUpdate msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.id);
        buf.writeNbt(msg.data);
    }

    public static PacketGuiComponentUpdate decode(FriendlyByteBuf buf) {
        return new PacketGuiComponentUpdate(buf.readUUID(), buf.readNbt());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle() {
        Screen gui = Minecraft.getInstance().screen;
        if (gui != null) {
            if (gui instanceof GuiCustom cgui) {
                CustomGuiComponentWrapper component = (CustomGuiComponentWrapper) cgui.guiWrapper.getComponentUuid(this.id);
                component.fromNBT(this.data);
                IGuiComponent guic = cgui.getComponent(this.id);
                guic.init();
            }
        }
    }
}