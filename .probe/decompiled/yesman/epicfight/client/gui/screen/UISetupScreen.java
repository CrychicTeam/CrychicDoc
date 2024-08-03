package yesman.epicfight.client.gui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.client.gui.component.UIComponent;
import yesman.epicfight.config.EpicFightOptions;
import yesman.epicfight.main.EpicFightMod;

@OnlyIn(Dist.CLIENT)
public class UISetupScreen extends Screen {

    private final EpicFightOptions config;

    protected final Screen parentScreen;

    private UIComponent draggingButton;

    protected UISetupScreen(Screen parentScreen) {
        super(Component.literal("epicfight.gui.configuration.ui_setup"));
        this.parentScreen = parentScreen;
        this.config = EpicFightMod.CLIENT_CONFIGS;
    }

    @Override
    public void init() {
        int weaponInnateX = (Integer) this.config.weaponInnateXBase.getValue().positionGetter.apply(this.f_96543_, this.config.weaponInnateX.getValue());
        int weaponInnateY = (Integer) this.config.weaponInnateYBase.getValue().positionGetter.apply(this.f_96544_, this.config.weaponInnateY.getValue());
        this.m_142416_(new UIComponent(weaponInnateX, weaponInnateY, this.config.weaponInnateX, this.config.weaponInnateY, this.config.weaponInnateXBase, this.config.weaponInnateYBase, 32, 32, 0, 0, 1, 1, 1, 1, 0, 163, 184, this, new ResourceLocation("epicfight", "textures/gui/skills/sweeping_edge.png")));
        int staminaX = (Integer) this.config.staminaBarXBase.getValue().positionGetter.apply(this.f_96543_, this.config.staminaBarX.getValue());
        int staminaY = (Integer) this.config.staminaBarYBase.getValue().positionGetter.apply(this.f_96544_, this.config.staminaBarY.getValue());
        this.m_142416_(new UIComponent(staminaX, staminaY, this.config.staminaBarX, this.config.staminaBarY, this.config.staminaBarXBase, this.config.staminaBarYBase, 118, 4, 2, 38, 237, 9, 256, 256, 255, 128, 64, this, new ResourceLocation("epicfight", "textures/gui/battle_icons.png")));
        int chargingBarX = (Integer) this.config.chargingBarXBase.getValue().positionGetter.apply(this.f_96543_, this.config.chargingBarX.getValue());
        int chargingBarY = (Integer) this.config.chargingBarYBase.getValue().positionGetter.apply(this.f_96544_, this.config.chargingBarY.getValue());
        this.m_142416_(new UIComponent(chargingBarX, chargingBarY, this.config.chargingBarX, this.config.chargingBarY, this.config.chargingBarXBase, this.config.chargingBarYBase, 238, 13, 1, 71, 237, 13, 256, 256, 255, 255, 255, this, new ResourceLocation("epicfight", "textures/gui/battle_icons.png")));
        int passivesX = (Integer) this.config.passivesXBase.getValue().positionGetter.apply(this.f_96543_, this.config.passivesX.getValue());
        int passivesY = (Integer) this.config.passivesYBase.getValue().positionGetter.apply(this.f_96544_, this.config.passivesY.getValue());
        this.m_142416_(new UIComponent.PassiveUIComponent(passivesX, passivesY, this.config.passivesX, this.config.passivesY, this.config.passivesXBase, this.config.passivesYBase, this.config.passivesAlignDirection, 24, 24, 0, 0, 1, 1, 1, 1, 255, 255, 255, this, new ResourceLocation("epicfight", "textures/gui/skills/guard.png"), new ResourceLocation("epicfight", "textures/gui/skills/berserker.png")));
    }

    @Override
    public boolean mouseClicked(double x, double y, int pressType) {
        for (GuiEventListener guieventlistener : this.m_6702_()) {
            if (guieventlistener instanceof UIComponent uiComponent && uiComponent.popupScreen.isOpen() && uiComponent.popupScreen.mouseClicked(x, y, pressType)) {
                this.m_7522_(guieventlistener);
                if (pressType == 0) {
                    this.m_7897_(true);
                }
                return true;
            }
            if (guieventlistener.mouseClicked(x, y, pressType)) {
                this.m_7522_(guieventlistener);
                if (pressType == 0) {
                    this.m_7897_(true);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280039_(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClose() {
        this.f_96541_.setScreen(this.parentScreen);
    }

    public void beginToDrag(UIComponent button) {
        this.draggingButton = button;
    }

    public void endDragging() {
        this.draggingButton = null;
    }

    public boolean isDraggingComponent(UIComponent button) {
        return this.draggingButton == button;
    }
}