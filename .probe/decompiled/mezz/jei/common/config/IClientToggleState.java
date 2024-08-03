package mezz.jei.common.config;

public interface IClientToggleState {

    boolean isOverlayEnabled();

    void toggleOverlayEnabled();

    boolean isEditModeEnabled();

    void toggleEditModeEnabled();

    boolean isCheatItemsEnabled();

    void toggleCheatItemsEnabled();

    void setCheatItemsEnabled(boolean var1);

    boolean isBookmarkOverlayEnabled();

    void toggleBookmarkEnabled();

    void setBookmarkEnabled(boolean var1);
}