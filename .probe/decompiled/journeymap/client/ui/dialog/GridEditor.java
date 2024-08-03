package journeymap.client.ui.dialog;

import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Color;
import java.awt.geom.Rectangle2D.Double;
import java.util.EnumSet;
import journeymap.client.Constants;
import journeymap.client.JourneymapClient;
import journeymap.client.io.ThemeLoader;
import journeymap.client.model.GridSpec;
import journeymap.client.model.GridSpecs;
import journeymap.client.model.MapType;
import journeymap.client.render.draw.DrawUtil;
import journeymap.client.texture.SimpleTextureImpl;
import journeymap.client.texture.Texture;
import journeymap.client.texture.TextureCache;
import journeymap.client.ui.UIManager;
import journeymap.client.ui.component.Button;
import journeymap.client.ui.component.ButtonList;
import journeymap.client.ui.component.CheckBox;
import journeymap.client.ui.component.IntSliderButton;
import journeymap.client.ui.component.JmUI;
import journeymap.client.ui.component.PropertyDropdownButton;
import journeymap.client.ui.theme.Theme;
import journeymap.client.ui.theme.ThemeToggle;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import journeymap.common.properties.catagory.Category;
import journeymap.common.properties.config.EnumField;
import journeymap.common.properties.config.IntegerField;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class GridEditor extends JmUI {

    private final Texture colorPickTexture;

    private final int tileSize = 128;

    private final int sampleTextureSize = 128;

    private GridSpecs gridSpecs;

    private PropertyDropdownButton<GridSpec.Style> buttonStyle;

    private IntSliderButton buttonOpacity;

    private CheckBox checkDay;

    private CheckBox checkNight;

    private CheckBox checkUnderground;

    private ThemeToggle buttonDay;

    private ThemeToggle buttonNight;

    private ThemeToggle buttonUnderground;

    private Integer activeColor;

    private MapType activeMapType;

    private Button buttonReset;

    private Button buttonCancel;

    private Button buttonClose;

    private Double colorPickRect;

    private ButtonList topButtons;

    private ButtonList leftButtons;

    private ButtonList leftChecks;

    private ButtonList bottomButtons;

    private ResourceLocation colorPicResource = Constants.birthdayMessage() == null ? TextureCache.ColorPicker : TextureCache.ColorPicker2;

    public GridEditor(JmUI returnDisplay) {
        super(Constants.getString("jm.common.grid_editor"), returnDisplay);
        this.colorPickTexture = TextureCache.getTexture(this.colorPicResource);
        this.colorPickRect = new Double(0.0, 0.0, (double) this.colorPickTexture.getWidth(), (double) this.colorPickTexture.getHeight());
        this.gridSpecs = JourneymapClient.getInstance().getCoreProperties().gridSpecs.clone();
        this.activeMapType = MapType.day(Level.OVERWORLD);
        this.activeColor = this.gridSpecs.getSpec(this.activeMapType).getColor();
    }

    @Override
    public void init() {
        try {
            this.setRenderBottomBar(true);
            if (this.getRenderables().isEmpty()) {
                GridSpec spec = this.gridSpecs.getSpec(this.activeMapType);
                this.buttonStyle = new PropertyDropdownButton<>(EnumSet.allOf(GridSpec.Style.class), Constants.getString("jm.common.grid_style"), new EnumField<>(Category.Hidden, "", spec.style), b -> this.updateGridSpecs());
                this.m_142416_(this.buttonStyle);
                this.buttonStyle.setDefaultStyle(false);
                this.buttonStyle.setDrawBackground(false);
                this.buttonOpacity = new IntSliderButton(new IntegerField(Category.Hidden, "", 0, 100, (int) Math.ceil((double) (spec.alpha * 100.0F))), Constants.getString("jm.common.grid_opacity") + " : ", "", true);
                this.m_142416_(this.buttonOpacity);
                this.buttonOpacity.setDefaultStyle(false);
                this.buttonOpacity.setDrawBackground(false);
                this.topButtons = new ButtonList(this.buttonStyle, this.buttonOpacity);
                this.topButtons.equalizeWidths(this.getFontRenderer());
                this.checkDay = new CheckBox("", this.activeMapType == MapType.day(Level.OVERWORLD), b -> this.updatePreview(MapType.day(Level.OVERWORLD)));
                this.m_142416_(this.checkDay);
                this.checkNight = new CheckBox("", this.activeMapType == MapType.night(Level.OVERWORLD), b -> this.updatePreview(MapType.night(Level.OVERWORLD)));
                this.m_142416_(this.checkNight);
                this.checkUnderground = new CheckBox("", this.activeMapType.isUnderground(), b -> this.updatePreview(MapType.underground(0, Level.OVERWORLD)));
                this.m_142416_(this.checkUnderground);
                this.leftChecks = new ButtonList(this.checkDay, this.checkNight, this.checkUnderground);
                Theme theme = ThemeLoader.getCurrentTheme();
                this.buttonDay = new ThemeToggle(theme, "jm.fullscreen.map_day", "day", b -> this.updatePreview(MapType.day(Level.OVERWORLD)));
                this.m_142416_(this.buttonDay);
                this.buttonNight = new ThemeToggle(theme, "jm.fullscreen.map_night", "night", b -> this.updatePreview(MapType.night(Level.OVERWORLD)));
                this.m_142416_(this.buttonNight);
                this.buttonUnderground = new ThemeToggle(theme, "jm.fullscreen.map_caves", "caves", b -> this.updatePreview(MapType.underground(0, Level.OVERWORLD)));
                this.m_142416_(this.buttonUnderground);
                this.leftButtons = new ButtonList(this.buttonDay, this.buttonNight, this.buttonUnderground);
                this.buttonReset = new Button(Constants.getString("jm.waypoint.reset"), b -> this.resetGridSpecs());
                this.m_142416_(this.buttonReset);
                this.buttonReset.setDefaultStyle(false);
                this.buttonCancel = new Button(Constants.getString("jm.waypoint.cancel"), b -> {
                    this.resetGridSpecs();
                    this.closeAndReturn();
                });
                this.buttonCancel.setDefaultStyle(false);
                this.m_142416_(this.buttonCancel);
                this.buttonClose = new Button(Constants.getString("jm.waypoint.save"), b -> this.saveAndClose());
                this.buttonClose.setDefaultStyle(false);
                this.m_142416_(this.buttonClose);
                this.bottomButtons = new ButtonList(this.buttonReset, this.buttonCancel, this.buttonClose);
                this.bottomButtons.equalizeWidths(this.getFontRenderer());
                this.getRenderables().addAll(this.topButtons);
                this.getRenderables().addAll(this.leftChecks);
                this.getRenderables().addAll(this.leftButtons);
                this.getRenderables().addAll(this.bottomButtons);
                this.updatePreview(this.activeMapType);
            }
        } catch (Throwable var3) {
            Journeymap.getLogger().error(LogFormatter.toString(var3));
            UIManager.INSTANCE.closeAll();
        }
    }

    @Override
    protected void layoutButtons(GuiGraphics graphics) {
        try {
            this.init();
            int hgap = 6;
            int vgap = 6;
            int startY = Math.max(40, (this.f_96544_ - 230) / 2);
            int centerX = this.f_96543_ / 2;
            int cpSize = this.topButtons.getHeight(6);
            int topRowWidth = 6 + cpSize + ((Button) this.topButtons.get(0)).m_5711_();
            int topRowLeft = centerX - topRowWidth / 2;
            this.topButtons.layoutVertical(topRowLeft + 6 + cpSize, startY, true, 6);
            this.drawColorPicker(graphics, topRowLeft, this.topButtons.getTopY(), (float) cpSize);
            int tileX = centerX - 64;
            int tileY = this.topButtons.getBottomY() + 12;
            this.drawMapTile(graphics, tileX, tileY);
            this.leftButtons.layoutVertical(tileX - ((Button) this.leftButtons.get(0)).m_5711_() - 6, tileY + 6, true, 6);
            this.leftChecks.setHeights(((Button) this.leftButtons.get(0)).m_93694_());
            this.leftChecks.setWidths(15);
            this.leftChecks.layoutVertical(this.leftButtons.getLeftX() - this.checkDay.m_5711_(), this.leftButtons.getTopY(), true, 6);
            int bottomY = Math.min(tileY + 128 + 12, this.f_96544_ - 10 - this.buttonClose.m_93694_());
            this.bottomButtons.equalizeWidths(this.getFontRenderer(), 6, ((Button) this.topButtons.get(0)).getRightX() - topRowLeft);
            this.bottomButtons.layoutCenteredHorizontal(centerX, bottomY, true, 6);
        } catch (Throwable var12) {
            this.logger.error("Error in GridEditor.layoutButtons: " + LogFormatter.toString(var12));
        }
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, float par3) {
        try {
            this.m_280273_(graphics);
            this.renderBottomBar(graphics.pose());
            this.layoutButtons(graphics);
            for (int k = 0; k < this.getRenderables().size(); k++) {
                net.minecraft.client.gui.components.Button guibutton = (net.minecraft.client.gui.components.Button) this.getRenderables().get(k);
                guibutton.m_88315_(graphics, x, y, 0.0F);
            }
            this.drawTitle(graphics);
            this.drawLogo(graphics.pose());
        } catch (Throwable var7) {
            this.logger.error("Error in GridEditor.render: " + LogFormatter.toString(var7));
        }
    }

    protected void drawColorPicker(GuiGraphics graphics, int x, int y, float size) {
        int sizeI = (int) size;
        graphics.fill(x - 1, y - 1, x + sizeI + 1, y + sizeI + 1, -6250336);
        float scale = size / (float) this.colorPickTexture.getWidth();
        if (this.colorPickRect.width != (double) size) {
            ((SimpleTextureImpl) this.colorPickTexture).resize(scale);
        }
        this.colorPickRect.setRect((double) x, (double) y, (double) size, (double) size);
        DrawUtil.drawImage(graphics.pose(), this.colorPickTexture, (double) x, (double) y, false, scale, 0.0);
        GridSpec activeSpec = this.gridSpecs.getSpec(this.activeMapType);
        int colorX = activeSpec.getColorX();
        int colorY = activeSpec.getColorY();
        if (colorX > 0 && colorY > 0) {
            colorX += x;
            colorY += y;
            DrawUtil.drawRectangle(graphics.pose(), (double) (colorX - 2), (double) (colorY - 2), 5.0, 5.0, Color.darkGray.getRGB(), 0.8F);
            DrawUtil.drawRectangle(graphics.pose(), (double) (colorX - 1), (double) colorY, 3.0, 1.0, this.activeColor, 1.0F);
            DrawUtil.drawRectangle(graphics.pose(), (double) colorX, (double) (colorY - 1), 1.0, 3.0, this.activeColor, 1.0F);
        }
    }

    protected void drawMapTile(GuiGraphics graphics, int x, int y) {
        float scale = 1.0F;
        graphics.fill(x - 1, y - 1, x + 128 + 1, y + 128 + 1, -6250336);
        Texture tileTex = this.getTileSample(this.activeMapType);
        DrawUtil.drawImage(graphics.pose(), tileTex, (double) x, (double) y, false, 1.0F, 0.0);
        if (scale == 2.0F) {
            DrawUtil.drawImage(graphics.pose(), tileTex, (double) (x + 128), (double) y, true, 1.0F, 0.0);
            DrawUtil.drawImage(graphics.pose(), tileTex, (double) x, (double) (y + 128), true, 1.0F, 180.0);
            DrawUtil.drawImage(graphics.pose(), tileTex, (double) (x + 128), (double) (y + 128), false, 1.0F, 180.0);
        }
        GridSpec gridSpec = this.gridSpecs.getSpec(this.activeMapType);
        gridSpec.beginTexture(9728, 33071, 1.0F);
        DrawUtil.drawBoundTexture(graphics.pose(), 0.0, 0.0, (double) x, (double) y, 0.0, 0.25, 0.25, (double) (x + 128), (double) (y + 128));
        gridSpec.finishTexture();
    }

    protected void drawLabel(GuiGraphics graphics, String label, int x, int y) {
        graphics.drawString(this.getFontRenderer(), label, x, y, Color.cyan.getRGB());
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        try {
            switch(keyCode) {
                case 256:
                    this.closeAndReturn();
                    return true;
                case 257:
                    this.saveAndClose();
                    return true;
            }
        } catch (Throwable var4) {
            this.logger.error("Error in GridEditor.keyTyped: " + LogFormatter.toString(var4));
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double mouseDX, double mouseDY) {
        try {
            if (this.buttonOpacity.dragging) {
                this.updateGridSpecs();
            } else {
                this.checkColorPicker(mouseX, mouseY);
            }
        } catch (Throwable var11) {
            this.logger.error("Error in GridEditor.mouseClickMove: " + LogFormatter.toString(var11));
        }
        return super.m_7979_(mouseX, mouseY, button, mouseDX, mouseDY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        try {
            super.m_6375_(mouseX, mouseY, mouseButton);
            if (mouseButton == 0) {
                this.checkColorPicker(mouseX, mouseY);
            }
        } catch (Throwable var7) {
            this.logger.error("Error in GridEditor.mouseClicked: " + LogFormatter.toString(var7));
        }
        return true;
    }

    protected void checkColorPicker(double mouseX, double mouseY) {
        if (this.colorPickRect.contains(mouseX, mouseY)) {
            int x = (int) (mouseX - (double) ((int) this.colorPickRect.x));
            int y = (int) (mouseY - (double) ((int) this.colorPickRect.y));
            this.activeColor = this.colorPickTexture.getRGB(x, y);
            GridSpec activeSpec = this.gridSpecs.getSpec(this.activeMapType);
            activeSpec.setColorCoords(x, y);
            this.updateGridSpecs();
        }
    }

    protected void updatePreview(MapType mapType) {
        this.activeMapType = mapType;
        GridSpec activeSpec = this.gridSpecs.getSpec(this.activeMapType);
        this.activeColor = activeSpec.getColor();
        this.buttonOpacity.setValue((int) (activeSpec.alpha * 100.0F));
        this.buttonStyle.setValue(activeSpec.style);
        this.checkDay.setToggled(Boolean.valueOf(mapType.isDay()));
        this.checkNight.setToggled(Boolean.valueOf(mapType.isNight()));
        this.checkUnderground.setToggled(Boolean.valueOf(mapType.isUnderground()));
        this.buttonDay.setToggled(Boolean.valueOf(mapType.isDay()));
        this.buttonNight.setToggled(Boolean.valueOf(mapType.isNight()));
        this.buttonUnderground.setToggled(Boolean.valueOf(mapType.isUnderground()));
    }

    protected void updateGridSpecs() {
        GridSpec activeSpec = this.gridSpecs.getSpec(this.activeMapType);
        int colorX = activeSpec.getColorX();
        int colorY = activeSpec.getColorY();
        GridSpec newSpec = new GridSpec(this.buttonStyle.getField().get(), new Color(this.activeColor), (float) this.buttonOpacity.getValue() / 100.0F).setColorCoords(colorX, colorY);
        if (this.checkDay.getToggled()) {
            this.gridSpecs.setSpec(MapType.day(Level.OVERWORLD), newSpec);
        }
        if (this.checkNight.getToggled()) {
            this.gridSpecs.setSpec(MapType.night(Level.OVERWORLD), newSpec);
        }
        if (this.checkUnderground.getToggled()) {
            this.gridSpecs.setSpec(MapType.underground(0, Level.OVERWORLD), newSpec);
        }
    }

    protected void saveAndClose() {
        this.updateGridSpecs();
        JourneymapClient.getInstance().getCoreProperties().gridSpecs.updateFrom(this.gridSpecs);
        JourneymapClient.getInstance().getCoreProperties().save();
        this.closeAndReturn();
    }

    protected void resetGridSpecs() {
        if (this.checkDay.getToggled()) {
            this.gridSpecs.setSpec(MapType.day(Level.OVERWORLD), GridSpecs.DEFAULT_DAY.clone());
        }
        if (this.checkNight.getToggled()) {
            this.gridSpecs.setSpec(MapType.night(Level.OVERWORLD), GridSpecs.DEFAULT_NIGHT.clone());
        }
        if (this.checkUnderground.getToggled()) {
            this.gridSpecs.setSpec(MapType.underground(0, Level.OVERWORLD), GridSpecs.DEFAULT_UNDERGROUND.clone());
        }
        this.m_6702_().clear();
        this.getRenderables().clear();
        this.init();
    }

    @Override
    protected void closeAndReturn() {
        if (returnDisplayStack != null && returnDisplayStack.peek() != null) {
            UIManager.INSTANCE.open((Screen) returnDisplayStack.pop());
        } else {
            UIManager.INSTANCE.closeAll();
        }
    }

    public Texture getTileSample(MapType mapType) {
        if (mapType.isNight()) {
            return TextureCache.getTexture(TextureCache.TileSampleNight);
        } else {
            return mapType.isUnderground() ? TextureCache.getTexture(TextureCache.TileSampleUnderground) : TextureCache.getTexture(TextureCache.TileSampleDay);
        }
    }

    @Override
    protected void renderBottomBar(PoseStack stack) {
        DrawUtil.drawRectangle(stack, 0.0, (double) (this.f_96544_ - 30), (double) this.f_96543_, (double) this.f_96544_, 0, 0.6F);
    }
}