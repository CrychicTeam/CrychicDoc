package yesman.epicfight.client.gui.screen;

import java.io.File;
import java.io.IOException;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.client.model.armor.CustomModelBakery;
import yesman.epicfight.client.gui.component.ColorWidget;
import yesman.epicfight.client.gui.component.EpicFightOptionList;
import yesman.epicfight.config.ClientConfig;
import yesman.epicfight.config.EpicFightOptions;
import yesman.epicfight.config.OptionHandler;
import yesman.epicfight.main.EpicFightMod;

@OnlyIn(Dist.CLIENT)
public class EpicFightGraphicOptionScreen extends EpicFightOptionSubScreen {

    private EpicFightOptionList optionsList;

    public EpicFightGraphicOptionScreen(Screen parentScreen, EpicFightOptions config) {
        super(parentScreen, config, Component.translatable("gui.epicfight.graphic_options"));
    }

    @Override
    protected void init() {
        super.init();
        this.optionsList = new EpicFightOptionList(this.f_96541_, this.f_96543_, this.f_96544_, 32, this.f_96544_ - 32, 25);
        OptionHandler<ClientConfig.HealthBarShowOptions> showHealthIndicator = this.config.healthBarShowOption;
        OptionHandler<Boolean> showTargetIndicator = this.config.showTargetIndicator;
        OptionHandler<Boolean> filterAnimation = this.config.filterAnimation;
        OptionHandler<Boolean> enableAimHelper = this.config.enableAimHelperPointer;
        OptionHandler<Double> aimHelperColor = this.config.aimHelperColor;
        OptionHandler<Boolean> bloodEffects = this.config.bloodEffects;
        int buttonHeight = -32;
        Button filterAnimationButton = Button.builder(Component.translatable("gui.epicfight.filter_animation." + (filterAnimation.getValue() ? "on" : "off")), button -> {
            filterAnimation.setValue(!filterAnimation.getValue());
            button.m_93666_(Component.translatable("gui.epicfight.filter_animation." + (filterAnimation.getValue() ? "on" : "off")));
        }).pos(this.f_96543_ / 2 + 5, this.f_96544_ / 4 + buttonHeight).size(160, 20).tooltip(Tooltip.create(this.f_96539_)).build();
        Button healthBarShowOptionButton = Button.builder(Component.translatable("gui.epicfight.health_bar_show_option." + showHealthIndicator.getValue().toString()), button -> {
            showHealthIndicator.setValue(showHealthIndicator.getValue().nextOption());
            button.m_93666_(Component.translatable("gui.epicfight.health_bar_show_option." + showHealthIndicator.getValue().toString()));
        }).pos(this.f_96543_ / 2 - 165, this.f_96544_ / 4 - 8).size(160, 20).tooltip(Tooltip.create(Component.translatable("gui.epicfight.filter_animation.tooltip"))).build();
        this.optionsList.addSmall(filterAnimationButton, healthBarShowOptionButton);
        buttonHeight += 24;
        Button showTargetIndicatorButton = Button.builder(Component.translatable("gui.epicfight.target_indicator." + (showTargetIndicator.getValue() ? "on" : "off")), button -> {
            showTargetIndicator.setValue(!showTargetIndicator.getValue());
            button.m_93666_(Component.translatable("gui.epicfight.target_indicator." + (showTargetIndicator.getValue() ? "on" : "off")));
        }).pos(this.f_96543_ / 2 + 5, this.f_96544_ / 4 - 8).size(160, 20).tooltip(Tooltip.create(Component.translatable("gui.epicfight.target_indicator.tooltip"))).build();
        Button enableAimHelperButton = Button.builder(Component.translatable("gui.epicfight.aim_helper." + (enableAimHelper.getValue() ? "on" : "off")), button -> {
            enableAimHelper.setValue(!enableAimHelper.getValue());
            button.m_93666_(Component.translatable("gui.epicfight.aim_helper." + (enableAimHelper.getValue() ? "on" : "off")));
        }).pos(this.f_96543_ / 2 - 165, this.f_96544_ / 4 - 8).size(160, 20).tooltip(Tooltip.create(Component.translatable("gui.epicfight.aim_helper.tooltip"))).build();
        this.optionsList.addSmall(showTargetIndicatorButton, enableAimHelperButton);
        buttonHeight += 24;
        Button bloodEffectsButton = Button.builder(Component.translatable("gui.epicfight.blood_effects." + (bloodEffects.getValue() ? "on" : "off")), button -> {
            bloodEffects.setValue(!bloodEffects.getValue());
            button.m_93666_(Component.translatable("gui.epicfight.blood_effects." + (bloodEffects.getValue() ? "on" : "off")));
        }).pos(this.f_96543_ / 2 - 165, this.f_96544_ / 4 - 8).size(160, 20).tooltip(Tooltip.create(Component.translatable("gui.epicfight.blood_effects.tooltip"))).build();
        Button exportCustomArmors = Button.builder(Component.translatable("gui.epicfight.export_custom_armor"), button -> {
            File resourcePackDirectory = Minecraft.getInstance().getResourcePackDirectory().toFile();
            try {
                CustomModelBakery.exportModels(resourcePackDirectory);
                Util.getPlatform().openFile(resourcePackDirectory);
            } catch (IOException var3x) {
                EpicFightMod.LOGGER.info("Failed to export custom armor models");
                var3x.printStackTrace();
            }
        }).pos(this.f_96543_ / 2 + 5, this.f_96544_ / 4 + buttonHeight).size(160, 20).tooltip(Tooltip.create(Component.translatable("gui.epicfight.export_custom_armor.tooltip"))).build();
        this.optionsList.addSmall(bloodEffectsButton, exportCustomArmors);
        buttonHeight += 24;
        Button uiSetupButton = Button.builder(Component.translatable("gui.epicfight.ui_setup"), button -> this.f_96541_.setScreen(new UISetupScreen(this))).pos(this.f_96543_ / 2 - 165, this.f_96544_ / 4 + buttonHeight).size(160, 20).tooltip(Tooltip.create(Component.translatable("gui.epicfight.ui_setup.tooltip"))).build();
        this.optionsList.addSmall(uiSetupButton, null);
        buttonHeight += 30;
        this.optionsList.addBig(new ColorWidget(this.f_96543_ / 2 - 150, this.f_96544_ / 4 + buttonHeight, 300, 20, Component.translatable("gui.epicfight.aim_helper_color"), aimHelperColor.getValue(), EpicFightMod.CLIENT_CONFIGS.aimHelperColor));
        this.m_7787_(this.optionsList);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.basicListRender(guiGraphics, this.optionsList, mouseX, mouseY, partialTicks);
    }
}