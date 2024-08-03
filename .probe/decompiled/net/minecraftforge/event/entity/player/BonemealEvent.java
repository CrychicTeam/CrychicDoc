package net.minecraftforge.event.entity.player;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event.HasResult;
import org.jetbrains.annotations.NotNull;

@Cancelable
@HasResult
public class BonemealEvent extends PlayerEvent {

    private final Level level;

    private final BlockPos pos;

    private final BlockState block;

    private final ItemStack stack;

    public BonemealEvent(@NotNull Player player, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState block, @NotNull ItemStack stack) {
        super(player);
        this.level = level;
        this.pos = pos;
        this.block = block;
        this.stack = stack;
    }

    public Level getLevel() {
        return this.level;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public BlockState getBlock() {
        return this.block;
    }

    @NotNull
    public ItemStack getStack() {
        return this.stack;
    }
}