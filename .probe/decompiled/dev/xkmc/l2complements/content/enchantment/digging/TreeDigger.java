package dev.xkmc.l2complements.content.enchantment.digging;

import dev.xkmc.l2complements.init.data.LCConfig;
import dev.xkmc.l2complements.init.data.LangData;
import dev.xkmc.l2complements.init.data.TagGen;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;

public record TreeDigger() implements BlockBreaker {

    @Override
    public BlockBreakerInstance getInstance(DiggerContext ctx) {
        int r = LCConfig.COMMON.treeChopMaxRadius.get();
        int h = LCConfig.COMMON.treeChopMaxHeight.get();
        int max = LCConfig.COMMON.treeChopMaxBlock.get();
        return new TreeInstance(-r, r, -r, h, -r, r, max, ctx.level(), this::match);
    }

    public int match(BlockState state) {
        if (state.m_204336_(BlockTags.LOGS)) {
            return 2;
        } else {
            return state.m_204336_(TagGen.AS_LEAF) ? 1 : 0;
        }
    }

    @Override
    public List<Component> descFull(int lv, String key, boolean alt, boolean book) {
        List<Component> ans = new ArrayList();
        ans.add(Component.translatable(key).withStyle(ChatFormatting.GRAY));
        if (lv > 0) {
            ans.add(LangData.IDS.TREE_CHOP.get().withStyle(ChatFormatting.GRAY));
        }
        ans.add(LangData.diggerRotate().withStyle(ChatFormatting.DARK_GRAY));
        return ans;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }
}