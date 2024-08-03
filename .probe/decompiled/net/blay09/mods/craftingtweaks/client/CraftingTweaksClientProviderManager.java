package net.blay09.mods.craftingtweaks.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.blay09.mods.craftingtweaks.api.GridGuiHandler;
import net.blay09.mods.craftingtweaks.api.impl.DefaultGridGuiHandler;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class CraftingTweaksClientProviderManager {

    private static final Map<Class<?>, GridGuiHandler> gridGuiHandlers = new HashMap();

    private static final DefaultGridGuiHandler defaultGridGuiHandler = new DefaultGridGuiHandler();

    public static <TScreen extends AbstractContainerScreen<TMenu>, TMenu extends AbstractContainerMenu> void registerCraftingGridGuiHandler(Class<TScreen> clazz, GridGuiHandler handler) {
        gridGuiHandlers.put(clazz, handler);
    }

    public static GridGuiHandler getGridGuiHandler(AbstractContainerScreen<?> screen) {
        GridGuiHandler exactHandler = (GridGuiHandler) gridGuiHandlers.get(screen.getClass());
        if (exactHandler != null) {
            return exactHandler;
        } else {
            for (Entry<Class<?>, GridGuiHandler> entry : gridGuiHandlers.entrySet()) {
                if (((Class) entry.getKey()).isAssignableFrom(screen.getClass())) {
                    return (GridGuiHandler) entry.getValue();
                }
            }
            return defaultGridGuiHandler;
        }
    }
}