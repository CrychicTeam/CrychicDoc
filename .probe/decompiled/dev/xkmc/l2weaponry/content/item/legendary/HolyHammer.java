package dev.xkmc.l2weaponry.content.item.legendary;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2weaponry.content.item.types.HammerItem;
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

public class HolyHammer extends HammerItem implements LegendaryWeapon {

    public HolyHammer(Tier tier, int damage, float speed, Item.Properties prop, ExtraToolConfig config) {
        super(tier, damage, speed, prop, config);
    }

    @Override
    public void onHurt(AttackCache event, LivingEntity le, ItemStack stack) {
        event.addHurtModifier(DamageModifier.addExtra(le.getAbsorptionAmount()));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> list, TooltipFlag pIsAdvanced) {
        list.add(LangData.HOLY_HAMMER.get());
    }
}