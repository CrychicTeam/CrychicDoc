package noppes.npcs.packets.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.shared.common.PacketBasic;

public class PacketNpcDelete extends PacketBasic {

    private final int id;

    public PacketNpcDelete(int id) {
        this.id = id;
    }

    public static void encode(PacketNpcDelete msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.id);
    }

    public static PacketNpcDelete decode(FriendlyByteBuf buf) {
        return new PacketNpcDelete(buf.readInt());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle() {
        Entity entity = Minecraft.getInstance().level.getEntity(this.id);
        if (entity != null && entity instanceof EntityNPCInterface) {
            ((EntityNPCInterface) entity).delete();
        }
    }
}