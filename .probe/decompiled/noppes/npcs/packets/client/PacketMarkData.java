package noppes.npcs.packets.client;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noppes.npcs.controllers.data.MarkData;
import noppes.npcs.shared.common.PacketBasic;

public class PacketMarkData extends PacketBasic {

    private final int id;

    private final CompoundTag data;

    public PacketMarkData(int id, CompoundTag data) {
        this.id = id;
        this.data = data;
    }

    public static void encode(PacketMarkData msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.id);
        buf.writeNbt(msg.data);
    }

    public static PacketMarkData decode(FriendlyByteBuf buf) {
        return new PacketMarkData(buf.readInt(), buf.readNbt());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle() {
        Entity entity = Minecraft.getInstance().level.getEntity(this.id);
        if (entity != null && entity instanceof LivingEntity) {
            MarkData mark = MarkData.get((LivingEntity) entity);
            mark.setNBT(this.data);
        }
    }
}