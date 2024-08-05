package net.minecraft.client.gui.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class CreditsAndAttributionScreen extends Screen {

    private static final int BUTTON_SPACING = 8;

    private static final int BUTTON_WIDTH = 210;

    private static final Component TITLE = Component.translatable("credits_and_attribution.screen.title");

    private static final Component CREDITS_BUTTON = Component.translatable("credits_and_attribution.button.credits");

    private static final Component ATTRIBUTION_BUTTON = Component.translatable("credits_and_attribution.button.attribution");

    private static final Component LICENSES_BUTTON = Component.translatable("credits_and_attribution.button.licenses");

    private final Screen lastScreen;

    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);

    public CreditsAndAttributionScreen(Screen screen0) {
        super(TITLE);
        this.lastScreen = screen0;
    }

    @Override
    protected void init() {
        this.layout.addToHeader(new StringWidget(this.m_96636_(), this.f_96547_));
        GridLayout $$0 = this.layout.addToContents(new GridLayout()).spacing(8);
        $$0.defaultCellSetting().alignHorizontallyCenter();
        GridLayout.RowHelper $$1 = $$0.createRowHelper(1);
        $$1.addChild(Button.builder(CREDITS_BUTTON, p_276287_ -> this.openCreditsScreen()).width(210).build());
        $$1.addChild(Button.builder(ATTRIBUTION_BUTTON, ConfirmLinkScreen.confirmLink("https://aka.ms/MinecraftJavaAttribution", this, true)).width(210).build());
        $$1.addChild(Button.builder(LICENSES_BUTTON, ConfirmLinkScreen.confirmLink("https://aka.ms/MinecraftJavaLicenses", this, true)).width(210).build());
        this.layout.addToFooter(Button.builder(CommonComponents.GUI_DONE, p_276311_ -> this.onClose()).build());
        this.layout.arrangeElements();
        this.layout.m_264134_(this::m_142416_);
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
    }

    private void openCreditsScreen() {
        this.f_96541_.setScreen(new WinScreen(false, () -> this.f_96541_.setScreen(this)));
    }

    @Override
    public void onClose() {
        this.f_96541_.setScreen(this.lastScreen);
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        super.render(guiGraphics0, int1, int2, float3);
    }
}