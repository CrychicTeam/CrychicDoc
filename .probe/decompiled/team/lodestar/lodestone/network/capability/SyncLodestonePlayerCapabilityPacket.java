package team.lodestar.lodestone.network.capability;

import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import team.lodestar.lodestone.capability.LodestonePlayerDataCapability;
import team.lodestar.lodestone.systems.network.LodestoneTwoWayNBTPacket;
import team.lodestar.lodestone.systems.network.LodestoneTwoWayPacket;

public class SyncLodestonePlayerCapabilityPacket extends LodestoneTwoWayNBTPacket {

    public static final String PLAYER_UUID = "player_uuid";

    private final UUID uuid;

    public SyncLodestonePlayerCapabilityPacket(CompoundTag tag) {
        super(tag);
        this.uuid = tag.getUUID("player_uuid");
    }

    public SyncLodestonePlayerCapabilityPacket(UUID uuid, CompoundTag tag) {
        super(handleTag(uuid, tag));
        this.uuid = uuid;
    }

    public static CompoundTag handleTag(UUID id, CompoundTag tag) {
        tag.putUUID("player_uuid", id);
        return tag;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void clientExecute(Supplier<NetworkEvent.Context> context, CompoundTag data) {
        Player player = Minecraft.getInstance().level.m_46003_(this.uuid);
        LodestonePlayerDataCapability.getCapabilityOptional(player).ifPresent(c -> c.deserializeNBT(data));
    }

    @Override
    public void serverExecute(Supplier<NetworkEvent.Context> context, CompoundTag data) {
        Player player = ((NetworkEvent.Context) context.get()).getSender().m_9236_().m_46003_(this.uuid);
        LodestonePlayerDataCapability.getCapabilityOptional(player).ifPresent(c -> c.deserializeNBT(data));
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, SyncLodestonePlayerCapabilityPacket.class, LodestoneTwoWayNBTPacket::encode, SyncLodestonePlayerCapabilityPacket::decode, LodestoneTwoWayPacket::handle);
    }

    public static SyncLodestonePlayerCapabilityPacket decode(FriendlyByteBuf buf) {
        return new SyncLodestonePlayerCapabilityPacket(buf.readNbt());
    }
}