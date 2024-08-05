package net.minecraft.client.gui.screens;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.Arrays;
import java.util.stream.Stream;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class MouseSettingsScreen extends OptionsSubScreen {

    private OptionsList list;

    private static OptionInstance<?>[] options(Options options0) {
        return new OptionInstance[] { options0.sensitivity(), options0.invertYMouse(), options0.mouseWheelSensitivity(), options0.discreteMouseScroll(), options0.touchscreen() };
    }

    public MouseSettingsScreen(Screen screen0, Options options1) {
        super(screen0, options1, Component.translatable("options.mouse_settings.title"));
    }

    @Override
    protected void init() {
        this.list = new OptionsList(this.f_96541_, this.f_96543_, this.f_96544_, 32, this.f_96544_ - 32, 25);
        if (InputConstants.isRawMouseInputSupported()) {
            this.list.addSmall((OptionInstance<?>[]) Stream.concat(Arrays.stream(options(this.f_96282_)), Stream.of(this.f_96282_.rawMouseInput())).toArray(OptionInstance[]::new));
        } else {
            this.list.addSmall(options(this.f_96282_));
        }
        this.m_7787_(this.list);
        this.m_142416_(Button.builder(CommonComponents.GUI_DONE, p_280804_ -> {
            this.f_96282_.save();
            this.f_96541_.setScreen(this.f_96281_);
        }).bounds(this.f_96543_ / 2 - 100, this.f_96544_ - 27, 200, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        this.list.m_88315_(guiGraphics0, int1, int2, float3);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 5, 16777215);
        super.m_88315_(guiGraphics0, int1, int2, float3);
    }
}