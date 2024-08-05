package noppes.npcs.client.gui.player.tabs;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import noppes.npcs.client.gui.player.GuiFaction;

public class InventoryTabFactions extends AbstractTab {

    private Component displayString = Component.translatable("menu.factions");

    public InventoryTabFactions() {
        super(1, 0, 0, new ItemStack(Items.RED_BANNER, 1));
    }

    @Override
    public void onTabClicked() {
        Minecraft mc = Minecraft.getInstance();
        mc.setScreen(new GuiFaction());
    }

    @Override
    public boolean shouldAddToList() {
        return true;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        if (this.f_93624_) {
            Minecraft mc = Minecraft.getInstance();
            boolean hovered = mouseX >= this.m_252754_() && mouseY >= this.m_252907_() && mouseX < this.m_252754_() + this.f_93618_ && mouseY < this.m_252907_() + this.f_93619_;
            if (hovered) {
                graphics.pose().translate((float) mouseX, (float) (this.m_252907_() + 2), 0.0F);
                this.drawHoveringText(graphics, Arrays.asList(this.displayString), -mc.font.width(this.displayString), 0, mc.font);
                graphics.pose().translate((float) (-mouseX), (float) (-(this.m_252907_() + 2)), 0.0F);
            }
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput0) {
    }

    protected void drawHoveringText(GuiGraphics graphics, List<Component> list, int x, int y, Font font) {
        if (!list.isEmpty()) {
            RenderSystem.disableDepthTest();
            int k = 0;
            for (Component o : list) {
                int l = font.width(o);
                if (l > k) {
                    k = l;
                }
            }
            int j2 = x;
            int k2 = y;
            int i1 = 8;
            if (list.size() > 1) {
                i1 += 2 + (list.size() - 1) * 10;
            }
            graphics.pose().pushPose();
            graphics.pose().translate(0.0F, 0.0F, 300.0F);
            int j1 = -267386864;
            graphics.fillGradient(x - 3, y - 4, x + k + 3, y - 3, j1, j1);
            graphics.fillGradient(x - 3, y + i1 + 3, x + k + 3, y + i1 + 4, j1, j1);
            graphics.fillGradient(x - 3, y - 3, x + k + 3, y + i1 + 3, j1, j1);
            graphics.fillGradient(x - 4, y - 3, x - 3, y + i1 + 3, j1, j1);
            graphics.fillGradient(x + k + 3, y - 3, x + k + 4, y + i1 + 3, j1, j1);
            int k1 = 1347420415;
            int l1 = (k1 & 16711422) >> 1 | k1 & 0xFF000000;
            graphics.fillGradient(x - 3, y - 3 + 1, x - 3 + 1, y + i1 + 3 - 1, k1, l1);
            graphics.fillGradient(x + k + 2, y - 3 + 1, x + k + 3, y + i1 + 3 - 1, k1, l1);
            graphics.fillGradient(x - 3, y - 3, x + k + 3, y - 3 + 1, k1, k1);
            graphics.fillGradient(x - 3, y + i1 + 2, x + k + 3, y + i1 + 3, l1, l1);
            for (int i2 = 0; i2 < list.size(); i2++) {
                Component s1 = (Component) list.get(i2);
                graphics.drawString(font, s1, j2, k2, -1);
                if (i2 == 0) {
                    k2 += 2;
                }
                k2 += 10;
            }
            graphics.pose().popPose();
            RenderSystem.enableDepthTest();
        }
    }
}