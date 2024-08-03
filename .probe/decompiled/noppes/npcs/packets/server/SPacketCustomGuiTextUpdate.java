package noppes.npcs.packets.server;

import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.api.gui.ICustomGuiComponent;
import noppes.npcs.api.wrapper.gui.CustomGuiAssetsSelectorWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiTextFieldWrapper;
import noppes.npcs.containers.ContainerCustomGui;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketCustomGuiTextUpdate extends PacketServerBasic {

    private final UUID id;

    private final String text;

    public SPacketCustomGuiTextUpdate(UUID id, String text) {
        this.id = id;
        this.text = text;
    }

    @Override
    public boolean toolAllowed(ItemStack item) {
        return true;
    }

    public static void encode(SPacketCustomGuiTextUpdate msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.id);
        buf.writeUtf(msg.text, 131068);
    }

    public static SPacketCustomGuiTextUpdate decode(FriendlyByteBuf buf) {
        return new SPacketCustomGuiTextUpdate(buf.readUUID(), buf.readUtf(131068));
    }

    @Override
    protected void handle() {
        if (this.player.f_36096_ instanceof ContainerCustomGui container) {
            ICustomGuiComponent comp = container.activeGui.getComponentUuid(this.id);
            if (comp instanceof CustomGuiTextFieldWrapper tf) {
                tf.setText(this.text);
                tf.onChange(container.activeGui);
            }
            if (comp instanceof CustomGuiAssetsSelectorWrapper as) {
                as.setSelected(this.text);
                as.onChange(container.activeGui);
            }
        }
    }
}