package dev.xkmc.modulargolems.events;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.AttackListener;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import java.util.Map.Entry;
import net.minecraft.world.item.ItemStack;

public class GolemAttackListener implements AttackListener {

    public void onCreateSource(CreateSourceEvent event) {
        if (event.getAttacker() instanceof AbstractGolemEntity<?, ?> golem) {
            for (Entry<GolemModifier, Integer> e : golem.getModifiers().entrySet()) {
                ((GolemModifier) e.getKey()).modifySource(golem, event, (Integer) e.getValue());
            }
        }
    }

    public void onHurt(AttackCache cache, ItemStack weapon) {
        if (cache.getAttacker() instanceof AbstractGolemEntity<?, ?> golem) {
            for (Entry<GolemModifier, Integer> entry : golem.getModifiers().entrySet()) {
                ((GolemModifier) entry.getKey()).modifyDamage(cache, golem, (Integer) entry.getValue());
            }
        }
    }
}