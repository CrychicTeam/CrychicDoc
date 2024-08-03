package net.mehvahdjukaar.supplementaries.mixins.forge.self;

import net.mehvahdjukaar.moonlight.api.block.ILightable;
import net.mehvahdjukaar.supplementaries.common.block.blocks.GunpowderBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.LightUpBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ GunpowderBlock.class })
public abstract class SelfGunpowderMixin extends LightUpBlock {

    protected SelfGunpowderMixin(BlockBehaviour.Properties arg) {
        super(arg);
    }

    public void onBlockExploded(BlockState state, Level world, BlockPos pos, Explosion explosion) {
        if (!world.isClientSide && this.m_7898_(state, world, pos)) {
            this.lightUp(null, state, pos, world, ILightable.FireSourceType.FLAMING_ARROW);
        } else {
            super.onBlockExploded(state, world, pos, explosion);
        }
    }
}