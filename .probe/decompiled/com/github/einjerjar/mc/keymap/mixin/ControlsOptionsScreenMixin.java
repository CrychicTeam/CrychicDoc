package com.github.einjerjar.mc.keymap.mixin;

import com.github.einjerjar.mc.keymap.client.gui.screen.KeymapScreen;
import com.github.einjerjar.mc.keymap.client.gui.screen.LayoutSelectionScreen;
import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ControlsScreen.class })
public class ControlsOptionsScreenMixin extends OptionsSubScreen {

    public ControlsOptionsScreenMixin(Screen arg, Options arg2, Component arg3) {
        super(arg, arg2, arg3);
    }

    @Inject(at = { @At("TAIL") }, method = { "init" })
    private void onInit(CallbackInfo ci) {
        if (KeymapConfig.instance().replaceKeybindScreen()) {
            int i = this.f_96543_ / 2 - 155;
            int x = i + 160;
            int y = this.f_96544_ / 6 - 12;
            for (GuiEventListener child : this.m_6702_()) {
                if (child instanceof Button bb && bb.m_252754_() == x && bb.m_252907_() == y) {
                    this.m_169411_(bb);
                    this.m_142416_(Button.builder(Component.translatable("keymap.keyCat"), this::clickHandler).pos(x, y).size(150, 20).build());
                    break;
                }
            }
        }
    }

    protected void clickHandler(Button button) {
        assert this.f_96541_ != null;
        Screen scr;
        if (KeymapConfig.instance().firstOpenDone()) {
            scr = new KeymapScreen(this);
        } else {
            scr = new LayoutSelectionScreen(this);
        }
        this.f_96541_.setScreen(scr);
    }
}