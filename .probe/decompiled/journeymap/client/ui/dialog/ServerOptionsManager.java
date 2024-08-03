package journeymap.client.ui.dialog;

import com.google.common.collect.Maps;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import journeymap.client.Constants;
import journeymap.client.JourneymapClient;
import journeymap.client.data.WorldData;
import journeymap.client.log.JMLogger;
import journeymap.client.ui.component.Button;
import journeymap.client.ui.component.JmUI;
import journeymap.client.ui.component.OptionsScrollListPane;
import journeymap.client.ui.component.ResetButton;
import journeymap.client.ui.option.CategorySlot;
import journeymap.client.ui.option.OptionScreen;
import journeymap.client.ui.option.OptionSlotFactory;
import journeymap.client.ui.option.SlotMetadata;
import journeymap.common.Journeymap;
import journeymap.common.helper.DimensionHelper;
import journeymap.common.network.data.ServerPropertyType;
import journeymap.common.properties.DefaultDimensionProperties;
import journeymap.common.properties.DimensionProperties;
import journeymap.common.properties.GlobalProperties;
import journeymap.common.properties.PropertiesBase;
import journeymap.common.properties.ServerCategory;
import journeymap.common.properties.catagory.Category;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.Level;

public class ServerOptionsManager extends OptionScreen {

    protected OptionsScrollListPane<CategorySlot> optionsListPane;

    protected final Map<Category, PropertiesBase> slotMap = Maps.newHashMap();

    private GlobalProperties globalProperties;

    private DefaultDimensionProperties defaultDimensionProperties;

    private final Map<ResourceKey<Level>, DimensionProperties> dimensionPropertyMap = Maps.newHashMap();

    protected Map<Category, List<SlotMetadata>> toolbars;

    protected final List<Category> categoryList;

    private boolean initialized = false;

    private Button buttonSave;

    public ServerOptionsManager(JmUI returnDisplay) {
        super(JourneymapClient.getInstance().getStateHandler().canServerAdmin() ? Constants.getString("jm.server.edit.label.admin.edit") : Constants.getString("jm.server.edit.label.admin.read_only"), returnDisplay);
        this.categoryList = new ArrayList(ServerCategory.values);
        this.requestInitData();
    }

    public ServerOptionsManager(JmUI returnDisplay, String title, List<Category> categoryList) {
        super(title, returnDisplay);
        this.categoryList = categoryList;
        this.requestInitData();
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, float par3) {
        try {
            if (this.optionsListPane != null) {
                List<FormattedCharSequence> lastTooltip = this.optionsListPane.lastTooltip;
                long lastTooltipTime = this.optionsListPane.lastTooltipTime;
                this.optionsListPane.lastTooltip = null;
                this.optionsListPane.m_88315_(graphics, x, y, par3);
                super.m_88315_(graphics, x, y, par3);
                if (this.optionsListPane.lastTooltip != null && !this.optionsListPane.lastTooltip.equals(lastTooltip)) {
                    this.optionsListPane.lastTooltipTime = lastTooltipTime;
                    if (System.currentTimeMillis() - this.optionsListPane.lastTooltipTime > this.optionsListPane.hoverDelay) {
                        Button button = this.optionsListPane.lastTooltipMetadata.getButton();
                        graphics.renderTooltip(this.getFontRenderer(), this.optionsListPane.lastTooltip, x, button.getBottomY() + 15);
                    }
                }
            }
        } catch (Throwable var9) {
            JMLogger.throwLogOnce("Error in OptionsManager.render(): " + var9, var9);
        }
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics graphics) {
    }

    @Override
    protected void layoutButtons(GuiGraphics graphics) {
        if (this.getRenderables().isEmpty()) {
            this.init();
        }
    }

    @Override
    public void init() {
        if (!this.initialized) {
            this.getRenderables().clear();
            this.specialBottomButtons.clear();
            this.initialized = true;
            this.buttonSave = new Button(Constants.getString("jm.waypoint.save"), b -> {
                this.save();
                this.buttonSave.setEnabled(false);
            });
            this.buttonSave.setEnabled(false);
            boolean canServerAdmin = JourneymapClient.getInstance().getStateHandler().canServerAdmin();
            if (!canServerAdmin) {
                this.buttonSave.setTooltip(Constants.getString("jm.server.button.save.read_only"));
            }
            this.buttonSave.setDefaultStyle(false);
            this.specialBottomButtons.add(this.buttonSave);
            this.m_142416_(this.buttonSave);
        }
        super.init();
        this.buttonServer.setEnabled(false);
        if (this.optionsListPane != null && this.slotMap.size() > WorldData.getDimensionProviders().size() + 2) {
            this.optionsListPane.m_93437_(this.f_96543_, this.f_96544_, 70, this.f_96544_ - 35);
        } else {
            this.optionsListPane = new OptionsScrollListPane<>(this, this.f_96541_, this.f_96543_, this.f_96544_, 70, this.f_96544_ - 35, 20);
            this.optionsListPane.m_93496_(false);
            this.optionsListPane.setAlignTop(true);
            List<CategorySlot> categorySlotList = this.getCategorySlotList();
            this.optionsListPane.setSlots(categorySlotList);
        }
        this.optionsListPane.updateSlots();
    }

    protected List<CategorySlot> getCategorySlotList() {
        return OptionSlotFactory.getOptionSlots(this.getToolbars(), this.slotMap, !JourneymapClient.getInstance().getStateHandler().canServerAdmin(), false);
    }

    protected Map<Category, List<SlotMetadata>> getToolbars() {
        this.toolbars = new HashMap();
        for (Category category : this.categoryList) {
            String name = Constants.getString("jm.config.reset");
            String tooltip = Constants.getString("jm.config.reset.tooltip");
            SlotMetadata<?> toolbarSlotMetadata = new SlotMetadata(new ResetButton(category), name, tooltip);
            this.toolbars.put(category, Collections.singletonList(toolbarSlotMetadata));
        }
        return this.toolbars;
    }

    private void onReset(Category category) {
        for (CategorySlot categorySlot : this.optionsListPane.getRootSlots()) {
            if (category.equals(categorySlot.getCategory())) {
                for (SlotMetadata<?> slotMetadata : categorySlot.getAllChildMetadata()) {
                    slotMetadata.resetToDefaultValue();
                }
                break;
            }
        }
    }

    protected void requestInitData() {
        this.requestData(ServerPropertyType.GLOBAL.getId(), "");
        this.requestData(ServerPropertyType.DEFAULT.getId(), "");
        WorldData.getDimensionProviders().forEach(dimensionProvider -> this.requestData(ServerPropertyType.DIMENSION.getId(), dimensionProvider.getDimensionId()));
    }

    protected void requestData(int id, String dim) {
        JourneymapClient.getInstance().getDispatcher().sendServerAdminScreenRequest(id, dim);
    }

    public void setData(ServerPropertyType requestType, String payload, String dim) {
        try {
            if (ServerPropertyType.GLOBAL.equals(requestType)) {
                this.globalProperties = new GlobalProperties().loadForClient(payload, false);
                this.slotMap.put(ServerCategory.Global, this.globalProperties);
            }
            if (ServerPropertyType.DEFAULT.equals(requestType)) {
                this.defaultDimensionProperties = new DefaultDimensionProperties().loadForClient(payload, false);
                this.slotMap.put(ServerCategory.Default, this.defaultDimensionProperties);
            }
            if (ServerPropertyType.DIMENSION.equals(requestType)) {
                DimensionProperties dimensionProperties = new DimensionProperties(dim).loadForClient(payload, false);
                this.dimensionPropertyMap.put(dimensionProperties.getDimension(), dimensionProperties);
                Category category = ServerCategory.create(dim, Constants.getString("jm.server.edit.label.selection.dimension", dim), Constants.getString("jm.server.edit.label.selection.dimension.tooltip"));
                this.categoryList.add(category);
                this.slotMap.put(category, dimensionProperties);
            }
            this.init();
        } catch (Exception var6) {
            Journeymap.getLogger().error("Error getting data", var6);
        }
    }

    protected void save() {
        this.sendSavePacket(ServerPropertyType.GLOBAL.getId(), this.globalProperties.toJsonString(false), "");
        this.sendSavePacket(ServerPropertyType.DEFAULT.getId(), this.defaultDimensionProperties.toJsonString(false), "");
        this.dimensionPropertyMap.values().forEach(dim -> this.sendSavePacket(ServerPropertyType.DIMENSION.getId(), dim.toJsonString(false), DimensionHelper.getDimKeyName(dim.getDimension())));
    }

    private void sendSavePacket(int id, String payload, String dim) {
        JourneymapClient.getInstance().getDispatcher().sendSaveAdminDataPacket(id, payload, dim);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        try {
            boolean pressed = this.optionsListPane.m_6375_(mouseX, mouseY, mouseButton);
            if (pressed) {
                SlotMetadata slotMetadata = this.optionsListPane.getLastPressed();
                if (slotMetadata != null) {
                    if (slotMetadata.getButton() instanceof ResetButton) {
                        this.onReset(((ResetButton) slotMetadata.getButton()).category);
                    }
                    if (!slotMetadata.isToolbar() || slotMetadata.getButton() instanceof ResetButton) {
                        this.buttonSave.setEnabled(true);
                    }
                }
            }
            return super.m_6375_(mouseX, mouseY, mouseButton);
        } catch (Throwable var8) {
            Journeymap.getLogger().error(var8.getMessage(), var8);
            return false;
        }
    }

    @Override
    public void resize(@NotNull Minecraft minecraft, int width, int height) {
        this.initialized = false;
        super.m_6574_(minecraft, width, height);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        this.optionsListPane.m_6348_(mouseX, mouseY, mouseButton);
        return super.m_6348_(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double mouseDX, double mouseDY) {
        this.optionsListPane.m_7979_(mouseX, mouseY, button, mouseDX, mouseDY);
        return super.m_7979_(mouseX, mouseY, button, mouseDX, mouseDY);
    }

    @Override
    public boolean mouseScrolled(double x, double y, double scroll) {
        this.optionsListPane.m_6050_(x, y, scroll);
        return super.m_6050_(x, y, scroll);
    }
}