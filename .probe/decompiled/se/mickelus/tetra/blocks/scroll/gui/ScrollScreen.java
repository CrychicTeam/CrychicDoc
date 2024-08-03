package se.mickelus.tetra.blocks.scroll.gui;

import java.util.List;
import java.util.Optional;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiText;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.ConfigHandler;

@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public class ScrollScreen extends Screen {

    private static int currentPage;

    private final String[] pages;

    private final GuiElement gui;

    private final GuiText text;

    public ScrollScreen(String key) {
        super(Component.literal("tetra:scroll"));
        this.pages = I18n.get("item.tetra.scroll." + key + ".details").split("\r");
        this.f_96543_ = 320;
        this.f_96544_ = 240;
        this.gui = new GuiElement(0, 0, this.f_96543_, this.f_96544_);
        this.gui.addChild(new GuiTexture(0, 0, 160, 186, new ResourceLocation("tetra", "textures/gui/pamphlet.png")).setAttachment(GuiAttachment.middleCenter));
        this.text = new GuiText(2, -75, 124, "");
        this.text.setAttachmentAnchor(GuiAttachment.middleCenter);
        this.text.setAttachmentPoint(GuiAttachment.topCenter);
        this.text.setColor(16766776);
        this.gui.addChild(this.text);
        this.gui.addChild(new ScrollPageButtonGui(70, -10, true, () -> this.changePage(currentPage - 1)).setAttachment(GuiAttachment.bottomLeft));
        this.gui.addChild(new ScrollPageButtonGui(-70, -10, false, () -> this.changePage(currentPage + 1)).setAttachment(GuiAttachment.bottomRight));
        this.changePage(currentPage);
    }

    private void changePage(int index) {
        currentPage = Mth.clamp(index, 0, this.pages.length - 1);
        this.text.setString(this.pages[currentPage]);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.gui.updateFocusState((this.f_96543_ - this.gui.getWidth()) / 2, (this.f_96544_ - this.gui.getHeight()) / 2, mouseX, mouseY);
        this.gui.draw(graphics, (this.f_96543_ - this.gui.getWidth()) / 2, (this.f_96544_ - this.gui.getHeight()) / 2, this.f_96543_, this.f_96544_, mouseX, mouseY, 1.0F);
        this.renderHoveredToolTip(graphics, mouseX, mouseY);
    }

    protected void renderHoveredToolTip(GuiGraphics graphics, int mouseX, int mouseY) {
        List<Component> tooltipLines = this.gui.getTooltipLines();
        if (tooltipLines != null) {
            graphics.renderTooltip(this.f_96547_, tooltipLines, Optional.empty(), mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        this.gui.onMouseClick((int) x, (int) y, button);
        return super.m_6375_(x, y, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double distance) {
        return this.gui.onMouseScroll(mouseX, mouseY, distance);
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        if (ConfigHandler.development.get()) {
            switch(typedChar) {
                case 'a':
                    this.changePage(currentPage - 1);
                    break;
                case 'd':
                    this.changePage(currentPage + 1);
            }
        }
        return false;
    }
}