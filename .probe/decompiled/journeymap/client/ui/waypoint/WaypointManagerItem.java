package journeymap.client.ui.waypoint;

import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Color;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import journeymap.client.Constants;
import journeymap.client.JourneymapClient;
import journeymap.client.command.CmdTeleportWaypoint;
import journeymap.client.render.draw.DrawUtil;
import journeymap.client.texture.Texture;
import journeymap.client.ui.UIManager;
import journeymap.client.ui.component.Button;
import journeymap.client.ui.component.ButtonList;
import journeymap.client.ui.component.OnOffButton;
import journeymap.client.ui.component.Slot;
import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.client.ui.option.SlotMetadata;
import journeymap.client.waypoint.Waypoint;
import journeymap.client.waypoint.WaypointStore;
import journeymap.common.Journeymap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;

public class WaypointManagerItem extends Slot {

    static Integer background = new Color(20, 20, 20).getRGB();

    static Integer backgroundHover = new Color(40, 40, 40).getRGB();

    final Font fontRenderer;

    final WaypointManager manager;

    int x;

    int y;

    int width;

    int internalWidth;

    Integer distance;

    Waypoint waypoint;

    OnOffButton buttonEnable;

    Button buttonRemove;

    Button buttonEdit;

    Button buttonFind;

    Button buttonTeleport;

    Button buttonChat;

    OnOffButton buttonDeviationEnable;

    int hgap = 4;

    ButtonList buttonListLeft;

    ButtonList buttonListRight;

    int slotIndex;

    SlotMetadata<Waypoint> slotMetadata;

    boolean displayHover = true;

    NumberFormat formatter = new DecimalFormat("0.##E0");

    public WaypointManagerItem(Waypoint waypoint, Font fontRenderer, WaypointManager manager) {
        int id = 0;
        this.waypoint = waypoint;
        this.fontRenderer = fontRenderer;
        this.manager = manager;
        new SlotMetadata(null, null, null, false);
        String on = Constants.getString("jm.common.on");
        String off = Constants.getString("jm.common.off");
        this.buttonEnable = new OnOffButton(on, off, true, Button.emptyPressable());
        this.buttonEnable.setToggled(waypoint.isEnable());
        this.buttonDeviationEnable = new OnOffButton(on, off, waypoint.showDeviation(), Button.emptyPressable());
        this.buttonFind = new Button(Constants.getString("jm.waypoint.find"));
        this.buttonTeleport = new Button(Constants.getString("jm.waypoint.teleport"));
        this.buttonTeleport.setDrawButton(manager.canUserTeleport);
        this.buttonTeleport.setEnabled(manager.canUserTeleport);
        this.buttonListLeft = new ButtonList(this.buttonEnable, this.buttonFind, this.buttonTeleport);
        if (JourneymapClient.getInstance().getWaypointProperties().showDeviationLabel.get()) {
            this.buttonListLeft.add(this.buttonDeviationEnable);
        }
        this.buttonListLeft.setHeights(manager.rowHeight);
        this.buttonListLeft.fitWidths(fontRenderer);
        this.buttonEdit = new Button(Constants.getString("jm.waypoint.edit"));
        this.buttonRemove = new Button(Constants.getString("jm.waypoint.remove"));
        this.buttonChat = new Button(Constants.getString("jm.waypoint.chat"));
        this.buttonChat.setTooltip(Constants.getString("jm.waypoint.chat.tooltip"));
        this.buttonListRight = new ButtonList(this.buttonChat, this.buttonEdit, this.buttonRemove);
        this.buttonListRight.setHeights(manager.rowHeight);
        this.buttonListRight.fitWidths(fontRenderer);
        this.internalWidth = fontRenderer.width("X") * 20 + fontRenderer.width(waypoint.getName());
        this.internalWidth = this.internalWidth + Math.min(manager.colLocation, manager.colName);
        this.internalWidth = this.internalWidth + this.buttonListLeft.getWidth(this.hgap);
        this.internalWidth = this.internalWidth + this.buttonListRight.getWidth(this.hgap);
        this.internalWidth += 10;
    }

    public int getSlotIndex() {
        return this.slotIndex;
    }

    protected void drawLabels(GuiGraphics graphics, Minecraft mc, int x, int y, Integer color) {
        if (this.waypoint != null) {
            boolean waypointValid = this.waypoint.isEnable() && this.waypoint.isInPlayerDimension();
            if (color == null) {
                color = waypointValid ? this.waypoint.getSafeColor() : 8421504;
            }
            Font fr = Minecraft.getInstance().font;
            int yOffset = 1 + (this.manager.rowHeight - 9) / 2;
            this.fontRenderer.drawInBatch(this.getDistanceString(), (float) (x + this.manager.colLocation), (float) (y + yOffset), color, true, graphics.pose().last().pose(), graphics.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
            boolean disableStrikeThrough = JourneymapClient.getInstance().getWaypointProperties().disableStrikeThrough.get();
            String name = !waypointValid && !disableStrikeThrough ? ChatFormatting.STRIKETHROUGH + this.waypoint.getName() : this.waypoint.getName();
            this.fontRenderer.drawInBatch(name, (float) this.manager.colName, (float) (y + yOffset), color, true, graphics.pose().last().pose(), graphics.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
        }
    }

    protected void drawWaypoint(PoseStack poseStack, int x, int y) {
        Texture wpTexture = this.waypoint.getTexture();
        DrawUtil.drawColoredImage(poseStack, wpTexture, this.waypoint.getIconColor(), 1.0F, (double) x, (double) (y - wpTexture.getHeight() / 2), 0.0);
    }

    protected void enableWaypoint(boolean enable) {
        this.buttonEnable.setToggled(enable);
        this.waypoint.setEnable(enable);
    }

    protected int getButtonEnableCenterX() {
        return this.buttonEnable.getCenterX();
    }

    protected int getButtonDeviationX() {
        return this.buttonDeviationEnable.getCenterX();
    }

    protected int getNameLeftX() {
        return this.x + this.manager.getMargin() + this.manager.colName;
    }

    protected int getLocationLeftX() {
        return this.x + this.manager.getMargin() + this.manager.colLocation;
    }

    private String getDistanceString() {
        String distance = String.valueOf(this.getDistance());
        int width = this.fontRenderer.width(distance);
        if (width > 35) {
            distance = this.formatter.format((long) this.getDistance()).toLowerCase(Locale.ROOT);
        }
        return String.format("%sm", distance);
    }

    public boolean clickScrollable(double mouseX, double mouseY) {
        try {
            if (this.waypoint == null) {
                return false;
            }
            if (this.buttonChat.mouseOver(mouseX, mouseY)) {
                try {
                    Minecraft.getInstance().setScreen(new WaypointChat(this.waypoint));
                } catch (Exception var6) {
                    Journeymap.getLogger().error("Error opening waypoint chat for waypoint {} :", this.waypoint.toString(), var6);
                }
                return true;
            }
            if (this.buttonRemove.mouseOver(mouseX, mouseY)) {
                this.manager.removeWaypoint(this);
                this.waypoint = null;
                return true;
            }
            if (this.buttonEnable.mouseOver(mouseX, mouseY)) {
                this.buttonEnable.toggle();
                this.waypoint.setEnable(this.buttonEnable.getToggled());
                if (this.waypoint.isDirty()) {
                    WaypointStore.INSTANCE.save(this.waypoint, false);
                }
                return true;
            }
            if (this.buttonDeviationEnable.mouseOver(mouseX, mouseY)) {
                this.buttonDeviationEnable.toggle();
                this.waypoint.setShowDeviation(this.buttonDeviationEnable.getToggled());
                if (this.waypoint.isDirty()) {
                    WaypointStore.INSTANCE.save(this.waypoint, false);
                }
                return true;
            }
            if (this.buttonEdit.mouseOver(mouseX, mouseY)) {
                UIManager.INSTANCE.openWaypointEditor(this.waypoint, false, this.manager);
                return true;
            }
            if (this.buttonFind.isEnabled() && this.buttonFind.mouseOver(mouseX, mouseY)) {
                UIManager.INSTANCE.openFullscreenMap(this.waypoint);
                return true;
            }
            if (this.manager.canUserTeleport && this.buttonTeleport.mouseOver(mouseX, mouseY)) {
                new CmdTeleportWaypoint(this.waypoint).run();
                Fullscreen.state().follow.set(true);
                UIManager.INSTANCE.closeAll();
                return true;
            }
        } catch (Exception var7) {
            Journeymap.getLogger().error("Problem with clickScrollable.", var7);
        }
        return false;
    }

    public int getDistance() {
        return this.distance == null ? 0 : this.distance;
    }

    public int getDistanceTo(Player player) {
        if (this.distance == null) {
            this.distance = (int) player.m_20182_().distanceTo(this.waypoint.getPosition());
        }
        return this.distance;
    }

    @Override
    public Collection<SlotMetadata> getMetadata() {
        return null;
    }

    @Override
    public void render(GuiGraphics graphics, int slotIndex, int y, int x, int rowWidth, int itemHeight, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
        Minecraft mc = this.manager.getMinecraft();
        this.width = rowWidth;
        this.x = x;
        this.y = y;
        boolean drawHovered = isMouseOver && this.displayHover;
        if (this.waypoint != null) {
            this.buttonListLeft.setOptions(true, drawHovered, true);
            this.buttonListRight.setOptions(true, drawHovered, true);
            Integer color = drawHovered ? backgroundHover : background;
            float alpha = drawHovered ? 1.0F : 0.4F;
            DrawUtil.drawRectangle(graphics.pose(), (double) this.x, (double) this.y, (double) this.width, (double) this.manager.rowHeight, color, alpha);
            int margin = this.manager.getMargin();
            this.drawWaypoint(graphics.pose(), this.x + margin + this.manager.colWaypoint, this.y + this.manager.rowHeight / 2);
            this.drawLabels(graphics, mc, this.x + margin, this.y, null);
            this.buttonTeleport.drawHovered(drawHovered);
            this.buttonFind.drawHovered(drawHovered);
            this.buttonEnable.drawHovered(drawHovered);
            this.buttonDeviationEnable.drawHovered(drawHovered);
            this.buttonRemove.drawHovered(drawHovered);
            this.buttonEdit.drawHovered(drawHovered);
            this.buttonChat.drawHovered(drawHovered);
            this.buttonFind.setEnabled(this.waypoint.isInPlayerDimension());
            this.buttonTeleport.setEnabled(this.waypoint.isTeleportReady());
            this.buttonListRight.layoutHorizontal(x + this.width - margin, y, false, this.hgap).draw(graphics, mouseX, mouseY);
            this.buttonListLeft.layoutHorizontal(this.buttonListRight.getLeftX() - this.hgap * 2, y, false, this.hgap).draw(graphics, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseEvent) {
        try {
            return this.clickScrollable(mouseX, mouseY);
        } catch (Exception var7) {
            Journeymap.getLogger().error("WARNING: Problem with mouseClicked.");
            throw new RuntimeException("mouseClicked", var7);
        }
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        return false;
    }

    @Override
    public boolean keyPressed(int key, int value, int modifier) {
        return false;
    }

    @Override
    public List<Slot> getChildSlots(int listWidth, int columnWidth) {
        return null;
    }

    @Override
    public SlotMetadata getLastPressed() {
        return null;
    }

    @Override
    public SlotMetadata getCurrentTooltip() {
        return null;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.buttonEnable.setToggled(this.waypoint.isEnable());
    }

    @Override
    public int getColumnWidth() {
        return this.width;
    }

    @Override
    public boolean contains(SlotMetadata slotMetadata) {
        return false;
    }

    @Override
    public void displayHover(boolean enabled) {
        this.displayHover = enabled;
    }

    static class DeviationComparator extends WaypointManagerItem.Sort {

        public DeviationComparator(boolean ascending) {
            super(ascending);
        }

        public int compare(WaypointManagerItem o1, WaypointManagerItem o2) {
            return this.ascending ? Boolean.compare(o1.waypoint.showDeviation(), o2.waypoint.showDeviation()) : Boolean.compare(o2.waypoint.showDeviation(), o1.waypoint.showDeviation());
        }
    }

    static class DistanceComparator extends WaypointManagerItem.Sort {

        Player player;

        public DistanceComparator(Player player, boolean ascending) {
            super(ascending);
            this.player = player;
        }

        public int compare(WaypointManagerItem o1, WaypointManagerItem o2) {
            double dist1 = (double) o1.getDistanceTo(this.player);
            double dist2 = (double) o2.getDistanceTo(this.player);
            return this.ascending ? Double.compare(dist1, dist2) : Double.compare(dist2, dist1);
        }
    }

    static class NameComparator extends WaypointManagerItem.Sort {

        public NameComparator(boolean ascending) {
            super(ascending);
        }

        public int compare(WaypointManagerItem o1, WaypointManagerItem o2) {
            return this.ascending ? o1.waypoint.getName().compareToIgnoreCase(o2.waypoint.getName()) : o2.waypoint.getName().compareToIgnoreCase(o1.waypoint.getName());
        }
    }

    abstract static class Sort implements Comparator<WaypointManagerItem> {

        boolean ascending;

        Sort(boolean ascending) {
            this.ascending = ascending;
        }

        public boolean equals(Object o) {
            return this == o ? true : o != null && this.getClass() == o.getClass();
        }

        public int hashCode() {
            return this.ascending ? 1 : 0;
        }
    }
}