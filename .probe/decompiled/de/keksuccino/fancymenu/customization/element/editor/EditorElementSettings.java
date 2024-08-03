package de.keksuccino.fancymenu.customization.element.editor;

public class EditorElementSettings {

    protected AbstractEditorElement editorElement;

    private boolean destroyable = true;

    private boolean hideInsteadOfDestroy = false;

    private boolean stretchable = true;

    private boolean orderable = true;

    private boolean copyable = true;

    private boolean delayable = true;

    private boolean fadeable = true;

    private boolean resizeable = true;

    private boolean supportsAdvancedPositioning = true;

    private boolean supportsAdvancedSizing = true;

    private boolean resizeableX = true;

    private boolean resizeableY = true;

    private boolean movable = true;

    private boolean anchorPointCanBeChanged = true;

    private boolean allowElementAnchorPoint = true;

    private boolean allowVanillaAnchorPoint = false;

    private boolean enableLoadingRequirements = true;

    private boolean identifierCopyable = true;

    private boolean skipReInit = false;

    public boolean isDestroyable() {
        return this.destroyable;
    }

    public void setDestroyable(boolean destroyable) {
        this.destroyable = destroyable;
        this.settingsChanged();
    }

    public boolean shouldHideInsteadOfDestroy() {
        return this.hideInsteadOfDestroy;
    }

    public void setHideInsteadOfDestroy(boolean hideInsteadOfDestroy) {
        this.hideInsteadOfDestroy = hideInsteadOfDestroy;
        this.settingsChanged();
    }

    public boolean isStretchable() {
        return this.stretchable;
    }

    public void setStretchable(boolean stretchable) {
        this.stretchable = stretchable;
        this.settingsChanged();
    }

    public boolean isOrderable() {
        return this.orderable;
    }

    public void setOrderable(boolean orderable) {
        this.orderable = orderable;
        this.settingsChanged();
    }

    public boolean isCopyable() {
        return this.copyable;
    }

    public void setCopyable(boolean copyable) {
        this.copyable = copyable;
        this.settingsChanged();
    }

    public boolean isDelayable() {
        return this.delayable;
    }

    public void setDelayable(boolean delayable) {
        this.delayable = delayable;
        this.settingsChanged();
    }

    public boolean isFadeable() {
        return this.fadeable;
    }

    public void setFadeable(boolean fadeable) {
        this.fadeable = fadeable;
        this.settingsChanged();
    }

    public boolean isResizeable() {
        return this.resizeable;
    }

    public void setResizeable(boolean resizeable) {
        this.resizeable = resizeable;
        this.settingsChanged();
    }

    public boolean isAdvancedPositioningSupported() {
        return this.supportsAdvancedPositioning;
    }

    public void setAdvancedPositioningSupported(boolean supported) {
        this.supportsAdvancedPositioning = supported;
        this.settingsChanged();
    }

    public boolean isAdvancedSizingSupported() {
        return this.supportsAdvancedSizing;
    }

    public void setAdvancedSizingSupported(boolean supported) {
        this.supportsAdvancedSizing = supported;
        this.settingsChanged();
    }

    public boolean isResizeableX() {
        return this.resizeableX;
    }

    public void setResizeableX(boolean resizeableX) {
        this.resizeableX = resizeableX;
        this.settingsChanged();
    }

    public boolean isResizeableY() {
        return this.resizeableY;
    }

    public void setResizeableY(boolean resizeableY) {
        this.resizeableY = resizeableY;
        this.settingsChanged();
    }

    public boolean isMovable() {
        return this.movable;
    }

    public void setMovable(boolean movable) {
        this.movable = movable;
        this.settingsChanged();
    }

    public boolean isAnchorPointChangeable() {
        return this.anchorPointCanBeChanged;
    }

    public void setAnchorPointChangeable(boolean changeable) {
        this.anchorPointCanBeChanged = changeable;
        this.settingsChanged();
    }

    public boolean isElementAnchorPointAllowed() {
        return this.allowElementAnchorPoint;
    }

    public void setElementAnchorPointAllowed(boolean allow) {
        this.allowElementAnchorPoint = allow;
        this.settingsChanged();
    }

    public boolean isVanillaAnchorPointAllowed() {
        return this.allowVanillaAnchorPoint;
    }

    public void setVanillaAnchorPointAllowed(boolean allow) {
        this.allowVanillaAnchorPoint = allow;
        this.settingsChanged();
    }

    public boolean isLoadingRequirementsEnabled() {
        return this.enableLoadingRequirements;
    }

    public void setLoadingRequirementsEnabled(boolean enabled) {
        this.enableLoadingRequirements = enabled;
        this.settingsChanged();
    }

    public boolean isIdentifierCopyable() {
        return this.identifierCopyable;
    }

    public void setIdentifierCopyable(boolean copyable) {
        this.identifierCopyable = copyable;
        this.settingsChanged();
    }

    public void setSkipReInitAfterSettingsChanged(boolean skip) {
        this.skipReInit = skip;
    }

    public void settingsChanged() {
        if (this.editorElement != null && !this.skipReInit) {
            this.editorElement.onSettingsChanged();
        }
    }
}