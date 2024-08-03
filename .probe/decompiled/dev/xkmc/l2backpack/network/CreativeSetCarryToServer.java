package dev.xkmc.l2backpack.network;

import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class CreativeSetCarryToServer extends SerialPacketBase {

    @SerialField
    public ItemStack stack;

    @SerialField
    public int count;

    @Deprecated
    public CreativeSetCarryToServer() {
    }

    public CreativeSetCarryToServer(ItemStack stack) {
        this.stack = stack;
    }

    public void handle(NetworkEvent.Context context) {
        ServerPlayer player = context.getSender();
        if (player != null) {
            if (player.m_150110_().instabuild) {
                player.f_36096_.setCarried(this.stack);
            }
        }
    }
}