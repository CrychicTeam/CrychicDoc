package fuzs.puzzleslib.api.network.v2;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public interface MessageV2<T extends MessageV2<T>> {

    void write(FriendlyByteBuf var1);

    void read(FriendlyByteBuf var1);

    MessageV2.MessageHandler<T> makeHandler();

    public abstract static class MessageHandler<T extends MessageV2<T>> {

        public abstract void handle(T var1, Player var2, Object var3);
    }
}