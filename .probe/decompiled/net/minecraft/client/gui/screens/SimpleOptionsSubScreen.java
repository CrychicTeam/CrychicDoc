package net.minecraft.client.gui.screens;

import javax.annotation.Nullable;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public abstract class SimpleOptionsSubScreen extends OptionsSubScreen {

    protected final OptionInstance<?>[] smallOptions;

    @Nullable
    private AbstractWidget narratorButton;

    protected OptionsList list;

    public SimpleOptionsSubScreen(Screen screen0, Options options1, Component component2, OptionInstance<?>[] optionInstance3) {
        super(screen0, options1, component2);
        this.smallOptions = optionInstance3;
    }

    @Override
    protected void init() {
        this.list = new OptionsList(this.f_96541_, this.f_96543_, this.f_96544_, 32, this.f_96544_ - 32, 25);
        this.list.addSmall(this.smallOptions);
        this.m_7787_(this.list);
        this.createFooter();
        this.narratorButton = this.list.findOption(this.f_96282_.narrator());
        if (this.narratorButton != null) {
            this.narratorButton.active = this.f_96541_.getNarrator().isActive();
        }
    }

    protected void createFooter() {
        this.m_142416_(Button.builder(CommonComponents.GUI_DONE, p_280827_ -> this.f_96541_.setScreen(this.f_96281_)).bounds(this.f_96543_ / 2 - 100, this.f_96544_ - 27, 200, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280419_(guiGraphics0, this.list, int1, int2, float3);
    }

    public void updateNarratorButton() {
        if (this.narratorButton instanceof CycleButton) {
            ((CycleButton) this.narratorButton).setValue(this.f_96282_.narrator().get());
        }
    }
}