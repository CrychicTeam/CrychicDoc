package dev.xkmc.l2weaponry.content.item.legendary;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2weaponry.content.item.types.ClawItem;
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

public class CheaterClaw extends ClawItem implements LegendaryWeapon {

    private static final String KEY_TARGET = "target";

    private static final String KEY_DAMAGE = "damage_bonus";

    public CheaterClaw(Tier tier, int damage, float speed, Item.Properties prop, ExtraToolConfig config) {
        super(tier, damage, speed, prop, config);
    }

    @Override
    public void onDamageFinal(AttackCache cache, LivingEntity le) {
        if (!(cache.getStrength() < 0.95F)) {
            if (cache.getAttackTarget().hurtTime <= 0) {
                float diff = cache.getPreDamage() - cache.getDamageDealt();
                cache.getWeapon().getOrCreateTag().putUUID("target", cache.getAttackTarget().m_20148_());
                double rate = LWConfig.COMMON.determinationRate.get();
                cache.getWeapon().getOrCreateTag().putFloat("damage_bonus", diff * (float) rate);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> list, TooltipFlag pIsAdvanced) {
        list.add(LangData.CHEATER_CLAW.get());
    }

    @Override
    public void onHurt(AttackCache event, LivingEntity le, ItemStack stack) {
        if (stack.getOrCreateTag().hasUUID("target") && event.getAttackTarget().m_20148_().equals(stack.getOrCreateTag().getUUID("target"))) {
            event.addHurtModifier(DamageModifier.addExtra(stack.getOrCreateTag().getFloat("damage_bonus")));
        }
    }
}