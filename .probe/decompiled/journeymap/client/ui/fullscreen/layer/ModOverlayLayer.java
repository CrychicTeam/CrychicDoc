package journeymap.client.ui.fullscreen.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import journeymap.client.api.display.IOverlayListener;
import journeymap.client.api.display.ModPopupMenu;
import journeymap.client.api.display.Overlay;
import journeymap.client.api.impl.ClientAPI;
import journeymap.client.api.impl.ModPopupMenuImpl;
import journeymap.client.api.util.UIState;
import journeymap.client.render.draw.DrawStep;
import journeymap.client.render.draw.OverlayDrawStep;
import journeymap.client.render.map.GridRenderer;
import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.common.Journeymap;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

public class ModOverlayLayer extends Layer {

    protected List<OverlayDrawStep> allDrawSteps = new ArrayList();

    protected List<OverlayDrawStep> visibleSteps = new ArrayList();

    protected List<OverlayDrawStep> touchedSteps = new ArrayList();

    protected BlockPos lastCoord;

    protected Double lastMousePosition;

    protected UIState lastUiState;

    protected boolean propagateClick;

    public ModOverlayLayer(Fullscreen fullscreen) {
        super(fullscreen);
    }

    private void ensureCurrent(Minecraft mc, GridRenderer gridRenderer, Double mousePosition, BlockPos blockCoord) {
        UIState currentUiState = gridRenderer.getUIState();
        boolean uiStateChange = !Objects.equals(this.lastUiState, currentUiState);
        if (uiStateChange || !Objects.equals(blockCoord, this.lastCoord) || this.lastMousePosition == null) {
            this.lastCoord = blockCoord;
            this.lastUiState = currentUiState;
            this.lastMousePosition = mousePosition;
            this.allDrawSteps.clear();
            ClientAPI.INSTANCE.getDrawSteps(this.allDrawSteps, currentUiState);
            this.updateOverlayState(gridRenderer, mousePosition, blockCoord, uiStateChange);
        }
    }

    @Override
    public List<DrawStep> onMouseMove(Minecraft mc, GridRenderer gridRenderer, Double mousePosition, BlockPos blockCoord, float fontScale, boolean isScrolling) {
        this.ensureCurrent(mc, gridRenderer, mousePosition, blockCoord);
        if (!this.touchedSteps.isEmpty()) {
            for (OverlayDrawStep overlayDrawStep : this.touchedSteps) {
                try {
                    Overlay overlay = overlayDrawStep.getOverlay();
                    IOverlayListener listener = overlay.getOverlayListener();
                    this.fireOnMouseMove(listener, mousePosition, blockCoord);
                    overlayDrawStep.setTitlePosition(mousePosition);
                } catch (Throwable var12) {
                    Journeymap.getLogger().error(var12.getMessage(), var12);
                }
            }
        }
        return Collections.emptyList();
    }

    @Override
    public List<DrawStep> onMouseClick(Minecraft mc, GridRenderer gridRenderer, Double mousePosition, BlockPos blockCoord, int button, boolean doubleClick, float fontScale) {
        this.ensureCurrent(mc, gridRenderer, mousePosition, blockCoord);
        this.propagateClick = true;
        if (!this.touchedSteps.isEmpty()) {
            for (OverlayDrawStep overlayDrawStep : this.touchedSteps) {
                try {
                    Overlay overlay = overlayDrawStep.getOverlay();
                    IOverlayListener listener = overlay.getOverlayListener();
                    if (listener != null) {
                        boolean continueClick = this.fireOnMouseClick(listener, mousePosition, blockCoord, button, doubleClick);
                        if (button == 1) {
                            this.fireOnMenuPopup(listener, mousePosition, blockCoord);
                        }
                        overlayDrawStep.setTitlePosition(mousePosition);
                        if (!continueClick) {
                            this.propagateClick = false;
                            break;
                        }
                    }
                } catch (Throwable var13) {
                    Journeymap.getLogger().error(var13.getMessage(), var13);
                }
            }
        }
        return Collections.emptyList();
    }

    @Override
    public boolean propagateClick() {
        return this.propagateClick;
    }

    private void updateOverlayState(GridRenderer gridRenderer, Double mousePosition, BlockPos blockCoord, boolean uiStateChange) {
        for (OverlayDrawStep overlayDrawStep : this.allDrawSteps) {
            Overlay overlay = overlayDrawStep.getOverlay();
            IOverlayListener listener = overlay.getOverlayListener();
            overlayDrawStep.setTitlePosition(null);
            boolean currentlyActive = this.visibleSteps.contains(overlayDrawStep);
            boolean currentlyTouched = this.touchedSteps.contains(overlayDrawStep);
            if (overlayDrawStep.isOnScreen(new PoseStack(), 0.0, 0.0, gridRenderer, 0.0)) {
                if (!currentlyActive) {
                    this.visibleSteps.add(overlayDrawStep);
                    this.fireActivate(listener);
                } else if (uiStateChange) {
                    this.fireActivate(listener);
                }
                java.awt.geom.Rectangle2D.Double bounds = overlayDrawStep.getBounds();
                if (bounds != null && bounds.contains(mousePosition)) {
                    if (!currentlyTouched) {
                        this.touchedSteps.add(overlayDrawStep);
                    }
                } else if (currentlyTouched) {
                    this.touchedSteps.remove(overlayDrawStep);
                    this.fireOnMouseOut(listener, mousePosition, blockCoord);
                }
            } else {
                if (currentlyTouched) {
                    this.touchedSteps.remove(overlayDrawStep);
                    this.fireOnMouseOut(listener, mousePosition, blockCoord);
                }
                if (currentlyActive) {
                    this.visibleSteps.remove(overlayDrawStep);
                    this.fireDeActivate(listener);
                }
            }
        }
    }

    private void fireActivate(IOverlayListener listener) {
        if (listener != null) {
            try {
                listener.onActivate(this.lastUiState);
            } catch (Throwable var3) {
                var3.printStackTrace();
            }
        }
    }

    private void fireDeActivate(IOverlayListener listener) {
        if (listener != null) {
            try {
                listener.onDeactivate(this.lastUiState);
            } catch (Throwable var3) {
                var3.printStackTrace();
            }
        }
    }

    private void fireOnMouseMove(IOverlayListener listener, Double mousePosition, BlockPos blockCoord) {
        if (listener != null) {
            try {
                listener.onMouseMove(this.lastUiState, mousePosition, blockCoord);
            } catch (Throwable var5) {
                var5.printStackTrace();
            }
        }
    }

    private void fireOnMenuPopup(IOverlayListener listener, Double mousePosition, BlockPos blockCoord) {
        if (listener != null) {
            try {
                ModPopupMenu menu = new ModPopupMenuImpl(this.fullscreen.popupMenu);
                listener.onOverlayMenuPopup(this.lastUiState, mousePosition, blockCoord, menu);
                this.fullscreen.popupMenu.displayOptions(blockCoord, menu);
            } catch (Throwable var5) {
                var5.printStackTrace();
            }
        }
    }

    private boolean fireOnMouseClick(IOverlayListener listener, Double mousePosition, BlockPos blockCoord, int button, boolean doubleClick) {
        if (listener != null) {
            try {
                return listener.onMouseClick(this.lastUiState, mousePosition, blockCoord, button, doubleClick);
            } catch (Throwable var7) {
                var7.printStackTrace();
            }
        }
        return true;
    }

    private void fireOnMouseOut(IOverlayListener listener, Double mousePosition, BlockPos blockCoord) {
        if (listener != null) {
            try {
                listener.onMouseOut(this.lastUiState, mousePosition, blockCoord);
            } catch (Throwable var5) {
                var5.printStackTrace();
            }
        }
    }
}