package net.blay09.mods.craftingtweaks.registry;

import com.google.gson.annotations.SerializedName;

public class CraftingTweaksRegistrationData {

    @SerializedName("modid")
    private String modId;

    private boolean enabled = true;

    private boolean silent;

    private String containerClass = "";

    private String containerCallbackClass = "";

    private String validContainerPredicateClass = "";

    private String getGridStartFunctionClass = "";

    private int gridSlotNumber = 1;

    private int gridSize = 9;

    private Integer buttonOffsetX;

    private Integer buttonOffsetY;

    private String alignToGrid = "left";

    private String buttonStyle = "default";

    private boolean hideButtons;

    private boolean phantomItems;

    private CraftingTweaksRegistrationData.TweakData tweakRotate = new CraftingTweaksRegistrationData.TweakData();

    private CraftingTweaksRegistrationData.TweakData tweakBalance = new CraftingTweaksRegistrationData.TweakData();

    private CraftingTweaksRegistrationData.TweakData tweakClear = new CraftingTweaksRegistrationData.TweakData();

    public String getModId() {
        return this.modId;
    }

    public void setModId(String modId) {
        this.modId = modId;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isSilent() {
        return this.silent;
    }

    public String getContainerClass() {
        return this.containerClass;
    }

    public void setContainerClass(String containerClass) {
        this.containerClass = containerClass;
    }

    public String getContainerCallbackClass() {
        return this.containerCallbackClass;
    }

    public void setContainerCallbackClass(String containerCallbackClass) {
        this.containerCallbackClass = containerCallbackClass;
    }

    public String getValidContainerPredicateClass() {
        return this.validContainerPredicateClass;
    }

    public void setValidContainerPredicateClass(String validContainerPredicateClass) {
        this.validContainerPredicateClass = validContainerPredicateClass;
    }

    public String getGetGridStartFunctionClass() {
        return this.getGridStartFunctionClass;
    }

    public void setGetGridStartFunctionClass(String getGridStartFunctionClass) {
        this.getGridStartFunctionClass = getGridStartFunctionClass;
    }

    public int getGridSlotNumber() {
        return this.gridSlotNumber;
    }

    public void setGridSlotNumber(int gridSlotNumber) {
        this.gridSlotNumber = gridSlotNumber;
    }

    public int getGridSize() {
        return this.gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public Integer getButtonOffsetX() {
        return this.buttonOffsetX;
    }

    public void setButtonOffsetX(Integer buttonOffsetX) {
        this.buttonOffsetX = buttonOffsetX;
    }

    public Integer getButtonOffsetY() {
        return this.buttonOffsetY;
    }

    public void setButtonOffsetY(Integer buttonOffsetY) {
        this.buttonOffsetY = buttonOffsetY;
    }

    public String getAlignToGrid() {
        return this.alignToGrid;
    }

    public void setAlignToGrid(String alignToGrid) {
        this.alignToGrid = alignToGrid;
    }

    public String getButtonStyle() {
        return this.buttonStyle;
    }

    public void setButtonStyle(String buttonStyle) {
        this.buttonStyle = buttonStyle;
    }

    public boolean isHideButtons() {
        return this.hideButtons;
    }

    public void setHideButtons(boolean hideButtons) {
        this.hideButtons = hideButtons;
    }

    public boolean isPhantomItems() {
        return this.phantomItems;
    }

    public void setPhantomItems(boolean phantomItems) {
        this.phantomItems = phantomItems;
    }

    public CraftingTweaksRegistrationData.TweakData getTweakRotate() {
        return this.tweakRotate;
    }

    public void setTweakRotate(CraftingTweaksRegistrationData.TweakData tweakRotate) {
        this.tweakRotate = tweakRotate;
    }

    public CraftingTweaksRegistrationData.TweakData getTweakBalance() {
        return this.tweakBalance;
    }

    public void setTweakBalance(CraftingTweaksRegistrationData.TweakData tweakBalance) {
        this.tweakBalance = tweakBalance;
    }

    public CraftingTweaksRegistrationData.TweakData getTweakClear() {
        return this.tweakClear;
    }

    public void setTweakClear(CraftingTweaksRegistrationData.TweakData tweakClear) {
        this.tweakClear = tweakClear;
    }

    public static class TweakData {

        private boolean enabled = true;

        private boolean showButton = true;

        private Integer buttonX;

        private Integer buttonY;

        public boolean isEnabled() {
            return this.enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isShowButton() {
            return this.showButton;
        }

        public void setShowButton(boolean showButton) {
            this.showButton = showButton;
        }

        public Integer getButtonX() {
            return this.buttonX;
        }

        public void setButtonX(Integer buttonX) {
            this.buttonX = buttonX;
        }

        public Integer getButtonY() {
            return this.buttonY;
        }

        public void setButtonY(Integer buttonY) {
            this.buttonY = buttonY;
        }
    }
}