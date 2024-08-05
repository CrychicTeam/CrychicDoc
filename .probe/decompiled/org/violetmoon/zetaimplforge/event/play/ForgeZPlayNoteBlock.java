package org.violetmoon.zetaimplforge.event.play;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraftforge.event.level.NoteBlockEvent;
import org.violetmoon.zeta.event.play.ZPlayNoteBlock;

public record ForgeZPlayNoteBlock(NoteBlockEvent.Play e) implements ZPlayNoteBlock {

    @Override
    public LevelAccessor getLevel() {
        return this.e.getLevel();
    }

    @Override
    public BlockPos getPos() {
        return this.e.getPos();
    }

    @Override
    public BlockState getState() {
        return this.e.getState();
    }

    @Override
    public int getVanillaNoteId() {
        return this.e.getVanillaNoteId();
    }

    @Override
    public NoteBlockInstrument getInstrument() {
        return this.e.getInstrument();
    }

    @Override
    public boolean isCanceled() {
        return this.e.isCanceled();
    }

    @Override
    public void setCanceled(boolean cancel) {
        this.e.setCanceled(cancel);
    }
}