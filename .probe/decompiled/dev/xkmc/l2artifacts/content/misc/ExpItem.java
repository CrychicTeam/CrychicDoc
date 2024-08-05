package dev.xkmc.l2artifacts.content.misc;

import dev.xkmc.l2artifacts.content.core.RankedItem;
import dev.xkmc.l2artifacts.content.upgrades.ArtifactUpgradeManager;
import dev.xkmc.l2artifacts.init.data.LangData;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ExpItem extends RankedItem {

    public ExpItem(Item.Properties properties, int rank) {
        super(properties, rank);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(LangData.EXP_CONVERSION.get(ArtifactUpgradeManager.getExpForConversion(this.rank, null)));
        super.m_7373_(stack, level, list, flag);
    }
}