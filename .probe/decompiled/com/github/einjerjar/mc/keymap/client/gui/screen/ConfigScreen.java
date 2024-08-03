package com.github.einjerjar.mc.keymap.client.gui.screen;

import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.widgets.utils.Point;
import com.github.einjerjar.mc.widgets2.ELabel2;
import com.github.einjerjar.mc.widgets2.ELineToggleButton;
import com.github.einjerjar.mc.widgets2.EScreen2;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public class ConfigScreen extends EScreen2 {

    public ConfigScreen(Screen parent) {
        super(parent);
        this.targetScreenWidth = 450;
    }

    @Override
    protected void onInit() {
        this.children.clear();
        Style bold = Style.EMPTY.withBold(true);
        int h = 9 + 8;
        ELabel2 labelGeneral = new ELabel2(Component.translatable("keymap.catGeneral").withStyle(bold), this.scr.left(), this.scr.top(), this.scr.w(), h);
        ELineToggleButton toggleReplaceKeybind = new ELineToggleButton(Component.translatable("keymap.optReplaceKeybindScreen"), this.scr.left(), this.scr.top() + h, this.scr.w(), h);
        toggleReplaceKeybind.padding(new Point<>(4));
        toggleReplaceKeybind.value(Boolean.valueOf(KeymapConfig.instance().replaceKeybindScreen()));
        toggleReplaceKeybind.onToggle(self -> KeymapConfig.instance().replaceKeybindScreen(self.value()));
        ELabel2 labelLayout = new ELabel2(Component.translatable("keymap.catLayout").withStyle(bold), this.scr.left(), this.scr.top() + h * 2, this.scr.w(), h);
        ELineToggleButton toggleAutoSelectLayout = new ELineToggleButton(Component.translatable("keymap.optAutoSelectLayout"), this.scr.left(), this.scr.top() + h * 3, this.scr.w(), h);
        toggleAutoSelectLayout.padding(new Point<>(4));
        toggleAutoSelectLayout.value(Boolean.valueOf(KeymapConfig.instance().autoSelectLayout()));
        toggleAutoSelectLayout.onToggle(self -> KeymapConfig.instance().autoSelectLayout(self.value()));
        ELabel2 labelTooltips = new ELabel2(Component.translatable("keymap.catTooltips").withStyle(bold), this.scr.left(), this.scr.top() + h * 4, this.scr.w(), h);
        ELineToggleButton toggleShowHelpTooltips = new ELineToggleButton(Component.translatable("keymap.optShowHelpTooltips"), this.scr.left(), this.scr.top() + h * 5, this.scr.w(), h);
        toggleShowHelpTooltips.padding(new Point<>(4));
        toggleShowHelpTooltips.value(Boolean.valueOf(KeymapConfig.instance().showHelpTooltips()));
        toggleShowHelpTooltips.onToggle(self -> KeymapConfig.instance().showHelpTooltips(self.value()));
        ELabel2 labelExtra = new ELabel2(Component.translatable("keymap.catExtra").withStyle(bold), this.scr.left(), this.scr.top() + h * 6, this.scr.w(), h);
        ELineToggleButton toggleFirstOpenDone = new ELineToggleButton(Component.translatable("keymap.optFirstOpenDoneExtra"), this.scr.left(), this.scr.top() + h * 7, this.scr.w(), h);
        ELineToggleButton toggleDebug = new ELineToggleButton(Component.translatable("keymap.optDebug"), this.scr.left(), this.scr.top() + h * 8, this.scr.w(), h);
        ELineToggleButton toggleDebug2 = new ELineToggleButton(Component.translatable("keymap.optDebug2"), this.scr.left(), this.scr.top() + h * 9, this.scr.w(), h);
        ELineToggleButton toggleMad = new ELineToggleButton(Component.translatable("keymap.optCrashOnProblematicError"), this.scr.left(), this.scr.top() + h * 10, this.scr.w(), h);
        toggleFirstOpenDone.padding(new Point<>(4));
        toggleFirstOpenDone.value(Boolean.valueOf(KeymapConfig.instance().firstOpenDone()));
        toggleFirstOpenDone.onToggle(self -> KeymapConfig.instance().firstOpenDone(self.value()));
        toggleDebug.padding(new Point<>(4));
        toggleDebug.value(Boolean.valueOf(KeymapConfig.instance().debug()));
        toggleDebug.onToggle(self -> KeymapConfig.instance().debug(self.value()));
        toggleDebug2.padding(new Point<>(4));
        toggleDebug2.value(Boolean.valueOf(KeymapConfig.instance().debug2()));
        toggleDebug2.onToggle(self -> KeymapConfig.instance().debug2(self.value()));
        toggleMad.padding(new Point<>(4));
        toggleMad.value(Boolean.valueOf(KeymapConfig.instance().crashOnProblematicError()));
        toggleMad.onToggle(self -> KeymapConfig.instance().crashOnProblematicError(self.value()));
        this.children.add(labelGeneral);
        this.children.add(toggleReplaceKeybind);
        this.children.add(labelLayout);
        this.children.add(toggleAutoSelectLayout);
        this.children.add(labelTooltips);
        this.children.add(toggleShowHelpTooltips);
        this.children.add(labelExtra);
        this.children.add(toggleFirstOpenDone);
        this.children.add(toggleDebug);
        this.children.add(toggleDebug2);
        this.children.add(toggleMad);
    }

    @Override
    protected void preRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.preRender(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void postRender(GuiGraphics poseStack, int mouseX, int mouseY, float partialTick) {
    }

    @Override
    public void onClose() {
        KeymapConfig.save();
        super.onClose();
    }
}