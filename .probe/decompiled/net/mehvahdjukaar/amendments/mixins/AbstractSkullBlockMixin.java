package net.mehvahdjukaar.amendments.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ AbstractSkullBlock.class })
public abstract class AbstractSkullBlockMixin extends BaseEntityBlock {

    protected AbstractSkullBlockMixin(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public SoundType getSoundType(BlockState state, LevelReader world, BlockPos pos, @Nullable Entity entity) {
        return SoundType.BONE_BLOCK;
    }

    @Override
    public SoundType getSoundType(BlockState state) {
        return SoundType.BONE_BLOCK;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}