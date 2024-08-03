package net.minecraft.network.protocol.game;

import com.google.common.collect.Lists;
import java.util.BitSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.SectionPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.world.level.lighting.LevelLightEngine;

public class ClientboundLightUpdatePacketData {

    private final BitSet skyYMask;

    private final BitSet blockYMask;

    private final BitSet emptySkyYMask;

    private final BitSet emptyBlockYMask;

    private final List<byte[]> skyUpdates;

    private final List<byte[]> blockUpdates;

    public ClientboundLightUpdatePacketData(ChunkPos chunkPos0, LevelLightEngine levelLightEngine1, @Nullable BitSet bitSet2, @Nullable BitSet bitSet3) {
        this.skyYMask = new BitSet();
        this.blockYMask = new BitSet();
        this.emptySkyYMask = new BitSet();
        this.emptyBlockYMask = new BitSet();
        this.skyUpdates = Lists.newArrayList();
        this.blockUpdates = Lists.newArrayList();
        for (int $$4 = 0; $$4 < levelLightEngine1.getLightSectionCount(); $$4++) {
            if (bitSet2 == null || bitSet2.get($$4)) {
                this.prepareSectionData(chunkPos0, levelLightEngine1, LightLayer.SKY, $$4, this.skyYMask, this.emptySkyYMask, this.skyUpdates);
            }
            if (bitSet3 == null || bitSet3.get($$4)) {
                this.prepareSectionData(chunkPos0, levelLightEngine1, LightLayer.BLOCK, $$4, this.blockYMask, this.emptyBlockYMask, this.blockUpdates);
            }
        }
    }

    public ClientboundLightUpdatePacketData(FriendlyByteBuf friendlyByteBuf0, int int1, int int2) {
        this.skyYMask = friendlyByteBuf0.readBitSet();
        this.blockYMask = friendlyByteBuf0.readBitSet();
        this.emptySkyYMask = friendlyByteBuf0.readBitSet();
        this.emptyBlockYMask = friendlyByteBuf0.readBitSet();
        this.skyUpdates = friendlyByteBuf0.readList(p_195756_ -> p_195756_.readByteArray(2048));
        this.blockUpdates = friendlyByteBuf0.readList(p_195753_ -> p_195753_.readByteArray(2048));
    }

    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeBitSet(this.skyYMask);
        friendlyByteBuf0.writeBitSet(this.blockYMask);
        friendlyByteBuf0.writeBitSet(this.emptySkyYMask);
        friendlyByteBuf0.writeBitSet(this.emptyBlockYMask);
        friendlyByteBuf0.writeCollection(this.skyUpdates, FriendlyByteBuf::m_130087_);
        friendlyByteBuf0.writeCollection(this.blockUpdates, FriendlyByteBuf::m_130087_);
    }

    private void prepareSectionData(ChunkPos chunkPos0, LevelLightEngine levelLightEngine1, LightLayer lightLayer2, int int3, BitSet bitSet4, BitSet bitSet5, List<byte[]> listByte6) {
        DataLayer $$7 = levelLightEngine1.getLayerListener(lightLayer2).getDataLayerData(SectionPos.of(chunkPos0, levelLightEngine1.getMinLightSection() + int3));
        if ($$7 != null) {
            if ($$7.isEmpty()) {
                bitSet5.set(int3);
            } else {
                bitSet4.set(int3);
                listByte6.add($$7.copy().getData());
            }
        }
    }

    public BitSet getSkyYMask() {
        return this.skyYMask;
    }

    public BitSet getEmptySkyYMask() {
        return this.emptySkyYMask;
    }

    public List<byte[]> getSkyUpdates() {
        return this.skyUpdates;
    }

    public BitSet getBlockYMask() {
        return this.blockYMask;
    }

    public BitSet getEmptyBlockYMask() {
        return this.emptyBlockYMask;
    }

    public List<byte[]> getBlockUpdates() {
        return this.blockUpdates;
    }
}