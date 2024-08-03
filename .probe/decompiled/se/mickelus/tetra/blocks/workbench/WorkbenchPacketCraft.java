package se.mickelus.tetra.blocks.workbench;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import se.mickelus.mutil.network.BlockPosPacket;

@ParametersAreNonnullByDefault
public class WorkbenchPacketCraft extends BlockPosPacket {

    public WorkbenchPacketCraft() {
    }

    public WorkbenchPacketCraft(BlockPos pos) {
        super(pos);
    }

    @Override
    public void handle(Player player) {
        WorkbenchTile workbench = (WorkbenchTile) player.m_9236_().getBlockEntity(this.pos);
        if (workbench != null) {
            workbench.craft(player);
        }
    }
}