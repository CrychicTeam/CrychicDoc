package dev.ftb.mods.ftbquests.item;

import dev.ftb.mods.ftbquests.block.FTBQuestsBlocks;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public class StageBarrierBlockItem extends BlockItem {

    public StageBarrierBlockItem() {
        super((Block) FTBQuestsBlocks.STAGE_BARRIER.get(), FTBQuestsItems.defaultProps());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.ftbquests.barrier.nogui").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        tooltip.add(Component.translatable("item.ftbquests.stage_barrier.config").withStyle(ChatFormatting.GRAY));
    }
}