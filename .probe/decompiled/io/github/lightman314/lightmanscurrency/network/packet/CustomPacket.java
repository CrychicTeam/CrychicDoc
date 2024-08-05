package io.github.lightman314.lightmanscurrency.network.packet;

import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public abstract class CustomPacket {

    public abstract void encode(@Nonnull FriendlyByteBuf var1);

    public abstract static class Handler<T extends CustomPacket> {

        @Nonnull
        public abstract T decode(@Nonnull FriendlyByteBuf var1);

        public final void handlePacket(@Nonnull T message, @Nonnull Supplier<NetworkEvent.Context> supplier) {
            NetworkEvent.Context context = (NetworkEvent.Context) supplier.get();
            context.enqueueWork(() -> this.handle(message, context.getSender()));
            context.setPacketHandled(true);
        }

        protected abstract void handle(@Nonnull T var1, @Nullable ServerPlayer var2);
    }

    public abstract static class SimpleHandler<T extends CustomPacket> extends CustomPacket.Handler<T> {

        protected final T instance;

        protected SimpleHandler(@Nonnull T instance) {
            this.instance = instance;
        }

        @Nonnull
        @Override
        public final T decode(@Nonnull FriendlyByteBuf buffer) {
            return this.instance;
        }
    }
}