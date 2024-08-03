package net.minecraft.client.gui.screens.advancements;

public enum AdvancementWidgetType {

    OBTAINED(0), UNOBTAINED(1);

    private final int y;

    private AdvancementWidgetType(int p_97324_) {
        this.y = p_97324_;
    }

    public int getIndex() {
        return this.y;
    }
}