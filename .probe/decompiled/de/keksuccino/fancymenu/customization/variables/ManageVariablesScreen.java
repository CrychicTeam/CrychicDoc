package de.keksuccino.fancymenu.customization.variables;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import de.keksuccino.fancymenu.util.cycle.CommonCycles;
import de.keksuccino.fancymenu.util.cycle.LocalizedEnumValueCycle;
import de.keksuccino.fancymenu.util.input.CharacterFilter;
import de.keksuccino.fancymenu.util.rendering.ui.UIBase;
import de.keksuccino.fancymenu.util.rendering.ui.screen.ConfirmationScreen;
import de.keksuccino.fancymenu.util.rendering.ui.screen.TextInputScreen;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v1.scrollarea.ScrollArea;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v1.scrollarea.entry.ScrollAreaEntry;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v1.scrollarea.entry.TextListScrollAreaEntry;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v1.scrollarea.entry.TextScrollAreaEntry;
import de.keksuccino.fancymenu.util.rendering.ui.widget.button.ExtendedButton;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ManageVariablesScreen extends Screen {

    protected Consumer<List<Variable>> callback;

    protected ScrollArea variableListScrollArea = new ScrollArea(0, 0, 0, 0);

    protected ExtendedButton doneButton;

    protected ExtendedButton setValueButton;

    protected ExtendedButton deleteVariableButton;

    protected ExtendedButton addVariableButton;

    protected ExtendedButton toggleResetOnLaunchButton;

    public ManageVariablesScreen(@NotNull Consumer<List<Variable>> callback) {
        super(Component.translatable("fancymenu.overlay.menu_bar.variables.manage"));
        this.callback = callback;
        this.updateVariableScrollArea();
    }

    @Override
    protected void init() {
        super.init();
        this.addVariableButton = new ExtendedButton(0, 0, 220, 20, Component.translatable("fancymenu.overlay.menu_bar.variables.manage.add_variable"), button -> {
            TextInputScreen s = new TextInputScreen(Component.translatable("fancymenu.overlay.menu_bar.variables.manage.add_variable.input_name"), CharacterFilter.buildOnlyLowercaseFileNameFilter(), call -> {
                if (call != null && !VariableHandler.variableExists(call)) {
                    VariableHandler.setVariable(call, "");
                    this.updateVariableScrollArea();
                }
                Minecraft.getInstance().setScreen(this);
            });
            Minecraft.getInstance().setScreen(s);
        });
        this.m_7787_(this.addVariableButton);
        UIBase.applyDefaultWidgetSkinTo(this.addVariableButton);
        this.setValueButton = new ExtendedButton(0, 0, 220, 20, Component.translatable("fancymenu.overlay.menu_bar.variables.manage.set_value"), button -> {
            ManageVariablesScreen.VariableScrollEntry e = this.getSelectedEntry();
            if (e != null) {
                TextInputScreen s = new TextInputScreen(Component.translatable("fancymenu.overlay.menu_bar.variables.manage.set_value"), null, call -> {
                    if (call != null) {
                        e.variable.setValue(call);
                    }
                    Minecraft.getInstance().setScreen(this);
                });
                s.setText(e.variable.getValue());
                Minecraft.getInstance().setScreen(s);
            }
        }).setIsActiveSupplier(consumes -> this.getSelectedEntry() != null);
        this.m_7787_(this.setValueButton);
        UIBase.applyDefaultWidgetSkinTo(this.setValueButton);
        this.deleteVariableButton = new ExtendedButton(0, 0, 220, 20, Component.translatable("fancymenu.overlay.menu_bar.variables.manage.delete_variable"), button -> {
            ManageVariablesScreen.VariableScrollEntry e = this.getSelectedEntry();
            if (e != null) {
                Minecraft.getInstance().setScreen(ConfirmationScreen.ofStrings(call -> {
                    if (call) {
                        VariableHandler.removeVariable(e.variable.getName());
                        this.updateVariableScrollArea();
                    }
                    Minecraft.getInstance().setScreen(this);
                }, LocalizationUtils.splitLocalizedStringLines("fancymenu.overlay.menu_bar.variables.manage.delete_variable.confirm")));
            }
        }).setIsActiveSupplier(consumes -> this.getSelectedEntry() != null);
        this.m_7787_(this.deleteVariableButton);
        UIBase.applyDefaultWidgetSkinTo(this.deleteVariableButton);
        LocalizedEnumValueCycle<CommonCycles.CycleEnabledDisabled> resetOnLaunchDisabled = CommonCycles.cycleEnabledDisabled("fancymenu.overlay.menu_bar.variables.manage.clear_on_launch", false);
        this.toggleResetOnLaunchButton = new ExtendedButton(0, 0, 220, 20, Component.empty(), button -> {
            ManageVariablesScreen.VariableScrollEntry e = this.getSelectedEntry();
            if (e != null) {
                e.variable.setResetOnLaunch(!e.variable.isResetOnLaunch());
            }
        }).setIsActiveSupplier(consumes -> this.getSelectedEntry() != null).setLabelSupplier(consumes -> {
            ManageVariablesScreen.VariableScrollEntry e = this.getSelectedEntry();
            if (e != null) {
                LocalizedEnumValueCycle<CommonCycles.CycleEnabledDisabled> enabledDisabled = CommonCycles.cycleEnabledDisabled("fancymenu.overlay.menu_bar.variables.manage.clear_on_launch");
                enabledDisabled.setCurrentValue(CommonCycles.CycleEnabledDisabled.getByBoolean(e.variable.isResetOnLaunch()));
                return enabledDisabled.getCycleComponent();
            } else {
                return resetOnLaunchDisabled.getCycleComponent();
            }
        });
        this.m_7787_(this.toggleResetOnLaunchButton);
        UIBase.applyDefaultWidgetSkinTo(this.toggleResetOnLaunchButton);
        this.doneButton = new ExtendedButton(0, 0, 220, 20, Component.translatable("fancymenu.guicomponents.done"), button -> this.callback.accept(VariableHandler.getVariables()));
        this.m_7787_(this.doneButton);
        UIBase.applyDefaultWidgetSkinTo(this.doneButton);
    }

    @Override
    public void onClose() {
        this.callback.accept(VariableHandler.getVariables());
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        RenderSystem.enableBlend();
        graphics.fill(0, 0, this.f_96543_, this.f_96544_, UIBase.getUIColorTheme().screen_background_color.getColorInt());
        Component titleComp = this.f_96539_.copy().withStyle(Style.EMPTY.withBold(true));
        graphics.drawString(this.f_96547_, titleComp, 20, 20, UIBase.getUIColorTheme().generic_text_base_color.getColorInt(), false);
        graphics.drawString(this.f_96547_, Component.translatable("fancymenu.overlay.menu_bar.variables.manage.variables"), 20, 50, UIBase.getUIColorTheme().generic_text_base_color.getColorInt(), false);
        this.variableListScrollArea.setWidth(this.f_96543_ / 2 - 40, true);
        this.variableListScrollArea.setHeight(this.f_96544_ - 85, true);
        this.variableListScrollArea.setX(20, true);
        this.variableListScrollArea.setY(65, true);
        this.variableListScrollArea.render(graphics, mouseX, mouseY, partial);
        int buttonWidth = this.f_96543_ - 20 - (this.variableListScrollArea.getXWithBorder() + this.variableListScrollArea.getWidthWithBorder() + 20);
        if (buttonWidth < 150) {
            buttonWidth = 150;
        }
        if (buttonWidth > 220) {
            buttonWidth = 220;
        }
        this.doneButton.m_93674_(buttonWidth);
        this.doneButton.m_252865_(this.f_96543_ - 20 - this.doneButton.m_5711_());
        this.doneButton.m_253211_(this.f_96544_ - 20 - 20);
        this.doneButton.render(graphics, mouseX, mouseY, partial);
        this.toggleResetOnLaunchButton.m_93674_(buttonWidth);
        this.toggleResetOnLaunchButton.m_252865_(this.f_96543_ - 20 - this.toggleResetOnLaunchButton.m_5711_());
        this.toggleResetOnLaunchButton.m_253211_(this.doneButton.m_252907_() - 15 - 20);
        this.toggleResetOnLaunchButton.render(graphics, mouseX, mouseY, partial);
        this.deleteVariableButton.m_93674_(buttonWidth);
        this.deleteVariableButton.m_252865_(this.f_96543_ - 20 - this.deleteVariableButton.m_5711_());
        this.deleteVariableButton.m_253211_(this.toggleResetOnLaunchButton.m_252907_() - 5 - 20);
        this.deleteVariableButton.render(graphics, mouseX, mouseY, partial);
        this.setValueButton.m_93674_(buttonWidth);
        this.setValueButton.m_252865_(this.f_96543_ - 20 - this.setValueButton.m_5711_());
        this.setValueButton.m_253211_(this.deleteVariableButton.m_252907_() - 5 - 20);
        this.setValueButton.render(graphics, mouseX, mouseY, partial);
        this.addVariableButton.m_93674_(buttonWidth);
        this.addVariableButton.m_252865_(this.f_96543_ - 20 - this.addVariableButton.m_5711_());
        this.addVariableButton.m_253211_(this.setValueButton.m_252907_() - 15 - 20);
        this.addVariableButton.render(graphics, mouseX, mouseY, partial);
        super.render(graphics, mouseX, mouseY, partial);
    }

    @Nullable
    protected ManageVariablesScreen.VariableScrollEntry getSelectedEntry() {
        for (ScrollAreaEntry e : this.variableListScrollArea.getEntries()) {
            if (e instanceof ManageVariablesScreen.VariableScrollEntry s && s.isSelected()) {
                return s;
            }
        }
        return null;
    }

    protected void updateVariableScrollArea() {
        this.variableListScrollArea.clearEntries();
        for (Variable v : VariableHandler.getVariables()) {
            ManageVariablesScreen.VariableScrollEntry e = new ManageVariablesScreen.VariableScrollEntry(this.variableListScrollArea, v, entry -> {
            });
            this.variableListScrollArea.addEntry(e);
        }
        if (this.variableListScrollArea.getEntries().isEmpty()) {
            this.variableListScrollArea.addEntry(new TextScrollAreaEntry(this.variableListScrollArea, Component.translatable("fancymenu.overlay.menu_bar.variables.manage.no_variables").setStyle(Style.EMPTY.withColor(UIBase.getUIColorTheme().error_text_color.getColorInt())), entry -> {
            }));
        }
    }

    public static class VariableScrollEntry extends TextListScrollAreaEntry {

        public Variable variable;

        public VariableScrollEntry(ScrollArea parent, @NotNull Variable variable, @NotNull Consumer<TextListScrollAreaEntry> onClick) {
            super(parent, Component.literal(variable.name).setStyle(Style.EMPTY.withColor(UIBase.getUIColorTheme().description_area_text_color.getColorInt())), UIBase.getUIColorTheme().listing_dot_color_1.getColor(), onClick);
            this.variable = variable;
        }
    }
}