package de.keksuccino.konkrete.gui.content.handling;

import de.keksuccino.konkrete.events.ScreenCharTypedEvent;
import de.keksuccino.konkrete.events.ScreenKeyPressedEvent;
import de.keksuccino.konkrete.events.ScreenKeyReleasedEvent;
import de.keksuccino.konkrete.events.ScreenMouseClickedEvent;
import de.keksuccino.konkrete.events.ScreenTickEvent;
import de.keksuccino.konkrete.input.CharData;
import de.keksuccino.konkrete.input.KeyboardData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AdvancedWidgetsHandler {

    protected static Map<IAdvancedWidgetBase, Long> widgets = new HashMap();

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new AdvancedWidgetsHandler());
    }

    protected AdvancedWidgetsHandler() {
    }

    public static void handleWidget(IAdvancedWidgetBase widget) {
        widgets.put(widget, System.currentTimeMillis());
    }

    @SubscribeEvent
    public void onScreenCharTyped(ScreenCharTypedEvent e) {
        for (Entry<IAdvancedWidgetBase, Long> m : widgets.entrySet()) {
            CharData d = new CharData(e.character, e.modifiers);
            ((IAdvancedWidgetBase) m.getKey()).onCharTyped(d);
        }
    }

    @SubscribeEvent
    public void onScreenKeyPressed(ScreenKeyPressedEvent e) {
        for (Entry<IAdvancedWidgetBase, Long> m : widgets.entrySet()) {
            KeyboardData d = new KeyboardData(e.keyCode, e.scanCode, e.modifiers);
            ((IAdvancedWidgetBase) m.getKey()).onKeyPress(d);
        }
    }

    @SubscribeEvent
    public void onScreenKeyReleased(ScreenKeyReleasedEvent e) {
        for (Entry<IAdvancedWidgetBase, Long> m : widgets.entrySet()) {
            KeyboardData d = new KeyboardData(e.keyCode, e.scanCode, e.modifiers);
            ((IAdvancedWidgetBase) m.getKey()).onKeyReleased(d);
        }
    }

    @SubscribeEvent
    public void onScreenMouseClicked(ScreenMouseClickedEvent e) {
        for (Entry<IAdvancedWidgetBase, Long> m : widgets.entrySet()) {
            ((IAdvancedWidgetBase) m.getKey()).onMouseClicked(e.mouseX, e.mouseY, e.mouseButton);
        }
    }

    @SubscribeEvent
    public void onScreenTick(ScreenTickEvent e) {
        for (Entry<IAdvancedWidgetBase, Long> m : widgets.entrySet()) {
            ((IAdvancedWidgetBase) m.getKey()).onTick();
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent e) {
        if (e.phase == TickEvent.Phase.START) {
            long now = System.currentTimeMillis();
            List<IAdvancedWidgetBase> garbageCollected = new ArrayList();
            for (Entry<IAdvancedWidgetBase, Long> m : widgets.entrySet()) {
                long lastRenderTick = (Long) m.getValue();
                IAdvancedWidgetBase widget = (IAdvancedWidgetBase) m.getKey();
                if (lastRenderTick + 2000L < now) {
                    garbageCollected.add(widget);
                }
            }
            for (IAdvancedWidgetBase widget : garbageCollected) {
                widgets.remove(widget);
            }
        }
    }

    @SubscribeEvent
    public void onInitScreen(ScreenEvent.Init.Pre e) {
        widgets.clear();
    }
}