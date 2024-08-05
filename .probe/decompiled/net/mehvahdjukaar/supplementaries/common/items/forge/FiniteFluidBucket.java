package net.mehvahdjukaar.supplementaries.common.items.forge;

import java.util.Optional;
import java.util.function.Supplier;
import net.mehvahdjukaar.supplementaries.common.fluids.FiniteFluid;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import org.jetbrains.annotations.Nullable;

public class FiniteFluidBucket extends BucketItem {

    public FiniteFluidBucket(Supplier<? extends FiniteFluid> supplier, Item.Properties builder) {
        super(supplier, builder);
    }

    public boolean emptyContents(@Nullable Player arg, Level arg2, BlockPos arg3, @Nullable BlockHitResult arg4, @Nullable ItemStack container) {
        Fluid content;
        BlockState blockstate;
        Block block;
        boolean flag;
        boolean var10000;
        label82: {
            content = this.getFluid();
            blockstate = arg2.getBlockState(arg3);
            block = blockstate.m_60734_();
            flag = blockstate.m_60722_(content);
            label72: if (!blockstate.m_60795_() && !flag) {
                if (block instanceof LiquidBlockContainer lc && lc.canPlaceLiquid(arg2, arg3, blockstate, content)) {
                    break label72;
                }
                var10000 = false;
                break label82;
            }
            var10000 = true;
        }
        boolean flag1 = var10000;
        Optional<FluidStack> containedFluidStack = Optional.ofNullable(container).flatMap(FluidUtil::getFluidContained);
        if (!flag1) {
            return arg4 != null && this.emptyContents(arg, arg2, arg4.getBlockPos().relative(arg4.getDirection()), null, container);
        } else if (containedFluidStack.isPresent() && content.getFluidType().isVaporizedOnPlacement(arg2, arg3, (FluidStack) containedFluidStack.get())) {
            content.getFluidType().onVaporize(arg, arg2, arg3, (FluidStack) containedFluidStack.get());
            return true;
        } else if (arg2.dimensionType().ultraWarm() && content.is(FluidTags.WATER)) {
            int i = arg3.m_123341_();
            int j = arg3.m_123342_();
            int k = arg3.m_123343_();
            arg2.playSound(arg, arg3, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (arg2.random.nextFloat() - arg2.random.nextFloat()) * 0.8F);
            for (int l = 0; l < 8; l++) {
                arg2.addParticle(ParticleTypes.LARGE_SMOKE, (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0, 0.0, 0.0);
            }
            return true;
        } else {
            if (block instanceof LiquidBlockContainer lc && lc.canPlaceLiquid(arg2, arg3, blockstate, content)) {
                lc.placeLiquid(arg2, arg3, blockstate, content.defaultFluidState());
                this.m_7718_(arg, arg2, arg3);
                return true;
            }
            if (!arg2.isClientSide && flag && !blockstate.m_278721_()) {
                arg2.m_46961_(arg3, true);
            }
            if (!arg2.setBlock(arg3, content.defaultFluidState().createLegacyBlock(), 11) && !blockstate.m_60819_().isSource()) {
                return false;
            } else {
                this.m_7718_(arg, arg2, arg3);
                return true;
            }
        }
    }
}