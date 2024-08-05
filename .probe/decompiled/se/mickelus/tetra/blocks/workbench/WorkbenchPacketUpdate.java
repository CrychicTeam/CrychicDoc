package se.mickelus.tetra.blocks.workbench;

import java.io.IOException;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import se.mickelus.mutil.network.AbstractPacket;
import se.mickelus.tetra.module.SchematicRegistry;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

@ParametersAreNonnullByDefault
public class WorkbenchPacketUpdate extends AbstractPacket {

    private BlockPos pos;

    private UpgradeSchematic schematic;

    private String selectedSlot;

    public WorkbenchPacketUpdate() {
    }

    public WorkbenchPacketUpdate(BlockPos pos, UpgradeSchematic schematic, String selectedSlot) {
        this.pos = pos;
        this.schematic = schematic;
        this.selectedSlot = selectedSlot;
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeInt(this.pos.m_123341_());
        buffer.writeInt(this.pos.m_123342_());
        buffer.writeInt(this.pos.m_123343_());
        try {
            if (this.schematic != null) {
                writeString(this.schematic.getKey(), buffer);
            } else {
                writeString("", buffer);
            }
            if (this.selectedSlot != null) {
                writeString(this.selectedSlot, buffer);
            } else {
                writeString("", buffer);
            }
        } catch (IOException var3) {
            System.err.println("An error occurred when writing schematic name to packet buffer");
        }
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
        int x = buffer.readInt();
        int y = buffer.readInt();
        int z = buffer.readInt();
        this.pos = new BlockPos(x, y, z);
        try {
            String schematicKey = readString(buffer);
            this.schematic = SchematicRegistry.getSchematic(schematicKey);
            this.selectedSlot = readString(buffer);
            if ("".equals(this.selectedSlot)) {
                this.selectedSlot = null;
            }
        } catch (IOException var6) {
            System.err.println("An error occurred when reading schematic name from packet buffer");
        }
    }

    @Override
    public void handle(Player player) {
        WorkbenchTile workbench = (WorkbenchTile) player.m_9236_().getBlockEntity(this.pos);
        if (workbench != null) {
            workbench.update(this.schematic, this.selectedSlot, player);
        }
    }
}