package journeymap.client.event.dispatchers;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import journeymap.client.api.display.ModPopupMenu;
import journeymap.client.api.event.forge.EntityRadarUpdateEvent;
import journeymap.client.api.event.forge.FullscreenDisplayEvent;
import journeymap.client.api.event.forge.PopupMenuEvent;
import journeymap.client.api.impl.ClientAPI;
import journeymap.client.api.impl.ThemeButtonDisplayFactory;
import journeymap.client.api.impl.ThemeToolbarDisplayFactory;
import journeymap.client.model.EntityDTO;
import journeymap.client.ui.component.Button;
import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.client.ui.theme.Theme;
import journeymap.client.ui.theme.ThemeButton;
import journeymap.client.ui.theme.ThemeToolbar;
import journeymap.client.waypoint.Waypoint;

public class CustomEventDispatcher {

    private final EventDispatcher dispatcher;

    private static CustomEventDispatcher INSTANCE;

    private CustomEventDispatcher(EventDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public static CustomEventDispatcher getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        } else {
            throw new UnsupportedOperationException("CustomEventDispatcher not initialized properly");
        }
    }

    public static void init(EventDispatcher dispatcher) {
        if (INSTANCE == null) {
            INSTANCE = new CustomEventDispatcher(dispatcher);
        }
    }

    public ThemeToolbar getMapTypeToolbar(Fullscreen fullscreen, Theme theme, Button... buttons) {
        ThemeButtonDisplayFactory factory = new ThemeButtonDisplayFactory(theme);
        FullscreenDisplayEvent.MapTypeButtonDisplayEvent event = new FullscreenDisplayEvent.MapTypeButtonDisplayEvent(fullscreen, factory);
        this.dispatcher.getMapTypeToolbar(event);
        if (!factory.getThemeButtonList().isEmpty()) {
            ArrayList<Button> themeButtonList = Lists.newArrayList(buttons);
            factory.getThemeButtonList().forEach(fullscreen::addButtonWidget);
            themeButtonList.addAll(0, factory.getThemeButtonList());
            return new ThemeToolbar(theme, (Button[]) themeButtonList.toArray(new Button[0]));
        } else {
            return new ThemeToolbar(theme, buttons);
        }
    }

    public ThemeToolbar getAddonToolbar(Fullscreen fullscreen, Theme theme) {
        ThemeButtonDisplayFactory factory = new ThemeButtonDisplayFactory(theme);
        FullscreenDisplayEvent.AddonButtonDisplayEvent event = new FullscreenDisplayEvent.AddonButtonDisplayEvent(fullscreen, factory);
        this.dispatcher.getAddonToolbar(event);
        if (!factory.getThemeButtonList().isEmpty()) {
            factory.getThemeButtonList().forEach(fullscreen::addButtonWidget);
            return new ThemeToolbar(theme, (ThemeButton[]) factory.getThemeButtonList().toArray(new ThemeButton[0]));
        } else {
            return null;
        }
    }

    public List<ThemeToolbar> getCustomToolBars(Fullscreen fullscreen, Theme theme) {
        ThemeToolbarDisplayFactory factory = new ThemeToolbarDisplayFactory(theme, fullscreen);
        FullscreenDisplayEvent.CustomToolbarEvent event = new FullscreenDisplayEvent.CustomToolbarEvent(fullscreen, factory);
        this.dispatcher.getCustomToolBars(event);
        if (!factory.getToolbarList().isEmpty()) {
            factory.getToolbarList().forEach(bar -> bar.addAllButtons(fullscreen));
            return factory.getToolbarList();
        } else {
            return null;
        }
    }

    public boolean popupWaypointMenuEvent(Fullscreen fullscreen, ModPopupMenu menu, Waypoint wp) {
        PopupMenuEvent.WaypointPopupMenuEvent event = new PopupMenuEvent.WaypointPopupMenuEvent(menu, fullscreen, wp.modWaypoint());
        this.dispatcher.popupWaypointMenuEvent(event);
        return !event.isCanceled();
    }

    public boolean popupMenuEvent(Fullscreen fullscreen, ModPopupMenu menu) {
        PopupMenuEvent.FullscreenPopupMenuEvent event = new PopupMenuEvent.FullscreenPopupMenuEvent(menu, fullscreen);
        this.dispatcher.popupMenuEvent(event);
        return !event.isCanceled();
    }

    public boolean entityRadarUpdateEvent(EntityRadarUpdateEvent.EntityType type, EntityDTO dto) {
        EntityRadarUpdateEvent event = new EntityRadarUpdateEvent(ClientAPI.INSTANCE.getLastUIState(), type, dto);
        this.dispatcher.entityRadarUpdateEvent(event);
        return !dto.isDisabled() && !event.isCanceled();
    }
}