package icyllis.modernui.view;

import icyllis.modernui.ModernUI;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.ApiStatus.Internal;

public abstract class ActionProvider {

    private static final Marker MARKER = MarkerManager.getMarker("ActionProvider");

    private ActionProvider.SubUiVisibilityListener mSubUiVisibilityListener;

    private ActionProvider.VisibilityListener mVisibilityListener;

    public abstract View onCreateActionView(MenuItem var1);

    public boolean overridesItemVisibility() {
        return false;
    }

    public boolean isVisible() {
        return true;
    }

    public void refreshVisibility() {
        if (this.mVisibilityListener != null && this.overridesItemVisibility()) {
            this.mVisibilityListener.onActionProviderVisibilityChanged(this.isVisible());
        }
    }

    public boolean onPerformDefaultAction() {
        return false;
    }

    public boolean hasSubMenu() {
        return false;
    }

    public void onPrepareSubMenu(SubMenu subMenu) {
    }

    public void subUiVisibilityChanged(boolean isVisible) {
        if (this.mSubUiVisibilityListener != null) {
            this.mSubUiVisibilityListener.onSubUiVisibilityChanged(isVisible);
        }
    }

    @Internal
    public void setSubUiVisibilityListener(ActionProvider.SubUiVisibilityListener listener) {
        this.mSubUiVisibilityListener = listener;
    }

    public void setVisibilityListener(ActionProvider.VisibilityListener listener) {
        if (this.mVisibilityListener != null) {
            ModernUI.LOGGER.warn(MARKER, "setVisibilityListener: Setting a new ActionProvider.VisibilityListener when one is already set. Are you reusing this {} instance while it is still in use somewhere else?", this.getClass().getSimpleName());
        }
        this.mVisibilityListener = listener;
    }

    @Internal
    public void reset() {
        this.mVisibilityListener = null;
        this.mSubUiVisibilityListener = null;
    }

    @FunctionalInterface
    @Internal
    public interface SubUiVisibilityListener {

        void onSubUiVisibilityChanged(boolean var1);
    }

    @FunctionalInterface
    public interface VisibilityListener {

        void onActionProviderVisibilityChanged(boolean var1);
    }
}