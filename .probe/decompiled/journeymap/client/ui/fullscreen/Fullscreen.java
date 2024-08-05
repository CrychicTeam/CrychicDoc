package journeymap.client.ui.fullscreen;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import info.journeymap.shaded.org.jetbrains.annotations.Nullable;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import journeymap.client.Constants;
import journeymap.client.JourneymapClient;
import journeymap.client.api.display.Context;
import journeymap.client.api.display.Overlay;
import journeymap.client.api.display.PolygonOverlay;
import journeymap.client.api.impl.ClientAPI;
import journeymap.client.api.model.IFullscreen;
import journeymap.client.api.model.MapPolygon;
import journeymap.client.api.model.ShapeProperties;
import journeymap.client.api.util.UIState;
import journeymap.client.data.DataCache;
import journeymap.client.data.WaypointsData;
import journeymap.client.event.dispatchers.CustomEventDispatcher;
import journeymap.client.event.dispatchers.FullscreenEventDispatcher;
import journeymap.client.feature.Feature;
import journeymap.client.feature.FeatureManager;
import journeymap.client.io.MapSaver;
import journeymap.client.io.ThemeLoader;
import journeymap.client.log.ChatLog;
import journeymap.client.log.StatTimer;
import journeymap.client.model.BlockMD;
import journeymap.client.model.EntityDTO;
import journeymap.client.model.MapState;
import journeymap.client.model.MapType;
import journeymap.client.properties.CoreProperties;
import journeymap.client.properties.FullMapProperties;
import journeymap.client.properties.MiniMapProperties;
import journeymap.client.render.draw.DrawUtil;
import journeymap.client.render.draw.RadarDrawStepFactory;
import journeymap.client.render.draw.WaypointDrawStepFactory;
import journeymap.client.render.map.GridRenderer;
import journeymap.client.render.map.RegionRenderer;
import journeymap.client.task.main.EnsureCurrentColorsTask;
import journeymap.client.task.multi.MapRegionTask;
import journeymap.client.task.multi.SaveMapTask;
import journeymap.client.texture.Texture;
import journeymap.client.texture.TextureCache;
import journeymap.client.ui.UIManager;
import journeymap.client.ui.component.ButtonList;
import journeymap.client.ui.component.IntSliderButton;
import journeymap.client.ui.component.JmUI;
import journeymap.client.ui.component.SearchTextBox;
import journeymap.client.ui.dialog.AutoMapConfirmation;
import journeymap.client.ui.dialog.DeleteMapConfirmation;
import journeymap.client.ui.dialog.FullscreenActions;
import journeymap.client.ui.fullscreen.layer.LayerDelegate;
import journeymap.client.ui.fullscreen.menu.PopupMenu;
import journeymap.client.ui.minimap.Shape;
import journeymap.client.ui.option.LocationFormat;
import journeymap.client.ui.theme.Theme;
import journeymap.client.ui.theme.ThemeButton;
import journeymap.client.ui.theme.ThemeToggle;
import journeymap.client.ui.theme.ThemeToolbar;
import journeymap.client.waypoint.Waypoint;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import journeymap.common.version.VersionCheck;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.Logger;

public class Fullscreen extends JmUI implements IFullscreen {

    private long lastGridUpdate = 0L;

    private static final MapState state = new MapState();

    private static final GridRenderer gridRenderer = new GridRenderer(Context.UI.Fullscreen);

    private final WaypointDrawStepFactory waypointRenderer = new WaypointDrawStepFactory();

    private final RadarDrawStepFactory radarRenderer = new RadarDrawStepFactory();

    private final LayerDelegate layerDelegate;

    private FullMapProperties fullMapProperties = JourneymapClient.getInstance().getFullMapProperties();

    private CoreProperties coreProperties = JourneymapClient.getInstance().getCoreProperties();

    private boolean firstLayoutPass = true;

    private boolean buttonsVisible = true;

    private Boolean isScrolling = false;

    private int msx;

    private int msy;

    private int mx;

    private int my;

    private Logger logger = Journeymap.getLogger();

    private MapChat chat;

    private ThemeButton buttonFollow;

    private ThemeButton buttonZoomIn;

    private ThemeButton buttonZoomOut;

    private ThemeButton buttonSearch;

    private SearchTextBox searchTextX;

    private SearchTextBox searchTextZ;

    private ThemeButton buttonExecuteSearch;

    private ThemeToolbar searchToolBar;

    private ThemeButton buttonDay;

    private ThemeButton buttonNight;

    private ThemeButton buttonTopo;

    private ThemeButton buttonBiome;

    private ThemeButton buttonLayers;

    private ThemeButton buttonCaves;

    private ThemeButton buttonAlert;

    private ThemeButton buttonOptions;

    private ThemeButton buttonClose;

    private ThemeButton buttonTheme;

    private ThemeButton buttonWaypointManager;

    private ThemeButton buttonMobs;

    private ThemeButton buttonAnimals;

    private ThemeButton buttonPets;

    private ThemeButton buttonVillagers;

    private ThemeButton buttonPlayers;

    private ThemeButton buttonGrid;

    private ThemeButton buttonKeys;

    private ThemeButton buttonAutomap;

    private ThemeButton buttonSavemap;

    private ThemeButton buttonDeletemap;

    private ThemeButton buttonDisable;

    private ThemeButton buttonResetPalette;

    private ThemeButton buttonBrowser;

    private ThemeButton buttonAbout;

    ThemeButton overlayRenderButton;

    private ThemeToolbar mapTypeToolbar;

    private ThemeToolbar optionsToolbar;

    private ThemeToolbar menuToolbar;

    private ThemeToolbar zoomToolbar;

    private ThemeToolbar addonToolbar;

    private List<ThemeToolbar> customToolbars;

    private int bgColor = 2236962;

    private Theme.LabelSpec statusLabelSpec;

    private StatTimer renderTimer = StatTimer.get("Fullscreen.render");

    private StatTimer drawMapTimer = StatTimer.get("Fullscreen.render.drawMap", 50);

    private StatTimer drawMapTimerWithRefresh = StatTimer.get("Fullscreen.drawMap+refreshState", 5);

    private LocationFormat locationFormat = new LocationFormat();

    private List<Overlay> tempOverlays = new ArrayList();

    private IntSliderButton sliderCaveLayer;

    private List<FormattedCharSequence> autoMapOnTooltip;

    private List<FormattedCharSequence> autoMapOffTooltip;

    private Double mapTypeToolbarBounds;

    private Double optionsToolbarBounds;

    private Double menuToolbarBounds;

    private Minecraft minecraft;

    public boolean chatOpenedFromEvent = false;

    public final PopupMenu popupMenu = new PopupMenu(this);

    private List<FormattedCharSequence> entityTooltip;

    public Fullscreen() {
        super(null);
        this.minecraft = Minecraft.getInstance();
        gridRenderer.setGridSize(getCalculatedGridSize());
        gridRenderer.clear();
        this.layerDelegate = new LayerDelegate(this);
        if (JourneymapClient.getInstance().getFullMapProperties().showCaves.get() && DataCache.getPlayer().underground && state.follow.get() && FeatureManager.getInstance().isAllowed(Feature.MapCaves)) {
            state.setMapType(MapType.underground(DataCache.getPlayer()));
        }
    }

    public static synchronized MapState state() {
        return state;
    }

    public static synchronized UIState uiState() {
        return gridRenderer.getUIState();
    }

    @Override
    public UIState getUiState() {
        return gridRenderer.getUIState();
    }

    @Override
    public Screen getScreen() {
        return this;
    }

    public void reset() {
        this.isScrolling = false;
        gridRenderer.setGridSize(getCalculatedGridSize());
        state.requireRefresh();
        gridRenderer.clear();
        this.getRenderables().clear();
    }

    @Override
    public void init() {
        this.fullMapProperties = JourneymapClient.getInstance().getFullMapProperties();
        state.requireRefresh();
        state.refresh(this.minecraft, Minecraft.getInstance().player, this.fullMapProperties);
        MapType mapType = state.getMapType();
        if (!mapType.dimension.equals(this.minecraft.player.m_9236_().dimension().location().getPath())) {
            gridRenderer.clear();
        }
        this.initButtons();
        String thisVersion = Journeymap.JM_VERSION.toString();
        String splashViewed = JourneymapClient.getInstance().getCoreProperties().splashViewed.get();
        if (splashViewed == null || !thisVersion.equals(splashViewed)) {
            UIManager.INSTANCE.openSplash(this);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        try {
            PoseStack poseStack = graphics.pose();
            this.renderBackground(graphics);
            this.drawMap(graphics, mouseX, mouseY);
            this.drawLogo(poseStack);
            this.renderTimer.start();
            this.layoutButtons(graphics);
            List<FormattedCharSequence> tooltip = null;
            if (this.firstLayoutPass) {
                this.layoutButtons(graphics);
                this.updateMapType(state.getMapType());
                this.firstLayoutPass = false;
            } else {
                for (int k = 0; k < this.getRenderables().size(); k++) {
                    Button guibutton = (Button) this.getRenderables().get(k);
                    guibutton.m_88315_(graphics, mouseX, mouseY, partialTicks);
                    if (tooltip == null && guibutton instanceof journeymap.client.ui.component.Button) {
                        journeymap.client.ui.component.Button button = (journeymap.client.ui.component.Button) guibutton;
                        button = (journeymap.client.ui.component.Button) guibutton;
                        if (button.mouseOver((double) this.mx, (double) this.my)) {
                            tooltip = button.getFormattedTooltip();
                        }
                    }
                }
            }
            if (this.chat != null) {
                this.chat.render(graphics, mouseX, mouseY, partialTicks);
            }
            if (tooltip == null || tooltip.isEmpty()) {
                tooltip = this.entityTooltip;
            }
            if (tooltip != null && !tooltip.isEmpty()) {
                this.renderWrappedToolTip(graphics, tooltip, this.mx, this.my, this.getFontRenderer());
                this.entityTooltip = null;
            }
        } catch (Exception var13) {
            this.logger.log(org.apache.logging.log4j.Level.ERROR, "Unexpected exception in jm.fullscreen.render(): " + LogFormatter.toString(var13));
            UIManager.INSTANCE.closeAll();
        } finally {
            this.renderTimer.stop();
        }
    }

    public void queueToolTip(List<Component> tooltips) {
        this.entityTooltip = (List<FormattedCharSequence>) tooltips.stream().map(Component::m_7532_).collect(Collectors.toList());
    }

    private static int getCalculatedGridSize() {
        Window mainWindow = Minecraft.getInstance().getWindow();
        int screenWidth = mainWindow != null ? mainWindow.getWidth() : 1920;
        int gridSize = 0;
        while (gridSize * 512 < screenWidth) {
            gridSize++;
        }
        if (++gridSize % 2 == 0) {
            gridSize++;
        }
        return gridSize;
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        super.m_6574_(minecraft, width, height);
        gridRenderer.setGridSize(getCalculatedGridSize());
        gridRenderer.clear();
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);
        state.requireRefresh();
        if (this.chat == null) {
            this.chat = new MapChat("", true);
        }
        if (this.chat != null) {
            this.chat.m_6575_(minecraft, width, height);
        }
        this.init();
        this.refreshState();
        this.mouseMoved(0.0, 0.0);
    }

    void initButtons() {
        if (this.getRenderables().isEmpty()) {
            this.firstLayoutPass = true;
            Theme theme = ThemeLoader.getCurrentTheme();
            MapType mapType = state.getMapType();
            this.bgColor = theme.fullscreen.background.getColor();
            this.statusLabelSpec = theme.fullscreen.statusLabel;
            this.buttonDay = (ThemeButton) this.m_142416_(new ThemeToggle(theme, "jm.fullscreen.map_day", "day", button -> {
                if (this.buttonDay.isEnabled()) {
                    this.updateMapType(MapType.day(state.getDimension()));
                }
            }));
            this.buttonDay.setToggled(Boolean.valueOf(mapType.isDay()), false);
            this.buttonDay.setStaysOn(true);
            this.buttonNight = (ThemeButton) this.m_142416_(new ThemeToggle(theme, "jm.fullscreen.map_night", "night", button -> {
                if (this.buttonNight.isEnabled()) {
                    this.updateMapType(MapType.night(state.getDimension()));
                }
            }));
            this.buttonNight.setToggled(Boolean.valueOf(mapType.isNight()), false);
            this.buttonNight.setStaysOn(true);
            this.buttonTopo = (ThemeButton) this.m_142416_(new ThemeToggle(theme, "jm.fullscreen.map_topo", "topo", button -> {
                if (this.buttonTopo.isEnabled()) {
                    this.updateMapType(MapType.topo(state.getDimension()));
                }
            }));
            this.buttonTopo.setDrawButton(this.coreProperties.mapTopography.get());
            this.buttonTopo.setToggled(Boolean.valueOf(mapType.isTopo()), false);
            this.buttonTopo.setStaysOn(true);
            this.buttonBiome = (ThemeButton) this.m_142416_(new ThemeToggle(theme, "jm.fullscreen.map_biome", "biome", button -> {
                if (this.buttonBiome.isEnabled()) {
                    this.updateMapType(MapType.biome(state.getDimension()));
                }
            }));
            this.buttonBiome.setDrawButton(this.coreProperties.mapBiome.get());
            this.buttonBiome.setToggled(Boolean.valueOf(mapType.isBiome()), false);
            this.buttonBiome.setStaysOn(true);
            this.buttonLayers = (ThemeButton) this.m_142416_(new ThemeToggle(theme, "jm.fullscreen.map_cave_layers", "layers", button -> {
                EntityDTO player = DataCache.getPlayer();
                this.buttonLayers.toggle();
                this.sliderCaveLayer.setDrawButton(this.buttonLayers.getToggled());
            }));
            this.buttonLayers.setEnabled(state.isCaveMappingAllowed());
            this.buttonLayers.setDrawButton(state.isCaveMappingAllowed());
            Font fontRenderer = this.getFontRenderer();
            this.sliderCaveLayer = (IntSliderButton) this.m_169394_(new IntSliderButton(state.getLastSlice(), Constants.getString("jm.fullscreen.map_cave_layers.button") + " ", ""));
            this.sliderCaveLayer.m_93674_(this.sliderCaveLayer.getFitWidth(fontRenderer) + fontRenderer.width("0"));
            this.sliderCaveLayer.setDefaultStyle(false);
            this.sliderCaveLayer.setDrawBackground(true);
            Theme.Control.ButtonSpec buttonSpec = this.buttonLayers.getButtonSpec();
            this.sliderCaveLayer.setBackgroundColors(Integer.valueOf(buttonSpec.buttonDisabled.getColor()), Integer.valueOf(buttonSpec.buttonOff.getColor()), Integer.valueOf(buttonSpec.buttonOff.getColor()));
            this.sliderCaveLayer.setLabelColors(Integer.valueOf(buttonSpec.iconHoverOff.getColor()), Integer.valueOf(buttonSpec.iconHoverOn.getColor()), Integer.valueOf(buttonSpec.iconDisabled.getColor()));
            this.sliderCaveLayer.setDrawButton(false);
            this.sliderCaveLayer.addClickListener(button -> {
                state.setMapType(MapType.underground(this.sliderCaveLayer.getValue(), state.getDimension()));
                state.setForceRefreshState(true);
                return true;
            });
            this.getRenderables().add(this.sliderCaveLayer);
            this.buttonSearch = (ThemeButton) this.m_142416_(new ThemeButton(theme, "jm.fullscreen.search", "search", button -> {
                this.buttonSearch.toggle();
                this.toggleSearchBar(this.buttonSearch.getToggled());
            }));
            this.searchTextX = (SearchTextBox) this.m_142416_(new SearchTextBox("x:", fontRenderer, 40, 20));
            this.searchTextZ = (SearchTextBox) this.m_142416_(new SearchTextBox("z:", fontRenderer, 40, 20));
            this.buttonExecuteSearch = (ThemeButton) this.m_142416_(new ThemeButton(theme, "jm.fullscreen.search_execute", "follow", button -> this.executeSearch()));
            this.searchTextX.setVisible(false);
            this.searchTextZ.setVisible(false);
            this.buttonExecuteSearch.setVisible(false);
            this.buttonFollow = (ThemeButton) this.m_142416_(new ThemeButton(theme, "jm.fullscreen.follow", "follow", button -> this.toggleFollow()));
            this.buttonZoomIn = (ThemeButton) this.m_142416_(new ThemeButton(theme, "jm.fullscreen.zoom_in", "zoomin", button -> this.zoomIn()));
            this.buttonZoomIn.setEnabled(this.fullMapProperties.zoomLevel.get() < 5);
            this.buttonZoomIn.setDisplayClickToggle(false);
            this.buttonZoomOut = (ThemeButton) this.m_142416_(new ThemeButton(theme, "jm.fullscreen.zoom_out", "zoomout", button -> this.zoomOut()));
            this.buttonZoomOut.setEnabled(this.fullMapProperties.zoomLevel.get() > 0);
            this.buttonZoomOut.setDisplayClickToggle(false);
            this.buttonWaypointManager = (ThemeButton) this.m_142416_(new ThemeButton(theme, "jm.waypoint.waypoints_button", "waypoints", button -> UIManager.INSTANCE.openWaypointManager(null, this)));
            this.buttonWaypointManager.setEnabled(WaypointsData.isManagerEnabled());
            this.buttonWaypointManager.setDrawButton(WaypointsData.isManagerEnabled());
            this.buttonTheme = (ThemeButton) this.m_142416_(new ThemeButton(theme, "jm.common.ui_theme", "theme", button -> {
                ThemeLoader.loadNextTheme();
                UIManager.INSTANCE.getMiniMap().reset();
                this.m_169413_();
            }));
            Style italic = Style.EMPTY.withItalic(true);
            FormattedCharSequence[] tooltips = new FormattedCharSequence[] { Constants.getFormattedText(Constants.getString("jm.common.ui_theme_name", theme.name), italic, fontRenderer, 200), Constants.getFormattedText(Constants.getString("jm.common.ui_theme_author", theme.author), italic, fontRenderer, 200) };
            this.buttonTheme.setAdditionalTooltips(Arrays.asList(tooltips));
            this.buttonOptions = (ThemeButton) this.m_142416_(new ThemeButton(theme, "jm.common.options_button", "options", button -> {
                try {
                    UIManager.INSTANCE.openOptionsManager(this);
                    this.m_169413_();
                } catch (Exception var3x) {
                    var3x.printStackTrace();
                }
            }));
            String versionAvailable = Constants.getString("jm.common.new_version_available", VersionCheck.getVersionAvailable());
            this.buttonAlert = (ThemeButton) this.m_142416_(new ThemeButton(theme, versionAvailable, versionAvailable, false, "alert", button -> {
                FullscreenActions.launchDownloadWebsite();
                this.buttonAlert.setDrawButton(false);
            }));
            this.buttonAlert.setDrawButton(VersionCheck.getVersionIsChecked() && !VersionCheck.getVersionIsCurrent());
            this.buttonAlert.setToggled(Boolean.valueOf(true));
            this.buttonClose = (ThemeButton) this.m_142416_(new ThemeButton(theme, "jm.common.close", "close", button -> UIManager.INSTANCE.closeAll()));
            this.buttonCaves = (ThemeButton) this.m_142416_(new ThemeToggle(theme, "jm.common.show_caves", "caves", this.fullMapProperties.showCaves, button -> {
                EntityDTO player = DataCache.getPlayer();
                this.buttonCaves.setToggled(Boolean.valueOf(!this.buttonCaves.getToggled()));
                if (this.buttonCaves.getToggled()) {
                    this.updateMapType(MapType.underground(player));
                } else {
                    this.updateMapType(MapType.day(player));
                }
            }));
            this.buttonCaves.setTooltip(new String[] { Constants.getString("jm.common.show_caves.tooltip") });
            this.buttonCaves.setDrawButton(state.isCaveMappingAllowed());
            this.buttonMobs = (ThemeButton) this.m_142416_(new ThemeToggle(theme, "jm.common.show_mobs", "monsters", this.fullMapProperties.showMobs, button -> this.buttonMobs.toggle()));
            this.buttonMobs.setTooltip(new String[] { Constants.getString("jm.common.show_mobs.tooltip") });
            this.buttonMobs.setDrawButton(FeatureManager.getInstance().isAllowed(Feature.RadarMobs));
            this.buttonAnimals = (ThemeButton) this.m_142416_(new ThemeToggle(theme, "jm.common.show_animals", "animals", this.fullMapProperties.showAnimals, button -> this.buttonAnimals.toggle()));
            this.buttonAnimals.setTooltip(new String[] { Constants.getString("jm.common.show_animals.tooltip") });
            this.buttonAnimals.setDrawButton(FeatureManager.getInstance().isAllowed(Feature.RadarAnimals));
            this.buttonPets = (ThemeButton) this.m_142416_(new ThemeToggle(theme, "jm.common.show_pets", "pets", this.fullMapProperties.showPets, button -> this.buttonPets.toggle()));
            this.buttonPets.setTooltip(new String[] { Constants.getString("jm.common.show_pets.tooltip") });
            this.buttonPets.setDrawButton(FeatureManager.getInstance().isAllowed(Feature.RadarAnimals));
            this.buttonVillagers = (ThemeButton) this.m_142416_(new ThemeToggle(theme, "jm.common.show_villagers", "villagers", this.fullMapProperties.showVillagers, button -> this.buttonVillagers.toggle()));
            this.buttonVillagers.setTooltip(new String[] { Constants.getString("jm.common.show_villagers.tooltip") });
            this.buttonVillagers.setDrawButton(FeatureManager.getInstance().isAllowed(Feature.RadarVillagers));
            this.buttonPlayers = (ThemeButton) this.m_142416_(new ThemeToggle(theme, "jm.common.show_players", "players", this.fullMapProperties.showPlayers, button -> this.buttonPlayers.toggle()));
            this.buttonPlayers.setTooltip(new String[] { Constants.getString("jm.common.show_players.tooltip") });
            this.buttonPlayers.setDrawButton(!this.minecraft.hasSingleplayerServer() && FeatureManager.getInstance().isAllowed(Feature.RadarPlayers));
            this.buttonGrid = (ThemeButton) this.m_142416_(new ThemeToggle(theme, "jm.common.show_grid", "grid", this.fullMapProperties.showGrid, button -> {
                this.buttonGrid.toggle();
                boolean shiftDown = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 340) || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 344);
                if (shiftDown) {
                    UIManager.INSTANCE.openGridEditor(this);
                    this.buttonGrid.setValue(Boolean.valueOf(true));
                }
            }));
            this.buttonGrid.setTooltip(new String[] { Constants.getString("jm.common.show_grid_shift.tooltip") });
            this.buttonGrid.setTooltip(new String[] { Constants.getString("jm.common.show_grid_shift.tooltip") });
            this.buttonKeys = (ThemeButton) this.m_142416_(new ThemeToggle(theme, "jm.common.show_keys", "keys", this.fullMapProperties.showKeys, button -> this.buttonKeys.toggle()));
            this.buttonKeys.setTooltip(new String[] { Constants.getString("jm.common.show_keys.tooltip") });
            this.buttonAbout = (ThemeButton) this.m_142416_(new ThemeButton(theme, "jm.common.splash_about", "about", button -> UIManager.INSTANCE.openSplash(this)));
            this.overlayRenderButton = new ThemeButton(theme, "region display on", " region display off", false, "server", button -> RegionRenderer.render(!RegionRenderer.TOGGLED));
            this.m_142416_(this.overlayRenderButton);
            this.buttonSavemap = (ThemeButton) this.m_142416_(new ThemeButton(theme, "jm.common.save_map", "savemap", button -> {
                this.buttonSavemap.setEnabled(false);
                try {
                    MapSaver mapSaver = new MapSaver(state.getWorldDir(), state.getMapType());
                    if (mapSaver.isValid()) {
                        JourneymapClient.getInstance().toggleTask(SaveMapTask.Manager.class, true, mapSaver);
                        ChatLog.announceI18N("jm.common.save_filename", mapSaver.getSaveFileName());
                    }
                } finally {
                    this.buttonSavemap.setToggled(Boolean.valueOf(false));
                    this.buttonSavemap.setEnabled(true);
                }
            }));
            this.buttonBrowser = (ThemeButton) this.m_142416_(new ThemeButton(theme, "jm.common.use_browser", "browser", button -> FullscreenActions.launchLocalhost()));
            boolean webMapEnabled = JourneymapClient.getInstance().getWebMapProperties().enabled.get();
            this.buttonBrowser.setEnabled(webMapEnabled);
            this.buttonBrowser.setDrawButton(webMapEnabled);
            boolean automapRunning = JourneymapClient.getInstance().isTaskManagerEnabled(MapRegionTask.Manager.class);
            String autoMapOn = Constants.getString("jm.common.automap_stop_title");
            String autoMapOff = Constants.getString("jm.common.automap_title");
            this.autoMapOnTooltip = fontRenderer.split(Constants.getTranslatedTextComponent("jm.common.automap_stop_text"), 200);
            this.autoMapOffTooltip = fontRenderer.split(Constants.getTranslatedTextComponent("jm.common.automap_text"), 200);
            this.buttonAutomap = (ThemeButton) this.m_142416_(new ThemeToggle(theme, autoMapOn, autoMapOff, "automap", button -> {
                if (!this.buttonAutomap.getToggled()) {
                    UIManager.INSTANCE.open(AutoMapConfirmation.class, this);
                } else {
                    JourneymapClient.getInstance().toggleTask(MapRegionTask.Manager.class, false, null);
                    this.buttonAutomap.setToggled(Boolean.valueOf(false), false);
                    this.m_169413_();
                }
            }));
            this.buttonAutomap.setEnabled(Minecraft.getInstance().hasSingleplayerServer() && JourneymapClient.getInstance().getCoreProperties().mappingEnabled.get());
            this.buttonAutomap.setToggled(Boolean.valueOf(automapRunning), false);
            this.buttonDeletemap = (ThemeButton) this.m_142416_(new ThemeButton(theme, "jm.common.deletemap_title", "delete", button -> UIManager.INSTANCE.open(DeleteMapConfirmation.class, this)));
            this.buttonDeletemap.setAdditionalTooltips(fontRenderer.split(Constants.getTranslatedTextComponent("jm.common.deletemap_text"), 200));
            this.buttonDisable = (ThemeButton) this.m_142416_(new ThemeToggle(theme, "jm.common.enable_mapping_false", "disable", button -> {
                this.buttonDisable.toggle();
                JourneymapClient.getInstance().getCoreProperties().mappingEnabled.set(!this.buttonDisable.getToggled());
                if (JourneymapClient.getInstance().getCoreProperties().mappingEnabled.get()) {
                    DataCache.INSTANCE.invalidateChunkMDCache();
                    ChatLog.announceI18N("jm.common.enable_mapping_true_text");
                } else {
                    JourneymapClient.getInstance().stopMapping();
                    BlockMD.reset();
                    ChatLog.announceI18N("jm.common.enable_mapping_false_text");
                }
            }));
            this.buttonResetPalette = (ThemeButton) this.m_142416_(new ThemeButton(theme, "jm.common.colorreset_title", "reset", button -> JourneymapClient.getInstance().queueMainThreadTask(new EnsureCurrentColorsTask(true, true))));
            this.buttonResetPalette.setAdditionalTooltips(fontRenderer.split(Constants.getTranslatedTextComponent("jm.common.colorreset_text"), 200));
            this.mapTypeToolbar = CustomEventDispatcher.getInstance().getMapTypeToolbar(this, theme, this.buttonLayers, this.buttonTopo, this.buttonBiome, this.buttonNight, this.buttonDay);
            this.mapTypeToolbar.addAllButtons(this);
            this.optionsToolbar = new ThemeToolbar(theme, this.buttonCaves, this.buttonMobs, this.buttonAnimals, this.buttonPets, this.buttonVillagers, this.buttonPlayers, this.buttonGrid, this.buttonKeys);
            this.optionsToolbar.addAllButtons(this);
            this.optionsToolbar.f_93624_ = false;
            this.menuToolbar = new ThemeToolbar(theme, this.buttonWaypointManager, this.buttonOptions, this.buttonAbout, this.buttonBrowser, this.buttonTheme, this.buttonResetPalette, this.buttonDeletemap, this.buttonSavemap, this.buttonAutomap, this.buttonDisable, this.overlayRenderButton);
            this.menuToolbar.addAllButtons(this);
            this.menuToolbar.f_93624_ = false;
            this.zoomToolbar = new ThemeToolbar(theme, this.buttonSearch, this.buttonFollow, this.buttonZoomIn, this.buttonZoomOut);
            this.zoomToolbar.setLayout(ButtonList.Layout.Vertical, ButtonList.Direction.LeftToRight);
            this.zoomToolbar.addAllButtons(this);
            this.searchToolBar = new ThemeToolbar(theme, this.searchTextX, this.searchTextZ, this.buttonExecuteSearch);
            this.searchToolBar.setLayout(ButtonList.Layout.CenteredHorizontal, ButtonList.Direction.LeftToRight);
            this.searchToolBar.addAllButtons(this);
            this.addonToolbar = CustomEventDispatcher.getInstance().getAddonToolbar(this, theme);
            if (this.addonToolbar != null) {
                this.addonToolbar.setLayout(ButtonList.Layout.CenteredVertical, ButtonList.Direction.LeftToRight);
                this.addonToolbar.addAllButtons(this);
            }
            this.customToolbars = CustomEventDispatcher.getInstance().getCustomToolBars(this, theme);
            this.getRenderables().add(this.buttonAlert);
            this.getRenderables().add(this.buttonClose);
        }
    }

    @Override
    protected void layoutButtons(GuiGraphics graphics) {
        if (this.buttonsVisible) {
            this.mx = (int) (this.minecraft.mouseHandler.xpos() * (double) this.f_96543_ / (double) this.minecraft.getWindow().getScreenWidth());
            this.my = (int) (this.minecraft.mouseHandler.ypos() * (double) this.f_96544_ / (double) this.minecraft.getWindow().getScreenHeight() - 1.0);
            if (this.buttonDay != null && !this.buttonDay.hasValidTextures()) {
                this.m_169413_();
            }
            if (this.getRenderables().isEmpty()) {
                this.initButtons();
            }
            this.menuToolbar.setDrawToolbar(!this.isChatOpen());
            MapType mapType = state.getMapType();
            this.buttonDay.setEnabled(state.isSurfaceMappingAllowed());
            this.buttonDay.setToggled(Boolean.valueOf(this.buttonDay.isEnabled() && mapType.isDay()));
            this.buttonNight.setEnabled(state.isSurfaceMappingAllowed());
            this.buttonNight.setToggled(Boolean.valueOf(this.buttonNight.isEnabled() && mapType.isNight()));
            this.buttonTopo.setEnabled(state.isTopoMappingAllowed());
            this.buttonTopo.setToggled(Boolean.valueOf(this.buttonTopo.isEnabled() && mapType.isTopo()));
            this.buttonBiome.setEnabled(state.isBiomeMappingAllowed());
            this.buttonBiome.setToggled(Boolean.valueOf(this.buttonBiome.isEnabled() && mapType.isBiome()));
            this.buttonCaves.setEnabled(state.isCaveMappingAllowed() && !Level.NETHER.equals(this.minecraft.level.m_46472_()));
            this.buttonCaves.setToggled(Boolean.valueOf(this.buttonCaves.isEnabled() && mapType.isUnderground()));
            this.buttonFollow.setEnabled(!state.follow.get());
            boolean automapRunning = JourneymapClient.getInstance().isTaskManagerEnabled(MapRegionTask.Manager.class);
            boolean mappingEnabled = JourneymapClient.getInstance().getCoreProperties().mappingEnabled.get();
            this.buttonDisable.setToggled(Boolean.valueOf(!mappingEnabled), false);
            this.buttonAutomap.setToggled(Boolean.valueOf(automapRunning), false);
            this.buttonAutomap.setEnabled(mappingEnabled);
            this.buttonAutomap.setAdditionalTooltips(automapRunning ? this.autoMapOnTooltip : this.autoMapOffTooltip);
            this.buttonWaypointManager.setEnabled(WaypointsData.isManagerEnabled());
            this.buttonWaypointManager.setDrawButton(WaypointsData.isManagerEnabled() && !this.isChatOpen());
            boolean webMapEnabled = JourneymapClient.getInstance().getWebMapProperties().enabled.get();
            this.buttonBrowser.setEnabled(webMapEnabled && mappingEnabled);
            this.buttonBrowser.setDrawButton(webMapEnabled && !this.isChatOpen());
            boolean mainThreadActive = JourneymapClient.getInstance().isMainThreadTaskActive();
            this.overlayRenderButton.setVisible(System.getProperty("journeymap.map_testing") != null);
            int padding = this.mapTypeToolbar.getToolbarSpec().padding;
            this.zoomToolbar.layoutCenteredVertical(this.zoomToolbar.getHMargin(), this.f_96544_ / 2, true, padding);
            this.searchToolBar.layoutHorizontal(this.zoomToolbar.getRightX() + 2, this.zoomToolbar.m_252907_() + 1, true, 7, true);
            this.searchTextX.m_252865_(this.searchTextX.m_252754_() + 3);
            this.searchTextZ.m_252865_(this.searchTextZ.m_252754_() + 2);
            this.buttonExecuteSearch.setDisplayClickToggle(false);
            if (this.addonToolbar != null) {
                this.addonToolbar.layoutCenteredVertical(this.f_96543_ - this.addonToolbar.m_5711_() - this.zoomToolbar.getHMargin(), this.f_96544_ / 2, true, padding);
            }
            int topY = this.mapTypeToolbar.getVMargin();
            int margin = this.mapTypeToolbar.getHMargin();
            this.buttonClose.leftOf(this.f_96543_ - this.zoomToolbar.getHMargin()).below(this.mapTypeToolbar.getVMargin());
            this.buttonAlert.leftOf(this.f_96543_ - this.zoomToolbar.getHMargin()).below(this.buttonClose, padding);
            int toolbarsWidth = this.mapTypeToolbar.m_5711_() + this.optionsToolbar.m_5711_() + margin + padding;
            int startX = (this.f_96543_ - toolbarsWidth) / 2;
            Double oldBounds = this.mapTypeToolbar.getBounds();
            this.mapTypeToolbar.layoutHorizontal(startX + this.mapTypeToolbar.m_5711_(), topY, false, padding);
            if (!this.mapTypeToolbar.getBounds().equals(oldBounds)) {
                this.mapTypeToolbarBounds = null;
            }
            oldBounds = this.optionsToolbar.getBounds();
            this.optionsToolbar.layoutHorizontal(this.mapTypeToolbar.getRightX() + margin, topY, true, padding);
            this.optionsToolbar.f_93624_ = true;
            if (!this.optionsToolbar.getBounds().equals(oldBounds)) {
                this.optionsToolbarBounds = null;
            }
            oldBounds = this.menuToolbar.getBounds();
            this.menuToolbar.layoutCenteredHorizontal(this.f_96543_ / 2, this.f_96544_ - this.menuToolbar.m_93694_() - this.menuToolbar.getVMargin(), true, padding);
            if (!this.menuToolbar.getBounds().equals(oldBounds)) {
                this.menuToolbarBounds = null;
            }
            if (this.sliderCaveLayer.isVisible()) {
                this.sliderCaveLayer.below(this.buttonLayers, 1).centerHorizontalOn(this.buttonLayers.getCenterX());
                int slice = this.sliderCaveLayer.getValue();
                int minY = slice << 4;
                int maxY = (slice + 1 << 4) - 1;
                this.sliderCaveLayer.setTooltip(new String[] { Constants.getString("jm.fullscreen.map_cave_layers.button.tooltip", minY, maxY) });
            }
        }
    }

    @Nullable
    public Double getOptionsToolbarBounds() {
        if (this.optionsToolbar != null && this.optionsToolbar.isVisible()) {
            Double unscaled = this.optionsToolbar.getBounds();
            this.optionsToolbarBounds = new Double(unscaled.x * this.getScaleFactor(), unscaled.y * this.getScaleFactor(), unscaled.width * this.getScaleFactor(), unscaled.height * this.getScaleFactor());
        }
        return this.optionsToolbarBounds;
    }

    @Nullable
    public Double getMenuToolbarBounds() {
        if (this.menuToolbar != null && this.menuToolbar.isVisible()) {
            Double unscaled = this.menuToolbar.getBounds();
            this.menuToolbarBounds = new Double(unscaled.x * this.getScaleFactor(), unscaled.y * this.getScaleFactor(), unscaled.width * this.getScaleFactor(), unscaled.height * this.getScaleFactor());
        }
        return this.menuToolbarBounds;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double wheel) {
        try {
            if (wheel > 0.0) {
                this.zoomIn();
            } else if (wheel < 0.0) {
                this.zoomOut();
            }
            return true;
        } catch (Throwable var8) {
            Journeymap.getLogger().error(LogFormatter.toPartialString(var8));
            return false;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        try {
            if (this.chat != null && !this.chat.isHidden()) {
                this.chat.mouseClicked(mouseX, mouseY, mouseButton);
            }
            java.awt.geom.Point2D.Double mousePosition = new java.awt.geom.Point2D.Double(this.minecraft.mouseHandler.xpos(), this.minecraft.mouseHandler.ypos());
            this.mx = (int) (this.minecraft.mouseHandler.xpos() * (double) this.f_96543_ / (double) this.minecraft.getWindow().getScreenWidth());
            this.my = (int) (this.minecraft.mouseHandler.ypos() * (double) this.f_96544_ / (double) this.minecraft.getWindow().getScreenHeight() - 1.0);
            if (!this.isMouseOverButton(mouseX, mouseY)) {
                if (FullscreenEventDispatcher.clickEventPre(this.getBlockAtMouse(), Minecraft.getInstance().level.m_46472_(), new java.awt.geom.Point2D.Double(mouseX, mouseY), mouseButton)) {
                    return false;
                }
                this.popupMenu.setClickLoc((int) mouseX, (int) mouseY);
                this.layerDelegate.onMouseClicked(this.minecraft, gridRenderer, mousePosition, mouseButton, this.getMapFontScale());
                FullscreenEventDispatcher.clickEventPost(this.getBlockAtMouse(), Minecraft.getInstance().level.m_46472_(), new java.awt.geom.Point2D.Double(mouseX, mouseY), mouseButton);
            }
            this.sliderCaveLayer.mouseClicked(mouseX, mouseY, mouseButton);
        } catch (Throwable var8) {
            Journeymap.getLogger().error(LogFormatter.toPartialString(var8));
        }
        this.popupMenu.resetPass();
        if (!this.searchTextX.mouseOver(mouseX, mouseY) && !this.searchTextZ.mouseOver(mouseX, mouseY)) {
            return super.m_6375_(mouseX, mouseY, mouseButton);
        } else {
            boolean zClicked = this.searchTextZ.mouseClicked(mouseX, mouseY, mouseButton);
            boolean xClicked = this.searchTextX.mouseClicked(mouseX, mouseY, mouseButton);
            super.m_7522_(zClicked ? this.searchTextZ : this.searchTextX);
            return true;
        }
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        java.awt.geom.Point2D.Double mousePosition = new java.awt.geom.Point2D.Double(this.minecraft.mouseHandler.xpos(), this.minecraft.mouseHandler.ypos());
        this.layerDelegate.onMouseMove(this.minecraft, gridRenderer, mousePosition, this.getMapFontScale(), this.isScrolling);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double mouseDX, double mouseDY) {
        this.sliderCaveLayer.mouseDragged(mouseX, mouseY, button, mouseDX, mouseDY);
        if (this.sliderCaveLayer.dragging || !(this.minecraft.screen instanceof Fullscreen)) {
            this.isScrolling = false;
        }
        long sysTime = Util.getMillis();
        boolean updateGrid = sysTime - this.lastGridUpdate > 200L;
        if (button == 0 && this.isScrolling && updateGrid) {
            this.lastGridUpdate = sysTime;
            this.updateGrid();
        }
        if (button == 0 && !this.isScrolling && !this.isMouseOverButton(mouseX, mouseY)) {
            java.awt.geom.Point2D.Double mousePosition = new java.awt.geom.Point2D.Double(this.minecraft.mouseHandler.xpos(), this.minecraft.mouseHandler.ypos());
            if (FullscreenEventDispatcher.dragEventPre(this.getBlockAtMouse(), Minecraft.getInstance().level.m_46472_(), new java.awt.geom.Point2D.Double(mouseX, mouseY), button)) {
                return false;
            }
            this.isScrolling = !this.sliderCaveLayer.dragging;
            this.msx = this.mx;
            this.msy = this.my;
            this.minecraft.mouseHandler.setIgnoreFirstMove();
            this.layerDelegate.onMouseMove(this.minecraft, gridRenderer, mousePosition, this.getMapFontScale(), this.isScrolling);
        }
        return super.m_7979_(mouseX, mouseY, button, mouseDX, mouseDY);
    }

    private void updateGrid() {
        java.awt.geom.Point2D.Double drag = this.getMouseDrag();
        this.updateGrid(-drag.getX(), -drag.getY());
    }

    private void updateGrid(double x, double z) {
        this.msx = this.mx;
        this.msy = this.my;
        try {
            gridRenderer.move(x, z);
            gridRenderer.updateTiles(state.getMapType(), state.getZoom(), state.isHighQuality(), this.minecraft.getWindow().getScreenWidth(), this.minecraft.getWindow().getScreenHeight(), false, 0.0, 0.0);
            gridRenderer.setZoom(this.fullMapProperties.zoomLevel.get());
        } catch (Exception var6) {
            this.logger.error("Error moving grid: " + var6);
        }
        this.setFollow(false);
        this.refreshState();
    }

    private java.awt.geom.Point2D.Double getMouseDrag() {
        int blockSize = (int) Math.pow(2.0, (double) this.fullMapProperties.zoomLevel.get().intValue());
        double mouseDragX = (double) (this.mx - this.msx) * this.getScaleFactor() / (double) blockSize;
        double mouseDragY = (double) (this.my - this.msy) * this.getScaleFactor() / (double) blockSize;
        return new java.awt.geom.Point2D.Double(mouseDragX, mouseDragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        try {
            java.awt.geom.Point2D.Double mousePosition = new java.awt.geom.Point2D.Double(this.minecraft.mouseHandler.xpos(), this.minecraft.mouseHandler.ypos());
            super.mouseReleased(mouseX, mouseY, button);
            if (this.sliderCaveLayer.isVisible() && this.sliderCaveLayer.dragging) {
                this.sliderCaveLayer.mouseReleased(mouseX, mouseY, button);
                this.isScrolling = false;
                return true;
            } else {
                if (this.isScrolling) {
                    this.isScrolling = false;
                    this.updateGrid();
                    FullscreenEventDispatcher.dragEventPost(this.getBlockAtMouse(), Minecraft.getInstance().level.m_46472_(), new java.awt.geom.Point2D.Double(mouseX, mouseY), button);
                }
                this.layerDelegate.onMouseMove(this.minecraft, gridRenderer, mousePosition, this.getMapFontScale(), this.isScrolling);
                return true;
            }
        } catch (Throwable var7) {
            Journeymap.getLogger().error(LogFormatter.toPartialString(var7));
            return false;
        }
    }

    @Override
    public void toggleMapType() {
        this.updateMapType(state.toggleMapType());
    }

    @Override
    public void updateMapType(Context.MapType mapType, Integer vSlice, ResourceKey<Level> dimension) {
        this.updateMapType(MapType.fromApiContextMapType(mapType, vSlice, dimension));
    }

    private void updateMapType(MapType newType) {
        if (!newType.isAllowed()) {
            newType = state.getMapType();
        }
        state.setMapType(newType);
        this.buttonDay.setToggled(Boolean.valueOf(newType.isDay()), false);
        this.buttonNight.setToggled(Boolean.valueOf(newType.isNight()), false);
        this.buttonBiome.setToggled(Boolean.valueOf(newType.isBiome()), false);
        this.buttonTopo.setToggled(Boolean.valueOf(newType.isTopo()), false);
        if (newType.isUnderground()) {
            this.sliderCaveLayer.setValue(newType.vSlice);
        }
        state.requireRefresh();
    }

    @Override
    public void zoomIn() {
        if (this.fullMapProperties.zoomLevel.get() < 5) {
            this.setZoom(this.fullMapProperties.zoomLevel.get() + 1);
        }
    }

    @Override
    public void zoomOut() {
        if (this.fullMapProperties.zoomLevel.get() > 0) {
            this.setZoom(this.fullMapProperties.zoomLevel.get() - 1);
        }
    }

    private void setZoom(int zoom) {
        if (state.setZoom(zoom)) {
            this.buttonZoomOut.setEnabled(this.fullMapProperties.zoomLevel.get() > 0);
            this.buttonZoomIn.setEnabled(this.fullMapProperties.zoomLevel.get() < 5);
            this.refreshState();
        }
    }

    public void toggleSearchBar(boolean toggled) {
        this.searchToolBar.setEnabled(toggled);
        this.searchToolBar.setVisible(toggled);
        this.buttonSearch.setToggled(Boolean.valueOf(toggled));
        this.searchTextZ.setVisible(toggled);
        this.searchTextX.setVisible(toggled);
        this.buttonExecuteSearch.setVisible(toggled);
    }

    void executeSearch() {
        this.buttonExecuteSearch.setToggled(Boolean.valueOf(true), false);
        try {
            int x = Integer.parseInt(this.searchTextX.getText().toLowerCase(Locale.ROOT).replace("x:", ""));
            int z = Integer.parseInt(this.searchTextZ.getText().toLowerCase(Locale.ROOT).replace("z:", ""));
            this.centerOn((double) x, (double) z);
        } catch (Exception var3) {
        }
    }

    void toggleFollow() {
        boolean isFollow = !state.follow.get();
        this.setFollow(isFollow);
        if (isFollow && this.minecraft.player != null) {
            this.sliderCaveLayer.setValue(this.minecraft.player.m_146904_() >> 4);
            if (state.getMapType().isUnderground()) {
                this.sliderCaveLayer.checkClickListeners();
            }
        }
    }

    void setFollow(Boolean follow) {
        state.follow.set(follow);
        if (follow) {
            state.resetMapType();
            this.refreshState();
        }
        ClientAPI.INSTANCE.flagOverlaysForRerender();
    }

    public BlockPos getBlockAtMouse() {
        java.awt.geom.Point2D.Double mousePosition = new java.awt.geom.Point2D.Double(this.minecraft.mouseHandler.xpos(), this.minecraft.mouseHandler.ypos());
        return this.layerDelegate.getBlockPos(this.minecraft, gridRenderer, mousePosition);
    }

    public void createWaypointAtMouse() {
        Waypoint waypoint = Waypoint.at(this.getBlockAtMouse(), Waypoint.Type.Normal, this.minecraft.player.m_9236_().dimension().location().toString());
        UIManager.INSTANCE.openWaypointEditor(waypoint, true, this, true);
    }

    public void chatPositionAtMouse() {
        Waypoint waypoint = Waypoint.at(this.getBlockAtMouse(), Waypoint.Type.Normal, state.getDimension().location().toString());
        this.chatOpenedFromEvent = true;
        this.openChat(waypoint.toChatString());
    }

    public boolean isChatOpen() {
        return this.chat != null && !this.chat.isHidden();
    }

    @Override
    public boolean keyPressed(int key, int value, int modifier) {
        switch(key) {
            case 256:
                if (this.isChatOpen()) {
                    this.chat.close();
                } else {
                    UIManager.INSTANCE.closeAll();
                }
                return true;
            default:
                if (this.minecraft.options.keyChat.matches(key, value) && !this.isChatOpen()) {
                    this.chatOpenedFromEvent = true;
                    this.openChat("");
                    return true;
                } else if (this.minecraft.options.keyCommand.matches(key, value) && !this.isChatOpen()) {
                    this.chatOpenedFromEvent = true;
                    this.openChat("/");
                    return true;
                } else {
                    if (this.isChatOpen()) {
                        this.chat.keyPressed(key, value, modifier);
                    }
                    return super.keyPressed(key, value, modifier);
                }
        }
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        if (this.isChatOpen() && this.chatOpenedFromEvent) {
            this.chatOpenedFromEvent = false;
            return false;
        } else if (this.searchTextX.m_198029_() && !this.isChatOpen()) {
            return this.searchTextX.m_5534_(typedChar, keyCode);
        } else {
            return this.searchTextZ.m_198029_() && !this.isChatOpen() ? this.searchTextZ.m_5534_(typedChar, keyCode) : this.chat.charTyped(typedChar, keyCode);
        }
    }

    public boolean isSearchFocused() {
        return this.searchTextX.m_198029_() || this.searchTextZ.m_198029_();
    }

    @Override
    public void tick() {
        super.m_86600_();
        if (this.chat != null) {
            this.chat.tick();
        }
    }

    @Override
    public void renderBackground(GuiGraphics graphics) {
        DrawUtil.drawRectangle(graphics.pose(), 0.0, 0.0, (double) this.f_96543_, (double) this.f_96544_, this.bgColor, 1.0F);
    }

    void drawMap(GuiGraphics graphics, int mouseX, int mouseY) {
        boolean refreshReady = this.isRefreshReady();
        StatTimer timer = refreshReady ? this.drawMapTimerWithRefresh : this.drawMapTimer;
        MapType mapType = state.getMapType();
        timer.start();
        try {
            this.sizeDisplay(graphics.pose(), false);
            double xOffset = 0.0;
            double yOffset = 0.0;
            if (this.isScrolling) {
                java.awt.geom.Point2D.Double drag = this.getMouseDrag();
                int blockSize = (int) Math.pow(2.0, (double) this.fullMapProperties.zoomLevel.get().intValue());
                double mouseDragX = drag.getX();
                double mouseDragY = drag.getY();
                xOffset = mouseDragX * (double) blockSize;
                yOffset = mouseDragY * (double) blockSize;
            } else if (refreshReady) {
                this.refreshState();
            } else {
                gridRenderer.setContext(state.getWorldDir(), mapType);
            }
            gridRenderer.clearGlErrors(false);
            gridRenderer.updateRotation(graphics.pose(), 0.0);
            if (state.follow.get()) {
                gridRenderer.center(state.getWorldDir(), mapType, this.minecraft.player.m_20185_(), this.minecraft.player.m_20189_(), this.fullMapProperties.zoomLevel.get());
                ClientAPI.INSTANCE.flagOverlaysForRerender();
            }
            gridRenderer.updateTiles(mapType, state.getZoom(), state.isHighQuality(), this.minecraft.getWindow().getScreenWidth(), this.minecraft.getWindow().getScreenHeight(), false, 0.0, 0.0);
            gridRenderer.draw(graphics, 1.0F, 0.8F, xOffset, yOffset, this.fullMapProperties.showGrid.get());
            gridRenderer.draw(graphics, state.getDrawSteps(), this, (int) ((double) mouseX + xOffset), (int) ((double) mouseY + yOffset), xOffset, yOffset, (double) this.getMapFontScale(), 0.0);
            gridRenderer.draw(graphics, state.getDrawWaypointSteps(), xOffset, yOffset, (double) this.getMapFontScale(), 0.0);
            if (this.fullMapProperties.showSelf.get()) {
                Point2D playerPixel = gridRenderer.getPixel(this.minecraft.player.m_20185_(), this.minecraft.player.m_20189_());
                if (playerPixel != null) {
                    float scale = this.fullMapProperties.selfDisplayScale.get();
                    Texture bgTex = TextureCache.getTexture(TextureCache.PlayerArrowBG);
                    Texture fgTex = TextureCache.getTexture(TextureCache.PlayerArrow);
                    DrawUtil.drawColoredEntity(graphics.pose(), playerPixel.getX() + xOffset, playerPixel.getY() + yOffset, bgTex, 16777215, 1.0F, scale, (double) this.minecraft.player.m_146908_());
                    int playerColor = this.coreProperties.getColor(this.coreProperties.colorSelf);
                    DrawUtil.drawColoredEntity(graphics.pose(), playerPixel.getX() + xOffset, playerPixel.getY() + yOffset, fgTex, playerColor, 1.0F, scale, (double) this.minecraft.player.m_146908_());
                }
            }
            gridRenderer.draw(graphics, this.layerDelegate.getDrawSteps(), xOffset, yOffset, (double) this.getMapFontScale(), 0.0);
            this.sizeDisplay(graphics.pose(), true);
        } finally {
            timer.stop();
            gridRenderer.clearGlErrors(true);
        }
    }

    private float getMapFontScale() {
        return this.fullMapProperties.fontScale.get();
    }

    public void centerOn(Waypoint waypoint) {
        if (waypoint.getDimensions().contains(this.minecraft.player.m_9236_().dimension().location().toString())) {
            if (!waypoint.isPersistent()) {
                this.addTempMarker(waypoint);
            }
            this.centerOn((double) waypoint.getX(), (double) waypoint.getZ());
        }
    }

    @Override
    public void centerOn(double x, double z) {
        state.follow.set(false);
        state.requireRefresh();
        gridRenderer.center(state.getWorldDir(), state.getMapType(), x, z, this.fullMapProperties.zoomLevel.get());
        this.refreshState();
        this.tick();
        ClientAPI.INSTANCE.flagOverlaysForRerender();
    }

    public void addTempMarker(Waypoint waypoint) {
        try {
            BlockPos pos = waypoint.getBlockPos();
            PolygonOverlay polygonOverlay = new PolygonOverlay("journeymap", waypoint.getName(), this.minecraft.player.m_9236_().dimension(), new ShapeProperties().setStrokeColor(255).setStrokeOpacity(1.0F).setStrokeWidth(1.5F), new MapPolygon(pos.offset(-1, 0, 2), pos.offset(2, 0, 2), pos.offset(2, 0, -1), pos.offset(-1, 0, -1)));
            polygonOverlay.setActiveMapTypes(EnumSet.allOf(Context.MapType.class));
            polygonOverlay.setActiveUIs(EnumSet.of(Context.UI.Fullscreen));
            polygonOverlay.setLabel(waypoint.getName());
            this.tempOverlays.add(polygonOverlay);
            ClientAPI.INSTANCE.show(polygonOverlay);
        } catch (Throwable var4) {
            Journeymap.getLogger().error("Error showing temp location marker: " + LogFormatter.toPartialString(var4));
        }
    }

    void refreshState() {
        Player player = this.minecraft.player;
        if (player == null) {
            this.logger.warn("Could not get player");
        } else {
            StatTimer timer = StatTimer.get("Fullscreen.refreshState");
            timer.start();
            try {
                this.menuToolbarBounds = null;
                this.optionsToolbarBounds = null;
                this.fullMapProperties = JourneymapClient.getInstance().getFullMapProperties();
                state.refresh(this.minecraft, player, this.fullMapProperties);
                MapType mapType = state.getMapType();
                gridRenderer.setContext(state.getWorldDir(), mapType);
                if (state.follow.get()) {
                    gridRenderer.center(state.getWorldDir(), mapType, this.minecraft.player.m_20185_(), this.minecraft.player.m_20189_(), this.fullMapProperties.zoomLevel.get());
                } else {
                    gridRenderer.setZoom(this.fullMapProperties.zoomLevel.get());
                }
                gridRenderer.updateTiles(mapType, state.getZoom(), state.isHighQuality(), this.minecraft.getWindow().getScreenWidth(), this.minecraft.getWindow().getScreenHeight(), true, 0.0, 0.0);
                gridRenderer.updateUIState(true);
                state.generateDrawSteps(this.minecraft, gridRenderer, this.waypointRenderer, this.radarRenderer, this.fullMapProperties, false);
                LocationFormat.LocationFormatKeys locationFormatKeys = this.locationFormat.getFormatKeys(this.fullMapProperties.locationFormat.get());
                state.playerLastPos = locationFormatKeys.format(this.fullMapProperties.locationFormatVerbose.get(), Mth.floor(this.minecraft.player.m_20185_()), Mth.floor(this.minecraft.player.m_20189_()), Mth.floor(this.minecraft.player.m_20191_().minY), this.minecraft.player.m_146904_() >> 4) + " " + state.getPlayerBiome();
                state.updateLastRefresh();
            } finally {
                timer.stop();
            }
        }
    }

    public void openChat(String defaultText) {
        if (this.chat != null) {
            this.chat.setText(defaultText);
            this.chat.setHidden(false);
        } else {
            this.chat = new MapChat(defaultText, false);
            this.chat.m_6574_(this.minecraft, this.f_96543_, this.f_96544_);
        }
    }

    @Override
    public void close() {
        for (Overlay temp : this.tempOverlays) {
            ClientAPI.INSTANCE.remove(temp);
        }
        gridRenderer.updateUIState(false);
        if (this.chat != null) {
            this.chat.close();
        }
    }

    @Override
    public void removed() {
    }

    boolean isRefreshReady() {
        return this.isScrolling ? false : state.shouldRefresh(super.getMinecraft(), this.fullMapProperties) || gridRenderer.hasUnloadedTile();
    }

    public void moveCanvas(int deltaBlockX, int deltaBlockz) {
        this.refreshState();
        gridRenderer.move((double) deltaBlockX, (double) deltaBlockz);
        gridRenderer.updateTiles(state.getMapType(), state.getZoom(), state.isHighQuality(), this.minecraft.getWindow().getScreenWidth(), this.minecraft.getWindow().getScreenHeight(), true, 0.0, 0.0);
        ClientAPI.INSTANCE.flagOverlaysForRerender();
        this.setFollow(false);
    }

    public void showCaveLayers() {
        if (!state.isUnderground()) {
            this.updateMapType(MapType.underground(3, state.getDimension()));
        }
    }

    @Override
    protected void drawLogo(PoseStack poseStack) {
        if (!this.logo.hasImage()) {
            this.logo = TextureCache.getTexture(TextureCache.Logo);
        }
        DrawUtil.sizeDisplay(poseStack, (double) this.minecraft.getWindow().getScreenWidth(), (double) this.minecraft.getWindow().getScreenHeight());
        Theme.Container.Toolbar toolbar = ThemeLoader.getCurrentTheme().container.toolbar;
        double scale = this.getScaleFactor() * 2.0;
        DrawUtil.sizeDisplay(poseStack, (double) this.f_96543_, (double) this.f_96544_);
        DrawUtil.drawImage(poseStack, this.logo, (double) toolbar.horizontal.margin, (double) toolbar.vertical.margin, false, (float) (1.0 / scale), 0.0);
    }

    @Override
    public final boolean isPauseScreen() {
        return false;
    }

    public void setTheme(String name) {
        try {
            MiniMapProperties mmp = JourneymapClient.getInstance().getMiniMapProperties(JourneymapClient.getInstance().getActiveMinimapId());
            mmp.shape.set(Shape.Rectangle);
            mmp.sizePercent.set(Integer.valueOf(20));
            mmp.save();
            Theme theme = ThemeLoader.getThemeByName(name);
            ThemeLoader.setCurrentTheme(theme);
            UIManager.INSTANCE.getMiniMap().reset();
            ChatLog.announceI18N("jm.common.ui_theme_applied");
            UIManager.INSTANCE.closeAll();
        } catch (Exception var4) {
            Journeymap.getLogger().error("Could not load Theme: " + LogFormatter.toString(var4));
        }
    }

    public void addButtonWidget(journeymap.client.ui.component.Button button) {
        this.m_142416_(button);
    }

    public MapType getMapType() {
        return gridRenderer.getMapType();
    }

    public void hideButtons() {
        if (!this.buttonsVisible) {
            this.buttonsVisible = true;
        } else {
            this.buttonsVisible = false;
            this.m_169413_();
        }
    }
}