package noppes.npcs;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import noppes.npcs.api.event.ForgeEvent;
import noppes.npcs.controllers.ScriptController;
import noppes.npcs.shared.common.util.LogWriter;
import org.apache.commons.lang3.StringUtils;

public class ForgeEventHandler {

    public static List<String> eventNames = new ArrayList();

    @SubscribeEvent
    public void forgeEntity(Event event) {
        if (CustomNpcs.Server != null && ScriptController.Instance.forgeScripts.isEnabled()) {
            try {
                if (event instanceof PlayerEvent ev && !(ev.getEntity().m_9236_() instanceof ServerLevel)) {
                    return;
                }
                if (event instanceof EntityEvent ev) {
                    if (ev.getEntity() != null && ev.getEntity().level() instanceof ServerLevel) {
                        if (event instanceof PlayerXpEvent) {
                            LogWriter.info(event);
                        }
                        EventHooks.onForgeEntityEvent(ev);
                        return;
                    }
                    return;
                }
                if (event instanceof LevelEvent ev) {
                    if (!(ev.getLevel() instanceof ServerLevel)) {
                        return;
                    }
                    EventHooks.onForgeLevelEvent(ev);
                    return;
                }
                if (event instanceof TickEvent && ((TickEvent) event).side == LogicalSide.CLIENT) {
                    return;
                }
                EventHooks.onForgeEvent(new ForgeEvent(event), event);
            } catch (Throwable var3) {
                LogWriter.error("Error in " + event.getClass().getName(), var3);
            }
        }
    }

    public static String getEventName(Class c) {
        String eventName = c.getName();
        int i = eventName.lastIndexOf(".");
        return StringUtils.uncapitalize(eventName.substring(i + 1).replace("$", ""));
    }
}