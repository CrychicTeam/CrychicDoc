package dev.xkmc.l2weaponry.content.item.types;

import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2library.util.raytrace.FastItem;
import dev.xkmc.l2weaponry.content.item.base.GenericShieldItem;
import dev.xkmc.l2weaponry.init.data.LangData;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class RoundShieldItem extends GenericShieldItem implements FastItem {

    public RoundShieldItem(Tier tier, int maxDefense, float recover, Item.Properties prop, ExtraToolConfig config) {
        super(tier, prop, config, maxDefense, (double) recover, true);
    }

    @Override
    public boolean isFast(ItemStack itemStack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> list, TooltipFlag pIsAdvanced) {
        list.add(LangData.TOOL_ROUND_SHIELD.get());
        super.appendHoverText(pStack, pLevel, list, pIsAdvanced);
    }
}