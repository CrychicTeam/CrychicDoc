package noppes.npcs.packets.server;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.EventHooks;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiScrollWrapper;
import noppes.npcs.containers.ContainerCustomGui;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketCustomGuiScrollClick extends PacketServerBasic {

    private final UUID id;

    private final int slotId;

    private final boolean doubleClicked;

    public SPacketCustomGuiScrollClick(UUID id, int slotId, boolean doubleClicked) {
        this.id = id;
        this.slotId = slotId;
        this.doubleClicked = doubleClicked;
    }

    @Override
    public boolean toolAllowed(ItemStack item) {
        return true;
    }

    public static void encode(SPacketCustomGuiScrollClick msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.id);
        buf.writeInt(msg.slotId);
        buf.writeBoolean(msg.doubleClicked);
    }

    public static SPacketCustomGuiScrollClick decode(FriendlyByteBuf buf) {
        return new SPacketCustomGuiScrollClick(buf.readUUID(), buf.readInt(), buf.readBoolean());
    }

    @Override
    protected void handle() {
        if (this.player.f_36096_ instanceof ContainerCustomGui container && container.activeGui.getComponentUuid(this.id) instanceof CustomGuiScrollWrapper scroll) {
            if (scroll.isMultiSelect()) {
                List<Integer> list = (List<Integer>) Arrays.stream(scroll.getSelection()).boxed().collect(Collectors.toList());
                if (list.contains(this.slotId)) {
                    list.remove(this.slotId);
                } else {
                    list.add(this.slotId);
                }
                scroll.setSelection(list.stream().mapToInt(Integer::intValue).toArray());
            } else {
                scroll.setSelection(this.slotId);
            }
            if (this.doubleClicked) {
                scroll.onDoubleClick(container.activeGui);
            } else {
                scroll.onClick(container.activeGui);
            }
            PlayerWrapper pw = (PlayerWrapper) NpcAPI.Instance().getIEntity(this.player);
            EventHooks.onCustomGuiScrollClick(pw, container.activeGui, scroll, this.slotId, scroll.getSelectionList(), this.doubleClicked);
        }
    }
}