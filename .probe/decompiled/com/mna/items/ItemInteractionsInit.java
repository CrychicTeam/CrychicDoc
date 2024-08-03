package com.mna.items;

import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.commons.lang3.mutable.MutableBoolean;

@EventBusSubscriber(bus = Bus.MOD)
public class ItemInteractionsInit {

    public static CauldronInteraction LEATHER_TO_VELLUM = (state, world, pos, player, hand, stack) -> {
        if (!world.isClientSide) {
            ItemStack vellum = new ItemStack(ItemInit.VELLUM.get(), 2);
            stack.shrink(1);
            if (!player.addItem(vellum)) {
                player.drop(vellum, true);
            }
            LayeredCauldronBlock.lowerFillLevel(state, world, pos);
        }
        return InteractionResult.sidedSuccess(world.isClientSide);
    };

    static InteractionResult emptyFluidJug(BlockState pBlockState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, ItemStack stack, FluidStack toExtract, BlockState targetState, SoundEvent pFillSound) {
        if (!pLevel.isClientSide()) {
            MutableBoolean success = new MutableBoolean(false);
            FluidUtil.getFluidHandler(stack).ifPresent(h -> {
                if (h.drain(toExtract, IFluidHandler.FluidAction.SIMULATE).getAmount() == toExtract.getAmount()) {
                    h.drain(toExtract, IFluidHandler.FluidAction.EXECUTE);
                    pPlayer.awardStat(Stats.USE_CAULDRON);
                    pPlayer.awardStat(Stats.ITEM_USED.get(ItemInit.FLUID_JUG.get()));
                    pLevel.setBlockAndUpdate(pPos, targetState);
                    pLevel.playSound((Player) null, pPos, pFillSound, SoundSource.BLOCKS, 1.0F, 1.0F);
                    pLevel.m_142346_((Entity) null, GameEvent.FLUID_PICKUP, pPos);
                    success.setTrue();
                }
            });
            return success.getValue() ? InteractionResult.SUCCESS : InteractionResult.FAIL;
        } else {
            return InteractionResult.CONSUME;
        }
    }

    static InteractionResult fillFluidJug(BlockState pBlockState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, ItemStack stack, FluidStack toInsert, Predicate<BlockState> pStatePredicate, SoundEvent pFillSound) {
        if (!pStatePredicate.test(pBlockState)) {
            return InteractionResult.PASS;
        } else if (!pLevel.isClientSide()) {
            MutableBoolean success = new MutableBoolean(false);
            FluidUtil.getFluidHandler(stack).ifPresent(h -> {
                if (h.fill(toInsert, IFluidHandler.FluidAction.SIMULATE) == toInsert.getAmount()) {
                    h.fill(toInsert, IFluidHandler.FluidAction.EXECUTE);
                    pPlayer.awardStat(Stats.USE_CAULDRON);
                    pPlayer.awardStat(Stats.ITEM_USED.get(ItemInit.FLUID_JUG.get()));
                    pLevel.setBlockAndUpdate(pPos, Blocks.CAULDRON.defaultBlockState());
                    pLevel.playSound((Player) null, pPos, pFillSound, SoundSource.BLOCKS, 1.0F, 1.0F);
                    pLevel.m_142346_((Entity) null, GameEvent.FLUID_PICKUP, pPos);
                    success.setTrue();
                }
            });
            return success.getValue() ? InteractionResult.SUCCESS : InteractionResult.FAIL;
        } else {
            return InteractionResult.CONSUME;
        }
    }

    @SubscribeEvent
    public static void setupInteractions(FMLCommonSetupEvent event) {
        CauldronInteraction.WATER.put(Items.LEATHER, LEATHER_TO_VELLUM);
        CauldronInteraction.WATER.put(ItemInit.MAGE_BOOTS.get(), CauldronInteraction.DYED_ITEM);
        CauldronInteraction.WATER.put(ItemInit.MAGE_HOOD.get(), CauldronInteraction.DYED_ITEM);
        CauldronInteraction.WATER.put(ItemInit.MAGE_LEGGINGS.get(), CauldronInteraction.DYED_ITEM);
        CauldronInteraction.WATER.put(ItemInit.MAGE_ROBES.get(), CauldronInteraction.DYED_ITEM);
        CauldronInteraction.WATER.put(ItemInit.RECIPE_COPY_BOOK.get(), CauldronInteraction.DYED_ITEM);
        CauldronInteraction.WATER.put(ItemInit.BANGLE.get(), CauldronInteraction.DYED_ITEM);
        CauldronInteraction.WATER.put(ItemInit.PRACTITIONERS_POUCH.get(), CauldronInteraction.DYED_ITEM);
        CauldronInteraction.WATER.put(ItemInit.SPELL_BOOK.get(), CauldronInteraction.DYED_ITEM);
        CauldronInteraction.WATER.put(ItemInit.ROTE_BOOK.get(), CauldronInteraction.DYED_ITEM);
        CauldronInteraction.WATER.put(ItemInit.GRIMOIRE.get(), CauldronInteraction.DYED_ITEM);
        CauldronInteraction.EMPTY.put(ItemInit.FLUID_JUG.get(), (CauldronInteraction) (blockState, level, pos, player, hand, stack) -> {
            FluidStack contained = ItemInit.FLUID_JUG.get().getFluidTagData(stack);
            if (!contained.isEmpty() && contained.getAmount() >= 1000 && (contained.getFluid() == Fluids.WATER || contained.getFluid() == Fluids.LAVA)) {
                FluidStack toExtract = contained.copy();
                toExtract.setAmount(1000);
                BlockState targetState = contained.getFluid() == Fluids.WATER ? (BlockState) Blocks.WATER_CAULDRON.defaultBlockState().m_61124_(LayeredCauldronBlock.LEVEL, 3) : Blocks.LAVA_CAULDRON.defaultBlockState();
                return emptyFluidJug(blockState, level, pos, player, hand, stack, toExtract, targetState, SoundEvents.BUCKET_EMPTY);
            } else {
                return InteractionResult.FAIL;
            }
        });
        CauldronInteraction.EMPTY.put(ItemInit.FLUID_JUG_INFINITE_WATER.get(), (CauldronInteraction) (blockState, level, pos, player, hand, stack) -> emptyFluidJug(blockState, level, pos, player, hand, stack, new FluidStack(Fluids.WATER, 1000), (BlockState) Blocks.WATER_CAULDRON.defaultBlockState().m_61124_(LayeredCauldronBlock.LEVEL, 3), SoundEvents.BUCKET_EMPTY));
        CauldronInteraction.EMPTY.put(ItemInit.FLUID_JUG_INFINITE_LAVA.get(), (CauldronInteraction) (blockState, level, pos, player, hand, stack) -> emptyFluidJug(blockState, level, pos, player, hand, stack, new FluidStack(Fluids.LAVA, 1000), Blocks.LAVA_CAULDRON.defaultBlockState(), SoundEvents.BUCKET_EMPTY));
        CauldronInteraction.WATER.put(ItemInit.FLUID_JUG.get(), (CauldronInteraction) (blockState, level, pos, player, hand, stack) -> fillFluidJug(blockState, level, pos, player, hand, stack, new FluidStack(Fluids.WATER, 1000), predicateBlockState -> (Integer) predicateBlockState.m_61143_(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_FILL));
        CauldronInteraction.WATER.put(ItemInit.FLUID_JUG_INFINITE_WATER.get(), (CauldronInteraction) (blockState, level, pos, player, hand, stack) -> fillFluidJug(blockState, level, pos, player, hand, stack, new FluidStack(Fluids.WATER, 1000), predicateBlockState -> (Integer) predicateBlockState.m_61143_(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_FILL));
        CauldronInteraction.LAVA.put(ItemInit.FLUID_JUG.get(), (CauldronInteraction) (blockState, level, pos, player, hand, stack) -> fillFluidJug(blockState, level, pos, player, hand, stack, new FluidStack(Fluids.LAVA, 1000), predicateBlockState -> true, SoundEvents.BUCKET_FILL));
        CauldronInteraction.LAVA.put(ItemInit.FLUID_JUG_INFINITE_LAVA.get(), (CauldronInteraction) (blockState, level, pos, player, hand, stack) -> fillFluidJug(blockState, level, pos, player, hand, stack, new FluidStack(Fluids.LAVA, 1000), predicateBlockState -> true, SoundEvents.BUCKET_FILL));
    }
}