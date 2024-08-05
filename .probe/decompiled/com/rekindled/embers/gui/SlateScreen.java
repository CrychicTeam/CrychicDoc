package com.rekindled.embers.gui;

import com.rekindled.embers.item.AlchemyHintItem;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SlateScreen extends AbstractContainerScreen<SlateMenu> {

    ResourceLocation inventory = new ResourceLocation("embers", "textures/gui/inventory_gui.png");

    ResourceLocation slate = new ResourceLocation("embers", "textures/gui/codebreaking_slate_gui.png");

    public SlateScreen(SlateMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.f_97727_ = SlateMenu.invHeight + SlateMenu.slateHeight;
        this.f_97726_ = SlateMenu.invWidth;
        this.f_97731_ = this.f_97727_ - 94;
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.m_280273_(graphics);
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
        this.m_280072_(graphics, pMouseX, pMouseY);
    }

    @Override
    public void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int invX = (this.f_96543_ - this.f_97726_) / 2;
        int x = (this.f_96543_ - SlateMenu.slateWidth) / 2;
        int y = (this.f_96544_ - this.f_97727_) / 2;
        graphics.blit(this.inventory, invX, y + SlateMenu.slateHeight, 0, 0, SlateMenu.invWidth, SlateMenu.invHeight);
        graphics.blit(this.slate, x, y, 0, 0, SlateMenu.slateWidth, SlateMenu.slateHeight);
        ItemStack baseStack = ((SlateMenu) this.f_97732_).m_38853_(0).getItem();
        ArrayList<ItemStack> inputs = AlchemyHintItem.getInputs(baseStack);
        for (int j = 0; j < inputs.size(); j++) {
            int slotX = x + SlateMenu.slateMargin + j * SlateMenu.layerWidth / inputs.size() + SlateMenu.layerWidth / (2 * inputs.size()) - 12;
            graphics.blit(this.slate, slotX, y + SlateMenu.slateMargin, 192, 0, 24, 24);
            graphics.renderFakeItem((ItemStack) inputs.get(j), slotX + 4, y + SlateMenu.slateMargin + 4);
        }
        for (int i = 0; i < SlateMenu.slotCount; i++) {
            int height = y + SlateMenu.slateMargin + i * SlateMenu.layerHeight + SlateMenu.layerHeight / 2 + 13;
            graphics.blit(this.slate, x - 30, height, 192, 24, 26, 26);
            if (!baseStack.isEmpty()) {
                ItemStack stack = ((SlateMenu) this.f_97732_).m_38853_(i).getItem();
                ArrayList<ItemStack> aspects = AlchemyHintItem.getAspects(stack);
                int blackPins = AlchemyHintItem.getBlackPins(stack);
                int whitePins = AlchemyHintItem.getWhitePins(stack);
                for (int j = 0; j < inputs.size(); j++) {
                    int slotX = x + SlateMenu.slateMargin + j * SlateMenu.layerWidth / inputs.size() + SlateMenu.layerWidth / (2 * inputs.size()) - 10;
                    graphics.blit(this.slate, slotX, height + 2, 218, 20, 20, 24);
                    if (j < aspects.size()) {
                        graphics.renderFakeItem((ItemStack) aspects.get(j), slotX + 2, height + 5);
                    }
                }
                int hintX = x + 155;
                int hintY = height + 3;
                graphics.blit(this.slate, hintX, hintY, 216, 0, 19, 19);
                if (inputs.size() <= 4) {
                    for (int h = 0; h < 2; h++) {
                        for (int v = 0; v < 2; v++) {
                            if (blackPins > 0) {
                                graphics.blit(this.slate, hintX + 3 + v * 8, hintY + 3 + h * 8, 198, 50, 6, 6);
                                blackPins--;
                            } else if (whitePins > 0) {
                                graphics.blit(this.slate, hintX + 3 + v * 8, hintY + 3 + h * 8, 204, 50, 6, 6);
                                whitePins--;
                            } else {
                                graphics.blit(this.slate, hintX + 3 + v * 8, hintY + 3 + h * 8, 192, 50, 6, 6);
                            }
                        }
                    }
                } else {
                    for (int h = 0; h < 3; h++) {
                        for (int vx = 0; vx < 3; vx++) {
                            if (blackPins > 0) {
                                graphics.blit(this.slate, hintX + 1 + vx * 6, hintY + 1 + h * 6, 198, 50, 6, 6);
                                blackPins--;
                            } else if (whitePins > 0) {
                                graphics.blit(this.slate, hintX + 1 + vx * 6, hintY + 1 + h * 6, 204, 50, 6, 6);
                                whitePins--;
                            } else {
                                graphics.blit(this.slate, hintX + 1 + vx * 6, hintY + 1 + h * 6, 192, 50, 6, 6);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.f_96547_, this.f_169604_, this.f_97730_, this.f_97731_, 4210752, false);
    }
}