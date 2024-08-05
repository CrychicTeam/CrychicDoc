package dev.xkmc.l2hostility.content.item.curio.misc;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.init.data.LangData;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class DivinityLight extends CurseCurioItem implements ICurioItem {

    public DivinityLight(Item.Properties props) {
        super(props);
    }

    @Override
    public double getGrowFactor(ItemStack stack, PlayerDifficulty player, MobTraitCap mob) {
        return 0.0;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof Player player && PlayerDifficulty.HOLDER.isProper(player)) {
            PlayerDifficulty cap = PlayerDifficulty.HOLDER.get(player);
            cap.getLevelEditor().setBase(0);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(LangData.DIVINITY_LIGHT.get().withStyle(ChatFormatting.GOLD));
    }
}