package net.liopyu.animationjs.events.subevents.server;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.liopyu.animationjs.events.EventHandlers;
import net.liopyu.animationjs.events.IAnimationTrigger;
import net.liopyu.animationjs.events.UniversalController;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "animationjs", bus = Bus.FORGE)
public class ServerEventHandlers {

    public static final Map<UUID, UniversalController> thisList = new HashMap();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        animatorJS$updateMovementBoolean(event.player);
        if (event.player instanceof ServerPlayer serverPlayer && EventHandlers.universalController.hasListeners()) {
            UniversalController cont = (UniversalController) thisList.get(serverPlayer.m_20148_());
            if (cont == null) {
                cont = new UniversalController(serverPlayer);
                thisList.put(serverPlayer.m_20148_(), cont);
            }
            EventHandlers.universalController.post(cont);
        }
    }

    public static void animatorJS$updateMovementBoolean(Player player) {
        if (player instanceof IAnimationTrigger accessor) {
            double currentX = player.m_20185_();
            double currentY = player.m_20186_();
            double currentZ = player.m_20189_();
            double deltaX = Math.abs(accessor.animatorJS$getPrevX() - currentX);
            double deltaY = Math.abs(accessor.animatorJS$getPrevY() - currentY);
            double deltaZ = Math.abs(accessor.animatorJS$getPrevZ() - currentZ);
            boolean movingX = deltaX > 0.001;
            boolean movingY = deltaY > 0.001;
            boolean movingZ = deltaZ > 0.001;
            accessor.animatorJS$setPrevX(currentX);
            accessor.animatorJS$setPrevY(currentY);
            accessor.animatorJS$setPrevZ(currentZ);
            if (movingX || movingY || movingZ) {
                accessor.animatorJS$setCooldown(1);
                accessor.animatorJS$setIsMoving(true);
            } else if (accessor.animatorJS$getCooldown() > 0) {
                accessor.animatorJS$setCooldown(accessor.animatorJS$getCooldown() - 1);
                accessor.animatorJS$setIsMoving(true);
            } else {
                accessor.animatorJS$setIsMoving(false);
            }
        }
    }
}