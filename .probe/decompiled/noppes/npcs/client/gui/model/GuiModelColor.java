package noppes.npcs.client.gui.model;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import noppes.npcs.api.wrapper.gui.CustomGuiButtonWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiTextFieldWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiTexturedRectWrapper;
import noppes.npcs.client.gui.custom.GuiCustom;
import noppes.npcs.client.gui.custom.components.CustomGuiButton;
import noppes.npcs.client.gui.custom.components.CustomGuiTextField;
import noppes.npcs.client.gui.custom.components.CustomGuiTexturedRect;
import noppes.npcs.containers.ContainerCustomGui;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.ITextfieldListener;

public class GuiModelColor extends GuiCustom implements ITextfieldListener {

    private GuiCustom parent;

    private static final ResourceLocation colorPicker = new ResourceLocation("moreplayermodels:textures/gui/color.png");

    private static final ResourceLocation colorgui = new ResourceLocation("moreplayermodels:textures/gui/color_gui.png");

    private int colorX;

    private int colorY;

    public int color;

    private CustomGuiTextField textfield;

    private CustomGuiButton button;

    private GuiModelColor.ColorCallback callback;

    public GuiModelColor(GuiCustom parent, int c, GuiModelColor.ColorCallback callback) {
        super((ContainerCustomGui) parent.m_6262_(), parent.inv, Component.empty());
        this.parent = parent;
        this.callback = callback;
        this.f_97727_ = 170;
        this.f_97726_ = 130;
        this.color = c;
        CustomGuiTexturedRectWrapper bg = new CustomGuiTexturedRectWrapper();
        bg.setTexture("customnpcs:textures/gui/components.png").setSize(this.f_97726_, this.f_97727_);
        bg.setTextureOffset(0, 0).setRepeatingTexture(64, 64, 4);
        this.background = new CustomGuiTexturedRect(this, bg);
        this.textfield = new CustomGuiTextField(this, new CustomGuiTextFieldWrapper(24, 35, 25, 60, 20).setCharacterType(2).setColor(this.color).setText(this.getColor()).setOnChange((gui, text) -> {
            this.color = Integer.parseInt(text.getText(), 16);
            callback.color(this.color);
            this.textfield.m_94202_(this.color);
        }));
        this.button = new CustomGuiButton(this, (CustomGuiButtonWrapper) new CustomGuiButtonWrapper(66, "x", 107, 8, 20, 20).setOnPress((gui, button) -> parent.subgui = null).setDisablePackets());
        this.f_96541_ = Minecraft.getInstance();
    }

    @Override
    public void init() {
        super.init();
        this.add(this.textfield);
        this.add(this.button);
        this.background.setTexture(colorgui);
        this.colorX = this.f_97735_ + 4;
        this.colorY = this.f_97736_ + 50;
    }

    @Override
    public void render(GuiGraphics graphics, int par1, int limbSwingAmount, float par3) {
        super.render(graphics, par1, limbSwingAmount, par3);
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, colorPicker);
        graphics.blit(colorPicker, this.colorX, this.colorY, 0, 0, 120, 120);
    }

    @Override
    public boolean mouseClicked(double i, double j, int k) {
        super.mouseClicked(i, j, k);
        if (!(i < (double) this.colorX) && !(i > (double) (this.colorX + 120)) && !(j < (double) this.colorY) && !(j > (double) (this.colorY + 120))) {
            Resource resource = (Resource) this.f_96541_.getResourceManager().m_213713_(colorPicker).orElse(null);
            if (resource != null) {
                try {
                    InputStream stream = resource.open();
                    try {
                        BufferedImage bufferedimage = ImageIO.read(stream);
                        int color = bufferedimage.getRGB((int) (i - (double) this.f_97735_ - 4.0) * 4, (int) (j - (double) this.f_97736_ - 50.0) * 4) & 16777215;
                        if (color != 0) {
                            this.color = color;
                            this.callback.color(color);
                            this.textfield.m_94202_(color);
                            this.textfield.m_94144_(this.getColor());
                        }
                    } catch (Throwable var11) {
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (Throwable var10) {
                                var11.addSuppressed(var10);
                            }
                        }
                        throw var11;
                    }
                    if (stream != null) {
                        stream.close();
                    }
                } catch (IOException var12) {
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void unFocused(GuiTextFieldNop textfield) {
        try {
            this.color = Integer.parseInt(textfield.m_94155_(), 16);
        } catch (NumberFormatException var3) {
            this.color = 0;
        }
        this.callback.color(this.color);
        textfield.m_94202_(this.color);
    }

    public String getColor() {
        String str = Integer.toHexString(this.color);
        while (str.length() < 6) {
            str = "0" + str;
        }
        return str;
    }

    public interface ColorCallback {

        void color(int var1);
    }
}