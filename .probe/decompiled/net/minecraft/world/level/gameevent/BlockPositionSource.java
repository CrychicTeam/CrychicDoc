package net.minecraft.world.level.gameevent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BlockPositionSource implements PositionSource {

    public static final Codec<BlockPositionSource> CODEC = RecordCodecBuilder.create(p_157710_ -> p_157710_.group(BlockPos.CODEC.fieldOf("pos").forGetter(p_223611_ -> p_223611_.pos)).apply(p_157710_, BlockPositionSource::new));

    final BlockPos pos;

    public BlockPositionSource(BlockPos blockPos0) {
        this.pos = blockPos0;
    }

    @Override
    public Optional<Vec3> getPosition(Level level0) {
        return Optional.of(Vec3.atCenterOf(this.pos));
    }

    @Override
    public PositionSourceType<?> getType() {
        return PositionSourceType.BLOCK;
    }

    public static class Type implements PositionSourceType<BlockPositionSource> {

        public BlockPositionSource read(FriendlyByteBuf friendlyByteBuf0) {
            return new BlockPositionSource(friendlyByteBuf0.readBlockPos());
        }

        public void write(FriendlyByteBuf friendlyByteBuf0, BlockPositionSource blockPositionSource1) {
            friendlyByteBuf0.writeBlockPos(blockPositionSource1.pos);
        }

        @Override
        public Codec<BlockPositionSource> codec() {
            return BlockPositionSource.CODEC;
        }
    }
}