package dev.xkmc.l2complements.events;

import dev.xkmc.l2complements.content.enchantment.core.SourceModifierEnchantment;
import dev.xkmc.l2complements.content.enchantment.weapon.VoidTouchEnchantment;
import dev.xkmc.l2complements.init.data.LCConfig;
import dev.xkmc.l2complements.init.registrate.LCEnchantments;
import dev.xkmc.l2complements.init.registrate.LCItems;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.AttackListener;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import java.util.HashSet;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.ModList;

public class MaterialDamageListener implements AttackListener {

    public static final HashSet<String> BAN_SPACE_SHARD = new HashSet();

    public static boolean isSpaceShardBanned() {
        if (LCConfig.COMMON.allowModBanSpaceShard.get()) {
            for (String e : BAN_SPACE_SHARD) {
                if (ModList.get().isLoaded(e)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void onCreateSource(CreateSourceEvent event) {
        if (event.getOriginal().equals(DamageTypes.MOB_ATTACK) || event.getOriginal().equals(DamageTypes.PLAYER_ATTACK)) {
            ItemStack stack = event.getAttacker().getMainHandItem();
            SourceModifierEnchantment.modifySource(stack, event);
        }
    }

    public void onDamageFinalized(AttackCache cache, ItemStack weapon) {
        LivingDamageEvent event = cache.getLivingDamageEvent();
        if (event != null) {
            if (cache.getAttackTarget() instanceof Player player) {
                float damage = cache.getPreDamage();
                if (event.getSource().is(DamageTypeTags.IS_EXPLOSION) && damage >= (float) LCConfig.COMMON.explosionDamage.get().intValue() && cache.getDamageDealt() < player.m_21223_() + player.getAbsorptionAmount()) {
                    player.getInventory().placeItemBackInInventory(LCItems.EXPLOSION_SHARD.asStack());
                }
            }
            if (cache.getAttackTarget() instanceof Chicken chicken && event.getSource().getMsgId().equals("sonic_boom") && cache.getDamageDealt() < chicken.m_21223_() + chicken.m_6103_()) {
                chicken.m_19983_(LCItems.RESONANT_FEATHER.asStack());
            }
            if (event.getSource().is(DamageTypeTags.IS_PROJECTILE) && event.getSource().getEntity() instanceof Player && !isSpaceShardBanned() && cache.getPreDamage() >= (float) LCConfig.COMMON.spaceDamage.get().intValue()) {
                cache.getAttackTarget().m_19983_(LCItems.SPACE_SHARD.asStack());
            }
        }
    }

    public void postAttack(AttackCache cache, LivingAttackEvent event, ItemStack weapon) {
        if (!weapon.isEmpty()) {
            ((VoidTouchEnchantment) LCEnchantments.VOID_TOUCH.get()).postAttack(cache, event, weapon);
        }
    }

    public void onHurt(AttackCache cache, ItemStack weapon) {
        if (!weapon.isEmpty()) {
            ((VoidTouchEnchantment) LCEnchantments.VOID_TOUCH.get()).initAttack(cache, weapon);
        }
    }

    public void postHurt(AttackCache cache, LivingHurtEvent event, ItemStack weapon) {
        if (!weapon.isEmpty()) {
            ((VoidTouchEnchantment) LCEnchantments.VOID_TOUCH.get()).postHurt(cache, event, weapon);
        }
    }

    public void onDamage(AttackCache cache, ItemStack weapon) {
        if (!weapon.isEmpty()) {
            ((VoidTouchEnchantment) LCEnchantments.VOID_TOUCH.get()).initDamage(cache, weapon);
        }
    }

    static {
        BAN_SPACE_SHARD.add("l2artifacts");
        BAN_SPACE_SHARD.add("l2hostility");
        BAN_SPACE_SHARD.add("apotheosis");
    }
}