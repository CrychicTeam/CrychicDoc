package team.lodestar.lodestone.systems.worldevent;

import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;
import team.lodestar.lodestone.network.worldevent.SyncWorldEventPacket;
import team.lodestar.lodestone.registry.common.LodestonePacketRegistry;
import team.lodestar.lodestone.registry.common.LodestoneWorldEventTypeRegistry;

public abstract class WorldEventInstance {

    public UUID uuid;

    public WorldEventType type;

    public Level level;

    public boolean discarded;

    public boolean dirty;

    public boolean frozen = false;

    public WorldEventInstance(WorldEventType type) {
        this.uuid = UUID.randomUUID();
        this.type = type;
    }

    public void sync(Level level) {
        if (!level.isClientSide && this.type.isClientSynced()) {
            sync(this);
        }
    }

    public void start(Level level) {
        this.level = level;
    }

    public void tick(Level level) {
    }

    public void end(Level level) {
        this.discarded = true;
    }

    public void setDirty() {
        this.dirty = true;
    }

    public boolean isFrozen() {
        return this.frozen;
    }

    public Level getLevel() {
        return this.level;
    }

    public CompoundTag serializeNBT(CompoundTag tag) {
        tag.putUUID("uuid", this.uuid);
        tag.putString("type", this.type.id.toString());
        tag.putBoolean("discarded", this.discarded);
        tag.putBoolean("frozen", this.frozen);
        return tag;
    }

    public WorldEventInstance deserializeNBT(CompoundTag tag) {
        this.uuid = tag.getUUID("uuid");
        this.type = (WorldEventType) LodestoneWorldEventTypeRegistry.EVENT_TYPES.get(new ResourceLocation(tag.getString("type")));
        this.discarded = tag.getBoolean("discarded");
        this.frozen = tag.getBoolean("frozen");
        return this;
    }

    public CompoundTag synchronizeNBT() {
        return this.serializeNBT(new CompoundTag());
    }

    public static <T extends WorldEventInstance> void sync(T instance) {
        LodestonePacketRegistry.LODESTONE_CHANNEL.send(PacketDistributor.ALL.noArg(), new SyncWorldEventPacket(instance.type.id, true, instance.serializeNBT(new CompoundTag())));
    }

    public static <T extends WorldEventInstance> void sync(T instance, ServerPlayer player) {
        LodestonePacketRegistry.LODESTONE_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SyncWorldEventPacket(instance.type.id, false, instance.serializeNBT(new CompoundTag())));
    }
}