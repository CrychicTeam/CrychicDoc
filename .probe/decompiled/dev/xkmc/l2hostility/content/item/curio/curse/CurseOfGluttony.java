package dev.xkmc.l2hostility.content.item.curio.curse;

import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class CurseOfGluttony extends CurseCurioItem {

    public CurseOfGluttony(Item.Properties props) {
        super(props);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        int rate = (int) Math.round(100.0 * LHConfig.COMMON.gluttonyBottleDropRate.get());
        list.add(LangData.ITEM_CHARM_GLUTTONY.get(rate).withStyle(ChatFormatting.GOLD));
    }
}