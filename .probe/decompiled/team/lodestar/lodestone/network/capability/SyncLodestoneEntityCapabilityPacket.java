package team.lodestar.lodestone.network.capability;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import team.lodestar.lodestone.capability.LodestoneEntityDataCapability;
import team.lodestar.lodestone.systems.network.LodestoneTwoWayNBTPacket;
import team.lodestar.lodestone.systems.network.LodestoneTwoWayPacket;

public class SyncLodestoneEntityCapabilityPacket extends LodestoneTwoWayNBTPacket {

    public static final String ENTITY_ID = "entity_id";

    private final int entityID;

    public SyncLodestoneEntityCapabilityPacket(CompoundTag tag) {
        super(tag);
        this.entityID = tag.getInt("entity_id");
    }

    public SyncLodestoneEntityCapabilityPacket(int id, CompoundTag tag) {
        super(handleTag(id, tag));
        this.entityID = id;
    }

    public static CompoundTag handleTag(int id, CompoundTag tag) {
        tag.putInt("entity_id", id);
        return tag;
    }

    @Override
    public void clientExecute(Supplier<NetworkEvent.Context> context, CompoundTag data) {
        Entity entity = Minecraft.getInstance().level.getEntity(this.entityID);
        LodestoneEntityDataCapability.getCapabilityOptional(entity).ifPresent(c -> c.deserializeNBT(data));
    }

    @Override
    public void serverExecute(Supplier<NetworkEvent.Context> context, CompoundTag data) {
        Entity entity = ((NetworkEvent.Context) context.get()).getSender().m_9236_().getEntity(this.entityID);
        LodestoneEntityDataCapability.getCapabilityOptional(entity).ifPresent(c -> c.deserializeNBT(data));
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, SyncLodestoneEntityCapabilityPacket.class, LodestoneTwoWayNBTPacket::encode, SyncLodestoneEntityCapabilityPacket::decode, LodestoneTwoWayPacket::handle);
    }

    public static SyncLodestoneEntityCapabilityPacket decode(FriendlyByteBuf buf) {
        return new SyncLodestoneEntityCapabilityPacket(buf.readNbt());
    }
}