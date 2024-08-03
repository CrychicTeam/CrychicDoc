package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.client.FTBQuestsNetClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public class DisplayItemRewardToastMessage extends BaseS2CMessage {

    private final ItemStack stack;

    private final int count;

    DisplayItemRewardToastMessage(FriendlyByteBuf buffer) {
        this.stack = buffer.readItem();
        this.count = buffer.readVarInt();
    }

    public DisplayItemRewardToastMessage(ItemStack is, int c) {
        this.stack = is;
        this.count = c;
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.DISPLAY_ITEM_REWARD_TOAST;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeItem(this.stack);
        buffer.writeVarInt(this.count);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        FTBQuestsNetClient.displayItemRewardToast(this.stack, this.count);
    }
}