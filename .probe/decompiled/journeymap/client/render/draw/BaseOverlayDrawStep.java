package journeymap.client.render.draw;

import com.google.common.base.Strings;
import com.mojang.blaze3d.vertex.PoseStack;
import info.journeymap.shaded.org.jetbrains.annotations.Nullable;
import java.awt.geom.Rectangle2D.Double;
import java.util.Objects;
import journeymap.client.api.display.Context;
import journeymap.client.api.display.Overlay;
import journeymap.client.api.model.TextProperties;
import journeymap.client.api.util.UIState;
import journeymap.client.render.map.GridRenderer;
import net.minecraft.client.renderer.MultiBufferSource;

public abstract class BaseOverlayDrawStep<T extends Overlay> implements OverlayDrawStep {

    public final T overlay;

    protected Double screenBounds = new Double();

    protected java.awt.geom.Point2D.Double titlePosition = null;

    protected java.awt.geom.Point2D.Double labelPosition = new java.awt.geom.Point2D.Double();

    protected UIState lastUiState = null;

    protected boolean dragging = false;

    protected boolean enabled = true;

    protected String[] labelLines;

    protected String[] titleLines;

    protected BaseOverlayDrawStep(T overlay) {
        this.overlay = overlay;
    }

    protected abstract void updatePositions(PoseStack var1, GridRenderer var2, double var3);

    protected void drawText(PoseStack poseStack, MultiBufferSource buffers, DrawStep.Pass pass, double xOffset, double yOffset, GridRenderer gridRenderer, double fontScale, double rotation) {
        TextProperties textProperties = this.overlay.getTextProperties();
        if (textProperties.isActiveIn(gridRenderer.getUIState())) {
            if (pass == DrawStep.Pass.Text) {
                if (this.labelPosition != null) {
                    if (this.labelLines == null) {
                        this.updateTextFields();
                    }
                    if (this.labelLines != null) {
                        double x = this.labelPosition.x + xOffset;
                        double y = this.labelPosition.y + yOffset;
                        DrawUtil.drawLabels(poseStack, buffers, this.labelLines, x, y, DrawUtil.HAlign.Center, DrawUtil.VAlign.Middle, textProperties.getBackgroundColor(), textProperties.getBackgroundOpacity(), textProperties.getColor(), textProperties.getOpacity(), (double) textProperties.getScale() * fontScale, textProperties.hasFontShadow(), rotation);
                    }
                }
            } else if (pass == DrawStep.Pass.Tooltip && gridRenderer.getUIState().ui != Context.UI.Minimap && this.titlePosition != null) {
                if (this.titleLines == null) {
                    this.updateTextFields();
                }
                if (this.titleLines != null) {
                    double x = this.titlePosition.x + 5.0 + xOffset;
                    double y = this.titlePosition.y + yOffset;
                    DrawUtil.drawLabels(poseStack, buffers, this.titleLines, x, y, DrawUtil.HAlign.Right, DrawUtil.VAlign.Above, textProperties.getBackgroundColor(), textProperties.getBackgroundOpacity(), textProperties.getColor(), textProperties.getOpacity(), (double) textProperties.getScale() * fontScale, textProperties.hasFontShadow(), rotation);
                }
            }
        }
    }

    @Override
    public boolean isOnScreen(PoseStack poseStack, double xOffset, double yOffset, GridRenderer gridRenderer, double rotation) {
        if (!this.enabled) {
            return false;
        } else {
            UIState uiState = gridRenderer.getUIState();
            if (!this.overlay.isActiveIn(uiState)) {
                return false;
            } else {
                boolean draggingDone = false;
                if (xOffset == 0.0 && yOffset == 0.0) {
                    draggingDone = this.dragging;
                    this.dragging = false;
                } else {
                    this.dragging = true;
                }
                if (draggingDone || uiState.ui == Context.UI.Minimap || this.overlay.getNeedsRerender() || !Objects.equals(uiState, this.lastUiState)) {
                    this.lastUiState = uiState;
                    this.updatePositions(poseStack, gridRenderer, rotation);
                    this.overlay.clearFlagForRerender();
                }
                return this.screenBounds == null ? false : gridRenderer.isOnScreen(this.screenBounds);
            }
        }
    }

    protected void updateTextFields() {
        if (this.labelPosition != null) {
            String labelText = this.overlay.getLabel();
            if (!Strings.isNullOrEmpty(labelText)) {
                this.labelLines = labelText.split("\n");
            } else {
                this.labelLines = null;
            }
        }
        if (this.titlePosition != null) {
            String titleText = this.overlay.getTitle();
            if (!Strings.isNullOrEmpty(titleText)) {
                this.titleLines = titleText.split("\n");
            } else {
                this.titleLines = null;
            }
        }
    }

    @Override
    public void setTitlePosition(@Nullable java.awt.geom.Point2D.Double titlePosition) {
        this.titlePosition = titlePosition;
    }

    @Override
    public int getDisplayOrder() {
        return this.overlay.getDisplayOrder();
    }

    @Override
    public String getModId() {
        return this.overlay.getModId();
    }

    @Override
    public Double getBounds() {
        return this.screenBounds;
    }

    @Override
    public Overlay getOverlay() {
        return this.overlay;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}