package dev.xkmc.l2archery.compat;

import dev.xkmc.l2archery.content.entity.GenericArrowEntity;
import dev.xkmc.l2archery.content.item.GenericBowItem;
import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemEntity;
import dev.xkmc.modulargolems.events.event.GolemBowAttackEvent;
import java.util.Optional;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GolemCompat {

    public static void register() {
        MinecraftForge.EVENT_BUS.register(GolemCompat.class);
    }

    @SubscribeEvent
    public static void onEquip(GolemBowAttackEvent event) {
        HumanoidGolemEntity golem = event.getEntity();
        if (event.getStack().getItem() instanceof GenericBowItem item) {
            Optional<AbstractArrow> opt = item.releaseUsingAndShootArrow(event.getStack(), golem.m_9236_(), golem, golem.m_21212_());
            if (opt.isPresent()) {
                AbstractArrow arrow = (AbstractArrow) opt.get();
                event.setArrow(arrow, arrow.pickup != AbstractArrow.Pickup.ALLOWED, true);
                if (arrow instanceof GenericArrowEntity entity) {
                    event.setParams((double) entity.data.bow().getConfig().speed(), (double) entity.features.flight().gravity);
                }
            }
        }
    }
}