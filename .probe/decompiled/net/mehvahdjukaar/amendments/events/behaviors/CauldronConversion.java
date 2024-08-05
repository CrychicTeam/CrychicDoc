package net.mehvahdjukaar.amendments.events.behaviors;

import com.mojang.datafixers.util.Pair;
import net.mehvahdjukaar.amendments.common.block.LiquidCauldronBlock;
import net.mehvahdjukaar.amendments.common.tile.LiquidCauldronBlockTile;
import net.mehvahdjukaar.amendments.configs.CommonConfigs;
import net.mehvahdjukaar.amendments.integration.CompatHandler;
import net.mehvahdjukaar.amendments.reg.ModRegistry;
import net.mehvahdjukaar.amendments.reg.ModTags;
import net.mehvahdjukaar.moonlight.api.fluids.BuiltInSoftFluids;
import net.mehvahdjukaar.moonlight.api.fluids.FluidContainerList;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidTank;
import net.mehvahdjukaar.moonlight.api.util.DispenserHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class CauldronConversion implements BlockUse {

    @Override
    public boolean isEnabled() {
        return (Boolean) CommonConfigs.LIQUID_CAULDRON.get();
    }

    @Override
    public boolean appliesToBlock(Block block) {
        return Blocks.CAULDRON == block;
    }

    @Override
    public InteractionResult tryPerformingAction(BlockState state, BlockPos pos, Level level, Player player, InteractionHand hand, ItemStack stack, BlockHitResult hit) {
        return player.isSecondaryUseActive() ? InteractionResult.PASS : convert(state, pos, level, player, hand, stack, true);
    }

    public static InteractionResult convert(BlockState state, BlockPos pos, Level level, Player player, InteractionHand hand, ItemStack stack, boolean checkCauldronInteractions) {
        BlockState newState = getNewState(pos, level, stack, checkCauldronInteractions);
        if (newState != null) {
            level.setBlockAndUpdate(pos, newState);
            if (level.getBlockEntity(pos) instanceof LiquidCauldronBlockTile te) {
                if (te.handleInteraction(player, hand)) {
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
                level.setBlockAndUpdate(pos, state);
                return InteractionResult.PASS;
            }
        }
        return InteractionResult.PASS;
    }

    public static BlockState getNewState(BlockPos pos, Level level, ItemStack stack) {
        return getNewState(pos, level, stack, true);
    }

    @Nullable
    public static BlockState getNewState(BlockPos pos, Level level, ItemStack stack, boolean checkCauldronInteractions) {
        Pair<SoftFluidStack, FluidContainerList.Category> fluid = SoftFluidStack.fromItem(stack);
        if (fluid == null) {
            return null;
        } else {
            SoftFluidStack first = (SoftFluidStack) fluid.getFirst();
            if (checkCauldronInteractions && ((CauldronBlock) Blocks.CAULDRON).f_151943_.containsKey(stack.getItem()) && !first.is(BuiltInSoftFluids.POTION.get())) {
                return null;
            } else {
                return CompatHandler.RATS && stack.is(Items.MILK_BUCKET) ? null : getNewState(pos, level, first);
            }
        }
    }

    @Nullable
    public static BlockState getNewState(BlockPos pos, Level level, SoftFluidStack fluid) {
        if (fluid != null && !fluid.is(ModTags.CAULDRON_BLACKLIST)) {
            BlockState newState;
            if (fluid.is(ModRegistry.DYE_SOFT_FLUID.get())) {
                newState = ((Block) ModRegistry.DYE_CAULDRON.get()).defaultBlockState();
            } else {
                newState = (BlockState) ((LiquidCauldronBlock) ModRegistry.LIQUID_CAULDRON.get()).m_49966_().m_61124_(LiquidCauldronBlock.BOILING, LiquidCauldronBlock.shouldBoil(level.getBlockState(pos.below()), fluid));
            }
            return newState;
        } else {
            return null;
        }
    }

    public static class DispenserBehavior extends DispenserHelper.AdditionalDispenserBehavior {

        public DispenserBehavior(Item item) {
            super(item);
        }

        @Override
        protected InteractionResultHolder<ItemStack> customBehavior(BlockSource source, ItemStack stack) {
            ServerLevel level = source.getLevel();
            BlockState originalState = source.getBlockState();
            BlockPos pos = source.getPos().relative((Direction) originalState.m_61143_(DispenserBlock.FACING));
            if (!originalState.m_60713_(Blocks.CAULDRON)) {
                return InteractionResultHolder.pass(stack);
            } else {
                BlockState newState = CauldronConversion.getNewState(pos, level, stack);
                if (newState != null) {
                    level.m_46597_(pos, newState);
                    if (level.m_7702_(pos) instanceof LiquidCauldronBlockTile te) {
                        SoftFluidTank tank = te.getSoftFluidTank();
                        ItemStack returnStack = tank.interactWithItem(stack, level, pos, false);
                        if (returnStack != null) {
                            return InteractionResultHolder.success(returnStack);
                        }
                        level.m_46597_(pos, originalState);
                    }
                }
                return InteractionResultHolder.pass(stack);
            }
        }
    }
}