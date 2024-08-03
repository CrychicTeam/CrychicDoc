package se.mickelus.tetra.blocks.workbench;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import se.mickelus.mutil.network.BlockPosPacket;
import se.mickelus.mutil.util.CastOptional;

@ParametersAreNonnullByDefault
public class WorkbenchPacketTweak extends BlockPosPacket {

    String slot;

    Map<String, Integer> tweaks;

    public WorkbenchPacketTweak() {
        this.tweaks = new HashMap();
    }

    public WorkbenchPacketTweak(BlockPos pos, String slot, Map<String, Integer> tweaks) {
        super(pos);
        this.slot = slot;
        this.tweaks = tweaks;
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        super.toBytes(buffer);
        try {
            writeString(this.slot, buffer);
        } catch (IOException var3) {
            System.err.println("An error occurred when writing tweak packet to buffer");
        }
        buffer.writeInt(this.tweaks.size());
        this.tweaks.forEach((tweakKey, step) -> {
            try {
                writeString(tweakKey, buffer);
                buffer.writeInt(step);
            } catch (IOException var4) {
                System.err.println("An error occurred when writing tweak packet to buffer");
            }
        });
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
        super.fromBytes(buffer);
        try {
            this.slot = readString(buffer);
            int size = buffer.readInt();
            for (int i = 0; i < size; i++) {
                this.tweaks.put(readString(buffer), buffer.readInt());
            }
        } catch (IOException var4) {
            System.err.println("An error occurred when reading tweak packet from buffer");
        }
    }

    @Override
    public void handle(Player player) {
        CastOptional.cast(player.m_9236_().getBlockEntity(this.pos), WorkbenchTile.class).ifPresent(workbench -> workbench.tweak(player, this.slot, this.tweaks));
    }
}