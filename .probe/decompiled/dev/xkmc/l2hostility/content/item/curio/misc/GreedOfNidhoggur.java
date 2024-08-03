package dev.xkmc.l2hostility.content.item.curio.misc;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
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

public class GreedOfNidhoggur extends CurseCurioItem {

    public GreedOfNidhoggur(Item.Properties props) {
        super(props);
    }

    @Override
    public int getExtraLevel() {
        return LHConfig.COMMON.nidhoggurExtraLevel.get();
    }

    @Override
    public double getLootFactor(ItemStack stack, PlayerDifficulty player, MobTraitCap mob) {
        return LHConfig.COMMON.greedDropFactor.get();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        int nid = (int) Math.round(100.0 * LHConfig.COMMON.nidhoggurDropFactor.get());
        list.add(LangData.ITEM_CHARM_GREED.get(LHConfig.COMMON.greedDropFactor.get()).withStyle(ChatFormatting.GOLD));
        list.add(LangData.NIDHOGGUR.get(nid).withStyle(ChatFormatting.GOLD));
    }
}