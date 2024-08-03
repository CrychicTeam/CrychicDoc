package dev.xkmc.l2backpack.network;

import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class CreativeSetCarryToClient extends SerialPacketBase {

    @SerialField
    public Item item;

    @SerialField
    public int count;

    @Deprecated
    public CreativeSetCarryToClient() {
    }

    public CreativeSetCarryToClient(ItemStack stack) {
        this.item = stack.getItem();
        this.count = stack.getCount();
    }

    public void handle(NetworkEvent.Context context) {
        Proxy.getClientPlayer().f_36096_.setCarried(new ItemStack(this.item, this.count));
    }
}