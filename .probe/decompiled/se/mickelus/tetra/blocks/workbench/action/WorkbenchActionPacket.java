package se.mickelus.tetra.blocks.workbench.action;

import java.io.IOException;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import se.mickelus.mutil.network.BlockPosPacket;
import se.mickelus.tetra.blocks.workbench.WorkbenchTile;

@ParametersAreNonnullByDefault
public class WorkbenchActionPacket extends BlockPosPacket {

    private String actionKey;

    public WorkbenchActionPacket() {
    }

    public WorkbenchActionPacket(BlockPos pos, String actionKey) {
        super(pos);
        this.actionKey = actionKey;
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        super.toBytes(buffer);
        try {
            writeString(this.actionKey, buffer);
        } catch (IOException var3) {
            System.err.println("An error occurred when writing action name to packet buffer");
        }
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
        super.fromBytes(buffer);
        try {
            this.actionKey = readString(buffer);
        } catch (IOException var3) {
            System.err.println("An error occurred when reading action name from packet buffer");
        }
    }

    @Override
    public void handle(Player player) {
        WorkbenchTile workbench = (WorkbenchTile) player.m_9236_().getBlockEntity(this.pos);
        if (workbench != null) {
            workbench.performAction(player, this.actionKey);
        }
    }
}