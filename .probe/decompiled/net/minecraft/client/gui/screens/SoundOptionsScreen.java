package net.minecraft.client.gui.screens;

import java.util.Arrays;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;

public class SoundOptionsScreen extends OptionsSubScreen {

    private OptionsList list;

    private static OptionInstance<?>[] buttonOptions(Options options0) {
        return new OptionInstance[] { options0.showSubtitles(), options0.directionalAudio() };
    }

    public SoundOptionsScreen(Screen screen0, Options options1) {
        super(screen0, options1, Component.translatable("options.sounds.title"));
    }

    @Override
    protected void init() {
        this.list = new OptionsList(this.f_96541_, this.f_96543_, this.f_96544_, 32, this.f_96544_ - 32, 25);
        this.list.addBig(this.f_96282_.getSoundSourceOptionInstance(SoundSource.MASTER));
        this.list.addSmall(this.getAllSoundOptionsExceptMaster());
        this.list.addBig(this.f_96282_.soundDevice());
        this.list.addSmall(buttonOptions(this.f_96282_));
        this.m_7787_(this.list);
        this.m_142416_(Button.builder(CommonComponents.GUI_DONE, p_280829_ -> {
            this.f_96541_.options.save();
            this.f_96541_.setScreen(this.f_96281_);
        }).bounds(this.f_96543_ / 2 - 100, this.f_96544_ - 27, 200, 20).build());
    }

    private OptionInstance<?>[] getAllSoundOptionsExceptMaster() {
        return (OptionInstance<?>[]) Arrays.stream(SoundSource.values()).filter(p_247780_ -> p_247780_ != SoundSource.MASTER).map(p_247779_ -> this.f_96282_.getSoundSourceOptionInstance(p_247779_)).toArray(OptionInstance[]::new);
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280419_(guiGraphics0, this.list, int1, int2, float3);
    }
}