package net.minecraft.client.gui.screens;

import net.minecraft.Util;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class DemoIntroScreen extends Screen {

    private static final ResourceLocation DEMO_BACKGROUND_LOCATION = new ResourceLocation("textures/gui/demo_background.png");

    private MultiLineLabel movementMessage = MultiLineLabel.EMPTY;

    private MultiLineLabel durationMessage = MultiLineLabel.EMPTY;

    public DemoIntroScreen() {
        super(Component.translatable("demo.help.title"));
    }

    @Override
    protected void init() {
        int $$0 = -16;
        this.m_142416_(Button.builder(Component.translatable("demo.help.buy"), p_280797_ -> {
            p_280797_.f_93623_ = false;
            Util.getPlatform().openUri("https://aka.ms/BuyMinecraftJava");
        }).bounds(this.f_96543_ / 2 - 116, this.f_96544_ / 2 + 62 + -16, 114, 20).build());
        this.m_142416_(Button.builder(Component.translatable("demo.help.later"), p_280798_ -> {
            this.f_96541_.setScreen(null);
            this.f_96541_.mouseHandler.grabMouse();
        }).bounds(this.f_96543_ / 2 + 2, this.f_96544_ / 2 + 62 + -16, 114, 20).build());
        Options $$1 = this.f_96541_.options;
        this.movementMessage = MultiLineLabel.create(this.f_96547_, Component.translatable("demo.help.movementShort", $$1.keyUp.getTranslatedKeyMessage(), $$1.keyLeft.getTranslatedKeyMessage(), $$1.keyDown.getTranslatedKeyMessage(), $$1.keyRight.getTranslatedKeyMessage()), Component.translatable("demo.help.movementMouse"), Component.translatable("demo.help.jump", $$1.keyJump.getTranslatedKeyMessage()), Component.translatable("demo.help.inventory", $$1.keyInventory.getTranslatedKeyMessage()));
        this.durationMessage = MultiLineLabel.create(this.f_96547_, Component.translatable("demo.help.fullWrapped"), 218);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics0) {
        super.renderBackground(guiGraphics0);
        int $$1 = (this.f_96543_ - 248) / 2;
        int $$2 = (this.f_96544_ - 166) / 2;
        guiGraphics0.blit(DEMO_BACKGROUND_LOCATION, $$1, $$2, 0, 0, 248, 166);
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.renderBackground(guiGraphics0);
        int $$4 = (this.f_96543_ - 248) / 2 + 10;
        int $$5 = (this.f_96544_ - 166) / 2 + 8;
        guiGraphics0.drawString(this.f_96547_, this.f_96539_, $$4, $$5, 2039583, false);
        $$5 = this.movementMessage.renderLeftAlignedNoShadow(guiGraphics0, $$4, $$5 + 12, 12, 5197647);
        this.durationMessage.renderLeftAlignedNoShadow(guiGraphics0, $$4, $$5 + 20, 9, 2039583);
        super.render(guiGraphics0, int1, int2, float3);
    }
}