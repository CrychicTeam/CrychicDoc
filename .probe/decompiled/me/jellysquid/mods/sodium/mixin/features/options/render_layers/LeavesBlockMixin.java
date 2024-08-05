package me.jellysquid.mods.sodium.mixin.features.options.render_layers;

import me.jellysquid.mods.sodium.client.SodiumClientMod;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ LeavesBlock.class })
public class LeavesBlockMixin extends Block {

    public LeavesBlockMixin() {
        super(BlockBehaviour.Properties.copy(Blocks.AIR));
        throw new AssertionError("Mixin constructor called!");
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
        return SodiumClientMod.options().quality.leavesQuality.isFancy(Minecraft.getInstance().options.graphicsMode().get()) ? super.m_6104_(state, stateFrom, direction) : stateFrom.m_60734_() instanceof LeavesBlock || super.m_6104_(state, stateFrom, direction);
    }
}