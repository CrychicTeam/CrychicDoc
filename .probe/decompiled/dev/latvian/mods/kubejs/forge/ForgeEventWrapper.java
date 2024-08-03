package dev.latvian.mods.kubejs.forge;

import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;

public record ForgeEventWrapper(String name, IEventBus eventBus) {

    public Object onEvent(Object eventClass, ForgeEventConsumer consumer) {
        if (!(eventClass instanceof CharSequence) && !(eventClass instanceof Class)) {
            throw new RuntimeException("Invalid syntax! " + this.name + ".onEvent(eventType, function) requires event class and handler");
        } else if (!KubeJS.getStartupScriptManager().firstLoad) {
            ConsoleJS.STARTUP.warn(this.name + ".onEvent() can't be reloaded! You will have to restart the game for changes to take effect.");
            return null;
        } else {
            try {
                Class type = eventClass instanceof Class<?> c ? c : Class.forName(eventClass.toString());
                this.eventBus.addListener(EventPriority.NORMAL, false, type, consumer);
                return null;
            } catch (Exception var5) {
                throw new RuntimeException(var5);
            }
        }
    }

    public Object onGenericEvent(Object eventClass, Object genericClass, GenericForgeEventConsumer consumer) {
        if ((eventClass instanceof CharSequence || eventClass instanceof Class) && (genericClass instanceof CharSequence || genericClass instanceof Class)) {
            if (!KubeJS.getStartupScriptManager().firstLoad) {
                ConsoleJS.STARTUP.warn(this.name + ".onGenericEvent() can't be reloaded! You will have to restart the game for changes to take effect.");
                return null;
            } else {
                try {
                    Class type = eventClass instanceof Class<?> c ? c : Class.forName(eventClass.toString());
                    Class genericType = genericClass instanceof Class<?> cx ? cx : Class.forName(genericClass.toString());
                    this.eventBus.addGenericListener(genericType, EventPriority.NORMAL, false, type, consumer);
                    return null;
                } catch (Exception var7) {
                    throw new RuntimeException(var7);
                }
            }
        } else {
            throw new RuntimeException("Invalid syntax! " + this.name + ".onGenericEvent(eventType, genericType, function) requires event class, generic class and handler");
        }
    }
}