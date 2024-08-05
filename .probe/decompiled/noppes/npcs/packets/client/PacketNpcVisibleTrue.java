package noppes.npcs.packets.client;

import java.lang.reflect.Constructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PlayMessages;
import noppes.npcs.shared.common.PacketBasic;

public class PacketNpcVisibleTrue extends PacketBasic {

    private static final Constructor<PlayMessages.SpawnEntity> constructor;

    private final PlayMessages.SpawnEntity pkt;

    private final int id;

    public PacketNpcVisibleTrue(Entity entity) {
        this.id = entity.getId();
        PlayMessages.SpawnEntity p = null;
        try {
            p = (PlayMessages.SpawnEntity) constructor.newInstance(entity);
        } catch (Exception var4) {
            var4.printStackTrace();
        }
        this.pkt = p;
    }

    public PacketNpcVisibleTrue(int id, PlayMessages.SpawnEntity pkt) {
        this.id = id;
        this.pkt = pkt;
    }

    public static void encode(PacketNpcVisibleTrue msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.id);
        PlayMessages.SpawnEntity.encode(msg.pkt, buf);
    }

    public static PacketNpcVisibleTrue decode(FriendlyByteBuf buf) {
        return new PacketNpcVisibleTrue(buf.readInt(), PlayMessages.SpawnEntity.decode(buf));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle() {
        ClientLevel w = Minecraft.getInstance().level;
        Entity entity = w.getEntity(this.id);
        if (entity == null) {
            PlayMessages.SpawnEntity.handle(this.pkt, this.ctx);
        }
    }

    static {
        Constructor<PlayMessages.SpawnEntity> con = null;
        try {
            con = PlayMessages.SpawnEntity.class.getDeclaredConstructor(Entity.class);
            con.setAccessible(true);
        } catch (NoSuchMethodException var2) {
            var2.printStackTrace();
        }
        constructor = con;
    }
}