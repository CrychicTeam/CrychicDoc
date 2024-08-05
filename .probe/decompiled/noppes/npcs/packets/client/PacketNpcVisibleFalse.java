package noppes.npcs.packets.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.shared.common.PacketBasic;

public class PacketNpcVisibleFalse extends PacketBasic {

    private final int id;

    public PacketNpcVisibleFalse(int id) {
        this.id = id;
    }

    public static void encode(PacketNpcVisibleFalse msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.id);
    }

    public static PacketNpcVisibleFalse decode(FriendlyByteBuf buf) {
        return new PacketNpcVisibleFalse(buf.readInt());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle() {
        ClientLevel w = Minecraft.getInstance().level;
        Entity entity = w.getEntity(this.id);
        if (entity != null && entity instanceof EntityNPCInterface) {
            w.removeEntity(this.id, Entity.RemovalReason.DISCARDED);
        }
    }
}