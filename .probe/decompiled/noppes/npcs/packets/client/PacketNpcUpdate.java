package noppes.npcs.packets.client;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.shared.common.PacketBasic;

public class PacketNpcUpdate extends PacketBasic {

    private final int id;

    private final CompoundTag data;

    public PacketNpcUpdate(int id, CompoundTag data) {
        this.id = id;
        this.data = data;
    }

    public static void encode(PacketNpcUpdate msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.id);
        buf.writeNbt(msg.data);
    }

    public static PacketNpcUpdate decode(FriendlyByteBuf buf) {
        return new PacketNpcUpdate(buf.readInt(), buf.readNbt());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle() {
        Entity entity = Minecraft.getInstance().level.getEntity(this.id);
        if (entity != null && entity instanceof EntityNPCInterface) {
            ((EntityNPCInterface) entity).readSpawnData(this.data);
        }
    }
}