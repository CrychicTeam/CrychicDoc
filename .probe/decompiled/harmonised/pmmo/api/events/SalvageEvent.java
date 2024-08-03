package harmonised.pmmo.api.events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class SalvageEvent extends PlayerEvent {

    BlockPos blockPos;

    public SalvageEvent(Player player, BlockPos blockPos) {
        super(player);
        this.blockPos = blockPos;
    }

    public boolean isCancelable() {
        return true;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }
}