package se.mickelus.tetra.blocks.forged.extractor;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import se.mickelus.mutil.network.BlockPosPacket;
import se.mickelus.mutil.util.TileEntityOptional;

@ParametersAreNonnullByDefault
public class CoreExtractorPistonUpdatePacket extends BlockPosPacket {

    private long timestamp;

    public CoreExtractorPistonUpdatePacket() {
    }

    public CoreExtractorPistonUpdatePacket(BlockPos pos, long timestamp) {
        super(pos);
        this.timestamp = timestamp;
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        super.toBytes(buffer);
        buffer.writeLong(this.timestamp);
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
        super.fromBytes(buffer);
        this.timestamp = buffer.readLong();
    }

    @Override
    public void handle(Player player) {
        TileEntityOptional.from(player.m_9236_(), this.pos, CoreExtractorPistonBlockEntity.class).ifPresent(tile -> tile.setEndTime(this.timestamp));
    }
}