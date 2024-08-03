package dev.xkmc.l2backpack.content.remote.player;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2backpack.events.TooltipUpdateEvents;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class EnderSyncPacket extends SerialPacketBase {

    @SerialField
    public ArrayList<EnderSyncPacket.Entry> list = new ArrayList();

    @Deprecated
    public EnderSyncPacket() {
    }

    public EnderSyncPacket(List<Pair<Integer, ItemStack>> list) {
        for (Pair<Integer, ItemStack> e : list) {
            this.list.add(new EnderSyncPacket.Entry((Integer) e.getFirst(), (ItemStack) e.getSecond()));
        }
    }

    public void handle(NetworkEvent.Context context) {
        for (EnderSyncPacket.Entry e : this.list) {
            TooltipUpdateEvents.onEnderSync(e.slot(), e.stack());
        }
    }

    public static record Entry(int slot, ItemStack stack) {
    }
}