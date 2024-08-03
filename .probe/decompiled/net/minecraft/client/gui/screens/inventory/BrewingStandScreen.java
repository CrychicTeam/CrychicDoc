package net.minecraft.client.gui.screens.inventory;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.BrewingStandMenu;

public class BrewingStandScreen extends AbstractContainerScreen<BrewingStandMenu> {

    private static final ResourceLocation BREWING_STAND_LOCATION = new ResourceLocation("textures/gui/container/brewing_stand.png");

    private static final int[] BUBBLELENGTHS = new int[] { 29, 24, 20, 16, 11, 6, 0 };

    public BrewingStandScreen(BrewingStandMenu brewingStandMenu0, Inventory inventory1, Component component2) {
        super(brewingStandMenu0, inventory1, component2);
    }

    @Override
    protected void init() {
        super.init();
        this.f_97728_ = (this.f_97726_ - this.f_96547_.width(this.f_96539_)) / 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        super.render(guiGraphics0, int1, int2, float3);
        this.m_280072_(guiGraphics0, int1, int2);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics0, float float1, int int2, int int3) {
        int $$4 = (this.f_96543_ - this.f_97726_) / 2;
        int $$5 = (this.f_96544_ - this.f_97727_) / 2;
        guiGraphics0.blit(BREWING_STAND_LOCATION, $$4, $$5, 0, 0, this.f_97726_, this.f_97727_);
        int $$6 = ((BrewingStandMenu) this.f_97732_).getFuel();
        int $$7 = Mth.clamp((18 * $$6 + 20 - 1) / 20, 0, 18);
        if ($$7 > 0) {
            guiGraphics0.blit(BREWING_STAND_LOCATION, $$4 + 60, $$5 + 44, 176, 29, $$7, 4);
        }
        int $$8 = ((BrewingStandMenu) this.f_97732_).getBrewingTicks();
        if ($$8 > 0) {
            int $$9 = (int) (28.0F * (1.0F - (float) $$8 / 400.0F));
            if ($$9 > 0) {
                guiGraphics0.blit(BREWING_STAND_LOCATION, $$4 + 97, $$5 + 16, 176, 0, 9, $$9);
            }
            $$9 = BUBBLELENGTHS[$$8 / 2 % 7];
            if ($$9 > 0) {
                guiGraphics0.blit(BREWING_STAND_LOCATION, $$4 + 63, $$5 + 14 + 29 - $$9, 185, 29 - $$9, 12, $$9);
            }
        }
    }
}