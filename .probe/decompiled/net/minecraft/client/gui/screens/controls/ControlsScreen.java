package net.minecraft.client.gui.screens.controls;

import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.MouseSettingsScreen;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class ControlsScreen extends OptionsSubScreen {

    private static final int ROW_SPACING = 24;

    public ControlsScreen(Screen screen0, Options options1) {
        super(screen0, options1, Component.translatable("controls.title"));
    }

    @Override
    protected void init() {
        super.m_7856_();
        int $$0 = this.f_96543_ / 2 - 155;
        int $$1 = $$0 + 160;
        int $$2 = this.f_96544_ / 6 - 12;
        this.m_142416_(Button.builder(Component.translatable("options.mouse_settings"), p_280846_ -> this.f_96541_.setScreen(new MouseSettingsScreen(this, this.f_96282_))).bounds($$0, $$2, 150, 20).build());
        this.m_142416_(Button.builder(Component.translatable("controls.keybinds"), p_280844_ -> this.f_96541_.setScreen(new KeyBindsScreen(this, this.f_96282_))).bounds($$1, $$2, 150, 20).build());
        $$2 += 24;
        this.m_142416_(this.f_96282_.toggleCrouch().createButton(this.f_96282_, $$0, $$2, 150));
        this.m_142416_(this.f_96282_.toggleSprint().createButton(this.f_96282_, $$1, $$2, 150));
        $$2 += 24;
        this.m_142416_(this.f_96282_.autoJump().createButton(this.f_96282_, $$0, $$2, 150));
        this.m_142416_(this.f_96282_.operatorItemsTab().createButton(this.f_96282_, $$1, $$2, 150));
        $$2 += 24;
        this.m_142416_(Button.builder(CommonComponents.GUI_DONE, p_280845_ -> this.f_96541_.setScreen(this.f_96281_)).bounds(this.f_96543_ / 2 - 100, $$2, 200, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 15, 16777215);
        super.m_88315_(guiGraphics0, int1, int2, float3);
    }
}