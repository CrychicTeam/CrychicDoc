package net.minecraft.client.gui.screens;

import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.PlayerModelPart;

public class SkinCustomizationScreen extends OptionsSubScreen {

    public SkinCustomizationScreen(Screen screen0, Options options1) {
        super(screen0, options1, Component.translatable("options.skinCustomisation.title"));
    }

    @Override
    protected void init() {
        int $$0 = 0;
        for (PlayerModelPart $$1 : PlayerModelPart.values()) {
            this.m_142416_(CycleButton.onOffBuilder(this.f_96282_.isModelPartEnabled($$1)).create(this.f_96543_ / 2 - 155 + $$0 % 2 * 160, this.f_96544_ / 6 + 24 * ($$0 >> 1), 150, 20, $$1.getName(), (p_169436_, p_169437_) -> this.f_96282_.toggleModelPart($$1, p_169437_)));
            $$0++;
        }
        this.m_142416_(this.f_96282_.mainHand().createButton(this.f_96282_, this.f_96543_ / 2 - 155 + $$0 % 2 * 160, this.f_96544_ / 6 + 24 * ($$0 >> 1), 150));
        if (++$$0 % 2 == 1) {
            $$0++;
        }
        this.m_142416_(Button.builder(CommonComponents.GUI_DONE, p_280828_ -> this.f_96541_.setScreen(this.f_96281_)).bounds(this.f_96543_ / 2 - 100, this.f_96544_ / 6 + 24 * ($$0 >> 1), 200, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 20, 16777215);
        super.m_88315_(guiGraphics0, int1, int2, float3);
    }
}