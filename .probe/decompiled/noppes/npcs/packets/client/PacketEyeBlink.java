package noppes.npcs.packets.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noppes.npcs.ModelData;
import noppes.npcs.ModelEyeData;
import noppes.npcs.client.parts.MpmPartData;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.shared.common.PacketBasic;

public class PacketEyeBlink extends PacketBasic {

    private final int id;

    public PacketEyeBlink(int id) {
        this.id = id;
    }

    public static void encode(PacketEyeBlink msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.id);
    }

    public static PacketEyeBlink decode(FriendlyByteBuf buf) {
        return new PacketEyeBlink(buf.readInt());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle() {
        Entity entity = Minecraft.getInstance().level.getEntity(this.id);
        if (entity != null && entity instanceof EntityNPCInterface) {
            ModelData data = ((EntityCustomNpc) entity).modelData;
            for (MpmPartData pd : data.mpmParts) {
                if (pd instanceof ModelEyeData) {
                    ((ModelEyeData) pd).blinkStart = System.currentTimeMillis();
                }
            }
        }
    }
}