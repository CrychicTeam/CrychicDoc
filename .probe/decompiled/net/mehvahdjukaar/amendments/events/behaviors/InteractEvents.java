package net.mehvahdjukaar.amendments.events.behaviors;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.mehvahdjukaar.amendments.integration.CompatHandler;
import net.mehvahdjukaar.amendments.integration.FlanCompat;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class InteractEvents {

    private static final Map<Item, ItemUseOnBlock> ITEM_USE_ON_BLOCK_HP = new IdentityHashMap();

    private static final Multimap<Item, ItemUseOnBlock> ITEM_USE_ON_BLOCK = HashMultimap.create();

    private static final Map<Item, ItemUse> ITEM_USE = new IdentityHashMap();

    private static final Map<Block, BlockUse> BLOCK_USE = new IdentityHashMap();

    public static boolean hasBlockPlacementAssociated(Item item) {
        return false;
    }

    public static void setupOverrides() {
        ITEM_USE.clear();
        ITEM_USE_ON_BLOCK_HP.clear();
        ITEM_USE_ON_BLOCK.clear();
        BLOCK_USE.clear();
        List<ItemUseOnBlock> itemUseOnBlockHP = new ArrayList();
        List<ItemUseOnBlock> itemUseOnBlock = new ArrayList();
        List<ItemUse> itemUse = new ArrayList();
        List<BlockUse> blockUse = new ArrayList();
        blockUse.add(new DirectionalCakeConversion());
        blockUse.add(new BellChainRing());
        blockUse.add(new CauldronDyeWater());
        blockUse.add(new ToolHookConversion());
        itemUseOnBlockHP.add(new SkullCandleConversion());
        itemUseOnBlockHP.add(new DyeBehavior());
        itemUseOnBlock.add(new SkullPileConversion());
        itemUseOnBlock.add(new DoubleCakeConversion());
        itemUseOnBlock.add(new CarpetStairsConversion());
        itemUseOnBlock.add(new CarpetSlabConversion());
        label65: for (Item i : BuiltInRegistries.ITEM) {
            for (ItemUseOnBlock b : itemUseOnBlock) {
                if (b.appliesToItem(i)) {
                    ITEM_USE_ON_BLOCK.put(i, b);
                }
            }
            for (ItemUse bx : itemUse) {
                if (bx.appliesToItem(i)) {
                    ITEM_USE.put(i, bx);
                    continue label65;
                }
            }
            Iterator var11 = itemUseOnBlockHP.iterator();
            ItemUseOnBlock bxx;
            do {
                if (!var11.hasNext()) {
                    continue label65;
                }
                bxx = (ItemUseOnBlock) var11.next();
            } while (!bxx.appliesToItem(i));
            ITEM_USE_ON_BLOCK_HP.put(i, bxx);
        }
        for (Block block : BuiltInRegistries.BLOCK) {
            for (BlockUse bxx : blockUse) {
                if (bxx.appliesToBlock(block)) {
                    BLOCK_USE.put(block, bxx);
                    break;
                }
            }
        }
    }

    public static InteractionResult onItemUsedOnBlockHP(Player player, Level level, ItemStack stack, InteractionHand hand, BlockHitResult hit) {
        Item item = stack.getItem();
        ItemUseOnBlock override = (ItemUseOnBlock) ITEM_USE_ON_BLOCK_HP.get(item);
        if (override == null || !override.isEnabled()) {
            return InteractionResult.PASS;
        } else if (CompatHandler.FLAN && override.altersWorld() && !FlanCompat.canPlace(player, hit.getBlockPos())) {
            return InteractionResult.PASS;
        } else {
            return override.altersWorld() && !Utils.mayPerformBlockAction(player, hit.getBlockPos(), stack) ? InteractionResult.PASS : override.tryPerformingAction(level, player, hand, stack, hit);
        }
    }

    public static InteractionResult onItemUsedOnBlock(Player player, Level level, ItemStack stack, InteractionHand hand, BlockHitResult hit) {
        Item item = stack.getItem();
        for (ItemUseOnBlock override : ITEM_USE_ON_BLOCK.get(item)) {
            if (override != null && override.isEnabled()) {
                if (CompatHandler.FLAN && override.altersWorld() && !FlanCompat.canPlace(player, hit.getBlockPos())) {
                    return InteractionResult.PASS;
                }
                if (override.altersWorld() && !Utils.mayPerformBlockAction(player, hit.getBlockPos(), stack)) {
                    return InteractionResult.PASS;
                }
                InteractionResult result = override.tryPerformingAction(level, player, hand, stack, hit);
                if (result != InteractionResult.PASS) {
                    return result;
                }
            }
        }
        BlockPos pos = hit.getBlockPos();
        BlockState state = level.getBlockState(pos);
        BlockUse o = (BlockUse) BLOCK_USE.get(state.m_60734_());
        if (o == null || !o.isEnabled()) {
            return InteractionResult.PASS;
        } else {
            return CompatHandler.FLAN && o.altersWorld() && !FlanCompat.canPlace(player, hit.getBlockPos()) ? InteractionResult.PASS : o.tryPerformingAction(state, pos, level, player, hand, stack, hit);
        }
    }

    public static InteractionResultHolder<ItemStack> onItemUse(Player player, Level level, InteractionHand hand, ItemStack stack) {
        Item item = stack.getItem();
        ItemUse override = (ItemUse) ITEM_USE.get(item);
        if (override != null && override.isEnabled()) {
            InteractionResult ret = override.tryPerformingAction(level, player, hand, stack, null);
            return switch(ret) {
                case CONSUME ->
                    InteractionResultHolder.consume(stack);
                case SUCCESS ->
                    InteractionResultHolder.success(stack);
                case FAIL ->
                    InteractionResultHolder.fail(stack);
                default ->
                    InteractionResultHolder.pass(stack);
            };
        } else {
            return InteractionResultHolder.pass(stack);
        }
    }

    public static InteractionResult replaceSimilarBlock(Block blockOverride, Player player, ItemStack stack, BlockPos pos, Level level, BlockState replaced, @Nullable SoundType sound, boolean keepWater, boolean playEvent, Property<?>... properties) {
        BlockState newState = blockOverride.defaultBlockState();
        for (Property<?> p : properties) {
            newState = Utils.replaceProperty(replaced, newState, p);
        }
        if (newState.m_61138_(BlockStateProperties.WATERLOGGED)) {
            FluidState fluidstate = level.getFluidState(pos);
            newState = (BlockState) newState.m_61124_(BlockStateProperties.WATERLOGGED, keepWater && fluidstate.is(FluidTags.WATER) && fluidstate.getAmount() == 8);
        }
        if (!level.setBlock(pos, newState, 3)) {
            return InteractionResult.FAIL;
        } else {
            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, pos, stack);
            }
            if (playEvent) {
                level.m_142346_(player, GameEvent.BLOCK_PLACE, pos);
            }
            if (sound == null) {
                sound = newState.m_60827_();
            }
            level.playSound(player, pos, sound.getPlaceSound(), SoundSource.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
            if (player == null || !player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }
}