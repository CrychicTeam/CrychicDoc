package dev.xkmc.modulargolems.content.capability;

import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class ConfigUpdateToServer extends SerialPacketBase {

    @SerialField
    public UUID id;

    @SerialField
    public int color;

    @SerialField
    public GolemConfigEntry entry;

    @Deprecated
    public ConfigUpdateToServer() {
    }

    public ConfigUpdateToServer(Level level, GolemConfigEntry entry) {
        this.entry = entry;
        this.id = entry.getID();
        this.color = entry.getColor();
        entry.clientTick(level, true);
    }

    public void handle(NetworkEvent.Context context) {
        ServerPlayer sender = context.getSender();
        if (sender != null) {
            GolemConfigEntry data = GolemConfigStorage.get(sender.serverLevel()).getOrCreateStorage(this.id, this.color, this.entry.init(this.id, this.color).getDisplayName());
            CompoundTag tag = TagCodec.toTag(new CompoundTag(), this.entry);
            assert tag != null;
            TagCodec.fromTag(tag, GolemConfigEntry.class, data, e -> true);
            data.sync(sender.serverLevel());
        }
    }
}