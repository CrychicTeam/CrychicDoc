package journeymap.client.ui.fullscreen.layer;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import journeymap.client.Constants;
import journeymap.client.JourneymapClient;
import journeymap.client.io.ThemeLoader;
import journeymap.client.properties.FullMapProperties;
import journeymap.client.render.RenderWrapper;
import journeymap.client.render.draw.DrawStep;
import journeymap.client.render.draw.DrawUtil;
import journeymap.client.render.map.GridRenderer;
import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.client.ui.theme.Theme;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Tuple;

public class KeybindingInfoLayer extends Layer {

    private final List<DrawStep> drawStepList = new ArrayList(1);

    private Font fontRenderer = Minecraft.getInstance().font;

    private final KeybindingInfoLayer.KeybindingInfoStep keybindingInfoStep;

    private FullMapProperties fullMapProperties = JourneymapClient.getInstance().getFullMapProperties();

    private final Minecraft mc = Minecraft.getInstance();

    public KeybindingInfoLayer(Fullscreen fullscreen) {
        super(fullscreen);
        this.keybindingInfoStep = new KeybindingInfoLayer.KeybindingInfoStep();
        this.drawStepList.add(this.keybindingInfoStep);
    }

    @Override
    public List<DrawStep> onMouseMove(Minecraft mc, GridRenderer gridRenderer, Double mousePosition, BlockPos blockPos, float fontScale, boolean isScrolling) {
        if (this.fullMapProperties.showKeys.get()) {
            if (this.keybindingInfoStep.panelRect.contains(mousePosition)) {
                this.keybindingInfoStep.hide();
            } else {
                this.keybindingInfoStep.show();
            }
            return this.drawStepList;
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<DrawStep> onMouseClick(Minecraft mc, GridRenderer gridRenderer, Double mousePosition, BlockPos blockCoord, int button, boolean doubleClick, float fontScale) {
        return this.fullMapProperties.showKeys.get() ? this.drawStepList : Collections.EMPTY_LIST;
    }

    @Override
    public boolean propagateClick() {
        return true;
    }

    class KeybindingInfoStep implements DrawStep {

        private double screenWidth;

        private double screenHeight;

        private double fontScale;

        private int pad;

        private ArrayList<Tuple<String, String>> lines;

        private int keyNameWidth = 0;

        private int keyDescWidth = 0;

        private int lineHeight = 0;

        Rectangle2D panelRect = new java.awt.geom.Rectangle2D.Double();

        Theme theme = ThemeLoader.getCurrentTheme();

        Theme.LabelSpec statusLabelSpec;

        int bgColor;

        float fgAlphaDefault = 1.0F;

        float bgAlphaDefault = 0.7F;

        float fgAlpha = this.fgAlphaDefault;

        float bgAlpha = this.bgAlphaDefault;

        @Override
        public void draw(GuiGraphics graphics, MultiBufferSource buffers, DrawStep.Pass pass, double xOffset, double yOffset, GridRenderer gridRenderer, double fontScale, double rotation) {
            if (pass == DrawStep.Pass.Text) {
                if (KeybindingInfoLayer.this.fullscreen.getMenuToolbarBounds() != null) {
                    this.updateLayout(gridRenderer, fontScale);
                    DrawUtil.drawRectangle(graphics.pose(), this.panelRect.getX(), this.panelRect.getY(), this.panelRect.getWidth(), this.panelRect.getHeight(), this.bgColor, this.bgAlpha);
                    int x = (int) this.panelRect.getX() + this.pad + this.keyNameWidth;
                    int y = (int) this.panelRect.getY() + this.pad;
                    int firstColor = this.theme.fullscreen.statusLabel.highlight.getColor();
                    int secondColor = this.theme.fullscreen.statusLabel.foreground.getColor();
                    try {
                        RenderWrapper.enableBlend();
                        for (Tuple<String, String> line : this.lines) {
                            DrawUtil.drawLabel(graphics, line.getA(), (double) x, (double) y, DrawUtil.HAlign.Left, DrawUtil.VAlign.Middle, null, 0.0F, firstColor, this.fgAlpha, fontScale, false);
                            DrawUtil.drawLabel(graphics, line.getB(), (double) (x + this.pad), (double) y, DrawUtil.HAlign.Right, DrawUtil.VAlign.Middle, null, 0.0F, secondColor, this.fgAlpha, fontScale, false);
                            y += this.lineHeight;
                        }
                    } finally {
                        RenderWrapper.disableBlend();
                    }
                }
            }
        }

        @Override
        public int getDisplayOrder() {
            return 0;
        }

        @Override
        public String getModId() {
            return "journeymap";
        }

        void hide() {
            this.bgAlpha = 0.2F;
            this.fgAlpha = 0.2F;
        }

        void show() {
            this.bgAlpha = this.bgAlphaDefault;
            this.fgAlpha = this.fgAlphaDefault;
        }

        private void updateLayout(GridRenderer gridRenderer, double fontScale) {
            Theme theme = ThemeLoader.getCurrentTheme();
            this.statusLabelSpec = theme.fullscreen.statusLabel;
            this.bgColor = this.statusLabelSpec.background.getColor();
            if (fontScale != this.fontScale || this.screenWidth != (double) gridRenderer.getWidth() || this.screenHeight != (double) gridRenderer.getHeight()) {
                this.screenWidth = (double) gridRenderer.getWidth();
                this.screenHeight = (double) gridRenderer.getHeight();
                this.fontScale = fontScale;
                this.pad = (int) (10.0 * fontScale);
                this.lineHeight = (int) (3.0 + fontScale * 9.0);
                this.initLines(fontScale);
                int panelWidth = this.keyNameWidth + this.keyDescWidth + 4 * this.pad;
                int panelHeight = this.lines.size() * this.lineHeight + this.pad;
                double scaleFactor = KeybindingInfoLayer.this.fullscreen.getScaleFactor();
                double panelX = this.screenWidth - (double) theme.container.toolbar.vertical.margin * scaleFactor - (double) panelWidth;
                double panelY = this.screenHeight - (double) theme.container.toolbar.horizontal.margin * scaleFactor - (double) panelHeight;
                this.panelRect.setRect(panelX, panelY, (double) panelWidth, (double) panelHeight);
                java.awt.geom.Rectangle2D.Double menuToolbarRect = KeybindingInfoLayer.this.fullscreen.getMenuToolbarBounds();
                if (menuToolbarRect != null && menuToolbarRect.intersects(this.panelRect) && panelX <= menuToolbarRect.getMaxX()) {
                    panelY = (double) ((int) menuToolbarRect.getMinY() - 5 - panelHeight);
                    this.panelRect.setRect(panelX, panelY, (double) panelWidth, (double) panelHeight);
                }
            }
        }

        private void initLines(double fontScale) {
            this.lines = new ArrayList();
            this.keyDescWidth = 0;
            this.keyNameWidth = 0;
            this.bgAlpha = this.fgAlphaDefault;
            this.bgAlpha = this.bgAlphaDefault;
            for (KeyMapping keyBinding : JourneymapClient.getInstance().getKeyEvents().getHandler().getInGuiKeybindings()) {
                this.initLine(keyBinding, fontScale);
            }
            this.initLine(KeybindingInfoLayer.this.mc.options.keyChat, fontScale);
        }

        private void initLine(KeyMapping keyBinding, double fontScale) {
            String keyName = keyBinding.getTranslatedKeyMessage().getString().toUpperCase();
            String keyDesc = Constants.getString(keyBinding.getName());
            Tuple<String, String> line = new Tuple<>(keyName, keyDesc);
            this.lines.add(line);
            this.keyNameWidth = (int) Math.max((double) this.keyNameWidth, fontScale * (double) KeybindingInfoLayer.this.fontRenderer.width(keyName));
            this.keyDescWidth = (int) Math.max((double) this.keyDescWidth, fontScale * (double) KeybindingInfoLayer.this.fontRenderer.width(keyDesc));
        }
    }
}