package journeymap.client.ui.waypoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import journeymap.client.Constants;
import journeymap.client.JourneymapClient;
import journeymap.client.command.CmdTeleportWaypoint;
import journeymap.client.log.JMLogger;
import journeymap.client.properties.ClientCategory;
import journeymap.client.ui.UIManager;
import journeymap.client.ui.component.Button;
import journeymap.client.ui.component.ButtonList;
import journeymap.client.ui.component.JmUI;
import journeymap.client.ui.component.OnOffButton;
import journeymap.client.ui.component.ScrollListPane;
import journeymap.client.ui.component.Slot;
import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.client.ui.option.CategorySlot;
import journeymap.client.ui.option.SlotMetadata;
import journeymap.client.waypoint.Waypoint;
import journeymap.client.waypoint.WaypointStore;
import journeymap.common.Journeymap;
import journeymap.common.properties.catagory.Category;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.FormattedCharSequence;

public class WaypointManager extends JmUI {

    static final String ASCEND = "▲";

    static final String DESCEND = "▼";

    static final int COLWAYPOINT = 0;

    static final int COLLOCATION = 20;

    static final int COLNAME = 60;

    static final int DEFAULT_ITEMWIDTH = 460;

    private static WaypointManagerItem.Sort currentSort;

    private final String on = Constants.getString("jm.common.on");

    private final String off = Constants.getString("jm.common.off");

    protected int colWaypoint = 0;

    protected int colLocation = 20;

    protected int colName = 60;

    protected int itemWidth = 460;

    protected ScrollListPane itemScrollPane;

    protected int rowHeight = 16;

    Boolean canUserTeleport;

    private SortButton buttonSortName;

    private SortButton buttonSortDistance;

    private SortButton buttonSortDeviation;

    private DimensionsDropDownButton buttonDimensions;

    private Button buttonClose;

    private Button buttonAdd;

    private Button buttonOptions;

    private OnOffButton buttonToggleAll;

    private ButtonList bottomButtons;

    private Waypoint focusWaypoint;

    private ArrayList<WaypointManagerItem> items = new ArrayList();

    private static boolean toggled = true;

    public WaypointManager() {
        this(null, null);
    }

    public WaypointManager(JmUI returnDisplay) {
        this(null, returnDisplay);
    }

    public WaypointManager(Waypoint focusWaypoint, JmUI returnDisplay) {
        super(Constants.getString("jm.waypoint.manage_title"), returnDisplay);
        this.focusWaypoint = focusWaypoint;
    }

    public void setFocusWaypoint(Waypoint focusWaypoint) {
        this.focusWaypoint = focusWaypoint;
    }

    @Override
    public void init() {
        try {
            this.setRenderBottomBar(true);
            this.getRenderables().clear();
            Journeymap.getLogger().debug("Checking if tp is permitted");
            this.canUserTeleport = CmdTeleportWaypoint.isPermitted(Minecraft.getInstance());
            Journeymap.getLogger().debug("Checked if tp button should be displayed:" + this.canUserTeleport);
            Font fr = this.getFontRenderer();
            WaypointManagerItem.Sort distanceSort = new WaypointManagerItem.DistanceComparator(Minecraft.getInstance().player, true);
            String distanceLabel = Constants.getString("jm.waypoint.distance");
            this.buttonSortDistance = new SortButton(distanceLabel, distanceSort, b -> {
                this.buttonSortDistance.toggle();
                this.updateSort(this.buttonSortDistance);
            });
            this.buttonSortDistance.setTextOnly(fr);
            this.getRenderables().add(this.buttonSortDistance);
            this.m_142416_(this.buttonSortDistance);
            WaypointManagerItem.Sort nameSort = new WaypointManagerItem.NameComparator(true);
            WaypointManagerItem.Sort deviationSort = new WaypointManagerItem.DeviationComparator(true);
            this.buttonSortName = new SortButton(Constants.getString("jm.waypoint.name"), nameSort, b -> {
                this.buttonSortName.toggle();
                this.updateSort(this.buttonSortName);
            });
            this.buttonSortName.setTextOnly(fr);
            this.m_142416_(this.buttonSortName);
            this.getRenderables().add(this.buttonSortName);
            this.buttonSortDeviation = new SortButton(Constants.getString("jm.waypoint.deviation_button_sort"), deviationSort, b -> {
                this.buttonSortDeviation.toggle();
                this.updateSort(this.buttonSortDeviation);
            });
            this.buttonSortDeviation.setTextOnly(fr);
            this.m_142416_(this.buttonSortDeviation);
            this.getRenderables().add(this.buttonSortDeviation);
            String enableOn = Constants.getString("jm.waypoint.enable_all", "", this.on);
            String enableOff = Constants.getString("jm.waypoint.enable_all", "", this.off);
            this.buttonToggleAll = new OnOffButton(enableOff, enableOn, true, b -> {
                boolean state = this.buttonToggleAll.getToggled();
                state = this.toggleItems(state);
                this.buttonToggleAll.setToggled(state);
            });
            this.buttonToggleAll.setTextOnly(this.getFontRenderer());
            this.m_142416_(this.buttonToggleAll);
            this.getRenderables().add(this.buttonToggleAll);
            this.buttonDimensions = new DimensionsDropDownButton(b -> this.updateItems());
            this.m_142416_(this.buttonDimensions);
            if (JourneymapClient.getInstance().getWaypointProperties().managerDimensionFocus.get()) {
                this.buttonDimensions.setDim(this.f_96541_.player.m_9236_().dimension());
            }
            this.buttonAdd = (Button) this.m_142416_(new Button(Constants.getString("jm.waypoint.new"), button -> {
                Waypoint waypoint = Waypoint.of(this.f_96541_.player);
                UIManager.INSTANCE.openWaypointEditor(waypoint, true, this);
            }));
            this.buttonAdd.fitWidth(this.getFontRenderer());
            this.buttonAdd.setWidth(this.buttonAdd.m_5711_() * 2);
            this.buttonOptions = (Button) this.m_142416_(new Button(Constants.getString("jm.common.options_button"), button -> UIManager.INSTANCE.openOptionsManager(this, ClientCategory.Waypoint, ClientCategory.WaypointBeacon)));
            this.buttonOptions.fitWidth(this.getFontRenderer());
            this.buttonClose = (Button) this.m_142416_(new Button(Constants.getString("jm.common.close"), button -> this.refreshAndClose()));
            this.bottomButtons = new ButtonList(this.buttonOptions, this.buttonAdd, this.buttonDimensions, this.buttonClose);
            this.buttonOptions.setDefaultStyle(false);
            this.buttonAdd.setDefaultStyle(false);
            this.buttonDimensions.setDefaultStyle(false);
            this.buttonClose.setDefaultStyle(false);
            this.getRenderables().addAll(this.bottomButtons);
            if (this.items.isEmpty()) {
                this.updateItems();
                if (currentSort == null) {
                    this.updateSort(this.buttonSortDistance);
                } else {
                    if (this.buttonSortDistance.sort.equals(currentSort)) {
                        this.buttonSortDistance.sort.ascending = currentSort.ascending;
                        this.buttonSortDistance.setActive(true);
                        this.buttonSortName.setActive(false);
                        this.buttonSortDeviation.setActive(false);
                    }
                    if (this.buttonSortName.sort.equals(currentSort)) {
                        this.buttonSortName.sort.ascending = currentSort.ascending;
                        this.buttonSortName.setActive(true);
                        this.buttonSortDistance.setActive(false);
                        this.buttonSortDeviation.setActive(false);
                    }
                    if (this.buttonSortDeviation.sort.equals(currentSort)) {
                        this.buttonSortDeviation.sort.ascending = currentSort.ascending;
                        this.buttonSortDeviation.setActive(true);
                        this.buttonSortName.setActive(false);
                        this.buttonSortDistance.setActive(false);
                    }
                }
            }
            if (this.itemScrollPane == null) {
                this.itemScrollPane = new ScrollListPane(this, this.f_96541_, this.f_96543_, this.f_96544_, 35, this.f_96544_ - 30, 20);
                this.itemScrollPane.m_93496_(false);
            } else {
                this.itemScrollPane.updateSize(this.f_96543_, this.f_96544_, 35, this.f_96544_ - 30);
                this.itemScrollPane.updateSlots();
            }
            this.itemScrollPane.setSlots(this.items);
            if (!this.items.isEmpty()) {
                this.itemScrollPane.scrollTo((Slot) this.items.get(0));
            }
        } catch (Throwable var8) {
            JMLogger.throwLogOnce("Error in OptionsManager.init(): " + var8, var8);
        }
    }

    @Override
    protected void layoutButtons(GuiGraphics guiGraphics) {
        if (this.getRenderables().isEmpty() || this.itemScrollPane == null) {
            this.init();
        }
        this.buttonToggleAll.setDrawButton(!this.items.isEmpty());
        this.buttonSortDistance.setDrawButton(!this.items.isEmpty());
        this.buttonSortName.setDrawButton(!this.items.isEmpty());
        this.buttonSortDeviation.setDrawButton(!this.items.isEmpty() && JourneymapClient.getInstance().getWaypointProperties().showDeviationLabel.get());
        this.bottomButtons.equalizeWidths(this.getFontRenderer());
        int bottomButtonWidth = Math.min(this.bottomButtons.getWidth(4) + 25, this.itemScrollPane.getRowWidth());
        this.bottomButtons.equalizeWidths(this.getFontRenderer(), 4, bottomButtonWidth);
        this.bottomButtons.layoutCenteredHorizontal(this.f_96543_ / 2, this.f_96544_ - 25, true, 4);
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, float par3) {
        if (this.f_96541_ != null) {
            if (this.getRenderables().isEmpty() || this.itemScrollPane == null) {
                this.init();
            }
            if (currentSort != null) {
                Collections.sort(this.items, currentSort);
            }
            if (this.itemScrollPane != null) {
                this.itemScrollPane.setSlots(this.items);
            }
            try {
                graphics.pose().pushPose();
                this.itemScrollPane.updateSize(this.f_96543_, this.f_96544_, 35, this.f_96544_ - 30);
                List<FormattedCharSequence> lastTooltip = this.itemScrollPane.lastTooltip;
                long lastTooltipTime = this.itemScrollPane.lastTooltipTime;
                this.itemScrollPane.lastTooltip = null;
                this.itemScrollPane.render(graphics, x, y, par3);
                super.render(graphics, x, y, par3);
                if (!this.items.isEmpty()) {
                    int headerY = 35 - 9;
                    WaypointManagerItem firstRow = (WaypointManagerItem) this.items.get(0);
                    if (firstRow.y > headerY + 16) {
                        headerY = firstRow.y - 16;
                    }
                    this.buttonToggleAll.centerHorizontalOn(firstRow.getButtonEnableCenterX()).setY(headerY);
                    this.buttonSortDistance.centerHorizontalOn(firstRow.getLocationLeftX()).setY(headerY);
                    this.colName = this.buttonSortDistance.getRightX() + 20;
                    this.buttonSortName.setScrollablePosition(this.colName - 5, headerY);
                    this.buttonSortDeviation.centerHorizontalOn(firstRow.getButtonDeviationX()).setY(headerY);
                }
                this.buttonToggleAll.drawUnderline(graphics.pose());
                for (List<SlotMetadata> toolbar : this.getToolbars().values()) {
                    for (SlotMetadata slotMetadata : toolbar) {
                        slotMetadata.getButton().secondaryDrawButton();
                    }
                }
                if (this.itemScrollPane.lastTooltip != null && this.itemScrollPane.lastTooltip.equals(lastTooltip)) {
                    this.itemScrollPane.lastTooltipTime = lastTooltipTime;
                    if (System.currentTimeMillis() - this.itemScrollPane.lastTooltipTime > this.itemScrollPane.hoverDelay) {
                        Button button = this.itemScrollPane.lastTooltipMetadata.getButton();
                        graphics.renderTooltip(this.getFontRenderer(), this.itemScrollPane.lastTooltip, x, button.getBottomY() + 15);
                    }
                }
            } catch (Throwable var15) {
                JMLogger.throwLogOnce("Error in OptionsManager.render(): " + var15, var15);
            } finally {
                graphics.pose().popPose();
            }
        }
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics) {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        try {
            if (mouseButton == 0) {
                boolean pressed = this.itemScrollPane.mouseClicked(mouseX, mouseY, mouseButton);
                if (pressed) {
                    this.checkPressedButton();
                    return true;
                }
            }
        } catch (Exception var7) {
            Journeymap.getLogger().error("Problem with mouseClicked", var7);
        }
        return super.m_6375_(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        this.itemScrollPane.mouseReleased(mouseX, mouseY, mouseButton);
        return super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double mouseDX, double mouseDY) {
        this.checkPressedButton();
        this.itemScrollPane.mouseDragged(mouseX, mouseY, button, mouseDX, mouseDY);
        return super.m_7979_(mouseX, mouseY, button, mouseDX, mouseDY);
    }

    @Override
    public boolean mouseScrolled(double x, double y, double scroll) {
        return this.itemScrollPane.m_6050_(x, y, scroll) ? true : super.m_6050_(x, y, scroll);
    }

    protected void checkPressedButton() {
        try {
            SlotMetadata slotMetadata = this.itemScrollPane.getLastPressed();
            if (slotMetadata != null) {
            }
            Slot parentSlot = (CategorySlot) this.itemScrollPane.getLastPressedParentSlot();
            if (parentSlot != null) {
            }
        } catch (Exception var3) {
            Journeymap.getLogger().error("WARNING: Problem checking buttons.");
            throw new RuntimeException("checkPressedButton", var3);
        }
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        if (keyCode == 256) {
            this.closeAndReturn();
        }
        if (this.itemScrollPane != null) {
            boolean keyUsed = this.itemScrollPane.charTyped(typedChar, keyCode);
            if (keyUsed) {
                return true;
            }
            if (keyCode == 265) {
                this.itemScrollPane.m_93429_(-this.rowHeight);
                return true;
            }
            if (keyCode == 264) {
                this.itemScrollPane.m_93429_(this.rowHeight);
                return true;
            }
            if (keyCode == 266) {
                this.itemScrollPane.m_93429_(-this.itemScrollPane.getHeight());
                return true;
            }
            if (keyCode == 267) {
                this.itemScrollPane.m_93429_(this.itemScrollPane.getHeight());
                return true;
            }
            if (keyCode == 268) {
                this.itemScrollPane.m_93429_((int) (-this.itemScrollPane.m_93517_()));
                return true;
            }
            if (keyCode == 269) {
                this.itemScrollPane.m_93429_((int) this.itemScrollPane.m_93517_());
                return true;
            }
        } else {
            Journeymap.getLogger().error("WARNING: itemScrollPane is null. This is an error state! Report to Developers!");
        }
        return true;
    }

    protected boolean toggleItems(boolean enable) {
        for (WaypointManagerItem item : this.items) {
            if (enable == item.waypoint.isEnable()) {
                enable = !enable;
                break;
            }
        }
        for (WaypointManagerItem itemx : this.items) {
            if (itemx.waypoint.isEnable() != enable) {
                itemx.enableWaypoint(enable);
            }
        }
        return !enable;
    }

    public static void toggleAllWaypoints() {
        toggled = !toggled;
        for (Waypoint waypoint : WaypointStore.INSTANCE.getAll()) {
            waypoint.setEnable(toggled);
            waypoint.setDirty();
        }
        WaypointStore.INSTANCE.bulkSave();
    }

    protected void updateItems() {
        this.items.clear();
        String currentDim = DimensionsDropDownButton.currentWorldProvider == null ? null : DimensionsDropDownButton.currentWorldProvider.getDimensionId();
        Font fr = this.getFontRenderer();
        Collection<Waypoint> waypoints = WaypointStore.INSTANCE.getAll();
        boolean allOn = true;
        this.itemWidth = 0;
        for (Waypoint waypoint : waypoints) {
            WaypointManagerItem item = new WaypointManagerItem(waypoint, fr, this);
            this.itemWidth = Math.max(item.internalWidth, this.itemWidth);
            item.getDistanceTo(this.f_96541_.player);
            if (currentDim == null || item.waypoint.getDimensions().contains(currentDim)) {
                this.items.add(item);
                if (allOn) {
                    allOn = waypoint.isEnable();
                }
            }
        }
        if (this.items.isEmpty()) {
            this.itemWidth = 460;
        }
        this.buttonToggleAll.setToggled(!allOn);
        this.updateCount();
    }

    protected void updateSort(SortButton sortButton) {
        for (Renderable button : this.getRenderables()) {
            if (button instanceof SortButton) {
                if (button == sortButton) {
                    if (!sortButton.sort.equals(currentSort)) {
                        sortButton.setActive(true);
                    }
                    currentSort = sortButton.sort;
                } else {
                    ((SortButton) button).setActive(false);
                }
            }
        }
    }

    protected void updateCount() {
        String itemCount = this.items.isEmpty() ? "" : Integer.toString(this.items.size());
        String enableOn = Constants.getString("jm.waypoint.enable_all", itemCount, this.on);
        String enableOff = Constants.getString("jm.waypoint.enable_all", itemCount, this.off);
        this.buttonToggleAll.setLabels(enableOff, enableOn);
    }

    protected boolean isSelected(WaypointManagerItem item) {
        return this.itemScrollPane.isSelectedItem(item.getSlotIndex());
    }

    protected int getMargin() {
        return this.f_96543_ > this.itemWidth + 2 ? (this.f_96543_ - this.itemWidth) / 2 : 0;
    }

    public void removeWaypoint(WaypointManagerItem item) {
        WaypointStore.INSTANCE.remove(item.waypoint, true);
        this.items.remove(item);
        this.updateItems();
    }

    protected void refreshAndClose() {
        this.closeAndReturn();
    }

    @Override
    protected void closeAndReturn() {
        this.bottomButtons.setEnabled(false);
        WaypointStore.INSTANCE.bulkSave();
        Fullscreen.state().requireRefresh();
        this.bottomButtons.setEnabled(true);
        if (returnDisplayStack != null && returnDisplayStack.peek() != null) {
            UIManager.INSTANCE.open((Screen) returnDisplayStack.pop());
        } else {
            UIManager.INSTANCE.closeAll();
        }
    }

    Map<Category, List<SlotMetadata>> getToolbars() {
        return Collections.EMPTY_MAP;
    }
}