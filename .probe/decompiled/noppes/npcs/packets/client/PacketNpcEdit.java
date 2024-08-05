package noppes.npcs.packets.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.shared.common.PacketBasic;

public class PacketNpcEdit extends PacketBasic {

    private final int id;

    public PacketNpcEdit(int id) {
        this.id = id;
    }

    public static void encode(PacketNpcEdit msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.id);
    }

    public static PacketNpcEdit decode(FriendlyByteBuf buf) {
        return new PacketNpcEdit(buf.readInt());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle() {
        Entity entity = Minecraft.getInstance().level.getEntity(this.id);
        if (entity != null && entity instanceof EntityNPCInterface) {
            NoppesUtil.setLastNpc((EntityNPCInterface) entity);
        } else {
            NoppesUtil.setLastNpc(null);
        }
    }
}