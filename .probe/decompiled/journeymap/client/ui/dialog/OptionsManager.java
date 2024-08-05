package journeymap.client.ui.dialog;

import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import journeymap.client.Constants;
import journeymap.client.JourneymapClient;
import journeymap.client.cartography.color.ColorManager;
import journeymap.client.data.DataCache;
import journeymap.client.io.ThemeLoader;
import journeymap.client.log.ChatLog;
import journeymap.client.log.JMLogger;
import journeymap.client.mod.ModBlockDelegate;
import journeymap.client.model.BlockMD;
import journeymap.client.model.MapType;
import journeymap.client.properties.ClientCategory;
import journeymap.client.properties.CoreProperties;
import journeymap.client.properties.MiniMapProperties;
import journeymap.client.render.draw.DrawUtil;
import journeymap.client.render.map.TileDrawStepCache;
import journeymap.client.service.webmap.Webmap;
import journeymap.client.task.main.SoftResetTask;
import journeymap.client.task.multi.MapPlayerTask;
import journeymap.client.task.multi.RenderSpec;
import journeymap.client.ui.UIManager;
import journeymap.client.ui.component.Button;
import journeymap.client.ui.component.ButtonList;
import journeymap.client.ui.component.CheckBox;
import journeymap.client.ui.component.IConfigFieldHolder;
import journeymap.client.ui.component.IntSliderButton;
import journeymap.client.ui.component.OptionsScrollListPane;
import journeymap.client.ui.component.ResetButton;
import journeymap.client.ui.component.Slot;
import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.client.ui.minimap.MiniMap;
import journeymap.client.ui.option.CategorySlot;
import journeymap.client.ui.option.OptionScreen;
import journeymap.client.ui.option.OptionSlotFactory;
import journeymap.client.ui.option.SlotMetadata;
import journeymap.client.waypoint.WaypointStore;
import journeymap.common.Journeymap;
import journeymap.common.properties.PropertiesBase;
import journeymap.common.properties.catagory.Category;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.FormattedCharSequence;

public class OptionsManager extends OptionScreen {

    protected static Set<Category> openCategories = new HashSet();

    protected final int inGameMinimapId;

    protected Category[] initialCategories;

    protected CheckBox minimap1PreviewButton;

    protected CheckBox minimap2PreviewButton;

    protected Button renderStatsButton;

    protected Button editGridMinimap1Button;

    protected Button editGridMinimap2Button;

    protected Button editMinimap1LocationsButton;

    protected Button editMinimap2LocationsButton;

    protected Button editGridFullscreenButton;

    protected SlotMetadata renderStatsSlotMetadata;

    protected CategorySlot cartographyCategorySlot;

    protected OptionsScrollListPane<CategorySlot> optionsListPane;

    protected Map<Category, List<SlotMetadata>> toolbars;

    protected Set<Category> changedCategories = new HashSet();

    protected boolean forceMinimapUpdate;

    protected ButtonList editGridButtons = new ButtonList();

    private MiniMapProperties currentMiniMapProp;

    private MiniMap currentMiniMap;

    public OptionsManager() {
        this(null);
    }

    public OptionsManager(Screen returnDisplay) {
        this(returnDisplay, (Category[]) openCategories.toArray(new Category[0]));
    }

    public OptionsManager(Screen returnDisplay, Category... initialCategories) {
        super(String.format("JourneyMap %s %s", Journeymap.JM_VERSION, Constants.getString("jm.common.options")), returnDisplay);
        this.initialCategories = initialCategories;
        this.inGameMinimapId = JourneymapClient.getInstance().getActiveMinimapId();
    }

    public OptionsManager(String title, Screen returnDisplay) {
        super(title, returnDisplay);
        this.inGameMinimapId = JourneymapClient.getInstance().getActiveMinimapId();
    }

    protected Map<Category, PropertiesBase> getSlots() {
        Map<Category, PropertiesBase> slotMap = Maps.newHashMap();
        slotMap.put(ClientCategory.MiniMap1, JourneymapClient.getInstance().getMiniMapProperties1());
        slotMap.put(ClientCategory.MiniMap2, JourneymapClient.getInstance().getMiniMapProperties2());
        slotMap.put(ClientCategory.FullMap, JourneymapClient.getInstance().getFullMapProperties());
        slotMap.put(ClientCategory.WebMap, JourneymapClient.getInstance().getWebMapProperties());
        slotMap.put(ClientCategory.Waypoint, JourneymapClient.getInstance().getWaypointProperties());
        slotMap.put(ClientCategory.Advanced, JourneymapClient.getInstance().getCoreProperties());
        return slotMap;
    }

    @Override
    public void init() {
        try {
            super.init();
            this.clientOptions.setEnabled(false);
            if (this.editGridMinimap1Button == null) {
                String name = Constants.getString("jm.common.grid_edit");
                String tooltip = Constants.getString("jm.common.grid_edit.tooltip");
                this.editGridMinimap1Button = new Button(name);
                this.editGridMinimap1Button.setTooltip(tooltip);
                this.editGridMinimap1Button.setDrawBackground(false);
                this.editGridMinimap2Button = new Button(name);
                this.editGridMinimap2Button.setTooltip(tooltip);
                this.editGridMinimap2Button.setDrawBackground(false);
                this.editGridFullscreenButton = new Button(name);
                this.editGridFullscreenButton.setTooltip(tooltip);
                this.editGridFullscreenButton.setDrawBackground(false);
                this.editGridButtons = new ButtonList(this.editGridMinimap1Button, this.editGridMinimap2Button, this.editGridFullscreenButton);
            }
            if (this.editMinimap1LocationsButton == null) {
                String name = Constants.getString("jm.common.minimap_position.button");
                String tooltip = Constants.getString("jm.common.minimap_position.button.tooltip");
                this.editMinimap1LocationsButton = new Button(name);
                this.editMinimap1LocationsButton.setTooltip(tooltip);
                this.editMinimap1LocationsButton.setDrawBackground(false);
                this.editMinimap2LocationsButton = new Button(name);
                this.editMinimap2LocationsButton.setTooltip(tooltip);
                this.editMinimap2LocationsButton.setDrawBackground(false);
            }
            if (this.minimap1PreviewButton == null) {
                String name = String.format("%s %s", Constants.getString("jm.minimap.preview"), "1");
                String tooltip = Constants.getString("jm.minimap.preview.tooltip", JourneymapClient.getInstance().getKeyEvents().getHandler().kbMinimapPreset.m_90863_());
                this.minimap1PreviewButton = new CheckBox(name, false);
                this.minimap1PreviewButton.setTooltip(new String[] { tooltip });
                if (Minecraft.getInstance().level == null) {
                    this.minimap1PreviewButton.setEnabled(false);
                }
            }
            if (this.minimap2PreviewButton == null) {
                String name = String.format("%s %s", Constants.getString("jm.minimap.preview"), "2");
                String tooltip = Constants.getString("jm.minimap.preview.tooltip", JourneymapClient.getInstance().getKeyEvents().getHandler().kbMinimapPreset.m_90863_());
                this.minimap2PreviewButton = new CheckBox(name, false);
                this.minimap2PreviewButton.setTooltip(new String[] { tooltip });
                if (Minecraft.getInstance().level == null) {
                    this.minimap2PreviewButton.setEnabled(false);
                }
            }
            if (this.renderStatsButton == null) {
                this.renderStatsButton = new OptionsManager.LabelButton(150, "jm.common.renderstats", 0, 0, 0);
                this.renderStatsButton.setEnabled(false);
            }
            if (this.optionsListPane == null) {
                List<Slot> categorySlots = new ArrayList();
                this.optionsListPane = new OptionsScrollListPane<>(this, this.f_96541_, this.f_96543_, this.f_96544_, 70, this.f_96544_ - 35, 20);
                this.optionsListPane.setAlignTop(true);
                this.optionsListPane.m_93496_(false);
                this.optionsListPane.setSlots(OptionSlotFactory.getOptionSlots(this.getToolbars(), this.getSlots()));
                if (this.initialCategories != null) {
                    for (Category initialCategory : this.initialCategories) {
                        for (CategorySlot categorySlot : this.optionsListPane.getRootSlots()) {
                            if (categorySlot.getCategory() == initialCategory) {
                                categorySlot.setSelected(true);
                                categorySlots.add(categorySlot);
                            }
                        }
                    }
                }
                for (Slot rootSlot : this.optionsListPane.getRootSlots()) {
                    if (rootSlot instanceof CategorySlot) {
                        CategorySlot categorySlotx = (CategorySlot) rootSlot;
                        Category category = categorySlotx.getCategory();
                        if (category == ClientCategory.MiniMap1) {
                            if (Minecraft.getInstance().level != null) {
                                categorySlotx.getAllChildMetadata().add(new SlotMetadata(this.minimap1PreviewButton, 4));
                            }
                            categorySlotx.getAllChildMetadata().add(new SlotMetadata(this.editGridMinimap1Button, 3));
                            categorySlotx.getAllChildMetadata().add(new SlotMetadata(this.editMinimap1LocationsButton, 2));
                        } else if (category == ClientCategory.MiniMap2) {
                            if (Minecraft.getInstance().level != null) {
                                categorySlotx.getAllChildMetadata().add(new SlotMetadata(this.minimap2PreviewButton, 4));
                            }
                            categorySlotx.getAllChildMetadata().add(new SlotMetadata(this.editGridMinimap2Button, 3));
                            categorySlotx.getAllChildMetadata().add(new SlotMetadata(this.editMinimap2LocationsButton, 2));
                        } else if (category == ClientCategory.FullMap) {
                            categorySlotx.getAllChildMetadata().add(new SlotMetadata(this.editGridMinimap2Button, 3));
                        } else if (category == ClientCategory.Cartography) {
                            this.cartographyCategorySlot = categorySlotx;
                            this.renderStatsSlotMetadata = new SlotMetadata(this.renderStatsButton, Constants.getString("jm.common.renderstats.title"), Constants.getString("jm.common.renderstats.tooltip"), 2);
                        }
                    }
                }
                this.optionsListPane.updateSlots();
                if (!categorySlots.isEmpty()) {
                    this.optionsListPane.scrollTo((Slot) categorySlots.get(0));
                }
            } else {
                this.optionsListPane.m_93437_(this.f_96543_, this.f_96544_, 70, this.f_96544_ - 35);
                this.optionsListPane.updateSlots();
            }
        } catch (Throwable var8) {
            JMLogger.throwLogOnce("Error in OptionsManager.init(): " + var8, var8);
        }
    }

    @Override
    protected void layoutButtons(GuiGraphics graphics) {
        if (this.getRenderables().isEmpty()) {
            this.init();
        }
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, float par3) {
        try {
            if (this.forceMinimapUpdate) {
                if (this.minimap1PreviewButton.m_142518_()) {
                    UIManager.INSTANCE.switchMiniMapPreset(1);
                } else if (this.minimap2PreviewButton.m_142518_()) {
                    UIManager.INSTANCE.switchMiniMapPreset(2);
                }
            }
            if (this.f_96541_.level != null) {
                this.updateRenderStats();
            }
            List<FormattedCharSequence> lastTooltip = this.optionsListPane.lastTooltip;
            long lastTooltipTime = this.optionsListPane.lastTooltipTime;
            this.optionsListPane.lastTooltip = null;
            this.optionsListPane.m_88315_(graphics, x, y, par3);
            super.m_88315_(graphics, x, y, par3);
            if (this.previewMiniMap()) {
                UIManager.INSTANCE.getMiniMap().drawMap(graphics, true);
                UIManager.INSTANCE.getMiniMap().updateDisplayVars(true);
            }
            if (this.optionsListPane.lastTooltip != null && !this.optionsListPane.lastTooltip.equals(lastTooltip)) {
                this.optionsListPane.lastTooltipTime = lastTooltipTime;
                if (System.currentTimeMillis() - this.optionsListPane.lastTooltipTime > this.optionsListPane.hoverDelay) {
                    Button button = this.optionsListPane.lastTooltipMetadata.getButton();
                    graphics.renderTooltip(this.getFontRenderer(), this.optionsListPane.lastTooltip, x, button.getBottomY() + 15);
                }
            }
        } catch (Throwable var9) {
            JMLogger.throwLogOnce("Error in OptionsManager.render(): " + var9, var9);
        }
    }

    private void updateRenderStats() {
        RenderSpec.getSurfaceSpec();
        RenderSpec.getTopoSpec();
        RenderSpec.getUndergroundSpec();
        for (Slot rootSlot : this.optionsListPane.getRootSlots()) {
            if (rootSlot instanceof CategorySlot categorySlot && categorySlot.getCategory() == ClientCategory.Cartography) {
                CoreProperties coreProperties = JourneymapClient.getInstance().getCoreProperties();
                for (SlotMetadata slotMetadata : categorySlot.getAllChildMetadata()) {
                    if (slotMetadata.getButton() instanceof IConfigFieldHolder) {
                        Object property = ((IConfigFieldHolder) slotMetadata.getButton()).getConfigField();
                        boolean limitButtonRange = false;
                        if (property == coreProperties.renderDistanceCaveMax) {
                            limitButtonRange = true;
                            slotMetadata.getButton().resetLabelColors();
                        } else if (property == coreProperties.renderDistanceSurfaceMax) {
                            limitButtonRange = true;
                            slotMetadata.getButton().resetLabelColors();
                        }
                        if (limitButtonRange) {
                            IntSliderButton button = (IntSliderButton) slotMetadata.getButton();
                            int renderDistance = JourneymapClient.getInstance().getRenderDistance();
                            button.maxValue = renderDistance;
                            if (button.getValue() > renderDistance) {
                                button.setValue(renderDistance);
                            }
                        }
                    }
                }
            }
        }
        String messageString = JourneymapClient.getInstance().getCoreProperties().mappingEnabled.get() ? MapPlayerTask.getSimpleStats() : Constants.getString("jm.common.enable_mapping_false_text");
        this.renderStatsButton.m_93666_(Constants.getStringTextComponent(messageString));
        if (this.cartographyCategorySlot != null) {
            this.renderStatsButton.setWidth(this.cartographyCategorySlot.getCurrentColumnWidth());
        }
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics) {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (this.previewMiniMap() && UIManager.INSTANCE.getMiniMap().withinBounds(mouseX, mouseY)) {
            return false;
        } else {
            try {
                boolean pressed = this.optionsListPane.m_6375_(mouseX, mouseY, mouseButton);
                if (pressed) {
                    this.checkPressedButton();
                }
                return super.m_6375_(mouseX, mouseY, mouseButton);
            } catch (Throwable var7) {
                Journeymap.getLogger().error(var7.getMessage(), var7);
                return false;
            }
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        this.optionsListPane.m_6348_(mouseX, mouseY, mouseButton);
        return super.m_6348_(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double mouseDX, double mouseDY) {
        if (this.optionsListPane.m_7979_(mouseX, mouseY, button, mouseDX, mouseDY)) {
            this.checkPressedButton();
        }
        return super.m_7979_(mouseX, mouseY, button, mouseDX, mouseDY);
    }

    @Override
    public boolean mouseScrolled(double x, double y, double scroll) {
        this.optionsListPane.m_6050_(x, y, scroll);
        return super.m_6050_(x, y, scroll);
    }

    protected void checkPressedButton() {
        SlotMetadata slotMetadata = this.optionsListPane.getLastPressed();
        if (slotMetadata != null) {
            if (slotMetadata.getButton() instanceof ResetButton) {
                this.resetOptions(((ResetButton) slotMetadata.getButton()).category);
            }
            if (slotMetadata.getName().equals(Constants.getString("jm.common.ui_theme"))) {
                ThemeLoader.getCurrentTheme(true);
                if (this.previewMiniMap()) {
                    UIManager.INSTANCE.getMiniMap().updateDisplayVars(true);
                }
            }
            if (this.editGridButtons.contains(slotMetadata.getButton())) {
                UIManager.INSTANCE.openGridEditor(this);
                return;
            }
            if (slotMetadata.getButton() == this.editMinimap1LocationsButton) {
                UIManager.INSTANCE.openMinimapPosition(this, JourneymapClient.getInstance().getMiniMapProperties(1));
                return;
            }
            if (slotMetadata.getButton() == this.editMinimap2LocationsButton) {
                UIManager.INSTANCE.openMinimapPosition(this, JourneymapClient.getInstance().getMiniMapProperties(2));
                return;
            }
            if (slotMetadata.getButton() == this.minimap1PreviewButton) {
                this.minimap2PreviewButton.setToggled(Boolean.valueOf(false));
                UIManager.INSTANCE.switchMiniMapPreset(1);
                UIManager.INSTANCE.getMiniMap().resetInitTime();
                this.currentMiniMap = UIManager.INSTANCE.getMiniMap();
                this.currentMiniMapProp = this.currentMiniMap.getCurrentMinimapProperties();
            }
            if (slotMetadata.getButton() == this.minimap2PreviewButton) {
                this.minimap1PreviewButton.setToggled(Boolean.valueOf(false));
                UIManager.INSTANCE.switchMiniMapPreset(2);
                UIManager.INSTANCE.getMiniMap().resetInitTime();
                this.currentMiniMap = UIManager.INSTANCE.getMiniMap();
                this.currentMiniMapProp = this.currentMiniMap.getCurrentMinimapProperties();
            }
        }
        CategorySlot categorySlot = (CategorySlot) this.optionsListPane.getLastPressedParentSlot();
        if (categorySlot != null) {
            Category category = categorySlot.getCategory();
            this.changedCategories.add(category);
            if (category == ClientCategory.MiniMap1 || category == ClientCategory.MiniMap2) {
                this.refreshMinimapOptions();
                DataCache.INSTANCE.resetRadarCaches();
                UIManager.INSTANCE.getMiniMap().updateDisplayVars(true);
            }
            if (category == ClientCategory.Cartography) {
                JourneymapClient.getInstance().getCoreProperties().save();
                RenderSpec.resetRenderSpecs();
            }
        }
    }

    protected void actionPerformed(Button button) {
        if (button == this.minimap1PreviewButton) {
            this.minimap2PreviewButton.setToggled(Boolean.valueOf(false));
            UIManager.INSTANCE.switchMiniMapPreset(1);
        }
        if (button == this.minimap2PreviewButton) {
            this.minimap1PreviewButton.setToggled(Boolean.valueOf(false));
            UIManager.INSTANCE.switchMiniMapPreset(2);
        }
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        switch(keyCode) {
            case 256:
                if (this.previewMiniMap()) {
                    this.minimap1PreviewButton.setToggled(Boolean.valueOf(false));
                    this.minimap2PreviewButton.setToggled(Boolean.valueOf(false));
                } else {
                    this.closeAndReturn();
                }
            default:
                boolean optionUpdated = this.optionsListPane.m_5534_(typedChar, keyCode);
                if (optionUpdated && this.previewMiniMap()) {
                    UIManager.INSTANCE.getMiniMap().updateDisplayVars(true);
                }
                return true;
        }
    }

    @Override
    public boolean keyPressed(int key, int value, int modifier) {
        switch(key) {
            case 256:
                if (this.previewMiniMap()) {
                    this.minimap1PreviewButton.setToggled(Boolean.valueOf(false));
                    this.minimap2PreviewButton.setToggled(Boolean.valueOf(false));
                } else {
                    this.closeAndReturn();
                }
            default:
                boolean optionUpdated = this.optionsListPane.m_7933_(key, value, modifier);
                if (optionUpdated && this.previewMiniMap()) {
                    UIManager.INSTANCE.getMiniMap().updateDisplayVars(true);
                }
                return optionUpdated;
        }
    }

    protected void resetOptions(Category category) {
        Set<PropertiesBase> updatedProperties = new HashSet();
        for (CategorySlot categorySlot : this.optionsListPane.getRootSlots()) {
            if (category.equals(categorySlot.getCategory())) {
                for (SlotMetadata slotMetadata : categorySlot.getAllChildMetadata()) {
                    slotMetadata.resetToDefaultValue();
                    if (ClientCategory.MiniMap1.equals(category) || ClientCategory.MiniMap2.equals(category)) {
                        UIManager.INSTANCE.getMiniMap().resetState();
                    }
                    if (ClientCategory.FullMap.equals(category)) {
                        Fullscreen.state().setMapType(MapType.Name.day);
                    }
                    if (slotMetadata.hasConfigField()) {
                        PropertiesBase properties = slotMetadata.getProperties();
                        if (properties != null) {
                            updatedProperties.add(properties);
                        }
                    }
                }
                break;
            }
        }
        for (PropertiesBase properties : updatedProperties) {
            properties.save();
        }
        RenderSpec.resetRenderSpecs();
    }

    public boolean previewMiniMap() {
        return this.minimap1PreviewButton.getToggled() || this.minimap2PreviewButton.getToggled();
    }

    public void refreshMinimapOptions() {
        Set<Category> cats = new HashSet();
        cats.add(ClientCategory.MiniMap1);
        cats.add(ClientCategory.MiniMap2);
        for (CategorySlot categorySlot : this.optionsListPane.getRootSlots()) {
            if (cats.contains(categorySlot.getCategory())) {
                for (SlotMetadata slotMetadata : categorySlot.getAllChildMetadata()) {
                    slotMetadata.getButton().refresh();
                }
            }
        }
    }

    @Override
    protected void closeAndReturn() {
        JourneymapClient.getInstance().getCoreProperties().optionsManagerViewed.set(Journeymap.JM_VERSION.toString()).save();
        JourneymapClient.getInstance().saveConfigProperties();
        if (this.f_96541_.level != null) {
            UIManager.INSTANCE.getMiniMap().setMiniMapProperties(JourneymapClient.getInstance().getMiniMapProperties(this.inGameMinimapId));
            for (Category category : this.changedCategories) {
                if (category == ClientCategory.MiniMap1) {
                    DataCache.INSTANCE.resetRadarCaches();
                    UIManager.INSTANCE.getMiniMap().reset();
                } else if (category == ClientCategory.MiniMap2) {
                    DataCache.INSTANCE.resetRadarCaches();
                } else if (category == ClientCategory.FullMap) {
                    DataCache.INSTANCE.resetRadarCaches();
                    ThemeLoader.getCurrentTheme(true);
                } else if (category == ClientCategory.WebMap) {
                    DataCache.INSTANCE.resetRadarCaches();
                    if (JourneymapClient.getInstance().getWebMapProperties().enabled.get()) {
                        Webmap.INSTANCE.start();
                    } else {
                        Webmap.INSTANCE.stop();
                    }
                    ChatLog.announceMod(true);
                } else if (category == ClientCategory.Waypoint) {
                    WaypointStore.INSTANCE.reset();
                } else if (category != ClientCategory.WaypointBeacon) {
                    if (category == ClientCategory.Cartography) {
                        ColorManager.INSTANCE.reset();
                        ModBlockDelegate.INSTANCE.reset();
                        BlockMD.reset();
                        RenderSpec.resetRenderSpecs();
                        TileDrawStepCache.instance().invalidateAll();
                        MiniMap.state().requireRefresh();
                        Fullscreen.state().requireRefresh();
                        MapPlayerTask.forceNearbyRemap();
                    } else if (category == ClientCategory.Advanced) {
                        SoftResetTask.queue();
                        ChatLog.announceMod(false);
                    }
                }
            }
            UIManager.INSTANCE.getMiniMap().reset();
            UIManager.INSTANCE.getMiniMap().updateDisplayVars(true);
        }
        if (returnDisplayStack != null && !returnDisplayStack.isEmpty() && returnDisplayStack.peek() instanceof Fullscreen) {
            ((Fullscreen) returnDisplayStack.peek()).reset();
        }
        openCategories.clear();
        for (CategorySlot categorySlot : this.optionsListPane.getRootSlots()) {
            if (categorySlot.isSelected()) {
                openCategories.add(categorySlot.getCategory());
            }
        }
        super.closeAndReturn();
    }

    protected Map<Category, List<SlotMetadata>> getToolbars() {
        if (this.toolbars == null) {
            this.toolbars = new HashMap();
            for (Category category : ClientCategory.values) {
                String name = Constants.getString("jm.config.reset");
                String tooltip = Constants.getString("jm.config.reset.tooltip");
                SlotMetadata toolbarSlotMetadata = new SlotMetadata(new ResetButton(category), name, tooltip);
                this.toolbars.put(category, Arrays.asList(toolbarSlotMetadata));
            }
        }
        return this.toolbars;
    }

    public static class LabelButton extends Button {

        DrawUtil.HAlign hAlign = DrawUtil.HAlign.Left;

        public LabelButton(int width, String key, Object... labelArgs) {
            super(Constants.getString(key, labelArgs));
            this.setTooltip(new String[] { Constants.getString(key + ".tooltip") });
            this.setDrawBackground(false);
            this.setDrawFrame(false);
            this.setEnabled(false);
            this.setLabelColors(Integer.valueOf(12632256), Integer.valueOf(12632256), Integer.valueOf(12632256));
            this.m_93674_(width);
        }

        @Override
        public int getFitWidth(Font fr) {
            return this.f_93618_;
        }

        @Override
        public void fitWidth(Font fr) {
        }

        public void setHAlign(DrawUtil.HAlign hAlign) {
            this.hAlign = hAlign;
        }

        @Override
        public void render(GuiGraphics graphics, int mouseX, int mouseY, float ticks) {
            DrawUtil.drawLabel(graphics, this.m_6035_().getString(), (double) (switch(this.hAlign) {
                case Left ->
                    this.getRightX();
                case Right ->
                    this.m_252754_();
                default ->
                    this.getCenterX();
            }), (double) this.getMiddleY(), this.hAlign, DrawUtil.VAlign.Middle, null, 0.0F, this.labelColor, 1.0F, 1.0, this.drawLabelShadow);
        }
    }
}