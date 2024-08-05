package com.simibubi.create.content.contraptions.pulley;

import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.ContraptionType;
import com.simibubi.create.content.contraptions.TranslatingContraption;
import com.simibubi.create.content.contraptions.render.ContraptionLighter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PulleyContraption extends TranslatingContraption {

    int initialOffset;

    @Override
    public ContraptionType getType() {
        return ContraptionType.PULLEY;
    }

    public PulleyContraption() {
    }

    public PulleyContraption(int initialOffset) {
        this.initialOffset = initialOffset;
    }

    @Override
    public boolean assemble(Level world, BlockPos pos) throws AssemblyException {
        if (!this.searchMovedStructure(world, pos, null)) {
            return false;
        } else {
            this.startMoving(world);
            return true;
        }
    }

    @Override
    protected boolean isAnchoringBlockAt(BlockPos pos) {
        if (pos.m_123341_() == this.anchor.m_123341_() && pos.m_123343_() == this.anchor.m_123343_()) {
            int y = pos.m_123342_();
            return y > this.anchor.m_123342_() && y <= this.anchor.m_123342_() + this.initialOffset + 1;
        } else {
            return false;
        }
    }

    @Override
    public CompoundTag writeNBT(boolean spawnPacket) {
        CompoundTag tag = super.writeNBT(spawnPacket);
        tag.putInt("InitialOffset", this.initialOffset);
        return tag;
    }

    @Override
    public void readNBT(Level world, CompoundTag nbt, boolean spawnData) {
        this.initialOffset = nbt.getInt("InitialOffset");
        super.readNBT(world, nbt, spawnData);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ContraptionLighter<?> makeLighter() {
        return new PulleyLighter(this);
    }

    public int getInitialOffset() {
        return this.initialOffset;
    }
}