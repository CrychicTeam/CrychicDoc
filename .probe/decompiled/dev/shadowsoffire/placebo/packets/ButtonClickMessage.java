package dev.shadowsoffire.placebo.packets;

import dev.shadowsoffire.placebo.network.MessageHelper;
import dev.shadowsoffire.placebo.network.MessageProvider;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class ButtonClickMessage {

    int button;

    public ButtonClickMessage(int button) {
        this.button = button;
    }

    public interface IButtonContainer {

        void onButtonClick(int var1);
    }

    public static class Provider implements MessageProvider<ButtonClickMessage> {

        @Override
        public Class<ButtonClickMessage> getMsgClass() {
            return ButtonClickMessage.class;
        }

        public ButtonClickMessage read(FriendlyByteBuf buf) {
            return new ButtonClickMessage(buf.readInt());
        }

        public void write(ButtonClickMessage msg, FriendlyByteBuf buf) {
            buf.writeInt(msg.button);
        }

        public void handle(ButtonClickMessage msg, Supplier<NetworkEvent.Context> ctx) {
            MessageHelper.handlePacket(() -> {
                if (((NetworkEvent.Context) ctx.get()).getSender().f_36096_ instanceof ButtonClickMessage.IButtonContainer) {
                    ((ButtonClickMessage.IButtonContainer) ((NetworkEvent.Context) ctx.get()).getSender().f_36096_).onButtonClick(msg.button);
                }
            }, ctx);
        }
    }
}