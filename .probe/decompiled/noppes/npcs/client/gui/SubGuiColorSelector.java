package noppes.npcs.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import noppes.npcs.shared.client.gui.components.GuiBasic;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.ITextfieldListener;

public class SubGuiColorSelector extends GuiBasic implements ITextfieldListener {

    private static final ResourceLocation resource = new ResourceLocation("customnpcs:textures/gui/color.png");

    private int colorX;

    private int colorY;

    private GuiTextFieldNop textfield;

    public int color;

    public SubGuiColorSelector(int color) {
        this.imageWidth = 176;
        this.imageHeight = 222;
        this.color = color;
        this.setBackground("smallbg.png");
    }

    @Override
    public void init() {
        super.init();
        this.colorX = this.guiLeft + 30;
        this.colorY = this.guiTop + 50;
        this.addTextField(this.textfield = new GuiTextFieldNop(0, this, this.guiLeft + 53, this.guiTop + 20, 70, 20, this.getColor()));
        this.textfield.m_94202_(this.color);
        this.addButton(new GuiButtonNop(this, 66, this.guiLeft + 112, this.guiTop + 198, 60, 20, "gui.done"));
    }

    public String getColor() {
        String str = Integer.toHexString(this.color);
        while (str.length() < 6) {
            str = "0" + str;
        }
        return str;
    }

    @Override
    public boolean charTyped(char c, int i) {
        String prev = this.textfield.m_94155_();
        super.charTyped(c, i);
        String newText = this.textfield.m_94155_();
        if (newText.equals(prev)) {
            return false;
        } else {
            try {
                this.color = Integer.parseInt(this.textfield.m_94155_(), 16);
                this.textfield.m_94202_(this.color);
            } catch (NumberFormatException var6) {
                this.textfield.m_94144_(prev);
            }
            return true;
        }
    }

    @Override
    public void buttonEvent(GuiButtonNop btn) {
        if (btn.id == 66) {
            this.close();
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, resource);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        graphics.blit(resource, this.colorX, this.colorY, 0, 0, 120, 120);
    }

    @Override
    public boolean mouseClicked(double i, double j, int k) {
        super.mouseClicked(i, j, k);
        if (!(i < (double) this.colorX) && !(i > (double) (this.colorX + 117)) && !(j < (double) this.colorY) && !(j > (double) (this.colorY + 117))) {
            InputStream stream = null;
            Resource iresource = (Resource) this.f_96541_.getResourceManager().m_213713_(resource).orElse(null);
            if (iresource != null) {
                try {
                    BufferedImage bufferedimage = ImageIO.read(stream = iresource.open());
                    this.color = bufferedimage.getRGB((int) (i - (double) this.guiLeft - 30.0) * 4, (int) (j - (double) this.guiTop - 50.0) * 4) & 16777215;
                    this.textfield.m_94202_(this.color);
                    this.textfield.m_94144_(this.getColor());
                } catch (IOException var17) {
                } finally {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException var16) {
                        }
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void unFocused(GuiTextFieldNop textfield) {
        int color = 0;
        try {
            color = Integer.parseInt(textfield.m_94155_(), 16);
        } catch (NumberFormatException var4) {
            color = 0;
        }
        this.color = color;
        textfield.m_94202_(color);
    }
}