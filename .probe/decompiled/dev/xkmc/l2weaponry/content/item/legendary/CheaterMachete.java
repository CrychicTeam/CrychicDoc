package dev.xkmc.l2weaponry.content.item.legendary;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2weaponry.content.item.types.MacheteItem;
import dev.xkmc.l2weaponry.init.data.LWConfig;
import dev.xkmc.l2weaponry.init.data.LangData;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class CheaterMachete extends MacheteItem implements LegendaryWeapon {

    private static final String KEY_TARGET = "target";

    private static final String KEY_DAMAGE = "accumulated_damage";

    public CheaterMachete(Tier tier, int damage, float speed, Item.Properties prop, ExtraToolConfig config) {
        super(tier, damage, speed, prop, config);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> list, TooltipFlag pIsAdvanced) {
        list.add(LangData.CHEATER_MACHETE.get());
    }

    @Override
    public void onHurt(AttackCache cache, LivingEntity le, ItemStack stack) {
        CompoundTag ctag = stack.getOrCreateTag();
        if (ctag.hasUUID("target") && cache.getAttackTarget().m_20148_().equals(ctag.getUUID("target"))) {
            float lost = cache.getAttackTarget().getMaxHealth() - cache.getAttackTarget().getHealth();
            float acc = ctag.getFloat("accumulated_damage");
            if (lost < acc) {
                double factor = LWConfig.COMMON.illusionRate.get();
                cache.addHurtModifier(DamageModifier.addExtra((float) factor * (acc - lost)));
            }
        }
    }

    @Override
    public void onHurtMaximized(AttackCache cache, LivingEntity le) {
        if (!(cache.getStrength() < 0.95F)) {
            if (cache.getAttackTarget().hurtTime <= 0) {
                CompoundTag ctag = cache.getWeapon().getOrCreateTag();
                if (ctag.hasUUID("target") && cache.getAttackTarget().m_20148_().equals(ctag.getUUID("target"))) {
                    float damage = ctag.getFloat("accumulated_damage");
                    ctag.putFloat("accumulated_damage", damage + cache.getPreDamage());
                } else {
                    ctag.putFloat("accumulated_damage", cache.getPreDamage());
                }
                ctag.putUUID("target", cache.getAttackTarget().m_20148_());
            }
        }
    }
}