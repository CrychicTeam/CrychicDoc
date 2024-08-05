package me.jellysquid.mods.lithium.mixin.util.inventory_comparator_tracking;

import me.jellysquid.mods.lithium.common.block.entity.inventory_comparator_tracking.ComparatorTracking;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComparatorBlock;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ComparatorBlock.class })
public abstract class ComparatorBlockMixin extends DiodeBlock {

    protected ComparatorBlockMixin(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Intrinsic
    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onPlace(state, world, pos, oldState, notify);
    }

    @Inject(method = { "onBlockAdded(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Z)V" }, at = { @At("RETURN") })
    private void notifyOnBlockAdded(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify, CallbackInfo ci) {
        if (!oldState.m_60713_(Blocks.COMPARATOR)) {
            ComparatorTracking.notifyNearbyBlockEntitiesAboutNewComparator(world, pos);
        }
    }
}