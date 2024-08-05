package yesman.epicfight.client.gui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.client.gui.component.EpicFightOptionList;
import yesman.epicfight.client.gui.component.RewindableButton;
import yesman.epicfight.config.EpicFightOptions;
import yesman.epicfight.config.OptionHandler;

@OnlyIn(Dist.CLIENT)
public class EpicFightControlOptionScreen extends EpicFightOptionSubScreen {

    private EpicFightOptionList optionsList;

    public EpicFightControlOptionScreen(Screen parentScreen, EpicFightOptions config) {
        super(parentScreen, config, Component.translatable("gui.epicfight.control_options"));
    }

    @Override
    protected void init() {
        super.init();
        this.optionsList = new EpicFightOptionList(this.f_96541_, this.f_96543_, this.f_96544_, 32, this.f_96544_ - 32, 25);
        OptionHandler<Integer> longPressCounter = this.config.longPressCount;
        OptionHandler<Boolean> cameraAutoSwitch = this.config.cameraAutoSwitch;
        OptionHandler<Boolean> autoPreparation = this.config.autoPreparation;
        OptionHandler<Boolean> noMiningInCombat = this.config.noMiningInCombat;
        int buttonHeight = -32;
        Button longPressCounterButton = new RewindableButton(this.f_96543_ / 2 - 165, this.f_96544_ / 4 + buttonHeight, 160, 20, Component.translatable("gui.epicfight.long_press_counter", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(longPressCounter.getValue())), button -> {
            longPressCounter.setValue(longPressCounter.getValue() + 1);
            button.m_93666_(Component.translatable("gui.epicfight.long_press_counter", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(longPressCounter.getValue())));
        }, button -> {
            longPressCounter.setValue(longPressCounter.getValue() - 1);
            button.m_93666_(Component.translatable("gui.epicfight.long_press_counter", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(longPressCounter.getValue())));
        });
        longPressCounterButton.m_257544_(Tooltip.create(Component.translatable("gui.epicfight.long_press_counter.tooltip")));
        Button cameraAutoSwitchButton = Button.builder(Component.translatable("gui.epicfight.camera_auto_switch." + (cameraAutoSwitch.getValue() ? "on" : "off")), button -> {
            cameraAutoSwitch.setValue(!cameraAutoSwitch.getValue());
            button.m_93666_(Component.translatable("gui.epicfight.camera_auto_switch." + (cameraAutoSwitch.getValue() ? "on" : "off")));
        }).pos(this.f_96543_ / 2 + 5, this.f_96544_ / 4 + buttonHeight).size(160, 20).tooltip(Tooltip.create(Component.translatable("gui.epicfight.camera_auto_switch.tooltip"))).build();
        this.optionsList.addSmall(longPressCounterButton, cameraAutoSwitchButton);
        buttonHeight += 24;
        Button autoPreparationButton = Button.builder(Component.translatable("gui.epicfight.auto_preparation." + (autoPreparation.getValue() ? "on" : "off")), button -> {
            autoPreparation.setValue(!autoPreparation.getValue());
            button.m_93666_(Component.translatable("gui.epicfight.auto_preparation." + (autoPreparation.getValue() ? "on" : "off")));
        }).pos(this.f_96543_ / 2 - 165, this.f_96544_ / 4 + buttonHeight).size(160, 20).tooltip(Tooltip.create(Component.translatable("gui.epicfight.auto_preparation.tooltip"))).build();
        Button autoSwitchingItems = Button.builder(Component.translatable("gui.epicfight.auto_switching_items"), button -> this.f_96541_.setScreen(new EditSwitchingItemScreen(this))).pos(this.f_96543_ / 2 + 5, this.f_96544_ / 4 + buttonHeight).size(160, 20).tooltip(Tooltip.create(Component.translatable("gui.epicfight.auto_switching_items.tooltip"))).build();
        this.optionsList.addSmall(autoPreparationButton, autoSwitchingItems);
        buttonHeight += 24;
        Button noMiningInCombatButton = Button.builder(Component.translatable("gui.epicfight.no_mining_in_combat." + (noMiningInCombat.getValue() ? "on" : "off")), button -> {
            noMiningInCombat.setValue(!noMiningInCombat.getValue());
            button.m_93666_(Component.translatable("gui.epicfight.no_mining_in_combat." + (noMiningInCombat.getValue() ? "on" : "off")));
        }).pos(this.f_96543_ / 2 - 165, this.f_96544_ / 4 + buttonHeight).size(160, 20).tooltip(Tooltip.create(Component.translatable("gui.epicfight.no_mining_in_combat.tooltip"))).build();
        this.optionsList.addSmall(noMiningInCombatButton, null);
        this.m_7787_(this.optionsList);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.basicListRender(guiGraphics, this.optionsList, mouseX, mouseY, partialTicks);
    }
}