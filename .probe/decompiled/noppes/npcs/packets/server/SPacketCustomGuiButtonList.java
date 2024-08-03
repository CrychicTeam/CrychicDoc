package noppes.npcs.packets.server;

import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.EventHooks;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiButtonListWrapper;
import noppes.npcs.containers.ContainerCustomGui;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketCustomGuiButtonList extends PacketServerBasic {

    private final UUID buttonId;

    private final boolean isRightClick;

    public SPacketCustomGuiButtonList(UUID id, boolean isRightClick) {
        this.buttonId = id;
        this.isRightClick = isRightClick;
    }

    @Override
    public boolean toolAllowed(ItemStack item) {
        return true;
    }

    public static void encode(SPacketCustomGuiButtonList msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.buttonId);
        buf.writeBoolean(msg.isRightClick);
    }

    public static SPacketCustomGuiButtonList decode(FriendlyByteBuf buf) {
        return new SPacketCustomGuiButtonList(buf.readUUID(), buf.readBoolean());
    }

    @Override
    protected void handle() {
        if (this.player.f_36096_ instanceof ContainerCustomGui container && container.activeGui.getComponentUuid(this.buttonId) instanceof CustomGuiButtonListWrapper button) {
            PlayerWrapper p = (PlayerWrapper) NpcAPI.Instance().getIEntity(this.player);
            button.setSelected(button.getSelected() + (this.isRightClick ? 1 : -1));
            button.onPress(container.activeGui);
            EventHooks.onCustomGuiButton(p, container.activeGui, button);
        }
    }
}