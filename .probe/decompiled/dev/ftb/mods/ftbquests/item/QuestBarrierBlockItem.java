package dev.ftb.mods.ftbquests.item;

import dev.ftb.mods.ftbquests.block.FTBQuestsBlocks;
import dev.ftb.mods.ftbquests.client.ClientQuestFile;
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

public class QuestBarrierBlockItem extends BlockItem {

    public QuestBarrierBlockItem() {
        super((Block) FTBQuestsBlocks.BARRIER.get(), FTBQuestsItems.defaultProps());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if (level != null) {
            tooltip.add(Component.translatable("item.ftbquests.barrier.nogui").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
            if (ClientQuestFile.exists() && !ClientQuestFile.INSTANCE.canEdit()) {
                tooltip.add(Component.translatable("item.ftbquests.barrier.disabled").withStyle(ChatFormatting.RED));
            } else {
                tooltip.add(Component.translatable("item.ftbquests.barrier.config").withStyle(ChatFormatting.GRAY));
            }
        }
    }
}