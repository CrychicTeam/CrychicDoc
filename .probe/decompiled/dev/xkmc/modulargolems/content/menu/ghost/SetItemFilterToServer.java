package dev.xkmc.modulargolems.content.menu.ghost;

import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class SetItemFilterToServer extends SerialPacketBase {

    @SerialField
    private ItemStack stack;

    @SerialField
    private int slot;

    @Deprecated
    public SetItemFilterToServer() {
    }

    protected SetItemFilterToServer(int slot, ItemStack stack) {
        this.slot = slot;
        this.stack = stack;
    }

    public void handle(NetworkEvent.Context context) {
        ServerPlayer sender = context.getSender();
        if (sender != null) {
            if (sender.f_36096_ instanceof GhostItemMenu menu) {
                menu.setSlotContent(this.slot, this.stack);
            }
        }
    }
}