package journeymap.client.event.handlers;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import journeymap.client.Constants;
import journeymap.client.event.handlers.keymapping.KeyBindingAction;
import journeymap.client.event.handlers.keymapping.KeyConflictContext;
import journeymap.client.event.handlers.keymapping.KeyEvent;
import journeymap.client.event.handlers.keymapping.KeyModifier;
import journeymap.client.event.handlers.keymapping.UpdateAwareKeyBinding;
import journeymap.client.log.ChatLog;
import journeymap.client.ui.UIManager;
import journeymap.client.ui.component.JmUI;
import journeymap.client.ui.dialog.OptionsManager;
import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.client.ui.minimap.MiniMap;
import journeymap.client.ui.waypoint.WaypointManager;
import journeymap.client.waypoint.Waypoint;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import org.apache.logging.log4j.Logger;

public class KeyEventHandler {

    public UpdateAwareKeyBinding kbMapZoomin;

    public UpdateAwareKeyBinding kbMapZoomout;

    public UpdateAwareKeyBinding kbMapToggleType;

    public UpdateAwareKeyBinding kbCreateWaypoint;

    public UpdateAwareKeyBinding kbToggleAllWaypoints;

    public UpdateAwareKeyBinding kbFullscreenCreateWaypoint;

    public UpdateAwareKeyBinding kbFullscreenChatPosition;

    public UpdateAwareKeyBinding kbFullscreenToggle;

    public UpdateAwareKeyBinding kbWaypointManager;

    public UpdateAwareKeyBinding kbMinimapToggle;

    public UpdateAwareKeyBinding kbMinimapPreset;

    public UpdateAwareKeyBinding kbFullmapOptionsManager;

    public UpdateAwareKeyBinding kbFullmapPanNorth;

    public UpdateAwareKeyBinding kbFullmapPanSouth;

    public UpdateAwareKeyBinding kbFullmapPanEast;

    public UpdateAwareKeyBinding kbFullmapPanWest;

    public UpdateAwareKeyBinding kbFullmapButtonHide;

    protected Comparator<KeyBindingAction> kbaComparator = Comparator.comparingInt(KeyBindingAction::order);

    protected final ListMultimap<Integer, KeyBindingAction> minimapPreviewActions = MultimapBuilder.hashKeys().arrayListValues(2).build();

    protected final ListMultimap<Integer, KeyBindingAction> inGameActions = MultimapBuilder.hashKeys().arrayListValues(2).build();

    protected final ListMultimap<Integer, KeyBindingAction> inGuiActions = MultimapBuilder.hashKeys().arrayListValues(2).build();

    protected Minecraft mc = Minecraft.getInstance();

    public boolean sortActionsNeeded = true;

    protected Logger logger = Journeymap.getLogger();

    protected final KeyEvent keyEvent;

    public KeyEventHandler(KeyEvent keyEvent) {
        this.keyEvent = keyEvent;
        this.kbMapZoomin = this.register("key.journeymap.zoom_in", KeyConflictContext.UNIVERSAL, KeyModifier.NONE, 61);
        this.kbMapZoomout = this.register("key.journeymap.zoom_out", KeyConflictContext.UNIVERSAL, KeyModifier.NONE, 45);
        this.kbMapToggleType = this.register("key.journeymap.minimap_type", KeyConflictContext.UNIVERSAL, KeyModifier.NONE, 91);
        this.kbMinimapPreset = this.register("key.journeymap.minimap_preset", KeyConflictContext.IN_GAME, KeyModifier.NONE, 92);
        this.kbCreateWaypoint = this.register("key.journeymap.create_waypoint", KeyConflictContext.IN_GAME, KeyModifier.NONE, 66);
        this.kbToggleAllWaypoints = this.register("key.journeymap.toggle_waypoints", KeyConflictContext.IN_GAME, KeyModifier.NONE, 90);
        this.kbFullscreenCreateWaypoint = this.register("key.journeymap.fullscreen_create_waypoint", KeyConflictContext.GUI, KeyModifier.NONE, 66);
        this.kbFullscreenChatPosition = this.register("key.journeymap.fullscreen_chat_position", KeyConflictContext.GUI, KeyModifier.NONE, 67);
        this.kbFullscreenToggle = this.register("key.journeymap.map_toggle_alt", KeyConflictContext.UNIVERSAL, KeyModifier.NONE, 74);
        this.kbWaypointManager = this.register("key.journeymap.fullscreen_waypoints", KeyConflictContext.UNIVERSAL, KeyModifier.NONE, 78);
        this.kbMinimapToggle = this.register("key.journeymap.minimap_toggle_alt", KeyConflictContext.IN_GAME, KeyModifier.CONTROL, 74);
        this.kbFullmapOptionsManager = this.register("key.journeymap.fullscreen_options", KeyConflictContext.GUI, KeyModifier.NONE, 79);
        this.kbFullmapPanNorth = this.register("key.journeymap.fullscreen.north", KeyConflictContext.GUI, KeyModifier.NONE, 265);
        this.kbFullmapPanSouth = this.register("key.journeymap.fullscreen.south", KeyConflictContext.GUI, KeyModifier.NONE, 264);
        this.kbFullmapPanEast = this.register("key.journeymap.fullscreen.east", KeyConflictContext.GUI, KeyModifier.NONE, 262);
        this.kbFullmapPanWest = this.register("key.journeymap.fullscreen.west", KeyConflictContext.GUI, KeyModifier.NONE, 263);
        this.kbFullmapButtonHide = this.register("key.journeymap.fullscreen.disable_buttons", KeyConflictContext.GUI, KeyModifier.NONE, 72);
    }

    public KeyEventHandler registerActions() {
        this.setAction(this.minimapPreviewActions, this.kbMapZoomin, () -> MiniMap.state().zoomIn());
        this.setAction(this.inGuiActions, this.kbMapZoomin, () -> this.getFullscreen().zoomIn());
        this.setAction(this.minimapPreviewActions, this.kbMapZoomout, () -> MiniMap.state().zoomOut());
        this.setAction(this.inGuiActions, this.kbMapZoomout, () -> this.getFullscreen().zoomOut());
        this.setAction(this.minimapPreviewActions, this.kbMapToggleType, () -> MiniMap.state().toggleMapType());
        this.setAction(this.inGuiActions, this.kbMapToggleType, () -> this.getFullscreen().toggleMapType());
        this.setAction(this.minimapPreviewActions, this.kbMinimapPreset, UIManager.INSTANCE::switchMiniMapPreset);
        this.inGameActions.putAll(this.minimapPreviewActions);
        this.setAction(this.inGameActions, this.kbCreateWaypoint, () -> UIManager.INSTANCE.openWaypointEditor(Waypoint.of(this.mc.player), true, null, true));
        this.setAction(this.inGameActions, this.kbToggleAllWaypoints, WaypointManager::toggleAllWaypoints);
        this.setAction(this.inGuiActions, this.kbFullscreenCreateWaypoint, () -> this.getFullscreen().createWaypointAtMouse());
        this.setAction(this.inGuiActions, this.kbFullscreenChatPosition, () -> this.getFullscreen().chatPositionAtMouse());
        this.setAction(this.inGameActions, this.kbFullscreenToggle, UIManager.INSTANCE::openFullscreenMap);
        this.setAction(this.inGuiActions, this.kbFullscreenToggle, UIManager.INSTANCE::closeAll);
        this.setAction(this.inGameActions, this.kbWaypointManager, () -> UIManager.INSTANCE.openWaypointManager(null, null));
        this.setAction(this.inGuiActions, this.kbWaypointManager, () -> {
            if (this.inFullscreenWithoutChat()) {
                UIManager.INSTANCE.openWaypointManager(null, this.getFullscreen());
            }
        });
        this.setAction(this.inGameActions, this.kbMinimapToggle, UIManager.INSTANCE::toggleMinimap);
        this.setAction(this.inGuiActions, this.kbFullmapOptionsManager, () -> UIManager.INSTANCE.openOptionsManager(this.getFullscreen()));
        this.setAction(this.inGuiActions, this.kbFullmapPanNorth, () -> this.getFullscreen().moveCanvas(0, -16));
        this.setAction(this.inGuiActions, this.kbFullmapPanSouth, () -> this.getFullscreen().moveCanvas(0, 16));
        this.setAction(this.inGuiActions, this.kbFullmapPanEast, () -> this.getFullscreen().moveCanvas(16, 0));
        this.setAction(this.inGuiActions, this.kbFullmapPanWest, () -> this.getFullscreen().moveCanvas(-16, 0));
        this.setAction(this.inGuiActions, this.kbFullmapButtonHide, () -> this.getFullscreen().hideButtons());
        return this;
    }

    private UpdateAwareKeyBinding register(String description, KeyConflictContext keyConflictContext, KeyModifier keyModifier, int keyCode) {
        String category = keyConflictContext == KeyConflictContext.GUI ? Constants.getString("jm.common.hotkeys_keybinding_fullscreen_category") : Constants.getString("jm.common.hotkeys_keybinding_category");
        UpdateAwareKeyBinding kb = new UpdateAwareKeyBinding(description, keyConflictContext, keyModifier, InputConstants.Type.KEYSYM, keyCode, category, this);
        try {
            this.keyEvent.register(kb);
        } catch (Throwable var8) {
            ChatLog.announceError("Unexpected error when registering keybinding : " + kb);
        }
        return kb;
    }

    private void setAction(ListMultimap<Integer, KeyBindingAction> multimap, UpdateAwareKeyBinding keyBinding, Runnable action) {
        multimap.put(keyBinding.getKey().getValue(), new KeyBindingAction(keyBinding, action));
    }

    public List<UpdateAwareKeyBinding> getInGuiKeybindings() {
        List<UpdateAwareKeyBinding> list = (List<UpdateAwareKeyBinding>) this.inGuiActions.values().stream().map(KeyBindingAction::getKeyBinding).collect(Collectors.toList());
        list.sort(Comparator.comparing(kb -> Constants.getString(kb.m_90860_())));
        return list;
    }

    protected void sortActions() {
        this.sortActions(this.minimapPreviewActions);
        this.sortActions(this.inGameActions);
        this.sortActions(this.inGuiActions);
        this.sortActionsNeeded = false;
    }

    private void sortActions(ListMultimap<Integer, KeyBindingAction> multimap) {
        List<KeyBindingAction> copy = new ArrayList(multimap.values());
        multimap.clear();
        for (KeyBindingAction kba : copy) {
            multimap.put(kba.getKeyBinding().getKey().getValue(), kba);
        }
        for (Integer key : multimap.keySet()) {
            multimap.get(key).sort(this.kbaComparator);
            Journeymap.getLogger().debug(multimap.get(key));
        }
    }

    public int getPressedKey(ListMultimap<Integer, KeyBindingAction> actions) {
        for (int key : actions.keySet()) {
            for (KeyBindingAction action : actions.get(key)) {
                if (key != -1 && (action.getKeyBinding().m_90857_() || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), key))) {
                    return action.getKeyBinding().getKey().getValue();
                }
            }
        }
        return -1;
    }

    public boolean onGameKeyboardEvent(int key) {
        if (key != InputConstants.UNKNOWN.getValue() && !this.debugKeyDown()) {
            int pressed = this.getPressedKey(this.inGameActions);
            if (pressed == key && !this.isInChat() && this.emptyScreen()) {
                return this.onInputEvent(this.inGameActions, key, true, false);
            }
        }
        return false;
    }

    public boolean onGuiKeyboardEvent(Screen screen, int key) {
        if (key != InputConstants.UNKNOWN.getValue() && !this.debugKeyDown()) {
            if (screen == null) {
                return true;
            }
            int pressed = this.getPressedKey(this.inGuiActions);
            if (pressed == key) {
                if (this.inFullscreenWithoutChat()) {
                    if (this.onInputEvent(this.inGuiActions, key, true, false)) {
                        return true;
                    }
                } else if (this.inMinimapPreview() && this.onInputEvent(this.minimapPreviewActions, key, false, false)) {
                    ((OptionsManager) this.mc.screen).refreshMinimapOptions();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean onMouseEvent(int key, Screen screen) {
        if (this.inFullscreenWithoutChat()) {
            return this.onInputEvent(this.inGuiActions, key, true, true);
        } else {
            if (this.inMinimapPreview()) {
                if (this.onInputEvent(this.minimapPreviewActions, key, false, true)) {
                    ((OptionsManager) this.mc.screen).refreshMinimapOptions();
                }
            } else if (screen == null) {
                return this.onInputEvent(this.inGameActions, key, true, true);
            }
            return false;
        }
    }

    protected boolean onInputEvent(Multimap<Integer, KeyBindingAction> multimap, int key, boolean useContext, boolean mouse) {
        try {
            if (this.sortActionsNeeded) {
                this.sortActions();
            }
            for (KeyBindingAction kba : multimap.get(key)) {
                if (kba.isActive(key, useContext, mouse ? InputConstants.Type.MOUSE : InputConstants.Type.KEYSYM) && Minecraft.getInstance().level != null && this.canUseKey()) {
                    this.logger.debug("Firing " + kba);
                    kba.getAction().run();
                    return true;
                }
            }
        } catch (Exception var7) {
            this.logger.error("Error checking keybinding", LogFormatter.toPartialString(var7));
        }
        return false;
    }

    protected Fullscreen getFullscreen() {
        return UIManager.INSTANCE.openFullscreenMap();
    }

    protected boolean isInChat() {
        return this.mc.screen instanceof ChatScreen;
    }

    protected boolean canUseKey() {
        return this.mc.screen instanceof JmUI || this.mc.screen == null;
    }

    protected boolean emptyScreen() {
        return this.mc.screen == null;
    }

    protected boolean debugKeyDown() {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 292);
    }

    protected boolean inFullscreenWithoutChat() {
        return this.mc.screen instanceof Fullscreen && !((Fullscreen) this.mc.screen).isChatOpen() && !((Fullscreen) this.mc.screen).isSearchFocused();
    }

    protected boolean inFullscreenWithChatOpen() {
        return this.mc.screen instanceof Fullscreen && ((Fullscreen) this.mc.screen).isChatOpen();
    }

    protected boolean inMinimapPreview() {
        return this.mc.screen instanceof OptionsManager && ((OptionsManager) this.mc.screen).previewMiniMap();
    }
}