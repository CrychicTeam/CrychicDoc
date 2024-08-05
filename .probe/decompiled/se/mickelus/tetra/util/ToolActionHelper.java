package se.mickelus.tetra.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.ForgeEventFactory;
import se.mickelus.tetra.TetraToolActions;

public class ToolActionHelper {

    public static final BiMap<ToolAction, TagKey<Block>> appropriateTools = HashBiMap.create(5);

    public static final TagKey<Block> hoeExtraTag = BlockTags.create(new ResourceLocation("tetra", "hoe_extra_mineable"));

    public static final Set<TagKey<Block>> cuttingDestroyTags = Sets.newHashSet(new TagKey[] { BlockTags.SWORD_EFFICIENT });

    public static final Set<Block> cuttingHarvestBlocks = Sets.newHashSet(new Block[] { Blocks.COBWEB });

    public static final TagKey<Block> hammerMineable = BlockTags.create(new ResourceLocation("mineable/hammer"));

    public static void init() {
        appropriateTools.put(ToolActions.AXE_DIG, BlockTags.MINEABLE_WITH_AXE);
        appropriateTools.put(ToolActions.PICKAXE_DIG, BlockTags.MINEABLE_WITH_PICKAXE);
        appropriateTools.put(ToolActions.SHOVEL_DIG, BlockTags.MINEABLE_WITH_SHOVEL);
        appropriateTools.put(ToolActions.HOE_DIG, BlockTags.MINEABLE_WITH_HOE);
        appropriateTools.put(TetraToolActions.hammer, hammerMineable);
    }

    public static Set<ToolAction> getAppropriateTools(BlockState state) {
        return (Set<ToolAction>) getActionsFor(state).collect(Collectors.toSet());
    }

    @Nullable
    public static ToolAction getAppropriateTool(BlockState state) {
        return (ToolAction) getActionsFor(state).findFirst().orElse(null);
    }

    public static boolean isEffectiveOn(ItemStack stack, BlockState state) {
        return getActionsFor(state).anyMatch(stack::canPerformAction);
    }

    private static Stream<ToolAction> getActionsFor(BlockState state) {
        return ToolAction.getActions().stream().filter(action -> isEffectiveOn(action, state));
    }

    public static boolean isEffectiveOn(ToolAction action, BlockState state) {
        if (appropriateTools.containsKey(action) && state.m_204336_((TagKey) appropriateTools.get(action))) {
            return true;
        } else {
            return !TetraToolActions.cut.equals(action) || !cuttingHarvestBlocks.contains(state.m_60734_()) && !cuttingDestroyTags.stream().anyMatch(state::m_204336_) ? ToolActions.HOE_DIG.equals(action) && state.m_204336_(hoeExtraTag) : true;
        }
    }

    public static boolean playerCanDestroyBlock(Player player, BlockState state, BlockPos pos, ItemStack toolStack) {
        return playerCanDestroyBlock(player, state, pos, toolStack, null);
    }

    public static boolean playerCanDestroyBlock(Player player, BlockState state, BlockPos pos, ItemStack toolStack, @Nullable ToolAction useAction) {
        if (state.m_60800_(player.m_9236_(), pos) < 0.0F) {
            return false;
        } else if (useAction == null ? isEffectiveOn(toolStack, state) : isEffectiveOn(useAction, state)) {
            return !toolStack.isCorrectToolForDrops(state) ? false : ForgeEventFactory.doPlayerHarvestCheck(player, state, true);
        } else {
            return false;
        }
    }
}