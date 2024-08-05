package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.BlockBehaviour;

public abstract class StemGrownBlock extends Block {

    public StemGrownBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    public abstract StemBlock getStem();

    public abstract AttachedStemBlock getAttachedStem();
}