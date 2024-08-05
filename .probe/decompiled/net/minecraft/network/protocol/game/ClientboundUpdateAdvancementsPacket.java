package net.minecraft.network.protocol.game;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;

public class ClientboundUpdateAdvancementsPacket implements Packet<ClientGamePacketListener> {

    private final boolean reset;

    private final Map<ResourceLocation, Advancement.Builder> added;

    private final Set<ResourceLocation> removed;

    private final Map<ResourceLocation, AdvancementProgress> progress;

    public ClientboundUpdateAdvancementsPacket(boolean boolean0, Collection<Advancement> collectionAdvancement1, Set<ResourceLocation> setResourceLocation2, Map<ResourceLocation, AdvancementProgress> mapResourceLocationAdvancementProgress3) {
        this.reset = boolean0;
        Builder<ResourceLocation, Advancement.Builder> $$4 = ImmutableMap.builder();
        for (Advancement $$5 : collectionAdvancement1) {
            $$4.put($$5.getId(), $$5.deconstruct());
        }
        this.added = $$4.build();
        this.removed = ImmutableSet.copyOf(setResourceLocation2);
        this.progress = ImmutableMap.copyOf(mapResourceLocationAdvancementProgress3);
    }

    public ClientboundUpdateAdvancementsPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.reset = friendlyByteBuf0.readBoolean();
        this.added = friendlyByteBuf0.readMap(FriendlyByteBuf::m_130281_, Advancement.Builder::m_138401_);
        this.removed = friendlyByteBuf0.readCollection(Sets::newLinkedHashSetWithExpectedSize, FriendlyByteBuf::m_130281_);
        this.progress = friendlyByteBuf0.readMap(FriendlyByteBuf::m_130281_, AdvancementProgress::m_8211_);
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeBoolean(this.reset);
        friendlyByteBuf0.writeMap(this.added, FriendlyByteBuf::m_130085_, (p_179441_, p_179442_) -> p_179442_.serializeToNetwork(p_179441_));
        friendlyByteBuf0.writeCollection(this.removed, FriendlyByteBuf::m_130085_);
        friendlyByteBuf0.writeMap(this.progress, FriendlyByteBuf::m_130085_, (p_179444_, p_179445_) -> p_179445_.serializeToNetwork(p_179444_));
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleUpdateAdvancementsPacket(this);
    }

    public Map<ResourceLocation, Advancement.Builder> getAdded() {
        return this.added;
    }

    public Set<ResourceLocation> getRemoved() {
        return this.removed;
    }

    public Map<ResourceLocation, AdvancementProgress> getProgress() {
        return this.progress;
    }

    public boolean shouldReset() {
        return this.reset;
    }
}