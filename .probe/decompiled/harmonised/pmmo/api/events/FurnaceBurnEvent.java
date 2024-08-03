package harmonised.pmmo.api.events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Event;

public class FurnaceBurnEvent extends Event {

    ItemStack input;

    Level level;

    BlockPos pos;

    public FurnaceBurnEvent(ItemStack input, Level level, BlockPos pos) {
        this.input = input;
        this.level = level;
        this.pos = pos;
    }

    public boolean isCancelable() {
        return false;
    }

    public ItemStack getInput() {
        return this.input;
    }

    public Level getLevel() {
        return this.level;
    }

    public BlockPos getPos() {
        return this.pos;
    }
}