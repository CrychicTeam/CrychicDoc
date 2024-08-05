package net.minecraft.client.gui.screens;

import com.mojang.text2speech.Narrator;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.AccessibilityOnboardingTextWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CommonButtons;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class AccessibilityOnboardingScreen extends Screen {

    private static final Component ONBOARDING_NARRATOR_MESSAGE = Component.translatable("accessibility.onboarding.screen.narrator");

    private static final int PADDING = 4;

    private static final int TITLE_PADDING = 16;

    private final PanoramaRenderer panorama = new PanoramaRenderer(TitleScreen.CUBE_MAP);

    private final LogoRenderer logoRenderer;

    private final Options options;

    private final boolean narratorAvailable;

    private boolean hasNarrated;

    private float timer;

    @Nullable
    private AccessibilityOnboardingTextWidget textWidget;

    public AccessibilityOnboardingScreen(Options options0) {
        super(Component.translatable("accessibility.onboarding.screen.title"));
        this.options = options0;
        this.logoRenderer = new LogoRenderer(true);
        this.narratorAvailable = Minecraft.getInstance().getNarrator().isActive();
    }

    @Override
    public void init() {
        int $$0 = this.initTitleYPos();
        FrameLayout $$1 = new FrameLayout(this.f_96543_, this.f_96544_ - $$0);
        $$1.defaultChildLayoutSetting().alignVerticallyTop().padding(4);
        GridLayout $$2 = $$1.addChild(new GridLayout());
        $$2.defaultCellSetting().alignHorizontallyCenter().padding(4);
        GridLayout.RowHelper $$3 = $$2.createRowHelper(1);
        $$3.defaultCellSetting().padding(2);
        this.textWidget = new AccessibilityOnboardingTextWidget(this.f_96547_, this.f_96539_, this.f_96543_);
        $$3.addChild(this.textWidget, $$3.newCellSettings().paddingBottom(16));
        AbstractWidget $$4 = this.options.narrator().createButton(this.options, 0, 0, 150);
        $$4.active = this.narratorAvailable;
        $$3.addChild($$4);
        if (this.narratorAvailable) {
            this.m_264313_($$4);
        }
        $$3.addChild(CommonButtons.accessibilityTextAndImage(p_280782_ -> this.closeAndSetScreen(new AccessibilityOptionsScreen(this, this.f_96541_.options))));
        $$3.addChild(CommonButtons.languageTextAndImage(p_280781_ -> this.closeAndSetScreen(new LanguageSelectScreen(this, this.f_96541_.options, this.f_96541_.getLanguageManager()))));
        $$1.addChild(Button.builder(CommonComponents.GUI_CONTINUE, p_267841_ -> this.onClose()).build(), $$1.newChildLayoutSettings().alignVerticallyBottom().padding(8));
        $$1.arrangeElements();
        FrameLayout.alignInRectangle($$1, 0, $$0, this.f_96543_, this.f_96544_, 0.5F, 0.0F);
        $$1.m_264134_(this::m_142416_);
    }

    private int initTitleYPos() {
        return 90;
    }

    @Override
    public void onClose() {
        this.closeAndSetScreen(new TitleScreen(true, this.logoRenderer));
    }

    private void closeAndSetScreen(Screen screen0) {
        this.options.onboardAccessibility = false;
        this.options.save();
        Narrator.getNarrator().clear();
        this.f_96541_.setScreen(screen0);
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.handleInitialNarrationDelay();
        this.panorama.render(0.0F, 1.0F);
        guiGraphics0.fill(0, 0, this.f_96543_, this.f_96544_, -1877995504);
        this.logoRenderer.renderLogo(guiGraphics0, this.f_96543_, 1.0F);
        if (this.textWidget != null) {
            this.textWidget.m_88315_(guiGraphics0, int1, int2, float3);
        }
        super.render(guiGraphics0, int1, int2, float3);
    }

    private void handleInitialNarrationDelay() {
        if (!this.hasNarrated && this.narratorAvailable) {
            if (this.timer < 40.0F) {
                this.timer++;
            } else if (this.f_96541_.isWindowActive()) {
                Narrator.getNarrator().say(ONBOARDING_NARRATOR_MESSAGE.getString(), true);
                this.hasNarrated = true;
            }
        }
    }
}