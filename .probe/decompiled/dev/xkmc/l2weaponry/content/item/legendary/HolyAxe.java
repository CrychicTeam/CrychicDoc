package dev.xkmc.l2weaponry.content.item.legendary;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2weaponry.content.item.types.BattleAxeItem;
import dev.xkmc.l2weaponry.init.data.LWConfig;
import dev.xkmc.l2weaponry.init.data.LangData;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class HolyAxe extends BattleAxeItem implements LegendaryWeapon {

    public HolyAxe(Tier tier, int damage, float speed, Item.Properties prop, ExtraToolConfig config) {
        super(tier, damage, speed, prop, config);
    }

    @Override
    public void onHurt(AttackCache event, LivingEntity le, ItemStack stack) {
        if (event.getCriticalHitEvent() == null || !((double) event.getStrength() < 0.9)) {
            float ans = le.getAbsorptionAmount();
            float health = event.getAttackTarget().getHealth();
            if (health > 0.0F) {
                double max = LWConfig.COMMON.dogmaticStandoffMax.get();
                double inc = LWConfig.COMMON.dogmaticStandoffGain.get();
                ans = (float) Math.max((double) ans, Math.min((double) health * max, (double) health * inc + (double) ans));
                le.setAbsorptionAmount(ans);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> list, TooltipFlag pIsAdvanced) {
        int max = (int) Math.round(LWConfig.COMMON.dogmaticStandoffMax.get() * 100.0);
        int inc = (int) Math.round(LWConfig.COMMON.dogmaticStandoffGain.get() * 100.0);
        list.add(LangData.HOLY_AXE.get(inc, max));
    }
}