package noppes.npcs.packets.client;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.shared.common.PacketBasic;

public class PacketNpcRole extends PacketBasic {

    private final int id;

    private final CompoundTag data;

    public PacketNpcRole(int id, CompoundTag data) {
        this.id = id;
        this.data = data;
    }

    public static void encode(PacketNpcRole msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.id);
        buf.writeNbt(msg.data);
    }

    public static PacketNpcRole decode(FriendlyByteBuf buf) {
        return new PacketNpcRole(buf.readInt(), buf.readNbt());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle() {
        Entity entity = Minecraft.getInstance().level.getEntity(this.id);
        if (entity != null && entity instanceof EntityNPCInterface) {
            ((EntityNPCInterface) entity).advanced.setRole(this.data.getInt("Role"));
            ((EntityNPCInterface) entity).role.load(this.data);
            NoppesUtil.setLastNpc((EntityNPCInterface) entity);
        }
    }
}