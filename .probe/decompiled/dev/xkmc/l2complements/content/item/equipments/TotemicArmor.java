package dev.xkmc.l2complements.content.item.equipments;

import dev.xkmc.l2complements.init.data.LCConfig;
import dev.xkmc.l2complements.init.data.LangData;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraArmorConfig;
import dev.xkmc.l2library.init.events.GeneralEventHandler;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TotemicArmor extends ExtraArmorConfig {

    public void onArmorTick(ItemStack stack, Level world, Player player) {
        super.onArmorTick(stack, world, player);
        if (player.f_19797_ % LCConfig.COMMON.totemicHealDuration.get() == 0) {
            player.m_5634_((float) LCConfig.COMMON.totemicHealAmount.get().intValue());
        }
    }

    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity) {
        if (entity instanceof Player player) {
            GeneralEventHandler.schedule(() -> player.m_5634_((float) amount));
        }
        return super.damageItem(stack, amount, entity);
    }

    public void addTooltip(ItemStack stack, List<Component> list) {
        list.add(LangData.IDS.TOTEMIC_ARMOR.get().withStyle(ChatFormatting.GRAY));
        super.addTooltip(stack, list);
    }
}