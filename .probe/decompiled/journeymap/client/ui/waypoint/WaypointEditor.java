package journeymap.client.ui.waypoint;

import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Color;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;
import java.util.Collection;
import journeymap.client.Constants;
import journeymap.client.JourneymapClient;
import journeymap.client.cartography.color.RGB;
import journeymap.client.data.WorldData;
import journeymap.client.log.JMLogger;
import journeymap.client.properties.FullMapProperties;
import journeymap.client.render.draw.DrawUtil;
import journeymap.client.texture.SimpleTextureImpl;
import journeymap.client.texture.Texture;
import journeymap.client.texture.TextureCache;
import journeymap.client.ui.UIManager;
import journeymap.client.ui.component.Button;
import journeymap.client.ui.component.ButtonList;
import journeymap.client.ui.component.JmUI;
import journeymap.client.ui.component.OnOffButton;
import journeymap.client.ui.component.ScrollPane;
import journeymap.client.ui.component.TextBox;
import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.client.ui.option.LocationFormat;
import journeymap.client.waypoint.Waypoint;
import journeymap.client.waypoint.WaypointStore;
import journeymap.common.Journeymap;
import journeymap.common.helper.DimensionHelper;
import journeymap.common.log.LogFormatter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.apache.logging.log4j.util.Strings;

public class WaypointEditor extends JmUI {

    private final Texture wpTexture;

    private final Texture colorPickTexture;

    private final Waypoint originalWaypoint;

    private final boolean isNew;

    String labelName = Constants.getString("jm.waypoint.name");

    String locationTitle = Constants.getString("jm.waypoint.location");

    String colorTitle = Constants.getString("jm.waypoint.color");

    String dimensionsTitle = Constants.getString("jm.waypoint.dimensions");

    String labelX = Constants.getString("jm.waypoint.x");

    String labelY = Constants.getString("jm.waypoint.y");

    String labelZ = Constants.getString("jm.waypoint.z");

    String labelR = Constants.getString("jm.waypoint.red_abbreviated");

    String labelG = Constants.getString("jm.waypoint.green_abbreviated");

    String labelB = Constants.getString("jm.waypoint.blue_abbreviated");

    String currentLocation = "";

    LocationFormat.LocationFormatKeys locationFormatKeys;

    private Button buttonRandomize;

    private OnOffButton buttonEnable;

    private Button buttonRemove;

    private Button buttonReset;

    private Button buttonSave;

    private Button buttonClose;

    private TextBox fieldName;

    private TextBox fieldR;

    private TextBox fieldG;

    private TextBox fieldB;

    private TextBox fieldX;

    private TextBox fieldY;

    private TextBox fieldZ;

    private ArrayList<TextBox> fieldList = new ArrayList();

    private ArrayList<WaypointEditor.DimensionButton> dimButtonList = new ArrayList();

    private ScrollPane dimScrollPane;

    private Integer currentColor;

    private Double colorPickRect;

    private String colorPickTooltip;

    private Waypoint editedWaypoint;

    private ButtonList bottomButtons;

    private float partialTicks;

    private boolean openedWithHotKey;

    private final int bottomY = -100;

    public WaypointEditor(Waypoint waypoint, boolean isNew, JmUI returnDisplay, boolean openedWithHotKey) {
        super(Constants.getString(isNew ? "jm.waypoint.new_title" : "jm.waypoint.edit_title"), returnDisplay);
        this.originalWaypoint = waypoint;
        this.editedWaypoint = new Waypoint(this.originalWaypoint);
        this.isNew = isNew;
        this.openedWithHotKey = openedWithHotKey;
        this.wpTexture = waypoint.getTexture();
        String tooltip = Constants.birthdayMessage();
        this.colorPickTooltip = tooltip;
        this.colorPickTexture = tooltip == null ? TextureCache.getTexture(TextureCache.ColorPicker) : TextureCache.getTexture(TextureCache.ColorPicker2);
        try {
            this.colorPickRect = new Double(0.0, 0.0, (double) this.colorPickTexture.getWidth(), (double) this.colorPickTexture.getHeight());
        } catch (Throwable var7) {
            Journeymap.getLogger().error("Error during WaypointEditor ctor: " + LogFormatter.toPartialString(var7));
            UIManager.INSTANCE.closeAll();
        }
    }

    @Override
    public void init() {
        try {
            this.setRenderBottomBar(true);
            FullMapProperties fullMapProperties = JourneymapClient.getInstance().getFullMapProperties();
            LocationFormat locationFormat = new LocationFormat();
            this.locationFormatKeys = locationFormat.getFormatKeys(fullMapProperties.locationFormat.get());
            String pos = this.locationFormatKeys.format(fullMapProperties.locationFormatVerbose.get(), Mth.floor(this.f_96541_.player.m_20185_()), Mth.floor(this.f_96541_.player.m_20189_()), Mth.floor(this.f_96541_.player.m_20191_().minY), Mth.floor((float) (this.f_96541_.player.m_146904_() >> 4)));
            this.currentLocation = Constants.getString("jm.waypoint.current_location", " " + pos);
            if (this.fieldList.isEmpty()) {
                Font fr = this.getFontRenderer();
                this.fieldName = new TextBox(this.originalWaypoint.getName(), fr, 160, 20);
                if (this.isNew) {
                    this.fieldName.setFocused(true);
                    this.fieldName.m_94201_();
                    this.fieldName.m_94208_(0);
                }
                this.fieldList.add(this.fieldName);
                int width9chars = this.getFontRenderer().width("-30000000") + 10;
                int width3chars = this.getFontRenderer().width("255") + 10;
                int width4chars = this.getFontRenderer().width("-255") + 10;
                int h = 20;
                this.fieldX = new TextBox(this.originalWaypoint.getX(), fr, width9chars, h, true, true);
                this.fieldX.setClamp(-30000000, 30000000);
                this.fieldList.add(this.fieldX);
                this.fieldZ = new TextBox(this.originalWaypoint.getZ(), fr, width9chars, h, true, true);
                this.fieldZ.setClamp(-30000000, 30000000);
                this.fieldList.add(this.fieldZ);
                int y = this.originalWaypoint.getY();
                this.fieldY = new TextBox(y, fr, width4chars, h, true, true);
                this.fieldY.setClamp(-99, this.f_96541_.level.m_151558_() - 1);
                this.fieldY.setMinLength(1);
                this.fieldList.add(this.fieldY);
                this.fieldR = new TextBox("", fr, width3chars, h, true, false);
                this.fieldR.setClamp(0, 255);
                this.fieldR.m_94199_(3);
                this.fieldList.add(this.fieldR);
                this.fieldG = new TextBox("", fr, width3chars, h, true, false);
                this.fieldG.setClamp(0, 255);
                this.fieldG.m_94199_(3);
                this.fieldList.add(this.fieldG);
                this.fieldB = new TextBox("", fr, width3chars, h, true, false);
                this.fieldB.setClamp(0, 255);
                this.fieldB.m_94199_(3);
                this.fieldList.add(this.fieldB);
                Collection<String> wpDims = this.originalWaypoint.getDimensions();
                for (WorldData.DimensionProvider provider : WorldData.getDimensionProviders(WaypointStore.INSTANCE.getLoadedDimensions())) {
                    String dimName = WorldData.getSafeDimensionName(provider);
                    String dimId = provider.getDimensionId();
                    try {
                        dimName = provider.getName();
                    } catch (Exception var16) {
                        JMLogger.throwLogOnce("Can't get dimension name from provider: ", var16);
                    }
                    WaypointEditor.DimensionButton button = new WaypointEditor.DimensionButton(dimId, dimName, wpDims.contains(dimId));
                    button.setDefaultStyle(false);
                    this.dimButtonList.add(button);
                }
                this.dimScrollPane = new ScrollPane(this, this.f_96541_, 0, 0, this.dimButtonList, ((WaypointEditor.DimensionButton) this.dimButtonList.get(0)).m_93694_(), 4);
                this.dimScrollPane.m_93471_(false);
            }
            if (this.getRenderables().isEmpty()) {
                String on = Constants.getString("jm.common.on");
                String off = Constants.getString("jm.common.off");
                String enableOn = Constants.getString("jm.waypoint.enable", on);
                String enableOff = Constants.getString("jm.waypoint.enable", off);
                this.buttonRandomize = (Button) this.m_142416_(new Button(Constants.getString("jm.waypoint.randomize"), buttonx -> this.setRandomColor()));
                this.buttonEnable = (OnOffButton) this.m_142416_(new OnOffButton(enableOn, enableOff, true, buttonx -> this.buttonEnable.toggle()));
                this.buttonEnable.setToggled(this.originalWaypoint.isEnable());
                this.buttonRemove = (Button) this.m_142416_(new Button(Constants.getString("jm.waypoint.remove"), buttonx -> this.remove()));
                this.buttonRemove.setEnabled(!this.isNew);
                this.buttonReset = (Button) this.m_142416_(new Button(Constants.getString("jm.waypoint.reset"), buttonx -> this.resetForm()));
                this.buttonSave = (Button) this.m_142416_(new Button(Constants.getString("jm.waypoint.save"), buttonx -> this.save()));
                String closeLabel = this.isNew ? "jm.waypoint.cancel" : "jm.common.close";
                this.buttonClose = (Button) this.m_142416_(new Button(Constants.getString(closeLabel), buttonx -> this.refreshAndClose(this.originalWaypoint)));
                this.getRenderables().add(this.buttonEnable);
                this.getRenderables().add(this.buttonRandomize);
                this.getRenderables().add(this.buttonRemove);
                this.getRenderables().add(this.buttonReset);
                this.getRenderables().add(this.buttonSave);
                this.getRenderables().add(this.buttonClose);
                this.buttonEnable.setDefaultStyle(false);
                this.buttonRandomize.setDefaultStyle(false);
                this.buttonRemove.setDefaultStyle(false);
                this.buttonReset.setDefaultStyle(false);
                this.buttonSave.setDefaultStyle(false);
                this.buttonClose.setDefaultStyle(false);
                this.bottomButtons = new ButtonList(this.buttonRemove, this.buttonSave, this.buttonClose);
                this.bottomButtons.equalizeWidths(this.getFontRenderer());
                this.setFormColor(this.originalWaypoint.getIconColor());
                this.validate();
            }
        } catch (Throwable var17) {
            Journeymap.getLogger().error(LogFormatter.toString(var17));
            UIManager.INSTANCE.closeAll();
        }
    }

    @Override
    protected void layoutButtons(GuiGraphics graphics) {
        try {
            this.init();
            Font fr = this.getFontRenderer();
            int vpad = 5;
            int hgap = fr.width("X") * 3;
            int vgap = this.fieldX.getHeight() + 5;
            int startY = Math.max(30, (this.f_96544_ - 200) / 2);
            int dcw = fr.width(this.dimensionsTitle);
            dcw = 8 + Math.max(dcw, this.dimScrollPane.getFitWidth(fr));
            int leftWidth = hgap * 2 + this.fieldX.getWidth() + this.fieldY.getWidth() + this.fieldZ.getWidth();
            int totalWidth = leftWidth + 10 + dcw;
            int leftX = (this.f_96543_ - totalWidth) / 2;
            int leftXEnd = leftX + leftWidth;
            int rightX = leftXEnd + 10;
            int rightXEnd = rightX + dcw;
            this.drawLabel(graphics, this.labelName, leftX, startY);
            int leftRowY = startY + 12;
            this.fieldName.setWidth(leftWidth);
            this.fieldName.setX(leftX);
            this.fieldName.setY(leftRowY);
            this.fieldName.render(graphics, leftX, leftRowY, this.partialTicks);
            leftRowY += vgap + 5;
            this.drawLabel(graphics, this.locationTitle, leftX, leftRowY);
            leftRowY += 12;
            this.drawLabelAndField(graphics, this.labelX, this.fieldX, leftX, leftRowY);
            this.drawLabelAndField(graphics, this.labelZ, this.fieldZ, this.fieldX.getX() + this.fieldX.getWidth() + hgap, leftRowY);
            this.drawLabelAndField(graphics, this.labelY, this.fieldY, this.fieldZ.getX() + this.fieldZ.getWidth() + hgap, leftRowY);
            leftRowY += vgap + 5;
            this.drawLabel(graphics, this.colorTitle, leftX, leftRowY);
            leftRowY += 12;
            this.drawLabelAndField(graphics, this.labelR, this.fieldR, leftX, leftRowY);
            this.drawLabelAndField(graphics, this.labelG, this.fieldG, this.fieldR.getX() + this.fieldR.getWidth() + hgap, leftRowY);
            this.drawLabelAndField(graphics, this.labelB, this.fieldB, this.fieldG.getX() + this.fieldG.getWidth() + hgap, leftRowY);
            this.buttonRandomize.setWidth(4 + Math.max(this.fieldB.getX() + this.fieldB.getWidth() - this.fieldR.getX(), 10 + fr.width(this.buttonRandomize.m_6035_().getString())));
            int var32;
            this.buttonRandomize.setScrollablePosition(this.fieldR.getX() - 2, var32 = leftRowY + vgap);
            int cpY = this.fieldB.getY();
            int cpSize = this.buttonRandomize.m_252907_() + this.buttonRandomize.m_93694_() - cpY - 2;
            int cpHAreaX = this.fieldB.getX() + this.fieldB.getWidth();
            int cpHArea = this.fieldName.getX() + this.fieldName.getWidth() - cpHAreaX;
            int cpX = cpHAreaX + (cpHArea - cpSize);
            this.drawColorPicker(graphics, cpX, cpY, (float) cpSize);
            int iconX = cpHAreaX + (cpX - cpHAreaX) / 2 - this.wpTexture.getWidth() / 2 + 1;
            int iconY = this.buttonRandomize.m_252907_() - 2;
            this.drawWaypoint(graphics.pose(), iconX, iconY);
            leftRowY = var32 + vgap;
            this.buttonEnable.fitWidth(fr);
            this.buttonEnable.m_93674_(Math.max(leftWidth / 2, this.buttonEnable.m_5711_()));
            this.buttonEnable.setScrollablePosition(leftX - 2, leftRowY);
            this.buttonReset.setWidth(leftWidth - this.buttonEnable.m_5711_() - 2);
            this.buttonReset.setScrollablePosition(leftXEnd - this.buttonReset.m_5711_() + 2, leftRowY);
            this.drawLabel(graphics, this.dimensionsTitle, rightX, startY);
            int rightRow = startY + 12;
            int scrollHeight = this.buttonReset.m_252907_() + this.buttonReset.m_93694_() - 2 - rightRow;
            this.dimScrollPane.setDimensions(dcw, scrollHeight, 0, 0, rightX, rightRow);
            int totalRow = Math.max(leftRowY + vgap, rightRow + vgap);
            this.bottomButtons.layoutFilledHorizontal(fr, leftX - 2, totalRow, rightXEnd + 2, 4, true);
        } catch (Throwable var26) {
            Journeymap.getLogger().error("Error during WaypointEditor layout: " + LogFormatter.toPartialString(var26));
            UIManager.INSTANCE.closeAll();
        }
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, float partialTicks) {
        try {
            this.partialTicks = partialTicks;
            super.renderBackground(graphics);
            super.renderBottomBar(graphics.pose());
            this.validate();
            this.layoutButtons(graphics);
            this.dimScrollPane.render(graphics, x, y, partialTicks);
            DrawUtil.drawLabel(graphics, this.currentLocation, (double) (this.f_96543_ / 2), (double) this.f_96544_, DrawUtil.HAlign.Center, DrawUtil.VAlign.Above, 0, 1.0F, 12632256, 1.0F, 1.0, true);
            for (int k = 0; k < this.getRenderables().size(); k++) {
                net.minecraft.client.gui.components.Button guibutton = (net.minecraft.client.gui.components.Button) this.getRenderables().get(k);
                guibutton.m_88315_(graphics, x, y, 0.0F);
            }
            if (this.colorPickTooltip != null && this.colorPickRect.contains((double) x, (double) y)) {
                Component tooltip = Constants.getStringTextComponent(this.colorPickTooltip);
                this.renderWrappedToolTip(graphics, this.getFontRenderer().split(tooltip, 200), x, y, this.getFontRenderer());
            }
            this.drawTitle(graphics);
            this.drawLogo(graphics.pose());
        } catch (Throwable var7) {
            Journeymap.getLogger().error("Error during WaypointEditor layout: " + LogFormatter.toPartialString(var7));
            UIManager.INSTANCE.closeAll();
        }
    }

    protected void drawWaypoint(PoseStack poseStack, int x, int y) {
        DrawUtil.drawColoredImage(poseStack, this.wpTexture, this.currentColor, 1.0F, (double) x, (double) (y - this.wpTexture.getHeight() / 2), 0.0);
    }

    protected void drawColorPicker(GuiGraphics graphics, int x, int y, float size) {
        int sizeI = (int) size;
        graphics.fill(x - 1, y - 1, x + sizeI + 1, y + sizeI + 1, -6250336);
        float scale = size / (float) this.colorPickTexture.getWidth();
        if (this.colorPickRect.width != (double) size) {
            ((SimpleTextureImpl) this.colorPickTexture).resize(scale);
        }
        this.colorPickRect.setRect((double) x, (double) y, (double) size, (double) size);
        DrawUtil.drawImage(graphics.pose(), this.colorPickTexture, (double) x, (double) y, false, 1.0F, 0.0);
    }

    protected void drawLabelAndField(GuiGraphics graphics, String label, TextBox field, int x, int y) {
        field.setX(x);
        field.setY(y);
        Font fr = this.getFontRenderer();
        int width = fr.width(label) + 4;
        graphics.drawString(this.getFontRenderer(), label, x - width, y + (field.getHeight() - 8) / 2, Color.cyan.getRGB());
        field.render(graphics, x, y, this.partialTicks);
    }

    protected void drawLabel(GuiGraphics graphics, String label, int x, int y) {
        graphics.drawString(this.getFontRenderer(), label, x, y, Color.cyan.getRGB());
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        switch(keyCode) {
            case 256:
                this.closeAndReturn();
                return true;
            case 257:
                this.save();
                return true;
            case 258:
                this.validate();
                this.onTab();
                return true;
            default:
                for (EditBox field : this.fieldList) {
                    field.keyPressed(keyCode, scanCode, modifiers);
                }
                this.updateWaypointFromForm();
                this.validate();
                return true;
        }
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        if (this.openedWithHotKey && this.isNew) {
            this.openedWithHotKey = false;
            return true;
        } else {
            for (EditBox field : this.fieldList) {
                field.charTyped(typedChar, keyCode);
            }
            this.updateWaypointFromForm();
            this.validate();
            return true;
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        this.dimScrollPane.m_6348_(mouseX, mouseY, mouseButton);
        for (EditBox field : this.fieldList) {
            field.m_6348_(mouseX, mouseY, mouseButton);
        }
        return super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double mouseDX, double mouseDY) {
        for (EditBox field : this.fieldList) {
            field.m_7979_(mouseX, mouseY, button, mouseDX, mouseDY);
        }
        this.checkColorPicker(mouseX, mouseY);
        this.dimScrollPane.mouseDragged(mouseX, mouseY, button, mouseDX, mouseDY);
        return super.m_7979_(mouseX, mouseY, button, mouseDX, mouseDY);
    }

    @Override
    public boolean mouseScrolled(double x, double y, double scroll) {
        this.dimScrollPane.m_6050_(x, y, scroll);
        return super.m_6050_(x, y, scroll);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (mouseButton == 0) {
            for (EditBox field : this.fieldList) {
                field.m_6375_(mouseX, mouseY, mouseButton);
            }
            this.checkColorPicker(mouseX, mouseY);
            this.dimScrollPane.m_6375_(mouseX, mouseY, mouseButton);
            Button button = this.dimScrollPane.mouseClicked((int) mouseX, (int) mouseY, mouseButton);
            if (button != null) {
                this.actionPerformed(button);
            }
        }
        return super.m_6375_(mouseX, mouseY, mouseButton);
    }

    protected void checkColorPicker(double mouseX, double mouseY) {
        if (this.colorPickRect.contains(mouseX, mouseY)) {
            int x = (int) (mouseX - (double) ((int) this.colorPickRect.x));
            int y = (int) (mouseY - (double) ((int) this.colorPickRect.y));
            this.setFormColor(this.colorPickTexture.getRGB(x, y));
        }
    }

    protected void setFormColor(Integer color) {
        this.currentColor = color;
        int[] c = RGB.ints(color);
        this.fieldR.m_94144_(Integer.toString(c[0]));
        this.fieldG.m_94144_(Integer.toString(c[1]));
        this.fieldB.m_94144_(Integer.toString(c[2]));
        this.updateWaypointFromForm();
    }

    protected void actionPerformed(net.minecraft.client.gui.components.Button guibutton) {
        if (this.dimButtonList.contains(guibutton)) {
            WaypointEditor.DimensionButton dimButton = (WaypointEditor.DimensionButton) guibutton;
            dimButton.toggle();
            this.updateWaypointFromForm();
        }
    }

    protected void setRandomColor() {
        this.editedWaypoint.setRandomColor();
        this.setFormColor(this.editedWaypoint.getIconColor());
    }

    protected void onTab() {
        boolean focusNext = false;
        boolean foundFocus = false;
        for (TextBox field : this.fieldList) {
            if (focusNext) {
                field.setFocused(true);
                foundFocus = true;
                break;
            }
            if (field.m_93696_()) {
                field.setFocused(false);
                field.clamp();
                focusNext = true;
            }
        }
        if (!foundFocus) {
            ((TextBox) this.fieldList.get(0)).setFocused(true);
        }
    }

    protected boolean validate() {
        boolean valid = true;
        if (this.fieldName != null) {
            valid = this.fieldName.hasMinLength() && Strings.isNotEmpty(this.fieldName.m_94155_());
        }
        if (valid && this.fieldY != null) {
            valid = this.fieldY.hasMinLength();
        }
        if (this.buttonSave != null) {
            this.buttonSave.setEnabled(valid && (this.isNew || !this.originalWaypoint.equals(this.editedWaypoint)));
        }
        return valid;
    }

    protected void remove() {
        WaypointStore.INSTANCE.remove(this.originalWaypoint, true);
        this.refreshAndClose(null);
    }

    protected void save() {
        if (this.validate()) {
            this.updateWaypointFromForm();
            WaypointStore.INSTANCE.remove(this.originalWaypoint, false);
            WaypointStore.INSTANCE.save(this.editedWaypoint, this.isNew);
            this.refreshAndClose(this.editedWaypoint);
        }
    }

    protected void resetForm() {
        this.editedWaypoint = new Waypoint(this.originalWaypoint);
        this.dimButtonList.clear();
        this.fieldList.clear();
        this.getRenderables().clear();
        this.init();
        this.validate();
    }

    protected void updateWaypointFromForm() {
        this.currentColor = RGB.toInteger(this.getSafeColorInt(this.fieldR), this.getSafeColorInt(this.fieldG), this.getSafeColorInt(this.fieldB));
        this.editedWaypoint.setColor(this.currentColor);
        this.fieldName.m_94202_(this.editedWaypoint.getSafeColor());
        ArrayList<String> dims = new ArrayList();
        for (WaypointEditor.DimensionButton db : this.dimButtonList) {
            if (db.getToggled()) {
                dims.add(db.dimension);
            }
        }
        this.editedWaypoint.setDimensions(dims);
        this.editedWaypoint.setEnable(this.buttonEnable.getToggled());
        this.editedWaypoint.setName(this.fieldName.m_94155_());
        this.editedWaypoint.setLocation(this.getSafeCoordInt(this.fieldX), this.getSafeCoordInt(this.fieldY), this.getSafeCoordInt(this.fieldZ), DimensionHelper.getDimKeyName(this.f_96541_.player));
    }

    protected int getSafeColorInt(TextBox field) {
        field.clamp();
        String text = field.m_94155_();
        if (text != null && !text.isEmpty()) {
            int val = 0;
            try {
                val = Integer.parseInt(text);
            } catch (NumberFormatException var5) {
            }
            return Math.max(0, Math.min(255, val));
        } else {
            return 0;
        }
    }

    protected int getSafeCoordInt(TextBox field) {
        String text = field.m_94155_();
        if (text != null && !text.isEmpty() && !text.equals("-")) {
            int val = 0;
            try {
                val = Integer.parseInt(text);
            } catch (NumberFormatException var5) {
            }
            return val;
        } else {
            return 0;
        }
    }

    protected void refreshAndClose(Waypoint focusWaypoint) {
        if (returnDisplayStack != null && returnDisplayStack.peek() != null && returnDisplayStack.peek() instanceof WaypointManager) {
            ((WaypointManager) returnDisplayStack.peek()).setFocusWaypoint(focusWaypoint);
        }
        Fullscreen.state().requireRefresh();
        this.closeAndReturn();
    }

    @Override
    protected void closeAndReturn() {
        if (returnDisplayStack != null && returnDisplayStack.peek() != null) {
            UIManager.INSTANCE.open((Screen) returnDisplayStack.pop());
        } else {
            UIManager.INSTANCE.closeAll();
        }
    }

    class DimensionButton extends OnOffButton {

        public final String dimension;

        DimensionButton(String dimension, String dimensionName, boolean toggled) {
            super(String.format("%s: %s", dimensionName, Constants.getString("jm.common.on")), String.format("%s: %s", dimensionName, Constants.getString("jm.common.off")), toggled, Button.emptyPressable());
            this.dimension = dimension;
            this.setToggled(Boolean.valueOf(toggled));
        }
    }
}