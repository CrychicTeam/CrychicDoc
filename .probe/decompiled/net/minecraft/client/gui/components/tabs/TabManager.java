package net.minecraft.client.gui.components.tabs;

import java.util.Objects;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;

public class TabManager {

    private final Consumer<AbstractWidget> addWidget;

    private final Consumer<AbstractWidget> removeWidget;

    @Nullable
    private Tab currentTab;

    @Nullable
    private ScreenRectangle tabArea;

    public TabManager(Consumer<AbstractWidget> consumerAbstractWidget0, Consumer<AbstractWidget> consumerAbstractWidget1) {
        this.addWidget = consumerAbstractWidget0;
        this.removeWidget = consumerAbstractWidget1;
    }

    public void setTabArea(ScreenRectangle screenRectangle0) {
        this.tabArea = screenRectangle0;
        Tab $$1 = this.getCurrentTab();
        if ($$1 != null) {
            $$1.doLayout(screenRectangle0);
        }
    }

    public void setCurrentTab(Tab tab0, boolean boolean1) {
        if (!Objects.equals(this.currentTab, tab0)) {
            if (this.currentTab != null) {
                this.currentTab.visitChildren(this.removeWidget);
            }
            this.currentTab = tab0;
            tab0.visitChildren(this.addWidget);
            if (this.tabArea != null) {
                tab0.doLayout(this.tabArea);
            }
            if (boolean1) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
        }
    }

    @Nullable
    public Tab getCurrentTab() {
        return this.currentTab;
    }

    public void tickCurrent() {
        Tab $$0 = this.getCurrentTab();
        if ($$0 != null) {
            $$0.tick();
        }
    }
}