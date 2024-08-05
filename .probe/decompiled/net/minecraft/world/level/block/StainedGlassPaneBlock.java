package net.minecraft.world.level.block;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class StainedGlassPaneBlock extends IronBarsBlock implements BeaconBeamBlock {

    private final DyeColor color;

    public StainedGlassPaneBlock(DyeColor dyeColor0, BlockBehaviour.Properties blockBehaviourProperties1) {
        super(blockBehaviourProperties1);
        this.color = dyeColor0;
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(f_52309_, false)).m_61124_(f_52310_, false)).m_61124_(f_52311_, false)).m_61124_(f_52312_, false)).m_61124_(f_52313_, false));
    }

    @Override
    public DyeColor getColor() {
        return this.color;
    }
}