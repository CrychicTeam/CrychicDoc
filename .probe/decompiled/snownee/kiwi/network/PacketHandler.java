package snownee.kiwi.network;

import io.netty.buffer.Unpooled;
import java.util.function.Consumer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

public abstract class PacketHandler implements IPacketHandler {

    public ResourceLocation id;

    private KiwiPacket.Direction direction;

    void setModId(String modId) {
        KiwiPacket annotation = (KiwiPacket) this.getClass().getDeclaredAnnotation(KiwiPacket.class);
        String v = annotation.value();
        if (v.contains(":")) {
            this.id = new ResourceLocation(v);
        } else {
            this.id = new ResourceLocation(modId, v);
        }
        this.direction = annotation.dir();
    }

    @Override
    public KiwiPacket.Direction getDirection() {
        return this.direction;
    }

    @Deprecated
    public void send(PacketDistributor.PacketTarget target, Consumer<FriendlyByteBuf> buf) {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer()).writeResourceLocation(this.id);
        buf.accept(buffer);
        Networking.send(target, buffer);
    }

    public void send(ServerPlayer player, Consumer<FriendlyByteBuf> buf) {
        this.send(() -> PacketDistributor.PLAYER.with(() -> player), buf);
    }

    @Deprecated
    public void sendAllExcept(ServerPlayer player, Consumer<FriendlyByteBuf> buf) {
        this.send(KPacketTarget.allExcept(player), buf);
    }

    public void sendToServer(Consumer<FriendlyByteBuf> buf) {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer()).writeResourceLocation(this.id);
        buf.accept(buffer);
        Networking.sendToServer(buffer);
    }

    public void send(KPacketTarget target, Consumer<FriendlyByteBuf> buf) {
        target.send(this, buf);
    }
}