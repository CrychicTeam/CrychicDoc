package dev.xkmc.l2hostility.content.item.curio.ring;

import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import org.jetbrains.annotations.Nullable;

public class RingOfLife extends CurseCurioItem {

    public RingOfLife(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        int perc = (int) Math.round(LHConfig.COMMON.ringOfLifeMaxDamage.get() * 100.0);
        list.add(LangData.ITEM_RING_LIFE.get(perc).withStyle(ChatFormatting.GOLD));
    }

    @Override
    public void onDamage(ItemStack stack, LivingEntity user, LivingDamageEvent event) {
        boolean bypassInvul = event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY);
        boolean bypassMagic = event.getSource().is(DamageTypeTags.BYPASSES_EFFECTS);
        if (!bypassInvul && !bypassMagic) {
            float damage = event.getAmount();
            float maxHealth = event.getEntity().getMaxHealth();
            damage = Math.min(damage, (float) ((double) maxHealth * LHConfig.COMMON.ringOfLifeMaxDamage.get()));
            event.setAmount(damage);
        }
    }
}