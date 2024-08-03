package journeymap.client.api.display;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import java.util.EnumSet;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import journeymap.client.api.model.TextProperties;
import journeymap.client.api.util.UIState;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

@ParametersAreNonnullByDefault
public abstract class Overlay extends Displayable {

    protected String overlayGroupName;

    protected String title;

    protected String label;

    protected ResourceKey<Level> dimension;

    protected int minZoom = 0;

    protected int maxZoom = 8;

    protected int displayOrder;

    protected EnumSet<Context.UI> activeUIs = EnumSet.of(Context.UI.Any);

    protected EnumSet<Context.MapType> activeMapTypes = EnumSet.of(Context.MapType.Any);

    protected TextProperties textProperties = new TextProperties();

    protected IOverlayListener overlayListener;

    protected boolean needsRerender = true;

    Overlay(String modId, String displayId) {
        super(modId, displayId);
    }

    public ResourceKey<Level> getDimension() {
        return this.dimension;
    }

    public Overlay setDimension(ResourceKey<Level> dimension) {
        this.dimension = dimension;
        return this;
    }

    public String getOverlayGroupName() {
        return this.overlayGroupName;
    }

    public Overlay setOverlayGroupName(String overlayGroupName) {
        this.overlayGroupName = overlayGroupName;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public Overlay setTitle(@Nullable String title) {
        this.title = title;
        return this;
    }

    public String getLabel() {
        return this.label;
    }

    public Overlay setLabel(@Nullable String label) {
        this.label = label;
        return this;
    }

    public int getMinZoom() {
        return this.minZoom;
    }

    public Overlay setMinZoom(int minZoom) {
        this.minZoom = Math.max(0, minZoom);
        return this;
    }

    public int getMaxZoom() {
        return this.maxZoom;
    }

    public Overlay setMaxZoom(int maxZoom) {
        this.maxZoom = Math.min(8, maxZoom);
        return this;
    }

    @Override
    public int getDisplayOrder() {
        return this.displayOrder;
    }

    public Overlay setDisplayOrder(int zIndex) {
        this.displayOrder = zIndex;
        return this;
    }

    public TextProperties getTextProperties() {
        return this.textProperties;
    }

    public Overlay setTextProperties(TextProperties textProperties) {
        this.textProperties = textProperties;
        return this;
    }

    public EnumSet<Context.UI> getActiveUIs() {
        return this.activeUIs;
    }

    public Overlay setActiveUIs(EnumSet<Context.UI> activeUIs) {
        if (activeUIs.contains(Context.UI.Any)) {
            activeUIs = EnumSet.of(Context.UI.Any);
        }
        this.activeUIs = activeUIs;
        return this;
    }

    public EnumSet<Context.MapType> getActiveMapTypes() {
        return this.activeMapTypes;
    }

    public Overlay setActiveMapTypes(EnumSet<Context.MapType> activeMapTypes) {
        if (activeMapTypes.contains(Context.MapType.Any)) {
            activeMapTypes = EnumSet.of(Context.MapType.Any);
        }
        this.activeMapTypes = activeMapTypes;
        return this;
    }

    public boolean isActiveIn(UIState uiState) {
        return uiState.active && this.dimension == uiState.dimension && (this.activeUIs.contains(Context.UI.Any) || this.activeUIs.contains(uiState.ui)) && (this.activeMapTypes.contains(Context.MapType.Any) || this.activeMapTypes.contains(uiState.mapType)) && this.minZoom <= uiState.zoom && this.maxZoom >= uiState.zoom;
    }

    public IOverlayListener getOverlayListener() {
        return this.overlayListener;
    }

    public Overlay setOverlayListener(@Nullable IOverlayListener overlayListener) {
        this.overlayListener = overlayListener;
        return this;
    }

    public void flagForRerender() {
        this.needsRerender = true;
    }

    public void clearFlagForRerender() {
        this.needsRerender = false;
    }

    public boolean getNeedsRerender() {
        return this.needsRerender;
    }

    protected final ToStringHelper toStringHelper(Overlay instance) {
        return MoreObjects.toStringHelper(this).add("label", this.label).add("title", this.title).add("overlayGroupName", this.overlayGroupName).add("activeMapTypes", this.activeMapTypes).add("activeUIs", this.activeUIs).add("dimension", this.dimension).add("displayOrder", this.displayOrder).add("maxZoom", this.maxZoom).add("minZoom", this.minZoom).add("textProperties", this.textProperties).add("hasOverlayListener", this.overlayListener != null);
    }
}