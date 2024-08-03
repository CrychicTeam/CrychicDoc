package net.minecraft.network.protocol.game;

import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.ShortSet;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;

public class ClientboundSectionBlocksUpdatePacket implements Packet<ClientGamePacketListener> {

    private static final int POS_IN_SECTION_BITS = 12;

    private final SectionPos sectionPos;

    private final short[] positions;

    private final BlockState[] states;

    public ClientboundSectionBlocksUpdatePacket(SectionPos sectionPos0, ShortSet shortSet1, LevelChunkSection levelChunkSection2) {
        this.sectionPos = sectionPos0;
        int $$3 = shortSet1.size();
        this.positions = new short[$$3];
        this.states = new BlockState[$$3];
        int $$4 = 0;
        for (ShortIterator var6 = shortSet1.iterator(); var6.hasNext(); $$4++) {
            short $$5 = (Short) var6.next();
            this.positions[$$4] = $$5;
            this.states[$$4] = levelChunkSection2.getBlockState(SectionPos.sectionRelativeX($$5), SectionPos.sectionRelativeY($$5), SectionPos.sectionRelativeZ($$5));
        }
    }

    public ClientboundSectionBlocksUpdatePacket(FriendlyByteBuf friendlyByteBuf0) {
        this.sectionPos = SectionPos.of(friendlyByteBuf0.readLong());
        int $$1 = friendlyByteBuf0.readVarInt();
        this.positions = new short[$$1];
        this.states = new BlockState[$$1];
        for (int $$2 = 0; $$2 < $$1; $$2++) {
            long $$3 = friendlyByteBuf0.readVarLong();
            this.positions[$$2] = (short) ((int) ($$3 & 4095L));
            this.states[$$2] = Block.BLOCK_STATE_REGISTRY.byId((int) ($$3 >>> 12));
        }
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeLong(this.sectionPos.asLong());
        friendlyByteBuf0.writeVarInt(this.positions.length);
        for (int $$1 = 0; $$1 < this.positions.length; $$1++) {
            friendlyByteBuf0.writeVarLong((long) Block.getId(this.states[$$1]) << 12 | (long) this.positions[$$1]);
        }
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleChunkBlocksUpdate(this);
    }

    public void runUpdates(BiConsumer<BlockPos, BlockState> biConsumerBlockPosBlockState0) {
        BlockPos.MutableBlockPos $$1 = new BlockPos.MutableBlockPos();
        for (int $$2 = 0; $$2 < this.positions.length; $$2++) {
            short $$3 = this.positions[$$2];
            $$1.set(this.sectionPos.relativeToBlockX($$3), this.sectionPos.relativeToBlockY($$3), this.sectionPos.relativeToBlockZ($$3));
            biConsumerBlockPosBlockState0.accept($$1, this.states[$$2]);
        }
    }
}