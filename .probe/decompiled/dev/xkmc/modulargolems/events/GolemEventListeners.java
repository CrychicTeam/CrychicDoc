package dev.xkmc.modulargolems.events;

import dev.xkmc.modulargolems.events.event.GolemEquipEvent;
import dev.xkmc.modulargolems.events.event.GolemThrowableEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.TridentItem;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "modulargolems", bus = Bus.FORGE)
public class GolemEventListeners {

    @SubscribeEvent
    public static void onEquip(GolemEquipEvent event) {
        if (event.getStack().getItem() instanceof ArrowItem) {
            event.setSlot(EquipmentSlot.OFFHAND, event.getStack().getCount());
        }
        if (!event.getEntity().m_6844_(EquipmentSlot.MAINHAND).isEmpty() && event.getStack().getItem() instanceof BowItem) {
            event.setSlot(EquipmentSlot.OFFHAND, 1);
        }
        if (event.getStack().getItem() instanceof BannerItem) {
            event.setSlot(EquipmentSlot.HEAD, 1);
        }
    }

    @SubscribeEvent
    public static void isThrowable(GolemThrowableEvent event) {
        if (event.getStack().getItem() instanceof TridentItem) {
            event.setThrowable(level -> {
                ThrownTrident ans = new ThrownTrident(level, event.getEntity(), event.getStack());
                ans.f_36705_ = AbstractArrow.Pickup.DISALLOWED;
                return ans;
            });
        }
    }
}