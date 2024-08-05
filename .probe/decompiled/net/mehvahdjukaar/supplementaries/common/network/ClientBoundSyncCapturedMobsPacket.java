package net.mehvahdjukaar.supplementaries.common.network;

import com.mojang.serialization.DataResult;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.mehvahdjukaar.moonlight.api.platform.network.ChannelHandler;
import net.mehvahdjukaar.moonlight.api.platform.network.Message;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.misc.mob_container.CapturedMobHandler;
import net.mehvahdjukaar.supplementaries.common.misc.mob_container.DataDefinedCatchableMob;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

public class ClientBoundSyncCapturedMobsPacket implements Message {

    protected final Set<DataDefinedCatchableMob> mobSet;

    @Nullable
    protected final DataDefinedCatchableMob fish;

    public ClientBoundSyncCapturedMobsPacket(Set<DataDefinedCatchableMob> mobMap, @Nullable DataDefinedCatchableMob fish) {
        this.mobSet = mobMap;
        this.fish = fish;
    }

    public ClientBoundSyncCapturedMobsPacket(FriendlyByteBuf buf) {
        int size = buf.readInt();
        this.mobSet = new HashSet();
        for (int i = 0; i < size; i++) {
            CompoundTag tag = buf.readNbt();
            if (tag != null) {
                DataResult<DataDefinedCatchableMob> r = DataDefinedCatchableMob.CODEC.parse(NbtOps.INSTANCE, tag);
                if (r.result().isPresent()) {
                    this.mobSet.add((DataDefinedCatchableMob) r.result().get());
                }
            }
        }
        if (buf.readBoolean()) {
            CompoundTag tag = buf.readNbt();
            DataResult<DataDefinedCatchableMob> r = DataDefinedCatchableMob.CODEC.parse(NbtOps.INSTANCE, tag);
            if (r.result().isPresent()) {
                this.fish = (DataDefinedCatchableMob) r.result().get();
            } else {
                this.fish = null;
            }
        } else {
            this.fish = null;
        }
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf buf) {
        List<CompoundTag> tags = new ArrayList();
        for (DataDefinedCatchableMob entry : this.mobSet) {
            if (entry == null) {
                Supplementaries.LOGGER.error("Found a null captured mob property. How??");
            } else {
                DataResult<Tag> r = DataDefinedCatchableMob.CODEC.encodeStart(NbtOps.INSTANCE, entry);
                if (r.result().isPresent()) {
                    tags.add((CompoundTag) r.result().get());
                }
            }
        }
        buf.writeInt(tags.size());
        tags.forEach(buf::m_130079_);
        if (this.fish != null) {
            DataResult<Tag> r = DataDefinedCatchableMob.CODEC.encodeStart(NbtOps.INSTANCE, this.fish);
            if (r.result().isPresent()) {
                buf.writeBoolean(true);
                buf.writeNbt((CompoundTag) r.result().get());
                return;
            }
        }
        buf.writeBoolean(false);
    }

    @Override
    public void handle(ChannelHandler.Context context) {
        CapturedMobHandler.acceptClientData(this.mobSet, this.fish);
        Supplementaries.LOGGER.info("Synced Captured Mobs settings");
    }
}