package net.minecraft.network.protocol.game;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.Heightmap;

public class ClientboundLevelChunkPacketData {

    private static final int TWO_MEGABYTES = 2097152;

    private final CompoundTag heightmaps;

    private final byte[] buffer;

    private final List<ClientboundLevelChunkPacketData.BlockEntityInfo> blockEntitiesData;

    public ClientboundLevelChunkPacketData(LevelChunk levelChunk0) {
        this.heightmaps = new CompoundTag();
        for (Entry<Heightmap.Types, Heightmap> $$1 : levelChunk0.m_6890_()) {
            if (((Heightmap.Types) $$1.getKey()).sendToClient()) {
                this.heightmaps.put(((Heightmap.Types) $$1.getKey()).getSerializationKey(), new LongArrayTag(((Heightmap) $$1.getValue()).getRawData()));
            }
        }
        this.buffer = new byte[calculateChunkSize(levelChunk0)];
        extractChunkData(new FriendlyByteBuf(this.getWriteBuffer()), levelChunk0);
        this.blockEntitiesData = Lists.newArrayList();
        for (Entry<BlockPos, BlockEntity> $$2 : levelChunk0.getBlockEntities().entrySet()) {
            this.blockEntitiesData.add(ClientboundLevelChunkPacketData.BlockEntityInfo.create((BlockEntity) $$2.getValue()));
        }
    }

    public ClientboundLevelChunkPacketData(FriendlyByteBuf friendlyByteBuf0, int int1, int int2) {
        this.heightmaps = friendlyByteBuf0.readNbt();
        if (this.heightmaps == null) {
            throw new RuntimeException("Can't read heightmap in packet for [" + int1 + ", " + int2 + "]");
        } else {
            int $$3 = friendlyByteBuf0.readVarInt();
            if ($$3 > 2097152) {
                throw new RuntimeException("Chunk Packet trying to allocate too much memory on read.");
            } else {
                this.buffer = new byte[$$3];
                friendlyByteBuf0.readBytes(this.buffer);
                this.blockEntitiesData = friendlyByteBuf0.readList(ClientboundLevelChunkPacketData.BlockEntityInfo::new);
            }
        }
    }

    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeNbt(this.heightmaps);
        friendlyByteBuf0.writeVarInt(this.buffer.length);
        friendlyByteBuf0.writeBytes(this.buffer);
        friendlyByteBuf0.writeCollection(this.blockEntitiesData, (p_195672_, p_195673_) -> p_195673_.write(p_195672_));
    }

    private static int calculateChunkSize(LevelChunk levelChunk0) {
        int $$1 = 0;
        for (LevelChunkSection $$2 : levelChunk0.m_7103_()) {
            $$1 += $$2.getSerializedSize();
        }
        return $$1;
    }

    private ByteBuf getWriteBuffer() {
        ByteBuf $$0 = Unpooled.wrappedBuffer(this.buffer);
        $$0.writerIndex(0);
        return $$0;
    }

    public static void extractChunkData(FriendlyByteBuf friendlyByteBuf0, LevelChunk levelChunk1) {
        for (LevelChunkSection $$2 : levelChunk1.m_7103_()) {
            $$2.write(friendlyByteBuf0);
        }
    }

    public Consumer<ClientboundLevelChunkPacketData.BlockEntityTagOutput> getBlockEntitiesTagsConsumer(int int0, int int1) {
        return p_195663_ -> this.getBlockEntitiesTags(p_195663_, int0, int1);
    }

    private void getBlockEntitiesTags(ClientboundLevelChunkPacketData.BlockEntityTagOutput clientboundLevelChunkPacketDataBlockEntityTagOutput0, int int1, int int2) {
        int $$3 = 16 * int1;
        int $$4 = 16 * int2;
        BlockPos.MutableBlockPos $$5 = new BlockPos.MutableBlockPos();
        for (ClientboundLevelChunkPacketData.BlockEntityInfo $$6 : this.blockEntitiesData) {
            int $$7 = $$3 + SectionPos.sectionRelative($$6.packedXZ >> 4);
            int $$8 = $$4 + SectionPos.sectionRelative($$6.packedXZ);
            $$5.set($$7, $$6.y, $$8);
            clientboundLevelChunkPacketDataBlockEntityTagOutput0.accept($$5, $$6.type, $$6.tag);
        }
    }

    public FriendlyByteBuf getReadBuffer() {
        return new FriendlyByteBuf(Unpooled.wrappedBuffer(this.buffer));
    }

    public CompoundTag getHeightmaps() {
        return this.heightmaps;
    }

    static class BlockEntityInfo {

        final int packedXZ;

        final int y;

        final BlockEntityType<?> type;

        @Nullable
        final CompoundTag tag;

        private BlockEntityInfo(int int0, int int1, BlockEntityType<?> blockEntityType2, @Nullable CompoundTag compoundTag3) {
            this.packedXZ = int0;
            this.y = int1;
            this.type = blockEntityType2;
            this.tag = compoundTag3;
        }

        private BlockEntityInfo(FriendlyByteBuf friendlyByteBuf0) {
            this.packedXZ = friendlyByteBuf0.readByte();
            this.y = friendlyByteBuf0.readShort();
            this.type = friendlyByteBuf0.readById(BuiltInRegistries.BLOCK_ENTITY_TYPE);
            this.tag = friendlyByteBuf0.readNbt();
        }

        void write(FriendlyByteBuf friendlyByteBuf0) {
            friendlyByteBuf0.writeByte(this.packedXZ);
            friendlyByteBuf0.writeShort(this.y);
            friendlyByteBuf0.writeId(BuiltInRegistries.BLOCK_ENTITY_TYPE, this.type);
            friendlyByteBuf0.writeNbt(this.tag);
        }

        static ClientboundLevelChunkPacketData.BlockEntityInfo create(BlockEntity blockEntity0) {
            CompoundTag $$1 = blockEntity0.getUpdateTag();
            BlockPos $$2 = blockEntity0.getBlockPos();
            int $$3 = SectionPos.sectionRelative($$2.m_123341_()) << 4 | SectionPos.sectionRelative($$2.m_123343_());
            return new ClientboundLevelChunkPacketData.BlockEntityInfo($$3, $$2.m_123342_(), blockEntity0.getType(), $$1.isEmpty() ? null : $$1);
        }
    }

    @FunctionalInterface
    public interface BlockEntityTagOutput {

        void accept(BlockPos var1, BlockEntityType<?> var2, @Nullable CompoundTag var3);
    }
}