package noppes.npcs.packets.server;

import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.EventHooks;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.gui.ICustomGuiComponent;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiAssetsSelectorWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiButtonWrapper;
import noppes.npcs.containers.ContainerCustomGui;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketCustomGuiButton extends PacketServerBasic {

    private final UUID buttonId;

    public SPacketCustomGuiButton(UUID id) {
        this.buttonId = id;
    }

    @Override
    public boolean toolAllowed(ItemStack item) {
        return true;
    }

    public static void encode(SPacketCustomGuiButton msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.buttonId);
    }

    public static SPacketCustomGuiButton decode(FriendlyByteBuf buf) {
        return new SPacketCustomGuiButton(buf.readUUID());
    }

    @Override
    protected void handle() {
        if (this.player.f_36096_ instanceof ContainerCustomGui container) {
            ICustomGuiComponent comp = container.activeGui.getComponentUuid(this.buttonId);
            if (comp instanceof CustomGuiButtonWrapper button) {
                PlayerWrapper p = (PlayerWrapper) NpcAPI.Instance().getIEntity(this.player);
                button.onPress(container.activeGui);
                EventHooks.onCustomGuiButton(p, container.activeGui, button);
            }
            if (comp instanceof CustomGuiAssetsSelectorWrapper assets) {
                PlayerWrapper p = (PlayerWrapper) NpcAPI.Instance().getIEntity(this.player);
                assets.onPress(container.activeGui);
            }
        }
    }
}