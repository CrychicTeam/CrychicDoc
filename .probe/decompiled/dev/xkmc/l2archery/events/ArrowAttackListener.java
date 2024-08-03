package dev.xkmc.l2archery.events;

import dev.xkmc.l2archery.content.entity.GenericArrowEntity;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.AttackListener;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import net.minecraft.world.item.ItemStack;

public class ArrowAttackListener implements AttackListener {

    public void onCreateSource(CreateSourceEvent event) {
        if (event.getDirect() instanceof GenericArrowEntity gen) {
            gen.onHurtEntity(event);
        }
    }

    public void onHurt(AttackCache cache, ItemStack weapon) {
        if (cache.getLivingHurtEvent().getSource().getDirectEntity() instanceof GenericArrowEntity gen) {
            gen.onHurtModification(cache);
        }
    }
}