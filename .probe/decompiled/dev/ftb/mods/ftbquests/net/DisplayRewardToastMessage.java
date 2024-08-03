package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftbquests.client.FTBQuestsNetClient;
import dev.ftb.mods.ftbquests.util.NetUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

public class DisplayRewardToastMessage extends BaseS2CMessage {

    private final long id;

    private final Component text;

    private final Icon icon;

    DisplayRewardToastMessage(FriendlyByteBuf buffer) {
        this.id = buffer.readLong();
        this.text = buffer.readComponent();
        this.icon = NetUtils.readIcon(buffer);
    }

    public DisplayRewardToastMessage(long _id, Component t, Icon i) {
        this.id = _id;
        this.text = t;
        this.icon = i;
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.DISPLAY_REWARD_TOAST;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeLong(this.id);
        buffer.writeComponent(this.text);
        NetUtils.writeIcon(buffer, this.icon);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        FTBQuestsNetClient.displayRewardToast(this.id, this.text, this.icon);
    }
}