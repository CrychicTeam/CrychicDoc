package dev.xkmc.l2artifacts.events;

import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.AttackListener;
import dev.xkmc.l2damagetracker.contents.attack.PlayerAttackCache;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.CriticalHitEvent;

public class ArtifactAttackListener implements AttackListener {

    public boolean onCriticalHit(PlayerAttackCache cache, CriticalHitEvent event) {
        return ArtifactEffectEvents.postEvent(event.getEntity(), event, SetEffect::playerAttackModifyEvent);
    }

    public void onAttack(AttackCache cache, ItemStack weapon) {
        if (cache.getAttackTarget() instanceof Player player) {
            ArtifactEffectEvents.postEvent(player, cache, SetEffect::playerAttackedEvent);
        }
    }

    public void onHurt(AttackCache cache, ItemStack weapon) {
        if (cache.getAttacker() instanceof Player player) {
            ArtifactEffectEvents.postEvent(player, cache, SetEffect::playerHurtOpponentEvent);
        }
    }

    public void onDamage(AttackCache cache, ItemStack weapon) {
        if (cache.getAttackTarget() instanceof Player player) {
            ArtifactEffectEvents.postEvent(player, cache, SetEffect::playerHurtEvent);
        }
    }

    public void onDamageFinalized(AttackCache cache, ItemStack weapon) {
        if (cache.getAttacker() instanceof Player player) {
            ArtifactEffectEvents.postEvent(player, cache, SetEffect::playerDamageOpponentEvent);
        }
    }
}