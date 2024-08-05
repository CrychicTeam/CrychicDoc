package com.simibubi.create.foundation.ponder.instruction;

import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.PonderWorld;
import com.simibubi.create.foundation.ponder.Selection;
import java.util.function.UnaryOperator;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ReplaceBlocksInstruction extends WorldModifyInstruction {

    private UnaryOperator<BlockState> stateToUse;

    private boolean replaceAir;

    private boolean spawnParticles;

    public ReplaceBlocksInstruction(Selection selection, UnaryOperator<BlockState> stateToUse, boolean replaceAir, boolean spawnParticles) {
        super(selection);
        this.stateToUse = stateToUse;
        this.replaceAir = replaceAir;
        this.spawnParticles = spawnParticles;
    }

    @Override
    protected void runModification(Selection selection, PonderScene scene) {
        PonderWorld world = scene.getWorld();
        selection.forEach(pos -> {
            if (world.getBounds().isInside(pos)) {
                BlockState prevState = world.getBlockState(pos);
                if (this.replaceAir || prevState != Blocks.AIR.defaultBlockState()) {
                    if (this.spawnParticles) {
                        world.addBlockDestroyEffects(pos, prevState);
                    }
                    world.m_46597_(pos, (BlockState) this.stateToUse.apply(prevState));
                }
            }
        });
    }

    @Override
    protected boolean needsRedraw() {
        return true;
    }
}