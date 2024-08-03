package yalter.mousetweaks;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends Screen {

    private final Screen previous;

    public ConfigScreen(Screen previous) {
        super(Component.literal("Mouse Tweaks Options"));
        this.previous = previous;
    }

    @Override
    protected void init() {
        Main.config.read();
        this.m_142416_(CycleButton.onOffBuilder(Main.config.rmbTweak).create(this.f_96543_ / 2 - 155, this.f_96544_ / 6, 150, 20, Component.literal("RMB Tweak"), (button, value) -> Main.config.rmbTweak = value));
        this.m_142416_(CycleButton.onOffBuilder(Main.config.wheelTweak).create(this.f_96543_ / 2 - 155, this.f_96544_ / 6 + 24, 150, 20, Component.literal("Wheel Tweak"), (button, value) -> Main.config.wheelTweak = value));
        this.m_142416_(CycleButton.onOffBuilder(Main.config.lmbTweakWithItem).create(this.f_96543_ / 2 + 5, this.f_96544_ / 6, 150, 20, Component.literal("LMB Tweak With Item"), (button, value) -> Main.config.lmbTweakWithItem = value));
        this.m_142416_(CycleButton.onOffBuilder(Main.config.lmbTweakWithoutItem).create(this.f_96543_ / 2 + 5, this.f_96544_ / 6 + 24, 150, 20, Component.literal("LMB Tweak Without Item"), (button, value) -> Main.config.lmbTweakWithoutItem = value));
        this.m_142416_(CycleButton.<WheelSearchOrder>builder(value -> {
            return Component.literal(switch(value) {
                case FIRST_TO_LAST ->
                    "First to Last";
                case LAST_TO_FIRST ->
                    "Last to First";
            });
        }).withValues(WheelSearchOrder.FIRST_TO_LAST, WheelSearchOrder.LAST_TO_FIRST).withInitialValue(Main.config.wheelSearchOrder).create(this.f_96543_ / 2 - 155, this.f_96544_ / 6 + 48, 310, 20, Component.literal("Wheel Tweak Search Order"), (button, value) -> Main.config.wheelSearchOrder = value));
        this.m_142416_(CycleButton.<WheelScrollDirection>builder(value -> {
            return Component.literal(switch(value) {
                case NORMAL ->
                    "Down to Push, Up to Pull";
                case INVERTED ->
                    "Up to Push, Down to Pull";
                case INVENTORY_POSITION_AWARE ->
                    "Inventory Position Aware";
                case INVENTORY_POSITION_AWARE_INVERTED ->
                    "Inventory Position Aware, Inverted";
            });
        }).withValues(WheelScrollDirection.NORMAL, WheelScrollDirection.INVERTED, WheelScrollDirection.INVENTORY_POSITION_AWARE, WheelScrollDirection.INVENTORY_POSITION_AWARE_INVERTED).withInitialValue(Main.config.wheelScrollDirection).create(this.f_96543_ / 2 - 155, this.f_96544_ / 6 + 72, 310, 20, Component.literal("Scroll Direction"), (button, value) -> Main.config.wheelScrollDirection = value));
        this.m_142416_(CycleButton.<ScrollItemScaling>builder(value -> {
            return Component.literal(switch(value) {
                case PROPORTIONAL ->
                    "Multiple Wheel Clicks Move Multiple Items";
                case ALWAYS_ONE ->
                    "Always Move One Item (macOS Compatibility)";
            });
        }).withValues(ScrollItemScaling.PROPORTIONAL, ScrollItemScaling.ALWAYS_ONE).withInitialValue(Main.config.scrollItemScaling).create(this.f_96543_ / 2 - 155, this.f_96544_ / 6 + 96, 310, 20, Component.literal("Scroll Scaling"), (button, value) -> Main.config.scrollItemScaling = value));
        this.m_142416_(CycleButton.onOffBuilder(Config.debug).create(this.f_96543_ / 2 - 155, this.f_96544_ / 6 + 120, 310, 20, Component.literal("Debug Mode"), (button, value) -> Config.debug = value));
        this.m_142416_(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose()).bounds(this.f_96543_ / 2 - 100, this.f_96544_ - 27, 200, 20).build());
    }

    @Override
    public void onClose() {
        this.f_96541_.setScreen(this.previous);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        this.m_280039_(guiGraphics);
        guiGraphics.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 15, 16777215);
        super.render(guiGraphics, i, j, f);
    }

    @Override
    public void removed() {
        Main.config.save();
    }
}