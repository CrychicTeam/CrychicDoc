package dev.xkmc.l2hostility.content.item.spawner;

import dev.xkmc.l2hostility.content.capability.chunk.SectionDifficulty;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2modularblock.mult.PlacementBlockMethod;
import dev.xkmc.l2modularblock.mult.ToolTipBlockMethod;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BurstTraitMethod implements PlacementBlockMethod, ToolTipBlockMethod {

    public BlockState getStateForPlacement(BlockState state, BlockPlaceContext ctx) {
        return (BlockState) state.m_61124_(TraitSpawnerBlock.STATE, (TraitSpawnerBlock.State) SectionDifficulty.sectionAt(ctx.m_43725_(), ctx.getClickedPos()).filter(SectionDifficulty::isCleared).map(e -> TraitSpawnerBlock.State.CLEAR).orElse(TraitSpawnerBlock.State.IDLE));
    }

    public void appendHoverText(ItemStack itemStack, @Nullable BlockGetter blockGetter, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(LangData.ITEM_SPAWNER.get().withStyle(ChatFormatting.GRAY));
    }
}