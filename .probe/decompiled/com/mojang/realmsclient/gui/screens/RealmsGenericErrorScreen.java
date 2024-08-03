package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.client.RealmsError;
import com.mojang.realmsclient.exception.RealmsServiceException;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsScreen;

public class RealmsGenericErrorScreen extends RealmsScreen {

    private final Screen nextScreen;

    private final RealmsGenericErrorScreen.ErrorMessage lines;

    private MultiLineLabel line2Split = MultiLineLabel.EMPTY;

    public RealmsGenericErrorScreen(RealmsServiceException realmsServiceException0, Screen screen1) {
        super(GameNarrator.NO_TITLE);
        this.nextScreen = screen1;
        this.lines = errorMessage(realmsServiceException0);
    }

    public RealmsGenericErrorScreen(Component component0, Screen screen1) {
        super(GameNarrator.NO_TITLE);
        this.nextScreen = screen1;
        this.lines = errorMessage(component0);
    }

    public RealmsGenericErrorScreen(Component component0, Component component1, Screen screen2) {
        super(GameNarrator.NO_TITLE);
        this.nextScreen = screen2;
        this.lines = errorMessage(component0, component1);
    }

    private static RealmsGenericErrorScreen.ErrorMessage errorMessage(RealmsServiceException realmsServiceException0) {
        RealmsError $$1 = realmsServiceException0.realmsError;
        if ($$1 == null) {
            return errorMessage(Component.translatable("mco.errorMessage.realmsService", realmsServiceException0.httpResultCode), Component.literal(realmsServiceException0.rawResponse));
        } else {
            int $$2 = $$1.getErrorCode();
            String $$3 = "mco.errorMessage." + $$2;
            return errorMessage(Component.translatable("mco.errorMessage.realmsService.realmsError", $$2), (Component) (I18n.exists($$3) ? Component.translatable($$3) : Component.nullToEmpty($$1.getErrorMessage())));
        }
    }

    private static RealmsGenericErrorScreen.ErrorMessage errorMessage(Component component0) {
        return errorMessage(Component.translatable("mco.errorMessage.generic"), component0);
    }

    private static RealmsGenericErrorScreen.ErrorMessage errorMessage(Component component0, Component component1) {
        return new RealmsGenericErrorScreen.ErrorMessage(component0, component1);
    }

    @Override
    public void init() {
        this.m_142416_(Button.builder(CommonComponents.GUI_OK, p_280728_ -> this.f_96541_.setScreen(this.nextScreen)).bounds(this.f_96543_ / 2 - 100, this.f_96544_ - 52, 200, 20).build());
        this.line2Split = MultiLineLabel.create(this.f_96547_, this.lines.detail, this.f_96543_ * 3 / 4);
    }

    @Override
    public Component getNarrationMessage() {
        return Component.empty().append(this.lines.title).append(": ").append(this.lines.detail);
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        guiGraphics0.drawCenteredString(this.f_96547_, this.lines.title, this.f_96543_ / 2, 80, 16777215);
        this.line2Split.renderCentered(guiGraphics0, this.f_96543_ / 2, 100, 9, 16711680);
        super.m_88315_(guiGraphics0, int1, int2, float3);
    }

    static record ErrorMessage(Component f_287789_, Component f_287787_) {

        private final Component title;

        private final Component detail;

        ErrorMessage(Component f_287789_, Component f_287787_) {
            this.title = f_287789_;
            this.detail = f_287787_;
        }
    }
}